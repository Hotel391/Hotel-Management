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
import models.Booking;
import models.BookingDetail;
import models.Customer;
import models.DetailService;
import models.Room;

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
            String startDateStr = (String) session.getAttribute("startDate");
            String endDateStr = (String) session.getAttribute("endDate");

            Date startDate = Date.valueOf(startDateStr); 
            Date endDate = Date.valueOf(endDateStr);
            int roomNumber = Integer.parseInt((String)session.getAttribute("roomNumber"));

            Booking booking = new Booking();
            Customer customer = new Customer();
            customer.setCustomerId(customerId);
            booking.setCustomer(customer);
            booking.setPaidAmount(totalPrice);
            bookingId = dal.BookingDAO.getInstance().insertNewBooking(booking);

            BookingDetail bookingDetail = new BookingDetail();
            Room room = new Room();
            Booking booking1 = new Booking();
            bookingDetail.setStartDate(startDate);
            bookingDetail.setEndDate(endDate);
            room.setRoomNumber(roomNumber);
            bookingDetail.setRoom(room);
            booking1.setBookingId(bookingId);
            bookingDetail.setBooking(booking1);
            bookingDetail.setTotalAmount(totalPrice);
            int bookingDetailId = dal.BookingDetailDAO.getInstance().insertNewBookingDetail(bookingDetail);

            List<DetailService> listDetailService = (List<DetailService>) session.getAttribute("listService");
            if (listDetailService != null) {
                for (DetailService detail : listDetailService) {
                    int serviceId = detail.getService().getServiceId();
                    int quantity = detail.getQuantity();
                    int priceAtTime = detail.getService().getPrice();
                    int tottalPriceAtTimeOfOneService = quantity * priceAtTime;
                    dal.DetailServiceDAO.getInstance().insertDetailService(bookingDetailId,
                            serviceId, quantity, tottalPriceAtTimeOfOneService);
                }
            }
            session.removeAttribute("listService");
            session.removeAttribute("totalPrice");
            session.removeAttribute("customerId");
            session.removeAttribute("startDate");
            session.removeAttribute("endDate");
            session.removeAttribute("roomNumber");

        } else if (status.equals("checkOut")) {
            bookingId = (int) session.getAttribute("bookingId");
            BookingDetail bookingDetail = dal.BookingDetailDAO.getInstance().getBookingDetalByBookingId(bookingId);
            int PaidAmount = bookingDetail.getBooking().getPaidAmount();
            int totalAmount = bookingDetail.getTotalAmount();
            
            if (PaidAmount == totalAmount) {
                Booking booking =new Booking();
                booking.setBookingId(bookingId);
                booking.setTotalPrice(totalAmount);
                dal.BookingDAO.getInstance().updateBookingTotalPrice(booking);
                booking.setStatus("Completed CheckOut");
                dal.BookingDAO.getInstance().updateBookingStatus(booking);
                dal.RoomDAO.getInstance().updateRoomStatus(bookingDetail.getRoom().getRoomNumber(), false);
                resp.sendRedirect(req.getContextPath() + "/receptionist/receipt");
                return;
            }
            totalPrice = totalAmount - PaidAmount;
            session.setAttribute("roomNumber", bookingDetail.getRoom().getRoomNumber());
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
            vnp_TxnRef = bookingId + "_CO";
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

        cld.add(Calendar.MINUTE, 15);
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
}
