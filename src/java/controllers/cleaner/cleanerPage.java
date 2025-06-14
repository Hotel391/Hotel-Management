package controllers.cleaner;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import models.Room;

/**
 *
 * @author HieuTT
 */
@WebServlet(name="CleanerPage", urlPatterns={"/cleaner/page"})
public class CleanerPage extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession sess=request.getSession(false);
        List<Room> rooms= dal.RoomDAO.getInstance().getAllNotAvailableRoomOfCleaner(1, 2, 1);
        request.setAttribute("rooms", rooms);
        request.getRequestDispatcher("/View/Cleaner/CleanerPage.jsp").forward(request, response);
    }
}
