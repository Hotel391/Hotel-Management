package controllers.manager;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import models.Service;


public class ManagerService extends HttpServlet {

    private String serviceIdd = "serviceId";
    private String serviceServlet = "service";
    private String submitt = "submit";
    private String serviceNamee = "serviceName";
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String choose = request.getParameter("choose");
        if (choose == null) {
            choose = "ViewAllService";
        }
        
        if(choose.equals("deleteService")){
            String serviceIdStr = request.getParameter(serviceIdd);
            int serviceId = Integer.parseInt(serviceIdStr);
            dal.ServiceDAO.getInstance().deleteService(serviceId);
            response.sendRedirect(serviceServlet);
        }

        //forward to InsertService.jsp
        if (choose.equals("insertService")) {
            String submit = request.getParameter(submitt);
            if (submit == null) {
                request.getRequestDispatcher("/View/Manager/InsertService.jsp").forward(request, response);
            }
        }

        //forward to UpdateService.jsp
        if (choose.equals("updateService")) {
            String submit = request.getParameter(submitt);
            if (submit == null) {
                String serviceID = request.getParameter(serviceIdd);
                String serviceName = request.getParameter(serviceNamee);
                String price = request.getParameter("price");
                request.setAttribute(serviceIdd, serviceID);
                request.setAttribute(serviceNamee, serviceName);
                request.setAttribute("price", price);
                request.getRequestDispatcher("/View/Manager/UpdateService.jsp").forward(request, response);
            }
        }

        //list all service
        if (choose.equals("ViewAllService")) {
            List<Service> list = dal.ServiceDAO.getInstance().getAllService();
            request.setAttribute("listS", list);
            request.getRequestDispatcher("/View/Manager/ViewService.jsp").forward(request, response);
        }
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String choose = request.getParameter("choose");

        //insert new service
        if (choose.equals("insertService")) {
            String submit = request.getParameter(submitt);
            if (submit != null) {
                String serviceName = request.getParameter(serviceNamee);
                String priceStr = request.getParameter("priceService");
                int price = 0;
                boolean haveError = false;

                // Set lại dữ liệu đã nhập để giữ lại nếu có lỗi
                request.setAttribute(serviceNamee, serviceName);
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
                    request.getRequestDispatcher("/View/Manager/InsertService.jsp").forward(request, response);
                    return;
                }

                dal.ServiceDAO.getInstance().insertService(serviceName, price);
                response.sendRedirect(serviceServlet);
                
            }
        }

        //update service
        if (choose.equals("updateService")) {
            String submit = request.getParameter(submitt);
            if (submit != null) {
                String serviceIdStr = request.getParameter(serviceIdd);
                String serviceName = request.getParameter(serviceNamee);
                String priceStr = request.getParameter("price");
                int serviceId = 0;
                int price = 0;
                try {
                    serviceId = Integer.parseInt(serviceIdStr);
                    price = Integer.parseInt(priceStr);
                } catch (Exception e) {
                    request.setAttribute(serviceIdd, serviceIdStr);
                    request.setAttribute(serviceNamee, serviceName);
                    request.setAttribute("price", priceStr);
                    request.setAttribute("error", "Price must be integer number");
                    request.getRequestDispatcher("/View/Manager/UpdateService.jsp").forward(request, response);
                }

                Service s = new Service(serviceId, serviceName, price);
                dal.ServiceDAO.getInstance().updateService(s);
                response.sendRedirect(serviceServlet);
            }
        }
    }
}
