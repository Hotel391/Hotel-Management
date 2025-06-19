package controllers.common;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.function.Function;
import models.EmailVerificationToken;
import services.RegisterService;
import utility.Email;
import utility.Encryption;
import utility.Validation;
import utility.ValidationRule;

/**
 *
 * @author HieuTT
 */
public class ConfirmResetPassword extends HttpServlet {
    private static final String LOGIN_PATH="login";
    private static final String SUCCESS_FIELD="success";
    private static final String EMAIL_FIELD = "email";
    private static final String TOKEN_IDENTITY="tokenId";
    private static final String ERROR_CONFIRM_FIELD = "errorConfirmPassword";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String token = request.getParameter("token");
        if (token == null || token.isEmpty()) {
            response.sendRedirect(LOGIN_PATH);
            return;
        }

        RegisterService service = new RegisterService();
        EmailVerificationToken tokenObject = service.getTokenByToken(token);
        if (tokenObject == null) {
            response.sendRedirect(LOGIN_PATH);
            return;
        }

        if (Email.isExpireTime(tokenObject.getExpiryDate().toLocalDateTime())) {
            request.setAttribute(SUCCESS_FIELD, "false");
        } else {
            request.setAttribute(EMAIL_FIELD, tokenObject.getEmail());
            request.setAttribute("oldPassword", tokenObject.getPassword());
            request.setAttribute(SUCCESS_FIELD, "true");
            request.setAttribute(TOKEN_IDENTITY, tokenObject.getTokenId());
        }

        request.getRequestDispatcher("View/ConfirmResetPassword.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute(SUCCESS_FIELD, "true");
        String email = request.getParameter(EMAIL_FIELD);
        request.setAttribute(EMAIL_FIELD, email);
        String tokenId = request.getParameter(TOKEN_IDENTITY);
        request.setAttribute(TOKEN_IDENTITY, tokenId);

        //verify Password
        String password = request.getParameter("password");
        String oldPassword=request.getParameter("oldPassword");
        request.setAttribute("oldPassword", oldPassword);
        boolean errorPassword = validatePassword(request,password,oldPassword);

        //verify Confirm Password
        String confirmPassword = request.getParameter("confirmPassword");
        boolean errorConfirmPassword = isErrorConfirmPassword(request, confirmPassword, password);

        if (errorPassword || errorConfirmPassword) {
            request.getRequestDispatcher("View/ConfirmResetPassword.jsp").forward(request, response);
            return;
        }
        password = Encryption.toSHA256(password);
        RegisterService service = new RegisterService();
        service.resetPassword(email, password);

        service.deleteConfirmedToken(Integer.parseInt(tokenId));

        response.sendRedirect(LOGIN_PATH);
    }
    
    private boolean validatePassword(HttpServletRequest request, String input, String oldPassword) {
        return Validation.validateField(request,
                "errorPassword",
                "Password",
                input,
                Function.identity(),
                List.of(
                        new ValidationRule<>(value-> !Encryption.toSHA256(value).equals(oldPassword),
                                "New password cannot be the same as old password"),
                        new ValidationRule<>(value -> value.charAt(0) != ' ' && value.charAt(value.length() - 1) != ' ',
                                "Password cannot start or end with space"),
                        new ValidationRule<>(value -> Validation.checkFormatException(value, "PASSWORD"),
                                "Password must contains 8 characters with lower, upper, special and digit")));
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
    }// </editor-fold>

}
