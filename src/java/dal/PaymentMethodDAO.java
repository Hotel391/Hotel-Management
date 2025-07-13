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

    public PaymentMethod getPaymentMethodCheckInByBookingId(int bookingId) {
        String sql = "SELECT b.PaymentMethodIdCheckIn, pm.paymentName from PaymentMethod pm join Booking b on pm.PaymentMethodId = b.PaymentMethodIdCheckIn where b.bookingId = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, bookingId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                PaymentMethod paymentMethod = new PaymentMethod();
                paymentMethod.setPaymentMethodId(rs.getInt("PaymentMethodIdCheckIn"));
                paymentMethod.setPaymentName(rs.getString("PaymentName"));
                return paymentMethod;
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return null;
    }

    public PaymentMethod getPaymentInfoByBookingId(int bookingId) {
        String sql = "SELECT b.PaymentMethodIdCheckIn, pm.PaymentName "
                + "FROM Booking b "
                + "JOIN PaymentMethod pm ON b.PaymentMethodIdCheckIn = pm.PaymentMethodId "
                + "WHERE b.BookingId = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, bookingId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int paymentMethodIdCheckIn = rs.getInt("PaymentMethodIdCheckIn");
                String paymentName = rs.getString("PaymentName");
                return new PaymentMethod(paymentMethodIdCheckIn, paymentName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public PaymentMethod getPaymentMethodByPaymentMethodId(int paymentMethodId) {
        String sql = "select * from PaymentMethod where paymentMethodId = ?";
        try (PreparedStatement ptm = con.prepareStatement(sql)) {
            ptm.setInt(1, paymentMethodId);
            try (ResultSet rs = ptm.executeQuery()) {
                if (rs.next()) {
                    PaymentMethod paymentMethod = new PaymentMethod();
                    paymentMethod.setPaymentMethodId(rs.getInt(1));
                    paymentMethod.setPaymentName(rs.getString(2));
                    return paymentMethod;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
