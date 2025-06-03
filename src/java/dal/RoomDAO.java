package dal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RoomDAO {
    private static RoomDAO instance;
    private Connection con;

    public static RoomDAO getInstance() {
        if (instance == null) {
            instance = new RoomDAO();
        }
        return instance;
    }

    private RoomDAO() {
        con = new DBContext().connect;
    }
    
    public int RoomCount(){
        String sql="select count(*) from Room";
        
        try(PreparedStatement st = con.prepareStatement(sql); ResultSet rs = st.executeQuery()){
            if(rs.next()){
                return rs.getInt(1);
            }
        } catch(SQLException e){
        }
        return 0;
    }

    public int RoomAvailableCount() {
        String sql = """
                     SELECT COUNT(*) \r
                     FROM Room r\r
                     WHERE NOT EXISTS (\r
                         SELECT 1 FROM BookingDetail bd \r
                     \tWHERE bd.RoomNumber = r.RoomNumber and \r
                     \tCONVERT(DATE, GETDATE()) >= bd.StartDate\r
                           and CONVERT(DATE, GETDATE()) < bd.EndDate\r
                     )\r
                     """
        ;
        
        try (PreparedStatement st = con.prepareStatement(sql); ResultSet rs = st.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
        }
        return 0;
    }
    public int RoomBookedCount() {
        String sql = """
                     SELECT COUNT(*) \r
                     FROM Room r\r
                     WHERE EXISTS (\r
                         SELECT 1 FROM BookingDetail bd \r
                     \tWHERE bd.RoomNumber = r.RoomNumber and \r
                     \tCONVERT(DATE, GETDATE()) >= bd.StartDate\r
                           and CONVERT(DATE, GETDATE()) < bd.EndDate\r
                     )\r
                     """
        ;
        
        try (PreparedStatement st = con.prepareStatement(sql); ResultSet rs = st.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
        }
        return 0;
    }
}
