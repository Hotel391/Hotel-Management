/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import models.BookingDetail;
import models.DetailService;
import models.Room;
import models.RoomNService;
import models.Service;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import models.TypeRoom;
import java.util.logging.Level;
import java.util.logging.Logger;

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
                + "      ,[IsActive]\n"
                + "      ,[Price]\n"
                + "  FROM [HotelManagementDB].[dbo].[Service]";
        List<Service> listService = new Vector<>();
        try (PreparedStatement ptm = con.prepareStatement(sql); ResultSet rs = ptm.executeQuery()) {

            while (rs.next()) {
                Service s = new Service(rs.getInt(1),
                        rs.getString(2),
                        rs.getBoolean(3),
                        rs.getInt(4));
                listService.add(s);
            }
        } catch (SQLException e) {
        }
        return listService;
    }

    public void toggleServiceStatus(int serviceId) {
        String sql = "UPDATE [Service] SET IsActive = IIF(IsActive = 1, 0, 1) WHERE ServiceId = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, serviceId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
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

        try (PreparedStatement ptm = con.prepareStatement(sql);) {

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
     
     //get service by booking detail id
     
    public List<Service> getServicesByBookingDetailId(int bookingDetailId) {
        List<Service> services = new ArrayList<>();
        String sql = "SELECT s.ServiceId, s.ServiceName, s.Price FROM Service s JOIN detailService ds ON s.ServiceId = ds.ServiceId WHERE ds.BookingDetailId = ?";

        try (PreparedStatement st = con.prepareStatement(sql)) {
            st.setInt(1, bookingDetailId);
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
            Logger.getLogger(ServiceDAO.class.getName()).log(Level.SEVERE, null, e);
        }
        return services;
    }

    public List<DetailService> getServiceByBookingDetailId(int bookingDetailId) {
        String sql = """
                    select s.*,ds.quantity from BookingDetail bd 
                    join DetailService ds on ds.BookingDetailId=bd.BookingDetailId
                    join Service s on s.ServiceId=ds.ServiceId
                    where bd.BookingDetailId=?""";
        List<DetailService> list = Collections.synchronizedList(new ArrayList<>());
        try (PreparedStatement ptm = con.prepareStatement(sql);) {
            ptm.setInt(1, bookingDetailId);
            ResultSet rs = ptm.executeQuery();
            while (rs.next()) {
                DetailService ds = new DetailService();
                ds.setService(new Service(rs.getInt(1), 
                                rs.getString(2), 
                                rs.getInt(3)));
                ds.setQuantity(rs.getInt(5));
                list.add(ds);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    //get all service not in booking detail
    public List<Service> getServiceNotInBookingDetail(int bookingDetailId) {
        String sql = """
                     select s.ServiceId,s.ServiceName,s.Price from Service s
                     where IsActive=1 and NOT EXISTS(
                         select ds.ServiceId from BookingDetail bd 
                         join DetailService ds on ds.BookingDetailId=bd.BookingDetailId
                         where bd.BookingDetailId=? and s.ServiceId=ds.ServiceId)""";
        List<Service> list = Collections.synchronizedList(new ArrayList<>());
        try (PreparedStatement ptm = con.prepareStatement(sql);) {
            ptm.setInt(1, bookingDetailId);
            ResultSet rs = ptm.executeQuery();
            while (rs.next()) {
                Service s = new Service(rs.getInt(1), 
                                        rs.getString(2), 
                                        rs.getInt(3));
                list.add(s);
            }
        } catch (SQLException e) {
            //
        }
        return list;
    }
    //get all service ids from DetailService
    public List<Integer> getAllServiceIdsFromDetailService() {
        List<Integer> serviceIds = new ArrayList<>();
        String sql = "SELECT ServiceId FROM DetailService";

        try (PreparedStatement ptm = con.prepareStatement(sql); ResultSet rs = ptm.executeQuery();) {
            while (rs.next()) {
                serviceIds.add(rs.getInt("ServiceId"));
            }
        } catch (SQLException e) {
            //
        }

        return serviceIds;
    }
    //update service in booking detail
    public void updateDetailService(int bookingDetailId, int serviceId, int quantity, int oldQuantity) {
        String sql = "UPDATE DetailService SET quantity = ? WHERE BookingDetailId = ? AND ServiceId = ?";
        try (PreparedStatement ptm = con.prepareStatement(sql)) {
            ptm.setInt(1, quantity);
            ptm.setInt(2, bookingDetailId);
            ptm.setInt(3, serviceId);
            ptm.executeUpdate();
        } catch (SQLException e) {
            //
        }
        sql="""
            update BookingDetail
            set TotalAmount=TotalAmount+(?-?)*
                (select Price from Service where ServiceId=?)
            where BookingDetailId=?"""
        ;
        try (PreparedStatement ptm = con.prepareStatement(sql)) {
            ptm.setInt(1, quantity);
            ptm.setInt(2, oldQuantity);
            ptm.setInt(3, serviceId);
            ptm.setInt(4, bookingDetailId);
            ptm.executeUpdate();
        } catch (SQLException e) {
            //
        }
        sql = """
            UPDATE DetailService
            SET PriceAtTime = PriceAtTime + (? - ?) * (SELECT Price FROM Service WHERE ServiceId = ?)
            WHERE BookingDetailId = ? AND ServiceId = ?""";
        try (PreparedStatement ptm = con.prepareStatement(sql)) {
            ptm.setInt(1, quantity);
            ptm.setInt(2, oldQuantity);
            ptm.setInt(3, serviceId);
            ptm.setInt(4, bookingDetailId);
            ptm.setInt(5, serviceId);
            ptm.executeUpdate();
        } catch (SQLException e) {
            //
        }

    }
    //delete service in booking detail
    public void deleteServiceInBookingDetail(int bookingDetailId, int serviceId, int oldQuantity) {
        String sql = "DELETE FROM DetailService WHERE BookingDetailId = ? AND ServiceId = ?";
        try (PreparedStatement ptm = con.prepareStatement(sql)) {
            ptm.setInt(1, bookingDetailId);
            ptm.setInt(2, serviceId);
            ptm.executeUpdate();
        } catch (SQLException e) {
            //
        }
        sql=""" 
            update BookingDetail
            set TotalAmount=TotalAmount-? *
                (select Price from Service where ServiceId=?)
            where BookingDetailId=?""";
        try (PreparedStatement ptm = con.prepareStatement(sql)) {
            ptm.setInt(1, oldQuantity);
            ptm.setInt(2, serviceId);
            ptm.setInt(3, bookingDetailId);
            ptm.executeUpdate();
        } catch (SQLException e) {
            //
        }
    }
    //insert service in booking detail
    public void insertDetailService(int bookingDetailId, int serviceId, int quantity) {
        String sql = """
                    INSERT INTO DetailService (BookingDetailId, ServiceId, quantity,PriceAtTime) 
                        VALUES (?, ?, ?, ? * (SELECT Price FROM Service WHERE ServiceId = ?))""";
        try (PreparedStatement ptm = con.prepareStatement(sql)) {
            ptm.setInt(1, bookingDetailId);
            ptm.setInt(2, serviceId);
            ptm.setInt(3, quantity);
            ptm.setInt(4, quantity);
            ptm.setInt(5, serviceId);
            ptm.executeUpdate();
        } catch (SQLException e) {
            //
        }
        sql = """
            update BookingDetail
            set TotalAmount=TotalAmount+ ?*
                (select Price from Service where ServiceId=?)
            where BookingDetailId=?""";
        try (PreparedStatement ptm = con.prepareStatement(sql)) {
            ptm.setInt(1, quantity);
            ptm.setInt(2, serviceId);
            ptm.setInt(3, bookingDetailId);
            ptm.executeUpdate();
        } catch (SQLException e) {
            //
        }
    }
}
