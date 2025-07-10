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
import models.CartService;

/**
 *
 * @author Hai Long
 */
public class CartServiceDAO {
    private static CartServiceDAO instance;
    private Connection con;

    private CartServiceDAO() {
        con = new DBContext().connect;
    }

    public static CartServiceDAO getInstance() {
        if (instance == null) {
            instance = new CartServiceDAO();
        }
        return instance;
    }
    
    //get all cartService by cartId
    
    public List<CartService> getAllCartServiceByCartId(int cartId) {
        List<CartService> list = new ArrayList<>();
        String sql = "SELECT * FROM CartService WHERE cartId = ?";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, cartId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                CartService cartService = new CartService();
                cartService.setCartId(rs.getInt("CartId"));
                cartService.setService(ServiceDAO.getInstance().getServiceById(rs.getInt("ServiceId")));
                cartService.setQuantity(rs.getInt("quantity"));
                cartService.setPriceAtTime(rs.getInt("priceAtTime"));
                list.add(cartService);
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return list;
    }
}
