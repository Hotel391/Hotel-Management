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

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String choose = request.getParameter("choose");
        if (choose == null) {
            choose = "viewAll";
        }
        String submit = request.getParameter(submitt);
        List<TypeRoom> tr = dal.RoomDAO.getInstance().getAllTypeRoom();

        if (choose.equals("deleteRoom")) {
            String roomNumberStr = request.getParameter(roomNumberr);
            int roomNumber = Integer.parseInt(roomNumberStr);
            dal.RoomDAO.getInstance().deleteRoom(roomNumber);
            response.sendRedirect(request.getContextPath() + "/admin/room");
        }

        

        //view list room
        if (choose.equals("viewAll")) {
            List<Room> r = dal.RoomDAO.getInstance().getAllRoom();
            request.setAttribute(typeRoom, tr);
            request.setAttribute("listR", r);
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
            }
        }
        
        
        if (choose.equals("search")) {
            roomNumberStr = request.getParameter(roomNumberr);
            typeRoomIdStr = request.getParameter(typeRoomIdd);

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

            request.setAttribute(typeRoom, tr);
            request.setAttribute("listR", rr);
            request.getRequestDispatcher("/View/Admin/ViewRoom.jsp").forward(request, response);
            return;
        }

        //insert new room
        if (choose.equals(insertRoom)) {
            String submit = request.getParameter(submitt);
            if (submit != null) {

                boolean haveError = false;
                int roomNumber = 0;
                int typeRoomId = 0;

                request.setAttribute(roomNumberr, roomNumberStr);

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
                    request.setAttribute("listR", r);
                    request.getRequestDispatcher("/View/Admin/ViewRoom.jsp").forward(request, response);
                    return;
                }
                dal.RoomDAO.getInstance().insertRoom(roomNumber, typeRoomId);
            }
        }
        response.sendRedirect(request.getContextPath() + "/admin/room");
    }
}
