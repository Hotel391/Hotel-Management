/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import dal.DBContext;
import models.Booking;
import models.BookingDetail;
import models.Customer;
import models.CustomerAccount;
import models.Review;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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

    public static void main(String[] args) {
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
        
        ReviewDAO rD = new ReviewDAO();
        List<Review> list = rD.getAllReview();
        for (Review review : list) {
            System.out.println(review.toString());
        }
    }
}
