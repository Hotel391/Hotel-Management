package dal;

import models.Employee;
import models.EmployeeAccount;
import models.Role;
import models.CleanerFloor;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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
        List<Employee> list = Collections.synchronizedList(new ArrayList<>());
        String sql = "SELECT e.*, r.RoleName, cf.StartFloor, cf.EndFloor "
                + "FROM Employee e "
                + "JOIN Role r ON r.RoleId = e.RoleId "
                + "LEFT JOIN CleanerFloor cf ON e.EmployeeId = cf.EmployeeId "
                + "WHERE r.RoleId NOT IN (0, 1)";  // Lọc bỏ các RoleId 0 và 1

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

                int startFloor = rs.getInt("StartFloor");
                int endFloor = rs.getInt("EndFloor");
                if (!rs.wasNull()) {
                    CleanerFloor cf = new CleanerFloor();
                    cf.setEmployee(e);
                    cf.setStartFloor(startFloor);
                    cf.setEndFloor(endFloor);
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
        }
        return 0;
    }

    public List<String> getAllString(String input) {
        List<String> listString = new ArrayList<>();
        String sql = "SELECT " + input + " FROM Employee";
        try (PreparedStatement st = con.prepareStatement(sql); ResultSet rs = st.executeQuery()) {
            while (rs.next()) {
                String value = rs.getString(1);
                if (value != null) {
                    listString.add(value);
                }
            }
        } catch (SQLException e) {
        }
        return listString;
    }

    public boolean isUsernameExisted(String username) {
        String sql = "select Username from Employee where Username COLLATE SQL_Latin1_General_CP1_CI_AS =?";
        try (PreparedStatement st = con.prepareStatement(sql)) {
            st.setString(1, username);
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
        //
    }

    public void updatePasswordAdminByUsername(String username, String newPassword) {
        String sql = "UPDATE Employee SET Password = ? WHERE Username COLLATE SQL_Latin1_General_CP1_CI_AS = ?";
        try (PreparedStatement ptm = con.prepareStatement(sql)) {
            ptm.setString(1, newPassword);
            ptm.setString(2, username);
            ptm.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Employee getEmployeeLogin(String username, String password) {
        String sql = """
                     select e.*, r.RoleName from Employee e
                     join Role r on r.RoleId=e.RoleId where (Username COLLATE SQL_Latin1_General_CP1_CI_AS = ? 
                     or email COLLATE SQL_Latin1_General_CP1_CI_AS = ?) and Password=?""";
        try (PreparedStatement st = con.prepareStatement(sql)) {
            st.setString(1, username);

            st.setString(2, username);

            st.setString(3, Encryption.toSHA256(password));

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

            String sqlEmployee = "INSERT INTO Employee (username, password, fullName,"
                    + " phoneNumber, email, gender, registrationDate, activate,  roleId) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement stmt = con.prepareStatement(sqlEmployee)) {
                stmt.setString(1, emp.getUsername());
                stmt.setString(2, emp.getPassword());
                stmt.setString(3, emp.getFullName());
                stmt.setString(4, emp.getPhoneNumber());
                stmt.setString(5, emp.getEmail());
                stmt.setBoolean(6, emp.isGender());
                stmt.setDate(7, emp.getRegistrationDate());
                stmt.setBoolean(8, emp.isActivate());
                stmt.setInt(9, emp.getRole().getRoleId());
                stmt.executeUpdate();
            }

            if (emp.getCleanerFloor() != null) {
                String sqlCleanerFloor = "INSERT INTO CleanerFloor (EmployeeId, StartFloor, EndFloor) VALUES (?, ?, ?)";
                try (PreparedStatement stmt = con.prepareStatement(sqlCleanerFloor)) {

                    stmt.setInt(1, emp.getEmployeeId());
                    stmt.setInt(2, emp.getCleanerFloor().getStartFloor());
                    stmt.setInt(3, emp.getCleanerFloor().getEndFloor());
                    stmt.executeUpdate();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateEmployee(Employee emp) {
        try {
            String sqlEmployee = "UPDATE Employee SET username = ?, fullName = ?,"
                    + " phoneNumber = ?, email = ?, roleId = ? WHERE employeeId = ?";

            try (PreparedStatement stmt = con.prepareStatement(sqlEmployee)) {
                stmt.setString(1, emp.getUsername());
                stmt.setString(2, emp.getFullName());
                stmt.setString(3, emp.getPhoneNumber());
                stmt.setString(4, emp.getEmail());
                stmt.setInt(5, emp.getRole().getRoleId());
                stmt.setInt(6, emp.getEmployeeId());
                stmt.executeUpdate();
            }

            if (emp.getCleanerFloor() != null) {
                try (PreparedStatement stmt = con.prepareStatement("DELETE FROM CleanerFloor "
                        + "WHERE EmployeeId = ?")) {

                    stmt.setInt(1, emp.getEmployeeId());
                    stmt.executeUpdate();
                }
                try (PreparedStatement stmt = con.prepareStatement("INSERT INTO CleanerFloor "
                        + "(EmployeeId, StartFloor, EndFloor) VALUES (?, ?, ?)")) {
                    stmt.setInt(1, emp.getEmployeeId());
                    stmt.setInt(2, emp.getCleanerFloor().getStartFloor());
                    stmt.setInt(3, emp.getCleanerFloor().getEndFloor());
                    stmt.executeUpdate();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteEmployee(int employeeId) {
        try {

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
        } catch (SQLException e) {
            e.printStackTrace();
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

    public EmployeeAccount getAccountByEmail(String email) {
        String sql = "SELECT Username, Password "
                + "FROM Employee "
                + "WHERE Email = ?";
        try (PreparedStatement st = con.prepareStatement(sql)) {
            st.setString(1, email);
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    EmployeeAccount account = new EmployeeAccount();
                    account.setUsername(rs.getString("Username"));
                    account.setPassword(rs.getString("Password"));
                    return account;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean isEmailExisted(String email, int excludeEmployeeId) {
        String sql = "SELECT 1 FROM Employee WHERE Email = ? AND EmployeeId != ?";
        try (PreparedStatement st = con.prepareStatement(sql)) {
            st.setString(1, email);
            st.setInt(2, excludeEmployeeId);
            return st.executeQuery().next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean isPhoneExisted(String phone, int excludeEmployeeId) {
        String sql = "SELECT 1 FROM Employee WHERE PhoneNumber = ? AND EmployeeId != ?";
        try (PreparedStatement st = con.prepareStatement(sql)) {
            st.setString(1, phone);
            st.setInt(2, excludeEmployeeId);
            return st.executeQuery().next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public Employee getEmployeeById(int employeeId) {
        String sql = "SELECT e.*, r.RoleName, cf.StartFloor, cf.EndFloor "
                + "FROM Employee e "
                + "JOIN Role r ON r.RoleId = e.RoleId "
                + "LEFT JOIN CleanerFloor cf ON e.EmployeeId = cf.EmployeeId "
                + "WHERE e.EmployeeId = ?";

        try (PreparedStatement st = con.prepareStatement(sql)) {
            st.setInt(1, employeeId);
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
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
                    e.setDateOfBirth(rs.getDate("DateOfBirth"));
                    e.setRegistrationDate(rs.getDate("RegistrationDate"));
                    e.setActivate(rs.getBoolean("Activate"));

                    Role r = new Role();
                    r.setRoleId(rs.getInt("RoleId"));
                    r.setRoleName(rs.getString("RoleName"));
                    e.setRole(r);

                    int startFloor = rs.getInt("StartFloor");
                    int endFloor = rs.getInt("EndFloor");
                    if (!rs.wasNull()) {
                        CleanerFloor cf = new CleanerFloor();
                        cf.setEmployee(e);
                        cf.setStartFloor(startFloor);
                        cf.setEndFloor(endFloor);
                        e.setCleanerFloor(cf);
                    }

                    return e;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void resetPassword(String email, String newPassword) {
        String sql = "UPDATE Employee SET Password = ? WHERE Email = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, newPassword);
            ps.setString(2, email);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateEmployeeStatus(int employeeId, boolean status) {
        String sql = "UPDATE Employee SET activate = ? WHERE employeeId = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, status ? 1 : 0);
            ps.setInt(2, employeeId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Employee> searchEmployee(String key) {
        String sql = "SELECT e.*, r.RoleName, cf.StartFloor, cf.EndFloor "
                + "FROM Employee e "
                + "JOIN Role r ON r.RoleId = e.RoleId "
                + "LEFT JOIN CleanerFloor cf ON e.EmployeeId = cf.EmployeeId "
                + "WHERE e.Username LIKE ? OR e.FullName LIKE ? OR e.PhoneNumber LIKE ? OR e.Email LIKE ? "
                + "ORDER BY e.EmployeeId";

        List<Employee> list = Collections.synchronizedList(new ArrayList<>());
        try (PreparedStatement st = con.prepareStatement(sql)) {
            String searchKey = "%" + key + "%";
            st.setString(1, searchKey);
            st.setString(2, searchKey);
            st.setString(3, searchKey);
            st.setString(4, searchKey);

            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    Employee e = new Employee();
                    e.setEmployeeId(rs.getInt("EmployeeId"));
                    e.setUsername(rs.getString("Username"));
                    e.setPassword(rs.getString("Password"));
                    e.setFullName(rs.getString("FullName"));
                    e.setPhoneNumber(rs.getString("PhoneNumber"));
                    e.setEmail(rs.getString("Email"));
                    e.setGender(rs.getBoolean("Gender"));
                    e.setCCCD(rs.getString("CCCD"));
                    e.setDateOfBirth(rs.getDate("dateOfBirth"));
                    e.setRegistrationDate(rs.getDate("registrationDate"));
                    e.setActivate(rs.getBoolean("activate"));

                    Role r = new Role(rs.getInt("RoleId"));
                    r.setRoleName(rs.getString("RoleName"));
                    e.setRole(r);

                    int startFloor = rs.getInt("StartFloor");
                    int endFloor = rs.getInt("EndFloor");
                    if (!rs.wasNull()) {
                        CleanerFloor cf = new CleanerFloor();
                        cf.setEmployee(e);
                        cf.setStartFloor(startFloor);
                        cf.setEndFloor(endFloor);
                        e.setCleanerFloor(cf);
                    }
                    list.add(e);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Employee> employeePagination(int index, String key) {
        List<Employee> list = Collections.synchronizedList(new ArrayList<>());
        String sql = "SELECT e.*, r.RoleName, cf.StartFloor, cf.EndFloor "
                + "FROM Employee e "
                + "JOIN Role r ON r.RoleId = e.RoleId "
                + "LEFT JOIN CleanerFloor cf ON e.EmployeeId = cf.EmployeeId "
                + "WHERE r.RoleId NOT IN (0, 1)";
        if (key != null && !key.isEmpty()) {
            sql += " AND (e.Username LIKE ? OR e.FullName LIKE ? OR e.PhoneNumber LIKE ? OR e.Email LIKE ?)";
        }

        sql += " ORDER BY e.EmployeeId OFFSET ? ROWS FETCH NEXT 5 ROWS ONLY";

        try (PreparedStatement st = con.prepareStatement(sql)) {
            int parameterIndex = 1;

            if (key != null && !key.isEmpty()) {
                String searchKey = "%" + key + "%";
                st.setString(parameterIndex++, searchKey);
                st.setString(parameterIndex++, searchKey);
                st.setString(parameterIndex++, searchKey);
                st.setString(parameterIndex++, searchKey);
            }

            st.setInt(parameterIndex++, (index - 1) * 5);
            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    Employee e = new Employee();
                    e.setEmployeeId(rs.getInt("EmployeeId"));
                    e.setUsername(rs.getString("Username"));
                    e.setPassword(rs.getString("Password"));
                    e.setFullName(rs.getString("FullName"));
                    e.setPhoneNumber(rs.getString("PhoneNumber"));
                    e.setEmail(rs.getString("Email"));
                    e.setGender(rs.getBoolean("Gender"));
                    e.setCCCD(rs.getString("CCCD"));
                    e.setDateOfBirth(rs.getDate("dateOfBirth"));
                    e.setRegistrationDate(rs.getDate("registrationDate"));
                    e.setActivate(rs.getBoolean("activate"));

                    Role r = new Role(rs.getInt("RoleId"));
                    r.setRoleName(rs.getString("RoleName"));
                    e.setRole(r);

                    int startFloor = rs.getInt("StartFloor");
                    int endFloor = rs.getInt("EndFloor");
                    if (!rs.wasNull()) {
                        CleanerFloor cf = new CleanerFloor();
                        cf.setEmployee(e);
                        cf.setStartFloor(startFloor);
                        cf.setEndFloor(endFloor);
                        e.setCleanerFloor(cf);
                    }
                    list.add(e);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public CleanerFloor getCleanerFloorByEmployeeId(int employeeId) {
        String sql = "SELECT StartFloor, EndFloor FROM CleanerFloor WHERE EmployeeId = ?";
        try (PreparedStatement st = con.prepareStatement(sql)) {
            st.setInt(1, employeeId);
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    CleanerFloor cf = new CleanerFloor();
                    cf.setStartFloor(rs.getInt("StartFloor"));
                    cf.setEndFloor(rs.getInt("EndFloor"));
                    return cf;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
