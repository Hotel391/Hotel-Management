package controllers.common;

import java.io.IOException;
import java.util.List;
import java.util.function.Function;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import models.EmailVerificationToken;
import services.IRegisterService;
import services.RegisterService;
import utility.Email;
import utility.Validation;
import utility.ValidationRule;

/**
 *
 * @author HieuTT
 */
public class ForgotPassword extends HttpServlet {

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
        Email emailService = new Email();
        token.setToken(emailService.generateToken());
        token.setExpiryDate(emailService.expireDateTime());
        service.registerToken(token);
        String linkConfirm = request.getScheme() + "://"
                + request.getServerName() + ":"
                + request.getServerPort()
                + request.getContextPath() + "/confirmResetPassword?token=" + token.getToken();
        emailService.sendEmail(email, email, linkConfirm, "reset");

        response.sendRedirect("verifyEmail?email=" + email + "&type=reset");
    }

    private boolean validateEmail(HttpServletRequest request, IRegisterService service, String input) {
        return Validation.validateField(request,
                "errorEmail",
                "Email",
                input,
                Function.identity(),
                "EMAIL",
                List.of(new ValidationRule<>(value -> !service.isEmailExists(value),
                                "Email already exists")));
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
