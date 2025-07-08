package controllers.customer;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import controllers.customer.actions.CartAction;
import controllers.customer.actions.DeleteCartAction;
import controllers.customer.actions.SaveUpdateCartAction;
import controllers.customer.actions.UpdateCartAction;
import controllers.customer.actions.ViewDetailsAction;
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
    private final Map<String, CartAction> actions = new HashMap<>();
   
    @Override
    public void init() {
        actions.put("deleteCart", new DeleteCartAction());
        actions.put("viewDetails", new ViewDetailsAction());
        actions.put("updateCart", new UpdateCartAction());
        actions.put("saveUpdateCart", new SaveUpdateCartAction());
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        HttpSession session = request.getSession();
        CustomerAccount customerAccount = (CustomerAccount) session.getAttribute("customerInfo");
        if(customerAccount == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        String action = request.getParameter("action");
        if (action != null && actions.containsKey(action)) {
            actions.get(action).execute(request, response);
        } else{
            int customerId = customerAccount.getCustomer().getCustomerId();
            request.setAttribute("carts", dal.CartDAO.getInstance().getCartByCustomerId(customerId));
            request.getRequestDispatcher("/View/Customer/Cart.jsp").forward(request, response);
        }
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
