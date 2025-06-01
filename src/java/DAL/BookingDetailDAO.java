package DAL;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BookingDetailDAO {
    private static BookingDetailDAO instance;
    private Connection con;
    private BookingDetailDAO() {
        con = new DBContext().connect;
    }
    public static BookingDetailDAO getInstance() {
        if (instance == null) {
            instance = new BookingDetailDAO();
        }
        return instance;
    }
    public int CheckinCount() {
        String sql = "SELECT COUNT(*) FROM BookingDetail";
        try (PreparedStatement st = con.prepareStatement(sql); ResultSet rs = st.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
        }
        return 0;
    }
}
