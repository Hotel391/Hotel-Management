package dal;

import models.TypeRoom;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

    public List<TypeRoom> getAllTypeRoom() {
        List<TypeRoom> list = Collections.synchronizedList(new ArrayList<>());
        String sql = "select typeId, typeName, Description, price from TypeRoom";
        try (PreparedStatement st = con.prepareStatement(sql); ResultSet rs = st.executeQuery()) {
            while (rs.next()) {
                TypeRoom tr = new TypeRoom(rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getInt(4));

                list.add(tr);
            }
        } catch (SQLException e) {
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
            st.setString(1, '%'+key+'%');
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

        List<TypeRoom> list = Collections.synchronizedList(new ArrayList<>());

        String sql = "select typeId, typeName, Description, price from TypeRoom \n";
        if (key != null && !key.isEmpty()) {
            sql += "where typeName like ? \n";
        }
        sql += "order by TypeId offset ? rows fetch next 5 rows only\n";

        try(PreparedStatement st = con.prepareStatement(sql)) {
            if(key != null && !key.isEmpty()){
                st.setString(1, '%'+key+'%');
                st.setInt(1, (index - 1) * 5);
            }else{
                st.setInt(1, (index - 1) * 5);
            }
            
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
}
