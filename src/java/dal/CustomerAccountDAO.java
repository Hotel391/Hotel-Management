package dal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import models.Customer;
import models.CustomerAccount;
import utility.Encryption;

public class CustomerAccountDAO {

    private static CustomerAccountDAO instance;
    private Connection con;

    public static CustomerAccountDAO getInstance() {
        if (instance == null) {
            instance = new CustomerAccountDAO();
        }
        return instance;
    }

    private CustomerAccountDAO() {
        con = new DBContext().connect;
    }

    public List<String> getAllUsername() {
        List<String> listUsername = new ArrayList<>();
        String sql = "select Username from CustomerAccount";
        try (PreparedStatement st = con.prepareStatement(sql); ResultSet rs = st.executeQuery()) {
            while (rs.next()) {
                listUsername.add(rs.getString(1));
            }
        } catch (SQLException e) {
            //
        }
        return listUsername;
    }
    //create a function to check login 

    public CustomerAccount checkLogin(String username, String password) {
        String sql = "select c.Email, ca.Username, ca.Password, ca.customerId \n"
                + "from customer c join CustomerAccount ca\n"
                + "on c.CustomerId = ca.CustomerId "
                + "where (Username=? or email=?) and Password=?";
        try (PreparedStatement st = con.prepareStatement(sql)) {
            st.setString(1, username);
            st.setString(2, username);
            st.setString(3, Encryption.toSHA256(password));
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                CustomerAccount ca = new CustomerAccount();
                ca.setUsername(rs.getString("Username"));
                ca.setPassword(rs.getString("Password"));
                int customerId = rs.getInt("CustomerId");
                Customer c = CustomerDAO.getInstance().getCustomerByCustomerID(customerId);
                ca.setCustomer(c);
                return ca;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    //create function check account by email
    
    public CustomerAccount checkAccountByEmail(String email) {
        String sql = "select ca.* from CustomerAccount ca join Customer c on ca.CustomerId = c.CustomerId where c.Email=?";
        try (PreparedStatement st = con.prepareStatement(sql)) {
            st.setString(1, email);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                CustomerAccount ca = new CustomerAccount();
                ca.setUsername(rs.getString("Username"));
                ca.setPassword(rs.getString("Password"));
                int customerId = rs.getInt("CustomerId");
                Customer c = CustomerDAO.getInstance().getCustomerByCustomerID(customerId);
                ca.setCustomer(c);
                return ca;
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Consider logging the exception properly
        }
        return null;
    }


    public void insertCustomerAccount(CustomerAccount customerAccount) {
        String sql = """
                     insert into CustomerAccount (Username,Password,CustomerId)\r
                     values (?,?,?)""";
        try (PreparedStatement st = con.prepareStatement(sql)) {
            st.setString(1, customerAccount.getUsername());
            st.setString(2, customerAccount.getPassword());
            st.setInt(3, customerAccount.getCustomer().getCustomerId());
            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void resetPasswrod(String email, String password) {
        String sql = """
                   update CustomerAccount
                   set Password=?
                   where CustomerId in (select CustomerId from Customer
                   where Email=?)""";
        try (PreparedStatement st = con.prepareStatement(sql)) {
            st.setString(1, password);
            st.setString(2, email);
            st.executeUpdate();
        } catch (SQLException e) {
            //
        }
    }
    //check existed by username

    public boolean isUsernameExisted(String username) {
        String sql = "select * from CustomerAccount where Username=?";
        try (PreparedStatement st = con.prepareStatement(sql)) {
            st.setString(1, username);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
        //
    }

    public void changePassword(String password, String username) {
        String sql = """
                     UPDATE CustomerAccount
                     SET Password = ?
                     WHERE Username = ?;""";
        try (PreparedStatement ptm = con.prepareStatement(sql)) {
            ptm.setString(1, password);
            ptm.setString(2, username);
            ptm.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void changeUsername(String username, int customerId) {
        String sql = "UPDATE CustomerAccount SET Username = ? WHERE CustomerId = ?";

        try (PreparedStatement ptm = con.prepareStatement(sql)) {
            ptm.setString(1, username);
            ptm.setInt(2, customerId);
            ptm.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
