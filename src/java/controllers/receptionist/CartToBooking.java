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

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String choose = request.getParameter("choose");
        if (choose == null) {
            choose = "viewCustomerToday";
        }

        if ("viewCustomerToday".equals(choose)) {
            List<Cart> allCarts = dal.CartDAO.getInstance().getAllCompletedCheckInCarts();
            List<Cart> listCartCompleteBank = new ArrayList<>();

            LocalDateTime now = LocalDateTime.now();
            LocalDate today = now.toLocalDate();

            for (Cart cart : allCarts) {
                LocalDate cartStartDate = cart.getStartDate().toLocalDate();

                // Chỉ lấy cart có startDate là hôm nay và sau 12h trưa
                if (cartStartDate.equals(today) && now.toLocalTime().isAfter(LocalTime.NOON)) {
                    listCartCompleteBank.add(cart);
                }
            }

            request.setAttribute("cartStatus", "bookToday");
            request.setAttribute("listCartCompleteBank", listCartCompleteBank);
            request.getRequestDispatcher("/View/Receptionist/CartToBooking.jsp").forward(request, response);
        }

        if ("viewCustomerHasDuaDon".equalsIgnoreCase(choose)) {
            List<Cart> allCarts = dal.CartDAO.getInstance().getAllCompletedCheckInCarts();
            List<Cart> listCartCompleteBank = new ArrayList<>();

            LocalDateTime now = LocalDateTime.now();

            for (Cart cart : allCarts) {
                LocalDateTime startLimit = cart.getStartDate().toLocalDate().atTime(12, 0); // 12h trưa startDate
                LocalDateTime endLimit = cart.getEndDate().toLocalDate().atTime(9, 0);     // 9h sáng endDate

                System.out.println("CartId: " + cart.getCartId());
                System.out.println("Start Limit: " + startLimit);
                System.out.println("End Limit: " + endLimit);
                System.out.println("Now: " + now);

                // Điều kiện thời gian
                if (now.isAfter(startLimit) && now.isBefore(endLimit)) {
                    boolean hasServiceId2 = false;

                    // Lấy danh sách dịch vụ trong giỏ
                    List<CartService> services = cart.getCartServices(); // đảm bảo cart có setCartServices khi truy vấn

                    if (services != null) {
                        for (CartService cs : services) {
                            if (cs.getService().getServiceId() == 2) {
                                hasServiceId2 = true;
                                break;
                            }
                        }
                    }

                    if (hasServiceId2) {
                        System.out.println("==> Được thêm (có serviceId = 2)");
                        listCartCompleteBank.add(cart);
                    } else {
                        System.out.println("==> Không có serviceId = 2");
                    }

                } else {
                    System.out.println("==> Không thỏa điều kiện thời gian");
                }
            }

            request.setAttribute("cartStatus", "view");
            request.setAttribute("listCartCompleteBank", listCartCompleteBank);
            request.getRequestDispatcher("/View/Receptionist/CartToBooking.jsp").forward(request, response);
        }

        if ("viewCustomerFuture".equals(choose)) {
            List<Cart> allCarts = dal.CartDAO.getInstance().getAllCompletedCheckInCarts();
            List<Cart> listCartCompleteBank = new ArrayList<>();

            LocalDateTime now = LocalDateTime.now();

            for (Cart cart : allCarts) {
                LocalDateTime startLimit = cart.getStartDate().toLocalDate().atTime(12, 0); // 12h trưa startDate
                LocalDateTime endLimit = cart.getEndDate().toLocalDate().atTime(9, 0);     // 9h sáng endDate

                System.out.println("CartId: " + cart.getCartId());
                System.out.println("Start Limit: " + startLimit);
                System.out.println("End Limit: " + endLimit);
                System.out.println("Now: " + now);

                if (now.isAfter(startLimit) && now.isBefore(endLimit)) {
                    System.out.println("==> Được thêm");
                    listCartCompleteBank.add(cart);
                } else {
                    System.out.println("==> Không thỏa điều kiện");
                }
            }

            request.setAttribute("cartStatus", "bookFuture");
            request.setAttribute("listCartCompleteBank", listCartCompleteBank);
            request.getRequestDispatcher("/View/Receptionist/CartToBooking.jsp").forward(request, response);
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
                String cartStatus = request.getParameter("cartStatus");
                if("bookToday".equalsIgnoreCase(cartStatus)){
                    ch = "viewCustomerToday";
                }else if("bookFuture".equalsIgnoreCase(cartStatus)){
                    ch = "viewCustomerFuture";
                }
                response.sendRedirect(request.getContextPath() + "/receptionist/cartToBooking?choose=" + ch);

            } catch (Exception e) {
                e.printStackTrace();
                request.setAttribute("errorMsg", "Lỗi xử lý nhận phòng: " + e.getMessage());
                request.getRequestDispatcher("/View/Receptionist/CartToBooking.jsp").forward(request, response);
            }
        }
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
