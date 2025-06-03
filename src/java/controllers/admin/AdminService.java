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
import models.Service;

/**
 *
 * @author Tuan'sPC
 */
public class AdminService extends HttpServlet {


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
            choose = "ViewAllService";
        }
        
        if(choose.equals("deleteService")){
            String serviceIdStr = request.getParameter("serviceId");
            int serviceId = Integer.parseInt(serviceIdStr);
            dal.ServiceDAO.getInstance().deleteService(serviceId);
            response.sendRedirect("service");
        }

        //forward to InsertService.jsp
        if (choose.equals("insertService")) {
            String submit = request.getParameter("submit");
            if (submit == null) {
                request.getRequestDispatcher("/View/Admin/InsertService.jsp").forward(request, response);
            }
        }

        //forward to UpdateService.jsp
        if (choose.equals("updateService")) {
            String submit = request.getParameter("submit");
            if (submit == null) {
                String serviceID = request.getParameter("serviceId");
                String serviceName = request.getParameter("serviceName");
                String price = request.getParameter("price");
                request.setAttribute("serviceId", serviceID);
                request.setAttribute("serviceName", serviceName);
                request.setAttribute("price", price);
                request.getRequestDispatcher("/View/Admin/UpdateService.jsp").forward(request, response);
            }
        }

        //list all service
        if (choose.equals("ViewAllService")) {
            List<Service> list = dal.ServiceDAO.getInstance().getAllService();
            request.setAttribute("listS", list);
            request.getRequestDispatcher("/View/Admin/ViewService.jsp").forward(request, response);
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

        //insert new service
        if (choose.equals("insertService")) {
            String submit = request.getParameter("submit");
            if (submit != null) {
                String serviceName = request.getParameter("serviceName");
                String priceStr = request.getParameter("priceService");
                int price = 0;
                boolean haveError = false;

                // Set lại dữ liệu đã nhập để giữ lại nếu có lỗi
                request.setAttribute("serviceName", serviceName);
                request.setAttribute("priceService", priceStr);

                if (serviceName == null || !serviceName.matches("^[a-zA-Z0-9\\s]+$")) {
                    request.setAttribute("nameError", "Service name must not contain special characters.");
                    haveError = true;
                }

                try {
                    price = Integer.parseInt(priceStr);
                    if (price < 0) {
                        request.setAttribute("priceError", "Price must be a positive integer.");
                        haveError = true;
                    }
                } catch (NumberFormatException e) {
                    request.setAttribute("priceError", "Price must be a positive integer.");
                    haveError = true;
                }

                if (haveError) {
                    request.getRequestDispatcher("/View/Admin/InsertService.jsp").forward(request, response);
                    return;
                }

                dal.ServiceDAO.getInstance().insertService(serviceName, price);
                response.sendRedirect("service");
                
            }
        }

        //update service
        if (choose.equals("updateService")) {
            String submit = request.getParameter("submit");
            if (submit != null) {
                String serviceIdStr = request.getParameter("serviceId");
                String serviceName = request.getParameter("serviceName");
                String priceStr = request.getParameter("price");
                int serviceId = 0;
                int price = 0;
                try {
                    serviceId = Integer.parseInt(serviceIdStr);
                    price = Integer.parseInt(priceStr);
                } catch (Exception e) {
                    request.setAttribute("serviceId", serviceIdStr);
                    request.setAttribute("serviceName", serviceName);
                    request.setAttribute("price", priceStr);
                    request.setAttribute("error", "Price must be integer number");
                    request.getRequestDispatcher("/View/Admin/UpdateService.jsp").forward(request, response);
                }

                Service s = new Service(serviceId, serviceName, price);
                dal.ServiceDAO.getInstance().updateService(s);
                response.sendRedirect("service");
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
