package dal;

import models.Booking;
import models.BookingDetail;
import models.Customer;
import models.CustomerAccount;
import models.Review;
import models.TypeRoom;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import models.Room;

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
                + "    ORDER BY r.[Date] DESC;";
        List<Review> listReview = Collections.synchronizedList(new ArrayList<>());
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

    public List<Review> searchReview(Integer star, Integer month, Integer year) {
        StringBuilder sql = new StringBuilder(
                "SELECT r.ReviewId, r.Rating, r.FeedBack, r.[Date], b.BookingId, c.FullName "
                + "FROM Review r "
                + "JOIN BookingDetail bd ON r.BookingDetailId = bd.BookingDetailId "
                + "JOIN Booking b ON bd.BookingId = b.BookingId "
                + "JOIN CustomerAccount ca ON r.Username = ca.Username "
                + "JOIN Customer c ON ca.CustomerId = c.CustomerId "
        );

        boolean hasStar = star != null;
        boolean hasMonth = month != null;
        boolean hasYear = year != null;
        List<Object> params = new ArrayList<>();
        boolean whereAdded = false;

        if (hasStar) {
            sql.append(whereAdded ? " AND " : " WHERE ").append("r.Rating = ?");
            params.add(star);
            whereAdded = true;
        }
        if (hasMonth && hasYear) {
            sql.append(whereAdded ? " AND " : " WHERE ").append("MONTH(r.[Date]) = ? AND YEAR(r.[Date]) = ?");
            params.add(month);
            params.add(year);
            whereAdded = true;
        }

        sql.append(" ORDER BY r.[Date] DESC");

        List<Review> listReview = new ArrayList<>();
        try (PreparedStatement ptm = con.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                ptm.setObject(i + 1, params.get(i));
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
            //
        }
        return listReview;
    }

    public List<Review> getReviewsByTypeRoomId(int typeId, String orderByClause, int offset, int limit) {
        StringBuilder sql = new StringBuilder("""
            SELECT rv.ReviewId, rv.Username, rv.Rating, rv.FeedBack, rv.Date
            FROM TypeRoom tr
            JOIN Room r ON r.TypeId = tr.TypeId
            JOIN BookingDetail bd ON bd.RoomNumber = r.RoomNumber
            JOIN Review rv ON rv.BookingDetailId = bd.BookingDetailId
            WHERE tr.TypeId = ?
        """);

        if (orderByClause != null && !orderByClause.isEmpty()) {
            sql.append(" ORDER BY ").append(orderByClause);
        }
        sql.append(" OFFSET ? ROWS FETCH NEXT ? ROWS ONLY");
        List<Review> reviews = new ArrayList<>();
        try (PreparedStatement ptm = con.prepareStatement(sql.toString())) {
            ptm.setInt(1, typeId);
            ptm.setInt(2, offset);
            ptm.setInt(3, limit);
            try (ResultSet rs = ptm.executeQuery()) {
                while (rs.next()) {
                    Review review = new Review();
                    review.setReviewId(rs.getInt("ReviewId"));
                    review.setUsername(rs.getString("Username"));
                    review.setRating(rs.getInt("Rating"));
                    review.setFeedBack(rs.getString("FeedBack"));
                    review.setDate(rs.getDate("Date"));
                    reviews.add(review);
                }
            }
        } catch (SQLException ex) {
            //
        }
        return reviews;
    }

    public List<Review> getTop10FiveStarReviews() {
        String sql = """
        SELECT TOP 10 
               r.ReviewId, r.Rating, r.FeedBack, r.[Date],
               c.FullName, r.Username, 
               bd.BookingDetailId, 
               t.TypeId, t.TypeName
        FROM Review r
        JOIN BookingDetail bd ON r.BookingDetailId = bd.BookingDetailId
        JOIN Room ro ON bd.RoomNumber = ro.RoomNumber
        JOIN TypeRoom t ON ro.TypeId = t.TypeId
        JOIN Booking b ON bd.BookingId = b.BookingId
        JOIN CustomerAccount ca ON r.Username = ca.Username
        JOIN Customer c ON ca.CustomerId = c.CustomerId
        WHERE r.Rating = 5
        ORDER BY r.[Date] DESC
    """;

        List<Review> list = new ArrayList<>();
        try (PreparedStatement ptm = con.prepareStatement(sql); ResultSet rs = ptm.executeQuery()) {
            while (rs.next()) {
                Review review = new Review();
                review.setReviewId(rs.getInt("ReviewId"));
                review.setRating(rs.getInt("Rating"));
                review.setFeedBack(rs.getString("FeedBack"));
                review.setDate(rs.getDate("Date"));

                Customer cus = new Customer();
                cus.setFullName(rs.getString("FullName"));
                CustomerAccount acc = new CustomerAccount();
                acc.setUsername(rs.getString("Username"));
                acc.setCustomer(cus);

                TypeRoom typeRoom = new TypeRoom();
                typeRoom.setTypeId(rs.getInt("TypeId"));
                typeRoom.setTypeName(rs.getString("TypeName"));

                Room room = new Room();
                room.setTypeRoom(typeRoom);

                Booking booking = new Booking(); // placeholder nếu cần thêm BookingId
                BookingDetail bookingDetail = new BookingDetail();
                bookingDetail.setBooking(booking);
                bookingDetail.setRoom(room);

                review.setBookingDetail(bookingDetail);
                review.setCustomerAccount(acc);
                list.add(review);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
