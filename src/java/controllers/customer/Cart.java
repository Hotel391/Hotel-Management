package controllers.customer;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import models.CustomerAccount;

/**
 *
 * @author HieuTT
 */
@WebServlet(name="Cart", urlPatterns={"/cart"})
public class Cart extends HttpServlet {
   
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        HttpSession session = request.getSession();
        CustomerAccount customerAccount = (CustomerAccount) session.getAttribute("customerInfo");
        if(customerAccount == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        int customerId = customerAccount.getCustomer().getCustomerId();
        request.setAttribute("carts", dal.CartDAO.getInstance().getCartByCustomerId(customerId));
        request.getRequestDispatcher("/View/Customer/Cart.jsp").forward(request, response);
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
