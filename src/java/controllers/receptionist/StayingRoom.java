package controllers.receptionist;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import models.BookingDetail;
import models.Customer;
import models.DetailService;
import models.Service;
import websocket.RoomStatusSocket;

/**
 *
 * @author HieuTT
 */
@WebServlet(name = "StayingRoom", urlPatterns = { "/receptionist/stayingRoom" })
public class StayingRoom extends HttpServlet {

    private static final int NUMBER_OF_ROWS = 8;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String search = request.getParameter("search");
        String pageStr = request.getParameter("page");
        String oldSearch = request.getParameter("oldSearch");
        String action = request.getParameter("action");
        if (pageStr == null || pageStr.isEmpty()) {
            pageStr = "1";
        }
        int currentPage = Integer.parseInt(pageStr);

        if (action != null && action.equals("viewService")) {
            request.setAttribute("action", action);
            handleViewService(request);
        } else if (action != null && action.equals("viewCustomer")) {
            request.setAttribute("action", action);
            handleViewCustomer(request);
        }

        if (search != null && !search.isEmpty()) {
            search = search.trim();
            if (oldSearch == null || !oldSearch.equals(search)) {
                currentPage = 1;
            }
            handleSearch(request, search, currentPage);
        } else {
            handleDefault(request, currentPage);
        }
        request.setAttribute("currentPage", currentPage);
        request.getRequestDispatcher("/View/Receptionist/StayingRoom.jsp").forward(request, response);

    }

    private void handleDefault(HttpServletRequest request, int currentPage)
            throws ServletException, IOException {
        List<BookingDetail> bookingDetails = dal.BookingDetailDAO.getInstance().getStayingRooms(NUMBER_OF_ROWS,
                currentPage, "");
        request.setAttribute("bookingDetails", bookingDetails);
        int totalStayingRooms = dal.BookingDetailDAO.getInstance().getTotalStayingRooms("");
        int totalPages = (int) Math.ceil((double) totalStayingRooms / NUMBER_OF_ROWS);
        request.setAttribute("totalPages", totalPages);
    }

    private void handleSearch(HttpServletRequest request, String search, int currentPage)
            throws ServletException, IOException {
        request.setAttribute("search", search);
        List<BookingDetail> bookingDetails = dal.BookingDetailDAO.getInstance().getStayingRooms(NUMBER_OF_ROWS,
                currentPage, search);
        request.setAttribute("bookingDetails", bookingDetails);
        int totalStayingRooms = dal.BookingDetailDAO.getInstance().getTotalStayingRooms(search);
        int totalPages = (int) Math.ceil((double) totalStayingRooms / NUMBER_OF_ROWS);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("oldSearch", search);
    }

    private void handleViewService(HttpServletRequest request)
            throws ServletException, IOException {
        String bookingDetailId = request.getParameter("bookingDetailId");
        List<DetailService> detailService = dal.ServiceDAO.getInstance().getServiceByBookingDetailId(Integer.parseInt(bookingDetailId));
        request.setAttribute("detailService", detailService);
        List<Service> otherServices = dal.ServiceDAO.getInstance().getServiceNotInBookingDetail(Integer.parseInt(bookingDetailId));
        request.setAttribute("otherServices", otherServices);
    }

    private void handleViewCustomer(HttpServletRequest request)
            throws ServletException, IOException {
        int bookingDetailId = Integer.parseInt(request.getParameter("bookingDetailId"));
        Customer customer = dal.CustomerDAO.getInstance().getCustomerByBookingDetailId(bookingDetailId);
        request.setAttribute("customer", customer);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String bookingDetailId = request.getParameter("bookingDetailId");
        String action = request.getParameter("action");
        if ("updateServices".equals(action)) {
            handleUpdateServices(request, bookingDetailId);
        } else {
            handleUpdateStatus(request);
        }

        String redirectUrl = buildRedirectUrl(request, action, bookingDetailId);
        response.sendRedirect(redirectUrl);
    }

    private void handleUpdateServices(HttpServletRequest request, String bookingDetailId)
            throws ServletException, IOException {
        String[] serviceIds = request.getParameterValues("serviceIds");
        String[] otherServiceIds = request.getParameterValues("otherServiceIds");
        String[] otherQuantitiesStr = request.getParameterValues("otherQuantities");
        List<DetailService> detailService = dal.ServiceDAO.getInstance().getServiceByBookingDetailId(Integer.parseInt(bookingDetailId));
        if (serviceIds != null && serviceIds.length > 0) {
            updateExistingServices(request, bookingDetailId, serviceIds, detailService);
        }
        // Handle other services
        if (otherServiceIds != null && otherServiceIds.length > 0) {
            for (int i = 0; i < otherServiceIds.length; i++) {
                int serviceId = Integer.parseInt(otherServiceIds[i]);
                int quantity = Integer.parseInt(otherQuantitiesStr[i]);
                if (quantity > 0) {
                    dal.ServiceDAO.getInstance().insertDetailService(Integer.parseInt(bookingDetailId), serviceId, quantity);
                }
            }
        }
    }

    private void updateExistingServices(HttpServletRequest request, String bookingDetailId, String[] serviceIds,
            List<DetailService> detailService) {
        Set<Integer> selectedServiceIds = new HashSet<>();

        for (String sid : serviceIds) {
            int serviceId = Integer.parseInt(sid);
            selectedServiceIds.add(serviceId);

            int quantity = Integer.parseInt(request.getParameter("quantity_" + sid));
            int oldQuantity = Integer.parseInt(request.getParameter("oldQuantity_" + sid));

            if (quantity != oldQuantity) {
                dal.ServiceDAO.getInstance().updateDetailService(Integer.parseInt(bookingDetailId), serviceId, quantity, oldQuantity);
            }
        }

        for (DetailService ds : detailService) {
            int existingServiceId = ds.getService().getServiceId();
            if (!selectedServiceIds.contains(existingServiceId)) {
                int oldQuantity = Integer.parseInt(request.getParameter("oldQuantity_" + existingServiceId));
                dal.ServiceDAO.getInstance().deleteServiceInBookingDetail(Integer.parseInt(bookingDetailId), existingServiceId,
                        oldQuantity);
            }
        }
    }

    private void handleUpdateStatus(HttpServletRequest request)
            throws ServletException, IOException {
        String roomNumber = request.getParameter("roomNumber");
        boolean isCleaner = Boolean.parseBoolean(request.getParameter("status"));
        int roomId = Integer.parseInt(roomNumber);
        dal.RoomDAO.getInstance().updateRoomStatus(roomId, !isCleaner);
        RoomStatusSocket.broadcast("{\"roomId\":" + roomId + "}");
    }

    private String buildRedirectUrl(HttpServletRequest request, String action, String bookingDetailId) {
        StringBuilder url = new StringBuilder(request.getContextPath() + "/receptionist/stayingRoom");
        String page = request.getParameter("page");
        String search = request.getParameter("search");
        String oldSearch = request.getParameter("oldSearch");
        boolean hasParam = false;

        if (page != null && !page.isEmpty() && !"1".equals(page)) {
            url.append("?page=").append(page);
            hasParam = true;
        }
        if (search != null && !search.isEmpty()) {
            url.append(hasParam ? "&" : "?").append("search=").append(search);
            hasParam = true;
        }
        if (oldSearch != null && !oldSearch.isEmpty()) {
            url.append(hasParam ? "&" : "?").append("oldSearch=").append(oldSearch);
            hasParam = true;
        }
        if ("updateServices".equals(action)) {
            url.append(hasParam ? "&" : "?").append("action=viewService");
            url.append("&bookingDetailId=").append(bookingDetailId);
            url.append("&success=1");
        }
        return url.toString();
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
