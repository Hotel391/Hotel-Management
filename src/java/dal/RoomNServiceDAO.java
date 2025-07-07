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

public class RoomNServiceDAO {
    private static RoomNServiceDAO instance;
    private Connection con;

    private RoomNServiceDAO() {
        con = new DBContext().connect;
    }

    public static RoomNServiceDAO getInstance() {
        if (instance == null) {
            instance = new RoomNServiceDAO();
        }
        return instance;
    }

    public void updateRoomService(int roomId, int serviceId, int quantity) {
        String sql = "UPDATE RoomService SET quantity = ? WHERE roomId = ? AND serviceId = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, quantity);
            ps.setInt(2, roomId);
            ps.setInt(3, serviceId);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    public void deleteRoomNServiceByTypeId(int typeId) {
        String sql = "DELETE FROM RoomNService WHERE typeId = ?";
        try (PreparedStatement st = con.prepareStatement(sql)) {
            st.setInt(1, typeId);
            st.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(TypeRoomDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

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
    
   public List<RoomNService> getServicesByRoomNumber(String roomNumber) {
        List<RoomNService> roomNServices = new ArrayList<>();
        String sql = "SELECT * FROM RoomNService WHERE roomNumber = ?";
        try (PreparedStatement st = con.prepareStatement(sql)) {
            st.setString(1, roomNumber);
            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    int typeId = rs.getInt("typeId");
                    int serviceId = rs.getInt("serviceId");
                    int quantity = rs.getInt("quantity");

                    TypeRoom typeRoom = TypeRoomDAO.getInstance().getTypeRoomById(typeId);
                    Service service = ServiceDAO.getInstance().getServiceById(serviceId);

                    RoomNService roomNService = new RoomNService(typeRoom, service, quantity);
                    roomNServices.add(roomNService);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return roomNServices;
    }

}


