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
        request.setAttribute("type", type);
        String email = request.getParameter("email");
        List<String> method = new ArrayList<>();
        method.add("register");
        method.add("reset");

        if (email == null || email.isEmpty()) {
            response.sendRedirect(method.get(0));
            return;
        }

        request.setAttribute("email", email);
        RegisterService service = new RegisterService();
        EmailVerificationToken token = service.getTokenByEmail(email);

        if (token == null && !"reset".equals(type)) {
            response.sendRedirect(method.get(0));
            return;
        } else if (token == null) {
            response.sendRedirect("login");
            return;
        }

        if (method.get(1).equals(type) && !service.isEmailExists(email)) {
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
            
            resendEmail(request, token, method, email, linkRaw, emailService);
        }

        request.getRequestDispatcher("View/VerifyEmail.jsp").forward(request, response);
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
