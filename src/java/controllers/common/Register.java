package controllers.common;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import models.EmailVerificationToken;
import services.RegisterService;
import utility.Email;
import utility.Encryption;
import utility.Validation;

public class Register extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("View/Register.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        RegisterService service = new RegisterService();

        // verify fullname
        String fullname = request.getParameter("fullname").trim();
        boolean errorFullname = fullname == null || fullname.isEmpty();
        if (errorFullname) {
            request.setAttribute("errorFullname", "Name is required");
        }
        if (!errorFullname) {
            request.removeAttribute("errorFullname");
        }

        // verify email
        String email = request.getParameter("email");
        boolean errorEmail = Validation.validateField(request, "errorEmail", email, "EMAIL", "Email",
                "Invalid email.");
        if (!errorEmail && service.isEmailExists(email)) {
            request.setAttribute("errorEmail", "Email is existed");
            errorEmail = true;
        }
        if (!errorEmail) {
            request.removeAttribute("errorEmail");
        }

        // verify Username
        String username = request.getParameter("username");
        boolean errorUsername = Validation.validateField(request, "errorUsername", username, "USERNAME", "Username",
                "Username just contain alphabet and number");
        if (!errorUsername && service.isUsernameExists(username)) {
            request.setAttribute("errorUsername", "Username is existed");
            errorUsername = true;
        }
        if (!errorUsername && Validation.checkFormatException(username, "EMOJI")) {
            request.setAttribute("errorUsername", "Username cannot contains emoji");
            errorUsername = true;
        }
        if (!errorUsername) {
            request.removeAttribute("errorUsername");
        }

        // verify password
        String password = request.getParameter("password");
        boolean errorPassword = Validation.validateField(request, "errorPassword", password, "PASSWORD", "Password",
                "Password must contains 8 characters with lower, upper, special and digit");
        if (!errorPassword) {
            request.removeAttribute("errorPassword");
        }

        // verify confirmPassword match password
        String confirmPassword = request.getParameter("confirmPassword");
        boolean errorConfirmPassword = isErrorConfirmPassword(request, confirmPassword, password);
        if (!errorConfirmPassword) {
            request.removeAttribute("errorConfirmPassword");
        }

        boolean gender = "0".equals(request.getParameter("gender"));
        if (errorFullname || errorEmail || errorUsername || errorPassword || errorConfirmPassword) {
            request.getRequestDispatcher("View/Register.jsp").forward(request, response);
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
        Email emailService = new Email();
        token.setToken(emailService.generateToken());
        token.setExpiryDate(emailService.expireDateTime());
        service.registerToken(token);
        String linkConfirm = request.getScheme() + "://"
                + request.getServerName() + ":"
                + request.getServerPort()
                + request.getContextPath()+"/confirmEmail?token="+token.getToken();
        emailService.sendEmail(email, username, linkConfirm,"register");
        
        response.sendRedirect("verifyEmail?email=" + email);
    }

    private boolean isErrorConfirmPassword(HttpServletRequest request, String confirmPassword, String password) {
        if (confirmPassword == null || confirmPassword.trim().isEmpty()) {
            request.setAttribute("errorConfirmPassword", "Confirm password is required");
            return true;
        }
        if (!confirmPassword.equals(password)) {
            request.setAttribute("errorConfirmPassword", "Confirm Password do not match Password");
            return true;
        }
        return false;
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }

}
