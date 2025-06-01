/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAL;

import Models.TypeRoom;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 *
 * @author Hai Long
 */
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

    public List<TypeRoom> getAllTypeRoom() {
        List<TypeRoom> list = new Vector();
        String sql = "select typeId, typeName, Description, price from TypeRoom";
        try (PreparedStatement st = con.prepareStatement(sql); ResultSet rs = st.executeQuery()) {
            while (rs.next()) {
                TypeRoom tr = new TypeRoom(rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getDouble(4));

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
                + "where typeName like '%" + key + "%'";
        List<TypeRoom> list = new Vector<>();

        try (PreparedStatement st = con.prepareStatement(sql); ResultSet rs = st.executeQuery()) {
            while (rs.next()) {
                TypeRoom tr = new TypeRoom(rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getDouble(4));

                list.add(tr);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;

    }

    public List<TypeRoom> typeRoomPagination(int index, String key) {

        List<TypeRoom> list = new Vector<>();

        String sql = "select typeId, typeName, Description, price from TypeRoom \n";
        if(key != null && !key.isEmpty()){
            sql += "where typeName like '%" + key + "%' \n";
        }
        sql += "order by TypeId offset ? rows fetch next 5 rows only\n";

        try {
            PreparedStatement st = con.prepareStatement(sql);
            st.setInt(1, (index - 1) * 5);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                TypeRoom tr = new TypeRoom(rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getDouble(4));

                list.add(tr);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;

    }

    public static void main(String[] args) {
        List<TypeRoom> tr = TypeRoomDAO.getInstance().getAllTypeRoom();

        for (TypeRoom e : tr) {
            System.out.println("ID: " + e.getTypeId());
            System.out.println("Name: " + e.getTypeName());
            System.out.println("Price: " + e.getPrice());
            System.out.println("Description: " + e.getDescription());
            System.out.println("------------");
        }
    }
}
