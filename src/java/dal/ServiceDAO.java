/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;


import models.Service;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import models.TypeRoom;

/**
 *
 * @author Tuan'sPC
 */
public class ServiceDAO {

    private static ServiceDAO instance;
    private Connection con;

    public static ServiceDAO getInstance() {
        if (instance == null) {
            instance = new ServiceDAO();
        }
        return instance;
    }

    private ServiceDAO() {
        con = new DBContext().connect;
    }

    public List<Service> getAllService() {

        String sql = "SELECT [ServiceId]\n"
                + "      ,[ServiceName]\n"
                + "      ,[Price]\n"
                + "  FROM [HotelManagementDB].[dbo].[Service]";
        List<Service> listService = Collections.synchronizedList(new ArrayList<>());
        try(PreparedStatement ptm = con.prepareStatement(sql)) {
            
            ResultSet rs = ptm.executeQuery();
            while (rs.next()) {
                Service s = new Service(rs.getInt(1),
                        rs.getString(2),
                        rs.getInt(3));
                listService.add(s);
            }
        } catch (SQLException e) {
        }
        return listService;
    }

    public void updateService(Service s) {
        String sql = "UPDATE [dbo].[Service]\n"
                + "   SET [ServiceName] = ?\n"
                + "      ,[Price] = ?\n"
                + " WHERE ServiceId = ?";

        try (PreparedStatement ptm = con.prepareStatement(sql);) {
            
            ptm.setString(1, s.getServiceName());
            ptm.setInt(2, s.getPrice());
            ptm.setInt(3, s.getServiceId());
            ptm.executeUpdate();
        } catch (SQLException ex) {
            ex.getStackTrace();
        }
    }

    public int insertService(String serviceName, int price) {
        String sql = "INSERT INTO [dbo].[Service]\n"
                + "            ([ServiceName]\n"
                + "            ,[Price])\n"
                + "     VALUES(?, ?)";
        int n = 0;
        try (PreparedStatement ptm = con.prepareStatement(sql);) {
            
            ptm.setString(1, serviceName);
            ptm.setInt(2, price);
            n = ptm.executeUpdate();
        } catch (SQLException ex) {
            ex.getStackTrace();
        }
        return n;
    }

    public void deleteService(int roomNumber) {
        String sql = "DELETE FROM [dbo].[Service]\n"
                + "      WHERE ServiceId=?";

        try ( PreparedStatement ptm = con.prepareStatement(sql);) {
           
            ptm.setInt(1, roomNumber);

            ptm.executeUpdate();
        } catch (SQLException ex) {
            ex.getStackTrace();
        }
    }
    
     public List<Service> getServicesNotInTypeRoom(TypeRoom typeRoom) {
        List<Service> services = new ArrayList<>();
        String sql = "SELECT * FROM Service WHERE ServiceId NOT IN (SELECT ServiceId FROM RoomNService WHERE TypeId = ?)";

        try (PreparedStatement st = con.prepareStatement(sql)) {
            st.setInt(1, typeRoom.getTypeId());
            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    Service service = new Service();
                    service.setServiceId(rs.getInt("ServiceId"));
                    service.setServiceName(rs.getString("ServiceName"));
                    service.setPrice(rs.getInt("Price"));
                    services.add(service);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return services;
    }

}
