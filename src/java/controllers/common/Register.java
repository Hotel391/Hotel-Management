package controllers.common;

import java.io.IOException;
import java.util.List;
import java.util.function.Function;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import models.EmailVerificationToken;
import services.IRegisterService;
import services.RegisterService;
import utility.EmailService;
import utility.Encryption;
import utility.Validation;
import utility.ValidationRule;

public class Register extends HttpServlet {
    private static final ExecutorService emailExecutor = Executors.newFixedThreadPool(5);

    private static final String FULLNAME_FIELD = "Fullname";
    private static final String EMAIL_FIELD = "Email";
    private static final String USERNAME_FIELD = "Username";
    private static final String PASSWORD_FIELD = "Password";
    private static final String CONFIRM_PASSWORD_FIELD = "confirmPassword";
    private static final String ERROR_CONFIRM_FIELD = "errorConfirmPassword";
    private static final String VIEW_REGISTER = "View/Register.jsp";
    private static final String REGISTER_SUBJECT = "register";
    private static final String VERIFY_EMAIL_URL = "verifyEmail?email=";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher(VIEW_REGISTER).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        IRegisterService service = new RegisterService();

        // verify fullname
        String fullname = request.getParameter(FULLNAME_FIELD.toLowerCase());
        boolean errorFullname = validateFullname(request, fullname);

        // verify email
        String email = request.getParameter(EMAIL_FIELD.toLowerCase());
        boolean errorEmail = validateEmail(request, service, email);

        // verify Username
        String username = request.getParameter(USERNAME_FIELD.toLowerCase());
        boolean errorUsername = validateUsername(request, service, username);

        // verify password
        String password = request.getParameter(PASSWORD_FIELD.toLowerCase());
        boolean errorPassword = validatePassword(request, password);

        // verify confirmPassword match password
        String confirmPassword = request.getParameter(CONFIRM_PASSWORD_FIELD);
        boolean errorConfirmPassword = isErrorConfirmPassword(request, confirmPassword, password);

        boolean gender = "0".equals(request.getParameter("gender"));

        boolean hasErrorAuthentication = errorEmail || errorUsername || errorPassword;

        if (hasErrorAuthentication || errorFullname || errorConfirmPassword) {
            request.getRequestDispatcher(VIEW_REGISTER).forward(request, response);
            return;
        }

        password = Encryption.toSHA256(password);

        EmailVerificationToken token = new EmailVerificationToken();
        service.deleteTokenByEmail(email);
        token.setEmail(email);
        token.setFullname(fullname);
        token.setGender(gender);
        token.setPassword(password);
        token.setUsername(username);

        EmailService emailService = new EmailService();
        token.setToken(emailService.generateToken());
        token.setExpiryDate(emailService.expireDateTime());
        service.registerToken(token);

        String linkConfirm = request.getScheme() + "://"
                + request.getServerName() + ":"
                + request.getServerPort()
                + request.getContextPath() + "/confirmEmail?token=" + token.getToken();
        Map<String,Object> data=new HashMap<>();
        data.put("username", username);
        data.put("confirmLink",linkConfirm);
        emailExecutor.submit(() 
                -> emailService.sendEmail(email, "Register Account", REGISTER_SUBJECT, data));

        response.sendRedirect(VERIFY_EMAIL_URL + email);
    }

    private boolean validateFullname(HttpServletRequest request, String input) {
        return Validation.validateField(request,
                "errorFullname",
                FULLNAME_FIELD,
                input,
                Function.identity(),
                "FULLNAME");
    }

    private boolean validateEmail(HttpServletRequest request, IRegisterService service, String input) {
        return Validation.validateField(request,
                "errorEmail",
                EMAIL_FIELD,
                input,
                Function.identity(),
                "EMAIL",
                List.of(new ValidationRule<>(value -> !service.isEmailExists(value),
                                "Email already exists")));
    }

    private boolean validateUsername(HttpServletRequest request, IRegisterService service, String input) {
        return Validation.validateField(request,
                "errorUsername",
                USERNAME_FIELD,
                input,
                Function.identity(),
                "USERNAME",
                List.of(new ValidationRule<>(value -> !service.isUsernameExists(value),
                                "Username already exists")));
    }

    private boolean validatePassword(HttpServletRequest request, String input) {
        return Validation.validateField(request,
                "errorPassword",
                PASSWORD_FIELD,
                input,
                Function.identity(),
                "PASSWORD");
    }

    private boolean isErrorConfirmPassword(HttpServletRequest request, String confirmPassword, String password) {
        boolean errorConfirmPassword = false;
        if (confirmPassword == null || confirmPassword.trim().isEmpty()) {
            request.setAttribute(ERROR_CONFIRM_FIELD, "Confirm password is required");
            errorConfirmPassword = true;
        } else if (!confirmPassword.equals(password)) {
            request.setAttribute(ERROR_CONFIRM_FIELD, "Confirm Password do not match Password");
            errorConfirmPassword = true;
        }
        if (!errorConfirmPassword) {
            request.removeAttribute(ERROR_CONFIRM_FIELD);
        }
        return errorConfirmPassword;
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }
}
