/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import models.RoomNService;
import models.Service;
import models.TypeRoom;

/**
 *
 * @author Hai Long
 */
public class RoomNServiceDAO {
    
     private static RoomNServiceDAO instance;
    private Connection con;

    public static RoomNServiceDAO getInstance() {
        if (instance == null) {
            instance = new RoomNServiceDAO();
        }
        return instance;
    }

    private RoomNServiceDAO() {
        con = new DBContext().connect;
    }
    
    public List<RoomNService> getRoomNServicesByTypeId(TypeRoom tr) {
        List<RoomNService> services = new ArrayList<>();

        String sql = "SELECT s.ServiceId, s.ServiceName, s.Price, rns.Quantity "
                + "FROM RoomNService rns "
                + "JOIN Service s ON rns.ServiceId = s.ServiceId "
                + "WHERE rns.TypeId = ?";

        try (PreparedStatement st = con.prepareStatement(sql)) {
            st.setInt(1, tr.getTypeId());

            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    Service service = new Service();
                    service.setServiceId(rs.getInt("ServiceId"));
                    service.setServiceName(rs.getString("ServiceName"));
                    service.setPrice(rs.getInt("Price"));

                    RoomNService rns = new RoomNService();
                    rns.setTypeRoom(tr);
                    rns.setService(service);
                    rns.setQuantity(rs.getInt("Quantity"));

                    services.add(rns);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return services;
    }
    
    //delete room and service by typeId
    
    public void deleteRoomNServiceByTypeId(int typeId) {
        String sql = "DELETE FROM RoomNService WHERE typeId = ?";
        try (PreparedStatement st = con.prepareStatement(sql)) {
            st.setInt(1, typeId);
            st.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(TypeRoomDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    //get all serviceId by typeId
    
    public List<Integer> getAllServiceIdByTypeId(int typeId) {
        List<Integer> serviceIds = new ArrayList<>();
        String sql = "SELECT serviceId FROM RoomNService WHERE typeId = ?";
        try (PreparedStatement st = con.prepareStatement(sql)) {
            st.setInt(1, typeId);
            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    serviceIds.add(rs.getInt("serviceId"));
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(TypeRoomDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return serviceIds;
    }
    
    //insert room and service by typeId
    
    public void insertRoomNServiceByTypeId(int typeId, int serviceId) {
        String sql = "INSERT INTO RoomNService(typeId, serviceId) VALUES(?, ?)";
        try (PreparedStatement st = con.prepareStatement(sql)) {
            st.setInt(1, typeId);
            st.setInt(2, serviceId);
            st.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(TypeRoomDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
