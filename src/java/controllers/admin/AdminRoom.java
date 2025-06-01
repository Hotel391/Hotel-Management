/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

package controllers.admin;

import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import models.Room;
import models.TypeRoom;

/**
 *
 * @author Tuan'sPC
 */
public class AdminRoom extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String choose = request.getParameter("choose");
        if (choose == null) {
            choose = "viewAll";
        }

        if (choose.equals("deleteRoom")) {
            String roomNumberStr = request.getParameter("roomNumber");
            int roomNumber = Integer.parseInt(roomNumberStr);
            dals.RoomDAO.getInstance().deleteRoom(roomNumber);
            response.sendRedirect("AdminRoom");
        }

        //forward sang InsertRoom.jsp
        if (choose.equals("insertRoom")) {
            String submit = request.getParameter("submit");
            if (submit == null) {
                List<TypeRoom> tr = dals.RoomDAO.getInstance().getAllTypeRoom();
                request.setAttribute("typeRoom", tr);
                request.getRequestDispatcher("View/Admin/InsertRoom.jsp").forward(request, response);
            }
        }

        //forward sang UpdateRoom.jsp
        if (choose.equals("updateRoom")) {
            String submit = request.getParameter("submit");
            List<TypeRoom> tr = dals.RoomDAO.getInstance().getAllTypeRoom();
            if (submit == null) {
                String roomNumber = request.getParameter("roomNumber");
                String typeRoomId = request.getParameter("typeRoomId");
                request.setAttribute("roomNumber", roomNumber);
                request.setAttribute("typeRoomId", typeRoomId);
                request.setAttribute("typeRoom", tr);
                request.getRequestDispatcher("View/Admin/UpdateRoom.jsp").forward(request, response);
            }
        }

        //view list room
        if (choose.equals("viewAll")) {
            List<Room> r = dals.RoomDAO.getInstance().getAllRoom();
            request.setAttribute("listR", r);
            request.getRequestDispatcher("View/Admin/ViewRoom.jsp").forward(request, response);
        }
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String choose = request.getParameter("choose");
        //update room
        if (choose.equals("updateRoom")) {
            String submit = request.getParameter("submit");
            if (submit != null) {
                String roomNumberstr = request.getParameter("roomNumber");
                String typeRoomIdstr = request.getParameter("typeRoomId");
                int roomNumber = 0;
                int typeRoomID = 0;
                try {
                    roomNumber = Integer.parseInt(roomNumberstr);
                    typeRoomID = Integer.parseInt(typeRoomIdstr);
                } catch (Exception e) {
                }
                dals.RoomDAO.getInstance().updateRoom(typeRoomID, roomNumber);
                response.sendRedirect("AdminRoom");
                return;
            }
        }

        //insert new room
        if (choose.equals("insertRoom")) {
            String submit = request.getParameter("submit");
            if (submit != null) {
                String roomNumberStr = request.getParameter("roomNumber");
                String typeRoomIdStr = request.getParameter("typeRoomId");

                boolean haveError = false;
                int roomNumber = 0;
                int typeRoomId = 0;

                request.setAttribute("roomNumber", roomNumberStr);

                List<TypeRoom> tr = dals.RoomDAO.getInstance().getAllTypeRoom();
                request.setAttribute("typeRoom", tr);

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
                    request.getRequestDispatcher("View/Admin/InsertRoom.jsp").forward(request, response);
                    return;
                }

                dals.RoomDAO.getInstance().insertRoom(roomNumber, typeRoomId);
                response.sendRedirect("AdminRoom");

            }
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}

