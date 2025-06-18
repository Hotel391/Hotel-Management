package controllers.manager;

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
        request.getRequestDispatcher("/View/Manager/Statistics.jsp").forward(request, response);
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

        List<String> labels = new ArrayList<>();
        Map<String, BigInteger> dataRaw = new HashMap<>();

        switch (typeX) {
            case "year" -> {
                labels = generateYearLabels(startYear, endYear);
                dataRaw = dal.BookingDAO.getInstance().totalMoneyInYears(startYear, endYear);
            }
            case "month" -> {
                labels = generateMonthLabels(startYear, startMonth, endYear, endMonth);
                dataRaw = dal.BookingDAO.getInstance().totalMoneyInMonths(startYear, startMonth, endYear, endMonth);
            }
            case "quarter" -> {
                labels = generateQuarterLabels(startYear, startQuarter, endYear, endQuarter);
                dataRaw = dal.BookingDAO.getInstance().totalMoneyInQuarters(startYear, startQuarter, endYear, endQuarter);
            }
            default -> {
                //
            }
        }
        request.setAttribute("labels", labels);
        List<BigInteger> data = getDataFromRaw(dataRaw, labels);

        request.setAttribute("data", data);
        request.getRequestDispatcher("/View/Manager/Statistics.jsp").forward(request, response);
    }

    private List<String> generateYearLabels(int start, int end) {
        List<String> labels = new ArrayList<>();
        for (int i = start; i <= end; i++) {
            labels.add(String.valueOf(i));
        }
        return labels;
    }

    private List<String> generateMonthLabels(int startYear, int startMonth, int endYear, int endMonth) {
        List<String> labels = new ArrayList<>();
        for (int year = startYear; year <= endYear; year++) {
            int fromMonth = (year == startYear) ? startMonth : 1;
            int toMonth = (year == endYear) ? endMonth : 12;
            for (int month = fromMonth; month <= toMonth; month++) {
                labels.add(year + "-" + String.format("%02d", month));
            }
        }
        return labels;
    }

    private List<String> generateQuarterLabels(int startYear, int startQuarter, int endYear, int endQuarter) {
        List<String> labels = new ArrayList<>();
        for (int year = startYear; year <= endYear; year++) {
            int fromQuarter = (year == startYear) ? startQuarter : 1;
            int toQuarter = (year == endYear) ? endQuarter : 4;
            for (int quarter = fromQuarter; quarter <= toQuarter; quarter++) {
                labels.add(year + "-Q" + quarter);
            }
        }
        return labels;
    }

    private List<BigInteger> getDataFromRaw(Map<String, BigInteger> dataRaw, List<String> labels) {
        List<BigInteger> data = new ArrayList<>();
        for (String label : labels) {
            data.add(dataRaw.getOrDefault(label, BigInteger.ZERO));
        }
        return data;
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
