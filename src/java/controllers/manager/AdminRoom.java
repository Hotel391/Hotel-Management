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
        List<TypeRoom> tr = null;
        if (choose.equals(insertRoom) || choose.equals(updateRoom)) {
            tr = dal.RoomDAO.getInstance().getAllTypeRoom();
        }

        if (choose.equals("deleteRoom")) {
            String roomNumberStr = request.getParameter(roomNumberr);
            int roomNumber = Integer.parseInt(roomNumberStr);
            dal.RoomDAO.getInstance().deleteRoom(roomNumber);
            response.sendRedirect("room");
        }

        //forward sang InsertRoom.jsp
        if (choose.equals(insertRoom)) {
            if (submit == null) {
                request.setAttribute(typeRoom, tr);
                request.getRequestDispatcher("/View/Manager/InsertRoom.jsp").forward(request, response);
            }
        }

        //forward sang UpdateRoom.jsp
        if (choose.equals(updateRoom)) {
            if (submit == null) {
                String roomNumber = request.getParameter(roomNumberr);
                String typeRoomId = request.getParameter(typeRoomIdd);
                request.setAttribute(roomNumberr, roomNumber);
                request.setAttribute(typeRoomIdd, typeRoomId);
                request.setAttribute(typeRoom, tr);
                request.getRequestDispatcher("/View/Manager/UpdateRoom.jsp").forward(request, response);
            }
        }

        //view list room
        if (choose.equals("viewAll")) {
            List<Room> r = dal.RoomDAO.getInstance().getAllRoom();
            request.setAttribute("listR", r);
            request.getRequestDispatcher("/View/Manager/ViewRoom.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String choose = request.getParameter("choose");
        String roomNumberStr = request.getParameter(roomNumberr);
        String typeRoomIdStr = request.getParameter(typeRoomIdd);
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

        //insert new room
        if (choose.equals(insertRoom)) {
            String submit = request.getParameter(submitt);
            if (submit != null) {

                boolean haveError = false;
                int roomNumber = 0;
                int typeRoomId = 0;

                request.setAttribute(roomNumberr, roomNumberStr);

                List<TypeRoom> tr = dal.RoomDAO.getInstance().getAllTypeRoom();
                request.setAttribute(typeRoom, tr);

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
                    request.getRequestDispatcher("/View/Manager/InsertRoom.jsp").forward(request, response);
                    return;
                }
                dal.RoomDAO.getInstance().insertRoom(roomNumber, typeRoomId);
            }
        }
        response.sendRedirect("room");
    }
}
