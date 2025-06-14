package dal;

import models.DailyRevenue;

import java.math.BigInteger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BookingDAO {

    private static BookingDAO instance;
    private Connection con;

    public static BookingDAO getInstance() {
        if (instance == null) {
            instance = new BookingDAO();
        }
        return instance;
    }

    private BookingDAO() {
        con = new DBContext().connect;
    }

    public int bookingCount() {
        String sql = "select count(*) from Booking";

        try (PreparedStatement st = con.prepareStatement(sql); ResultSet rs = st.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
        }
        return 0;
    }

    public int checkoutCount() {
        String sql = "select count(*) from Booking where PayDay is not null";
        try (PreparedStatement st = con.prepareStatement(sql); ResultSet rs = st.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
        }
        return 0;
    }

    public List<DailyRevenue> totalMoneyInOneWeek() {
        String sql = """
                     SELECT 
                       DATENAME(WEEKDAY, PayDay) AS WeekdayName,
                       DAY(PayDay) AS [Day],
                       SUM(TotalPrice) AS TotalPrice
                     FROM Booking
                     WHERE PayDay >= DATEADD(DAY, -6, CAST(GETDATE() AS DATE))
                     GROUP BY DATENAME(WEEKDAY, PayDay), DAY(PayDay), DATEPART(WEEKDAY, PayDay)
                     ORDER BY DATEPART(WEEKDAY, PayDay);""";

        List<DailyRevenue> result = new ArrayList<>();

        try (PreparedStatement st = con.prepareStatement(sql); ResultSet rs = st.executeQuery()) {
            while (rs.next()) {
                DailyRevenue dr = new DailyRevenue();
                dr.setWeekdayName(rs.getString("WeekdayName"));
                dr.setDay(rs.getInt("Day"));
                dr.setTotalPrice(rs.getDouble("TotalPrice"));
                result.add(dr);
            }
        } catch (SQLException e) {
        }
        return result;
    }
    
    public Map<String, BigInteger> totalMoneyInYears(int startYear, int endYear) {
        String sql = "SELECT YEAR(PayDay) AS Year, SUM(TotalPrice) AS totalMoney " +
                     "FROM Booking " +
                     "WHERE PayDay >= ? AND PayDay < ? and Status='Completed'" +
                     "GROUP BY YEAR(PayDay) " +
                     "ORDER BY Year";

        Map<String, BigInteger> result = new HashMap<>();

        try (PreparedStatement st = con.prepareStatement(sql)) {
            st.setString(1, startYear + "-01-01");
            st.setString(2, (endYear + 1) + "-01-01");
            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    String year = rs.getString("Year");
                    BigInteger totalMoney = rs.getBigDecimal("totalMoney").toBigInteger();
                    result.put(year, totalMoney);
                }
            }
        } catch (SQLException e) {
        }
        return result;
    }

    public Map<String, BigInteger> totalMoneyInQuarters(int startYear, int startQuarter, int endYear, int endQuarter) {
        String sql = "SELECT YEAR(PayDay) AS Year, DATEPART(QUARTER, PayDay) AS Quarter, SUM(TotalPrice) AS totalMoney " +
                     "FROM Booking " +
                     "WHERE PayDay >= ? AND PayDay < ? and Status='Completed'" +
                     "GROUP BY YEAR(PayDay), DATEPART(QUARTER, PayDay) " +
                     "ORDER BY Year, Quarter";

        Map<String, BigInteger> result = new HashMap<>();

        try (PreparedStatement st = con.prepareStatement(sql)) {
            st.setString(1, startYear + "-" + String.format("%02d", startQuarter*3-2) + "-01");
            st.setString(2, (endYear + 1) + "-" + String.format("%02d", endQuarter*3) + "-01");
            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    String key = rs.getInt("Year") + "-Q" + rs.getInt("Quarter");
                    BigInteger totalMoney = rs.getBigDecimal("totalMoney").toBigInteger();
                    result.put(key, totalMoney);
                }
            }
        } catch (SQLException e) {
        }
        return result;
    }

    public Map<String, BigInteger> totalMoneyInMonths(int startYear, int startMonth, int endYear, int endMonth) {
        String sql = "SELECT YEAR(PayDay) AS Year, DATEPART(Month, PayDay) AS Month, SUM(TotalPrice) AS totalMoney " +
                     "FROM Booking " +
                     "WHERE PayDay >= ? AND PayDay < ? and Status='Completed'" +
                     "GROUP BY YEAR(PayDay), DATEPART(Month, PayDay) " +
                     "ORDER BY Year, Month";

        Map<String, BigInteger> result = new HashMap<>();

        try (PreparedStatement st = con.prepareStatement(sql)) {
            st.setString(1, startYear + "-" + String.format("%02d", startMonth) + "-01");
            st.setString(2, endYear + "-" + String.format("%02d", endMonth+1) + "-01");
            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    String key = rs.getInt("Year") + "-" + String.format("%02d", rs.getInt("Month"));
                    BigInteger totalMoney = rs.getBigDecimal("totalMoney").toBigInteger();
                    result.put(key, totalMoney);
                }
            }
        } catch (SQLException e) {
        }
        return result;
    }
}
