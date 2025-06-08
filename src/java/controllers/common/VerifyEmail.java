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
 * @author TranTrungHieu
 */
public class VerifyEmail extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String type = request.getParameter("type");
        String email = request.getParameter("email");
        if (email == null || email.isEmpty()) {
            response.sendRedirect("register");
            return;
        }

        request.setAttribute("email", email);
        RegisterService service = new RegisterService();
        EmailVerificationToken token = service.getTokenByEmail(email);

        if (token == null && !"reset".equals(type)) {
            response.sendRedirect("register");
            return;
        } else if (token == null) {
            response.sendRedirect("login");
            return;
        }

        if ("reset".equals(type) && !service.isEmailExists(email)) {
            response.sendRedirect("login");
            return;
        }

        String resend = request.getParameter("resend");
        if ("true".equals(resend)) {
            Email emailService = new Email();
            if (Email.isExpireTime(token.getExpiryDate().toLocalDateTime())) {
                token.setExpiryDate(emailService.expireDateTime());
                token.setToken(emailService.generateToken());
                service.updateToken(token);
            }
            String linkRaw = request.getScheme() + "://"
                    + request.getServerName() + ":"
                    + request.getServerPort()
                    + request.getContextPath();
            if (!"reset".equals(request.getParameter("type"))) {
                String linkConfirm = linkRaw + "/confirmEmail?token=" + token.getToken();
                emailService.sendEmail(email, token.getUsername(), linkConfirm, "register");
            } else {
                String linkConfirm = linkRaw + "/confirmResetPassword?token=" + token.getToken();
                emailService.sendEmail(email, email, linkConfirm, "reset");
            }
        }

        request.getRequestDispatcher("View/VerifyEmail.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
