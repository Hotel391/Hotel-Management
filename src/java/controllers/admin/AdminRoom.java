package controllers.admin;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import models.Room;
import models.TypeRoom;

public class AdminRoom extends HttpServlet {

    private String submitt = "submit";
    private String updateRoom = "updateRoom";
    private String insertRoom = "insertRoom";
    private String roomNumberr = "roomNumber";
    private String typeRoom = "typeRoom";
    private String typeRoomIdd = "typeRoomId";
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
        String submit = request.getParameter(submitt);
        List<TypeRoom> tr = dal.RoomDAO.getInstance().getAllTypeRoom();

        //search
        if (choose.equals("search")) {
            String roomNumberStr = request.getParameter(roomNumberr);
            String typeRoomIdStr = request.getParameter(typeRoomIdd);

            Integer roomNumber = null;
            Integer typeRoomId = null;

            try {
                if (roomNumberStr != null && !roomNumberStr.trim().isEmpty()) {
                    roomNumber = Integer.valueOf(roomNumberStr);
                }
                if (typeRoomIdStr != null && !typeRoomIdStr.trim().isEmpty()) {
                    typeRoomId = Integer.valueOf(typeRoomIdStr);
                }
            } catch (NumberFormatException e) {
                // Optional: set error message or logging
            }

            List<Room> rr = dal.RoomDAO.getInstance().searchAllRoom(roomNumber, typeRoomId);
            paginateRoomList(request, rr);
            request.setAttribute(typeRoom, tr);
            request.getRequestDispatcher("/View/Admin/ViewRoom.jsp").forward(request, response);
            return;
        }
        
        if (choose.equals("deleteRoom")) {
            String roomNumberStr = request.getParameter(roomNumberr);
            String typeRoomIdStr = request.getParameter(typeRoomIdd);
            int roomNumber = Integer.parseInt(roomNumberStr);
            dal.RoomDAO.getInstance().deleteRoom(roomNumber);
            String redirectUrl = String.format("%s/admin/room?choose=search&page=%s&typeRoomId=%s&success=true&action=delete",
                        request.getContextPath(),
                        pageStr,
                        typeRoomIdStr != null ? typeRoomIdStr : ""
                );
                response.sendRedirect(redirectUrl);
                return;
        }

        //view list room
        if (choose.equals("viewAll")) {
            List<Room> r = dal.RoomDAO.getInstance().getAllRoom();
            paginateRoomList(request, r);
            request.setAttribute(typeRoom, tr);
            request.getRequestDispatcher("/View/Admin/ViewRoom.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String choose = request.getParameter("choose");
        String roomNumberStr = request.getParameter(roomNumberr);
        String typeRoomIdStr = request.getParameter(typeRoomIdd);
        List<TypeRoom> tr = dal.RoomDAO.getInstance().getAllTypeRoom();
        List<Room> r = dal.RoomDAO.getInstance().getAllRoom();
        String pageStr = request.getParameter(pagee);
        if (pageStr == null || pageStr.isEmpty()) {
            pageStr = "1";
        }

        //update room
        if (choose.equals(updateRoom)) {
            String submit = request.getParameter(submitt);
            if (submit != null) {
                int roomNumber = 0;
                int typeRoomID = 0;
                try {
                    roomNumber = Integer.parseInt(roomNumberStr);
                    typeRoomID = Integer.parseInt(typeRoomIdStr);
                } catch (NumberFormatException e) {
                    request.setAttribute("errorMessage", "Lỗi định dạng số phòng hoặc loại phòng.");
                }
                dal.RoomDAO.getInstance().updateRoom(typeRoomID, roomNumber);
                
                 // redirect giữ lại filter sau khi thêm
                String redirectUrl = String.format("%s/admin/room?choose=search&page=%s&typeRoomId=%s&success=true&action=update",
                        request.getContextPath(),
                        pageStr,
                        typeRoomIdStr != null ? typeRoomIdStr : ""
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
                        request.setAttribute("error", "Room Number must be a positive integer.");
                        haveError = true;
                    }
                } catch (NumberFormatException e) {
                    request.setAttribute("error", "Room Number must be a positive integer.");
                    haveError = true;
                }

                if (haveError) {
                    request.setAttribute(typeRoom, tr);
                    paginateRoomList(request, r);
                    request.getRequestDispatcher("/View/Admin/ViewRoom.jsp").forward(request, response);
                    return;
                }

                // insert thành công
                dal.RoomDAO.getInstance().insertRoom(roomNumber, typeRoomId);

                // redirect giữ lại filter sau khi thêm
                String redirectUrl = String.format("%s/admin/room?choose=search&page=%s&typeRoomId=%s&success=true&action=add",
                        request.getContextPath(),
                        pageStr,
                        typeRoomIdStr != null ? typeRoomIdStr : ""
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
