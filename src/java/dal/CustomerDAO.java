package dal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import models.Customer;

public class CustomerDAO {

    private static CustomerDAO instance;
    private Connection con;

    public static CustomerDAO getInstance() {
        if (instance == null) {
            instance = new CustomerDAO();
        }
        return instance;
    }

    private CustomerDAO() {
        con = new DBContext().connect;
    }

    public int customerCount() {
        String sql = "SELECT COUNT(*) FROM Customer";
        try (PreparedStatement st = con.prepareStatement(sql); ResultSet rs = st.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            //
        }
        return 0;
    }

    public List<String> getAllEmail() {
        List<String> listEmail = new ArrayList<>();
        String sql = "select Email from Customer";
        try (PreparedStatement st = con.prepareStatement(sql); ResultSet rs = st.executeQuery()) {
            while (rs.next()) {
                listEmail.add(rs.getString(1));
            }
        } catch (SQLException e) {
            //
        }
        return listEmail;
    }

    public int insertCustomer(Customer customer) {
        String sql = """
                     insert into Customer (FullName,Email,Gender,RoleId)\r
                     values (?,?,?,4)""";
        try (PreparedStatement st = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            st.setString(1, customer.getFullName());
            st.setString(2, customer.getEmail());
            st.setBoolean(3, customer.getGender());
            st.executeUpdate();

            try(ResultSet rs=st.getGeneratedKeys()){
                if(rs.next()){
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            //
        }
        return 0;
    }
}
