/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vnpay;

import dal.BookingDAO;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.sql.Date;
import java.util.LinkedHashMap;
import java.util.Random;
import models.Booking;
import models.BookingDetail;
import models.Customer;
import models.DetailService;
import models.Room;
import models.TypeRoom;

/**
 *
 * @author CTT VNPAY
 */
public class ajaxServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String bankCode = req.getParameter("bankCode");

        HttpSession session = req.getSession();
        String status = (String) session.getAttribute("status");
        int totalPrice = 0;
        int bookingId = 0;

        if (status.equals("checkIn")) {
            Object obj = session.getAttribute("totalPrice");
            if (obj instanceof Double) {
                totalPrice = ((Double) obj).intValue();
            }
            int customerId = (int) session.getAttribute("customerId");
            Booking booking = new Booking();
            Customer customer = new Customer();
            customer.setCustomerId(customerId);
            booking.setCustomer(customer);
            session.setAttribute("paidAmount", totalPrice);
            bookingId = dal.BookingDAO.getInstance().insertNewBooking(booking);

            String startDateStr = (String) session.getAttribute("startDate");
            String endDateStr = (String) session.getAttribute("endDate");
            Date startDate = Date.valueOf(startDateStr);
            Date endDate = Date.valueOf(endDateStr);
            String[] roomNumbers = (String[]) session.getAttribute("roomNumbers");
            long numberOfNights = (endDate.getTime() - startDate.getTime()) / (1000 * 60 * 60 * 24);

            // Lấy Map roomServicesMap
            Map<String, List<DetailService>> roomServicesMap
                    = (Map<String, List<DetailService>>) session.getAttribute("roomServicesMap");
            List<Integer> listBookingDetailId = new ArrayList<>();
            for (String roomNumberStr : roomNumbers) {
                int roomNumber = Integer.parseInt(roomNumberStr);
                // Lấy giá phòng theo từng room
                Room roomObj = dal.RoomDAO.getInstance().getRoomByRoomNumber(roomNumber);
                int typeId = roomObj.getTypeRoom().getTypeId();
                double roomPrice = dal.TypeRoomDAO.getInstance().getPriceByTypeId(typeId);
                double totalRoomPrice = roomPrice * numberOfNights;

                // Tính tổng tiền dịch vụ (nếu có)
                int totalServiceCost = 0;
                if (roomServicesMap != null && roomServicesMap.containsKey(roomNumberStr)) {
                    List<DetailService> serviceList = roomServicesMap.get(roomNumberStr);
                    for (DetailService detail : serviceList) {
                        int quantity = detail.getQuantity();
                        int priceAtTime = detail.getService().getPrice();
                        int total = quantity * priceAtTime;
                        totalServiceCost += total;
                    }
                }

                // Tổng tiền phòng + dịch vụ
                int totalAmount = (int) Math.round(totalRoomPrice) + totalServiceCost;

                // Tạo BookingDetail cho từng phòng
                BookingDetail bookingDetail = new BookingDetail();
                Room room = new Room();
                Booking booking1 = new Booking();
                bookingDetail.setStartDate(startDate);
                bookingDetail.setEndDate(endDate);
                room.setRoomNumber(roomNumber);
                bookingDetail.setRoom(room);
                booking1.setBookingId(bookingId);
                bookingDetail.setBooking(booking1);
                bookingDetail.setTotalAmount((int) totalAmount);
                int bookingDetailId = dal.BookingDetailDAO.getInstance().insertNewBookingDetail(bookingDetail);
                listBookingDetailId.add(bookingDetailId);

                // Insert dịch vụ cho từng phòng (nếu có)
                if (roomServicesMap != null && roomServicesMap.containsKey(roomNumberStr)) {
                    List<DetailService> serviceList = roomServicesMap.get(roomNumberStr);
                    for (DetailService detail : serviceList) {
                        int serviceId = detail.getService().getServiceId();
                        int quantity = detail.getQuantity();
                        int priceAtTime = detail.getService().getPrice();
                        int total = quantity * priceAtTime;
                        dal.DetailServiceDAO.getInstance().insertDetailService(
                                bookingDetailId, serviceId, quantity, total
                        );
                    }
                }
            }

            session.setAttribute("listBookingDetailId", listBookingDetailId);

            session.removeAttribute("totalPrice");

        } else if (status.equals("checkOut")) {
            List<Integer> listRoomNumbers = new ArrayList<>();

            bookingId = (int) session.getAttribute("bookingId");
            List<BookingDetail> bookingDetail = dal.BookingDetailDAO.getInstance().getBookingDetailsByBookingId(bookingId);
            int paidAmount = bookingDetail.get(0).getBooking().getPaidAmount();

            int totalAmount = 0;
            for (BookingDetail detail : bookingDetail) {
                totalAmount += detail.getTotalAmount();
                listRoomNumbers.add(detail.getRoom().getRoomNumber());
            }

            if (paidAmount == totalAmount) {
                Booking booking = new Booking();
                booking.setBookingId(bookingId);
                booking.setTotalPrice(totalAmount);
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

                for (BookingDetail bd : bookingDetail) {
                    int bdId = bd.getBookingDetailId();
                    List<DetailService> services = dal.DetailServiceDAO.getInstance().getServicesByBookingDetailId(bdId);
                    for (DetailService d : services) {
                        totalServicePrice += d.getPriceAtTime();
                    }
                }

                resp.sendRedirect(req.getContextPath() + "/receptionist/receipt");
                return;
            }
            session.setAttribute("listRoomNumber", listRoomNumbers);
            session.setAttribute("totalPriceUpdate", totalAmount);
            session.removeAttribute("bookingId");
        }

        String vnp_Version = "2.1.0"; // Phiên bản API của VNPay
        String vnp_Command = "pay"; // Lệnh yêu cầu, ở đây là thanh toán
        String orderType = "other"; // Loại hàng hóa

        long amount = (long) (totalPrice * 100);
        String vnp_TxnRef = null;
        if ("checkIn".equals(status)) {
            vnp_TxnRef = bookingId + "_CI";//dky ma rieng
        } else {
            String CO = generateRandomCodeWithUnderscore(6);
            vnp_TxnRef = bookingId + CO;
        }
        String vnp_IpAddr = Config.getIpAddress(req); // Lấy địa chỉ IP của client

        String vnp_TmnCode = Config.vnp_TmnCode;

        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", vnp_Version);
        vnp_Params.put("vnp_Command", vnp_Command);
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.put("vnp_Amount", String.valueOf(amount));
        vnp_Params.put("vnp_CurrCode", "VND");

        if (bankCode != null && !bankCode.isEmpty()) {
            vnp_Params.put("vnp_BankCode", bankCode);
        }
        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_OrderInfo", "Thanh toan don hang:" + vnp_TxnRef);
        vnp_Params.put("vnp_OrderType", orderType);

        String locate = req.getParameter("language");
        if (locate != null && !locate.isEmpty()) {
            vnp_Params.put("vnp_Locale", locate);
        } else {
            vnp_Params.put("vnp_Locale", "vn");
        }
        vnp_Params.put("vnp_ReturnUrl", Config.vnp_ReturnUrl);
        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);

        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

        cld.add(Calendar.MINUTE, 1);
        String vnp_ExpireDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

        List fieldNames = new ArrayList(vnp_Params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        Iterator itr = fieldNames.iterator();
        while (itr.hasNext()) {
            String fieldName = (String) itr.next();
            String fieldValue = (String) vnp_Params.get(fieldName);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                //Build hash data
                hashData.append(fieldName);
                hashData.append('=');
                hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                //Build query
                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()));
                query.append('=');
                query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                if (itr.hasNext()) {
                    query.append('&');
                    hashData.append('&');
                }
            }
        }
        String queryUrl = query.toString();
        String vnp_SecureHash = Config.hmacSHA512(Config.secretKey, hashData.toString());
        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
        String paymentUrl = Config.vnp_PayUrl + "?" + queryUrl;
        resp.sendRedirect(paymentUrl);
    }

    public static String generateRandomCodeWithUnderscore(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder sb = new StringBuilder("_"); // dấu _
        Random random = new Random();

        for (int i = 0; i < length; i++) {
            sb.append(characters.charAt(random.nextInt(characters.length())));
        }

        return sb.toString();
    }
}
