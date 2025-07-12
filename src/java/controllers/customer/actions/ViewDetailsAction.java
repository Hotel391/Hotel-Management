package controllers.customer.actions;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import models.Cart;

import java.io.IOException;

/**
 *
 * @author HieuTT
 */
public class ViewDetailsAction implements CartAction {

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int cartId = Integer.parseInt(request.getParameter("cartId"));
        Cart cart = dal.CartDAO.getInstance().getDetailCartForIsPayment(cartId);
        request.setAttribute("action", "viewDetails");
        request.setAttribute("cartDetails", cart);
        request.getRequestDispatcher("/View/Customer/Cart.jsp").forward(request, response);
    }
    
}
