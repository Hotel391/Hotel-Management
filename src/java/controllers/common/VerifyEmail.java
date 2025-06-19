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
    private static final String EMAIL_FIELD="email";
    private static final String REGISTER_URL="register";
    private static final String RESET_STATUS="reset";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String type = request.getParameter("type");
        String email = request.getParameter(EMAIL_FIELD);

        if (email == null || email.isEmpty()) {
            response.sendRedirect(REGISTER_URL);
            return;
        }

        request.setAttribute("type", type);
        request.setAttribute(EMAIL_FIELD, email);
        request.getRequestDispatcher("View/VerifyEmail.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String type = request.getParameter("type");
        String email = request.getParameter(EMAIL_FIELD);
        String resend = request.getParameter("resend");
        //check valid resend
        if (!"true".equals(resend) || email == null || email.isEmpty()) {
            response.sendRedirect("verifyEmail?email=" + email + "&type=" + type);
            return;
        }
        //initial service and token
        RegisterService service = new RegisterService();
        EmailVerificationToken token = service.getTokenByEmail(email);
        //check token
        if (token == null && !RESET_STATUS.equals(type)) {
            response.sendRedirect(REGISTER_URL);
            return;
        } else if (token == null) {
            response.sendRedirect("login");
            return;
        }
        //check reset password and exist email
        if (RESET_STATUS.equals(type) && !service.isEmailExists(email)) {
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

        resendEmail(request, token, List.of(REGISTER_URL, RESET_STATUS), email, linkRaw, emailService);

        response.sendRedirect("verifyEmail?email=" + email + "&type=" + type + "&resend=true");
    }

    private void resendEmail(HttpServletRequest request, EmailVerificationToken token,
            List<String> method, String email, String linkRaw, Email emailService) {
        //reset password
        String linkConfirm;
        String type;
        if (!method.get(1).equals(request.getParameter("type"))) {
            linkConfirm = linkRaw + "/confirmEmail?token=" + token.getToken();
            type= method.get(0);
        } else{
            linkConfirm=linkRaw + "/confirmResetPassword?token=" + token.getToken();
            type= method.get(1);
        }
        //register account
        emailService.sendEmail(email, token.getUsername(), linkConfirm, type);

    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
