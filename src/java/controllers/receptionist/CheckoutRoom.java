/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controllers.receptionist;

import dal.BookingDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import models.BookingDetail;
import models.Customer;
import dal.BookingDetailDAO;
import dal.CustomerDAO;
import dal.TypeRoomDAO;
import jakarta.servlet.http.HttpSession;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import models.Booking;
import java.util.Calendar;
import java.time.LocalTime;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import models.DetailService;
import models.PaymentMethod;
import models.Room;
import models.TypeRoom;
import utility.EmailService;
import utility.email_factory.EmailTemplateFactory;

/**
 *
 * @author Hai Long
 */
@WebServlet(name = "CheckoutRoom", urlPatterns = {"/receptionist/checkoutRoom"})
public class CheckoutRoom extends HttpServlet {

    private static final ExecutorService emailExecutor = Executors.newFixedThreadPool(10);

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        HttpSession session = request.getSession(true);

        String service = request.getParameter("service");

        System.out.println("service: " + service);

        System.out.println("checkout");

        if (service == null) {
            service = "view";
        }

        if ("view".equals(service)) {

            showCheckoutRoom(request, response);
        }

        if ("checkout".equals(service)) {

            String paymentMethodSelected = request.getParameter("paymentMethod");

            int bookingId = Integer.parseInt(request.getParameter("bookingId"));

            System.out.println("bookingId: " + bookingId);

            Booking bookingSelected = BookingDAO.getInstance().getBookingByBookingId(bookingId);

            List<BookingDetail> detail = BookingDetailDAO.getInstance().getBookingDetailByBookingId(bookingSelected);

            int totalPrice = bookingSelected.getTotalPrice();
            
            int fineMoney = 0;

            for (BookingDetail bookingDetail : detail) {

                int typePrice = TypeRoomDAO.getInstance().getTypeRoomByRoomNumber(bookingDetail.getRoom().getRoomNumber()).getPrice();

                System.out.println("typePrice: " + typePrice);

                LocalDate endDate = bookingDetail.getEndDate().toLocalDate();
                LocalTime checkOutTime = LocalTime.now();
                LocalTime expectedCheckOutTime = LocalTime.of(9, 0);

                if (endDate.equals(LocalDate.now())) {
                    if (checkOutTime.isAfter(expectedCheckOutTime)) {
                        Duration duration = Duration.between(expectedCheckOutTime, checkOutTime);
                        long hoursLate = duration.toHours();
                        if (hoursLate <= 3) {
                            fineMoney += typePrice * 0.1 * hoursLate;
                            System.out.println("fineMoney before 1pm: " + fineMoney);
                        } else {
                            fineMoney += typePrice;
                            System.out.println("fineMoney after 1pm: " + fineMoney);
                        }
                    }
                }
                //fineMoney
                session.setAttribute("fineMoney", fineMoney);
                //bookingSelected.setTotalPrice(totalPrice);
                //BookingDAO.getInstance().updateTotalPrice(bookingSelected);

            }

            String unPaidAmount = request.getParameter("unPaidAmount");
            if (unPaidAmount != null && !unPaidAmount.isEmpty()) {
                session.setAttribute("bookingId", bookingId);
                session.setAttribute("status", "checkOut");
                response.sendRedirect(request.getContextPath() + "/payment");
                return;
            }

            if ("default".equals(paymentMethodSelected)) {
                int customerId = Integer.parseInt(request.getParameter("customerId"));
                System.out.println("customerId: " + customerId);
                String customerName = CustomerDAO.getInstance().getCustomerByCustomerID(customerId).getFullName();
                request.setAttribute("paymentMethodError", "Vui lòng chọn phương thức thanh toán cho khách hàng: " + customerName);
                showCheckoutRoom(request, response);
                return;
            }

            if ("online".equals(paymentMethodSelected)) {

                System.out.println("done to online");

                session.setAttribute("bookingId", bookingId);

                session.setAttribute("status", "checkOut");

                response.sendRedirect(request.getContextPath() + "/payment");

            } else {
                List<Integer> listRoomNumbers = new ArrayList<>();

                List<BookingDetail> bookingDetail = dal.BookingDetailDAO.getInstance().getBookingDetailsByBookingId(bookingId);
                int paidAmount = bookingDetail.get(0).getBooking().getPaidAmount();

                int totalAmount = 0;
                for (BookingDetail detailItem : bookingDetail) {
                    totalAmount += detailItem.getTotalAmount();
                    listRoomNumbers.add(detailItem.getRoom().getRoomNumber());
                }

                Booking booking = new Booking();
                PaymentMethod paymentMethodCheckOut = new PaymentMethod();
                booking.setBookingId(bookingId);
                booking.setTotalPrice(totalAmount);
                paymentMethodCheckOut.setPaymentMethodId(2);
                booking.setPaymentMethod(paymentMethodCheckOut);
                booking.setStatus("Completed CheckOut");

                dal.BookingDAO.getInstance().updateBookingTotalPrice(booking);
                dal.BookingDAO.getInstance().updateBookingStatus(booking);

                // Tính số đêm
                long numberOfNights = 1;
                if (!bookingDetail.isEmpty()) {
                    Date startDate = bookingDetail.get(0).getStartDate();
                    Date endDate = bookingDetail.get(0).getEndDate();
                    numberOfNights = (endDate.getTime() - startDate.getTime()) / (1000 * 60 * 60 * 24);
                }

                // Tính loại phòng
                Map<String, Integer> typeCountMap = new LinkedHashMap<>();
                Map<String, Integer> typePriceMap = new LinkedHashMap<>();

                for (BookingDetail bd : bookingDetail) {
                    int roomNumber = bd.getRoom().getRoomNumber();

                    // Cập nhật trạng thái phòng (cần dọn)
                    dal.RoomDAO.getInstance().updateRoomStatus(roomNumber, false);

                    Room room = dal.RoomDAO.getInstance().getRoomByRoomNumber(roomNumber);
                    TypeRoom type = room.getTypeRoom();
                    String typeName = type.getTypeName();
                    int unitPrice = type.getPrice();

                    typeCountMap.put(typeName, typeCountMap.getOrDefault(typeName, 0) + 1);
                    typePriceMap.put(typeName, unitPrice);
                }

                //các list cần để gửi email
                Booking booking1 = dal.BookingDAO.getInstance().getBookingByBookingId(bookingId);
                System.out.println("paymentmethod email: " + booking1.getPaymentMethodCheckIn().getPaymentName());
                Customer customer = dal.CustomerDAO.getInstance().getCustomerById(booking1.getCustomer().getCustomerId());
                String customerName = customer.getFullName();
                String email = customer.getEmail();
                String phone = customer.getPhoneNumber();
                String paymentMethod = booking1.getPaymentMethodCheckIn().getPaymentName();

                List<String> typeRoom = new ArrayList<>();
                List<Integer> quantityTypeRoom = new ArrayList<>();
                List<Integer> priceTypeRoom = new ArrayList<>();
                int totalRoomPrice = 0;
                int totalServicePrice = 0;

                for (String typeName : typeCountMap.keySet()) {
                    int quantity = typeCountMap.get(typeName);
                    int unitPrice = typePriceMap.get(typeName);
                    int total = quantity * unitPrice * (int) numberOfNights;
                    typeRoom.add(typeName);
                    quantityTypeRoom.add(quantity);
                    priceTypeRoom.add(total);
                    totalRoomPrice += total;
                }

                // Tính tổng dịch vụ
                Map<String, Integer> serviceQuantityMap = new LinkedHashMap<>();
                Map<String, Integer> servicePriceMap = new LinkedHashMap<>();

                for (BookingDetail bd : bookingDetail) {
                    int bdId = bd.getBookingDetailId();
                    List<DetailService> servicesList = dal.DetailServiceDAO.getInstance().getServicesByBookingDetailId(bdId);
                    for (DetailService d : servicesList) {
                        String serviceName = d.getService().getServiceName();
                        int quantity = d.getQuantity();
                        int priceAtTime = d.getPriceAtTime();

                        serviceQuantityMap.put(serviceName, serviceQuantityMap.getOrDefault(serviceName, 0) + quantity);
                        servicePriceMap.put(serviceName, servicePriceMap.getOrDefault(serviceName, 0) + priceAtTime);
                        totalServicePrice += priceAtTime;
                    }
                }

                List<String> services = new ArrayList<>();
                List<Integer> serviceQuantity = new ArrayList<>();
                List<Integer> servicePrice = new ArrayList<>();
                for (String name : serviceQuantityMap.keySet()) {
                    services.add(name);
                    serviceQuantity.add(serviceQuantityMap.get(name));
                    servicePrice.add(servicePriceMap.get(name));
                }

                Map<String, Object> data = new HashMap<>();
                data.put("customerName", customerName);
                data.put("email", email);
                data.put("phone", phone);
                data.put("paymentMethod", paymentMethod);
                data.put("typeRoom", typeRoom);
                data.put("quantityTypeRoom", quantityTypeRoom);
                data.put("priceTypeRoom", priceTypeRoom);
                data.put("services", services);
                data.put("serviceQuantity", serviceQuantity);
                data.put("servicePrice", servicePrice);
                data.put("paymentMethod", paymentMethod);
                data.put("fineMoney", 0);
                data.put("totalRoomPrice", totalRoomPrice);
                data.put("totalServicePrice", totalServicePrice);

                emailExecutor.submit(() -> {
                    EmailService emailService = new EmailService();
                    emailService.sendEmail(email, "Receipt information", EmailTemplateFactory.EmailType.RECEIPT, data);
                });

                response.sendRedirect(request.getContextPath() + "/receptionist/receipt");

            }

        }

    }

    private void showCheckoutRoom(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        long millis = System.currentTimeMillis();

        System.out.println("view");

        Date currentDate = new Date(millis);
        

        HashMap<Booking, List<BookingDetail>> checkoutList = new LinkedHashMap<>();

        String phoneSearch = request.getParameter("phoneSearch");
                
        List<Booking> bookingCheckout = BookingDAO.getInstance().getBookingCheckout(phoneSearch);

        for (Booking booking : bookingCheckout) {
            List<BookingDetail> detailList = BookingDetailDAO.getInstance().getBookingDetailByBookingId(booking);
            checkoutList.put(booking, detailList);
        }

        request.setAttribute("today", currentDate);

        request.setAttribute("checkoutList", checkoutList);

        request.getRequestDispatcher("/View/Receptionist/CheckoutRoom.jsp").forward(request, response);
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
