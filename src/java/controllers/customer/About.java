package controllers.customer;

import dal.ReviewDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import models.Review;

@WebServlet(name = "AboutUs", urlPatterns = {"/about"})
public class About extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        ReviewDAO reviewDAO = ReviewDAO.getInstance();
        List<Review> topReviews = reviewDAO.getTop10FiveStarReviews();
        request.setAttribute("topReviews", topReviews);

        request.getRequestDispatcher("/View/Customer/About.jsp").forward(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Show About Us page";
    }
}
