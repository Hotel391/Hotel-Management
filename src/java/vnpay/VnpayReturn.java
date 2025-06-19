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
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import models.Booking;

/**
 *
 * @author HP
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

        try (PrintWriter out = response.getWriter()) {
            Map fields = new HashMap();
            for (Enumeration params = request.getParameterNames(); params.hasMoreElements();) {
                String fieldName = URLEncoder.encode((String) params.nextElement(), StandardCharsets.US_ASCII.toString());
                String fieldValue = URLEncoder.encode(request.getParameter(fieldName), StandardCharsets.US_ASCII.toString());
                if ((fieldValue != null) && (fieldValue.length() > 0)) {
                    fields.put(fieldName, fieldValue);
                }
            }

            String vnp_SecureHash = request.getParameter("vnp_SecureHash");
            if (fields.containsKey("vnp_SecureHashType")) {
                fields.remove("vnp_SecureHashType");
            }
            if (fields.containsKey("vnp_SecureHash")) {
                fields.remove("vnp_SecureHash");
            }
            String signValue = Config.hashAllFields(fields);
            if (signValue.equals(vnp_SecureHash)) {
                String paymentCode = request.getParameter("vnp_TransactionNo");

                String bookingId = request.getParameter("vnp_TxnRef");

                Booking booking = new Booking();
                booking.setBookingId(Integer.parseInt(bookingId));
                String status = (String) session.getAttribute("status");
                boolean transSuccess = false;

                if (status.equals("checkIn")) {
                    if ("00".equals(request.getParameter("vnp_TransactionStatus"))) {
                        booking.setStatus("Completed CheckIn");
                        transSuccess = true;
                    } else {
                        booking.setStatus("Failed");
                    }
                } else {
                    if ("00".equals(request.getParameter("vnp_TransactionStatus"))) {
                        booking.setStatus("Completed CheckOut");
                        int totalPrice = (int) session.getAttribute("totalPriceUpdate");
                        booking.setTotalPrice(totalPrice);
                        dal.BookingDAO.getInstance().updateBookingTotalPrice(booking);
                        transSuccess = true;
                    } else {
                        booking.setStatus("Completed CheckIn");
                    }
                }
//                System.out.println(booking.getStatus());
                session.removeAttribute("status");
                dal.BookingDAO.getInstance().updateBookingStatus(booking);
//                response.sendRedirect(request.getContextPath() + "/receptionist/receipt");
                request.setAttribute("transResult", transSuccess);
                request.getRequestDispatcher("paymentResult.jsp").forward(request, response);
            } else {
                //RETURN PAGE ERROR
                System.out.println("GD KO HOP LE (invalid signature)");
            }
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
