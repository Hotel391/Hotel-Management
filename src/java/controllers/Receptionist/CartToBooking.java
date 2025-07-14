package controllers.receptionist;

import dal.BookingDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.sql.Date;
import java.time.LocalDateTime;
import java.util.List;
import models.Booking;
import models.BookingDetail;
import models.Cart;
import models.CartService;
import models.Customer;
import models.DetailService;
import models.PaymentMethod;
import models.Room;
import models.Service;

/**
 *
 * @author Tuan'sPC
 */
@WebServlet(name = "CartToBooking", urlPatterns = {"/receptionist/cartToBooking"})
public class CartToBooking extends HttpServlet {

    private String pagee = "page";
    private String error;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String choose = request.getParameter("choose");
        if (choose == null) {
            choose = "viewCustomerToday";
        }

//        if ("search".equals(choose)) {
//            String email = request.getParameter("searchEmail");
//            String source = request.getParameter("source"); // viewCustomerToday / viewCustomerFuture / viewCustomerHasDuaDon
//            List<Cart> allCarts = dal.CartDAO.getInstance().getAllCompletedCheckInCarts();
//            List<Cart> filtered = new ArrayList<>();
//            LocalDateTime now = LocalDateTime.now();
//
//            for (Cart cart : allCarts) {
//                boolean match = false;
//
//                if ("viewCustomerToday".equals(source)) {
//                    LocalDate today = now.toLocalDate();
//                    if (cart.getStartDate().toLocalDate().equals(today) && now.toLocalTime().isAfter(LocalTime.NOON)) {
//                        match = true;
//                        request.setAttribute("cartStatus", "bookToday");
//                    }
//                } else if ("viewCustomerFuture".equals(source)) {
//                    LocalDateTime startLimit = cart.getStartDate().toLocalDate().atTime(12, 0);
//                    LocalDateTime endLimit = cart.getEndDate().toLocalDate().atTime(9, 0);
//                    if (now.isAfter(startLimit) && now.isBefore(endLimit)) {
//                        match = true;
//                        request.setAttribute("cartStatus", "bookFuture");
//                    }
//                } else if ("viewCustomerHasDuaDon".equals(source)) {
//                    LocalDateTime startLimit = cart.getStartDate().toLocalDate().atTime(12, 0);
//                    LocalDateTime endLimit = cart.getEndDate().toLocalDate().atTime(9, 0);
//                    boolean hasServiceId2 = cart.getCartServices().stream().anyMatch(cs -> cs.getService().getServiceId() == 2);
//                    if (now.isAfter(startLimit) && now.isBefore(endLimit) && hasServiceId2) {
//                        match = true;
//                        request.setAttribute("cartStatus", "view");
//                    }
//                }
//
//                if (match && cart.getMainCustomer().getEmail().equalsIgnoreCase(email)) {
//                    filtered.add(cart);
//                }
//            }
//
//            paginateCartList(request, filtered);
//            request.getRequestDispatcher("/View/Receptionist/CartToBooking.jsp").forward(request, response);
//        }
        if ("viewCustomerToday".equals(choose)) {
            viewCustomerToday(request, response);
        }

        if ("viewCustomerHasDuaDon".equalsIgnoreCase(choose)) {
            viewCustomerHasDuaDon(request, response);
        }

        if ("viewCustomerFuture".equals(choose)) {
            viewCustomerFuture(request, response);
        }

        if ("updateCCCD".equalsIgnoreCase(choose)) {
            updateCccd(request, response);
        }

        if ("cartToBooking".equalsIgnoreCase(choose)) {
            try {
                // Nhận các thông số từ form
                int cartId = Integer.parseInt(request.getParameter("cartId"));
                int paidAmount = Integer.parseInt(request.getParameter("paidAmount"));
                String payDayStr = request.getParameter("payDay");  // Định dạng phải là: yyyy-MM-dd HH:mm:ss
                String status = request.getParameter("status");
                int customerId = Integer.parseInt(request.getParameter("customerId"));
                int paymentMethodId = Integer.parseInt(request.getParameter("paymentMethodId"));
                String startDateStr = request.getParameter("startDate");
                String endDateStr = request.getParameter("endDate");
                int roomNumber = Integer.parseInt(request.getParameter("roomNumber"));

                Customer selectCustomer = dal.CustomerDAO.getInstance().getCustomerByCustomerID(customerId);
                String cartStatus = request.getParameter("cartStatus");
                if (selectCustomer.getCCCD() == null) {
                    request.setAttribute("customerId", selectCustomer.getCustomerId());
                    request.setAttribute("fullname", selectCustomer.getFullName());
                    request.setAttribute("email", selectCustomer.getEmail());
                    if ("bookToday".equalsIgnoreCase(cartStatus)) {
                        request.setAttribute("error", "errorBookToday");
                        request.setAttribute("choose", "viewCustomerToday");
                        viewCustomerToday(request, response);
                    } else if ("bookFuture".equalsIgnoreCase(cartStatus)) {
                        request.setAttribute("error", "errorBookFuture");
                        request.setAttribute("choose", "viewCustomerFuture");
                        viewCustomerFuture(request, response);
                    }
                    return;
                }

                // Chuyển đổi chuỗi ngày
                LocalDate startDate = LocalDate.parse(startDateStr); // yyyy-MM-dd
                LocalDate endDate = LocalDate.parse(endDateStr);

                // Chuyển đổi payDay
                Timestamp payDay = Timestamp.valueOf(payDayStr); // yêu cầu định dạng yyyy-MM-dd HH:mm:ss

                // Lấy danh sách dịch vụ
                String[] serviceIds = request.getParameterValues("serviceId");
                String[] quantities = request.getParameterValues("quantity");
                String[] priceAtTimes = request.getParameterValues("priceAtTime");

                List<DetailService> detailServices = new ArrayList<>();
                if (serviceIds != null) {
                    for (int i = 0; i < serviceIds.length; i++) {
                        int serviceId = Integer.parseInt(serviceIds[i]);
                        int quantity = Integer.parseInt(quantities[i]);
                        int priceAtTime = Integer.parseInt(priceAtTimes[i]);

                        Service service = new Service();
                        service.setServiceId(serviceId);

                        DetailService ds = new DetailService();
                        ds.setService(service);
                        ds.setQuantity(quantity);
                        ds.setPriceAtTime(priceAtTime);

                        detailServices.add(ds);
                    }
                }

                // Tạo Booking
                Booking booking = new Booking();
                booking.setPayDay(new java.sql.Date(payDay.getTime())); // Giữ kiểu Date
                booking.setStatus(status);
                booking.setPaidAmount(paidAmount);

                Customer customer = new Customer();
                customer.setCustomerId(customerId);
                booking.setCustomer(customer);

                PaymentMethod pm = new PaymentMethod();
                pm.setPaymentMethodId(paymentMethodId);
                booking.setPaymentMethodCheckIn(pm);

                // Insert Booking
                int bookingId = dal.CartDAO.getInstance().insertCartToBooking(booking);

                // Insert BookingDetail
                BookingDetail bd = new BookingDetail();
                Booking b = new Booking();
                b.setBookingId(bookingId);
                bd.setBooking(b);
                Room r = new Room();
                r.setRoomNumber(roomNumber);
                bd.setRoom(r);
                bd.setStartDate(Date.valueOf(startDate));
                bd.setEndDate(Date.valueOf(endDate));
                bd.setTotalAmount(paidAmount);

                int bookingDetailId = dal.CartDAO.getInstance().insertCartToBookingDetail(bd);

                // Insert DetailService
                if (!detailServices.isEmpty()) {
                    dal.CartDAO.getInstance().insertDetailServices(bookingDetailId, detailServices);
                }

                //xóa cart
                dal.CartDAO.getInstance().deleteCartAndCartServiceById(cartId);

                // Thành công
                String ch = null;
                String pageStr = request.getParameter("page");

                if ("bookToday".equalsIgnoreCase(cartStatus)) {
                    ch = "viewCustomerToday";
                } else if ("bookFuture".equalsIgnoreCase(cartStatus)) {
                    ch = "viewCustomerFuture";
                }
                response.sendRedirect(request.getContextPath() + "/receptionist/cartToBooking?choose=" + ch + "&page=" + pageStr);

            } catch (Exception e) {
                e.printStackTrace();
                request.setAttribute("errorMsg", "Lỗi xử lý nhận phòng: " + e.getMessage());
                request.getRequestDispatcher("/View/Receptionist/CartToBooking.jsp").forward(request, response);
            }
        }
    }

    private void updateCccd(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String cartStatus = request.getParameter("cartStatus");
        String customerIdstr = request.getParameter("customerId");
        String email = request.getParameter("email");
        String fullname = request.getParameter("fullname");
        int customerId = Integer.parseInt(customerIdstr);
        String cccd = request.getParameter("cccd");
        
        System.out.println(cartStatus);
        System.out.println(customerIdstr);
        System.out.println(email);
        System.out.println(fullname);
        System.out.println(cccd);
        
        if (!cccd.matches("\\d{12}")) {
            request.setAttribute("fullname", fullname);
            request.setAttribute("email", email);
            request.setAttribute("customerId", customerId); // PHẢI có
            request.setAttribute("cccdError", "Căn cước công dân phải gồm đúng 12 chữ số.");

            request.setAttribute("error", "errorBookToday"); // hoặc errorBookFuture
            request.setAttribute("cartStatus", cartStatus);

            if ("bookToday".equalsIgnoreCase(cartStatus) || cartStatus == null) {
                request.setAttribute("error", "errorBookToday");
                request.setAttribute("choose", "viewCustomerToday");
                viewCustomerToday(request, response);
            } else if ("bookFuture".equalsIgnoreCase(cartStatus)) {
                request.setAttribute("error", "errorBookFuture");
                request.setAttribute("choose", "viewCustomerFuture");
                viewCustomerFuture(request, response);
            }
            return;
        }
        dal.CustomerDAO.getInstance().updateCustomerCCCD(cccd, customerId);
        if("bookToday".equalsIgnoreCase(cartStatus)){
            viewCustomerToday(request, response);
        }else if("bookFuture".equalsIgnoreCase(cartStatus)){
            viewCustomerFuture(request, response);
        }
    }

    private void viewCustomerToday(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Cart> allCarts = dal.CartDAO.getInstance().getAllCompletedCheckInCarts();
        List<Cart> filteredCarts = new ArrayList<>();

        LocalDateTime now = LocalDateTime.now();
        LocalDate today = now.toLocalDate();

        for (Cart cart : allCarts) {
            LocalDate cartStartDate = cart.getStartDate().toLocalDate();
            if (cartStartDate.equals(today) && now.toLocalTime().isAfter(LocalTime.NOON)) {
                filteredCarts.add(cart);
            }
        }

        paginateCartList(request, filteredCarts); // thêm dòng này

        request.setAttribute("cartStatus", "bookToday");
        request.getRequestDispatcher("/View/Receptionist/CartToBooking.jsp").forward(request, response);
    }

    private void viewCustomerFuture(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Cart> allCarts = dal.CartDAO.getInstance().getAllCompletedCheckInCarts();
        List<Cart> listCartCompleteBank = new ArrayList<>();

        LocalDateTime now = LocalDateTime.now();

        for (Cart cart : allCarts) {
            LocalDateTime startLimit = cart.getStartDate().toLocalDate().atTime(12, 0);
            LocalDateTime endLimit = cart.getEndDate().toLocalDate().atTime(9, 0);

            if (now.isAfter(startLimit) && now.isBefore(endLimit)) {
                listCartCompleteBank.add(cart);
            }
        }

        request.setAttribute("cartStatus", "bookFuture");
        paginateCartList(request, listCartCompleteBank);
        request.getRequestDispatcher("/View/Receptionist/CartToBooking.jsp").forward(request, response);
    }

    private void viewCustomerHasDuaDon(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Cart> allCarts = dal.CartDAO.getInstance().getAllCompletedCheckInCarts();
        List<Cart> listCartCompleteBank = new ArrayList<>();

        LocalDateTime now = LocalDateTime.now();

        for (Cart cart : allCarts) {
            LocalDateTime startLimit = cart.getStartDate().toLocalDate().atTime(12, 0);
            LocalDateTime endLimit = cart.getEndDate().toLocalDate().atTime(9, 0);

            if (now.isAfter(startLimit) && now.isBefore(endLimit)) {
                boolean hasServiceId2 = false;
                List<CartService> services = cart.getCartServices();

                if (services != null) {
                    for (CartService cs : services) {
                        if (cs.getService().getServiceId() == 2) {
                            hasServiceId2 = true;
                            break;
                        }
                    }
                }

                if (hasServiceId2) {
                    listCartCompleteBank.add(cart);
                }
            }
        }

        request.setAttribute("cartStatus", "view");
        paginateCartList(request, listCartCompleteBank);
        request.getRequestDispatcher("/View/Receptionist/CartToBooking.jsp").forward(request, response);
    }

    private void paginateCartList(HttpServletRequest request, List<Cart> fullList) {
        String pageStr = request.getParameter("page");
        int page = pageStr != null ? Integer.parseInt(pageStr) : 1;
        int recordsPerPage = 1;

        int totalRecords = fullList.size();
        int totalPages = (int) Math.ceil(totalRecords * 1.0 / recordsPerPage);

        if (page > totalPages && totalPages > 0) {
            page = totalPages;
        }

        int start = (page - 1) * recordsPerPage;
        int end = Math.min(start + recordsPerPage, totalRecords);
        List<Cart> paginatedList = fullList.subList(start, end);

        request.setAttribute("listCartCompleteBank", paginatedList);
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
