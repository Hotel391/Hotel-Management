package controllers.admin;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

public class Statistics extends HttpServlet {

    private static final String TYPE_X_FIELD = "typeX";
    private static final String START_YEAR_FIELD = "startYear";
    private static final String END_YEAR_FIELD = "endYear";
    private static final String START_MONTH_FIELD = "startMonth";
    private static final String END_MONTH_FIELD = "endMonth";
    private static final String START_QUARTER_FIELD = "startQuarter";
    private static final String END_QUARTER_FIELD = "endQuarter";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int currentYear = LocalDate.now().getYear();
        request.setAttribute("currentYear", currentYear);
        request.getRequestDispatcher("/View/Admin/Statistics.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int currentYear = LocalDate.now().getYear();
        request.setAttribute("currentYear", currentYear);
        String typeX = request.getParameter(TYPE_X_FIELD);
        int startYear = Integer.parseInt(request.getParameter(START_YEAR_FIELD));
        int endYear = Integer.parseInt(request.getParameter(END_YEAR_FIELD));
        int startMonth = Integer.parseInt(request.getParameter(START_MONTH_FIELD));
        int endMonth = Integer.parseInt(request.getParameter(END_MONTH_FIELD));
        int startQuarter = Integer.parseInt(request.getParameter(START_QUARTER_FIELD));
        int endQuarter = Integer.parseInt(request.getParameter(END_QUARTER_FIELD));

        List<String> label = new ArrayList<>();
        Map<String, BigInteger> dataRaw = new HashMap<>();

        switch (typeX) {
            case "year" -> {
                int start = startYear;
                int end = endYear;
                for (int i = start; i <= end; i++) {
                    label.add(String.valueOf(i));
                }
                dataRaw = dal.BookingDAO.getInstance().totalMoneyInYears(startYear, endYear);
            }
            case "month" -> {
                int yearStart = startYear;
                int yearEnd = endYear;
                for (int year = yearStart; year <= yearEnd; year++) {
                    int monthFrom = (year == yearStart) ? startMonth : 1;
                    int monthTo = (year == yearEnd) ? endMonth : 12;
                    for (int month = monthFrom; month <= monthTo; month++) {
                        label.add(year + "-" + String.format("%02d", month));
                    }
                }
                dataRaw = dal.BookingDAO.getInstance().totalMoneyInMonths(startYear, startMonth, endYear, endMonth);
            }
            case "quarter" -> {
                int yearStart = startYear;
                int yearEnd = endYear;
                for (int year = yearStart; year <= yearEnd; year++) {
                    int quarterFrom = (year == yearStart) ? startQuarter : 1;
                    int quarterTo = (year == yearEnd) ? endQuarter : 4;
                    for (int quarter = quarterFrom; quarter <= quarterTo; quarter++) {
                        label.add(year + "-Q" + quarter);
                    }
                }
                dataRaw = dal.BookingDAO.getInstance().totalMoneyInQuarters(startYear, startQuarter, endYear, endQuarter);
            }
            default -> {
                //
            }
        }
        request.setAttribute("labels", label);
        List<BigInteger> data = new ArrayList<>();
        for (String l : label) {
            BigInteger value = dataRaw.getOrDefault(l, BigInteger.ZERO);
            data.add(value);
        }

        request.setAttribute("data", data);
        System.out.println("Label: " + label);
        System.out.println("Data: " + data);
        request.getRequestDispatcher("/View/Admin/Statistics.jsp").forward(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
