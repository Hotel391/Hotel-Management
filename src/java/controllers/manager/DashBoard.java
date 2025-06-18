package controllers.manager;

import models.DailyRevenue;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

public class DashBoard extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("roomCount", dal.RoomDAO.getInstance().roomCount());
        request.setAttribute("bookingCount", dal.BookingDAO.getInstance().bookingCount());
        request.setAttribute("checkoutCount", dal.BookingDAO.getInstance().checkoutCount());
        request.setAttribute("checkinCount", dal.BookingDetailDAO.getInstance().checkinCount());
        List<DailyRevenue> dailyRevenue = dal.BookingDAO.getInstance().totalMoneyInOneWeek();


        List<String> label = generateWeekLabels();
        List<Integer> data = mapRevenueToWeekLabels(dailyRevenue,label);
        request.setAttribute("labels", label);
        request.setAttribute("data", data);
        request.setAttribute("availableRoomCount", dal.RoomDAO.getInstance().roomAvailableCount());
        request.setAttribute("soldOutRoomCount", dal.RoomDAO.getInstance().roomBookedCount());
        request.setAttribute("employeeCount", dal.EmployeeDAO.getInstance().countEmployee());
        request.setAttribute("customerCount", dal.CustomerDAO.getInstance().customerCount());
        request.getRequestDispatcher("/View/Admin/Dashboard.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }

    public static List<String> generateWeekLabels() {
        List<String> fullWeek = Arrays.asList("Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday");
        Calendar calendar = Calendar.getInstance();
        int todayIndex = calendar.get(Calendar.DAY_OF_WEEK) - 1;

        List<String> label = new ArrayList<>();
        for (int i = 1; i <= 7; i++) {
            label.add(fullWeek.get((todayIndex + i) % 7));
        }
        return label;
    }

    public static List<Integer> mapRevenueToWeekLabels(List<DailyRevenue> revenues, List<String> label) {
        List<Integer> data = new ArrayList<>(Collections.nCopies(7, 0));
        for (DailyRevenue dr : revenues) {
            int index = label.indexOf(dr.getWeekdayName());
            if (index != -1) {
                data.set(index, (int) dr.getTotalPrice());
            }
        }
        return data;
    }
}
