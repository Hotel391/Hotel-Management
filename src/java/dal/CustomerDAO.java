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
import java.util.logging.Level;
import java.util.logging.Logger;

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

    public boolean isEmailExisted(String email) {
        String sql = "SELECT 1 FROM Customer WHERE Email = ?";
        try (PreparedStatement st = con.prepareStatement(sql)) {
            st.setString(1, email);
            return st.executeQuery().next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean isPhoneExisted(String phone) {
        String sql = "SELECT 1 FROM Customer WHERE PhoneNumber = ?";
        try (PreparedStatement st = con.prepareStatement(sql)) {
            st.setString(1, phone);
            return st.executeQuery().next();
        } catch (SQLException e) {
        }
        return false;
    }

    //get customer by booking detail id
    public Customer getCustomerByBookingDetailId(int bookingDetailId) {
        String sql = "SELECT c.* FROM Customer c JOIN Booking b ON c.CustomerId = b.CustomerId JOIN BookingDetail bd ON b.BookingId = bd.BookingId WHERE bd.BookingDetailId = ?";
        try (PreparedStatement st = con.prepareStatement(sql)) {
            st.setInt(1, bookingDetailId);
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    return new Customer(
                            rs.getInt("CustomerId"),
                            rs.getString("FullName"),
                            rs.getString("PhoneNumber"),
                            rs.getString("Email"),
                            rs.getBoolean("Gender"),
                            rs.getString("CCCD"),
                            rs.getBoolean("activate"),
                            new Role(rs.getInt("RoleId"))
                    );
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(CustomerDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public void updateCustomerStatus(int customerId, boolean newStatus) {
        String sql = "UPDATE Customer SET activate = ? WHERE CustomerId = ?";

        try (PreparedStatement st = con.prepareStatement(sql)) {
            st.setBoolean(1, newStatus);
            st.setInt(2, customerId);
            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Customer getCustomerById(int customerId) {
        String sql = "SELECT * FROM Customer WHERE CustomerId = ?";
        try (PreparedStatement st = con.prepareStatement(sql)) {
            st.setInt(1, customerId);
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    Customer customer = new Customer();
                    customer.setCustomerId(rs.getInt("CustomerId"));
                    customer.setFullName(rs.getString("FullName"));
                    customer.setPhoneNumber(rs.getString("PhoneNumber"));
                    customer.setEmail(rs.getString("Email"));
                    customer.setActivate(rs.getBoolean("activate"));
                    return customer;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int countCustomer() {
        String sql = "SELECT COUNT(*) FROM Customer";
        try (PreparedStatement st = con.prepareStatement(sql); ResultSet rs = st.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public List<Customer> searchCustomer(String key) {
        List<Customer> list = new ArrayList<>();
        String sql = """
            SELECT c.*, r.RoleName, ca.Username
            FROM Customer c
            JOIN Role r ON c.RoleId = r.RoleId
            JOIN CustomerAccount ca ON c.CustomerId = ca.CustomerId
            WHERE c.FullName LIKE ? OR c.PhoneNumber LIKE ? OR c.Email LIKE ?
            ORDER BY c.CustomerId
            """;

        try (PreparedStatement st = con.prepareStatement(sql)) {
            String searchKey = "%" + key + "%";
            st.setString(1, searchKey);
            st.setString(2, searchKey);
            st.setString(3, searchKey);

            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    Customer customer = new Customer();
                    customer.setCustomerId(rs.getInt("CustomerId"));
                    customer.setFullName(rs.getString("FullName"));
                    customer.setPhoneNumber(rs.getString("PhoneNumber"));
                    customer.setEmail(rs.getString("Email"));
                    customer.setGender(rs.getBoolean("Gender"));
                    customer.setCCCD(rs.getString("CCCD"));
                    customer.setActivate(rs.getBoolean("activate"));

                    Role role = new Role(rs.getInt("RoleId"));
                    role.setRoleName(rs.getString("RoleName"));
                    customer.setRole(role);

                    CustomerAccount customerAccount = new CustomerAccount();
                    customerAccount.setUsername(rs.getString("Username"));
                    customer.setCustomerAccount(customerAccount);

                    list.add(customer);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Customer> customerPagination(int index, String key) {
        List<Customer> list = new ArrayList<>();
        String sql = """
            SELECT c.*, r.RoleName, ca.Username
            FROM Customer c
            JOIN Role r ON c.RoleId = r.RoleId
            JOIN CustomerAccount ca ON c.CustomerId = ca.CustomerId
            WHERE 1=1
            """;

        if (key != null && !key.isEmpty()) {
            sql += " AND (c.FullName LIKE ? OR c.PhoneNumber LIKE ? OR c.Email LIKE ?)";
        }

        sql += " ORDER BY c.CustomerId OFFSET ? ROWS FETCH NEXT 5 ROWS ONLY";

        try (PreparedStatement st = con.prepareStatement(sql)) {
            int parameterIndex = 1;

            if (key != null && !key.isEmpty()) {
                String searchKey = "%" + key + "%";
                st.setString(parameterIndex++, searchKey);
                st.setString(parameterIndex++, searchKey);
                st.setString(parameterIndex++, searchKey);
            }

            st.setInt(parameterIndex++, (index - 1) * 5);

            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    Customer customer = new Customer();
                    customer.setCustomerId(rs.getInt("CustomerId"));
                    customer.setFullName(rs.getString("FullName"));
                    customer.setPhoneNumber(rs.getString("PhoneNumber"));
                    customer.setEmail(rs.getString("Email"));
                    customer.setGender(rs.getBoolean("Gender"));
                    customer.setCCCD(rs.getString("CCCD"));
                    customer.setActivate(rs.getBoolean("activate"));

                    Role role = new Role(rs.getInt("RoleId"));
                    role.setRoleName(rs.getString("RoleName"));
                    customer.setRole(role);

                    CustomerAccount customerAccount = new CustomerAccount();
                    customerAccount.setUsername(rs.getString("Username"));
                    customer.setCustomerAccount(customerAccount);

                    list.add(customer);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
