package dal;

import models.Role;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RoleDAO {
    private static RoleDAO instance;
    private Connection con;

    public static RoleDAO getInstance() {
        if (instance == null) {
            instance = new RoleDAO();
        }
        return instance;
    }

    private RoleDAO() {
        con = new DBContext().connect;
    }

    public List<Role> getAllRoles() {
        List<Role> list = new ArrayList<>();
        String sql = "SELECT roleId, roleName FROM Role where RoleId not in (0, 1)";
        try (PreparedStatement stmt = con.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Role role = new Role(rs.getInt("roleId"), rs.getString("roleName"));
                list.add(role);
            }
        } catch (SQLException e) {
        }
        return list;
    }

    public Role getRoleById(int roleId) {
        if (roleId <= 0) {
            return null;
        }
        String sql = "SELECT roleId, roleName FROM Role WHERE roleId = ?";
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, roleId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Role(rs.getInt("roleId"), rs.getString("roleName"));
                }
            }
        } catch (SQLException e) {
        }
        return null;
    }

    public boolean addRole(Role role) {
        if (role == null || role.getRoleName() == null || role.getRoleName().trim().isEmpty()) {
            return false;
        }
        String sql = "INSERT INTO Role (roleName) VALUES (?)";
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, role.getRoleName().trim());
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            return false;
        }
    }

    public boolean updateRole(Role role) {
        if (role == null || role.getRoleName() == null || role.getRoleName().trim().isEmpty() || role.getRoleId() <= 0) {
            return false;
        }
        String sql = "UPDATE Role SET roleName = ? WHERE roleId = ?";
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, role.getRoleName().trim());
            stmt.setInt(2, role.getRoleId());
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            return false;
        }
    }

    public boolean deleteRole(int roleId) {
        if (roleId <= 0) {
            return false;
        }
        String checkSql = "SELECT COUNT(*) FROM Employee WHERE roleId = ?";
        try (PreparedStatement checkStmt = con.prepareStatement(checkSql)) {
            checkStmt.setInt(1, roleId);
            try (ResultSet rs = checkStmt.executeQuery()) {
                if (rs.next() && rs.getInt(1) > 0) {
                    return false; 
                }
            }
        } catch (SQLException e) {
            return false;
        }

        String sql = "DELETE FROM Role WHERE roleId = ?";
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, roleId);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            return false;
        }
    }
}