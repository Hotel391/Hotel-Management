package Controllers.Admin;

import Models.DailyRevenue;
import Models.Employee;
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
        request.setAttribute("roomCount", DAL.RoomDAO.getInstance().RoomCount());
        request.setAttribute("bookingCount", DAL.BookingDAO.getInstance().BookingCount());
        request.setAttribute("checkoutCount", DAL.BookingDAO.getInstance().CheckoutCount());
        request.setAttribute("checkinCount", DAL.BookingDetailDAO.getInstance().CheckinCount());
        List<DailyRevenue> dailyRevenue = DAL.BookingDAO.getInstance().totalMoneyInOneWeek();
        List<String> fullWeek = Arrays.asList(
                "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"
        );

        Calendar calendar = Calendar.getInstance();
        int todayIndex = calendar.get(Calendar.DAY_OF_WEEK) - 1;  // Calendar: Sunday=1, nên trừ 1 để match index

        List<String> label = new ArrayList<>();
        for (int i = 1; i <= 7; i++) {
            label.add(fullWeek.get((todayIndex + i) % 7));
        }
        List<Integer> data = new ArrayList<>(Collections.nCopies(7, 0));

        for (DailyRevenue dr : dailyRevenue) {
            String weekday = dr.getWeekdayName();
            int index = label.indexOf(weekday);
            if (index != -1) {
                data.set(index, (int) dr.getTotalPrice());
            }
        }
        request.setAttribute("labels", label);
        request.setAttribute("data", data);
        request.setAttribute("availableRoomCount", DAL.RoomDAO.getInstance().RoomAvailableCount());
        request.setAttribute("soldOutRoomCount", DAL.RoomDAO.getInstance().RoomBookedCount());
        request.setAttribute("employeeCount", DAL.EmployeeDAO.getInstance().countEmployee());
        request.setAttribute("customerCount", DAL.CustomerDAO.getInstance().customerCount());
        request.getRequestDispatcher("/View/Admin/Dashboard.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }

}
