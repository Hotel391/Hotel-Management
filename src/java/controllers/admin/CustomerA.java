package controllers.admin;

import dal.CustomerDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import models.Customer;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "CustomerController", urlPatterns = {"/admin/customers"})
public class CustomerA extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        showCustomer(request, response);
    }

   @Override
protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
    String action = request.getParameter("action");

    try {
        if ("toggleStatus".equals(action)) {
            int customerId = Integer.parseInt(request.getParameter("customerId"));
            Customer customer = CustomerDAO.getInstance().getCustomerByCustomerID(customerId);

            if (customer != null) {
                boolean newStatus = !customer.getActivate();
                customer.setActivate(newStatus);
                CustomerDAO.getInstance().updateCustomerStatus(customer.getCustomerId(), newStatus);

                String key = request.getParameter("key");
                String currentPage = request.getParameter("page");

                String redirectUrl = request.getContextPath() + "/admin/customers";
                if (key != null && !key.trim().isEmpty()) {
                    redirectUrl += "?key=" + key;
                }
                if (currentPage != null) {
                    if (key != null && !key.trim().isEmpty()) {
                        redirectUrl += "&page=" + currentPage;
                    } else {
                        redirectUrl += "?page=" + currentPage;
                    }
                }

                response.sendRedirect(redirectUrl); 
            } else {
                request.setAttribute("error", "Customer not found.");
                showCustomer(request, response);  
            }
        } else {
            request.setAttribute("error", "Invalid action.");
            showCustomer(request, response); 
        }
    } catch (Exception e) {
        request.setAttribute("error", "Error: " + e.getMessage());
        showCustomer(request, response); 
    }
}



    private void showCustomer(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Customer> customerList;

        String key = request.getParameter("key");
        int customerTotal;
        int endPage;

        if (key != null && !key.trim().isEmpty()) {
            customerTotal = CustomerDAO.getInstance().searchCustomer(key).size();
            endPage = customerTotal / 5;
            if (customerTotal % 5 != 0) {
                endPage++;
            }
            request.setAttribute("key", key);
        } else {
            customerTotal = CustomerDAO.getInstance().countCustomer();
            endPage = customerTotal / 5;
            if (customerTotal % 5 != 0) {
                endPage++;
            }
        }

        int currentPage = Integer.parseInt(request.getParameter("page") == null ? "1" : request.getParameter("page"));

        customerList = CustomerDAO.getInstance().customerPagination(currentPage, key);

        request.setAttribute("currentPage", currentPage);
        request.setAttribute("endPage", endPage);
        request.setAttribute("listCustomer", customerList);
        request.getRequestDispatcher("/View/Admin/Customers.jsp").forward(request, response);
    }


    private void toggleCustomerStatus(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int customerId = Integer.parseInt(request.getParameter("customerId"));
            Customer customer = CustomerDAO.getInstance().getCustomerByCustomerID(customerId);

            if (customer != null) {
                boolean newStatus = !customer.getActivate(); 
                customer.setActivate(newStatus);
                CustomerDAO.getInstance().updateCustomerStatus(customer.getCustomerId(), newStatus);
                response.sendRedirect(request.getContextPath() + "/admin/customers");
            } else {
                request.setAttribute("error", "Customer not found.");
                doGet(request, response);
            }

        } catch (NumberFormatException e) {
            request.setAttribute("error", "Invalid customer ID format.");
            doGet(request, response);
        }
    }

}
