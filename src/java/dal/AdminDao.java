package dal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Vector;
import models.Employee;
import models.Role;

/**
 *
 * @author Tuan'sPC
 */
public class AdminDao {

    private static AdminDao instance;
    private Connection con;

    public static AdminDao getInstance() {
        if (instance == null) {
            instance = new AdminDao();
        }
        return instance;
    }

    private AdminDao() {
        con = new DBContext().connect;
    }

    public List<Employee> getAllEmployee() {
        List<Employee> list = new Vector<>();
        String sql = """
                     SELECT 
                         e.EmployeeId,
                         e.Username,
                         e.Password,
                         e.FullName,
                         e.Address,
                         e.PhoneNumber,
                         e.Email,
                         e.Gender,
                         e.CCCD,
                         e.dateOfBirth,
                         e.registrationDate,
                         e.activate,
                         e.RoleId,
                         r.RoleName
                     FROM 
                         Employee e
                     JOIN 
                         Role r ON e.RoleId = r.RoleId
                     \tWHERE e.RoleId = 1;""";
        try (PreparedStatement st = con.prepareStatement(sql); ResultSet rs = st.executeQuery()) {
            while (rs.next()) {
                Role r = new Role(rs.getInt(8));
                r.setRoleId(rs.getInt(13));
                r.setRoleName(rs.getString(14));
                Employee em = new Employee(rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4),
                        rs.getString(5),
                        rs.getString(6),
                        rs.getString(7),
                        rs.getBoolean(8),
                        rs.getString(9),
                        rs.getDate(10),
                        rs.getDate(11),
                        rs.getBoolean(12),
                        r);

                list.add(em);

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<String> getAllUsernames() {
        List<String> usernames = new Vector<>();
        String sql = "SELECT Username FROM Employee";
        try (PreparedStatement st = con.prepareStatement(sql); ResultSet rs = st.executeQuery()) {

            while (rs.next()) {
                usernames.add(rs.getString("Username"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return usernames;
    }

    public void deleteManagerAccount(int employeeID) {
        String sql = "DELETE from Employee Where EmployeeId = ?";
        try (PreparedStatement st = con.prepareStatement(sql)) {
            st.setInt(1, employeeID);
            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addNewAccountManager(String userName, String password) {
        String sql = """
                     INSERT INTO [dbo].[Employee]
                                ([Username]
                                ,[Password]
                                ,[registrationDate]
                                ,[activate]
                                ,[RoleId])
                          VALUES (?, ? ,?, ?, ?)""";
        try (PreparedStatement ptm = con.prepareStatement(sql);) {

            ptm.setString(1, userName);
            ptm.setString(2, password);
            ptm.setDate(3, java.sql.Date.valueOf(LocalDate.now()));
            ptm.setBoolean(4, true);
            ptm.setInt(5, 1);
            ptm.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

}
