
package controllers.receptionist;

import dal.RoomDAO;
import dal.TypeRoomDAO;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Arrays;
import models.Room;

@WebServlet(name = "SearchRoom", urlPatterns = {"/receptionist/searchRoom"})
public class SearchRoom extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String startDateStr = request.getParameter("startDate");
        String endDateStr = request.getParameter("endDate");
        String typeRoomIdStr = request.getParameter("typeRoomId");
        String checkInAction = request.getParameter("checkIn");
        String removeRoomNumber = request.getParameter("removeRoomNumber");
        String addRoomNumber = request.getParameter("addRoom");
        String pageStr = request.getParameter("page");
        int page = pageStr != null && !pageStr.isEmpty() ? Integer.parseInt(pageStr) : 1;
        int recordsPerPage = 8;

        HttpSession session = request.getSession();
        List<String> selectedRooms = (List<String>) session.getAttribute("selectedRooms");
        if (selectedRooms == null) {
            selectedRooms = new ArrayList<>();
        }


        if (addRoomNumber != null && !addRoomNumber.isEmpty()) {
            if (!selectedRooms.contains(addRoomNumber)) {
                selectedRooms.add(addRoomNumber);
            }
            session.setAttribute("selectedRooms", selectedRooms);
            response.sendRedirect(request.getContextPath() + "/receptionist/searchRoom?startDate=" +
                (startDateStr != null ? startDateStr : "") + "&endDate=" +
                (endDateStr != null ? endDateStr : "") + "&typeRoomId=" +
                (typeRoomIdStr != null ? typeRoomIdStr : "") + "&page=" + page);
            return;
        }


        if (removeRoomNumber != null && !removeRoomNumber.isEmpty()) {
            selectedRooms.remove(removeRoomNumber);
            session.setAttribute("selectedRooms", selectedRooms);
            response.sendRedirect(request.getContextPath() + "/receptionist/searchRoom?startDate=" +
                (startDateStr != null ? startDateStr : "") + "&endDate=" +
                (endDateStr != null ? endDateStr : "") + "&typeRoomId=" +
                (typeRoomIdStr != null ? typeRoomIdStr : "") + "&page=" + page);
            return;
        }


        Map<String, String> selectedRoomTypes = new HashMap<>();
        for (String roomNumber : selectedRooms) {
            try {
                int roomNum = Integer.parseInt(roomNumber);
                Room room = RoomDAO.getInstance().getRoomByNumber(roomNum);
                if (room != null && room.getTypeRoom() != null) {
                    selectedRoomTypes.put(roomNumber, room.getTypeRoom().getTypeName());
                } else {
                    selectedRoomTypes.put(roomNumber, "Unknown");
                }
            } catch (NumberFormatException e) {
                selectedRoomTypes.put(roomNumber, "Unknown");
            }
        }

        request.setAttribute("startDateSearch", startDateStr != null ? startDateStr : "");
        request.setAttribute("endDateSearch", endDateStr != null ? endDateStr : "");
        request.setAttribute("typeRoomIdSearch", typeRoomIdStr != null ? typeRoomIdStr : "");
        request.setAttribute("currentPage", page);
        request.setAttribute("typeRooms", RoomDAO.getInstance().getAllTypeRoom());
        request.setAttribute("selectedRooms", selectedRooms);
        request.setAttribute("selectedRoomTypes", selectedRoomTypes);

        if (checkInAction != null) {
            if (selectedRooms.isEmpty()) {
                request.setAttribute("errorMessage", "Please select at least one room to book.");
                showSearchRoom(request, response, startDateStr, endDateStr, typeRoomIdStr, pageStr);
                return;
            }

            if (startDateStr == null || endDateStr == null || startDateStr.isEmpty() || endDateStr.isEmpty()) {
                request.setAttribute("errorMessage", "Please select both start and end dates.");
                showSearchRoom(request, response, startDateStr, endDateStr, typeRoomIdStr, pageStr);
                return;
            }

            try {
                java.sql.Date startDate = java.sql.Date.valueOf(startDateStr);
                java.sql.Date endDate = java.sql.Date.valueOf(endDateStr);

                if (startDateStr.compareTo(new java.text.SimpleDateFormat("yyyy-MM-dd").format(new Date())) < 0) {
                    request.setAttribute("errorMessage", "Start date cannot be before today.");
                    showSearchRoom(request, response, startDateStr, endDateStr, typeRoomIdStr, pageStr);
                    return;
                }

                if (endDate.compareTo(startDate) <= 0) {
                    request.setAttribute("errorMessage", "End date must be after start date.");
                    showSearchRoom(request, response, startDateStr, endDateStr, typeRoomIdStr, pageStr);
                    return;
                }

                session.setAttribute("startDate", startDateStr);
                session.setAttribute("endDate", endDateStr);
                session.setAttribute("roomNumbers", selectedRooms.toArray(new String[0]));
                double totalPrice = calculateTotalPriceMultiple(selectedRooms.toArray(new String[0]), startDateStr, endDateStr);
                session.setAttribute("totalPrice", totalPrice);

                Map<String, String> roomTypeMap = new HashMap<>();
                for (String roomNumber : selectedRooms) {
                    try {
                        int roomNum = Integer.parseInt(roomNumber);
                        Room room = RoomDAO.getInstance().getRoomByNumber(roomNum);
                        if (room != null && room.getTypeRoom() != null) {
                            roomTypeMap.put(roomNumber, room.getTypeRoom().getTypeName());
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Error parsing room number " + roomNumber + ": " + e.getMessage());
                    }
                }
                session.setAttribute("roomTypeMap", roomTypeMap);
                response.sendRedirect(request.getContextPath() + "/receptionist/roomInformation");
                return;
            } catch (IllegalArgumentException e) {
                request.setAttribute("errorMessage", "Invalid date format.");
                showSearchRoom(request, response, startDateStr, endDateStr, typeRoomIdStr, pageStr);
                return;
            }
        }

 
        if (startDateStr == null || endDateStr == null || startDateStr.isEmpty() || endDateStr.isEmpty()) {
            request.setAttribute("errorMessage", "Please select both start and end dates.");
            showSearchRoom(request, response, startDateStr, endDateStr, typeRoomIdStr, pageStr);
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
                    typeRoomId = Integer.parseInt(typeRoomIdStr);
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
    private double calculateTotalPriceMultiple(String[] roomNumbers, String startDateStr, String endDateStr) {
        if (roomNumbers == null || roomNumbers.length == 0 || startDateStr == null || endDateStr == null) {
            System.out.println("calculateTotalPriceMultiple: Invalid input - roomNumbers=" + 
                              (roomNumbers == null ? "null" : Arrays.toString(roomNumbers)) + 
                              ", startDate=" + startDateStr + ", endDate=" + endDateStr);
            return 0;
        }
        try {
            java.sql.Date startDate = java.sql.Date.valueOf(startDateStr);
            java.sql.Date endDate = java.sql.Date.valueOf(endDateStr);
            long numberOfNights = (endDate.getTime() - startDate.getTime()) / (1000 * 60 * 60 * 24);
            if (numberOfNights <= 0) {
                System.out.println("calculateTotalPriceMultiple: Invalid number of nights=" + numberOfNights);
                return 0;
            }

            double totalPrice = 0;
            for (String roomNumber : roomNumbers) {
                try {
                    int roomNum = Integer.parseInt(roomNumber);
                    Room room = RoomDAO.getInstance().getRoomByNumber(roomNum);
                    if (room != null && room.getTypeRoom() != null) {
                        double basePrice = TypeRoomDAO.getInstance().getPriceByTypeId(room.getTypeRoom().getTypeId());
                        totalPrice += basePrice * numberOfNights;
                    } else {
                        System.out.println("Room not found or no type for roomNumber=" + roomNumber);
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Error parsing room number " + roomNumber + ": " + e.getMessage());
                }
            }
            System.out.println("Calculated totalPrice=" + totalPrice);
            return totalPrice;
        } catch (IllegalArgumentException e) {
            System.out.println("calculateTotalPriceMultiple: Invalid date format - " + e.getMessage());
            return 0;
        }
    }

    private void showSearchRoom(HttpServletRequest request, HttpServletResponse response,
            String startDateStr, String endDateStr, String typeRoomIdStr, String pageStr)
            throws ServletException, IOException {
        request.setAttribute("startDateSearch", startDateStr != null ? startDateStr : "");
        request.setAttribute("endDateSearch", endDateStr != null ? endDateStr : "");
        request.setAttribute("typeRoomIdSearch", typeRoomIdStr != null ? typeRoomIdStr : "");
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
