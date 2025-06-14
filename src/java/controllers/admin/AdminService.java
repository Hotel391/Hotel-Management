package controllers.admin;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import models.Service;

public class AdminService extends HttpServlet {

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

        if (choose.equals("deleteService")) {
            String serviceIdStr = request.getParameter(serviceIdd);
            int serviceId = Integer.parseInt(serviceIdStr);
            dal.ServiceDAO.getInstance().deleteService(serviceId);
            response.sendRedirect(serviceServlet);
        }

        //list all service
        if (choose.equals("ViewAllService")) {
            List<Service> list = dal.ServiceDAO.getInstance().getAllService();
            request.setAttribute("listS", list);
            request.getRequestDispatcher("/View/Admin/ViewService.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String choose = request.getParameter("choose");
        List<Service> list = dal.ServiceDAO.getInstance().getAllService();

        //insert new service
        if (choose.equals("insertService")) {
            String submit = request.getParameter(submitt);
            if (submit != null) {
                String serviceName = request.getParameter(serviceNamee+"Add");
                String priceStr = request.getParameter("priceServiceAdd");
                int price = 0;
                boolean haveError = false;

                // Set lại dữ liệu đã nhập để giữ lại nếu có lỗi
                request.setAttribute(serviceNamee, serviceName);
                request.setAttribute("priceService", priceStr);

                if (serviceName == null || !serviceName.matches("^[\\p{L}0-9]+( [\\p{L}0-9]+)*$")) {
                    request.setAttribute("nameAddError", "Service name must only contain letters, digits, and a single space between words.");
                    haveError = true;
                }

                try {
                    price = Integer.parseInt(priceStr);
                    if (price < 0) {
                        request.setAttribute("priceAddError", "Price must be a positive integer.");
                        haveError = true;
                    }
                } catch (NumberFormatException e) {
                    request.setAttribute("priceAddError", "Price must be a positive integer.");
                    haveError = true;
                }

                if (haveError) {
                    request.setAttribute("listS", list);
                    request.getRequestDispatcher("/View/Admin/ViewService.jsp").forward(request, response);
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
                boolean haveError = false;
                
                if (serviceName == null || !serviceName.matches("^[\\p{L}0-9]+( [\\p{L}0-9]+)*$")) {
                    request.setAttribute("serviceNameUpdateError", "Service name must only contain letters, digits, and a single space between words.");
                    haveError = true;
                }

                
                try {
                    serviceId = Integer.parseInt(serviceIdStr);
                    price = Integer.parseInt(priceStr);
                } catch (Exception e) {
                    haveError = true;
                    request.setAttribute("priceUpdateError", "Price must be integer number");
                    
                }
                
                if (haveError) {
                    request.setAttribute("listS", list);
                    request.getRequestDispatcher("/View/Admin/ViewService.jsp").forward(request, response);
                    return;
                }


                Service s = new Service(serviceId, serviceName, price);
                dal.ServiceDAO.getInstance().updateService(s);
                response.sendRedirect(serviceServlet);
            }
        }
    }
}
