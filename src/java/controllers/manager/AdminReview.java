package controllers.manager;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.sql.Date;
import java.util.List;
import models.Review;

public class AdminReview extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String submit = request.getParameter("submit");
        List<Review> list;
        if (submit == null) {
            list = dal.ReviewDAO.getInstance().getAllReview();
            request.setAttribute("list", list);
            request.getRequestDispatcher("/View/Admin/ViewReview.jsp").forward(request, response);
        } 
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String submit = request.getParameter("submit");
        List<Review> list;
        if(submit != null){ // tìm kiếm theo fullname và date hoặc 1 trong 2
            String fullname = request.getParameter("fullName");
            String dateStr = request.getParameter("date");
            Date date = null;
            if (dateStr != null && !dateStr.isEmpty()) {
                date = Date.valueOf(dateStr);
            }
            list = dal.ReviewDAO.getInstance().searchReview(fullname, date);
            request.setAttribute("list", list);
            request.getRequestDispatcher("/View/Admin/ViewReview.jsp").forward(request, response);
        }
    }
}
