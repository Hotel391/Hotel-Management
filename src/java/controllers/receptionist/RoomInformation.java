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
            request.setAttribute("errorMessage", "Không có phòng nào được chọn để đặt.");
            request.getRequestDispatcher("/View/Receptionist/RoomInformation.jsp").forward(request, response);
            return;
        }
        if (startDate == null || endDate == null || totalPrice == null) {
            request.setAttribute("errorMessage", "Thông tin đặt phòng chưa đầy đủ.");
            request.getRequestDispatcher("/View/Receptionist/RoomInformation.jsp").forward(request, response);
            return;
        }

        List<Map<String, Object>> roomDetails = new ArrayList<>();
        Map<Integer, Integer> includedServiceQuantities = new HashMap<>();

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

                    List<DetailService> includedServices = new ArrayList<>();
                    for (Service s : ServiceDAO.getInstance().getServicesByTypeRoom(typeRoom.getTypeId())) {
                        DetailService ds = new DetailService();
                        ds.setService(s);
                        int quantity = s.getServiceName().equalsIgnoreCase("Ăn sáng") || s.getServiceName().equalsIgnoreCase("Spa") ? (int) numberOfNights : 1;
                        ds.setQuantity(quantity);
                        includedServices.add(ds);
                        includedServiceQuantities.put(s.getServiceId(), quantity);
                    }
                    roomDetail.put("includedServices", includedServices);

                    List<Service> optionalServices = ServiceDAO.getInstance().getAllService();
                    roomDetail.put("optionalServices", optionalServices);

                    roomDetails.add(roomDetail);
                }
            }
        }

        session.setAttribute("includedServiceQuantities", includedServiceQuantities);

        request.setAttribute("startDate", startDate);
        request.setAttribute("endDate", endDate);
        request.setAttribute("roomNumbers", roomNumbers);
        request.setAttribute("roomDetails", roomDetails);
        request.setAttribute("totalPrice", totalPrice);
        request.setAttribute("numberOfNights", numberOfNights);

        request.getRequestDispatcher("/View/Receptionist/RoomInformation.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();

        String[] roomNumbers = (String[]) session.getAttribute("roomNumbers");
        Double totalRoomPrice = (Double) session.getAttribute("totalPrice");
        Map<String, String> roomTypeMap = (Map<String, String>) session.getAttribute("roomTypeMap");
        String startDate = (String) session.getAttribute("startDate");
        String endDate = (String) session.getAttribute("endDate");
        Map<Integer, Integer> includedServiceQuantities = (Map<Integer, Integer>) session.getAttribute("includedServiceQuantities");

        if (roomNumbers == null || roomNumbers.length == 0 || totalRoomPrice == null || roomTypeMap == null || includedServiceQuantities == null) {
            request.setAttribute("errorMessage", "Thông tin đặt phòng bị thiếu. Vui lòng bắt đầu lại.");
            request.getRequestDispatcher("/View/Receptionist/RoomInformation.jsp").forward(request, response);
            return;
        }

        java.sql.Date startDateSql = java.sql.Date.valueOf(startDate);
        java.sql.Date endDateSql = java.sql.Date.valueOf(endDate);
        long numberOfNights = (endDateSql.getTime() - startDateSql.getTime()) / (1000 * 60 * 60 * 24);

        Map<String, List<DetailService>> roomServicesMap = new HashMap<>();
        double totalServiceCost = 0;

        for (String roomNumber : roomNumbers) {
            List<DetailService> roomServices = new ArrayList<>();
            Map<Integer, DetailService> serviceMap = new HashMap<>();

            String typeName = roomTypeMap.get(roomNumber);
            TypeRoom typeRoom = TypeRoomDAO.getInstance().getTypeRoomByName(typeName);
            List<Service> includedServices = ServiceDAO.getInstance().getServicesByTypeRoom(typeRoom.getTypeId());

            for (Service s : includedServices) {
                Integer quantity = includedServiceQuantities.getOrDefault(s.getServiceId(), s.getServiceName().equalsIgnoreCase("Ăn sáng") || s.getServiceName().equalsIgnoreCase("Spa") ? (int) numberOfNights : 1);
                DetailService ds = new DetailService();
                ds.setService(s);
                ds.setQuantity(quantity);
                serviceMap.put(s.getServiceId(), ds);
                System.out.println("Included Service: " + s.getServiceName() + ", Quantity: " + quantity + ", Room: " + roomNumber);
            }

            String[] selectedIds = request.getParameterValues("serviceId_" + roomNumber);
            if (selectedIds != null) {
                for (String serviceIdStr : selectedIds) {
                    try {
                        int serviceId = Integer.parseInt(serviceIdStr);
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
                                    System.out.println("Invalid quantity for serviceId: " + serviceId + ", Room: " + roomNumber + ", Defaulting to 1");
                                }
                            }

                            if (serviceMap.containsKey(serviceId)) {
                                DetailService existingDs = serviceMap.get(serviceId);
                                existingDs.setQuantity(existingDs.getQuantity() + quantity);
                            } else {
                                DetailService ds = new DetailService();
                                ds.setService(service);
                                ds.setQuantity(quantity);
                                serviceMap.put(serviceId, ds);
                            }

                            double cost = service.getPrice() * quantity;
                            totalServiceCost += cost;
                            System.out.println("Optional Service: " + service.getServiceName() + ", Price: " + service.getPrice() + ", Quantity: " + quantity + ", Cost: " + cost + ", Room: " + roomNumber);
                        } else {
                            System.out.println("Service not found for serviceId: " + serviceId + ", Room: " + roomNumber);
                        }
                    } catch (NumberFormatException ignored) {
                        System.out.println("Invalid serviceId: " + serviceIdStr + ", Room: " + roomNumber);
                    }
                }
            } else {
                System.out.println("No optional services selected for room: " + roomNumber);
            }

            roomServices.addAll(serviceMap.values());
            roomServicesMap.put(roomNumber, roomServices);
        }

        session.setAttribute("roomServicesMap", roomServicesMap);
        session.setAttribute("totalPrice", totalRoomPrice + totalServiceCost);
        System.out.println("Total Service Cost: " + totalServiceCost);
        System.out.println("Total Price: " + (totalRoomPrice + totalServiceCost));

        response.sendRedirect(request.getContextPath() + "/receptionist/checkout");
    }

    @Override
    public String getServletInfo() {
        return "Handles room info and service selection";
    }
}