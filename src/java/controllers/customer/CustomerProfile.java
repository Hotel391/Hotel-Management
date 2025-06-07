package controllers.customer;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import models.Customer;
import models.CustomerAccount;
import utility.Validation;

@WebServlet(name = "CustomerProfile", urlPatterns = {"/customer/customerProfile"})
public class CustomerProfile extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String service = request.getParameter("service");
        String submit = request.getParameter("submit");

        if (service == null) {
            service = "info";
        }

        if (service.equals("update")) {
            if (submit == null) {
                String username = request.getParameter("username");
                CustomerAccount ca = dal.CustomerDAO.getInstance().getCustomer(username);
                request.setAttribute("customerAccount", ca);
                request.getRequestDispatcher("/View/Customer/UpdateProfile.jsp").forward(request, response);
            } else {
                String username = request.getParameter("username");
                String fullName = request.getParameter("fullName");
                String email = request.getParameter("email");
                String phoneNumber = request.getParameter("phoneNumber");
                String gender = request.getParameter("gender");
                boolean genderBoolean = Boolean.parseBoolean(gender);
                int genderValue = genderBoolean ? 1 : 0;
                String cccd = request.getParameter("cccd");

                boolean hasError = false;
                hasError |= Validation.validateField(request, "fullNameError", fullName, "FULLNAME", "Full Name", "Full name must be 2–100 characters, letters and spaces only.");
                hasError |= Validation.validateField(request, "emailError", email, "EMAIL", "Email", "Invalid email format.");
                hasError |= Validation.validateField(request, "phoneError", phoneNumber, "PHONE_NUMBER", "Phone Number", "Phone number must start with 0 and have 10–11 digits.");

                if (hasError) {
                    CustomerAccount ca = new CustomerAccount();
                    ca.setUsername(username);
                    Customer c = new Customer();
                    c.setFullName(fullName);
                    c.setEmail(email);
                    c.setPhoneNumber(phoneNumber);
                    c.setGender(genderBoolean);
                    c.setCCCD(cccd);
                    ca.setCustomer(c);
                    request.setAttribute("username", username);
                    request.setAttribute("customerAccount", ca);
                    request.getRequestDispatcher("/View/Customer/UpdateProfile.jsp").forward(request, response);
                    return;
                }
                dal.CustomerDAO.getInstance().updateCustomerInfo(username, fullName, email, phoneNumber, genderValue, cccd);
                response.sendRedirect(request.getContextPath() + "/customer/customerProfile?service=info&username=" + username);
                return;
            }
        }

        if (service.equals("info")) {
            String username = request.getParameter("username");
            CustomerAccount ca = dal.CustomerDAO.getInstance().getCustomer(username);
            request.setAttribute("customerAccount", ca);
            request.getRequestDispatcher("/View/Customer/Profile.jsp").forward(request, response);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
