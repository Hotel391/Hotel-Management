package controllers.customer;

import dal.TypeRoomDAO;
import dal.ReviewDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import models.TypeRoom;
import models.Review;

@WebServlet(name = "Home", urlPatterns = {"/home"})
public class Home extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/View/Customer/Home.jsp").forward(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {

            TypeRoomDAO typeRoomDAO = TypeRoomDAO.getInstance();
            List<TypeRoom> topRoomTypes = typeRoomDAO.getTop5MostBookedRoomTypes();
            request.setAttribute("topRoomTypes", topRoomTypes);

            ReviewDAO reviewDAO = ReviewDAO.getInstance();
            List<Review> topReviews = reviewDAO.getTop10FiveStarReviews();
            request.setAttribute("topReviews", topReviews);

            request.getRequestDispatcher("/View/Customer/Home.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Đã xảy ra lỗi khi tải dữ liệu trang chủ.");
            request.getRequestDispatcher("/View/Customer/Home.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Home page controller";
    }

}
