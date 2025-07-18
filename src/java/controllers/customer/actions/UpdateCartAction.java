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
import models.CustomerAccount;
import models.Service;

/**
 *
 * @author HieuTT
 */
public class UpdateCartAction implements CartAction {

    private static final int maxTimeSpan = models.Cart.MAX_TIME_SPAN;
    private final Date maxCheckoutDate = models.Cart.MAX_CHECKOUT_DATE;
    private final Date maxCheckinDate = models.Cart.MAX_CHECKIN_DATE;
    private final int maxServiceQuantity = Service.MAX_SERVICE_QUANTITY;
    private final int minServiceQuantity = Service.MIN_SERVICE_QUANTITY;

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("maxTimeSpan", maxTimeSpan);
        request.setAttribute("maxCheckinDate", this.maxCheckinDate);
        request.setAttribute("maxCheckoutDate", this.maxCheckoutDate);
        request.setAttribute("maxServiceQuantity", this.maxServiceQuantity);
        request.setAttribute("minServiceQuantity", this.minServiceQuantity);
        int cartId = Integer.parseInt(request.getParameter("cartId"));
        HttpSession session = request.getSession();
        CustomerAccount customer = (CustomerAccount) session.getAttribute("customerInfo");
        if(!dal.CartDAO.getInstance().checkCartOfCustomer(customer.getCustomer().getCustomerId(), cartId)) {
            response.sendRedirect("cart");
            return;
        }
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
        session.setAttribute("cartId", cartId);
        request.setAttribute("action", "updateCart");
        request.setAttribute("cartDetails", cart);
        request.setAttribute("serviceCannotDisable", serviceCannotDisable);
        request.setAttribute("otherServices", otherServices);
        request.getRequestDispatcher("/View/Customer/Cart.jsp").forward(request, response);
    }
    
}
