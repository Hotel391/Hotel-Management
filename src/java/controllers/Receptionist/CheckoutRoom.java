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

/**
 *
 * @author Hai Long
 */
@WebServlet(name = "CheckoutRoom", urlPatterns = {"/receptionist/checkoutRoom"})
public class CheckoutRoom extends HttpServlet {

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

        if (service == null) {
            service = "view";
        }

        if ("view".equals(service)) {

            long millis = System.currentTimeMillis();

            Date currentDate = new Date(millis);

            HashMap<BookingDetail, Customer> checkoutInfor = new LinkedHashMap<>();

            List<BookingDetail> checkoutList = BookingDetailDAO.getInstance().getBookingDetailByEndDate(currentDate);

            for (BookingDetail bookingDetail : checkoutList) {
                Customer customerCheckout = CustomerDAO.getInstance().getCustomerByBookingDetailId(bookingDetail.getBookingDetailId());
                checkoutInfor.put(bookingDetail, customerCheckout);
            }

            request.setAttribute("today", currentDate);

            request.setAttribute("checkoutList", checkoutInfor);

            request.getRequestDispatcher("/View/Receptionist/CheckoutRoom.jsp").forward(request, response);
        }

        if ("checkout".equals(service)) {
            int bookingId = Integer.parseInt(request.getParameter("bookingId"));

            Booking booking = BookingDAO.getInstance().getBookingByBookingId(bookingId);

            List<BookingDetail> detail = BookingDetailDAO.getInstance().getBookingDetailByBookingId(booking);

            int totalPrice = booking.getTotalPrice();

            for (BookingDetail bookingDetail : detail) {
                //write code to fine customer 10%/hour if they checkout late after 10am and before 1pm else later 1pm fine 1 day more do not use timestamp

                int typePrice = TypeRoomDAO.getInstance().getTypeRoomByRoomNumber(bookingDetail.getRoom().getRoomNumber()).getPrice();
                
                System.out.println("typePrice: " + typePrice);

                LocalDate endDate = bookingDetail.getEndDate().toLocalDate();
                LocalTime checkOutTime = LocalTime.now();
                LocalTime expectedCheckOutTime = LocalTime.of(10, 0);

                if (endDate.equals(LocalDate.now())) {
                    if (checkOutTime.isAfter(expectedCheckOutTime)) {
                        Duration duration = Duration.between(expectedCheckOutTime, checkOutTime);
                        long hoursLate = duration.toHours();
                        if (hoursLate <= 3) {
                            totalPrice += typePrice * 0.1 * hoursLate;
                            System.out.println("totalPrice before 1pm: " + totalPrice);
                        } else {
                            totalPrice += typePrice;
                            System.out.println("totalPrice   after 1pm: " + totalPrice);
                        }
                    }
                }
                System.out.println("totalPrice: " + totalPrice);
                booking.setTotalPrice(totalPrice);
//                BookingDAO.getInstance().updateBooking(booking);

            }

            session.setAttribute("bookingId", bookingId);

            session.setAttribute("status", "checkOut");

            response.sendRedirect(request.getContextPath() + "/payment");

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