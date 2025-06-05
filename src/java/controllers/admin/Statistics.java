package controllers.admin;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class Statistics extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
<<<<<<<< HEAD:src/java/controllers/admin/Statistics.java
        request.getRequestDispatcher("/View/Admin/Statistics.jsp").forward(request, response);
========
        List<Employee> list=dal.EmployeeDAO.getInstance().getAllEmployee();
        request.setAttribute("listEmployee", list);
        request.getRequestDispatcher("/View/Admin/Dashboard.jsp").forward(request, response);
>>>>>>>> origin/HaiLong:src/java/controllers/admin/DashBoard.java
    } 

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
