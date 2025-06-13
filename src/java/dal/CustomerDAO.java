package dal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import models.Customer;
import models.CustomerAccount;
import models.Role;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

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

    public CustomerAccount getCustomerAccount(String username) {
        CustomerAccount ca = null;
        String sql = """
                 SELECT 
                     ca.Username,
                     ca.Password,
                     c.CustomerId,
                     c.FullName,
                     c.PhoneNumber,
                     c.Email,
                     c.Gender,
                     c.CCCD,
                     c.activate,
                     r.RoleId,
                     r.RoleName
                 FROM 
                     CustomerAccount ca
                 JOIN 
                     Customer c ON ca.CustomerId = c.CustomerId
                 JOIN 
                     Role r ON c.RoleId = r.RoleId
                 WHERE ca.Username = ?""";

        try (PreparedStatement st = con.prepareStatement(sql)) {
            st.setString(1, username);
            ResultSet rs = st.executeQuery();

            if (rs.next()) {
                // Create and populate Role
                Role role = new Role(rs.getInt(8));
                role.setRoleId(rs.getInt("RoleId"));
                role.setRoleName(rs.getString("RoleName"));

                // Create and populate Customer
                Customer customer = new Customer();
                customer.setCustomerId(rs.getInt("CustomerId"));
                customer.setFullName(rs.getString("FullName"));
                customer.setPhoneNumber(rs.getString("PhoneNumber"));
                customer.setEmail(rs.getString("Email"));
                customer.setGender(rs.getBoolean("Gender"));
                customer.setCCCD(rs.getString("CCCD"));
                customer.setActivate(rs.getBoolean("activate"));
                customer.setRole(role);

                // Create and populate CustomerAccount
                ca = new CustomerAccount();
                ca.setUsername(rs.getString("Username"));
                ca.setPassword(rs.getString("Password"));
                ca.setCustomer(customer);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return ca;
    }

    public void updateCustomerInfo(String username, String fullname,
            String phoneNumber, int gender) {
        String sql = "UPDATE Customer SET FullName = ?, PhoneNumber = ?, Gender = ? "
                + "WHERE CustomerId = (SELECT CustomerId FROM CustomerAccount WHERE Username = ?)";
        try (PreparedStatement ptm = con.prepareStatement(sql)) {
            ptm.setString(1, fullname);
            ptm.setString(2, phoneNumber);
            ptm.setInt(3, gender);
            ptm.setString(4, username);
            ptm.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
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

    //create function to search customer by customerID

    public Customer getCustomerByCustomerID(int customerID) {
        String sql = "select * from Customer where CustomerID = ?";
        try (PreparedStatement st = con.prepareStatement(sql);) {
            st.setInt(1, customerID);
            try (ResultSet rs = st.executeQuery();) {
                if (rs.next()) {
                    return new Customer(rs.getInt(1),
                            rs.getString(2),
                            rs.getString(3),
                            rs.getString(4),
                            rs.getBoolean(5),
                            rs.getString(6),
                            rs.getBoolean(7),
                            new Role(rs.getInt(8)));
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    //create function to check existed email
    public boolean checkExistedEmail(String email) {
        String sql = "select * from Customer where Email = ?";
        try (PreparedStatement st = con.prepareStatement(sql);) {
            st.setString(1, email);
            try (ResultSet rs = st.executeQuery();) {
                if (rs.next()) {
                    return true;
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public List<String> getAllPhone() {
        List<String> listPhone = new ArrayList<>();
        String sql = "select PhoneNumber from Customer";
        try (PreparedStatement st = con.prepareStatement(sql); ResultSet rs = st.executeQuery()) {
            while (rs.next()) {
                listPhone.add(rs.getString(1));
            }
        } catch (SQLException e) {
            //
        }
        return listPhone;
    }

    public int insertCustomer(Customer customer) {
        String sql = """
                     insert into Customer (FullName,Email,Gender,activate,RoleId)\r
                     values (?,?,?,?,4)""";
        try (PreparedStatement st = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            st.setString(1, customer.getFullName());
            st.setString(2, customer.getEmail());
            st.setBoolean(3, customer.getGender());
            st.setBoolean(4, customer.getActivate());
  
            st.executeUpdate();

            try (ResultSet rs = st.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
    public List<String> getAllString(String columnName) {
        List<String> list = new ArrayList<>();
        String sql = "SELECT " + columnName + " FROM Customer";
        try (PreparedStatement stmt = con.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                list.add(rs.getString(columnName));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    public int getCustomerIdByEmail(String email) {
        String sql = "SELECT CustomerId FROM Customer WHERE Email = ?";
        try (PreparedStatement st = con.prepareStatement(sql)) {
            st.setString(1, email);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                return rs.getInt("CustomerId");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public void updateCustomerInfo(int customerId, String fullName, boolean gender) {
        String sql = "UPDATE Customer SET FullName = ?, Gender = ? WHERE CustomerId = ?";
        try (PreparedStatement st = con.prepareStatement(sql)) {
            st.setString(1, fullName);
            st.setBoolean(2, gender);
            st.setInt(3, customerId);
            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
