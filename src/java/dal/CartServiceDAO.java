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
import models.RoomNService;
import models.Service;
import models.TypeRoom;

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

    public boolean updatePriceStTimeOfTableCartService(CartService cartService) {
        String sql = "UPDATE CartService set priceAtTime = ? WHERE cartId = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, cartService.getPriceAtTime());
            ps.setInt(2, cartService.getCartId());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println(e);
        }
        return false;
    }

    public RoomNService selectAllRoomAndService(int typeId, int serviceId) {

        String sql = "select * from RoomNService where TypeId =? and ServiceId =?";
        try (PreparedStatement ptm = con.prepareStatement(sql)) {
            ptm.setInt(1, typeId);
            ptm.setInt(2, serviceId);
            try (ResultSet rs = ptm.executeQuery();) {
                while (rs.next()) {
                    RoomNService room = new RoomNService();
                    TypeRoom typeRoom = new TypeRoom();
                    typeRoom.setTypeId(rs.getInt("TypeId"));
                    room.setTypeRoom(typeRoom);
                    Service service = new Service();
                    service.setServiceId(rs.getInt("ServiceId"));
                    room.setService(service);
                    room.setQuantity(rs.getInt("quantity"));
                    return room;
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
