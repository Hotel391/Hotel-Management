/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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

}
