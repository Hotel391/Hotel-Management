package dal;

import models.Booking;
import models.BookingDetail;
import models.Customer;
import models.CustomerAccount;
import models.Review;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import java.util.List;
import java.util.Vector;

public class ReviewDAO {

    private static ReviewDAO instance;
    private Connection con;

    public static ReviewDAO getInstance() {
        if (instance == null) {
            instance = new ReviewDAO();
        }
        return instance;
    }

    private ReviewDAO() {
        con = new DBContext().connect;
    }

    public List<Review> getAllReview() {
        String sql = "SELECT \n"
                + "    r.ReviewId,\n"
                + "    r.Rating,\n"
                + "    r.FeedBack,\n"
                + "    r.[Date],\n"
                + "    b.BookingId,\n"
                + "    c.FullName\n"
                + "FROM \n"
                + "    Review r\n"
                + "JOIN \n"
                + "    BookingDetail bd ON r.BookingDetailId = bd.BookingDetailId\n"
                + "JOIN \n"
                + "    Booking b ON bd.BookingId = b.BookingId\n"
                + "JOIN \n"
                + "    CustomerAccount ca ON r.Username = ca.Username\n"
                + "JOIN \n"
                + "    Customer c ON ca.CustomerId = c.CustomerId\n"
                + "ORDER BY r.[Date] DESC;";
        List<Review> listReview = new Vector<>();
        try (PreparedStatement ptm = con.prepareStatement(sql); ResultSet rs = ptm.executeQuery()) {

            while (rs.next()) {
                Booking b = new Booking();
                BookingDetail bd = new BookingDetail();
                b.setBookingId(rs.getInt(5));
                bd.setBooking(b);
                CustomerAccount acc = new CustomerAccount();
                Customer cus = new Customer();
                cus.setFullName(rs.getString(6));
                acc.setCustomer(cus);
                Review p = new Review(rs.getInt(1),
                        rs.getInt(2),
                        rs.getString(3),
                        rs.getDate(4),
                        bd,
                        acc);
                listReview.add(p);
            }
        } catch (SQLException ex) {
            ex.getStackTrace();
        }
        return listReview;
    }

    public List<Review> searchReview(Integer start, Date date) {
        String sql = "SELECT \n"
                + "    r.ReviewId,\n"
                + "    r.Rating,\n"
                + "    r.FeedBack,\n"
                + "    r.[Date],\n"
                + "    b.BookingId,\n"
                + "    c.FullName\n"
                + "FROM \n"
                + "    Review r\n"
                + "JOIN BookingDetail bd ON r.BookingDetailId = bd.BookingDetailId\n"
                + "JOIN Booking b ON bd.BookingId = b.BookingId\n"
                + "JOIN CustomerAccount ca ON r.Username = ca.Username\n"
                + "JOIN Customer c ON ca.CustomerId = c.CustomerId\n";

        boolean hasStart = start != null;
        boolean hasDate = date != null;

        // Thêm điều kiện WHERE để search
        if (hasStart && hasDate) {
            sql += "WHERE r.Rating = ? AND r.[Date] = ?";
        } else if (hasStart) {
            sql += "WHERE r.Rating = ?";
        } else if (hasDate) {
            sql += "WHERE r.[Date] = ?";
        }
        
        sql += " ORDER BY r.[Date] DESC;";

        List<Review> listReview = new Vector<>();
        try (PreparedStatement ptm = con.prepareStatement(sql);) {
             int paramIndex = 1;
            if (hasStart) {
                ptm.setInt(paramIndex++, start);
            }
            if (hasDate) {
                ptm.setDate(paramIndex++, new java.sql.Date(date.getTime()));
            }

            try (ResultSet rs = ptm.executeQuery()) {
                while (rs.next()) {
                    Booking b = new Booking();
                    BookingDetail bd = new BookingDetail();
                    b.setBookingId(rs.getInt(5));
                    bd.setBooking(b);
                    CustomerAccount acc = new CustomerAccount();
                    Customer cus = new Customer();
                    cus.setFullName(rs.getString(6));
                    acc.setCustomer(cus);
                    Review p = new Review(
                            rs.getInt(1),
                            rs.getInt(2),
                            rs.getString(3),
                            rs.getDate(4),
                            bd,
                            acc
                    );
                    listReview.add(p);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return listReview;
    }
}
