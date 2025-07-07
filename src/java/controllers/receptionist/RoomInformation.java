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

        java.sql.Date startDateSql = java.sql.Date.valueOf(startDate);
        java.sql.Date endDateSql = java.sql.Date.valueOf(endDate);
        long numberOfNights = (endDateSql.getTime() - startDateSql.getTime()) / (1000 * 60 * 60 * 24);

        for (String roomNumber : roomNumbers) {
            String typeName = roomTypeMap.get(roomNumber);
            if (typeName != null) {
                TypeRoom typeRoom = TypeRoomDAO.getInstance().getTypeRoomByName(typeName);
                if (typeRoom != null) {
                    Map<String, Object> roomDetail = new HashMap<>();
                    roomDetail.put("roomNumber", roomNumber);
                    roomDetail.put("typeName", typeName);
                    roomDetail.put("price", typeRoom.getPrice() * numberOfNights);

                    List<Service> includedServices = ServiceDAO.getInstance().getServicesByTypeRoom(typeRoom.getTypeId());
                    roomDetail.put("includedServices", includedServices);

                    List<Service> allServices = ServiceDAO.getInstance().getAllService();
                    List<Service> optionalServices = new ArrayList<>();
                    for (Service s : allServices) {
                        if (!includedServices.contains(s)) {
                            optionalServices.add(s);
                        }
                    }
                    roomDetail.put("optionalServices", optionalServices);

                    roomDetails.add(roomDetail);
                }
            }
        }

        request.setAttribute("startDate", startDate);
        request.setAttribute("endDate", endDate);
        request.setAttribute("roomNumbers", roomNumbers);
        request.setAttribute("roomDetails", roomDetails);
        request.setAttribute("totalPrice", totalPrice);

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

            String typeName = roomTypeMap.get(roomNumber);
            TypeRoom typeRoom = TypeRoomDAO.getInstance().getTypeRoomByName(typeName);

            List<Service> includedServices = ServiceDAO.getInstance().getServicesByTypeRoom(typeRoom.getTypeId());
            for (Service s : includedServices) {
                DetailService ds = new DetailService();
                ds.setService(s);
                ds.setQuantity(1);
                roomServices.add(ds);
            }

            String[] selectedIds = request.getParameterValues("serviceId_" + roomNumber);
            if (selectedIds != null) {
                for (String serviceIdStr : selectedIds) {
                    try {
                        int serviceId = Integer.parseInt(serviceIdStr);

                        if (includedServices.stream().anyMatch(s -> s.getServiceId() == serviceId)) {
                            continue;
                        }

                        Service service = ServiceDAO.getInstance().getServiceById(serviceId);
                        if (service != null) {
                            int quantity = 1; 
                            String qtyParam = request.getParameter("quantity_" + roomNumber + "_" + serviceId);
                            if (qtyParam != null) {
                                try {
                                    quantity = Integer.parseInt(qtyParam);
                                    if (quantity <= 0) {
                                        quantity = 1;
                                    }
                                } catch (NumberFormatException ignored) {
                                }
                            }

                            DetailService ds = new DetailService();
                            ds.setService(service);
                            ds.setQuantity(quantity);
                            roomServices.add(ds);
                            totalServiceCost += service.getPrice() * quantity;
                        }

                    } catch (NumberFormatException ignored) {
                    }
                }
            }

            roomServicesMap.put(roomNumber, roomServices);
        }

        session.setAttribute("roomServicesMap", roomServicesMap);
        session.setAttribute("totalPrice", totalRoomPrice + totalServiceCost);

        response.sendRedirect(request.getContextPath() + "/receptionist/checkout");
    }

    @Override
    public String getServletInfo() {
        return "Handles room info and service selection";
    }
}
