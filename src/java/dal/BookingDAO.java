package dal;

import models.DailyRevenue;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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

    public int BookingCount() {
        String sql = "select count(*) from Booking";

        try (PreparedStatement st = con.prepareStatement(sql); ResultSet rs = st.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
        }
        return 0;
    }

    public int CheckoutCount() {
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
}
