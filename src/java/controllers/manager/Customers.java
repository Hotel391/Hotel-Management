package controllers.manager;

import controllers.manager.*;
import dal.CustomerDAO;
import dal.RoleDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import models.Customer;

import java.io.IOException;
import java.util.List;
import models.Role;

@WebServlet(name = "CustomerController", urlPatterns = {"/manager/customers"})
public class Customers extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Role> roleList = RoleDAO.getInstance().getAllRoles();
        request.setAttribute("listRole", roleList);

        String key = request.getParameter("key");

        int CustumerTotal;
        if (key != null && !key.trim().isEmpty()) {
            CustumerTotal = CustomerDAO.getInstance().searchCustomer(key).size();
        } else {
            CustumerTotal = CustomerDAO.getInstance().countCustomer();
        }

        int endPage = CustumerTotal / 5;
        if (CustumerTotal % 5 != 0) {
            endPage++;
        }

        request.setAttribute("endPage", endPage);

        int currentPage = Integer.parseInt(request.getParameter("page") == null ? "1" : request.getParameter("page"));
        if (currentPage > endPage) {
            currentPage = endPage;
        }

        List<Customer> customerList = CustomerDAO.getInstance().customerPagination(currentPage, key);
        request.setAttribute("currentPage", currentPage);
        request.setAttribute("listCustomer", customerList);
        request.setAttribute("key", key);

        request.getRequestDispatcher("/View/Manager/Customers.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        String page = request.getParameter("page");
        String key = request.getParameter("key");

        try {
            switch (action) {
                case "toggleStatus":
                    int customerId = Integer.parseInt(request.getParameter("customerId"));
                    Customer customer = CustomerDAO.getInstance().getCustomerByCustomerID(customerId);
                    if (customer != null) {
                        boolean newStatus = !customer.getActivate();
                        CustomerDAO.getInstance().updateCustomerStatus(customer.getCustomerId(), newStatus);
                        request.getSession().setAttribute("success", "Trạng thái khách hàng đã được cập nhật.");
                    }
                    break;
                default:
                    request.setAttribute("error", "Hành động không hợp lệ.");
                    doGet(request, response);
                    return;
            }
        } catch (Exception e) {
            request.setAttribute("error", "Lỗi: " + e.getMessage());
            doGet(request, response);
            return;
        }

        String redirectUrl = request.getContextPath() + "/manager/customers";
        if (page != null || key != null) {
            redirectUrl += "?";
            if (page != null) {
                redirectUrl += "page=" + page;
            }
            if (key != null) {
                if (page != null) {
                    redirectUrl += "&";
                }
                redirectUrl += "key=" + key;
            }
        }
        response.sendRedirect(redirectUrl);
    }
}
