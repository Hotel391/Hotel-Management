package dal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import models.EmailVerificationToken;

public class EmailVerificationTokenDAO {
    private static EmailVerificationTokenDAO instance;
    private Connection con;

    private EmailVerificationTokenDAO() {
        con= new DBContext().connect;
    }

    public static EmailVerificationTokenDAO getInstance() {
        if (instance == null) {
            instance=new EmailVerificationTokenDAO();
        }
        return instance;
    }

    public void insertToken(EmailVerificationToken token) {
        String sql = """
                     insert into EmailVerificationToken(Fullname,Email,Username,Password,Gender,Token,ExpiryDate)\r
                     values(?,?,?,?,?,?,?)""";
        try (PreparedStatement st = con.prepareStatement(sql)) {
            st.setString(1, token.getFullname());
            st.setString(2, token.getEmail());
            st.setString(3, token.getUsername());
            st.setString(4, token.getPassword());
            st.setBoolean(5, token.getGender());
            st.setString(6, token.getToken());
            st.setDate(7, token.getExpiryDate());
            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
