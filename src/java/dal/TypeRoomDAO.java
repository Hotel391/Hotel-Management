package dal;

import models.TypeRoom;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import models.RoomNService;
import models.Service;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class TypeRoomDAO {

    private static TypeRoomDAO instance;
    private Connection con;

    public static TypeRoomDAO getInstance() {
        if (instance == null) {
            instance = new TypeRoomDAO();
        }
        return instance;
    }

    private TypeRoomDAO() {
        con = new DBContext().connect;
    }

    public List<TypeRoom> getAllNameOfTypeRoom() {
        String sql = "select TypeId, TypeName from TypeRoom";
        List<TypeRoom> typeRooms = Collections.synchronizedList(new ArrayList<>());

        try (PreparedStatement st = con.prepareStatement(sql); ResultSet rs = st.executeQuery()) {
            while (rs.next()) {
                TypeRoom typeRoom = new TypeRoom();
                typeRoom.setTypeId(rs.getInt(1));
                typeRoom.setTypeName(rs.getString(2));
                typeRooms.add(typeRoom);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return typeRooms;
    }
    
    public void insertTypeRoom(TypeRoom typeRoom) {
        String sql = "INSERT INTO TypeRoom(TypeName, Description, Price) VALUES(?, ?, ?)";
        try (PreparedStatement st = con.prepareStatement(sql)) {
            st.setString(1, typeRoom.getTypeName());
            st.setString(2, typeRoom.getDescription());
            st.setInt(3, typeRoom.getPrice());
            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public TypeRoom getTypeRoomById(int typeId) {
        String sql = "SELECT TypeId, TypeName, Description, Price FROM TypeRoom WHERE TypeId = ?";
        try (PreparedStatement st = con.prepareStatement(sql)) {
            st.setInt(1, typeId);
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    TypeRoom typeRoom = new TypeRoom();
                    typeRoom.setTypeId(rs.getInt(1));
                    typeRoom.setTypeName(rs.getString(2));
                    typeRoom.setDescription(rs.getString(3));
                    typeRoom.setPrice(rs.getInt(4));
                    return typeRoom;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public TypeRoom getTypeRoomByRoomNumber(int roomNumber) {
        String sql = "SELECT tr.TypeId, tr.TypeName, tr.Description, tr.Price FROM TypeRoom tr JOIN Room r ON tr.TypeId = r.TypeId WHERE r.RoomNumber = ?";
        try (PreparedStatement st = con.prepareStatement(sql)) {
            st.setInt(1, roomNumber);
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    TypeRoom typeRoom = new TypeRoom();
                    typeRoom.setTypeId(rs.getInt(1));
                    typeRoom.setTypeName(rs.getString(2));
                    typeRoom.setDescription(rs.getString(3));
                    typeRoom.setPrice(rs.getInt(4));
                    return typeRoom;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public TypeRoom getTypeRoomByName(String typeName) {
        String sql = "SELECT TypeId, TypeName, Description, Price FROM TypeRoom WHERE TypeName = ?";
        try (PreparedStatement st = con.prepareStatement(sql)) {
            st.setString(1, typeName);
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    TypeRoom typeRoom = new TypeRoom();
                    typeRoom.setTypeId(rs.getInt(1));
                    typeRoom.setTypeName(rs.getString(2));
                    typeRoom.setDescription(rs.getString(3));
                    typeRoom.setPrice(rs.getInt(4));
                    return typeRoom;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<String> getAllTypeRoomName() {
        String sql = "select TypeName from TypeRoom";
        List<String> typeRooms = Collections.synchronizedList(new ArrayList<>());

        try (PreparedStatement st = con.prepareStatement(sql); ResultSet rs = st.executeQuery()) {
            while (rs.next()) {
                typeRooms.add(rs.getString(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return typeRooms;
    }

    public List<TypeRoom> getAllTypeRoom() {
        List<TypeRoom> list = new ArrayList<>();
        Map<Integer, TypeRoom> map = new HashMap<>();
        String sql = """
        SELECT 
            tr.TypeId, tr.TypeName, tr.Description, tr.Price,
            s.ServiceId, s.ServiceName, s.Price AS ServicePrice,
            rns.Quantity
        FROM TypeRoom tr
        LEFT JOIN RoomNService rns ON tr.TypeId = rns.TypeId
        LEFT JOIN Service s ON rns.ServiceId = s.ServiceId
        ORDER BY tr.TypeId
        """;

        try (
                PreparedStatement st = con.prepareStatement(sql); ResultSet rs = st.executeQuery()) {
            while (rs.next()) {
                int typeId = rs.getInt("TypeId");
                TypeRoom typeRoom = map.get(typeId);

                if (typeRoom == null) {
                    typeRoom = new TypeRoom(
                            typeId,
                            rs.getString("TypeName"),
                            rs.getString("Description"),
                            rs.getInt("Price")
                    );
                    map.put(typeId, typeRoom);
                    list.add(typeRoom);
                }

                int serviceId = rs.getInt("ServiceId");
                if (serviceId != 0) {
                    Service service = new Service(
                            serviceId,
                            rs.getString("ServiceName"),
                            rs.getInt("ServicePrice")
                    );
                    RoomNService rns = new RoomNService(typeRoom, service, rs.getInt("Quantity"));
                    typeRoom.addRoomNService(rns);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public int getTypeRoomQuantity() {
        String sql = "select count(*) from TypeRoom";

        try (PreparedStatement st = con.prepareStatement(sql); ResultSet rs = st.executeQuery()) {
            while (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    public List<TypeRoom> searchTypeRoom(String key) {
        String sql = "select typeId, typeName, Description, price from TypeRoom\n"
                + "where typeName like ?";
        List<TypeRoom> list = Collections.synchronizedList(new ArrayList<>());

        try (PreparedStatement st = con.prepareStatement(sql)) {
            st.setString(1, '%' + key + '%');
            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    TypeRoom tr = new TypeRoom(rs.getInt(1),
                            rs.getString(2),
                            rs.getString(3),
                            rs.getInt(4));

                    list.add(tr);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<TypeRoom> typeRoomPagination(int index, String key) {
        List<TypeRoom> list = new ArrayList<>();

        String sql = "SELECT typeId, typeName, Description, price FROM TypeRoom \n";
        if (key != null && !key.isEmpty()) {
            sql += "WHERE typeName LIKE ? \n";
        }
        sql += "ORDER BY TypeId OFFSET ? ROWS FETCH NEXT 5 ROWS ONLY";

        try (PreparedStatement st = con.prepareStatement(sql)) {
            if (key != null && !key.isEmpty()) {
                st.setString(1, "%" + key + "%");
                st.setInt(2, (index - 1) * 5);
            } else {
                st.setInt(1, (index - 1) * 5);
            }

            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    TypeRoom tr = new TypeRoom(rs.getInt(1),
                            rs.getString(2),
                            rs.getString(3),
                            rs.getInt(4));

                    tr.setServices(RoomNServiceDAO.getInstance().getRoomNServicesByTypeId(tr));
                    tr.setOtherServices(ServiceDAO.getInstance().getServicesNotInTypeRoom(tr));
                    list.add(tr);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    public boolean updateTypeRoom(int typeId, String typeName, int price) {
        String sql = "UPDATE TypeRoom SET typeName = ?, price = ? WHERE typeId = ?";
        try (PreparedStatement st = con.prepareStatement(sql)) {
            st.setString(1, typeName);
            st.setInt(2, price);
            st.setInt(3, typeId);
            int rowsAffected = st.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public boolean updateDescription(int typeId, String description) {
        String sql = "UPDATE TypeRoom SET Description = ? WHERE typeId = ?";
        try (PreparedStatement st = con.prepareStatement(sql)) {
            st.setString(1, description);
            st.setInt(2, typeId);
            int rowsAffected = st.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public String getDescriptionByTypeId(int typeId) {
        String sql = "SELECT Description FROM TypeRoom WHERE typeId = ?";
        try (PreparedStatement st = con.prepareStatement(sql)) {
            st.setInt(1, typeId);
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("Description");
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public TypeRoom getTypeRoomByNameAndPrice(String typeName, int price) {
        String sql = "SELECT typeName, price FROM TypeRoom WHERE typeName = ? AND price = ?";
        try (PreparedStatement st = con.prepareStatement(sql)) {
            st.setString(1, typeName);
            st.setInt(2, price);
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    TypeRoom typeRoom = new TypeRoom();
                    typeRoom.setTypeName(rs.getString("typeName"));
                    typeRoom.setPrice(rs.getInt("price"));
                    return typeRoom;
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(TypeRoomDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public void deleteTypeRoom(int typeId) {
        String sql = "DELETE FROM TypeRoom WHERE typeId = ?";
        try (PreparedStatement st = con.prepareStatement(sql)) {
            st.setInt(1, typeId);
            st.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(TypeRoomDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void addTypeRoom(String typeName, String description, int price) {
        String sql = "INSERT INTO TypeRoom(typeName, Description, price) VALUES(?, ?, ?)";
        try (PreparedStatement st = con.prepareStatement(sql)) {
            st.setString(1, typeName);
            st.setString(2, description);
            st.setInt(3, price);
            st.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(TypeRoomDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public double getPriceByTypeId(int typeId) {
        double price = 0.0;

        String sql = "SELECT Price FROM TypeRoom WHERE TypeId = ?";

        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, typeId); 
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    price = rs.getDouble("Price");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return price;
    }

    public List<TypeRoom> getAvailableTypeRooms(Date startDate, Date endDate, int pageIndex, int pageSize, Integer minPrice, Integer maxPrice, int adult, int children, String orderByClause) {
        List<TypeRoom> availableTypeRooms = Collections.synchronizedList(new ArrayList<>());
        StringBuilder sql = new StringBuilder("""
                SELECT * FROM (
                    SELECT 
                        tr.TypeId,
                        tr.TypeName,
                        tr.Price + COALESCE(svc.ServicePrice, 0) AS Price,
                        tr.Description,
                        COUNT(DISTINCT CASE 
                            WHEN bd.BookingDetailId IS NULL THEN r.RoomNumber
                            END) AS AvailableRoomCount,
                        AVG(rv.Rating * 1.0) AS Rating,
                        COUNT(distinct rv.ReviewId) AS numberOfReview,
                        tr.Adult,
                        tr.Children+tr.Adult AS totalCapacity
                    FROM TypeRoom tr
                    JOIN Room r ON r.TypeId = tr.TypeId
                    LEFT JOIN BookingDetail bd 
                        ON bd.RoomNumber = r.RoomNumber
                        AND NOT (bd.EndDate < ? OR bd.StartDate > ?)
                    LEFT JOIN Cart c ON c.RoomNumber = r.RoomNumber AND c.isPayment = 1
                        AND NOT (c.EndDate < ? OR c.StartDate > ?)
                    LEFT JOIN BookingDetail bd2 ON bd2.RoomNumber = r.RoomNumber
                    LEFT JOIN Review rv ON rv.BookingDetailId = bd2.BookingDetailId
                    LEFT JOIN RoomNService rns ON tr.TypeId = rns.TypeId
                    LEFT JOIN Service s ON s.ServiceId = rns.ServiceId
                    LEFT JOIN (
                        SELECT 
                        rns.TypeId,
                        SUM(rns.Quantity * s.Price) AS ServicePrice
                        FROM RoomNService rns
                        JOIN Service s ON s.ServiceId = rns.ServiceId
                        GROUP BY rns.TypeId
                    ) svc ON svc.TypeId = tr.TypeId
                    GROUP BY tr.TypeId, tr.TypeName, tr.Price, tr.Description, svc.ServicePrice, tr.Adult, tr.Children
                ) AS sub
                WHERE sub.Adult >= ? AND sub.totalCapacity >= ?
                 """);
        List<Object> params = new ArrayList<>();
        params.add(startDate);
        params.add(endDate);
        params.add(startDate);
        params.add(endDate);
        params.add(adult);
        params.add(adult + children);
        if (minPrice != null) {
            sql.append(" AND sub.Price >= ?");
            params.add(minPrice);
        }
        if (maxPrice != null) {
            sql.append(" AND sub.Price <= ?");
            params.add(maxPrice);
        }
        sql.append(" ORDER BY ").append(orderByClause).append(" OFFSET ? ROWS FETCH NEXT ? ROWS ONLY");
        params.add((pageIndex - 1) * pageSize);
        params.add(pageSize);
        try (PreparedStatement ptm = con.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                ptm.setObject(i + 1, params.get(i));
            }
            try (ResultSet rs = ptm.executeQuery()) {
                while (rs.next()) {
                    TypeRoom typeRoom = new TypeRoom();
                    typeRoom.setTypeId(rs.getInt("TypeId"));
                    typeRoom.setTypeName(rs.getString("TypeName"));
                    typeRoom.setNumberOfAvailableRooms(rs.getInt("AvailableRoomCount"));
                    typeRoom.setPrice(rs.getInt("Price"));
                    typeRoom.setDescription(rs.getString("Description"));
                    typeRoom.setAverageRating(rs.getDouble("Rating"));
                    typeRoom.setNumberOfReviews(rs.getInt("numberOfReview"));
                    typeRoom.setImages(getRoomImagesByTypeId(typeRoom.getTypeId()));
                    availableTypeRooms.add(typeRoom);
                }
            }
        } catch (SQLException e) {
            //
        }
        return availableTypeRooms;
    }

    public List<String> getRoomImagesByTypeId(int typeId) {
        List<String> images = new ArrayList<>();
        String sql = "SELECT Image FROM RoomImage WHERE TypeId = ?";

        try (PreparedStatement ptm = con.prepareStatement(sql)) {
            ptm.setInt(1, typeId);
            try (ResultSet rs = ptm.executeQuery()) {
                while (rs.next()) {
                    images.add(rs.getString("Image"));
                }
            }
        } catch (SQLException e) {
        }
        return images;
    }

    public int getTotalTypeRoom(Date startDate, Date endDate) {
        return getTotalTypeRoom(startDate, endDate, null, null, 2, 1);
    }

    public int getTotalTypeRoom(Date startDate, Date endDate, Integer minPrice, Integer maxPrice, int adult, int children) {
        StringBuilder sql = new StringBuilder("""
                SELECT COUNT(*) AS totalTypeRoom
                FROM (
                    SELECT tr.TypeId,
                        tr.Price + COALESCE(svc.ServicePrice, 0) AS Price,
                        tr.Adult,
                        tr.Children + tr.Adult AS totalCapacity
                    FROM TypeRoom tr
                    JOIN Room r ON r.TypeId = tr.TypeId
                    LEFT JOIN BookingDetail bd 
                        ON bd.RoomNumber = r.RoomNumber
                        AND NOT (bd.EndDate < ? OR bd.StartDate > ?)
                    LEFT JOIN RoomNService rns ON tr.TypeId = rns.TypeId
                    LEFT JOIN Service s ON s.ServiceId = rns.ServiceId
                    LEFT JOIN (
                        SELECT 
                            rns.TypeId,
                            SUM(rns.Quantity * s.Price) AS ServicePrice
                        FROM RoomNService rns
                        JOIN Service s ON s.ServiceId = rns.ServiceId
                        GROUP BY rns.TypeId
                    ) svc ON svc.TypeId = tr.TypeId
                    GROUP BY tr.TypeId, tr.Price, svc.ServicePrice, tr.Adult, tr.Children
                    ) AS t
                WHERE t.Adult >= ? AND t.totalCapacity >= ?
                """);
        List<Object> params = new ArrayList<>();
        params.add(startDate);
        params.add(endDate);
        params.add(adult);
        params.add(adult + children);
        if (minPrice != null) {
            sql.append(" AND t.Price >= ?");
            params.add(minPrice);
        }
        if (maxPrice != null) {
            sql.append(" AND t.Price <= ?");
            params.add(maxPrice);
        }
        
        try (PreparedStatement ptm = con.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                ptm.setObject(i + 1, params.get(i));
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

    public TypeRoom getTypeRoomByTypeId(Date checkin, Date checkout, int typeId, String orderByClause, int offset, int limit) {
        String sql = """
                    SELECT 
                        tr.TypeId,
                        tr.TypeName,
                        tr.Price AS OriginPrice,
                        COALESCE(svc.ServicePrice, 0) AS ServicePrice,
                        tr.Price + COALESCE(svc.ServicePrice, 0) AS Price,
                        tr.Description,
                        COUNT(DISTINCT CASE 
                            WHEN bd.BookingDetailId IS NULL THEN r.RoomNumber
                        END) AS AvailableRoomCount,
                        AVG(rv.Rating * 1.0) AS Rating,
                        COUNT(rv.Rating) AS numberOfReview
                    FROM TypeRoom tr
                    JOIN Room r ON r.TypeId = tr.TypeId
                    LEFT JOIN BookingDetail bd 
                        ON bd.RoomNumber = r.RoomNumber
                        AND NOT (bd.EndDate < ? OR bd.StartDate > ?)
                    LEFT JOIN BookingDetail bd2 ON bd2.RoomNumber = r.RoomNumber
                    LEFT JOIN Cart c ON c.RoomNumber = r.RoomNumber
                        AND c.isPayment = 1
                        AND NOT (c.EndDate < ? OR c.StartDate > ?)
                    LEFT JOIN Review rv ON rv.BookingDetailId = bd2.BookingDetailId
                    LEFT JOIN (
                        SELECT 
                            rns.TypeId,
                            SUM(rns.Quantity * s.Price) AS ServicePrice
                        FROM RoomNService rns
                        JOIN Service s ON s.ServiceId = rns.ServiceId
                        GROUP BY rns.TypeId
                    ) svc ON svc.TypeId = tr.TypeId
                    WHERE tr.TypeId = ?
                    GROUP BY tr.TypeId, tr.TypeName, tr.Price, svc.ServicePrice, tr.Description;
        """;
        try (PreparedStatement ptm = con.prepareStatement(sql)) {
            ptm.setDate(1, checkin);
            ptm.setDate(2, checkout);
            ptm.setDate(3, checkin);
            ptm.setDate(4, checkout);
            ptm.setInt(5, typeId);
            try (ResultSet rs = ptm.executeQuery()) {
                if (rs.next()) {
                    TypeRoom typeRoom = new TypeRoom();
                    typeRoom.setTypeId(rs.getInt("TypeId"));
                    typeRoom.setTypeName(rs.getString("TypeName"));
                    typeRoom.setOriginPrice(rs.getInt("OriginPrice"));
                    typeRoom.setServicePrice(rs.getInt("ServicePrice"));
                    typeRoom.setPrice(rs.getInt("Price"));
                    typeRoom.setDescription(rs.getString("Description"));
                    typeRoom.setNumberOfAvailableRooms(rs.getInt("AvailableRoomCount"));
                    typeRoom.setAverageRating(rs.getDouble("Rating"));
                    typeRoom.setNumberOfReviews(rs.getInt("numberOfReview"));
                    typeRoom.setImages(getRoomImagesByTypeId(typeId));
                    typeRoom.setServices(RoomNServiceDAO.getInstance().getRoomNServicesByTypeId(typeRoom));
                    typeRoom.setReviews(ReviewDAO.getInstance().getReviewsByTypeRoomId(typeId, orderByClause, offset, limit));
                    return typeRoom;
                }
            }
        } catch (SQLException e) {
            //
        }
        return null;
    }
}