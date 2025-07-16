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
                cartService.setPriceAtTime(new BigInteger(rs.getString("priceAtTime")));
                list.add(cartService);
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return list;
    }

    public boolean updatePriceStTimeOfTableCartService(CartService cartService) {
        String sql = "UPDATE CartService set priceAtTime = ? WHERE cartId = ? AND serviceId = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, cartService.getPriceAtTime().longValue());
            ps.setInt(2, cartService.getCartId());
            ps.setInt(3, cartService.getService().getServiceId());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println(e);
        }
        return false;
    }

    public List<RoomNService> selectAllRoomAndService(int typeId) {
        List<RoomNService> listRoomNService = new ArrayList<>();
        String sql = """
                       select rns.TypeId, rns.ServiceId, rns.quantity, s.ServiceName, s.Price from RoomNService rns 
                       join Service s on rns.ServiceId = s.ServiceId
                       where TypeId = ? """;
        try (PreparedStatement ptm = con.prepareStatement(sql)) {
            ptm.setInt(1, typeId);
            try (ResultSet rs = ptm.executeQuery();) {
                while (rs.next()) {
                    RoomNService roomNService = new RoomNService();
                    TypeRoom typeRoom = new TypeRoom();
                    Service service = new Service();
                    typeRoom.setTypeId(rs.getInt("TypeId"));
                    service.setServiceId(rs.getInt("ServiceId"));
                    roomNService.setQuantity(rs.getInt("quantity"));
                    service.setServiceName(rs.getString("ServiceName"));
                    service.setPrice(rs.getInt("Price"));
                    roomNService.setTypeRoom(typeRoom);
                    roomNService.setService(service);
                    listRoomNService.add(roomNService);
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listRoomNService;
    }
}
