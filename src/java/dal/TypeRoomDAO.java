package dal;

import java.sql.Statement;
import models.TypeRoom;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import models.RoomNService;
import models.Service;

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
    
    
    public int insertTypeRoom(TypeRoom typeRoom) {
        String sql = "INSERT INTO TypeRoom(TypeName, Description, Price, Adult, Children) VALUES(?, ?, ?, ?, ?)";
        int typeId = -1; // giá trị mặc định nếu thất bại
        try (PreparedStatement st = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            st.setString(1, typeRoom.getTypeName());
            st.setString(2, typeRoom.getDescription());
            st.setInt(3, typeRoom.getPrice());
            st.setInt(4, typeRoom.getMaxAdult());
            st.setInt(5, typeRoom.getMaxChildren());
            
            st.executeUpdate();

            // Lấy khóa chính tự động sinh (typeId)
            ResultSet rs = st.getGeneratedKeys();
            if (rs.next()) {
                typeId = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return typeId;
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

    public TypeRoom getTypeRoomByNameAndId(String typeName, int typeId) {
        String sql = "SELECT TypeId, TypeName, Description, Price FROM TypeRoom WHERE TypeName = ? And TypeId != ?";
        try (PreparedStatement st = con.prepareStatement(sql)) {
            st.setString(1, typeName);
            st.setInt(2, typeId);
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
    
    //get type room by room number
    
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
                    tr.setImages(RoomImageDAO.getInstance().getRoomImagesByTypeId(rs.getInt(1)));
                               

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

        String sql = "SELECT * FROM TypeRoom \n";
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
                    TypeRoom tr = new TypeRoom(rs.getInt("typeId"),
                            rs.getString("typeName"),
                            rs.getString("description"),
                            rs.getInt("price"),
                            rs.getInt("Adult"),
                            rs.getInt("Children"));

                    tr.setServices(RoomNServiceDAO.getInstance().getRoomNServicesByTypeId(tr));
                    tr.setOtherServices(ServiceDAO.getInstance().getServicesNotInTypeRoom(tr));
                    tr.setImages(RoomImageDAO.getInstance().getRoomImagesByTypeId(rs.getInt("typeId")));
                    
                    list.add(tr);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    public boolean editTypeRoom(int typeId, String typeName, int price, int maxAdult, int maxChildren) {
        String sql = "UPDATE TypeRoom SET typeName = ?, price = ?, Adult = ?, Children = ? WHERE typeId = ? ";
        try (PreparedStatement st = con.prepareStatement(sql)) {
            st.setString(1, typeName);
            st.setInt(2, price);
            st.setInt(3, maxAdult);
            st.setInt(4, maxChildren);
            st.setInt(5, typeId);
            int rowsAffected = st.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }
    
    public boolean updateTypeRoom(int typeId, String typeName, int price) {
        String sql = "UPDATE TypeRoom SET typeName = ?, price = ? WHERE typeId = ? ";
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

    public TypeRoom getTypeRoomByNameAndPriceAndQuantity(int typeId, String typeName, int price, int maxAdult, int maxChildren) {
        String sql = "SELECT typeId, typeName, price, adult, children FROM TypeRoom WHERE typeId = ? and typeName = ? AND price = ? "
                + "AND Adult = ? AND Children = ?";
        try (PreparedStatement st = con.prepareStatement(sql)) {
            st.setInt(1, typeId);
            st.setString(2, typeName);
            st.setInt(3, price);
            st.setInt(4, maxAdult);
            st.setInt(5, maxChildren);
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    TypeRoom typeRoom = new TypeRoom();
                    typeRoom.setTypeId(rs.getInt("typeId"));
                    typeRoom.setTypeName(rs.getString("typeName"));
                    typeRoom.setPrice(rs.getInt("price"));
                    typeRoom.setMaxAdult(rs.getInt("Adult"));
                    typeRoom.setMaxChildren(rs.getInt("Children"));
                    return typeRoom;
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public void deleteTypeRoom(int typeId) {
        String sql = "DELETE FROM TypeRoom WHERE typeId = ?";
        try (PreparedStatement st = con.prepareStatement(sql)) {
            st.setInt(1, typeId);
            st.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
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
           ex.printStackTrace();
        }
    }
    
    public static void main(String[] args) {
        System.out.println(TypeRoomDAO.getInstance().typeRoomPagination(1, "").get(0).getImages());
    }
}