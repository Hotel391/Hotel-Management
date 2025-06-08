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
import java.util.List;
import java.util.Vector;

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
        List<Service> listService = new Vector<>();
        try {
            PreparedStatement ptm = con.prepareStatement(sql);
            ResultSet rs = ptm.executeQuery();
            while (rs.next()) {
                Service s = new Service(rs.getInt(1),
                        rs.getString(2),
                        rs.getInt(3));
                listService.add(s);
            }
        } catch (Exception e) {
        }
        return listService;
    }

    public void updateService(Service s) {
        String sql = "UPDATE [dbo].[Service]\n"
                + "   SET [ServiceName] = ?\n"
                + "      ,[Price] = ?\n"
                + " WHERE ServiceId = ?";

        try {
            PreparedStatement ptm = con.prepareStatement(sql);
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
        try {
            PreparedStatement ptm = con.prepareStatement(sql);
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

        try {
            PreparedStatement ptm = con.prepareStatement(sql);
            ptm.setInt(1, roomNumber);

            ptm.executeUpdate();
        } catch (SQLException ex) {
            ex.getStackTrace();
        }
    }

}
