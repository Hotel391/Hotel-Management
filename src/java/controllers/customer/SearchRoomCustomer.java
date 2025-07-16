package controllers.customer;

import java.io.IOException;
import java.math.BigInteger;

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
   
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        String checkin=request.getParameter("checkin");
        Date checkinDate = getDate(checkin,null,Date.valueOf(LocalDate.now()));
        request.setAttribute("checkin", checkinDate);

        String checkout=request.getParameter("checkout");
        Date checkoutDate = getDate(checkout, true, checkinDate);

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
        handleDefault(request, checkinDate, checkoutDate, adult, children);
        request.setAttribute("checkout", checkoutDate);
        request.getRequestDispatcher("/View/Customer/SearchRoom.jsp").forward(request, response);
    } 

    private void handleDefault(HttpServletRequest request, Date checkin, Date checkout, int adult, int children) throws ServletException, IOException {
        BigInteger minPrice = readMinPrice(request);
        BigInteger maxPrice = readMaxPrice(request, minPrice);

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

    private BigInteger readMinPrice(HttpServletRequest request) {
        String minPriceStr = request.getParameter("minPrice");
        return readInputField(request, "errorPrice", minPriceStr, s -> new BigInteger(s.replaceAll("\\D", "")),
                    List.of(
                        new ValidationRule<>(value -> value.compareTo(BigInteger.ZERO) >= 0, "Minimum price must be a non-negative number."),
                        new ValidationRule<>(value -> value.compareTo(new BigInteger("10000000000")) <= 0, "Minimum price must be less than or equal to 10,000,000,000.")
                    ));
    }

    private BigInteger readMaxPrice(HttpServletRequest request, BigInteger minPrice) {
        String maxPriceStr = request.getParameter("maxPrice");
        BigInteger baseMinPrice = minPrice == null ? BigInteger.ZERO : minPrice;
        return readInputField(request, "errorPrice", maxPriceStr, s -> new BigInteger(s.replaceAll("\\D", "")),
                    List.of(
                        new ValidationRule<>(value -> value.compareTo(BigInteger.ZERO) > 0, "Maximum price must be a positive number."),
                        new ValidationRule<>(value -> value.compareTo(baseMinPrice) >= 0, "Maximum price must be greater or equal to minimum price."),
                        new ValidationRule<>(value -> value.compareTo(new BigInteger("10000000000")) <= 0, "Maximum price must be less than or equal to 10,000,000,000.")
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

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
