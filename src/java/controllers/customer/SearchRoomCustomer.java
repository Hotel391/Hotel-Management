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
import utility.ValidationRule;
import static utility.Validation.readInputField;

/**
 *
 * @author HieuTT
 */
@WebServlet(name="SearchRoomCustomer", urlPatterns={"/searchRoom"})
public class SearchRoomCustomer extends HttpServlet {
    private static final int PAGE_SIZE = 4;
    private int maxPrice=dal.TypeRoomDAO.getInstance().getMaxPriceOfTypeRoom();
    private static final int maxTimeSpan = models.Cart.MAX_TIME_SPAN;
    private Date maxCheckoutDate=models.Cart.MAX_CHECKOUT_DATE;
    private Date maxCheckinDate=models.Cart.MAX_CHECKIN_DATE;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        Date checkinDate = getCheckinDate(request);
        Date checkoutDate = getCheckoutDate(request, checkinDate);

        request.setAttribute("checkin", checkinDate);
        request.setAttribute("checkout", checkoutDate);
        int adult= request.getParameter("adults") != null ? Integer.parseInt(request.getParameter("adults")) : 1;
        int children= request.getParameter("children") != null ? Integer.parseInt(request.getParameter("children")) : 0;
        if(adult < 1) {
            adult = 1;
        }
        if(children < 0) {
            children = 0;
        }
        request.setAttribute("adults", adult);
        request.setAttribute("children", children);
        setMaxOfMaxPriceAndTimeSpan(request);
        handleDefault(request, checkinDate, checkoutDate, adult, children);
        request.setAttribute("checkout", checkoutDate);
        request.getRequestDispatcher("/View/Customer/SearchRoom.jsp").forward(request, response);
    } 

    private void handleDefault(HttpServletRequest request, Date checkin, Date checkout, int adult, int children) throws ServletException, IOException {
        Integer minPrice = readMinPrice(request);
        Integer maxPrice = readMaxPrice(request, minPrice);

        int numberOfTypeRoom = dal.TypeRoomDAO.getInstance().getTotalTypeRoom(checkin, checkout,minPrice, maxPrice, adult, children);
        int totalPages = (int) Math.ceil((double) numberOfTypeRoom / PAGE_SIZE);
        request.setAttribute("totalPages", totalPages);
        int currentPage = request.getParameter("page") != null ? Integer.parseInt(request.getParameter("page")) : 1;
        request.setAttribute("currentPage", currentPage);
        String sortOrder = getSortOrder(request);
        setSortAttribute(request, sortOrder);
        List<TypeRoom> typeRooms = dal.TypeRoomDAO.getInstance().getAvailableTypeRooms(checkin, checkout, currentPage, PAGE_SIZE, minPrice, maxPrice,adult, children, sortOrder);
        request.setAttribute("typeRooms", typeRooms);
    }

    private Integer readMinPrice(HttpServletRequest request) {
        String minPriceStr = request.getParameter("minPrice");
        return readInputField(request, "errorPrice", minPriceStr, s-> Integer.valueOf(s.replaceAll("\\D", "")), 
                    List.of(
                        new ValidationRule<>(value-> value >= 0, "Giá tối thiểu không được âm."),
                        new ValidationRule<>(value -> value <= maxPrice, "Giá tối thiểu phải nhỏ hơn hoặc bằng "+ String.format("%,d", maxPrice).replace(',', '.') + ".")
                    ));
    }

    private Integer readMaxPrice(HttpServletRequest request, Integer minPrice) {
        String maxPriceStr = request.getParameter("maxPrice");
        Integer baseMinPrice = minPrice == null ? 0 : minPrice;
        return readInputField(request, "errorPrice", maxPriceStr, s-> Integer.valueOf(s.replaceAll("\\D", "")), 
                    List.of(
                        new ValidationRule<>(value-> value > 0, "Giá tối đa phải là một số dương."),
                        new ValidationRule<>(value -> value >= baseMinPrice, "Giá tối đa phải lớn hơn hoặc bằng giá tối thiểu."),
                        new ValidationRule<>(value -> value <= maxPrice, "Giá tối đa phải nhỏ hơn hoặc bằng "+ String.format("%,d", maxPrice).replace(',', '.') + ".")
                    ));
    }

    private Date getCheckinDate(HttpServletRequest request) throws ServletException, IOException {
        String dateStr = request.getParameter("checkin");
        Date defaultDate = Date.valueOf(LocalDate.now());
        Date checkin= readInputField(dateStr, Date::valueOf, 
                List.of(
                    new ValidationRule<>(value -> !value.before(defaultDate), "Check-in date must be today or after today."),
                    new ValidationRule<>(value -> !value.after(maxCheckinDate), "Check-in date must be before " + maxCheckinDate + ".")
                ), defaultDate);
        request.setAttribute("checkin", checkin);
        return checkin;
    }

    private Date getCheckoutDate(HttpServletRequest request, Date checkinDate) throws ServletException, IOException {
        String dateStr = request.getParameter("checkout");
        Date defaultDate = Date.valueOf(checkinDate.toLocalDate().plusDays(1));
        Date calculatedMaxDate = Date.valueOf(checkinDate.toLocalDate().plusDays(maxTimeSpan));
        final Date maxCheckoutDateLocal = calculatedMaxDate.after(maxCheckoutDate) ? maxCheckoutDate : calculatedMaxDate;
        Date checkout= readInputField(dateStr, Date::valueOf, 
                List.of(
                    new ValidationRule<>(value -> value.after(checkinDate), "Check-out date must be after check-in date."),
                    new ValidationRule<>(value -> !value.after(maxCheckoutDateLocal), "Check-out date must be before " + maxCheckoutDateLocal + ".")
                ), defaultDate);
        request.setAttribute("checkout", checkout);
        return checkout;
    }

    private String getSortOrder(HttpServletRequest request) {
        String sortOrder = request.getParameter("sort") != null ? request.getParameter("sort") : "price-low";
        return switch (sortOrder) {
            case "price-low" -> "sub.Price";
            case "price-high" -> "sub.Price DESC";
            case "rating-high" -> "sub.Rating DESC";
            default -> "sub.Price";
        }; // Default sort by price low
    }
    private void setSortAttribute(HttpServletRequest request, String sort) {
        switch (sort) {
            case "sub.Price" -> request.setAttribute("sortOrder", "price-low");
            case "sub.Price DESC" -> request.setAttribute("sortOrder", "price-high");
            case "sub.Rating DESC" -> request.setAttribute("sortOrder", "rating-high");
            default -> {
            }
        }
    }

    private void setMaxOfMaxPriceAndTimeSpan(HttpServletRequest request) {
        request.setAttribute("maxPriceAvailable", maxPrice);
        request.setAttribute("maxTimeSpan", maxTimeSpan);
        request.setAttribute("maxCheckinDate", this.maxCheckinDate);
        request.setAttribute("maxCheckoutDate", this.maxCheckoutDate);
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
