package controllers.customer;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.sql.Date;
import models.TypeRoom;

/**
 *
 * @author HieuTT
 */
@WebServlet(name="DetailRoom", urlPatterns={"/detailRoom"})
public class DetailRoom extends HttpServlet {
   
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        int typeId = request.getParameter("typeRoomId") != null ? Integer.parseInt(request.getParameter("typeRoomId")) : 0;
        Date checkin=request.getParameter("checkin") != null ? Date.valueOf(request.getParameter("checkin")) : null;
        Date checkout=request.getParameter("checkout") != null ? Date.valueOf(request.getParameter("checkout")) : null;
        if(typeId==0 || checkin == null || checkout == null) {
            response.sendRedirect("searchRoom");
            return;
        }
        TypeRoom selectedTypeRoom = dal.TypeRoomDAO.getInstance().getTypeRoomByTypeId(checkin, checkout, typeId);
        if(selectedTypeRoom == null) {
            response.sendRedirect("searchRoom");
            return;
        }
        request.setAttribute("selectedTypeRoom", selectedTypeRoom);
        request.getRequestDispatcher("View/Customer/DetailRoom.jsp").forward(request, response);
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
