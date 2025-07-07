package dal;

import java.sql.Statement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import models.Booking;
import models.BookingDetail;
import java.sql.Date;
import models.Room;

public class BookingDetailDAO {

    private static BookingDetailDAO instance;
    private Connection con;

    private BookingDetailDAO() {
        con = new DBContext().connect;
    }

    public static BookingDetailDAO getInstance() {
        if (instance == null) {
            instance = new BookingDetailDAO();
        }
        return instance;
    }

    public int checkinCount() {
        String sql = "SELECT COUNT(*) FROM BookingDetail";
        try (PreparedStatement st = con.prepareStatement(sql); ResultSet rs = st.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
        }
        return 0;
    }

    public List<BookingDetail> getBookingDetailByBookingId(Booking booking) {
        List<BookingDetail> bookingDetails = new ArrayList<>();
        String sql = "SELECT * FROM BookingDetail WHERE BookingId = ?";
        try (PreparedStatement st = con.prepareStatement(sql)) {
            st.setInt(1, booking.getBookingId());
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                BookingDetail bookingDetail = new BookingDetail();
                bookingDetail.setBookingDetailId(rs.getInt("BookingDetailId"));
                bookingDetail.setStartDate(rs.getDate("StartDate"));
                bookingDetail.setEndDate(rs.getDate("EndDate"));
                bookingDetail.setRoom(RoomDAO.getInstance().getRoomByNumber(rs.getInt("RoomNumber")));
                bookingDetail.setServices(
                        DetailServiceDAO.getInstance()
                                .getAllDetailServiceByBookingDetailId(rs.getInt("BookingDetailId")));
                bookingDetails.add(bookingDetail);
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return bookingDetails;
    }
    
    //get booking detail by booking id
    public List<BookingDetail> getBookingDetailByBookingIdAndRoomNumber(Booking booking, int roomNumber) {
        List<BookingDetail> bookingDetails = new ArrayList<>();
        String sql = "SELECT * FROM BookingDetail WHERE BookingId = ? and roomNumber = ?";
        try (PreparedStatement st = con.prepareStatement(sql)) {
            st.setInt(1, booking.getBookingId());
            st.setInt(2, roomNumber);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                BookingDetail bookingDetail = new BookingDetail();
                bookingDetail.setBookingDetailId(rs.getInt("BookingDetailId"));
                bookingDetail.setStartDate(rs.getDate("StartDate"));
                bookingDetail.setEndDate(rs.getDate("EndDate"));
                bookingDetail.setRoom(RoomDAO.getInstance().getRoomByNumber(rs.getInt("RoomNumber")));
                bookingDetail.setServices(
                        DetailServiceDAO.getInstance()
                                .getAllDetailServiceByBookingDetailId(rs.getInt("BookingDetailId")));
                bookingDetails.add(bookingDetail);
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return bookingDetails;
    }

    public List<BookingDetail> getBookingDetailByEndDate(Date endDate) {
        List<BookingDetail> bookingDetails = new ArrayList<>();
        String sql = "select bd.* from Booking b join BookingDetail bd on b.BookingId = bd.BookingId\n"
                + " where  EndDate >= ? and b.Status != 'Completed CheckOut' order by RoomNumber";
        try (PreparedStatement st = con.prepareStatement(sql)) {
            st.setDate(1, endDate);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                BookingDetail bookingDetail = new BookingDetail();
                bookingDetail.setBookingDetailId(rs.getInt("BookingDetailId"));
                bookingDetail.setStartDate(rs.getDate("StartDate"));
                bookingDetail.setEndDate(rs.getDate("EndDate"));
                bookingDetail.setRoom(RoomDAO.getInstance().getRoomByNumber(rs.getInt("RoomNumber")));
                bookingDetail.setServices(
                        DetailServiceDAO.getInstance()
                                .getAllDetailServiceByBookingDetailId(rs.getInt("BookingDetailId")));
                bookingDetail.setBooking(BookingDAO.getInstance().getBookingByBookingId(rs.getInt("BookingId")));
                bookingDetails.add(bookingDetail);
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return bookingDetails;
    }

    public List<BookingDetail> getStayingRooms(int numberRow, int pageIndex, String search) {
        List<BookingDetail> bookingDetails = new ArrayList<>();
        StringBuilder sql = new StringBuilder("""
                select bd.BookingDetailId, bd.StartDate, bd.EndDate, bd.TotalAmount, r.RoomNumber, r.isCleaner from BookingDetail bd
                join Room r on r.RoomNumber=bd.RoomNumber
                join Booking b on b.BookingId=bd.BookingId
                WHERE bd.StartDate<= CAST(GETDATE() AS DATE) and bd.EndDate>=CAST(GETDATE() AS DATE) and 
                    b.Status not like 'Completed CheckOut'""");
        if (search != null && !search.isEmpty()) {
            sql.append(" AND r.RoomNumber LIKE ?");
        }
        sql.append(" ORDER BY r.RoomNumber OFFSET ? ROWS FETCH NEXT %d ROWS ONLY".formatted(numberRow));
        try (PreparedStatement st = con.prepareStatement(sql.toString())) {
            int paramIndex = 1;
            if (search != null && !search.isEmpty()) {
                st.setString(paramIndex++, "%" + search + "%");
            }
            st.setInt(paramIndex, (pageIndex - 1) * numberRow);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                BookingDetail bookingDetail = new BookingDetail();
                bookingDetail.setBookingDetailId(rs.getInt("BookingDetailId"));
                bookingDetail.setStartDate(rs.getDate("StartDate"));
                bookingDetail.setEndDate(rs.getDate("EndDate"));
                bookingDetail.setTotalAmount(rs.getInt("TotalAmount"));

                Room room = new Room();
                room.setRoomNumber(rs.getInt("RoomNumber"));
                room.setIsCleaner(rs.getBoolean("isCleaner"));

                bookingDetail.setRoom(room);
                bookingDetails.add(bookingDetail);
            }
        } catch (SQLException e) {
        }
        return bookingDetails;
    }

    public int getTotalStayingRooms(String search) {
        String sql = """
                    select COUNT(*) from Room r
                    join BookingDetail bd on bd.RoomNumber=r.RoomNumber
                    join TypeRoom tr on tr.TypeId=r.TypeId
                    join Booking b on b.BookingId=bd.BookingId
                    WHERE bd.StartDate<= CAST(GETDATE() AS DATE) and bd.EndDate>=CAST(GETDATE() AS DATE) and
                        b.Status not like 'Completed CheckOut'
                     """;

        if (search != null && !search.isEmpty()) {
            sql += " AND r.RoomNumber LIKE ?";
        }

        try (PreparedStatement ptm = con.prepareStatement(sql)) {
            if (search != null && !search.isEmpty()) {
                ptm.setString(1, "%" + search + "%");
            }
            try (ResultSet rs = ptm.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            //
        }
        return 0;
    }

    public int insertNewBookingDetail(BookingDetail detail) {
        String sql = "INSERT INTO [dbo].[BookingDetail] "
                + "([StartDate], [EndDate], [BookingId], [RoomNumber], [TotalAmount]) "
                + "VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement st = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            st.setDate(1, detail.getStartDate());
            st.setDate(2, detail.getEndDate());
            st.setInt(3, detail.getBooking().getBookingId());
            st.setInt(4, detail.getRoom().getRoomNumber());
            st.setInt(5, detail.getTotalAmount());
            st.executeUpdate();

            try (ResultSet generatedKeys = st.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Creating BookingDetail failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            System.out.println("Error inserting BookingDetail: " + e.getMessage());
            return -1;
        }
    }

    public boolean updateTotalAmountBookingDetail(BookingDetail detail) {
        String sql = "UPDATE [dbo].[BookingDetail]\n"
                + "   SET [TotalAmount] = ?\n"
                + " WHERE BookingDetailId = ?";
        try (PreparedStatement st = con.prepareStatement(sql)) {
            st.setInt(1, detail.getTotalAmount());
            st.setInt(2, detail.getBookingDetailId());
            st.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error deleting BookingDetail: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteBookingDetailByBookingId(int bookingId) {
        String sql = "DELETE FROM BookingDetail WHERE [BookingId] = ?";
        try (PreparedStatement st = con.prepareStatement(sql)) {
            st.setInt(1, bookingId);
            int rows = st.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            System.out.println("Error deleting BookingDetail: " + e.getMessage());
            return false;
        }
    }

//    public List<BookingDetail> getBookingDetailsByBookingId(int bookingId) {
//        List<BookingDetail> bookingDetails = new ArrayList<>();
//        String sql = """
//                     SELECT 
//                         b.BookingId,
//                         bd.BookingDetailId,
//                         b.PaidAmount,
//                         bd.TotalAmount, 
//                         bd.RoomNumber
//                     FROM 
//                         Booking b
//                     JOIN 
//                         BookingDetail bd ON b.BookingId = bd.BookingId
//                     WHERE 
//                         b.BookingId = ?;""";
//
//        try (PreparedStatement ptm = con.prepareStatement(sql)) {
//            ptm.setInt(1, bookingId);
//            try (ResultSet rs = ptm.executeQuery()) {
//                while (rs.next()) {
//                    BookingDetail bookingDetail = new BookingDetail();
//                    Booking booking = new Booking();
//                    booking.setBookingId(rs.getInt(1));
//                    bookingDetail.setBookingDetailId(rs.getInt(2));
//                    booking.setPaidAmount(rs.getInt(3));
//                    bookingDetail.setBooking(booking);
//                    bookingDetail.setTotalAmount(rs.getInt(4));
//                    Room room = new Room();
//                    room.setRoomNumber(rs.getInt(5));
//                    bookingDetail.setRoom(room);
//                }
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return bookingDetails;
//    }
    
    public List<BookingDetail> getBookingDetailsByBookingId(int bookingId) {
        List<BookingDetail> bookingDetails = new ArrayList<>();
        String sql = """
        SELECT 
            b.BookingId,
            bd.BookingDetailId,
            b.PaidAmount,
            bd.TotalAmount,
            bd.RoomNumber,
            bd.StartDate,
            bd.EndDate
        FROM 
            Booking b
        JOIN 
            BookingDetail bd ON b.BookingId = bd.BookingId
        WHERE 
            b.BookingId = ?
    """;

        try (PreparedStatement ptm = con.prepareStatement(sql)) {
            ptm.setInt(1, bookingId);

            try (ResultSet rs = ptm.executeQuery()) {
                while (rs.next()) {
                    BookingDetail bookingDetail = new BookingDetail();
                    Booking booking = new Booking();
                    Room room = new Room();

                    booking.setBookingId(rs.getInt("BookingId"));
                    booking.setPaidAmount(rs.getInt("PaidAmount"));

                    bookingDetail.setBookingDetailId(rs.getInt("BookingDetailId"));
                    bookingDetail.setTotalAmount(rs.getInt("TotalAmount"));
                    bookingDetail.setStartDate(rs.getDate("StartDate"));
                    bookingDetail.setEndDate(rs.getDate("EndDate"));

                    room.setRoomNumber(rs.getInt("RoomNumber"));

                    bookingDetail.setBooking(booking);
                    bookingDetail.setRoom(room);

                    bookingDetails.add(bookingDetail);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bookingDetails;
    }

    public int getTotalTypeRoom(Date startDate, Date endDate, Integer minPrice, Integer maxPrice) {
        StringBuilder sql = new StringBuilder("""
                SELECT COUNT(DISTINCT tr.TypeId) AS totalTypeRoom
                FROM TypeRoom tr
                JOIN Room r ON r.TypeId = tr.TypeId
                JOIN BookingDetail bd 
                    ON bd.RoomNumber = r.RoomNumber
                    AND bd.StartDate <= ?
                    AND bd.EndDate >= ?
                WHERE 1=1
                """);
        if (minPrice != null) {
            sql.append(" AND tr.Price >= ?");
        }
        if (maxPrice != null) {
            sql.append(" AND tr.Price <= ?");
        }
        try (PreparedStatement ptm = con.prepareStatement(sql.toString())) {
            ptm.setDate(1, startDate);
            ptm.setDate(2, endDate);
            if (minPrice != null) {
                ptm.setInt(3, minPrice);
                if (maxPrice != null) {
                    ptm.setInt(4, maxPrice);
                }
            } else if (maxPrice != null) {
                ptm.setInt(3, maxPrice);
            }
            try (ResultSet rs = ptm.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("totalTypeRoom");
                }
            }
        } catch (SQLException e) {
            //
        }
        return 0;
    }

    public boolean canPostFeedback(int typeId, int customerId) {
        String sql = """
                SELECT count(*) as count
                FROM Customer c
                JOIN Booking b ON c.CustomerId = b.CustomerId
                JOIN BookingDetail bd ON b.BookingId = bd.BookingId
                JOIN Room r ON bd.RoomNumber = r.RoomNumber
                JOIN TypeRoom tr ON r.TypeId = tr.TypeId
                LEFT JOIN Review rv ON rv.BookingDetailId = bd.BookingDetailId
                WHERE rv.ReviewId IS NULL
                    AND c.CustomerId = ?
                    AND tr.TypeId = ?
                """;
        try (PreparedStatement ptm = con.prepareStatement(sql)) {
            ptm.setInt(1, customerId);
            ptm.setInt(2, typeId);
            try (ResultSet rs = ptm.executeQuery()) {
                if (rs.next()) {
                    int count = rs.getInt("count");
                    return count > 0;
                }
            }
        } catch (SQLException e) {
            //
        }
        return false;
    }

    public void insertReview(int customerId, int typeId, String username, String reviewContent, int rating) {
        String sql = "INSERT INTO Review (Rating, FeedBack, Date, BookingDetailId, Username) "
                + "VALUES (?, ?, ?, ?, ?)";
        int bookingDetailId = getBookingDetailId(customerId, typeId);
        if (bookingDetailId == -1) {
            return;
        }
        try (PreparedStatement st = con.prepareStatement(sql)) {
            st.setInt(1, rating);
            st.setString(2, reviewContent);
            st.setDate(3, Date.valueOf(java.time.LocalDate.now()));
            st.setInt(4, bookingDetailId);
            st.setString(5, username);
            st.executeUpdate();
        } catch (SQLException e) {
            //
        }
    }

    private int getBookingDetailId(int customerId, int typeId) {
        String sql = """
                SELECT TOP 1 bd.BookingDetailId
                FROM BookingDetail bd
                JOIN Booking b ON bd.BookingId = b.BookingId
                JOIN Room r ON bd.RoomNumber = r.RoomNumber
                JOIN TypeRoom tr ON r.TypeId = tr.TypeId
                WHERE b.CustomerId = ? AND tr.TypeId = ?
                ORDER BY bd.StartDate ASC
                """;
        try (PreparedStatement ptm = con.prepareStatement(sql)) {
            ptm.setInt(1, customerId);
            ptm.setInt(2, typeId);
            try (ResultSet rs = ptm.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("BookingDetailId");
                }
            }
        } catch (SQLException e) {
            //
        }
        return -1;
    }
    
    //get booking detail by id
    
    public BookingDetail getBookingDetailByBookingDetailId(int bookingDetailId) {
        BookingDetail bookingDetail = null;
        String sql = "SELECT * FROM BookingDetail WHERE BookingDetailId = ?";
        try (PreparedStatement st = con.prepareStatement(sql)) {
            st.setInt(1, bookingDetailId);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                bookingDetail = new BookingDetail();
                bookingDetail.setBookingDetailId(rs.getInt("BookingDetailId"));
                bookingDetail.setStartDate(rs.getDate("StartDate"));
                bookingDetail.setEndDate(rs.getDate("EndDate"));
                bookingDetail.setRoom(RoomDAO.getInstance().getRoomByNumber(rs.getInt("RoomNumber")));
                bookingDetail.setServices(
                        DetailServiceDAO.getInstance()
                                .getAllDetailServiceByBookingDetailId(rs.getInt("BookingDetailId")));
                bookingDetail.setBooking(BookingDAO.getInstance().getBookingByBookingId(rs.getInt("BookingId")));
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return bookingDetail;
    }
}
