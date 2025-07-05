/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import models.DetailService;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import models.DetailService;

/**
 *
 * @author Hai Long
 */
public class DetailServiceDAO {
    private static DetailServiceDAO instance;
    private Connection con;

    public static DetailServiceDAO getInstance() {
        if (instance == null) {
            instance = new DetailServiceDAO();
        }
        return instance;
    }

    private DetailServiceDAO() {
        con = new DBContext().connect;
    }
    
    //get all detail service by booking detail id
    
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
}
