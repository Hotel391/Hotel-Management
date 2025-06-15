package controllers.cleaner;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import models.CleanerFloor;

import models.Employee;
import models.Room;

/**
 *
 * @author HieuTT
 */
@WebServlet(name = "CleanerPage", urlPatterns = {"/cleaner/page"})
public class CleanerPage extends HttpServlet {

    private static final int NUMBER_OF_ROOMS_PER_PAGE = 7;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession sess = request.getSession(false);
        if (sess == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        Employee emp = (Employee) sess.getAttribute("employeeInfo");
        if (emp == null || emp.getRole().getRoleId() != Employee.ROLE_CLEANER) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        CleanerFloor cleanerFloor = dal.EmployeeDAO.getInstance().getCleanerFloorByEmployeeId(emp.getEmployeeId());
        String pageStr = request.getParameter("page");
        int page = 1;
        if (pageStr != null && !pageStr.isEmpty()) {
            page = Integer.parseInt(pageStr);
        }
        request.setAttribute("currentPage", page);
        int totalRecords = dal.RoomDAO.getInstance().getTotalNotAvailableRoomOfCleaner(
                cleanerFloor.getStartFloor(), cleanerFloor.getEndFloor()
        );
        int totalPages = (int) Math.ceil((double) totalRecords / NUMBER_OF_ROOMS_PER_PAGE);
        request.setAttribute("totalPages", totalPages);
        List<Room> rooms = dal.RoomDAO.getInstance().getAllNotAvailableRoomOfCleaner(cleanerFloor.getStartFloor(),
                cleanerFloor.getEndFloor(), page, NUMBER_OF_ROOMS_PER_PAGE);
        request.setAttribute("rooms", rooms);
        request.getRequestDispatcher("/View/Cleaner/CleanerPage.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String[] roomNumbers = request.getParameterValues("roomIds");
        for (String roomNumber : roomNumbers) {
            dal.RoomDAO.getInstance().updateRoomStatus(Integer.parseInt(roomNumber), Room.STATUS_CLEANING);
        }
        String pageStr = request.getParameter("currentPage");
        int page = 1;
        if (pageStr != null && !pageStr.isEmpty()) {
            page = Integer.parseInt(pageStr);
        }
        response.sendRedirect(request.getContextPath() + "/cleaner/page?page=" + page);
    }
}
