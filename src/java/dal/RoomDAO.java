package dal;

import models.BookingDetail;
import models.Room;
import models.RoomNService;
import models.Service;
import models.TypeRoom;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RoomDAO {

    private static RoomDAO instance;
    private Connection con;

    public static RoomDAO getInstance() {
        if (instance == null) {
            instance = new RoomDAO();
        }
        return instance;
    }

    private RoomDAO() {
        con = new DBContext().connect;
    }

    public int roomCount() {
        String sql = "select count(*) from Room";

        try (PreparedStatement st = con.prepareStatement(sql); ResultSet rs = st.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
        }
        return 0;
    }

    public int roomAvailableCount() {
        String sql = """
                     SELECT COUNT(*) \r
                     FROM Room r\r
                     WHERE NOT EXISTS (\r
                         SELECT 1 FROM BookingDetail bd \r
                     \tWHERE bd.RoomNumber = r.RoomNumber and \r
                     \tCONVERT(DATE, GETDATE()) >= bd.StartDate\r
                           and CONVERT(DATE, GETDATE()) < bd.EndDate\r
                     )\r
                     """;

        try (PreparedStatement st = con.prepareStatement(sql); ResultSet rs = st.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
        }
        return 0;
    }

    public int roomBookedCount() {
        String sql = """
                     SELECT COUNT(*) \r
                     FROM Room r\r
                     WHERE EXISTS (\r
                         SELECT 1 FROM BookingDetail bd \r
                     \tWHERE bd.RoomNumber = r.RoomNumber and \r
                     \tCONVERT(DATE, GETDATE()) >= bd.StartDate\r
                           and CONVERT(DATE, GETDATE()) < bd.EndDate\r
                     )\r
                     """;

        try (PreparedStatement st = con.prepareStatement(sql); ResultSet rs = st.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
        }
        return 0;
    }

    public Room getRoomByNumber(int roomNumber) {
        String sql = "SELECT * from room where roomNumber = ?";

        try (PreparedStatement ptm = con.prepareStatement(sql)) {
            ptm.setInt(1, roomNumber);
            try (ResultSet rs = ptm.executeQuery()) {
                if (rs.next()) {

                    Room room = new Room();

                    room.setIsCleaner(rs.getBoolean("isCleaner"));

                    room.setRoomNumber(rs.getInt("RoomNumber"));

                    room.setTypeRoom(TypeRoomDAO.getInstance().getTypeRoomById(rs.getInt("typeId")));

                    return room;
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public Room getRoomByRoomNumber(int roomNumber) {
        String sql = "SELECT * FROM Room r JOIN TypeRoom t ON r.TypeId = t.TypeId WHERE r.RoomNumber = ?";
        try (PreparedStatement st = con.prepareStatement(sql)) {
            st.setInt(1, roomNumber);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                Room r = new Room();
                r.setRoomNumber(roomNumber);
                TypeRoom t = new TypeRoom();
                t.setTypeId(rs.getInt("TypeId"));
                t.setTypeName(rs.getString("TypeName"));
                t.setPrice(rs.getInt("Price"));
                r.setTypeRoom(t);
                return r;
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Room> getAllRoom() {
        List<Room> listRoom = Collections.synchronizedList(new ArrayList<>());

        String sql = "SELECT r.RoomNumber, r.isCleaner, r.IsActive,"
                + "tr.TypeId, tr.TypeName, tr.Description, tr.Price, "
                + "s.ServiceId, s.ServiceName, s.Price AS ServicePrice, rns.quantity "
                + "FROM Room r "
                + "JOIN TypeRoom tr ON r.TypeId = tr.TypeId "
                + "LEFT JOIN RoomNService rns ON tr.TypeId = rns.TypeId "
                + "LEFT JOIN [Service] s ON rns.ServiceId = s.ServiceId "
                + "ORDER BY r.RoomNumber";

        try (PreparedStatement ptm = con.prepareStatement(sql); ResultSet rs = ptm.executeQuery()) {

            while (rs.next()) {
                int roomNumber = rs.getInt("RoomNumber");
                Room foundRoom = null;

                // Tìm xem Room đã tồn tại trong list chưa
                for (Room r : listRoom) {
                    if (r.getRoomNumber() == roomNumber) {
                        foundRoom = r;
                        break;
                    }
                }

                if (foundRoom == null) {
                    TypeRoom typeRoom = new TypeRoom();
                    typeRoom.setTypeId(rs.getInt("TypeId"));
                    typeRoom.setTypeName(rs.getString("TypeName"));
                    typeRoom.setDescription(rs.getString("Description"));
                    typeRoom.setPrice(rs.getInt("Price"));

                    foundRoom = new Room(roomNumber, rs.getBoolean("isCleaner"),
                            rs.getBoolean("IsActive"), typeRoom);
                    listRoom.add(foundRoom);
                }

                int serviceId = rs.getInt("ServiceId");
                if (serviceId != 0) {
                    Service service = new Service();
                    service.setServiceId(serviceId);
                    service.setServiceName(rs.getString("ServiceName"));
                    service.setPrice(rs.getInt("ServicePrice"));

                    RoomNService rns = new RoomNService();
                    rns.setService(service);
                    rns.setQuantity(rs.getInt("quantity"));
                    rns.setTypeRoom(foundRoom.getTypeRoom());

                    foundRoom.getTypeRoom().addRoomNService(rns);
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return listRoom;
    }

    public List<Room> searchAllRoom(String roomNumber, Integer typeRoomId) {
        List<Room> listRoom = new Vector<>();

        StringBuilder sql = new StringBuilder("SELECT r.RoomNumber, r.isCleaner, r.IsActive, "
                + "tr.TypeId, tr.TypeName, tr.Description, tr.Price, "
                + "s.ServiceId, s.ServiceName, s.Price AS ServicePrice, rns.quantity "
                + "FROM Room r "
                + "JOIN TypeRoom tr ON r.TypeId = tr.TypeId "
                + "LEFT JOIN RoomNService rns ON tr.TypeId = rns.TypeId "
                + "LEFT JOIN [Service] s ON rns.ServiceId = s.ServiceId "
                + "WHERE 1=1 ");

        if (roomNumber != null) {
            sql.append("AND r.RoomNumber like ? ");
        }
        if (typeRoomId != null) {
            sql.append("AND tr.TypeId = ? ");
        }

        sql.append("ORDER BY r.RoomNumber");

        try (PreparedStatement ptm = con.prepareStatement(sql.toString())) {
            int index = 1;
            if (roomNumber != null) {
                ptm.setString(index++, "%" + roomNumber + "%");
            }
            if (typeRoomId != null) {
                ptm.setInt(index++, typeRoomId);
            }

            try (ResultSet rs = ptm.executeQuery()) {
                while (rs.next()) {
                    int rNumber = rs.getInt("RoomNumber");
                    Room foundRoom = null;

                    // Tìm xem Room đã tồn tại trong list chưa
                    for (Room r : listRoom) {
                        if (r.getRoomNumber() == rNumber) {
                            foundRoom = r;
                            break;
                        }
                    }

                    if (foundRoom == null) {
                        TypeRoom typeRoom = new TypeRoom();
                        typeRoom.setTypeId(rs.getInt("TypeId"));
                        typeRoom.setTypeName(rs.getString("TypeName"));
                        typeRoom.setDescription(rs.getString("Description"));
                        typeRoom.setPrice(rs.getInt("Price"));

                        foundRoom = new Room(rNumber, rs.getBoolean("isCleaner"),
                                rs.getBoolean("IsActive"), typeRoom);
                        listRoom.add(foundRoom);
                    }

                    int serviceId = rs.getInt("ServiceId");
                    if (!rs.wasNull()) {
                        Service service = new Service();
                        service.setServiceId(serviceId);
                        service.setServiceName(rs.getString("ServiceName"));
                        service.setPrice(rs.getInt("ServicePrice"));

                        RoomNService rns = new RoomNService();
                        rns.setService(service);
                        rns.setQuantity(rs.getInt("quantity"));
                        rns.setTypeRoom(foundRoom.getTypeRoom());

                        foundRoom.getTypeRoom().addRoomNService(rns);
                    }
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return listRoom;
    }

    public void updateRoom(int typeRoomID, int roomNumber, boolean isActive) {
        String sql = "UPDATE [dbo].[Room]\n"
                + "   SET [TypeId] = ?\n"
                + "   ,[IsActive] = ?\n"
                + " WHERE [RoomNumber] =?";

        try (PreparedStatement ptm = con.prepareStatement(sql);) {
            ptm.setInt(1, typeRoomID);
            ptm.setBoolean(2, isActive);
            ptm.setInt(3, roomNumber);
            ptm.executeUpdate();
        } catch (SQLException ex) {
            ex.getStackTrace();
        }
    }

    public int insertRoom(int roomNumber, int typeRoom) {
        String sql = "INSERT INTO [dbo].[Room]\n"
                + "           ([RoomNumber]\n"
                + "           ,[TypeId])\n"
                + "     VALUES(?, ?)";
        int n = 0;
        try (PreparedStatement ptm = con.prepareStatement(sql);) {

            ptm.setInt(1, roomNumber);
            ptm.setInt(2, typeRoom);
            n = ptm.executeUpdate();
        } catch (SQLException ex) {
            ex.getStackTrace();
        }
        return n;
    }

    public void deleteRoom(int roomNumber) {
        String sql = "DELETE FROM [dbo].[Room]\n"
                + "      WHERE RoomNumber =?";

        try (PreparedStatement ptm = con.prepareStatement(sql);) {
            ptm.setInt(1, roomNumber);
            ptm.executeUpdate();
        } catch (SQLException ex) {
            ex.getStackTrace();
        }
    }

    public void changeIsActiveRoom(int roomNumber, String isActiveStr) {
        String sql = "UPDATE [dbo].[Room] SET [IsActive] = ? WHERE RoomNumber = ?";

        try (PreparedStatement ptm = con.prepareStatement(sql)) {
            // Chuyển String sang boolean rồi đảo ngược
            boolean currentIsActive = "1".equals(isActiveStr) || "true".equalsIgnoreCase(isActiveStr);
            boolean newIsActive = !currentIsActive;

            ptm.setBoolean(1, newIsActive);
            ptm.setInt(2, roomNumber);
            ptm.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public List<TypeRoom> getAllTypeRoom() {
        List<TypeRoom> listTypeRoom = Collections.synchronizedList(new ArrayList<>());

        String sql = "SELECT [TypeId]\n"
                + "      ,[TypeName]\n"
                + "      ,[Description]\n"
                + "      ,[Price]\n"
                + "  FROM [HotelManagementDB].[dbo].[TypeRoom]";

        try (PreparedStatement ptm = con.prepareStatement(sql); ResultSet rs = ptm.executeQuery()) {

            while (rs.next()) {
                TypeRoom tr = new TypeRoom(rs.getInt(1),
                        rs.getString(2), rs.getString(3),
                        rs.getInt(4));
                listTypeRoom.add(tr);
            }

        } catch (Exception ex) {
        }

        return listTypeRoom;
    }

    public List<Room> getAllNotAvailableRoomOfCleaner(int startFloor, int endFloor, int startIndex, int numberRow) {
        List<Room> listRoom = Collections.synchronizedList(new ArrayList<>());

        String sql = String.format("""
                        SELECT RoomNumber FROM Room
                        WHERE isCleaner = 0 AND RoomNumber > ? AND RoomNumber < ?
                        ORDER BY RoomNumber
                        OFFSET ? ROWS FETCH NEXT %d ROWS ONLY
                        """, numberRow);

        try (PreparedStatement ptm = con.prepareStatement(sql)) {
            ptm.setInt(1, startFloor * 1000);
            ptm.setInt(2, (endFloor + 1) * 1000);
            ptm.setInt(3, (startIndex - 1) * numberRow);
            try (ResultSet rs = ptm.executeQuery()) {
                while (rs.next()) {
                    int roomNumber = rs.getInt("RoomNumber");
                    Room room = new Room();
                    room.setRoomNumber(roomNumber);
                    listRoom.add(room);
                }
            }
        } catch (SQLException e) {
            //
        }
        return listRoom;
    }

    public int getTotalNotAvailableRoomOfCleaner(int startFloor, int endFloor) {
        String sql = """
                     SELECT COUNT(RoomNumber) FROM Room
                     WHERE isCleaner = 0 AND RoomNumber > ? AND RoomNumber < ?
                     """;

        try (PreparedStatement ptm = con.prepareStatement(sql)) {
            ptm.setInt(1, startFloor * 1000);
            ptm.setInt(2, (endFloor + 1) * 1000);
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

    public void updateRoomStatus(int roomNumber, boolean status) {
        String sql = "UPDATE Room SET isCleaner = ? WHERE RoomNumber = ?";

        try (PreparedStatement ptm = con.prepareStatement(sql)) {
            ptm.setBoolean(1, status);
            ptm.setInt(2, roomNumber);
            ptm.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Room> getStayingRooms(int numberRow, int pageIndex, String search) {
        List<Room> stayingRooms = new ArrayList<>();
        StringBuilder sql = new StringBuilder("""
            select r.RoomNumber, r.isCleaner,r.TypeId, tr.TypeName, tr.Price from Room r
            join BookingDetail bd on bd.RoomNumber=r.RoomNumber
            join TypeRoom tr on tr.TypeId=r.TypeId
            WHERE bd.StartDate<= CAST(GETDATE() AS DATE) and bd.EndDate>=CAST(GETDATE() AS DATE)
        """);

        if (search != null && !search.isEmpty()) {
            sql.append(" AND r.RoomNumber LIKE ?");
        }

        sql.append(" ORDER BY r.RoomNumber OFFSET ? ROWS FETCH NEXT %d ROWS ONLY".formatted(numberRow));

        try (PreparedStatement ptm = con.prepareStatement(sql.toString())) {
            int paramIndex = 1;
            if (search != null && !search.isEmpty()) {
                ptm.setString(paramIndex++, "%" + search + "%");
            }
            ptm.setInt(paramIndex, (pageIndex - 1) * numberRow);

            try (ResultSet rs = ptm.executeQuery()) {
                while (rs.next()) {
                    int roomNumber = rs.getInt("RoomNumber");
                    boolean isCleaner = rs.getBoolean("isCleaner");
                    int typeId = rs.getInt("TypeId");
                    String typeName = rs.getString("TypeName");
                    int price = rs.getInt("Price");

                    TypeRoom typeRoom = new TypeRoom();
                    typeRoom.setTypeId(typeId);
                    typeRoom.setTypeName(typeName);
                    typeRoom.setPrice(price);

                    Room room = new Room(roomNumber, isCleaner, typeRoom);
                    stayingRooms.add(room);
                }
            }
        } catch (SQLException e) {
            //
        }
        return stayingRooms;
    }

    public int getTotalStayingRooms(String search) {
        String sql = """
                    select COUNT(*) from Room r
                    join BookingDetail bd on bd.RoomNumber=r.RoomNumber
                    join TypeRoom tr on tr.TypeId=r.TypeId
                    WHERE bd.StartDate<= CAST(GETDATE() AS DATE) and bd.EndDate>=CAST(GETDATE() AS DATE)
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

        }
        return 0;
    }

    public List<Room> searchAvailableRooms(java.sql.Date startDate,
            java.sql.Date endDate, Integer typeRoomId, Integer adult, Integer children, int page, int recordsPerPage) {
        List<Room> availableRooms = new ArrayList<>();
        int startIndex = (page - 1) * recordsPerPage;

        StringBuilder queryBuilder = new StringBuilder("""
        SELECT r.RoomNumber, r.TypeId, r.IsActive, 
               tr.TypeName, tr.Price, tr.Description, tr.Adult, tr.Children
        FROM Room r
        JOIN TypeRoom tr ON r.TypeId = tr.TypeId
        LEFT JOIN BookingDetail bd ON bd.RoomNumber = r.RoomNumber
            AND NOT (bd.EndDate <= ? OR bd.StartDate >= ?)
            AND EXISTS (
                SELECT 1 FROM Booking b 
                WHERE b.BookingId = bd.BookingId 
                AND b.Status != 'Completed CheckOut')
        LEFT JOIN Cart c ON c.RoomNumber = r.RoomNumber AND c.isPayment = 1
            AND NOT (c.EndDate <= ? OR c.StartDate >= ?)
        WHERE r.IsActive = 1 AND r.isCleaner = 1
          AND bd.BookingDetailId IS NULL
          AND c.CartId IS NULL
    """);

        if (typeRoomId != null) {
            queryBuilder.append(" AND r.TypeId = ?");
        }
        if (adult != null) {
            queryBuilder.append(" AND tr.Adult >= ?");
        }
        if (children != null) {
            queryBuilder.append(" AND tr.Children >= ?");
        }

        queryBuilder.append(" ORDER BY r.RoomNumber OFFSET ? ROWS FETCH NEXT ? ROWS ONLY");

        try (PreparedStatement pst = con.prepareStatement(queryBuilder.toString())) {
            int paramIndex = 1;
            pst.setDate(paramIndex++, startDate); // bd.EndDate <= ?
            pst.setDate(paramIndex++, endDate);   // bd.StartDate >= ?
            pst.setDate(paramIndex++, startDate); // c.EndDate <= ?
            pst.setDate(paramIndex++, endDate);   // c.StartDate >= ?

            if (typeRoomId != null) {
                pst.setInt(paramIndex++, typeRoomId);
            }
            if (adult != null) {
                pst.setInt(paramIndex++, adult);
            }
            if (children != null) {
                pst.setInt(paramIndex++, children);
            }

            pst.setInt(paramIndex++, startIndex);
            pst.setInt(paramIndex, recordsPerPage);

            ResultSet rs = pst.executeQuery();
            System.out.println("Executing query: " + queryBuilder.toString());
            System.out.println("Parameters - startDate: " + startDate + ", endDate: " + endDate
                    + ", typeRoomId: " + typeRoomId + ", adult: " + adult + ", children: " + children
                    + ", page: " + page + ", recordsPerPage: " + recordsPerPage);
            while (rs.next()) {
                Room room = new Room();
                room.setRoomNumber(rs.getInt("RoomNumber"));
                room.setIsActive(rs.getBoolean("IsActive"));

                TypeRoom typeRoom = new TypeRoom();
                typeRoom.setTypeId(rs.getInt("TypeId"));
                typeRoom.setTypeName(rs.getString("TypeName"));
                typeRoom.setPrice(rs.getInt("Price"));
                typeRoom.setDescription(rs.getString("Description"));
                typeRoom.setMaxAdult(rs.getInt("Adult")); // Map to maxAdult
                typeRoom.setMaxChildren(rs.getInt("Children")); // Map to maxChildren

                room.setTypeRoom(typeRoom);

                List<RoomNService> services = getServicesForRoom(rs.getInt("TypeId"));
                typeRoom.setServices(services);

                availableRooms.add(room);
                System.out.println("Found room: " + room.getRoomNumber() + ", Type: " + typeRoom.getTypeName());
            }
            System.out.println("Total rooms found: " + availableRooms.size());
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("SQL Error: " + e.getMessage());
        }

        return availableRooms;
    }

    public List<RoomNService> getServicesForRoom(int typeId) {
        List<RoomNService> services = new ArrayList<>();
        String sql = "SELECT s.ServiceName, s.Price, rns.Quantity FROM RoomNService rns "
                + "JOIN Service s ON rns.ServiceId = s.ServiceId "
                + "WHERE rns.TypeId = ? "
                + "ORDER BY s.Price ASC";
        try (PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setInt(1, typeId);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                Service service = new Service();
                service.setServiceName(rs.getString("ServiceName"));
                service.setPrice(rs.getInt("Price"));

                RoomNService roomNService = new RoomNService();
                roomNService.setService(service);
                roomNService.setQuantity(rs.getInt("Quantity"));

                services.add(roomNService);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return services;
    }

    public Service getServiceById(int serviceId) {
        Service service = null;
        String query = "SELECT * FROM [Service] WHERE ServiceId = ?";

        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setInt(1, serviceId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    service = new Service();
                    service.setServiceId(rs.getInt("ServiceId"));
                    service.setServiceName(rs.getString("ServiceName"));
                    service.setPrice(rs.getInt("Price"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return service;
    }

    public int countAvailableRooms(java.sql.Date startDate, java.sql.Date endDate, Integer typeRoomId, Integer adult, Integer children) {
        int count = 0;
        StringBuilder queryBuilder = new StringBuilder("""
        SELECT COUNT(r.RoomNumber)
        FROM Room r
        JOIN TypeRoom tr ON r.TypeId = tr.TypeId
        WHERE r.IsActive = 1  AND r.isCleaner = 1
        AND r.RoomNumber NOT IN (
            SELECT bd.RoomNumber
            FROM BookingDetail bd
            JOIN Booking b ON bd.BookingId = b.BookingId
            WHERE (bd.StartDate < ? AND bd.EndDate > ?)
            AND EXISTS (
                SELECT 1 FROM Booking b 
                WHERE b.BookingId = bd.BookingId 
                AND b.Status != 'Completed CheckOut')
            UNION
            SELECT c.RoomNumber
            FROM Cart c
            WHERE c.isPayment = 1
            AND (c.StartDate < ? AND c.EndDate > ?)
        )
        """);

        if (typeRoomId != null) {
            queryBuilder.append(" AND r.TypeId = ?");
        }
        if (adult != null) {
            queryBuilder.append(" AND tr.Adult >= ?");
        }
        if (children != null) {
            queryBuilder.append(" AND tr.Children >= ?");
        }

        try (PreparedStatement pst = con.prepareStatement(queryBuilder.toString())) {
            int paramIndex = 1;

            pst.setDate(paramIndex++, endDate);
            pst.setDate(paramIndex++, startDate);
            pst.setDate(paramIndex++, endDate);
            pst.setDate(paramIndex++, startDate);

            if (typeRoomId != null) {
                pst.setInt(paramIndex++, typeRoomId);
            }
            if (adult != null) {
                pst.setInt(paramIndex++, adult);
            }
            if (children != null) {
                pst.setInt(paramIndex++, children);
            }

            ResultSet rs = pst.executeQuery();
            System.out.println("Count query: " + queryBuilder.toString());
            System.out.println("Count parameters - startDate: " + startDate + ", endDate: " + endDate
                    + ", typeRoomId: " + typeRoomId + ", adult: " + adult + ", children: " + children);
            if (rs.next()) {
                count = rs.getInt(1);
                System.out.println("Total available rooms: " + count);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("SQL Error: " + e.getMessage());
        }

        return count;
    }

    public Integer getTypeIdByRoomNumber(int roomNumber) {
        Integer typeId = null;
        String query = "SELECT TypeId FROM Room WHERE RoomNumber = ?";

        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setInt(1, roomNumber);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                typeId = rs.getInt("TypeId");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return typeId;
    }

    public boolean isRoomAvailable(int roomNumber, java.sql.Date startDate, java.sql.Date endDate) {
        String query = """
            SELECT COUNT(*) FROM Room r
            WHERE r.RoomNumber = ?
            AND r.IsActive = 1
            AND r.isCleaner = 1
            AND r.RoomNumber NOT IN (
                SELECT bd.RoomNumber FROM BookingDetail bd
                WHERE (bd.StartDate < ? AND bd.EndDate > ?)
                AND EXISTS (
                    SELECT 1 FROM Booking b 
                    WHERE b.BookingId = bd.BookingId 
                    AND b.Status != 'Completed CheckOut')
                UNION
                SELECT c.RoomNumber FROM Cart c
                WHERE c.isPayment = 1
                AND (c.StartDate < ? AND c.EndDate > ?)
            )
        """;
        try {
            con.setAutoCommit(false);
            con.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
            try (PreparedStatement pst = con.prepareStatement(query)) {
                pst.setInt(1, roomNumber);
                pst.setDate(2, endDate);   // bd.StartDate < endDate
                pst.setDate(3, startDate); // bd.EndDate > startDate
                pst.setDate(4, endDate);   // c.StartDate < endDate
                pst.setDate(5, startDate); // c.EndDate > startDate
                ResultSet rs = pst.executeQuery();
                boolean available = rs.next() && rs.getInt(1) > 0;
                System.out.println("Room " + roomNumber + " availability check: " + available
                        + ", startDate: " + startDate + ", endDate: " + endDate);
                con.commit();
                return available;
            }
        } catch (SQLException e) {
            try {
                con.rollback();
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
            System.out.println("SQL Error in isRoomAvailable for room " + roomNumber + ": " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            try {
                con.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public List<String> getUnavailableRooms(List<String> roomNumbers, java.sql.Date startDate, java.sql.Date endDate) {
        List<String> conflictRooms = new ArrayList<>();
        for (String roomNumStr : roomNumbers) {
            try {
                int roomNumber = Integer.parseInt(roomNumStr);
                boolean available = isRoomAvailable(roomNumber, startDate, endDate);
                if (!available) {
                    conflictRooms.add(roomNumStr);
                }
            } catch (NumberFormatException e) {
                System.out.println("Room number không hợp lệ: " + roomNumStr);
            }
        }
        return conflictRooms;
    }

    public int getAvailableRoomCount() {
        String sql = "SELECT COUNT(*) FROM Room WHERE isActive = 1";
        try (PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int getTouristThisYear() {
        String sql = """
            SELECT COUNT(DISTINCT CustomerId)
            FROM Booking
            WHERE YEAR(PayDay) = YEAR(GETDATE())
              AND Status IN (N'Completed CheckIn', N'Completed CheckOut')
        """;
        try (PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
