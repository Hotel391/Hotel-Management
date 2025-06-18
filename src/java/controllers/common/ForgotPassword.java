package controllers.common;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import models.EmailVerificationToken;
import services.RegisterService;
import utility.Email;

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
        if (!service.isEmailExists(email)) {
            request.setAttribute("errorEmail", "This email doesn't exist");
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

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
