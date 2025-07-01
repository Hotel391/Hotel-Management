package controllers.receptionist;

import dal.ServiceDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import models.DetailService;
import models.Service;

@WebServlet(urlPatterns = {"/receptionist/roomInformation"})
public class RoomInformation extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();

        List<Service> services = ServiceDAO.getInstance().getAllService();
        session.setAttribute("services", services);

        request.getRequestDispatcher("/View/Receptionist/RoomInformation.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();

        String[] serviceIds = request.getParameterValues("serviceId");
        String[] quantities = request.getParameterValues("quantity");

        List<DetailService> listServices = new ArrayList<>();
        int totalServiceCost = 0;

        if (serviceIds != null && quantities != null) {
            for (int i = 0; i < serviceIds.length; i++) {
                int serviceId = Integer.parseInt(serviceIds[i]);
                int quantity = Integer.parseInt(quantities[i]);

                if (quantity > 0) {
                    Service service = ServiceDAO.getInstance().getServiceById(serviceId);
                    DetailService ds = new DetailService();
                    ds.setService(service);
                    ds.setQuantity(quantity);

                    totalServiceCost += service.getPrice() * quantity;

                    listServices.add(ds);
                }
            }
        }

        session.setAttribute("selectedServices", listServices);

       
        Object obj = session.getAttribute("totalPrice");
        int totalRoomPrice = (obj instanceof Number) ? ((Number) obj).intValue() : 0;

        int totalPrice = totalRoomPrice + totalServiceCost;
        session.setAttribute("totalPrice", totalPrice);

        response.sendRedirect(request.getContextPath() + "/receptionist/checkout");
    }

    @Override
    public String getServletInfo() {
        return "Handles room information and service selection";
    }

}
