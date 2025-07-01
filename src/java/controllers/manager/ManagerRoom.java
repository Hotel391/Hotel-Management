package controllers.manager;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import models.Room;
import models.TypeRoom;

public class ManagerRoom extends HttpServlet {

    private String submitt = "submit";
    private String updateRoom = "updateRoom";
    private String insertRoom = "insertRoom";
    private String roomNumberr = "roomNumber";
    private String typeRoomIdd = "typeRoomId";
    private String typeRoom = "typeRoom";
    private String roomNumberSearch = "roomNumberSearch";
    private String typeRoomIdSearch = "typeRoomIdSearch";
    private String pagee = "page";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String choose = request.getParameter("choose");
        if (choose == null) {
            choose = "viewAll";
        }
        String pageStr = request.getParameter(pagee);
        if (pageStr == null || pageStr.isEmpty()) {
            pageStr = "1";
        }

        List<TypeRoom> tr = dal.RoomDAO.getInstance().getAllTypeRoom();

        //search
        if (choose.equals("search")) {
            String roomNumberStr = request.getParameter(roomNumberSearch);
            String typeRoomIdStr = request.getParameter(typeRoomIdSearch);

            Integer typeRoomId = null;

            try {
                if (typeRoomIdStr != null && !typeRoomIdStr.trim().isEmpty()) {
                    typeRoomId = Integer.valueOf(typeRoomIdStr);
                }
            } catch (NumberFormatException e) {
                // Optional: set error message or logging
            }

            List<Room> rr = dal.RoomDAO.getInstance().searchAllRoom(roomNumberStr, typeRoomId);
            paginateRoomList(request, rr);
            request.setAttribute(typeRoom, tr);
            request.getRequestDispatcher("/View/Manager/ViewRoom.jsp").forward(request, response);
            return;
        }

//        if (choose.equals("deleteRoom")) {
//            String roomNumberStr = request.getParameter(roomNumberr);
//            String roomNumberSearchStr = request.getParameter(roomNumberSearch);
//            String typeRoomIdSearchStr = request.getParameter(typeRoomIdSearch);
//            int roomNumber = Integer.parseInt(roomNumberStr);
//            dal.RoomDAO.getInstance().deleteRoom(roomNumber);
//            String redirectUrl = String.format("%s/manager/room?choose=search&page=%s&roomNumberSearch=%s&typeRoomIdSearch=%s&success=true&action=delete",
//                    request.getContextPath(),
//                    pageStr,
//                    roomNumberSearchStr,
//                    typeRoomIdSearchStr != null ? typeRoomIdSearchStr : ""
//            );
//            response.sendRedirect(redirectUrl);
//            return;
//        }

        //view list room
        if (choose.equals("viewAll")) {
            List<Room> r = dal.RoomDAO.getInstance().getAllRoom();
            paginateRoomList(request, r);
            request.setAttribute(typeRoom, tr);
            request.getRequestDispatcher("/View/Manager/ViewRoom.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String choose = request.getParameter("choose");
        String roomNumberStr = request.getParameter(roomNumberr);
        String typeRoomIdStr = request.getParameter(typeRoomIdd);
        String roomNumberSearchStr = request.getParameter(roomNumberSearch);
        String typeRoomIdSearchStr = request.getParameter(typeRoomIdSearch);
        List<TypeRoom> tr = dal.RoomDAO.getInstance().getAllTypeRoom();
        List<Room> r = dal.RoomDAO.getInstance().getAllRoom();
        String pageStr = request.getParameter(pagee);
        if (pageStr == null || pageStr.isEmpty()) {
            pageStr = "1";
        }

        //update room
        if (choose.equals(updateRoom)) {
            String submit = request.getParameter(submitt);
            String isActiveStr = request.getParameter("isActive");
            if (submit != null) {
                int roomNumber = 0;
                int typeRoomID = 0;
                boolean isActive = true;
                try {
                    roomNumber = Integer.parseInt(roomNumberStr);
                    typeRoomID = Integer.parseInt(typeRoomIdStr);
                    isActive = Boolean.parseBoolean(isActiveStr);
                } catch (NumberFormatException e) {
                    request.setAttribute("errorMessage", "Lỗi định dạng số phòng hoặc loại phòng.");
                }
                dal.RoomDAO.getInstance().updateRoom(typeRoomID, roomNumber, isActive);

                // redirect giữ lại filter sau khi thêm
                String redirectUrl = String.format("%s/manager/room?choose=search&page=%s&roomNumberSearch=%s&typeRoomIdSearch=%s&success=true&action=update",
                        request.getContextPath(),
                        pageStr,
                        roomNumberSearchStr,
                        typeRoomIdSearchStr != null ? typeRoomIdSearchStr : ""
                );
                response.sendRedirect(redirectUrl);
                return;

            }
        }

        //insert new room
        if (choose.equals(insertRoom)) {
            String submit = request.getParameter(submitt);
            if (submit != null) {

                boolean haveError = false;
                int roomNumber = 0;
                int typeRoomId = 0;

                try {
                    typeRoomId = Integer.parseInt(typeRoomIdStr);
                    roomNumber = Integer.parseInt(roomNumberStr);
                    if (roomNumber < 0) {
                        request.setAttribute("error", "Số phòng phải là số lớn hơn 0.");
                        haveError = true;
                    }
                } catch (NumberFormatException e) {
                    request.setAttribute("error", "Số phòng phải là số lớn hơn 0.");
                    haveError = true;
                }

                for (Room room : r) {
                    if (room.getRoomNumber() == roomNumber) {
                        request.setAttribute("error", "Số phòng đã tồn tại.");
                        haveError = true;
                    }
                }

                if (haveError) {

                    Integer typeRoomIdSearch = null;

                    if (typeRoomIdSearchStr != null && !typeRoomIdSearchStr.trim().isEmpty()) {
                        typeRoomIdSearch = Integer.valueOf(typeRoomIdSearchStr);
                    }

                    List<Room> listRoomSearch = dal.RoomDAO.getInstance().searchAllRoom(
                            roomNumberSearchStr,
                            typeRoomIdSearch);
                    request.setAttribute(typeRoom, tr);
                    paginateRoomList(request, listRoomSearch);

                    request.setAttribute("currentPage", Integer.valueOf(pageStr));
                    request.getRequestDispatcher("/View/Manager/ViewRoom.jsp").forward(request, response);
                    return;
                }

                // insert thành công
                dal.RoomDAO.getInstance().insertRoom(roomNumber, typeRoomId);

                // redirect giữ lại filter sau khi thêm
                String redirectUrl = String.format("%s/manager/room?choose=search&page=%s&roomNumberSearch=%s&typeRoomIdSearch=%s&success=true&action=add",
                        request.getContextPath(),
                        pageStr,
                        roomNumberSearchStr,
                        typeRoomIdSearchStr != null ? typeRoomIdSearchStr : ""
                );
                response.sendRedirect(redirectUrl);
                return;
            }
        }

    }

    private void paginateRoomList(HttpServletRequest request, List<Room> fullList) {
        String pageStr = request.getParameter(pagee);
        int page = pageStr != null ? Integer.parseInt(pageStr) : 1;
        int recordsPerPage = 8;

        int totalRecords = fullList.size();
        int totalPages = (int) Math.ceil(totalRecords * 1.0 / recordsPerPage);

        // Giảm page nếu vượt quá số trang thực tế
        if (page > totalPages && totalPages > 0) {
            page = totalPages;
        }

        int start = (page - 1) * recordsPerPage;
        int end = Math.min(start + recordsPerPage, totalRecords);
        List<Room> paginatedList = fullList.subList(start, end);

        request.setAttribute("listR", paginatedList);
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);

    }

}
