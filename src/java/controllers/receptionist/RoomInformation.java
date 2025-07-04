package controllers.receptionist;

import dal.ServiceDAO;
import dal.TypeRoomDAO;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import models.DetailService;
import models.Service;
import models.TypeRoom;

@WebServlet(urlPatterns = {"/receptionist/roomInformation"})
public class RoomInformation extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();


        String startDate = (String) session.getAttribute("startDate");
        String endDate = (String) session.getAttribute("endDate");
        String[] roomNumbers = (String[]) session.getAttribute("roomNumbers"); 
        Double totalPrice = (Double) session.getAttribute("totalPrice");
        Map<String, String> roomTypeMap = (Map<String, String>) session.getAttribute("roomTypeMap");


        if (roomNumbers == null || roomNumbers.length == 0 || roomTypeMap == null) {
            request.setAttribute("errorMessage", "No rooms selected for booking.");
            request.getRequestDispatcher("/View/Receptionist/RoomInformation.jsp").forward(request, response);
            return;
        }
        if (startDate == null || endDate == null || totalPrice == null) {
            request.setAttribute("errorMessage", "Booking details are incomplete.");
            request.getRequestDispatcher("/View/Receptionist/RoomInformation.jsp").forward(request, response);
            return;
        }


        List<Map<String, Object>> roomDetails = new ArrayList<>();
        try {
            java.sql.Date startDateSql = java.sql.Date.valueOf(startDate);
            java.sql.Date endDateSql = java.sql.Date.valueOf(endDate);
            long numberOfNights = (endDateSql.getTime() - startDateSql.getTime()) / (1000 * 60 * 60 * 24);

            for (String roomNumber : roomNumbers) {
                String typeName = roomTypeMap.get(roomNumbers);
                if (typeName != null) {
                    TypeRoom typeRoom = TypeRoomDAO.getInstance().getTypeRoomByName(typeName);
                    if (typeRoom != null) {
                        Map<String, Object> roomDetail = new HashMap<>();
                        roomDetail.put("roomNumber", roomNumber);
                        roomDetail.put("typeName", typeName);
                        roomDetail.put("price", typeRoom.getPrice() * numberOfNights);
                        roomDetails.add(roomDetail);
                    }
                }
            }
        } catch (IllegalArgumentException e) {
            request.setAttribute("errorMessage", "Invalid date format.");
            request.getRequestDispatcher("/View/Receptionist/RoomInformation.jsp").forward(request, response);
            return;
        }


        request.setAttribute("startDate", startDate);
        request.setAttribute("endDate", endDate);
        request.setAttribute("roomNumbers", roomNumbers);
        request.setAttribute("roomDetails", roomDetails);
        request.setAttribute("totalPrice", totalPrice);

        List<Service> services = ServiceDAO.getInstance().getAllService();
        session.setAttribute("services", services);

        request.getRequestDispatcher("/View/Receptionist/RoomInformation.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();

        String[] roomNumbers = (String[]) session.getAttribute("roomNumbers"); 
        Double totalRoomPrice = (Double) session.getAttribute("totalPrice");
        Map<String, String> roomTypeMap = (Map<String, String>) session.getAttribute("roomTypeMap");
        if (roomNumbers == null || roomNumbers.length == 0 || totalRoomPrice == null || roomTypeMap == null) {
            request.setAttribute("errorMessage", "Booking details are missing. Please start over.");
            request.getRequestDispatcher("/View/Receptionist/RoomInformation.jsp").forward(request, response);
            return;
        }

        Map<String, List<DetailService>> roomServicesMap = new HashMap<>();
        double totalServiceCost = 0;

        for (String roomNumber : roomNumbers) {
            List<DetailService> roomServices = new ArrayList<>();
            String[] serviceIds = request.getParameterValues("serviceId_" + roomNumber);
            if (serviceIds != null) {
                for (String serviceIdStr : serviceIds) {
                    try {
                        int serviceId = Integer.parseInt(serviceIdStr);
                        String quantityParam = request.getParameter("quantity_" + roomNumber + "_" + serviceId);
                        int quantity = quantityParam != null ? Integer.parseInt(quantityParam) : 0;

                        if (quantity > 0) {
                            Service service = ServiceDAO.getInstance().getServiceById(serviceId);
                            if (service != null) {
                                DetailService ds = new DetailService();
                                ds.setService(service);
                                ds.setQuantity(quantity);
                                roomServices.add(ds);
                                totalServiceCost += service.getPrice() * quantity;
                            }
                        }
                    } catch (NumberFormatException e) {
                        
                        continue;
                    }
                }
            }
            roomServicesMap.put(roomNumber, roomServices);
        }

        session.setAttribute("roomServicesMap", roomServicesMap);

        double totalPrice = totalRoomPrice + totalServiceCost;
        session.setAttribute("totalPrice", totalPrice);

        response.sendRedirect(request.getContextPath() + "/receptionist/checkout");
    }

    @Override
    public String getServletInfo() {
        return "Handles room information and per-room service selection";
    }
}