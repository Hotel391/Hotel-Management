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
        String adultStr = request.getParameter("adult");
        String childrenStr = request.getParameter("children");
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
            response.sendRedirect(request.getContextPath() + "/receptionist/searchRoom?startDate="
                    + (startDateStr != null ? startDateStr : "") + "&endDate="
                    + (endDateStr != null ? endDateStr : "") + "&typeRoomId="
                    + (typeRoomIdStr != null ? typeRoomIdStr : "") + "&adult="
                    + (adultStr != null ? adultStr : "") + "&children="
                    + (childrenStr != null ? childrenStr : "") + "&page=" + page);
            return;
        }

        if (removeRoomNumber != null && !removeRoomNumber.isEmpty()) {
            selectedRooms.remove(removeRoomNumber);
            session.setAttribute("selectedRooms", selectedRooms);
            response.sendRedirect(request.getContextPath() + "/receptionist/searchRoom?startDate="
                    + (startDateStr != null ? startDateStr : "") + "&endDate="
                    + (endDateStr != null ? endDateStr : "") + "&typeRoomId="
                    + (typeRoomIdStr != null ? typeRoomIdStr : "") + "&adult="
                    + (adultStr != null ? adultStr : "") + "&children="
                    + (childrenStr != null ? childrenStr : "") + "&page=" + page);
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
        request.setAttribute("adultSearch", adultStr != null ? adultStr : "");
        request.setAttribute("childrenSearch", childrenStr != null ? childrenStr : "");
        request.setAttribute("currentPage", page);
        request.setAttribute("typeRooms", RoomDAO.getInstance().getAllTypeRoom());
        request.setAttribute("selectedRooms", selectedRooms);
        request.setAttribute("selectedRoomTypes", selectedRoomTypes);

        if (checkInAction != null) {

            if (startDateStr == null || endDateStr == null || startDateStr.isEmpty() || endDateStr.isEmpty()) {
                request.setAttribute("errorMessage", "Vui lòng chọn cả ngày nhận phòng và ngày trả phòng");
                showSearchRoom(request, response, startDateStr, endDateStr, typeRoomIdStr, adultStr, childrenStr, pageStr);
                return;
            }

            try {
                java.sql.Date startDate = java.sql.Date.valueOf(startDateStr);
                java.sql.Date endDate = java.sql.Date.valueOf(endDateStr);

                if (startDateStr.compareTo(new java.text.SimpleDateFormat("yyyy-MM-dd").format(new Date())) < 0) {
                    request.setAttribute("errorMessage", "Ngày nhận phòng bắt đầu từ ngày hôm nay.");
                    showSearchRoom(request, response, startDateStr, endDateStr, typeRoomIdStr, adultStr, childrenStr, pageStr);
                    return;
                }

                if (endDate.compareTo(startDate) <= 0) {
                    request.setAttribute("errorMessage", "Ngày trả phong phải sau ngày nhận phòng.");
                    showSearchRoom(request, response, startDateStr, endDateStr, typeRoomIdStr, adultStr, childrenStr, pageStr);
                    return;
                }

                long numberOfDays = (endDate.getTime() - startDate.getTime()) / (1000 * 60 * 60 * 24);
                if (numberOfDays > 90) {
                    request.setAttribute("errorMessage", "Vui lòng đặt lại thời gian.Thời gian đặt phòng không được vượt quá 3 tháng (90 ngày).");
                    session.removeAttribute("selectedRooms");
                    showSearchRoom(request, response, startDateStr, endDateStr, typeRoomIdStr, adultStr, childrenStr, pageStr);
                    return;
                }
                List<String> conflictRooms = RoomDAO.getInstance().getUnavailableRooms(selectedRooms, startDate, endDate);

                if (!conflictRooms.isEmpty()) {
                    selectedRooms.removeAll(conflictRooms);
                    session.setAttribute("selectedRooms", selectedRooms);

                    request.setAttribute("errorMessage", "Phòng đã bị người khác đặt: " + String.join(", ", conflictRooms));
                    showSearchRoom(request, response, startDateStr, endDateStr, typeRoomIdStr, adultStr, childrenStr, pageStr);
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
                    }
                }
                session.setAttribute("roomTypeMap", roomTypeMap);
                response.sendRedirect(request.getContextPath() + "/receptionist/roomInformation");
                return;
            } catch (IllegalArgumentException e) {
                request.setAttribute("errorMessage", "Định dạng ngày không hợp lệ (YYYY-MM-DD).");
                showSearchRoom(request, response, startDateStr, endDateStr, typeRoomIdStr, adultStr, childrenStr, pageStr);
                return;
            }
        }

        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
        String todayStr = sdf.format(new Date());
        String tomorrowStr = sdf.format(new Date(new Date().getTime() + 24 * 60 * 60 * 1000));

        if (startDateStr == null || startDateStr.isEmpty()) {
            startDateStr = todayStr;
        }
        if (endDateStr == null || endDateStr.isEmpty()) {
            endDateStr = tomorrowStr;
        }

        try {
            java.sql.Date startDate = java.sql.Date.valueOf(startDateStr);
            java.sql.Date endDate = java.sql.Date.valueOf(endDateStr);

            if (startDateStr.compareTo(todayStr) < 0) {
                request.setAttribute("errorMessage", "Ngày nhận phòng bắt đầu từ ngày hôm nay.");
                showSearchRoom(request, response, startDateStr, endDateStr, typeRoomIdStr, adultStr, childrenStr, pageStr);
                return;
            }

            if (endDate.compareTo(startDate) <= 0) {
                request.setAttribute("errorMessage", "Ngày trả phòng phải sau ngày nhận phòng.");
                showSearchRoom(request, response, startDateStr, endDateStr, typeRoomIdStr, adultStr, childrenStr, pageStr);
                return;
            }

            Integer typeRoomId = null;
            Integer adult = null;
            Integer children = null;

            if (typeRoomIdStr != null && !typeRoomIdStr.isEmpty()) {
                try {
                    typeRoomId = Integer.parseInt(typeRoomIdStr);
                } catch (NumberFormatException e) {
                    request.setAttribute("errorMessage", "Loại phòng không hợp lệ.");
                    showSearchRoom(request, response, startDateStr, endDateStr, typeRoomIdStr, adultStr, childrenStr, pageStr);
                    return;
                }
            }

            if (adultStr != null && !adultStr.isEmpty()) {
                try {
                    adult = Integer.parseInt(adultStr);
                    if (adult < 0) {
                        adult = 0;
                    }
                } catch (NumberFormatException e) {
                    request.setAttribute("errorMessage", "Số người lớn không hợp lệ.");
                    showSearchRoom(request, response, startDateStr, endDateStr, typeRoomIdStr, adultStr, childrenStr, pageStr);
                    return;
                }
            }

            if (childrenStr != null && !childrenStr.isEmpty()) {
                try {
                    children = Integer.parseInt(childrenStr);
                    if (children < 0) {
                        children = 0;
                    }
                } catch (NumberFormatException e) {
                    request.setAttribute("errorMessage", "Số trẻ em không hợp lệ.");
                    showSearchRoom(request, response, startDateStr, endDateStr, typeRoomIdStr, adultStr, childrenStr, pageStr);
                    return;
                }
            }

            List<Room> availableRooms = RoomDAO.getInstance().searchAvailableRooms(startDate, endDate, typeRoomId, adult, children, page, recordsPerPage);
            int totalRecords = RoomDAO.getInstance().countAvailableRooms(startDate, endDate, typeRoomId, adult, children);
            int totalPages = (int) Math.ceil(totalRecords * 1.0 / recordsPerPage);
            request.setAttribute("availableRooms", availableRooms);
            request.setAttribute("totalPages", totalPages);
        } catch (IllegalArgumentException e) {
            request.setAttribute("errorMessage", "Định dạng ngày không hợp lệ (YYYY-MM-DD).");
            showSearchRoom(request, response, startDateStr, endDateStr, typeRoomIdStr, adultStr, childrenStr, pageStr);
            return;
        }

        showSearchRoom(request, response, startDateStr, endDateStr, typeRoomIdStr, adultStr, childrenStr, pageStr);
    }

    private double calculateTotalPriceMultiple(String[] roomNumbers, String startDateStr, String endDateStr) {
        if (roomNumbers == null || roomNumbers.length == 0 || startDateStr == null || endDateStr == null) {
            return 0;
        }
        try {
            java.sql.Date startDate = java.sql.Date.valueOf(startDateStr);
            java.sql.Date endDate = java.sql.Date.valueOf(endDateStr);
            long numberOfNights = (endDate.getTime() - startDate.getTime()) / (1000 * 60 * 60 * 24);
            if (numberOfNights <= 0) {
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
                    }
                } catch (NumberFormatException e) {
                }
            }
            return totalPrice;
        } catch (IllegalArgumentException e) {
            return 0;
        }
    }

    private void showSearchRoom(HttpServletRequest request, HttpServletResponse response,
            String startDateStr, String endDateStr, String typeRoomIdStr, String adultStr, String childrenStr, String pageStr)
            throws ServletException, IOException {
        request.setAttribute("startDateSearch", startDateStr != null ? startDateStr : "");
        request.setAttribute("endDateSearch", endDateStr != null ? endDateStr : "");
        request.setAttribute("typeRoomIdSearch", typeRoomIdStr != null ? typeRoomIdStr : "");
        request.setAttribute("adultSearch", adultStr != null ? adultStr : "");
        request.setAttribute("childrenSearch", childrenStr != null ? childrenStr : "");
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
}
