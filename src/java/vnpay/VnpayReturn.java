/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package vnpay;

import dal.BookingDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import models.Booking;
import models.BookingDetail;
import models.Customer;
import models.DetailService;
import models.Room;
import models.TypeRoom;
import utility.EmailService;

/**
 *
 * @author TuanPC
 */
public class VnpayReturn extends HttpServlet {

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
        HttpSession session = request.getSession();

        Map<String, String> fields = new HashMap<>();
        for (Enumeration<String> params = request.getParameterNames(); params.hasMoreElements();) {
            String fieldName = URLEncoder.encode(params.nextElement(), StandardCharsets.US_ASCII.toString());
            String fieldValue = URLEncoder.encode(request.getParameter(fieldName), StandardCharsets.US_ASCII.toString());
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                fields.put(fieldName, fieldValue);
            }
        }

        String vnp_SecureHash = request.getParameter("vnp_SecureHash");
        fields.remove("vnp_SecureHashType");
        fields.remove("vnp_SecureHash");

        String signValue = Config.hashAllFields(fields);
        if (signValue.equals(vnp_SecureHash)) {
            String paymentCode = request.getParameter("vnp_TransactionNo");

            String vnp_TxnRef = request.getParameter("vnp_TxnRef"); // VD: "123_CI" hoặc "123_CO"
            String bookingIdStr = vnp_TxnRef.split("_")[0];
            int bookingId = Integer.parseInt(bookingIdStr);

            Booking booking = new Booking();
            booking.setBookingId((bookingId));
            String status = (String) session.getAttribute("status");

            boolean transSuccess = false;
            if ("checkIn".equals(status)) {
                if ("00".equals(request.getParameter("vnp_TransactionStatus"))) {
                    booking.setStatus("Completed CheckIn");
                    request.setAttribute("pageChange", "checkIn");
                    transSuccess = true;

                    Object paidAmountObj = session.getAttribute("paidAmount");
                    int paidAmount = 0;
                    if (paidAmountObj == null) {
                        throw new IllegalStateException("paidAmount is missing in session");
                    } else {
                        paidAmount = (int) paidAmountObj;
                    }
                    booking.setPaidAmount(paidAmount);
                    dal.BookingDAO.getInstance().updateBookingPaidAmount(booking);

                    //chỗ m cần đây hiếu
                    int customerId = (int) session.getAttribute("customerId");
                    Customer customer = dal.CustomerDAO.getInstance().getCustomerById(customerId);
                    String customerName = customer.getFullName();
                    String email = customer.getEmail();
                    String phone = customer.getPhoneNumber();
                    String startDateStr = (String) session.getAttribute("startDate");
                    String endDateStr = (String) session.getAttribute("endDate");
                    String[] roomNumbers = (String[]) session.getAttribute("roomNumbers");
                    Map<String, List<DetailService>> roomServicesMap
                            = (Map<String, List<DetailService>>) session.getAttribute("roomServicesMap");

                    // Sau khi đã insert BookingDetail và DetailService
                    // Tạo các list cần thiết
                    List<String> typeRoom = new ArrayList<>();
                    List<Integer> quantityTypeRoom = new ArrayList<>();
                    List<Integer> priceTypeRoom = new ArrayList<>();
                    List<String> services = new ArrayList<>();
                    List<Integer> serviceQuantity = new ArrayList<>();
                    List<Integer> servicePrice = new ArrayList<>();

                    // Tạo map đếm loại phòng
                    Map<String, Integer> typeCountMap = new LinkedHashMap<>();
                    Map<String, Integer> typePriceMap = new LinkedHashMap<>();
                    Map<String, Integer> serviceQuantityMap = new LinkedHashMap<>();
                    Map<String, Integer> servicePriceMap = new LinkedHashMap<>();

                    long numberOfNights = 1; // default
                    if (startDateStr != null && endDateStr != null) {
                        Date startDate = Date.valueOf(startDateStr);
                        Date endDate = Date.valueOf(endDateStr);
                        numberOfNights = (endDate.getTime() - startDate.getTime()) / (1000 * 60 * 60 * 24);
                    }

                    //đây là chỗ tìm và đếm
                    for (String roomNumberStr : roomNumbers) {
                        int roomNumber = Integer.parseInt(roomNumberStr);
                        Room room = dal.RoomDAO.getInstance().getRoomByRoomNumber(roomNumber);
                        int typeId = room.getTypeRoom().getTypeId();
                        TypeRoom type = dal.TypeRoomDAO.getInstance().getTypeRoomById(typeId);

                        String typeName = type.getTypeName();
                        int price = type.getPrice();

                        if (!typeCountMap.containsKey(typeName)) {
                            typeCountMap.put(typeName, 1);
                            typePriceMap.put(typeName, price);
                        } else {
                            typeCountMap.put(typeName, typeCountMap.get(typeName) + 1);
                        }
                    }

                    //chỗ ghi list đây
                    for (String typeName : typeCountMap.keySet()) {
                        int quantity = typeCountMap.get(typeName);
                        int unitPrice = typePriceMap.get(typeName);
                        int totalPrice = (int) (quantity * unitPrice * numberOfNights);
                        typeRoom.add(typeName);
                        quantityTypeRoom.add(quantity);
                        priceTypeRoom.add(totalPrice);
                    }

                    // Lấy danh sách dịch vụ
                    if (roomServicesMap != null) {
                        for (String roomNumber : roomNumbers) {
                            List<DetailService> list = roomServicesMap.get(roomNumber);
                            if (list != null) {
                                for (DetailService d : list) {
                                    String serviceName = d.getService().getServiceName();
                                    int quantity = d.getQuantity();
                                    int price = d.getService().getPrice();

                                    // Cộng dồn số lượng nếu đã có
                                    serviceQuantityMap.put(serviceName,
                                            serviceQuantityMap.getOrDefault(serviceName, 0) + quantity);

                                    // Ghi lại giá
                                    servicePriceMap.putIfAbsent(serviceName, price);
                                }
                            }
                        }
                    }

                    // đẩy vào list danh sách các dịch vụ của tất cả
                    for (String serviceName : serviceQuantityMap.keySet()) {
                        services.add(serviceName);
                        int quantity = serviceQuantityMap.get(serviceName);
                        int unitPrice = servicePriceMap.get(serviceName);
                        serviceQuantity.add(quantity);
                        servicePrice.add(quantity * unitPrice);
                    }

                    //tổng tiền
                    int total = (int) session.getAttribute("paidAmount");

                    //tên phương thức thanh toán
                    String paymentMethod = dal.BookingDAO.getInstance().getPaymentNameByBookingId(bookingId);

                    System.out.println("========== CHECK-IN SUMMARY ==========");

                    System.out.println("Customer Name: " + customerName);
                    System.out.println("Email: " + email);
                    System.out.println("Phone: " + phone);
                    System.out.println("Start Date: " + startDateStr);
                    System.out.println("End Date: " + endDateStr);
                    System.out.println("Total Paid: " + total);
                    System.out.println("Payment Method: " + paymentMethod);

                    System.out.println("---- ROOM TYPES ----");
                    for (int i = 0; i < typeRoom.size(); i++) {
                        System.out.println("Type: " + typeRoom.get(i)
                                + ", Quantity: " + quantityTypeRoom.get(i)
                                + ", Price: " + priceTypeRoom.get(i));
                    }

                    System.out.println("---- SERVICES ----");
                    for (int i = 0; i < services.size(); i++) {
                        System.out.println("Service: " + services.get(i)
                                + ", Quantity: " + serviceQuantity.get(i)
                                + ", Price: " + servicePrice.get(i));
                    }

                    System.out.println("========== END OF SUMMARY ==========");
                    Map<String, Object> data = new HashMap<>();
                    data.put("customerName", customerName);
                    data.put("email", email);
                    data.put("phone", phone);
                    data.put("startDate", startDateStr);
                    data.put("endDate", endDateStr);
                    data.put("total", total);
                    data.put("paymentMethod", paymentMethod);
                    data.put("typeRoom", typeRoom);
                    data.put("quantityTypeRoom", quantityTypeRoom);
                    data.put("priceTypeRoom", priceTypeRoom);
                    data.put("services", services);
                    data.put("serviceQuantity", serviceQuantity);
                    data.put("servicePrice", servicePrice);
                    emailExecutor.submit(() -> {
                        System.out.println("Sending email to " + email);
                        EmailService emailService = new EmailService();
                        emailService.sendEmail(email, "Confirm Checkin information", "checkin", data);
                    });

                    session.removeAttribute("paidAmount");
                    session.removeAttribute("bookingDetailId");
                    session.removeAttribute("listService");
                    session.removeAttribute("roomServicesMap");
                    session.removeAttribute("roomNumbers");

                } else {
                    booking.setStatus("Failed");
                    List<Integer> listBookingDetailId = (List<Integer>) session.getAttribute("listBookingDetailId");
                    if (listBookingDetailId != null) {
                        for (int bookingDetailId : listBookingDetailId) {
                            dal.DetailServiceDAO.getInstance().deleteDetailService(bookingDetailId);
                        }
                    }
                    dal.BookingDetailDAO.getInstance().deleteBookingDetailByBookingId(bookingId);
                    session.removeAttribute("listBookingDetailId");
                }
            } else {
                if ("00".equals(request.getParameter("vnp_TransactionStatus"))) {
                    booking.setStatus("Completed CheckOut");

                    // Cập nhật tổng tiền đã thanh toán
                    int totalPrice = (int) session.getAttribute("totalPriceUpdate");
                    booking.setTotalPrice(totalPrice);
                    dal.BookingDAO.getInstance().updateBookingTotalPrice(booking);

                    // Lấy lại thông tin Booking và Customer
                    Booking booking1 = dal.BookingDAO.getInstance().getBookingByBookingId(bookingId);
                    Customer customer = dal.CustomerDAO.getInstance().getCustomerById(booking1.getCustomer().getCustomerId());
                    String customerName = customer.getFullName();
                    String email = customer.getEmail();
                    String phone = customer.getPhoneNumber();
                    String paymentMethod = booking1.getPaymentMethod().getPaymentName();

                    // Lấy danh sách BookingDetail
                    List<BookingDetail> bookingDetails = dal.BookingDetailDAO.getInstance().getBookingDetailsByBookingId(bookingId);

                    long numberOfNights = 1;
                    if (!bookingDetails.isEmpty()) {
                        Date startDate = bookingDetails.get(0).getStartDate();
                        Date endDate = bookingDetails.get(0).getEndDate();
                        numberOfNights = (endDate.getTime() - startDate.getTime()) / (1000 * 60 * 60 * 24);
                    }

                    // Tính loại phòng
                    Map<String, Integer> typeCountMap = new LinkedHashMap<>();
                    Map<String, Integer> typePriceMap = new LinkedHashMap<>();

                    for (BookingDetail bd : bookingDetails) {
                        int roomNumber = bd.getRoom().getRoomNumber();

                        // Cập nhật trạng thái phòng (set về false - cần dọn)
                        dal.RoomDAO.getInstance().updateRoomStatus(roomNumber, false);

                        Room room = dal.RoomDAO.getInstance().getRoomByRoomNumber(roomNumber);
                        TypeRoom type = room.getTypeRoom();
                        String typeName = type.getTypeName();
                        int unitPrice = type.getPrice();

                        typeCountMap.put(typeName, typeCountMap.getOrDefault(typeName, 0) + 1);
                        typePriceMap.put(typeName, unitPrice);
                    }

                    //dữ liệu để gửi email
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

                    // Tính tổng tiền dịch vụ
                    for (BookingDetail bd : bookingDetails) {
                        int bdId = bd.getBookingDetailId();
                        List<DetailService> services = dal.DetailServiceDAO.getInstance().getServicesByBookingDetailId(bdId);
                        for (DetailService d : services) {
                            totalServicePrice += d.getPriceAtTime();
                        }
                    }

                    Map<String, Object> data = new HashMap<>();
                    data.put("customerName", customerName);
                    data.put("email", email);
                    data.put("phone", phone);
                    data.put("paymentMethod", paymentMethod);
                    data.put("typeRoom", typeRoom);
                    data.put("quantityTypeRoom", quantityTypeRoom);
                    data.put("priceTypeRoom", priceTypeRoom);
                    data.put("services", Collections.EMPTY_LIST);
                    data.put("serviceQuantity", Collections.EMPTY_LIST);
                    data.put("servicePrice", Collections.EMPTY_LIST);
                    data.put("paymentMethod", paymentMethod);
                    data.put("fineMoney", 0);
                    data.put("totalRoomPrice", totalRoomPrice);
                    data.put("totalServicePrice", totalServicePrice);

                    emailExecutor.submit(() -> {
                        System.out.println("Sending email to " + email);
                        EmailService emailService = new EmailService();
                        emailService.sendEmail(email, "Confirm Checkin information", "checkin", data);
                    });

                    request.setAttribute("pageChange", "checkOut");
                    session.removeAttribute("listRoomNumber");
                    transSuccess = true;
                } else {
                    booking.setStatus("Completed CheckIn");
                }
            }

            session.removeAttribute("status");
            dal.BookingDAO.getInstance().updateBookingStatus(booking);
            request.setAttribute("transResult", transSuccess);

            request.getRequestDispatcher("/paymentResult.jsp").forward(request, response);
        } else {
            //RETURN PAGE ERROR
            System.out.println("GD KO HOP LE (invalid signature)");

            // Thử lấy bookingId từ vnp_TxnRef
            String vnp_TxnRef = request.getParameter("vnp_TxnRef");
            if (vnp_TxnRef != null && vnp_TxnRef.contains("_")) {
                try {
                    String bookingIdStr = vnp_TxnRef.split("_")[0];
                    int bookingId = Integer.parseInt(bookingIdStr);

                    List<Integer> listBookingDetailId = (List<Integer>) session.getAttribute("listBookingDetailId");
                    if (listBookingDetailId != null) {
                        for (int bookingDetailId : listBookingDetailId) {
                            dal.DetailServiceDAO.getInstance().deleteDetailService(bookingDetailId);
                        }
                    }
                    dal.BookingDetailDAO.getInstance().deleteBookingDetailByBookingId(bookingId);
                    session.removeAttribute("listBookingDetailId");

                    Booking booking = new Booking();
                    booking.setBookingId(bookingId);
                    booking.setStatus("Failed");

                    dal.BookingDAO.getInstance().updateBookingStatus(booking);
                    request.setAttribute("transResult", false);
                } catch (NumberFormatException e) {
                    System.out.println("Cannot parse booking ID from vnp_TxnRef: " + vnp_TxnRef);
                }
            }
            request.getRequestDispatcher("/paymentResult.jsp").forward(request, response);
        }

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
