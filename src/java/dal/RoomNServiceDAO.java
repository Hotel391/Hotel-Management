package dal;

import java.sql.Connection;

public class RoomNServiceDAO {
    private static RoomNServiceDAO instance;
    private Connection con;

    private RoomNServiceDAO() {
        con = new DBContext().connect;
    }

    public static RoomNServiceDAO getInstance() {
        if (instance == null) {
            instance = new RoomNServiceDAO();
        }
        return instance;
    }

    public void updateRoomService(int roomId, int serviceId, int quantity) {
        String sql = "UPDATE RoomService SET quantity = ? WHERE roomId = ? AND serviceId = ?";
        try (var ps = con.prepareStatement(sql)) {
            ps.setInt(1, quantity);
            ps.setInt(2, roomId);
            ps.setInt(3, serviceId);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
