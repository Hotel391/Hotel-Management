package dal;

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

    public List<Room> getAllRoom() {
        List<Room> listRoom = Collections.synchronizedList(new ArrayList<>());

        String sql = "SELECT r.RoomNumber, r.isCleaner, "
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

                    foundRoom = new Room(roomNumber, rs.getBoolean("isCleaner"), typeRoom);
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

    public void updateRoom(int typeRoomID, int roomNumber) {
        String sql = "UPDATE [dbo].[Room]\n"
                + "   SET [TypeId] = ?\n"
                + " WHERE [RoomNumber] =?";

        try (PreparedStatement ptm = con.prepareStatement(sql);) {
            ptm.setInt(1, typeRoomID);
            ptm.setInt(2, roomNumber);
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

    public List<TypeRoom> getAllTypeRoom() {
        List<TypeRoom> listTypeRoom = Collections.synchronizedList(new ArrayList<>());

        String sql = "SELECT [TypeId]\n"
                + "      ,[Description]\n"
                + "      ,[TypeName]\n"
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
                    int typeId= rs.getInt("TypeId");
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
            //
        }
        return 0;
    }
}
