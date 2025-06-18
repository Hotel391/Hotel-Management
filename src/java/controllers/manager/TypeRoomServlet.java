package controllers.manager;

import models.TypeRoom;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;

@WebServlet(name = "TypeRoomServlet", urlPatterns = {"/admin/types"})
public class TypeRoomServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        List<TypeRoom> typeRoomList;

        //Search Type Room
        String key = request.getParameter("key");

        System.out.println("key: " + key);
        
        int typeRoomTotal;
        
        int endPage;

        if (key != null && !key.trim().isEmpty()) {
            typeRoomTotal = dal.TypeRoomDAO.getInstance().searchTypeRoom(key).size();

            endPage = typeRoomTotal / 5;

            if (typeRoomTotal % 5 != 0) {
                endPage++;
            }
            
            request.setAttribute("key", key);
        } else {
            //Take pagination numbers

            typeRoomTotal = dal.TypeRoomDAO.getInstance().getTypeRoomQuantity();

            endPage = typeRoomTotal / 5;

            if (typeRoomTotal % 5 != 0) {
                endPage++;
            }
        }

            request.setAttribute("endPage", endPage);

            //Take data number each page
            int indexPage = Integer.parseInt(request.getParameter("page") == null ? "1" : request.getParameter("page"));

            typeRoomList = dal.TypeRoomDAO.getInstance().typeRoomPagination(indexPage, key);

            request.setAttribute("currentPage", indexPage);


        request.setAttribute("typeRoom", typeRoomList);

        request.getRequestDispatcher("/View/Admin/TypeRoom.jsp").forward(request, response);

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
        return "Short description";
    }// </editor-fold>

}
