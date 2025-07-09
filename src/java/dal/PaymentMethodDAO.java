/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import models.PaymentMethod;

/**
 *
 * @author Hai Long
 */
public class PaymentMethodDAO {

    private static PaymentMethodDAO instance;
    private Connection con;

    public static PaymentMethodDAO getInstance() {
        if (instance == null) {
            instance = new PaymentMethodDAO();
        }
        return instance;
    }

    public PaymentMethodDAO() {
        con = new DBContext().connect;
    }

    //get paymentmethod by bookingid
    public PaymentMethod getPaymentMethodByBookingId(int bookingId) {
        String sql = "SELECT pm.paymentMethodId, pm.paymentName from PaymentMethod pm join Booking b on pm.paymentMethodId = b.paymentMethodId where b.bookingId = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, bookingId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                PaymentMethod paymentMethod = new PaymentMethod();
                paymentMethod.setPaymentMethodId(rs.getInt("PaymentMethodId"));
                paymentMethod.setPaymentName(rs.getString("PaymentName"));
                return paymentMethod;
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return null;
    }
}
