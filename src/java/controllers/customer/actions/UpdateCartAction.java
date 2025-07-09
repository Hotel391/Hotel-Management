package controllers.customer.actions;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Date;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import models.Cart;
import models.CartService;
import models.Service;

/**
 *
 * @author HieuTT
 */
public class UpdateCartAction implements CartAction {

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int cartId = Integer.parseInt(request.getParameter("cartId"));
        Cart cart = dal.CartDAO.getInstance().getDetailCartForIsPayment(cartId);
        List<Integer> serviceIds = new ArrayList<>();
        for (CartService service : cart.getCartServices()) {
            serviceIds.add(service.getService().getServiceId());
        }
        Date checkinDate = cart.getStartDate();
        Date checkoutDate = cart.getEndDate();
        int numberOfNight = (int) ChronoUnit.DAYS.between(checkinDate.toLocalDate(), checkoutDate.toLocalDate());
        Map<Integer,Integer> serviceCannotDisable= dal.CartDAO.getInstance().getServiceCannotDisable(cartId);
        serviceCannotDisable.replaceAll((serviceId, quantity) -> quantity * numberOfNight);
        for(int id : dal.CartDAO.getInstance().serviceIdsDonNeedTimes) {
            if (serviceCannotDisable.containsKey(id)) {
                serviceCannotDisable.put(id, 1);
            }
        }
        
        List<Service> otherServices= dal.CartDAO.getInstance().getOtherServices(serviceIds);
        HttpSession session = request.getSession();
        session.setAttribute("cartId", cartId);
        request.setAttribute("action", "updateCart");
        request.setAttribute("cartDetails", cart);
        request.setAttribute("serviceCannotDisable", serviceCannotDisable);
        request.setAttribute("otherServices", otherServices);
        request.getRequestDispatcher("/View/Customer/Cart.jsp").forward(request, response);
    }
    
}
