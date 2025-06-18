package controllers.receptionists;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import models.DetailService;
import models.Room;
import models.Service;
import websocket.RoomStatusSocket;

/**
 *
 * @author HieuTT
 */
@WebServlet(name = "StayingRoom", urlPatterns = {"/receptionist/stayingRoom"})
public class StayingRoom extends HttpServlet {

    private static final int NUMBER_OF_ROWS = 4;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String search = request.getParameter("search");
        String pageStr = request.getParameter("page");
        String oldSearch = request.getParameter("oldSearch");
        String action = request.getParameter("action");
        if (pageStr == null || pageStr.isEmpty()) {
            pageStr = "1";
        }
        int currentPage = Integer.parseInt(pageStr);
        
        if (action != null && action.equals("view")) {
            request.setAttribute("action", action);
            request.setAttribute("currentPage", currentPage);
            handleView(request);
        }
        
        if (search != null && !search.isEmpty()) {
            search = search.trim();
            if (oldSearch == null || !oldSearch.equals(search)) {
                currentPage = 1;
            }
            handleSearch(request, search, currentPage);
        } else {
            handleDefault(request, currentPage);
        }
        request.setAttribute("currentPage", currentPage);
        request.getRequestDispatcher("/View/Receptionist/StayingRoom.jsp").forward(request, response);

    }

    private void handleDefault(HttpServletRequest request, int currentPage)
            throws ServletException, IOException {
        List<Room> stayingRooms = dal.RoomDAO.getInstance().getStayingRooms(NUMBER_OF_ROWS, currentPage, "");
        request.setAttribute("stayingRooms", stayingRooms);
        int totalStayingRooms = dal.RoomDAO.getInstance().getTotalStayingRooms("");
        int totalPages = (int) Math.ceil((double) totalStayingRooms / NUMBER_OF_ROWS);
        request.setAttribute("totalPages", totalPages);
    }

    private void handleSearch(HttpServletRequest request, String search, int currentPage)
            throws ServletException, IOException {
        request.setAttribute("search", search);
        List<Room> stayingRooms = dal.RoomDAO.getInstance().getStayingRooms(NUMBER_OF_ROWS, currentPage, search);
        request.setAttribute("stayingRooms", stayingRooms);
        int totalStayingRooms = dal.RoomDAO.getInstance().getTotalStayingRooms(search);
        int totalPages = (int) Math.ceil((double) totalStayingRooms / NUMBER_OF_ROWS);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("oldSearch", search);
    }

    private void handleView(HttpServletRequest request)
            throws ServletException, IOException {
        int roomNumber = Integer.parseInt(request.getParameter("roomNumber"));
        List<DetailService> services = dal.ServiceDAO.getInstance().getServiceByRoomNumber(roomNumber);
        request.setAttribute("services", services);
        List<Service> otherServices = dal.ServiceDAO.getInstance().getServiceNotInRoom(roomNumber);
        request.setAttribute("otherServices", otherServices);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String roomNumber = request.getParameter("roomNumber");
        boolean isCleaner = Boolean.parseBoolean(request.getParameter("status"));
        int roomId = Integer.parseInt(roomNumber);
        dal.RoomDAO.getInstance().updateRoomStatus(roomId, !isCleaner);
        RoomStatusSocket.broadcast("{\"roomId\":" + roomId + "}");

        StringBuilder linkForward = new StringBuilder(request.getContextPath() + "/receptionist/stayingRoom");
        String pageStr = request.getParameter("page");
        String search = request.getParameter("search");
        String oldSearch = request.getParameter("oldSearch");
        boolean hasParam = false;

        if (pageStr != null && !pageStr.isEmpty()) {
            int currentPage = Integer.parseInt(pageStr);
            if (currentPage > 1) {
                linkForward.append("?page=").append(currentPage);
                hasParam = true;
            }
        }
        if (search != null && !search.isEmpty()) {
            linkForward.append(hasParam ? "&" : "?").append("search=").append(search);
        }
        if (oldSearch != null && !oldSearch.isEmpty()) {
            linkForward.append(hasParam ? "&" : "?").append("oldSearch=").append(oldSearch);
        }
        response.sendRedirect(linkForward.toString());
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
