/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
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

/**
 *
 * @author Tuan'sPC
 */
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
                + "    Customer c ON ca.CustomerId = c.CustomerId;";
        List<Review> listReview = new Vector<>();
        try {
            PreparedStatement ptm = con.prepareStatement(sql);
            ResultSet rs = ptm.executeQuery();
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

    public List<Review> searchReview(String fullName, Date date) {
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

        boolean hasFullName = fullName != null && !fullName.trim().isEmpty();
        boolean hasDate = date != null;

        // Thêm điều kiện WHERE để search
        if (hasFullName && hasDate) {
            sql += "WHERE c.FullName LIKE ? AND r.[Date] = ?";
        } else if (hasFullName) {
            sql += "WHERE c.FullName LIKE ?";
        } else if (hasDate) {
            sql += "WHERE r.[Date] = ?";
        }

        List<Review> listReview = new Vector<>();
        try {
            PreparedStatement ptm = con.prepareStatement(sql);

            if (hasFullName) {
                ptm.setString(1, "%" + fullName + "%");
            }
            if (hasDate) {
                ptm.setDate(2, new java.sql.Date(date.getTime()));
            }

            ResultSet rs = ptm.executeQuery();
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
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return listReview;
    }

    public static void main(String[] args) {

        ReviewDAO rD = new ReviewDAO();
        Date date = Date.valueOf("2024-06-06");

        List<Review> list = rD.searchReview("a", null);
        for (Review review : list) {
            System.out.println(review.toString());
        }
    }
}
