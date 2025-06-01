package DAL;

import Models.Employee;
import Models.Role;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EmployeeDAO {

    private static EmployeeDAO instance;
    private Connection con;

    public static EmployeeDAO getInstance() {
        if (instance == null) {
            instance = new EmployeeDAO();
        }
        return instance;
    }

    private EmployeeDAO() {
        con = new DBContext().connect;
    }

    public List<Employee> getAllEmployee() {
        List<Employee> list = new ArrayList();
        String sql = "select e.*, r.RoleName from Employee e\n"
                + "join Role r on r.RoleId=e.RoleId";
        try (PreparedStatement st = con.prepareStatement(sql); ResultSet rs = st.executeQuery()) {
            while (rs.next()) {
                Employee e = new Employee();
                e.setEmployeeId(rs.getInt("EmployeeId"));
                e.setUsername(rs.getString("Username"));
                e.setPassword(rs.getString("Password"));
                e.setFullName(rs.getString("FullName"));
                e.setAddress(rs.getString("Address"));
                e.setPhoneNumber(rs.getString("PhoneNumber"));
                e.setEmail(rs.getString("Email"));
                e.setGender(rs.getBoolean("Gender"));
                e.setCCCD(rs.getString("CCCD"));
                e.setDateOfBirth(rs.getDate("dateOfBirth"));
                e.setRegistrationDate(rs.getDate("registrationDate"));
                e.setActivate(rs.getBoolean("activate"));

                Role r = new Role();
                r.setRoleId(rs.getInt("RoleId"));
                r.setRoleName(rs.getString("RoleName"));
                e.setRole(r);
                list.add(e);
            }
        } catch (SQLException e) {
        }
        return list;
    }
    public int countEmployee() {
        String sql = "select count(*) from Employee";
        try (PreparedStatement st = con.prepareStatement(sql); ResultSet rs = st.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
        }
        return 0;
    }
}
