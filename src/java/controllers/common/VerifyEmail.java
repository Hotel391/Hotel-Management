package controllers.common;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
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

        request.setAttribute("type", type);
        request.setAttribute("email", email);
        request.getRequestDispatcher("View/VerifyEmail.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String type = request.getParameter("type");
        String email = request.getParameter("email");
        String resend = request.getParameter("resend");

        // Nếu không phải resend, chuyển về GET logic bình thường
        if (!"true".equals(resend) || email == null || email.isEmpty()) {
            response.sendRedirect("verifyEmail?email=" + email + "&type=" + type);
            return;
        }

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

        resendEmail(request, token, List.of("register", "reset"), email, linkRaw, emailService);

        // ✅ Sau gửi xong, redirect về GET trang để JS cooldown hoạt động
        response.sendRedirect("verifyEmail?email=" + email + "&type=" + type + "&resend=true");
    }

    private void resendEmail(HttpServletRequest request, EmailVerificationToken token,
            List<String> method, String email, String linkRaw, Email emailService) {
        //reset password
        if (!method.get(1).equals(request.getParameter("type"))) {
            String linkConfirm = linkRaw + "/confirmEmail?token=" + token.getToken();
            emailService.sendEmail(email, token.getUsername(), linkConfirm, method.get(0));
            return;
        }
        //register account
        String linkConfirm = linkRaw + "/confirmResetPassword?token=" + token.getToken();
        emailService.sendEmail(email, email, linkConfirm, method.get(1));

    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
