package controllers.customer;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

import models.Customer;
import models.CustomerAccount;
import models.TypeRoom;
import services.sorting.ReviewSortFactory;
import services.sorting.ReviewSortStrategy;
import static utility.Validation.readInputField;
import utility.ValidationRule;

/**
 *
 * @author HieuTT
 */
@WebServlet(name="DetailRoom", urlPatterns={"/detailRoom"})
public class DetailRoom extends HttpServlet {
    private static final int NUMBER_OF_REVIEWS_PER_PAGE = 4;
   
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        int typeId = request.getParameter("typeRoomId") != null ? Integer.parseInt(request.getParameter("typeRoomId")) : 0;
        Date checkin=getCheckinDate(request);
        Date checkout=getCheckoutDate(request, checkin);
        if(typeId==0 || checkin == null || checkout == null) {
            response.sendRedirect("searchRoom");
            return;
        }

        String sortOption= request.getParameter("sortBy") != null ? request.getParameter("sortBy") : "";
        ReviewSortStrategy strategy=ReviewSortFactory.getStrategy(sortOption);
        String orderByClause = strategy.getOrderByClause();
        processPostFeedBack(request, typeId);
        int currentPage = request.getParameter("page") != null ? Integer.parseInt(request.getParameter("page")) : 1;
        int offset = (currentPage - 1) * NUMBER_OF_REVIEWS_PER_PAGE;
        int adults = getAdults(request);
        int children = getChildren(request);
        
        TypeRoom selectedTypeRoom = dal.TypeRoomDAO.getInstance().getTypeRoomByTypeId(checkin, checkout, typeId, adults, children, orderByClause, offset, NUMBER_OF_REVIEWS_PER_PAGE);
        if(selectedTypeRoom == null) {
            response.sendRedirect("searchRoom");
            return;
        }
        
        int numberOfReviews= selectedTypeRoom.getNumberOfReviews();
        int totalPages = (int) Math.ceil((double) numberOfReviews / NUMBER_OF_REVIEWS_PER_PAGE);
        request.setAttribute("currentPage", currentPage);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("selectedTypeRoom", selectedTypeRoom);
        request.getRequestDispatcher("View/Customer/DetailRoom.jsp").forward(request, response);
    } 

    private void processPostFeedBack(HttpServletRequest request, int typeId) throws ServletException, IOException {
        HttpSession session = request.getSession();
        if (session.getAttribute("customerInfo") == null) {
            return;
        }
        CustomerAccount customerAccount= (CustomerAccount) session.getAttribute("customerInfo");
        Customer customer= customerAccount.getCustomer();
        boolean canPostFeedback = dal.BookingDetailDAO.getInstance().canPostFeedback(typeId, customer.getCustomerId());
        if(canPostFeedback){
            request.setAttribute("canPostFeedback", true);
            return;
        }
        request.setAttribute("canPostFeedback", false);
    }

    private Date getCheckinDate(HttpServletRequest request) throws ServletException, IOException {
        String dateStr = request.getParameter("checkin");
        Date defaultDate = Date.valueOf(LocalDate.now());
        Date checkin= readInputField(dateStr, Date::valueOf, 
                List.of(
                    new ValidationRule<>(value -> !value.before(defaultDate), "Check-in date must be today or after today.")
                ), defaultDate);
        request.setAttribute("checkin", checkin);
        return checkin;
    }

    private Date getCheckoutDate(HttpServletRequest request, Date checkinDate) throws ServletException, IOException {
        String dateStr = request.getParameter("checkout");
        Date defaultDate = Date.valueOf(checkinDate.toLocalDate().plusDays(1));
        Date checkout= readInputField(dateStr, Date::valueOf, 
                List.of(
                    new ValidationRule<>(value -> value.after(checkinDate), "Check-out date must be after check-in date.")
                ), defaultDate);
        request.setAttribute("checkout", checkout);
        return checkout;
    }

    private int getAdults(HttpServletRequest request) throws ServletException, IOException {
        String adultsStr = request.getParameter("adults");
        int defaultAdults = 1;
        int adults = readInputField(adultsStr, Integer::parseInt, 
                List.of(
                    new ValidationRule<>(value -> value > 0, "Number of adults must be greater than 0.")
                ), defaultAdults);
        request.setAttribute("adults", adults);
        return adults;
    }

    private int getChildren(HttpServletRequest request) throws ServletException, IOException {
        String childrenStr = request.getParameter("children");
        int defaultChildren = 0;
        int children = readInputField(childrenStr, Integer::parseInt, 
                List.of(
                    new ValidationRule<>(value -> value >= 0, "Number of children must be 0 or greater.")
                ), defaultChildren);
        request.setAttribute("children", children);
        return children;
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        String action = request.getParameter("action");
        int typeId = request.getParameter("typeRoomId") != null ? Integer.parseInt(request.getParameter("typeRoomId")) : 0;
        if(typeId == 0) {
            response.sendRedirect("searchRoom");
            return;
        }
        HttpSession session = request.getSession();
        if (session.getAttribute("customerInfo") == null) {
            response.sendRedirect("login");
            return;
        }
        CustomerAccount customerAccount = (CustomerAccount) session.getAttribute("customerInfo");
        Customer customer = customerAccount.getCustomer();

        if( "addToCart".equals(action)) {
            boolean isAdded = addToCart(request, typeId, customer.getCustomerId());
            if (isAdded) {
                response.sendRedirect("cart");
                return;
            }
        }
            
        if ("postFeedback".equals(action)) {
            addReview(request, customer.getCustomerId(), typeId, customerAccount.getUsername());
        }
        redirectAgain(request, response, typeId);
    }

    private void addReview(HttpServletRequest request, int customerId, int typeId, String username) throws ServletException, IOException {
        String reviewContent= request.getParameter("reviewContent");
        int rating = readRating(request);
        if (reviewContent == null || reviewContent.trim().isEmpty()) {
            return;
        }
        dal.BookingDetailDAO.getInstance().insertReview(customerId, typeId, username, reviewContent, rating);
    }

    private int readRating(HttpServletRequest request) throws ServletException, IOException {
        String ratingStr = request.getParameter("rating");
        int defaultRating = 5; // Default rating value
        return readInputField(ratingStr, Integer::parseInt, 
                List.of(
                    new ValidationRule<>(value -> value >= 1 && value <= 5, "Rating must be between 0 and 5.")
                ), defaultRating);
    }

    private void redirectAgain(HttpServletRequest request, HttpServletResponse response, int typeId) throws IOException, ServletException {
        Date checkDatein = getCheckinDate(request);
        Date checkDateout = getCheckoutDate(request, checkDatein);
        int adults = getAdults(request);
        int children = getChildren(request);
        String url = "detailRoom?typeRoomId=" + typeId + "&checkin=" + checkDatein + "&checkout=" + checkDateout + "&adults=" + adults + "&children=" + children;
        response.sendRedirect(url);
    }

    private boolean addToCart(HttpServletRequest request, int typeId, int customerId) throws IOException, ServletException {
        int adults = getAdults(request);
        int children = getChildren(request);
        Date checkin = getCheckinDate(request);
        Date checkout = getCheckoutDate(request, checkin);
        boolean isPayment= false;

        return dal.CartDAO.getInstance().addToCart(customerId, typeId, checkin, checkout, adults, children, isPayment);
    }
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
