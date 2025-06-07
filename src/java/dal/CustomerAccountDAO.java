package dal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import models.CustomerAccount;
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
            //
        }
    }
}
