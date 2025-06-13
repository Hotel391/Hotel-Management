package dal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import models.EmailVerificationToken;
import utility.Email;

public class EmailVerificationTokenDAO {

    private static EmailVerificationTokenDAO instance;
    private Connection con;

    private EmailVerificationTokenDAO() {
        con = new DBContext().connect;
    }

    public static EmailVerificationTokenDAO getInstance() {
        if (instance == null) {
            instance = new EmailVerificationTokenDAO();
        }
        return instance;
    }

    public EmailVerificationToken getTokenByEmail(String email) {
        String sql = """
                select TokenId,Username, Token, ExpiryDate, IsUsed from EmailVerificationToken
                where Email=?""";
        try (PreparedStatement st = con.prepareStatement(sql)) {
            st.setString(1, email);
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    EmailVerificationToken token = new EmailVerificationToken();
                    token.setEmail(email);
                    token.setTokenId(rs.getInt(1));
                    token.setUsername(rs.getString(2));
                    token.setToken(rs.getString(3));
                    token.setExpiryDate(rs.getTimestamp(4));
                    token.setIsUsed(rs.getBoolean(5));
                    return token;
                }
            }
        } catch (SQLException e) {

        }
        return null;
    }

    public EmailVerificationToken getTokenByToken(String token) {
        String sql = """
                select TokenId,Fullname,Email,Username,Password,Gender, ExpiryDate from EmailVerificationToken
                where Token=?""";
        try (PreparedStatement st = con.prepareStatement(sql)) {
            st.setString(1, token);
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    EmailVerificationToken emailToken = new EmailVerificationToken();
                    emailToken.setTokenId(rs.getInt("TokenId"));
                    emailToken.setFullname(rs.getString("Fullname"));
                    emailToken.setEmail(rs.getString("Email"));
                    emailToken.setUsername(rs.getString("Username"));
                    emailToken.setPassword(rs.getString("Password"));
                    emailToken.setGender(rs.getBoolean("Gender"));
                    emailToken.setToken(token);
                    emailToken.setExpiryDate(rs.getTimestamp("ExpiryDate"));
                    return emailToken;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public boolean checkExistedUsername(String username){
        String sql="""
                   select TokenId, ExpiryDate from EmailVerificationToken
                   where Username COLLATE SQL_Latin1_General_CP1_CI_AS =?""";
        try(PreparedStatement st=con.prepareStatement(sql)) {
            st.setString(1, username);
            try(ResultSet rs=st.executeQuery()) {
                if(rs.next()){
                    if(Email.isExpireTime(rs.getTimestamp(2).toLocalDateTime())){
                        deleteToken(rs.getInt(1));
                        return false;
                    }
                    return true;
                }
            } 
        } catch (SQLException e) {
        }
        return false;
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
            st.setTimestamp(7, token.getExpiryDate());
            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateToken(EmailVerificationToken token) {
        String sql = """
                update EmailVerificationToken
                set ExpiryDate=?, Token=?
                where TokenId=?""";
        try (PreparedStatement st = con.prepareStatement(sql)) {
            st.setTimestamp(1, token.getExpiryDate());
            st.setString(2, token.getToken());
            st.setInt(3, token.getTokenId());
            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteToken(int tokenId) {
        String sql = "DELETE FROM EmailVerificationToken WHERE TokenId = ?";
        try (PreparedStatement st = con.prepareStatement(sql)) {
            st.setInt(1, tokenId);
            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public void deleteTokenByEmail(String email){
        String sql = "DELETE FROM EmailVerificationToken WHERE Email = ?";
        try (PreparedStatement st = con.prepareStatement(sql)) {
            st.setString(1, email);
            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
