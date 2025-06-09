package dal;

import models.CleanerFloor;
import models.Employee;
import models.Role;
import models.CleanerFloor;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;
import utility.Encryption;

public class EmployeeDAO {

    private static EmployeeDAO instance;
    private Connection con;

    public static EmployeeDAO getInstance() {
        if (instance == null) {
            instance = new EmployeeDAO();
        }
        return instance;
    }

    public EmployeeDAO() {
        con = new DBContext().connect;
    }

    public List<Employee> getAllEmployee() {
        List<Employee> list = new Vector();
        String sql = "SELECT e.*, r.RoleName, cf.Floor "
                + "FROM Employee e "
                + "JOIN Role r ON r.RoleId = e.RoleId "
                + "LEFT JOIN CleanerFloor cf ON e.EmployeeId = cf.EmployeeId where r.RoleId not in (0, 1)";

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

                Role r = new Role(rs.getInt(8));
                r.setRoleId(rs.getInt("RoleId"));
                r.setRoleName(rs.getString("RoleName"));
                e.setRole(r);

                int floor = rs.getInt("Floor");
                if (!rs.wasNull()) {
                    CleanerFloor cf = new CleanerFloor();
                    cf.setEmployee(e);
                    cf.setFloor(floor);
                    e.setCleanerFloor(cf);
                }
                list.add(e);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public int countEmployee() {
        String sql = "SELECT COUNT(*) FROM Employee";
        try (PreparedStatement st = con.prepareStatement(sql); ResultSet rs = st.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            // Empty catch block
        }
        return 0;
    }

    public List<String> getAllString(String input) {
        List<String> listString = new ArrayList<>();
        List<String> allowedColumns = Arrays.asList("Username", "FullName", "PhoneNumber", "Email", "CCCD");
        if (!allowedColumns.contains(input)) {
            return listString;
        }
        String sql = "SELECT " + input + " FROM Employee";
        try (PreparedStatement st = con.prepareStatement(sql); ResultSet rs = st.executeQuery()) {
            while (rs.next()) {
                String value = rs.getString(1);
                if (value != null) {
                    listString.add(value);
                }
            }
        } catch (SQLException e) {
            // Empty catch block
        }
        return listString;
    }

    public void updatePasswordAdminByUsername(String username, String newPassword) {
        String sql = "UPDATE Employee SET Password = ? WHERE Username COLLATE SQL_Latin1_General_CP1_CS_AS = ?";
        try (PreparedStatement ptm = con.prepareStatement(sql)) {
            ptm.setString(1, newPassword);
            ptm.setString(2, username);
            ptm.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Employee getEmployeeLogin(String username, String password) {
        String sql = "select e.*, r.RoleName from Employee e\n"
                + "join Role r on r.RoleId=e.RoleId where Username=? and Password=?";
        try (PreparedStatement st = con.prepareStatement(sql)) {
            st.setString(1, username);

            st.setString(2, Encryption.toSHA256(password));

            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                Employee e = new Employee();
                e.setEmployeeId(rs.getInt("EmployeeId"));
                e.setUsername(rs.getString("Username"));
                e.setPassword(Encryption.toSHA256(password));
                e.setFullName(rs.getString("FullName"));
                e.setAddress(rs.getString("Address"));
                e.setPhoneNumber(rs.getString("PhoneNumber"));
                e.setEmail(rs.getString("Email"));
                e.setGender(rs.getBoolean("Gender"));
                e.setCCCD(rs.getString("CCCD"));
                e.setDateOfBirth(rs.getDate("dateOfBirth"));
                e.setRegistrationDate(rs.getDate("registrationDate"));
                e.setActivate(rs.getBoolean("activate"));

                Role r = new Role(rs.getInt(8));
                r.setRoleId(rs.getInt("RoleId"));
                r.setRoleName(rs.getString("RoleName"));
                e.setRole(r);
                return e;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Employee getAccountAdmin(String username) {
        String sql = "SELECT Username, Password, RoleId FROM Employee "
                + "WHERE Username COLLATE SQL_Latin1_General_CP1_CI_AS = ? AND RoleId = 0";

        try (PreparedStatement ptm = con.prepareStatement(sql)) {
            ptm.setString(1, username);
            ResultSet rs = ptm.executeQuery();
            if (rs.next()) {
                Role role = new Role();
                role.setRoleId(rs.getInt("RoleId"));
                Employee emp = new Employee();
                emp.setUsername(rs.getString("Username"));
                emp.setPassword(rs.getString("Password"));
                emp.setRole(role);
                return emp;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public void addEmployee(Employee emp) {
        try {
            con.setAutoCommit(false);
            String sqlEmployee = "INSERT INTO Employee (username, password, fullName,"
                    + " address, phoneNumber, email, gender, CCCD, dateOfBirth, "
                    + "registrationDate, activate, roleId) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement stmt = con.prepareStatement(sqlEmployee, PreparedStatement.RETURN_GENERATED_KEYS)) {
                stmt.setString(1, emp.getUsername());
                stmt.setString(2, emp.getPassword());
                stmt.setString(3, emp.getFullName());
                stmt.setString(4, emp.getAddress());
                stmt.setString(5, emp.getPhoneNumber());
                stmt.setString(6, emp.getEmail());
                stmt.setBoolean(7, emp.isGender());
                stmt.setString(8, emp.getCCCD());
                stmt.setDate(9, emp.getDateOfBirth());
                stmt.setDate(10, emp.getRegistrationDate());
                stmt.setBoolean(11, emp.isActivate());
                stmt.setInt(12, emp.getRole().getRoleId());
                stmt.executeUpdate();

                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        emp.setEmployeeId(rs.getInt(1));
                    }
                }
            }

            if (emp.getCleanerFloor() != null) {
                String sqlCleanerFloor = "INSERT INTO CleanerFloor (EmployeeId, Floor) VALUES (?, ?)";
                try (PreparedStatement stmt = con.prepareStatement(sqlCleanerFloor)) {
                    stmt.setInt(1, emp.getEmployeeId());
                    stmt.setInt(2, emp.getCleanerFloor().getFloor());
                    stmt.executeUpdate();
                }
            }

            con.commit();
        } catch (SQLException e) {
            try {
                con.rollback();
            } catch (SQLException ex) {
            }
        } finally {
            try {
                con.setAutoCommit(true);
            } catch (SQLException e) {
            }
        }
    }

    public void updateEmployee(Employee emp) {
        try {
            con.setAutoCommit(false);
            String sqlEmployee = "UPDATE Employee SET username = ?, password = ?, "
                    + "fullName = ?, address = ?, phoneNumber = ?, email = ?, gender = ?, CCCD = ?, "
                    + "dateOfBirth = ?, registrationDate = ?, activate = ?, roleId = ? WHERE employeeId = ?";
            try (PreparedStatement stmt = con.prepareStatement(sqlEmployee)) {
                stmt.setString(1, emp.getUsername());
                stmt.setString(2, emp.getPassword());
                stmt.setString(3, emp.getFullName());
                stmt.setString(4, emp.getAddress());
                stmt.setString(5, emp.getPhoneNumber());
                stmt.setString(6, emp.getEmail());
                stmt.setBoolean(7, emp.isGender());
                stmt.setString(8, emp.getCCCD());
                stmt.setDate(9, emp.getDateOfBirth());
                stmt.setDate(10, emp.getRegistrationDate());
                stmt.setBoolean(11, emp.isActivate());
                stmt.setInt(12, emp.getRole().getRoleId());
                stmt.setInt(13, emp.getEmployeeId());
                stmt.executeUpdate();
            }

            String sqlDeleteCleanerFloor = "DELETE FROM CleanerFloor WHERE EmployeeId = ?";
            try (PreparedStatement stmt = con.prepareStatement(sqlDeleteCleanerFloor)) {
                stmt.setInt(1, emp.getEmployeeId());
                stmt.executeUpdate();
            }

            if (emp.getCleanerFloor() != null) {
                String sqlInsertCleanerFloor = "INSERT INTO CleanerFloor (EmployeeId, Floor) VALUES (?, ?)";
                try (PreparedStatement stmt = con.prepareStatement(sqlInsertCleanerFloor)) {
                    stmt.setInt(1, emp.getEmployeeId());
                    stmt.setInt(2, emp.getCleanerFloor().getFloor());
                    stmt.executeUpdate();
                }
            }

            con.commit();
        } catch (SQLException e) {
            try {
                con.rollback();
            } catch (SQLException ex) {
            }
        } finally {
            try {
                con.setAutoCommit(true);
            } catch (SQLException e) {
            }
        }
    }

    public void deleteEmployee(int employeeId) {
        try {
            con.setAutoCommit(false);
            String sqlDeleteCleanerFloor = "DELETE FROM CleanerFloor WHERE EmployeeId = ?";
            try (PreparedStatement stmt = con.prepareStatement(sqlDeleteCleanerFloor)) {
                stmt.setInt(1, employeeId);
                stmt.executeUpdate();
            }

            String sqlDeleteEmployee = "DELETE FROM Employee WHERE employeeId = ?";
            try (PreparedStatement stmt = con.prepareStatement(sqlDeleteEmployee)) {
                stmt.setInt(1, employeeId);
                stmt.executeUpdate();
            }

            con.commit();
        } catch (SQLException e) {
            try {
                con.rollback();
            } catch (SQLException ex) {
            }
        } finally {
            try {
                con.setAutoCommit(true);
            } catch (SQLException e) {
            }
        }
    }

    public boolean updateEmployeeProfile(Employee employee) {
        String sql = "UPDATE Employee SET fullName = ?, address = ?, phoneNumber = ?, email = ? WHERE employeeId = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, employee.getFullName());
            ps.setString(2, employee.getAddress());
            ps.setString(3, employee.getPhoneNumber());
            ps.setString(4, employee.getEmail());
            ps.setInt(5, employee.getEmployeeId());
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean changePassword(int employeeId, String newEncryptedPassword) {
        String sql = "UPDATE Employee SET password = ? WHERE employeeId = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, newEncryptedPassword);
            ps.setInt(2, employeeId);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

}
