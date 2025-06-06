package controllers.common;

import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class Register extends HttpServlet {
   
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        request.getRequestDispatcher("View/Register.jsp").forward(request, response);
    } 

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        String fullname=request.getParameter("fullname");
        isErrorName(request,fullname);
    }
    
    private boolean isErrorName(HttpServletRequest request, String fullname){
        if(fullname==null || fullname.trim().isEmpty()){
            request.setAttribute("errorName", "Name cannot be empty");
            return true;
        }
        
        
        return false;
    }
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
