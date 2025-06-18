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
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import models.Booking;
import models.BookingDetail;

/**
 *
 * @author Hai Long
 */
@WebServlet(name = "Receipt", urlPatterns = {"/manager/receipt"})
public class Receipt extends HttpServlet {

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

        String service = request.getParameter("service");

        if (service == null) {
            service = "view";
        }

        if ("view".equals(service)) {

            List<Booking> bookingList = new ArrayList<>();

            HashMap<Booking, List<BookingDetail>> detailList = new HashMap<>();

            int detailTotal;

            int endPage = 0;

            int indexPage = Integer.parseInt(request.getParameter("page") == null ? "1" : request.getParameter("page"));

            String search = request.getParameter("search");

            if (search != null && !search.isEmpty()) {

                String startDate = request.getParameter("startDate");

                String endDate = request.getParameter("endDate");

                if (startDate == null || endDate == null || startDate.isEmpty() || endDate.isEmpty()) {
                    request.setAttribute("error", "Fill in all information of date");
                    bookingList = BookingDAO.getInstance().getBookingByCustomerId(46, indexPage, 2);
                    detailTotal = BookingDAO.getInstance().getBookingCountByCustomerId(46); 
                    
                    System.out.println(detailTotal);

                    endPage = detailTotal / 2;

                    if (detailTotal % 2 != 0) {
                        endPage++;
                    }

                } else {
                    Date start = Date.valueOf(startDate);
                    Date end = Date.valueOf(endDate);
                    System.out.println(start + " " + end);
                    bookingList = BookingDAO.getInstance().getBookingByCustomerIdAndDate(46, indexPage, 1, start, end);
                    detailTotal = BookingDAO.getInstance().getTotalBookingByCustomerIdAndDate(46, start, end);

                    endPage = detailTotal / 1;

                    if (detailTotal % 1 != 0) {
                        endPage++;
                    }
                    request.setAttribute("search", search);
                    
                    request.setAttribute("start", start);
                    
                    request.setAttribute("end", end);
                }
            } else {
                bookingList = BookingDAO.getInstance().getBookingByCustomerId(46, indexPage, 2);

                detailTotal = BookingDAO.getInstance().getBookingCountByCustomerId(46);

                endPage = detailTotal / 2;

                if (detailTotal % 2 != 0) {
                    endPage++;
                }
            }

            request.setAttribute("endPage", endPage);

            for (Booking booking : bookingList) {
                detailList.put(booking, BookingDetailDAO.getInstance().getBookingDetailByBookingId(booking));
            }

            request.setAttribute("bookList", bookingList);

            request.setAttribute("currentPage", indexPage);

            request.setAttribute("detailList", detailList);

            request.getRequestDispatcher("/View/manager/Receipt.jsp").forward(request, response);

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
