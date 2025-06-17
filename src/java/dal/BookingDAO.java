package dal;

import models.DailyRevenue;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
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
        String sql = "SELECT COUNT(*) FROM Booking WHERE CustomerID = ?";
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
        String sql = "SELECT * FROM Booking WHERE CustomerID = ? ORDER BY BookingId OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
        
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
        String sql = "SELECT * FROM Booking WHERE CustomerID = ? AND PayDay >= ? AND PayDay <= ? ORDER BY BookingId OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
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
        String sql = "SELECT COUNT(*) FROM Booking WHERE CustomerID = ? AND PayDay >= ? AND PayDay <= ?";
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

    

}
