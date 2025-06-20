package controllers.receptionist;

import dal.RoomDAO;
import dal.TypeRoomDAO;
import java.io.IOException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.Date;
import models.Room;

@WebServlet(name = "SearchRoom", urlPatterns = {"/receptionist/searchRoom"})
public class SearchRoom extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    String startDateStr = request.getParameter("startDate");
    String endDateStr = request.getParameter("endDate");
    String typeRoomIdStr = request.getParameter("typeRoomId");
    String roomNumber = request.getParameter("roomNumber");
    String pageStr = request.getParameter("page");

    int page = pageStr != null && !pageStr.isEmpty() ? Integer.parseInt(pageStr) : 1;
    int recordsPerPage = 8;

    request.setAttribute("startDateSearch", startDateStr != null ? startDateStr : "");
    request.setAttribute("endDateSearch", endDateStr != null ? endDateStr : "");
    request.setAttribute("typeRoomIdSearch", typeRoomIdStr != null ? typeRoomIdStr : "");
    request.setAttribute("currentPage", page);
    request.setAttribute("typeRooms", RoomDAO.getInstance().getAllTypeRoom());

    String checkInAction = request.getParameter("checkIn");
    if (checkInAction != null) {
        HttpSession session = request.getSession();
        session.setAttribute("startDate", startDateStr);
        session.setAttribute("endDate", endDateStr);
        session.setAttribute("roomNumber", roomNumber);
        session.setAttribute("typeRoomId", typeRoomIdStr);

        if (typeRoomIdStr == null || typeRoomIdStr.isEmpty()) {
            Integer typeRoomId = RoomDAO.getInstance().getTypeIdByRoomNumber(Integer.parseInt(roomNumber));
            typeRoomIdStr = typeRoomId != null ? typeRoomId.toString() : "0"; 
        }

        double totalPrice = calculateTotalPrice(typeRoomIdStr, startDateStr, endDateStr);
        session.setAttribute("totalPrice", totalPrice);

        response.sendRedirect(request.getContextPath() + "/receptionist/checkout");
        return;
    }

    if (startDateStr != null && !startDateStr.isEmpty() && endDateStr != null && !endDateStr.isEmpty()) {
        java.sql.Date startDate;
        java.sql.Date endDate;
        try {
            startDate = java.sql.Date.valueOf(startDateStr);
            endDate = java.sql.Date.valueOf(endDateStr);
        } catch (IllegalArgumentException e) {
            request.setAttribute("errorMessage", "Invalid date format.");
            showSearchRoom(request, response, startDateStr, endDateStr, typeRoomIdStr, pageStr);
            return;
        }

        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
        String todayStr = sdf.format(new Date());

        if (startDateStr.compareTo(todayStr) < 0) {
            request.setAttribute("errorMessage", "Start date cannot be before today.");
            showSearchRoom(request, response, startDateStr, endDateStr, typeRoomIdStr, pageStr);
            return;
        }

        if (endDate.compareTo(startDate) <= 0) {
            request.setAttribute("errorMessage", "End date must be after start date.");
            showSearchRoom(request, response, startDateStr, endDateStr, typeRoomIdStr, pageStr);
            return;
        }

        Integer typeRoomId = null;
        if (typeRoomIdStr != null && !typeRoomIdStr.isEmpty()) {
            try {
                typeRoomId = Integer.valueOf(typeRoomIdStr);
            } catch (NumberFormatException e) {
                request.setAttribute("errorMessage", "Invalid room type.");
                showSearchRoom(request, response, startDateStr, endDateStr, typeRoomIdStr, pageStr);
                return;
            }
        }

        List<Room> availableRooms = RoomDAO.getInstance().searchAvailableRooms(startDate, endDate, typeRoomId, page, recordsPerPage);
        int totalRecords = RoomDAO.getInstance().countAvailableRooms(startDate, endDate, typeRoomId);
        int totalPages = (int) Math.ceil(totalRecords * 1.0 / recordsPerPage);

        request.setAttribute("availableRooms", availableRooms);
        request.setAttribute("totalPages", totalPages);
    }

    showSearchRoom(request, response, startDateStr, endDateStr, typeRoomIdStr, pageStr);
}



    private double calculateTotalPrice(String typeRoomIdStr, String startDateStr, String endDateStr) {
        if (typeRoomIdStr == null) return 0;

        double basePrice = TypeRoomDAO.getInstance().getPriceByTypeId(Integer.parseInt(typeRoomIdStr));

        java.sql.Date startDate = java.sql.Date.valueOf(startDateStr);
        java.sql.Date endDate = java.sql.Date.valueOf(endDateStr);

        long numberOfNights = (endDate.getTime() - startDate.getTime()) / (1000 * 60 * 60 * 24);
        return basePrice * numberOfNights;
    }

    private void showSearchRoom(HttpServletRequest request, HttpServletResponse response,
            String startDateStr, String endDateStr, String typeRoomIdStr, String pageStr)
            throws ServletException, IOException {
        request.setAttribute("startDateSearch", startDateStr);
        request.setAttribute("endDateSearch", endDateStr);
        request.setAttribute("typeRoomIdSearch", typeRoomIdStr);
        request.setAttribute("currentPage", pageStr != null ? Integer.parseInt(pageStr) : 1);
        request.getRequestDispatcher("/View/Receptionist/SearchRoom.jsp").forward(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Search room based on dates and room type.";
    }
}