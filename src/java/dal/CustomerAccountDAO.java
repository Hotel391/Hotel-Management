package dal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import models.Customer;
import models.CustomerAccount;
import models.Role;
import utility.Encryption;
/**
 *
 * @author TranTrungHieu
 */
public class CustomerAccountDAO {
    private static CustomerAccountDAO instance;
    private Connection con;

    public static CustomerAccountDAO getInstance(){
        if(instance == null){
            instance= new CustomerAccountDAO();
        }
        return instance;
    }

    private CustomerAccountDAO(){
        con=new DBContext().connect;
    }

    public List<String> getAllUsername(){
        List<String> listUsername=new ArrayList<>();
        String sql="select Username from CustomerAccount";
        try(PreparedStatement st=con.prepareStatement(sql); ResultSet rs=st.executeQuery()) {
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
        String sql = "select * from CustomerAccount where Username=? and Password=?";
        try (PreparedStatement st = con.prepareStatement(sql)) {
            st.setString(1, username);
            st.setString(2, password);
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


    public void insertCustomerAccount(CustomerAccount customerAccount) {
        String sql = """
                     insert into CustomerAccount (Username,Password,CustomerId)\r
                     values (?,?,?)""" 
        ;
        try (PreparedStatement st = con.prepareStatement(sql)) {
            st.setString(1, customerAccount.getUsername());
            st.setString(2, customerAccount.getPassword());
            st.setInt(3, customerAccount.getCustomer().getCustomerId());
            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
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
    }
    
    public static void main(String[] args) {
       CustomerAccountDAO cDao = new CustomerAccountDAO();
       
        System.out.println(Encryption.toSHA256("123"));
       
       cDao.insertCustomerAccount(new CustomerAccount("tranthib@gmail.com", Encryption.toSHA256("123"), CustomerDAO.getInstance().getCustomerByCustomerID(2)));
               
    }
}
