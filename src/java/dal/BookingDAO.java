package dal;

import java.sql.Statement;
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
import models.Booking;
import java.sql.Date;

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
        String sql = "SELECT YEAR(PayDay) AS Year, SUM(TotalPrice) AS totalMoney "
                + "FROM Booking "
                + "WHERE PayDay >= ? AND PayDay < ? and Status='Completed'"
                + "GROUP BY YEAR(PayDay) "
                + "ORDER BY Year";

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

    //get Booking by booking id
    public Booking getBookingByBookingId(int bookingId) {
        String sql = "SELECT * FROM Booking WHERE BookingID = ?";
        try (PreparedStatement st = con.prepareStatement(sql)) {
            st.setInt(1, bookingId);
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    Booking booking = new Booking();
                    booking.setBookingId(rs.getInt("BookingID"));
                    booking.setCustomer(CustomerDAO.getInstance().getCustomerByCustomerID(rs.getInt("CustomerID")));
                    booking.setPayDay(rs.getDate("PayDay"));
                    booking.setTotalPrice(rs.getInt("TotalPrice"));
                    booking.setStatus(rs.getString("Status"));
                    booking.setPaymentMethod(PaymentMethodDAO.getInstance().getPaymentMethodByBookingId(rs.getInt("BookingId")));
                    return booking;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Map<String, BigInteger> totalMoneyInQuarters(int startYear, int startQuarter, int endYear, int endQuarter) {
        String sql = "SELECT YEAR(PayDay) AS Year, DATEPART(QUARTER, PayDay) AS Quarter, SUM(TotalPrice) AS totalMoney "
                + "FROM Booking "
                + "WHERE PayDay >= ? AND PayDay < ? and Status='Completed'"
                + "GROUP BY YEAR(PayDay), DATEPART(QUARTER, PayDay) "
                + "ORDER BY Year, Quarter";

        Map<String, BigInteger> result = new HashMap<>();

        try (PreparedStatement st = con.prepareStatement(sql)) {
            st.setString(1, startYear + "-" + String.format("%02d", startQuarter * 3 - 2) + "-01");
            st.setString(2, getFirstDateOfNextQuarter(endYear, endQuarter));
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
        String sql = "SELECT YEAR(PayDay) AS Year, DATEPART(Month, PayDay) AS Month, SUM(TotalPrice) AS totalMoney "
                + "FROM Booking "
                + "WHERE PayDay >= ? AND PayDay < ? and Status='Completed'"
                + "GROUP BY YEAR(PayDay), DATEPART(Month, PayDay) "
                + "ORDER BY Year, Month";

        Map<String, BigInteger> result = new HashMap<>();

        try (PreparedStatement st = con.prepareStatement(sql)) {
            st.setString(1, startYear + "-" + String.format("%02d", startMonth) + "-01");
            // Tính tháng tiếp theo của endMonth
            int nextMonth = endMonth + 1;
            int nextYear = endYear;
            if (nextMonth > 12) {
                nextMonth = 1;
                nextYear++;
            }
            st.setString(2, nextYear + "-" + String.format("%02d", nextMonth) + "-01");
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

    private String getFirstDateOfNextQuarter(int year, int quarter) {
        if (quarter == 4) {
            return (year + 1) + "-01-01";
        } else {
            return year + "-" + String.format("%02d", quarter * 3 + 1) + "-01";
        }
    }

    //return all booking by customerid, customer and paymentMethod in model is an object
    public List<Booking> getBookingsByCustomerId(int customerId) {
        List<Booking> bookings = new ArrayList<>();
        String sql = "SELECT * FROM Booking WHERE CustomerID = ?";
        try (PreparedStatement st = con.prepareStatement(sql)) {
            st.setInt(1, customerId);
            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    Booking booking = new Booking();
                    booking.setBookingId(rs.getInt("BookingID"));
                    booking.setCustomer(CustomerDAO.getInstance().getCustomerByCustomerID(customerId));
                    booking.setPayDay(rs.getDate("PayDay"));
                    booking.setTotalPrice(rs.getInt("TotalPrice"));
                    booking.setPayDay(rs.getDate("PayDay"));
                    booking.setPaymentMethod(PaymentMethodDAO.getInstance().getPaymentMethodByBookingId(rs.getInt("BookingId")));
                    bookings.add(booking);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bookings;
    }

    //get amount of booking by customerId
    public int getBookingCountByCustomerId(int customerId) {
        String sql = "SELECT COUNT(*) FROM Booking WHERE CustomerID = ? and status = 'Completed'";
        try (PreparedStatement st = con.prepareStatement(sql)) {
            st.setInt(1, customerId);
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    //getBookingByCustomerId pagination
    public List<Booking> getBookingByCustomerId(int customerId, int page, int pageSize) {
        List<Booking> bookings = new ArrayList<>();
        String sql = "SELECT * FROM Booking WHERE CustomerID = ? AND Status = 'Completed' ORDER BY BookingId OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

        try (PreparedStatement st = con.prepareStatement(sql)) {
            st.setInt(1, customerId);
            st.setInt(2, (page - 1) * pageSize);
            st.setInt(3, pageSize);
            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    Booking booking = new Booking();
                    System.out.println("booking");
                    booking.setBookingId(rs.getInt("BookingID"));
                    booking.setCustomer(CustomerDAO.getInstance().getCustomerByCustomerID(rs.getInt("CustomerId")));
                    booking.setPayDay(rs.getDate("PayDay"));
                    booking.setTotalPrice(rs.getInt("TotalPrice"));
                    booking.setStatus(rs.getString("status"));
                    booking.setPaymentMethod(PaymentMethodDAO.getInstance().getPaymentMethodByBookingId(rs.getInt("BookingId")));
                    bookings.add(booking);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bookings;
    }

    //getBookingByCustomerId pagination and filter by start date and end date
    public List<Booking> getBookingByCustomerIdAndDate(int customerId, int page, int pageSize, Date startDate, Date endDate) {
        List<Booking> bookings = new ArrayList<>();
        String sql = "SELECT * FROM Booking WHERE CustomerID = ? AND PayDay >= ? AND PayDay <= ? AND Status = 'Completed' ORDER BY BookingId OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
        System.out.println(sql);
        try (PreparedStatement st = con.prepareStatement(sql)) {
            st.setInt(1, customerId);
            st.setDate(2, startDate);
            st.setDate(3, endDate);
            st.setInt(4, (page - 1) * pageSize);
            st.setInt(5, pageSize);
            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    Booking booking = new Booking();
                    booking.setBookingId(rs.getInt("BookingID"));
                    booking.setCustomer(CustomerDAO.getInstance().getCustomerByCustomerID(rs.getInt("CustomerId")));
                    booking.setPayDay(rs.getDate("PayDay"));
                    booking.setTotalPrice(rs.getInt("TotalPrice"));
                    booking.setStatus(rs.getString("status"));
                    booking.setPaymentMethod(PaymentMethodDAO.getInstance().getPaymentMethodByBookingId(rs.getInt("BookingId")));
                    System.out.println(booking);
                    bookings.add(booking);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bookings;
    }

    //total booking filter by customerId, start date and end date
    public int getTotalBookingByCustomerIdAndDate(int customerId, Date startDate, Date endDate) {
        String sql = "SELECT COUNT(*) FROM Booking WHERE CustomerID = ? AND PayDay >= ? AND PayDay <= ? And Status = 'Completed'";
        try (PreparedStatement st = con.prepareStatement(sql)) {
            st.setInt(1, customerId);
            st.setDate(2, startDate);
            st.setDate(3, endDate);
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int insertNewBooking(Booking booking) {
        String sql = "INSERT INTO [dbo].[Booking]\n"
                + "            ([PayDay]\n"
                + "           ,[CustomerId]\n"
                + "           ,[PaymentMethodId]\n"
                + "           ,[PaidAmount])\n"
                + "     VALUES(?, ?, ?)";
        try (PreparedStatement st = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);) {

            st.setDate(1, java.sql.Date.valueOf(java.time.LocalDate.now()));
            st.setInt(2, booking.getCustomer().getCustomerId());
            st.setInt(3, 1);
            st.setInt(4, booking.getPaidAmount());
            st.executeUpdate();

            try (ResultSet generatedKeys = st.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Creating payment failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            System.out.println(e);
            return -1;
        }
    }

    public boolean updateBookingStatus(Booking booking) {
        String sql = "UPDATE [dbo].[Booking]\n"
                + "   SET [Status] = ?\n"
                + " WHERE BookingId = ?";
        try (PreparedStatement st = con.prepareStatement(sql);) {
            st.setString(1, booking.getStatus());
            st.setInt(2, booking.getBookingId());
            return st.executeUpdate() > 0;
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        return false;
    }
    public boolean updateBookingTotalPrice(Booking booking) {
        String sql = "UPDATE [dbo].[Booking]\n"
                + "   SET [TotalPrice] = ?\n"
                + " WHERE [BookingId] = ?";

        try (PreparedStatement st = con.prepareStatement(sql)) {
            st.setInt(1, booking.getTotalPrice()); 
            st.setInt(2, booking.getBookingId());
            return st.executeUpdate() > 0;
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        return false;
    }

    //get booking by payday
    public List<Booking> getBookingByPayDay(Date payDay) {
        List<Booking> bookings = new ArrayList<>();
        String sql = "SELECT * FROM Booking WHERE PayDay = ? and Status = 'Completed'";
        try (PreparedStatement st = con.prepareStatement(sql)) {
            st.setDate(1, payDay);
            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    Booking booking = new Booking();
                    booking.setBookingId(rs.getInt("BookingID"));
                    booking.setCustomer(CustomerDAO.getInstance().getCustomerByCustomerID(rs.getInt("CustomerId")));
                    booking.setPayDay(rs.getDate("PayDay"));
                    booking.setTotalPrice(rs.getInt("TotalPrice"));
                    booking.setStatus(rs.getString("status"));
                    booking.setPaymentMethod(PaymentMethodDAO.getInstance().getPaymentMethodByBookingId(rs.getInt("BookingId")));
                    bookings.add(booking);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bookings;
    }

}
