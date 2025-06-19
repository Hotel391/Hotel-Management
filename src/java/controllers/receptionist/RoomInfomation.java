package controllers.receptionist;

import dal.RoomDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import models.Booking;
import models.BookingDetail;
import models.DetailService;
import models.Room;
import models.RoomNService;
import models.Service;

@WebServlet(name = "RoomInfomation", urlPatterns = {"/receptionist/roomInfomation", "/receptionist/confirmBooking"})
public class RoomInfomation extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String startDateStr = request.getParameter("startDate");
        String endDateStr = request.getParameter("endDate");
        String roomNumberStr = request.getParameter("roomNumber");
        String typeRoomIdStr = request.getParameter("typeRoomId");

        if (startDateStr == null || startDateStr.isEmpty() || endDateStr == null || endDateStr.isEmpty()
                || roomNumberStr == null || roomNumberStr.isEmpty()) {
            request.setAttribute("errorMessage", "Hãy nhập ngày và số phòng.");
            request.setAttribute("startDateSearch", startDateStr != null ? startDateStr : "");
            request.setAttribute("endDateSearch", endDateStr != null ? endDateStr : "");
            request.setAttribute("typeRoomIdSearch", typeRoomIdStr != null ? typeRoomIdStr : "");
            request.setAttribute("currentPage", 1);
            request.setAttribute("typeRooms", RoomDAO.getInstance().getAllTypeRoom());
            request.getRequestDispatcher("/View/Receptionist/SearchRoom.jsp").forward(request, response);
            return;
        }

        int roomNumber;
        try {
            roomNumber = Integer.parseInt(roomNumberStr);
        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage", "Invalid room number format.");
            request.setAttribute("startDateSearch", startDateStr);
            request.setAttribute("endDateSearch", endDateStr);
            request.setAttribute("typeRoomIdSearch", typeRoomIdStr != null ? typeRoomIdStr : "");
            request.setAttribute("currentPage", 1);
            request.setAttribute("typeRooms", RoomDAO.getInstance().getAllTypeRoom());
            request.getRequestDispatcher("/View/Receptionist/SearchRoom.jsp").forward(request, response);
            return;
        }

        Date startDate, endDate;
        Room room = RoomDAO.getInstance().getRoomByNumber(roomNumber);
       
        List<RoomNService> roomServices = RoomDAO.getInstance().getServicesForRoom(room.getTypeRoom().getTypeId());

        request.setAttribute("startDate", startDateStr);
        request.setAttribute("endDate", endDateStr);
        request.setAttribute("roomNumber", roomNumber);
        request.setAttribute("room", room);
        request.setAttribute("roomServices", roomServices);

        request.getRequestDispatcher("/View/Receptionist/RoomInformation.jsp").forward(request, response);
    }

   @Override
protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

    HttpSession session = request.getSession();

    String startDateStr = request.getParameter("startDate");
    String endDateStr = request.getParameter("endDate");
    String roomNumberStr = request.getParameter("roomNumber");
    String[] selectedServices = request.getParameterValues("selectedServices");

    if (startDateStr == null || endDateStr == null || roomNumberStr == null
            || startDateStr.isEmpty() || endDateStr.isEmpty() || roomNumberStr.isEmpty()) {
        request.setAttribute("errorMessage", "Missing required booking details.");
        request.getRequestDispatcher("/View/Receptionist/RoomInformation.jsp").forward(request, response);
        return;
    }

    int roomNumber;
    try {
        roomNumber = Integer.parseInt(roomNumberStr);
    } catch (NumberFormatException e) {
        request.setAttribute("errorMessage", "Invalid room number format.");
        request.getRequestDispatcher("/View/Receptionist/RoomInformation.jsp").forward(request, response);
        return;
    }

    Date startDate, endDate;
    try {
        startDate = Date.valueOf(startDateStr);
        endDate = Date.valueOf(endDateStr);
    } catch (IllegalArgumentException e) {
        request.setAttribute("errorMessage", "Invalid date format.");
        request.getRequestDispatcher("/View/Receptionist/RoomInformation.jsp").forward(request, response);
        return;
    }

    Room room = RoomDAO.getInstance().getRoomByNumber(roomNumber);
    if (room == null) {
        request.setAttribute("errorMessage", "Room not found.");
        request.getRequestDispatcher("/View/Receptionist/RoomInformation.jsp").forward(request, response);
        return;
    }



    List<Service> services = new ArrayList<>();    int totalPrice = room.getTypeRoom().getPrice() * (int) java.time.temporal.ChronoUnit.DAYS.between(startDate.toLocalDate(), endDate.toLocalDate());
    List<DetailService> detailServices = new ArrayList<>();
     if (selectedServices != null) {
        for (String serviceIdStr : selectedServices) {
            try {
                int serviceId = Integer.parseInt(serviceIdStr); 
                String quantityStr = request.getParameter("serviceQuantities[" + serviceId + "]");  
                int quantity = quantityStr != null ? Integer.parseInt(quantityStr) : 0;

                if (quantity >= 0) {
                    Service service = RoomDAO.getInstance().getServiceById(serviceId);  
                    if (service != null) {
                 
                        DetailService detailService = new DetailService();
                        detailService.setService(service);
                        detailService.setQuantity(quantity);
                        detailServices.add(detailService);
                    }
                }
            } catch (NumberFormatException e) {
            }
        }
    }

    Booking booking = new Booking();
    booking.setStatus("PENDING");

    BookingDetail bookingDetail = new BookingDetail();
    bookingDetail.setStartDate(startDate);
    bookingDetail.setEndDate(endDate);
    bookingDetail.setRoom(room);
    bookingDetail.setBooking(booking);
    bookingDetail.setServices(services);

    for (DetailService ds : detailServices) {
        ds.setBookingDetail(bookingDetail);
    }

    List<BookingDetail> bookingDetails = new ArrayList<>();
    bookingDetails.add(bookingDetail);

    booking.setTotalPrice(totalPrice); 

    session.setAttribute("detailServices", detailServices);
    session.setAttribute("booking", booking);
    session.setAttribute("bookingDetails", bookingDetails);
    session.setAttribute("bookingStartDate", startDateStr);
    session.setAttribute("bookingEndDate", endDateStr);
    session.setAttribute("bookingRoomNumber", roomNumber);
    session.setAttribute("bookingRoomType", room.getTypeRoom().getTypeName());
    session.setAttribute("bookingServices", detailServices);

    response.sendRedirect(request.getContextPath() + "/View/Receptionist/BookingConfirmation.jsp");
}



    @Override
    public String getServletInfo() {
        return "Room Information";
    }
}
