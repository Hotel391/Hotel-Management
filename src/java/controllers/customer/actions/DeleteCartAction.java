package controllers.customer.actions;

import dal.CartDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 *
 * @author HieuTT
 */
public class DeleteCartAction implements CartAction{

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int cartId = Integer.parseInt(request.getParameter("cartId"));
        CartDAO.getInstance().deleteCart(cartId);
        response.sendRedirect(request.getContextPath() + "/cart");
    }
    
}
