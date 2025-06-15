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
//    
//    <!--search-->
//                            <div class="d-flex justify-content-between align-items-center mb-3">
//                                <form method="post" action="${pageContext.request.contextPath}/admin/review" class="d-flex gap-2">
//
//                                    <input type="text" name="fullName" class="form-control search-input" placeholder="Enter fullName" />
//
//                                    <!-- Nhập ngày -->
//                                    <input type="number" name="day" min="1" max="31" class="form-control" placeholder="Day" />
//
//                                    <!-- Nhập tháng -->
//                                    <input type="number" name="month" min="1" max="12" class="form-control" placeholder="Month" />
//
//                                    <!-- Nhập năm -->
//                                    <input type="number" name="year" min="1900" max="2100" class="form-control" placeholder="Year" />
//
//                                    <div class="form-group d-flex gap-2">
//                                        <button type="submit" name="submit" class="btn btn-primary">Search</button>
//                                        <button type="reset" name="reset" class="btn btn-secondary">Reset</button>
//                                    </div>
//
//                                    <input type="hidden" name="choose" value="listReview"> 
//                                </form>
//                            </div>

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
        
        sql += " ORDER BY r.[Date] DESC;";

        List<Review> listReview = new Vector<>();
        try (PreparedStatement ptm = con.prepareStatement(sql);) {
             int paramIndex = 1;
            if (hasFullName) {
                ptm.setString(paramIndex++, "%" + fullName + "%");
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
