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
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import models.Booking;
import models.BookingDetail;
import models.DetailService;
import models.Room;

/**
 *
 * @author TuanPC
 */
public class VnpayReturn extends HttpServlet {

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
                    session.removeAttribute("paidAmount");
                    session.removeAttribute("bookingDetailId");
                    session.removeAttribute("listService");

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
                    int totalPrice = (int) session.getAttribute("totalPriceUpdate");
                    booking.setTotalPrice(totalPrice);
                    dal.BookingDAO.getInstance().updateBookingTotalPrice(booking);

                    int roomNumber = (int) session.getAttribute("roomNumber");
                    dal.RoomDAO.getInstance().updateRoomStatus(roomNumber, false);
                    request.setAttribute("pageChange", "checkOut");
                    session.removeAttribute("roomNumber");
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
