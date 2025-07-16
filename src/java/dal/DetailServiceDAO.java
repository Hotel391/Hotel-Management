/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import java.math.BigInteger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import models.DetailService;
import models.Service;

public class DetailServiceDAO {
    
    private static DetailServiceDAO instance;
    private Connection con;

    private DetailServiceDAO() {
        con = new DBContext().connect;
    }

    public static DetailServiceDAO getInstance() {
        if (instance == null) {
            instance = new DetailServiceDAO();
        }
        return instance;
    }

    public boolean insertDetailService(int bookingDetailId, int serviceId, int quantity, int priceAtTime) {
        String sql = "INSERT INTO DetailService (BookingDetailId, ServiceId, quantity, PriceAtTime) VALUES (?, ?, ?, ?)";
        try (PreparedStatement st = con.prepareStatement(sql)) {
            st.setInt(1, bookingDetailId);
            st.setInt(2, serviceId);
            st.setInt(3, quantity);
            st.setInt(4, priceAtTime);
            return st.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Insert DetailService failed: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteDetailService(int bookingDetailId) {
        String sql = "DELETE FROM DetailService WHERE BookingDetailId = ?";
        try (PreparedStatement st = con.prepareStatement(sql)) {
            st.setInt(1, bookingDetailId);
            int rows = st.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            System.out.println("Delete DetailService failed: " + e.getMessage());
            return false;
        }
    }

    public List<DetailService> getAllDetailServiceByBookingDetailId(int bookingDetailId) {
        List<DetailService> list = new ArrayList<>();
        String sql = "SELECT * FROM DetailService WHERE BookingDetailId = ?";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, bookingDetailId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                DetailService detailService = new DetailService();
                
                detailService.setService(ServiceDAO.getInstance().getServiceByServiceId(rs.getInt("ServiceId")));
                detailService.setQuantity(rs.getInt("quantity"));
                list.add(detailService);
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return list;
    }

    public List<DetailService> getServicesByBookingDetailId(int bookingDetailId) {
        List<DetailService> list = new ArrayList<>();
        String sql = """
        SELECT ds.Quantity, ds.PriceAtTime, s.ServiceId, s.ServiceName, s.Price
        FROM DetailService ds
        JOIN Service s ON ds.ServiceId = s.ServiceId
        WHERE ds.BookingDetailId = ?
    """;

        try (PreparedStatement st = con.prepareStatement(sql)) {
            st.setInt(1, bookingDetailId);
            ResultSet rs = st.executeQuery();

            while (rs.next()) {
                DetailService detail = new DetailService();

                // Gán số lượng và giá tại thời điểm
                detail.setQuantity(rs.getInt("Quantity"));
                detail.setPriceAtTime(new BigInteger(rs.getString("PriceAtTime"))); // phải có setter trong DetailService

                // Tạo và gán Service
                Service service = new Service();
                service.setServiceId(rs.getInt("ServiceId"));
                service.setServiceName(rs.getString("ServiceName"));
                service.setPrice(rs.getInt("Price"));
                detail.setService(service);

                list.add(detail);
            }
        } catch (SQLException e) {
            System.out.println("Get DetailService failed: " + e.getMessage());
        }
        return list;
    }
}
