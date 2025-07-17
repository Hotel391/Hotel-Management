package controllers.customer.actions;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import models.Cart;
import models.CartService;

import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static utility.Validation.readInputField;
import utility.ValidationRule;

/**
 *
 * @author HieuTT
 */
public class SaveUpdateCartAction implements CartAction {

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        int cartId = (int) session.getAttribute("cartId");
        session.removeAttribute("cartId");
        if( isValidDate(request, "checkin") && isValidDate(request, "checkout") ) {
            Date checkinDate = getCheckinDate(request);
            Date checkoutDate = getCheckoutDate(request, checkinDate);
            dal.CartDAO.getInstance().updateCheckinCheckout(cartId, checkinDate, checkoutDate);
        }

        updateCurrentCartServices(request, cartId);
        updateOtherCartServices(request, cartId);

        // Redirect to the cart page or show a success message
        response.sendRedirect("cart");
    }

    private boolean isValidDate(HttpServletRequest request, String inputField) {
        String rawInput= request.getParameter(inputField);
        if (rawInput == null || rawInput.trim().isEmpty()) {
            return false;
        }
        Date value;
        try{
            value = Date.valueOf(rawInput.trim());
        } catch (IllegalArgumentException e) {
            return false;
        }
        return !value.before(Date.valueOf(LocalDate.now()));
    }
    
    private Date getCheckinDate(HttpServletRequest request) throws ServletException, IOException {
        String dateStr = request.getParameter("checkin");
        Date defaultDate = Date.valueOf(LocalDate.now());
        return readInputField(dateStr, Date::valueOf, 
                List.of(
                    new ValidationRule<>(value -> !value.before(defaultDate), "Check-in date must be today or after today.")
                ), defaultDate);
    }

    private Date getCheckoutDate(HttpServletRequest request, Date checkinDate) throws ServletException, IOException {
        String dateStr = request.getParameter("checkout");
        Date defaultDate = Date.valueOf(checkinDate.toLocalDate().plusDays(1));
        return readInputField(dateStr, Date::valueOf, 
                List.of(
                    new ValidationRule<>(value -> value.after(checkinDate), "Check-out date must be after check-in date.")
                ), defaultDate);
    }

    private void updateCurrentCartServices(HttpServletRequest request, int cartId) {
        Cart cart = dal.CartDAO.getInstance().getDetailCartForIsPayment(cartId);
        List<CartService> currentCartServices = cart.getCartServices();
        Date checkinDate = cart.getStartDate();
        Date checkoutDate = cart.getEndDate();
        int numberOfNight = (int) java.time.temporal.ChronoUnit.DAYS.between(checkinDate.toLocalDate(), checkoutDate.toLocalDate());
        Map<Integer, Integer> serviceCannotDisable = dal.CartDAO.getInstance().getServiceCannotDisable(cartId);
        serviceCannotDisable.replaceAll((serviceId, quantity) -> quantity * numberOfNight);
        for (int id : dal.CartDAO.getInstance().serviceIdsDonNeedTimes) {
            if (serviceCannotDisable.containsKey(id)) {
                serviceCannotDisable.put(id, 1);
            }
        }
        
        for(CartService cs : currentCartServices) {
            int serviceId= cs.getService().getServiceId();
            String checkboxParam= "service_" + serviceId;
            String quantityParam = "quantity_" + serviceId;

            boolean isProtected= serviceCannotDisable.containsKey(serviceId);
            if(request.getParameter(checkboxParam) == null && !isProtected) {
                dal.CartDAO.getInstance().deleteCartService(cartId, serviceId);
                continue;
            }
            String quantityStr= request.getParameter(quantityParam);
            try {
                int quantity = Integer.parseInt(quantityStr);
                int defaultQuantity=1;
                if(isProtected) {
                    defaultQuantity = serviceCannotDisable.get(serviceId);
                }
                if(quantity >= defaultQuantity && quantity <= 1000){
                    dal.CartDAO.getInstance().updateCartServiceQuantity(cartId, serviceId, quantity);
                }
            } catch (NumberFormatException e) {
                //
            }
        }
    }

    private void updateOtherCartServices(HttpServletRequest request, int cartId) {
        request.getParameterMap().forEach((key, value) -> {
            if (key.startsWith("oService_")) {
                try{
                    int serviceId= Integer.parseInt(key.substring(9));
                    String quantityStr = request.getParameter("oQuantity_" + serviceId);
                    int quantity = Integer.parseInt(quantityStr);
                    if (quantity > 0 && quantity <= 1000) {
                        dal.CartDAO.getInstance().addServiceToCart(cartId, serviceId, quantity);
                    }
                } catch (NumberFormatException e) {
                    // Handle invalid quantity input
                }
            }
        });
    }
}