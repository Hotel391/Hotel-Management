package controllers.customer;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import models.TypeRoom;
import static utility.Validation.readPriceInput;
import utility.ValidationRule;

/**
 *
 * @author HieuTT
 */
@WebServlet(name="SearchRoom", urlPatterns={"/searchRoom"})
public class SearchRoom extends HttpServlet {
    private static final int PAGE_SIZE = 4;
   
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        String checkin=request.getParameter("checkin");
        Date checkinDate = getDate(checkin,null,Date.valueOf(LocalDate.now()));
        request.setAttribute("checkin", checkinDate);

        String checkout=request.getParameter("checkout");
        Date checkoutDate = getDate(checkout, true, checkinDate);
        
        handleDefault(request, checkinDate, checkoutDate);
        request.setAttribute("checkout", checkoutDate);
        request.getRequestDispatcher("/View/Customer/SearchRoom.jsp").forward(request, response);
    } 

    private void handleDefault(HttpServletRequest request, Date checkin, Date checkout) throws ServletException, IOException {
        Integer minPrice = readMinPrice(request);
        Integer maxPrice = readMaxPrice(request, minPrice);
        
        int numberOfTypeRoom = dal.TypeRoomDAO.getInstance().getTotalTypeRoom(checkin, checkout,minPrice, maxPrice);
        int totalPages = (int) Math.ceil((double) numberOfTypeRoom / PAGE_SIZE);
        request.setAttribute("totalPages", totalPages);
        int currentPage = request.getParameter("page") != null ? Integer.parseInt(request.getParameter("page")) : 1;
        request.setAttribute("currentPage", currentPage);
        List<TypeRoom> typeRooms = dal.TypeRoomDAO.getInstance().getAvailableTypeRooms(checkin, checkout, currentPage, PAGE_SIZE, minPrice, maxPrice);
        request.setAttribute("typeRooms", typeRooms);
    }

    private Integer readMinPrice(HttpServletRequest request) {
        String minPriceStr = request.getParameter("minPrice");
        return readPriceInput(request, "errorPrice", minPriceStr, s-> Integer.valueOf(s.replaceAll("\\D", "")), 
                    List.of(
                        new ValidationRule<>(value-> value >= 0, "Minimum price must be a non-negative number."),
                        new ValidationRule<>(value -> value <= 1000000000, "Minimum price must be less than or equal to 1,000,000,000.")
                    ));
    }

    private Integer readMaxPrice(HttpServletRequest request, Integer minPrice) {
        String maxPriceStr = request.getParameter("maxPrice");
        Integer baseMinPrice = minPrice == null ? 0 : minPrice;
        return readPriceInput(request, "errorPrice", maxPriceStr, s-> Integer.valueOf(s.replaceAll("\\D", "")), 
                    List.of(
                        new ValidationRule<>(value-> value > 0, "Maximum price must be a positive number."),
                        new ValidationRule<>(value -> value > baseMinPrice, "Maximum price must be greater than minimum price."),
                        new ValidationRule<>(value -> value <= 1000000000, "Maximum price must be less than or equal to 1,000,000,000.")
                    ));
    }

    private Date getDate(String dateStr, Boolean isCheckout, Date minDate) {
        if (dateStr == null || dateStr.isEmpty()) {
            if(isCheckout != null && isCheckout) {
                return java.sql.Date.valueOf(LocalDate.now().plusDays(1));
            }
            return java.sql.Date.valueOf(LocalDate.now());
        } else {
            Date result= Date.valueOf(dateStr);
            if(result.before(minDate)) {
                if(isCheckout != null && isCheckout) {
                    return java.sql.Date.valueOf(LocalDate.now().plusDays(1));
                }
                return java.sql.Date.valueOf(LocalDate.now());
            }
            return result;
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
