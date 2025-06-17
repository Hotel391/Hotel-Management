package controllers.admin;

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
        List<Review> fullList = dal.ReviewDAO.getInstance().getAllReview();
        paginateReviewList(request, fullList);

        request.getRequestDispatcher("/View/Admin/ViewReview.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        if ("search".equals(action)) {
//            String fullname = request.getParameter("fullName");
            String starFilterStr = request.getParameter("starFilter");
            Integer starFilter = null;
            String dateStr = request.getParameter("date");
            Date date = null;
            if (dateStr != null && !dateStr.isEmpty()) {
                date = Date.valueOf(dateStr);
            }
            if(starFilterStr != null && !starFilterStr.trim().isEmpty()){
                starFilter = Integer.valueOf(starFilterStr);
            }

            List<Review> filteredList = dal.ReviewDAO.getInstance().searchReview(starFilter, date);

            paginateReviewList(request, filteredList);

            request.setAttribute("starFilter", starFilter);
            request.setAttribute("date", dateStr);
            request.getRequestDispatcher("/View/Admin/ViewReview.jsp").forward(request, response);
        } else {
            doGet(request, response);
        }
    }

    private void paginateReviewList(HttpServletRequest request, List<Review> fullList) {
        String pageStr = request.getParameter("page");
        int page = pageStr != null ? Integer.parseInt(pageStr) : 1;
        int recordsPerPage = 5;

        int totalRecords = fullList.size();
        int totalPages = (int) Math.ceil(totalRecords * 1.0 / recordsPerPage);

        int start = (page - 1) * recordsPerPage;
        int end = Math.min(start + recordsPerPage, totalRecords);
        List<Review> paginatedList = fullList.subList(start, end);

        request.setAttribute("list", paginatedList);
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);
    }

}
