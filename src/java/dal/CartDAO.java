package dal;

import java.math.BigInteger;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import models.Booking;
import models.BookingDetail;

import models.Cart;
import models.CartService;
import models.Customer;
import models.DetailService;
import models.PaymentMethod;
import models.Room;
import models.Service;
import models.TypeRoom;

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

    public final int[] serviceIdsDonNeedTimes = {1, 2, 4, 7};
    private final int[] serviceHaveMoneyButNoNeedTimes = {2};

    public List<Cart> getCartByCustomerId(int customerId) {
        String sql = """
                SELECT c.CartId, c.TotalPrice, c.Status, c.StartDate, c.EndDate, c.isActive,
                c.isPayment, c.PaymentMethodId, c.Adults, c.Children, c.RoomNumber, pm.PaymentName
                FROM Cart c
                LEFT JOIN PaymentMethod pm ON pm.PaymentMethodId = c.PaymentMethodId
                WHERE CustomerId = ?
                ORDER BY isPayment DESC, isActive DESC, StartDate ASC
                """;

        List<Cart> carts = new ArrayList<>();

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, customerId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Cart cart = mapCartFromResultSet(rs);
                    if (!cart.isIsPayment()) {
                        processUnpaidCart(cart);
                    }
                    carts.add(cart);
                }
            }
        } catch (SQLException e) {
            // Handle exception (log or rethrow)
        }

        return carts;
    }

    private Cart mapCartFromResultSet(ResultSet rs) throws SQLException {
        Cart cart = new Cart();
        cart.setCartId(rs.getInt("CartId"));
        cart.setTotalPrice(new BigInteger(rs.getString("TotalPrice")));
        cart.setStatus(rs.getString("Status"));
        cart.setStartDate(rs.getDate("StartDate"));
        cart.setEndDate(rs.getDate("EndDate"));
        cart.setIsActive(rs.getBoolean("isActive"));
        cart.setIsPayment(rs.getBoolean("isPayment"));
        cart.setAdults(rs.getInt("Adults"));
        cart.setChildren(rs.getInt("Children"));
        cart.setRoomNumber(rs.getInt("RoomNumber"));
        cart.setRoom(getRoomAndTypeRoom(cart.getRoomNumber()));

        PaymentMethod paymentMethod = new PaymentMethod();
        paymentMethod.setPaymentMethodId(rs.getInt("PaymentMethodId"));
        paymentMethod.setPaymentName(rs.getString("PaymentName"));
        cart.setPaymentMethod(paymentMethod);

        return cart;
    }

    private void processUnpaidCart(Cart cart) {
        LocalDate today = LocalDate.now();
        Date startDate = cart.getStartDate();
        Date endDate = cart.getEndDate();
        
        if (!cart.isIsActive() && !startDate.before(Date.valueOf(today))) {
            reactivateCartIfRoomAvailable(cart, startDate, endDate);
        }
        
        if (cart.isIsActive()) {
            deactivateCartIfStartDatePast(cart, today);
        }

        if(cart.isIsActive() && !checkRoomOfCartStatus(cart.getCartId())){
            handleRoomNumberConflict(cart, startDate, endDate);
        }

        if (cart.isIsActive()) {
            handleRoomNumberConflict(cart, startDate, endDate);
        }

        if (cart.isIsActive()) {
            updateCartPricingAndServices(cart, startDate, endDate);
        }
    }

    private boolean checkRoomOfCartStatus(int cartId){
        String sql = """
                select r.IsActive from Room r
                join Cart ca on ca.RoomNumber=r.RoomNumber
                where ca.CartId=?
                """;
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, cartId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getBoolean("IsActive");
                }
            }
        } catch (SQLException e) {
            // Handle exception
        }
        return true;
    }

    private void reactivateCartIfRoomAvailable(Cart cart, Date startDate, Date endDate) {
        int newRoom = getRoomNumber(getTyperoomOfRoomNumber(cart.getRoomNumber()), startDate, endDate);
        if (newRoom != 0) {
            cart.setRoomNumber(newRoom);
            updateCartRoomNumber(cart.getCartId(), newRoom);
            updateCartIsActiveToActive(cart.getCartId());
            cart.setIsActive(true);
        }
    }

    private int getTyperoomOfRoomNumber(int roomNumber) {
        String sql = """
                   select tr.TypeId from TypeRoom tr
                   join Room r on r.TypeId=tr.TypeId
                   where r.RoomNumber=?
                   """;
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, roomNumber);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("TypeId");
                }
            }
        } catch (SQLException e) {
            // Handle exception
        }
        return 0;
    }

    private void updateCartPricingAndServices(Cart cart, Date startDate, Date endDate) {
        Map<Integer, Integer> requiredServices = getServiceCannotDisable(cart.getCartId());
        int numberOfNight = (int) ChronoUnit.DAYS.between(startDate.toLocalDate(), endDate.toLocalDate());
        requiredServices.replaceAll((serviceId, quantity) -> quantity * numberOfNight);
        for (int id : serviceIdsDonNeedTimes) {
            if (requiredServices.containsKey(id)) {
                requiredServices.put(id, 1);
            }
        }

        List<CartService> currentServices = getCartServicesByCartId(cart.getCartId());

        Set<Integer> existingServiceIds = currentServices.stream()
                .map(cs -> cs.getService().getServiceId())
                .collect(Collectors.toSet());

        for (CartService cs : currentServices) {
            int serviceId = cs.getService().getServiceId();
            int quantity = cs.getQuantity();
            if (requiredServices.containsKey(serviceId)) {
                int requiredQty = requiredServices.get(serviceId);
                if (quantity < requiredQty) {
                    updateCartServiceQuantity(cart.getCartId(), serviceId, requiredQty);
                }
            }
        }

        for (Map.Entry<Integer, Integer> entry : requiredServices.entrySet()) {
            int serviceId = entry.getKey();
            int requiredQty = entry.getValue();
            if (!existingServiceIds.contains(serviceId)) {
                addServiceToCart(cart.getCartId(), serviceId, requiredQty);
            }
        }
        BigInteger totalPrice = getTotalPriceOfCart(cart.getCartId(), numberOfNight)
                .add(getTypeRoomPriceByCartId(cart.getCartId()).multiply(BigInteger.valueOf((long)numberOfNight - 1)))
                .add(getTotalServicePriceHaveMoneyButNoNeedTimes(cart.getRoom().getTypeRoom().getTypeId()).multiply(BigInteger.valueOf((long)numberOfNight - 1)));
        if (!cart.getTotalPrice().equals(totalPrice)) {
            cart.setTotalPrice(totalPrice);
            updateCartTotalPrice(cart.getCartId(), totalPrice);
        }

    }

    public void handleRoomNumberConflict(Cart cart, Date startDate, Date endDate) {
        if (!checkRoomNumberStatus(cart.getRoomNumber(), startDate, endDate)) {
            int newRoom = getRoomNumber(cart.getRoomNumber(), startDate, endDate);
            if (newRoom == 0) {
                cart.setIsActive(false);
                updateCartActiveToIsActive(cart.getCartId());
            } else {
                cart.setRoomNumber(newRoom);
                updateCartRoomNumber(cart.getCartId(), newRoom);
            }
        }
    }

    private void deactivateCartIfStartDatePast(Cart cart, LocalDate today) {
        if (cart.getStartDate().before(Date.valueOf(today))) {
            cart.setIsActive(false);
            updateCartActiveToIsActive(cart.getCartId());
        }
    }

    private BigInteger getTypeRoomPriceByCartId(int cartId) {
        String sql = """
                select tr.Price as totalPrice from Cart c
                join Room r on r.RoomNumber=c.RoomNumber
                join TypeRoom tr on tr.TypeId=r.TypeId
                where c.CartId=?
                """;
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, cartId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new BigInteger(rs.getString("totalPrice"));
                }
            }
        } catch (SQLException e) {
            // Handle exception
        }
        return BigInteger.ZERO;
    }

    private BigInteger getTotalPriceOfCart(int cartId, long numberOfNight) {
        String sql = """
                select tr.Price+ISNULL(csp.totalServicePrice, 0)-ISNULL(rnsp.totalRoomServicePrice, 0) * ?
                as CalculatedTotalPrice
                from Cart c
                join Room r on r.RoomNumber=c.RoomNumber
                join TypeRoom tr on tr.TypeId=r.TypeId
                left join (
                    select cs.CartId, Sum(CAST(cs.quantity AS BIGINT)* s.Price) as totalServicePrice  from CartService cs
                    join Service s on s.ServiceId=cs.ServiceId
                    group by cs.CartId
                ) csp on csp.CartId=c.CartId
                left join (
                    select rns.TypeId, Sum(CAST(rns.quantity AS BIGINT)* s.Price) as totalRoomServicePrice  from RoomNService rns
                    join Service s on s.ServiceId=rns.ServiceId
                    group by rns.TypeId
                ) rnsp on rnsp.TypeId=tr.TypeId
                where c.CartId=?
                """;
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, numberOfNight);
            ps.setInt(2, cartId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new BigInteger(rs.getString("CalculatedTotalPrice"));
                }
            }
        } catch (SQLException e) {
            // Handle exception
        }
        return BigInteger.ZERO;
    }

    private BigInteger getTotalServicePriceHaveMoneyButNoNeedTimes(int typeId) {
        if (serviceHaveMoneyButNoNeedTimes.length == 0) {
            return BigInteger.ZERO;
        }
        StringBuilder placeholders = new StringBuilder();
        for (int i = 0; i < serviceHaveMoneyButNoNeedTimes.length; i++) {
            placeholders.append("?");
            if (i < serviceHaveMoneyButNoNeedTimes.length - 1) {
                placeholders.append(",");
            }
        }
        String sql = """
                select ISNULL(SUM(rns.quantity * s.Price), 0) as totalServicePrice
                from RoomNService rns
                join Service s on s.ServiceId = rns.ServiceId
                where rns.TypeId = ? AND s.ServiceId IN (""" + placeholders + ")";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, typeId);
            for (int i = 0; i < serviceHaveMoneyButNoNeedTimes.length; i++) {
                ps.setInt(i + 2, serviceHaveMoneyButNoNeedTimes[i]);
            }
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new BigInteger(rs.getString("totalServicePrice"));
                }
            }
        } catch (SQLException e) {
            // Handle exception
        }
        return BigInteger.ZERO;
    }

    private void updateCartTotalPrice(int cartId, BigInteger totalPrice) {
        String sql = "UPDATE Cart SET TotalPrice = ? WHERE CartId = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, totalPrice.longValue());
            ps.setInt(2, cartId);
            ps.executeUpdate();
        } catch (SQLException e) {
            // Handle exception
        }
    }

    private Room getRoomAndTypeRoom(int roomNumber) {
        String sql = """
                SELECT
                    tr.TypeId,
                    tr.TypeName,
                    ISNULL(AVG(rv.Rating * 1.0), 0) AS AvgRating,
                    COUNT(DISTINCT rv.ReviewId) AS NumberOfReviews,
                    (
                        SELECT TOP 1 Image
                        FROM RoomImage
                        WHERE RoomImage.TypeId = tr.TypeId
                        ORDER BY RoomImageId ASC
                    ) AS FirstImage
                FROM Room r
                JOIN TypeRoom tr ON r.TypeId = tr.TypeId
                LEFT JOIN BookingDetail bd ON bd.RoomNumber = r.RoomNumber
                LEFT JOIN Review rv ON rv.BookingDetailId = bd.BookingDetailId
                WHERE tr.TypeId = (
                    SELECT TypeId
                    FROM Room
                    WHERE RoomNumber = ?
                )
                GROUP BY tr.TypeId, tr.TypeName, tr.Price""";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, roomNumber);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Room room = new Room();
                    room.setRoomNumber(roomNumber);
                    TypeRoom typeRoom = new TypeRoom();
                    typeRoom.setTypeId(rs.getInt("TypeId"));
                    typeRoom.setTypeName(rs.getString("TypeName"));
                    typeRoom.setAverageRating(rs.getDouble("AvgRating"));

                    List<String> images = new ArrayList<>();
                    images.add(rs.getString("FirstImage"));
                    typeRoom.setImages(images);

                    typeRoom.setNumberOfReviews(rs.getInt("NumberOfReviews"));
                    room.setTypeRoom(typeRoom);
                    return room;
                }
            }
        } catch (SQLException e) {
            // Handle exception
        }
        return null;
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

    private void updateCartIsActiveToActive(int cartId) {
        String sql = "UPDATE Cart SET isActive = 1 WHERE CartId = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, cartId);
            ps.executeUpdate();
        } catch (SQLException e) {
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

    public boolean checkRoomNumberStatus(int roomNumber, Date checkin, Date checkout) {
        String sql = """
                select count(*) as isAvailable from Room r
                LEFT JOIN(
                    select bd.BookingDetailId,bd.RoomNumber from BookingDetail bd
                    join Booking b on b.BookingId=bd.BookingId and
                    (b.Status='Completed Checkin' or b.Status='Processing')
                    where NOT (bd.EndDate <= ? OR bd.StartDate >= ?)
                ) BookingDetailCheck on BookingDetailCheck.RoomNumber=r.RoomNumber
                LEFT JOIN Cart c ON c.RoomNumber = r.RoomNumber
                    AND c.isPayment = 1
                    AND NOT (c.EndDate <= ? OR c.StartDate >= ?)
                where BookingDetailCheck.BookingDetailId is null and c.CartId is null and r.RoomNumber=?
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

    public boolean addToCart(int customerId, int typeId, Date checkin, Date checkout, int adults, int children,
            boolean isPayment) {
        int roomNumber = getRoomNumber(typeId, checkin, checkout);
        // Calculate number of nights
        LocalDate checkinLocal = checkin.toLocalDate();
        LocalDate checkoutLocal = checkout.toLocalDate();
        int numberOfNight = (int) ChronoUnit.DAYS.between(checkinLocal, checkoutLocal);
        BigInteger totalPrice = getTotalPriceOfTypeRoom(typeId).multiply(BigInteger.valueOf(numberOfNight));
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
            ps.setLong(9, totalPrice.longValue());
            int affectedRows = ps.executeUpdate();
            if (affectedRows > 0) {
                try (var rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        int cartId = rs.getInt(1);
                        updateCartService(cartId, typeId, numberOfNight);
                        return true;
                    }
                }
            }
        } catch (SQLException e) {
            //
        }
        return false;
    }

    private BigInteger getTotalPriceOfTypeRoom(int typeId) {
        String sql = """
                select tr.Price as totalPrice from TypeRoom tr
                where tr.TypeId=?
                """;
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, typeId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new BigInteger(rs.getString("totalPrice"));
                }
            }
        } catch (SQLException e) {
            // Handle exception
        }
        return BigInteger.ZERO;
    }

    private int getRoomNumber(int typeId, Date checkin, Date checkout) {
        String sql = """
                select top 1 r.RoomNumber from Room r
                LEFT JOIN(
                    select bd.BookingDetailId,bd.RoomNumber from BookingDetail bd
                    join Booking b on b.BookingId=bd.BookingId and
                    (b.Status='Completed Checkin' or b.Status='Processing')
                    where NOT (bd.EndDate <= ? OR bd.StartDate >= ?)
                ) BookingDetailCheck on BookingDetailCheck.RoomNumber=r.RoomNumber
                left join Cart c on c.RoomNumber=r.RoomNumber and c.isPayment=1
                AND NOT (c.EndDate <= ? OR c.StartDate >= ?)
                where r.TypeId=? and BookingDetailCheck.BookingDetailId is null and c.CartId is null
                order by r.RoomNumber desc
                """;
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setDate(1, checkin);
            ps.setDate(2, checkout);
            ps.setDate(3, checkin);
            ps.setDate(4, checkout);
            ps.setInt(5, typeId);
            var rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("RoomNumber");
            }
        } catch (SQLException e) {
            //
        }
        return 0;
    }

    private void updateCartService(int cartId, int typeId, int numberOfNight) {
        // Lấy danh sách dịch vụ của loại phòng
        String sql = "SELECT ServiceId, Quantity FROM RoomNService WHERE TypeId = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, typeId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int serviceId = rs.getInt("ServiceId");
                    int quantity = rs.getInt("Quantity");
                    boolean isNoMultiply = false;
                    for (int id : serviceIdsDonNeedTimes) {
                        if (id == serviceId) {
                            isNoMultiply = true;
                            break;
                        }
                    }
                    int finalQuantity = isNoMultiply ? quantity : quantity * numberOfNight;
                    addServiceToCart(cartId, serviceId, finalQuantity);
                }
            }
        } catch (SQLException e) {
            // Handle exception
        }
    }

    public void deleteCart(int cartId) {
        String sql = """
                DELETE FROM CartService WHERE CartId = ?;
                DELETE FROM Cart WHERE CartId = ?""";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, cartId);
            ps.setInt(2, cartId);
            ps.executeUpdate();
        } catch (SQLException e) {
            // Handle exception
        }
    }

    public Cart getDetailCartForIsPayment(int cartId) {
        String sql = """
                SELECT c.CartId, c.StartDate, c.EndDate FROM Cart c
                WHERE c.CartId = ?""";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, cartId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Cart cart = new Cart();
                    cart.setCartId(rs.getInt("CartId"));
                    cart.setStartDate(rs.getDate("StartDate"));
                    cart.setEndDate(rs.getDate("EndDate"));
                    // Load services for the cart
                    List<CartService> cartServices = getCartServicesByCartId(cartId);
                    cart.setCartServices(cartServices);

                    return cart;
                }
            }
        } catch (SQLException e) {
            // Handle exception
        }
        return null;
    }

    public boolean checkCartOfCustomer(int customerId, int cartId) {
        String sql = "SELECT COUNT(*) FROM Cart WHERE CustomerId = ? AND CartId = ? And isPayment = 0 and isActive = 1";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, customerId);
            ps.setInt(2, cartId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            // Handle exception
        }
        return false;
    }

    public List<CartService> getCartServicesByCartId(int cartId) {
        String sql = """
                select cs.CartId,s.ServiceId,s.ServiceName,s.Price,cs.quantity from CartService cs
                join Service s on s.ServiceId=cs.ServiceId
                where cs.CartId=?""";
        List<CartService> cartServices = new ArrayList<>();
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, cartId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    CartService cartService = new CartService();
                    cartService.setCartId(rs.getInt("CartId"));
                    cartService.setQuantity(rs.getInt("quantity"));

                    Service service = new Service();
                    service.setServiceId(rs.getInt("ServiceId"));
                    service.setServiceName(rs.getString("ServiceName"));
                    service.setPrice(rs.getInt("Price"));

                    cartService.setService(service);
                    cartServices.add(cartService);
                }
                return cartServices;
            }
        } catch (SQLException e) {
            // Handle exception
        }
        return cartServices;
    }

    public Map<Integer, Integer> getServiceCannotDisable(int cartId) {
        String sql = """
                SELECT rns.ServiceId, rns.quantity
                FROM Room r
                JOIN RoomNService rns ON rns.TypeId = r.TypeId
                WHERE r.RoomNumber = (
                    select RoomNumber from Cart
                    where CartId=?
                )""";
        Map<Integer, Integer> serviceMap = new HashMap<>();
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, cartId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    serviceMap.put(rs.getInt("ServiceId"), rs.getInt("quantity"));
                }
            }
        } catch (SQLException e) {
            // Handle exception
        }
        return serviceMap;
    }

    public List<Service> getOtherServices(List<Integer> serviceIds) {
        StringBuilder placeholders = new StringBuilder();
        for (int i = 0; i < serviceIds.size(); i++) {
            placeholders.append("?");
            if (i < serviceIds.size() - 1) {
                placeholders.append(",");
            }
        }
        String sql = "SELECT ServiceId, ServiceName, Price FROM Service WHERE ServiceId NOT IN (" + placeholders + ")";
        List<Service> services = new ArrayList<>();
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            for (int i = 0; i < serviceIds.size(); i++) {
                ps.setInt(i + 1, serviceIds.get(i));
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Service service = new Service();
                    service.setServiceId(rs.getInt("ServiceId"));
                    service.setServiceName(rs.getString("ServiceName"));
                    service.setPrice(rs.getInt("Price"));
                    services.add(service);
                }
            }
        } catch (SQLException e) {
            // Handle exception
        }
        return services;
    }

    public void updateCheckinCheckout(int cartId, Date checkin, Date checkout) {
        String sql = "UPDATE Cart SET StartDate = ?, EndDate = ? WHERE CartId = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setDate(1, checkin);
            ps.setDate(2, checkout);
            ps.setInt(3, cartId);
            ps.executeUpdate();
        } catch (SQLException e) {
            // Handle exception
        }
    }

    public void updateCartServiceQuantity(int cartId, int serviceId, int quantity) {
        String sql = "UPDATE CartService SET quantity = ? WHERE CartId = ? AND ServiceId = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, quantity);
            ps.setInt(2, cartId);
            ps.setInt(3, serviceId);
            ps.executeUpdate();
        } catch (SQLException e) {
            // Handle exception
        }
    }

    public void deleteCartService(int cartId, int serviceId) {
        String sql = "DELETE FROM CartService WHERE CartId = ? AND ServiceId = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, cartId);
            ps.setInt(2, serviceId);
            ps.executeUpdate();
        } catch (SQLException e) {
            // Handle exception
        }
    }

    public void addServiceToCart(int cartId, int serviceId, int quantity) {
        String sql = "INSERT INTO CartService (CartId, ServiceId, quantity) VALUES (?, ?, ?)";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, cartId);
            ps.setInt(2, serviceId);
            ps.setInt(3, quantity);
            ps.executeUpdate();
        } catch (SQLException e) {
            // Handle exception
        }
    }

    //write function get cart by cartId
    public Cart getCartByCartId(int cartId) {
        String sql = """
                SELECT c.CartId, c.TotalPrice, c.Status, c.StartDate, c.EndDate, c.isActive, c.CustomerId,
                                c.isPayment, c.PaymentMethodId, c.Adults, c.Children, c.RoomNumber
                                FROM Cart c
                                WHERE c.CartId = ?
                """;

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, cartId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Cart cart = new Cart();
                    cart.setCartId(rs.getInt("CartId"));
                    cart.setTotalPrice(new BigInteger(rs.getString("TotalPrice")));
                    cart.setStatus(rs.getString("Status"));
                    cart.setStartDate(rs.getDate("StartDate"));
                    cart.setEndDate(rs.getDate("EndDate"));
                    cart.setIsActive(rs.getBoolean("IsActive"));
                    Customer customer = new Customer();
                    customer.setCustomerId(rs.getInt("CustomerId"));
                    cart.setCustomer(customer);
                    PaymentMethod paymentMethod = new PaymentMethod();
                    paymentMethod.setPaymentMethodId(rs.getInt("PaymentMethodId"));
                    cart.setPaymentMethod(paymentMethod);
                    cart.setIsPayment(rs.getBoolean("isPayment"));
                    cart.setAdults(rs.getInt("Adults"));
                    cart.setChildren(rs.getInt("Children"));
                    cart.setRoomNumber(rs.getInt("RoomNumber"));
                    cart.setRoom(getRoomAndTypeRoom(cart.getRoomNumber()));
                    List<CartService> cartServices = getCartServicesByCartId(cartId);
                    cart.setCartServices(cartServices);
                    return cart;
                }
            }
        } catch (SQLException e) {
            // Handle exception (log or rethrow)
        }

        return null;
    }

    public void updateMainCustomerId(int mainCustomerId, int cartId) {
        String sql = "update Cart set mainCustomerid = ? where cartId = ?";
//        , PayDay = GETDATE()
        try (PreparedStatement ptm = con.prepareStatement(sql)) {
            ptm.setInt(1, mainCustomerId);
            ptm.setInt(2, cartId);
            ptm.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateStatusAndIsPayment(Cart cart) {
        String sql = "update Cart set Status = ? , isPayment = ?, PaymentMethodId = ? where cartId = ?";
        try (PreparedStatement ptm = con.prepareStatement(sql)) {
            ptm.setString(1, cart.getStatus());
            ptm.setBoolean(2, cart.isIsPayment());
            if (cart.getPaymentMethod() == null) {
                ptm.setNull(3, java.sql.Types.INTEGER);
            } else {
                ptm.setInt(3, cart.getPaymentMethod().getPaymentMethodId());
            }

            ptm.setInt(4, cart.getCartId());
            ptm.executeUpdate();
        } catch (Exception e) {
        }
    }

    public void updateCartInCheckout(Cart cart) {
        String sql = "update Cart set Status = 'Processing' , isPayment = 1, PayDay = ?, PaymentMethodId = 1  where cartId = ?";
        try (PreparedStatement ptm = con.prepareStatement(sql)) {
            ptm.setTimestamp(1, cart.getPayDay());
            ptm.setInt(2, cart.getCartId());
            ptm.executeUpdate();
        } catch (Exception e) {
        }
    }

    public void updateCartToFail(Cart cart) {
        String sql = "update Cart set Status = 'Pending' , isPayment = 0, PayDay = null  where cartId = ?";
        try (PreparedStatement ptm = con.prepareStatement(sql)) {
            ptm.setInt(1, cart.getCartId());
            ptm.executeUpdate();
        } catch (Exception e) {
        }
    }

    public void changeRoomNumber(Cart cart, Date startDate, Date endDate) {
        if (!checkRoomNumberStatus(cart.getRoomNumber(), startDate, endDate)) {
            int newRoom = getRoomNumber(getTyperoomOfRoomNumber(cart.getRoomNumber()), startDate, endDate);
            if (newRoom == 0) {
                cart.setRoomNumber(newRoom);
            } else {
                cart.setRoomNumber(newRoom);
                updateCartRoomNumber(cart.getCartId(), newRoom);
            }
        }
    }

    public Cart checkCart(int cartId, int customerId) {
        String sql = """
                SELECT c.CartId, c.TotalPrice, c.Status, c.StartDate, c.EndDate, c.isActive,
                                                c.isPayment, c.PaymentMethodId, c.Adults, c.Children, c.RoomNumber
                                                FROM Cart c where c.CartId = ? and c.CustomerId = ?
                """;

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, cartId);
            ps.setInt(2, customerId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Cart cart = new Cart();
                    cart.setCartId(rs.getInt("CartId"));
                    cart.setTotalPrice(new BigInteger(rs.getString("TotalPrice")));
                    cart.setStatus(rs.getString("Status"));
                    cart.setStartDate(rs.getDate("StartDate"));
                    cart.setEndDate(rs.getDate("EndDate"));
                    cart.setIsActive(rs.getBoolean("IsActive"));
                    cart.setIsPayment(rs.getBoolean("isPayment"));
                    cart.setAdults(rs.getInt("Adults"));
                    cart.setChildren(rs.getInt("Children"));
                    cart.setRoomNumber(rs.getInt("RoomNumber"));
                    cart.setRoom(getRoomAndTypeRoom(cart.getRoomNumber()));
                    List<CartService> cartServices = getCartServicesByCartId(cartId);
                    cart.setCartServices(cartServices);
                    return cart;
                }
            }
        } catch (SQLException e) {
            // Handle exception (log or rethrow)
        }

        return null;
    }

    public List<Cart> getAllCompletedCheckInCarts() {
        List<Cart> listCart = new ArrayList<>();
        String sql = """
        SELECT c.CartId, c.StartDate, c.EndDate, c.TotalPrice, c.RoomNumber, c.Status,
               c.PayDay, c.isActive, c.isPayment,
               pm.PaymentMethodId, pm.PaymentName,
               cus.CustomerId, cus.FullName, cus.Email, cus.PhoneNumber, cus.Gender,
               s.ServiceId, s.ServiceName, s.Price, cs.Quantity
        FROM Cart c
        JOIN PaymentMethod pm ON c.PaymentMethodId = pm.PaymentMethodId
        JOIN Customer cus ON c.mainCustomerId = cus.CustomerId
        LEFT JOIN CartService cs ON c.CartId = cs.CartId
        LEFT JOIN Service s ON s.ServiceId = cs.ServiceId
        WHERE c.Status = 'Completed CheckIn' AND c.isPayment = 1
        ORDER BY c.CartId
    """;

        try (PreparedStatement ptm = con.prepareStatement(sql); ResultSet rs = ptm.executeQuery()) {
            Map<Integer, Cart> cartMap = new LinkedHashMap<>();
            while (rs.next()) {
                int cartId = rs.getInt("CartId");
                Cart cart = cartMap.get(cartId);
                if (cart == null) {
                    cart = new Cart();
                    cart.setCartId(cartId);
                    cart.setStartDate(rs.getDate("StartDate"));
                    cart.setEndDate(rs.getDate("EndDate"));
                    cart.setTotalPrice(new BigInteger(rs.getString("TotalPrice")));
                    cart.setRoomNumber(rs.getInt("RoomNumber"));
                    cart.setStatus(rs.getString("Status"));
                    cart.setPayDay(rs.getTimestamp("PayDay"));
                    cart.setIsActive(rs.getBoolean("isActive"));
                    cart.setIsPayment(rs.getBoolean("isPayment"));

                    PaymentMethod pm = new PaymentMethod(rs.getInt("PaymentMethodId"), rs.getString("PaymentName"));
                    cart.setPaymentMethod(pm);

                    Customer cus = new Customer();
                    cus.setCustomerId(rs.getInt("CustomerId"));
                    cus.setFullName(rs.getString("FullName"));
                    cus.setEmail(rs.getString("Email"));
                    cus.setPhoneNumber(rs.getString("PhoneNumber"));
                    cus.setGender(rs.getBoolean("Gender"));
                    cart.setMainCustomer(cus);

                    cartMap.put(cartId, cart);
                }

                int serviceId = rs.getInt("ServiceId");
                if (serviceId != 0) {
                    Service service = new Service(serviceId, rs.getString("ServiceName"), rs.getInt("Price"));
                    int quantity = rs.getInt("Quantity");
                    CartService cs = new CartService();
                    cs.setService(service);
                    cs.setQuantity(quantity);
                    cart.getCartServices().add(cs);
                }
            }
            listCart.addAll(cartMap.values());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return listCart;
    }

    public int insertCartToBooking(Booking booking) {
        String sql = """
        INSERT INTO Booking (PayDay, Status, CustomerId, PaidAmount, PaymentMethodIdCheckIn)
        VALUES (?, ?, ?, ?, ?)""";

        try (PreparedStatement st = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            st.setTimestamp(1, new java.sql.Timestamp(booking.getPayDay().getTime()));
            st.setString(2, booking.getStatus());
            st.setInt(3, booking.getCustomer().getCustomerId());
            st.setLong(4, booking.getPaidAmount().longValue());
            st.setInt(5, booking.getPaymentMethodCheckIn().getPaymentMethodId());

            st.executeUpdate();

            try (ResultSet rs = st.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1); // BookingId
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1;
    }

    public int insertCartToBookingDetail(BookingDetail bd) {
        String sql = """
                 INSERT INTO BookingDetail (StartDate, EndDate, BookingId, RoomNumber, TotalAmount)
                 VALUES (?, ?, ?, ?, ?)""";
        try (PreparedStatement st = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            st.setDate(1, bd.getStartDate());
            st.setDate(2, bd.getEndDate());
            st.setInt(3, bd.getBooking().getBookingId());
            st.setInt(4, bd.getRoom().getRoomNumber());
            st.setLong(5, bd.getTotalAmount().longValue());

            st.executeUpdate();
            try (ResultSet rs = st.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1); // BookingDetailId
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public void insertDetailServices(int bookingDetailId, List<DetailService> services) {
        String sql = """
        INSERT INTO DetailService (BookingDetailId, ServiceId, quantity, PriceAtTime)
        VALUES (?, ?, ?, ?)
    """;
        try (PreparedStatement st = con.prepareStatement(sql)) {
            for (DetailService ds : services) {
                st.setInt(1, bookingDetailId);
                st.setInt(2, ds.getService().getServiceId());
                st.setInt(3, ds.getQuantity());
                st.setLong(4, ds.getPriceAtTime().longValue());
                st.addBatch();
            }
            st.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteCartAndCartServiceById(int cartId) {
        String deleteCartServiceSql = "DELETE FROM CartService WHERE CartId = ?";
        String deleteCartSql = "DELETE FROM Cart WHERE CartId = ?";

        try (
                PreparedStatement ps1 = con.prepareStatement(deleteCartServiceSql); PreparedStatement ps2 = con.prepareStatement(deleteCartSql)) {
            // Xóa CartService trước do có khóa ngoại tới Cart
            ps1.setInt(1, cartId);
            ps1.executeUpdate();

            // Xóa Cart
            ps2.setInt(1, cartId);
            ps2.executeUpdate();

            System.out.println("Đã xóa cartId = " + cartId + " thành công.");

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Lỗi khi xóa cartId: " + cartId, e);
        }
    }
}
