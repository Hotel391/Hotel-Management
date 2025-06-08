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

/**
 *
 * @author HieuTT
 */
public class ConfirmResetPassword extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String token = request.getParameter("token");
        if (token == null || token.isEmpty()) {
            response.sendRedirect("login");
            return;
        }

        RegisterService service = new RegisterService();
        EmailVerificationToken tokenObject = service.getTokenByToken(token);
        if (tokenObject == null) {
            response.sendRedirect("login");
            return;
        }

        if (Email.isExpireTime(tokenObject.getExpiryDate().toLocalDateTime())) {
            request.setAttribute("success", "false");
        } else {
            request.setAttribute("email", tokenObject.getEmail());
            request.setAttribute("success", "true");
            request.setAttribute("tokenId", tokenObject.getTokenId());
        }

        request.getRequestDispatcher("View/ConfirmResetPassword.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("success", "true");
        String email = request.getParameter("email");
        request.setAttribute("email", email);
        String tokenId=request.getParameter("tokenId");
        request.setAttribute("tokenId", tokenId);

        //verify Password
        String password = request.getParameter("password");
        boolean errorPassword = Validation.validateField(request, "errorPassword", password, "PASSWORD", "Password",
                "Password must contains 8 characters with lower, upper, special and digit");
        if (!errorPassword) {
            request.removeAttribute("errorPassword");
        }

        //verify Confirm Password
        String confirmPassword = request.getParameter("confirmPassword");
        boolean errorConfirmPassword = isErrorConfirmPassword(request, confirmPassword, password);
        if (!errorConfirmPassword) {
            request.removeAttribute("errorConfirmPassword");
        }

        if (errorPassword || errorConfirmPassword) {
            request.getRequestDispatcher("View/ConfirmResetPassword.jsp").forward(request, response);
            return;
        }
        password=Encryption.toSHA256(password);
        dal.CustomerAccountDAO.getInstance().resetPasswrod(email, password);
        
        RegisterService service=new RegisterService();
        
        service.deleteConfirmedToken(Integer.parseInt(tokenId));
        
        response.sendRedirect("login");
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
    }// </editor-fold>

}
