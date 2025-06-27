package controllers.customer;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.sql.Date;

/**
 *
 * @author HieuTT
 */
@WebServlet(name="SearchRoom", urlPatterns={"/searchRoom"})
public class SearchRoom extends HttpServlet {
   
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        String checkin=request.getParameter("checkin");
        Date checkinDate = getDate(checkin);

        String checkout=request.getParameter("checkout");
        Date checkoutDate = getDate(checkout);

        
    } 

    private Date getDate(String dateStr) {
        if (dateStr == null || dateStr.isEmpty()) {
            return java.sql.Date.valueOf(java.time.LocalDate.now());
        } else {
            return Date.valueOf(dateStr);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
