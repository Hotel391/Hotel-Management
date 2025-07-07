package dal;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import models.Cart;
import models.PaymentMethod;

/**
 *
 * @author HieuTT
 */
public class CartDAO {
    private static CartDAO instance;
    private Connection con;

    private CartDAO() {
        con = new DBContext().connect;
    }

    public static CartDAO getInstance() {
        if (instance == null) {
            instance = new CartDAO();
        }
        return instance;
    }

    public List<Cart> getCartByCustomerId(int customerId) {
        String sql = """
                SELECT c.CartId, c.TotalPrice, c.Status, c.StartDate, c.EndDate, c.isActive, 
                c.isPayment, c.PaymentMethodId, c.Adults, c.Children, c.RoomNumber, pm.PaymentName 
                FROM Cart c 
                left join PaymentMethod pm on pm.PaymentMethodId=c.PaymentMethodId
                WHERE CustomerId = ? 
                order by isPayment desc, StartDate asc
                """;
        List<Cart> carts = Collections.synchronizedList(new ArrayList<>());
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, customerId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Cart cart = new Cart();
                    cart.setCartId(rs.getInt("CartId"));
                    cart.setTotalPrice(rs.getInt("TotalPrice"));
                    cart.setStatus(rs.getString("Status"));
                    cart.setStartDate(rs.getDate("StartDate"));
                    cart.setIsActive(rs.getBoolean("isActive"));
                    cart.setIsPayment(rs.getBoolean("isPayment"));
                    if(!cart.isIsPayment() &&cart.isIsActive() && cart.getStartDate().before(Date.valueOf(LocalDate.now()))) {
                        cart.setIsActive(false);
                        updateCartActiveToIsActive(cart.getCartId());
                    }
                    cart.setEndDate(rs.getDate("EndDate"));
                    cart.setAdults(rs.getInt("Adults"));
                    cart.setChildren(rs.getInt("Children"));
                    cart.setRoomNumber(rs.getInt("RoomNumber"));
                    if(cart.isIsActive() && !checkRoomNumberStatus(cart.getRoomNumber(), cart.getStartDate(), cart.getEndDate())) {
                        int roomNumber = getRoomNumber(cart.getRoomNumber(), cart.getStartDate(), cart.getEndDate());
                        if (roomNumber == 0) {
                            cart.setIsActive(false);
                            updateCartActiveToIsActive(cart.getCartId());
                        } else {
                            cart.setRoomNumber(roomNumber);
                            updateCartRoomNumber(cart.getCartId(), roomNumber);
                        }
                    }
                    PaymentMethod paymentMethod = new PaymentMethod();
                    paymentMethod.setPaymentMethodId(rs.getInt("PaymentMethodId"));
                    paymentMethod.setPaymentName(rs.getString("PaymentName"));
                    cart.setPaymentMethod(paymentMethod);
                    carts.add(cart);
                }
            }
        } catch (SQLException e) {
            // Handle exception
        }
        return carts;
    }

    private void updateCartActiveToIsActive(int cartId) {
        String sql = "UPDATE Cart SET isActive = 0 WHERE CartId = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, cartId);
            ps.executeUpdate();
        } catch (Exception e) {
            // Handle exception
        }
    }

    private void updateCartRoomNumber(int cartId, int roomNumber) {
        String sql = "UPDATE Cart SET RoomNumber = ? WHERE CartId = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, roomNumber);
            ps.setInt(2, cartId);
            ps.executeUpdate();
        } catch (SQLException e) {
            // Handle exception
        }
    }

    private boolean checkRoomNumberStatus(int roomNumber, Date checkin, Date checkout) {
        String sql = """
                select count(*) as isAvailable from Room r
                left join BookingDetail bd on bd.RoomNumber=r.RoomNumber
                    and not (bd.EndDate <= ? OR bd.StartDate >= ?)
                LEFT JOIN Cart c ON c.RoomNumber = r.RoomNumber
                    AND c.isPayment = 1
                    AND NOT (c.EndDate <= ? OR c.StartDate >= ?)
                where bd.BookingDetailId is null and c.CartId is null and r.RoomNumber=?
                """;
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setDate(1, checkin);
            ps.setDate(2, checkout);
            ps.setDate(3, checkin);
            ps.setDate(4, checkout);
            ps.setInt(5, roomNumber);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("isAvailable") > 0;
                }
            }
        } catch (SQLException e) {
            // Handle exception
        }
        return false;
    }

    public boolean addToCart(int customerId, int typeId, Date checkin, Date checkout, int adults, int children, boolean isPayment) {
        int roomNumber = getRoomNumber(typeId, checkin, checkout);
        int totalPrice = getTotalPriceOfTypeRoom(typeId);
        if (roomNumber == 0) {
            return false;
        }
        String status = "Pending";
        String sql = "INSERT INTO Cart (StartDate, EndDate, isPayment, CustomerId, RoomNumber, Status, Adults, Children, TotalPrice) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = con.prepareStatement(sql, java.sql.Statement.RETURN_GENERATED_KEYS)) {
            ps.setDate(1, checkin);
            ps.setDate(2, checkout);
            ps.setBoolean(3, isPayment);
            ps.setInt(4, customerId);
            ps.setInt(5, roomNumber);
            ps.setString(6, status);
            ps.setInt(7, adults);
            ps.setInt(8, children);
            ps.setInt(9, totalPrice);
            int affectedRows = ps.executeUpdate();
            if (affectedRows > 0) {
                try (var rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        int cartId = rs.getInt(1);
                        updateCartService(cartId, typeId);
                        return true;
                    }
                }
            }
        } catch (SQLException e) {
            //
        }
        return false;
    }
    private int getTotalPriceOfTypeRoom(int typeId) {
        String sql = """
                select tr.Price+Sum(rns.quantity*s.Price) as totalPrice from TypeRoom tr
                join RoomNService rns on rns.TypeId=tr.TypeId
                join Service s on s.ServiceId=rns.ServiceId
                where tr.TypeId=?
                group by tr.Price
                """;
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, typeId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("totalPrice");
                }
            }
        } catch (SQLException e) {
            //
        }
        return 0;
    }
    private int getRoomNumber(int typeId, Date checkin, Date checkout) {
        String sql="""
                select top 1 r.RoomNumber from Room r
                left join BookingDetail bd on bd.RoomNumber=r.RoomNumber
                And NOT (bd.EndDate <= ? OR bd.StartDate >= ?)
                left join Cart c on c.RoomNumber=r.RoomNumber and c.isPayment=1
                AND NOT (c.EndDate <= ? OR c.StartDate >= ?)
                where r.TypeId=? and bd.BookingDetailId is null and c.CartId is null
                order by r.RoomNumber desc
                """;
        try(PreparedStatement ps= con.prepareStatement(sql)) {
            ps.setDate(1, checkin);
            ps.setDate(2, checkout);
            ps.setDate(3, checkin);
            ps.setDate(4, checkout);
            ps.setInt(5, typeId);
            var rs = ps.executeQuery();
            if(rs.next()) {
                return rs.getInt("RoomNumber");
            }
        } catch (SQLException e) {
            //
        }
        return 0;
    }

    private void updateCartService(int cartId, int typeId) {
        String sql = """
            INSERT INTO CartService (CartId, ServiceId, quantity)
            SELECT ?,ServiceId,Quantity
            FROM RoomNService
            WHERE TypeId = ?""";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, cartId);
            ps.setInt(2, typeId);
            ps.executeUpdate();
        } catch (SQLException e) {
            //
        }
    }
}
