package dal;

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
    
    //insert type room with type name, price, desc and parameter is TypeRoom Object
    
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

    
    //get type name by name
    
    public TypeRoom getTypeRoomByName(String typeName) {
        String sql = "select TypeId, TypeName, Description, Price from TypeRoom where TypeName = ?";
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
    
    //get type room by type id

    public TypeRoom getTypeRoomById(int typeId) {
        String sql = "select TypeId, TypeName, Description, Price from TypeRoom where TypeId = ?";
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
    

//    public List<TypeRoom> getAllTypeRoom() {
//        List<TypeRoom> list = Collections.synchronizedList(new ArrayList<>());
//        String sql = "select typeId, typeName, Description, price from TypeRoom";
//        try (PreparedStatement st = con.prepareStatement(sql); ResultSet rs = st.executeQuery()) {
//            while (rs.next()) {
//                TypeRoom tr = new TypeRoom(rs.getInt(1),
//                        rs.getString(2),
//                        rs.getString(3),
//                        rs.getInt(4));
//
//                list.add(tr);
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return list;
//    }
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

                // Nếu chưa có, tạo mới và thêm vào map và list
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

                // Nếu có dịch vụ kèm theo
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

//    public List<TypeRoom> typeRoomPagination(int index, String key) {
//
//        List<TypeRoom> list = Collections.synchronizedList(new ArrayList<>());
//
//        String sql = "select typeId, typeName, Description, price from TypeRoom \n";
//        if (key != null && !key.isEmpty()) {
//            sql += "where typeName like ? \n";
//        }
//        sql += "order by TypeId offset ? rows fetch next 5 rows only\n";
//
//        try (PreparedStatement st = con.prepareStatement(sql)) {
//            if (key != null && !key.isEmpty()) {
//                st.setString(1, '%' + key + '%');
//                st.setInt(1, (index - 1) * 5);
//            } else {
//                st.setInt(1, (index - 1) * 5);
//            }
//
//            try (ResultSet rs = st.executeQuery()) {
//                while (rs.next()) {
//                    TypeRoom tr = new TypeRoom(rs.getInt(1),
//                            rs.getString(2),
//                            rs.getString(3),
//                            rs.getInt(4));
//
//                    list.add(tr);
//                }
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return list;
//
//    }
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

                    // 👉 Load danh sách dịch vụ đi kèm cho từng TypeRoom
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
    
    //get room and service not in that type room, modify from function getRoomNServicesByTypeId
    
   
   


    
    
    //create func to update price and type room name
    
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

        //function update description return boolean
    
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

    
   //get description by typeId
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
        
        //get typeroom by typename and price
        
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


    

    
    
    


}
