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
import utility.Validation;
import utility.ValidationRule;

/**
 *
 * @author HieuTT
 */
public class ForgotPassword extends HttpServlet {
    private static final ExecutorService emailExecutor = Executors.newFixedThreadPool(5);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("View/EnterEmailForgotPassword.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String email = request.getParameter("email");
        RegisterService service = new RegisterService();
        if (validateEmail(request, service, email)) {
            request.getRequestDispatcher("View/EnterEmailForgotPassword.jsp").forward(request, response);
            return;
        }

        EmailVerificationToken token = new EmailVerificationToken();
        service.deleteTokenByEmail(email);
        var account=service.getAccountByEmail(email);
        token.setEmail(email);
        token.setUsername(account.getUsername());
        token.setPassword(account.getPassword());
        EmailService emailService = new EmailService();
        token.setToken(emailService.generateToken());
        token.setExpiryDate(emailService.expireDateTime());
        service.registerToken(token);
        String linkConfirm = request.getScheme() + "://"
                + request.getServerName() + ":"
                + request.getServerPort()
                + request.getContextPath() + "/confirmResetPassword?token=" + token.getToken();
        Map<String,Object> data=new HashMap<>();
        data.put("username", email);
        data.put("confirmLink",linkConfirm);
        emailExecutor.submit(() -> emailService.sendEmail(email, "Reset Password", "reset", data));

        response.sendRedirect("verifyEmail?email=" + email + "&type=reset");
    }

    private boolean validateEmail(HttpServletRequest request, IRegisterService service, String input) {
        return Validation.validateField(request,
                "errorEmail",
                "Email",
                input,
                Function.identity(),
                "EMAIL",
                List.of(new ValidationRule<>(value -> service.isEmailExists(value),
                                "Email didn't existed")));
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
