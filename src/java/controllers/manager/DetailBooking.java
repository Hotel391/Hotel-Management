/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controllers.manager;

import dal.BookingDAO;
import dal.BookingDetailDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import models.Booking;
import models.BookingDetail;

/**
 *
 * @author Hai Long
 */
@WebServlet(name = "DetailBooking", urlPatterns = {"/manager/detailBooking"})
public class DetailBooking extends HttpServlet {

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
        
       int bookingId = Integer.parseInt(request.getParameter("bookingId"));
        
        int endPage = 0;
        
        int total = 0;
        
        int indexPage = Integer.parseInt(request.getParameter("page") == null ? "1" : request.getParameter("page"));

        Booking booking = BookingDAO.getInstance().getBookingByBookingId(bookingId);

        List<BookingDetail> detailList = new ArrayList<>();

        String searchRoom = request.getParameter("searchRoom");

        if (searchRoom != null && !searchRoom.isEmpty()) {
            int roomNumber = Integer.parseInt(searchRoom);
            total = BookingDetailDAO.getInstance().getBookingDetailByBookingIdAndRoomNumber(booking, roomNumber).size();
            detailList = BookingDetailDAO.getInstance().getBookingDetailByBookingIdAndRoomNumberPagination(booking, roomNumber, indexPage);
            request.setAttribute("search", "search");
            request.setAttribute("searchRoom", roomNumber);
        } else {
            total = BookingDetailDAO.getInstance().getBookingDetailByBookingId(booking).size();
            detailList = BookingDetailDAO.getInstance().getBookingDetailByBookingIdPagination(booking, indexPage);
        }

        endPage = total / 2;
        
        if(total % 2 != 0) endPage++;
        
        request.setAttribute("detailList", detailList);

        request.setAttribute("bookingInfo", booking);
        
        request.setAttribute("bookingId", bookingId);
        
        request.setAttribute("currentPage", indexPage);
        
        request.setAttribute("endPage", endPage);

        request.getRequestDispatcher("/View/Manager/DetailBooking.jsp").forward(request, response);
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
