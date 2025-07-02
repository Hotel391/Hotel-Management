/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controllers.receptionist;

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
import jakarta.servlet.http.HttpSession;
import java.util.LinkedHashMap;

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
            
            System.out.println(checkoutList);
            
            for (BookingDetail bookingDetail : checkoutList) {
                Customer customerCheckout = CustomerDAO.getInstance().getCustomerByBookingDetailId(bookingDetail.getBookingDetailId());
                checkoutInfor.put(bookingDetail, customerCheckout);
            }
            
            System.out.println(checkoutInfor);
            
            request.setAttribute("today", currentDate);
            
            request.setAttribute("checkoutList", checkoutInfor);
            
            request.getRequestDispatcher("/View/Receptionist/CheckoutRoom.jsp").forward(request, response);
        }
        
        if ("checkout".equals(service)) {
            int bookingId = Integer.parseInt(request.getParameter("bookingId"));
            
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
