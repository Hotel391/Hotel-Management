package controllers.customer;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.sql.Date;
import models.TypeRoom;
import services.sorting.ReviewSortFactory;
import services.sorting.ReviewSortStrategy;

/**
 *
 * @author HieuTT
 */
@WebServlet(name="DetailRoom", urlPatterns={"/detailRoom"})
public class DetailRoom extends HttpServlet {
    private static final int NUMBER_OF_REVIEWS_PER_PAGE = 4;
   
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        int typeId = request.getParameter("typeRoomId") != null ? Integer.parseInt(request.getParameter("typeRoomId")) : 0;
        Date checkin=request.getParameter("checkin") != null ? Date.valueOf(request.getParameter("checkin")) : null;
        Date checkout=request.getParameter("checkout") != null ? Date.valueOf(request.getParameter("checkout")) : null;
        String sortOption= request.getParameter("sortBy") != null ? request.getParameter("sortBy") : "";
        ReviewSortStrategy strategy=ReviewSortFactory.getStrategy(sortOption);
        String orderByClause = strategy.getOrderByClause();

        if(typeId==0 || checkin == null || checkout == null) {
            response.sendRedirect("searchRoom");
            return;
        }
        int currentPage = request.getParameter("page") != null ? Integer.parseInt(request.getParameter("page")) : 1;
        int offset = (currentPage - 1) * NUMBER_OF_REVIEWS_PER_PAGE;
        
        TypeRoom selectedTypeRoom = dal.TypeRoomDAO.getInstance().getTypeRoomByTypeId(checkin, checkout, typeId, orderByClause, offset, NUMBER_OF_REVIEWS_PER_PAGE);
        if(selectedTypeRoom == null) {
            response.sendRedirect("searchRoom");
            return;
        }
        
        int numberOfReviews= selectedTypeRoom.getNumberOfReviews();
        int totalPages = (int) Math.ceil((double) numberOfReviews / NUMBER_OF_REVIEWS_PER_PAGE);
        request.setAttribute("currentPage", currentPage);
        request.setAttribute("totalPages", totalPages);
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
