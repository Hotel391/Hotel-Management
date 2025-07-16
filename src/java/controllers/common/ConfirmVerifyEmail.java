package controllers.common;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import models.Customer;
import models.CustomerAccount;
import models.EmailVerificationToken;
import services.RegisterService;
import utility.EmailService;

/**
 *
 * @author TranTrungHieu
 */
public class ConfirmVerifyEmail extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String token = request.getParameter("token");
        if (token == null || token.isEmpty()) {
            response.sendRedirect("register");
            return;
        }

        RegisterService service = new RegisterService();
        EmailVerificationToken tokenObject = service.getTokenByToken(token);

        if (tokenObject == null) {
            response.sendRedirect("login");
            return;
        }

        if (EmailService.isExpireTime(tokenObject.getExpiryDate().toLocalDateTime())) {
            request.setAttribute("success", "false");
        } else {
            Customer customer = new Customer();
            customer.setFullName(tokenObject.getFullname());
            customer.setEmail(tokenObject.getEmail());
            customer.setGender(tokenObject.getGender());
            if (!service.isEmailExistsInCustomer(tokenObject.getEmail())) {
                customer.setCustomerId(service.insertCustomer(customer));
            } else {
                customer.setCustomerId(service.getCustomerIdByEmail(tokenObject.getEmail()));
                service.updateCustomerInfo(customer.getCustomerId(), customer.getFullName(), customer.getGender());
            }
            insertCustomerAccount(service, customer, tokenObject);
            request.setAttribute("success", "true");

            service.deleteConfirmedToken(tokenObject.getTokenId());
        }

        request.getRequestDispatcher("View/ConfirmVerifyEmail.jsp").forward(request, response);
    }

    public void insertCustomerAccount(RegisterService service, Customer customer, EmailVerificationToken token) {
        CustomerAccount account = new CustomerAccount();
        account.setUsername(token.getUsername());
        account.setPassword(token.getPassword());
        account.setCustomer(customer);
        service.insertCustomerAccount(account);
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
