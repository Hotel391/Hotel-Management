package controllers.admin;

import dal.RoleDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import models.Role;



@WebServlet(name = "Roles", urlPatterns = {"/admin/roles"})
public class Roles extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Role> roleList = RoleDAO.getInstance().getAllRoles();
        if (roleList == null || roleList.isEmpty()) {
            request.setAttribute("error", "No roles found or error retrieving roles.");
        }
        request.setAttribute("listRole", roleList);
        request.getRequestDispatcher("/View/Admin/Role.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        String error = null;

        try {
            if (action != null) {
                switch (action) {
                    case "add":
                        String roleNameAdd = request.getParameter("roleName");
                        if (roleNameAdd == null || roleNameAdd.trim().isEmpty()) {
                            error = "Role name cannot be empty.";
                            break;
                        }
                        if (roleNameAdd.trim().length() > 50) {
                            error = "Role name must be less than 50 characters.";
                            break;
                        }
                        Role newRole = new Role(0, roleNameAdd.trim());
                        if (!RoleDAO.getInstance().addRole(newRole)) {
                            error = "Failed to add role. Role name may already exist.";
                        }
                        break;
                    case "update":
                        int roleIdUpdate = Integer.parseInt(request.getParameter("roleId"));
                        String roleNameUpdate = request.getParameter("roleName");
                        if (roleNameUpdate == null || roleNameUpdate.trim().isEmpty()) {
                            error = "Role name cannot be empty.";
                            break;
                        }
                        if (roleNameUpdate.trim().length() > 50) {
                            error = "Role name must be less than 50 characters.";
                            break;
                        }
                        Role updatedRole = new Role(roleIdUpdate, roleNameUpdate.trim());
                        if (!RoleDAO.getInstance().updateRole(updatedRole)) {
                            error = "Failed to update role. Role may not exist or name may already exist.";
                        }
                        break;
                    case "delete":
                        int roleIdDelete = Integer.parseInt(request.getParameter("roleId"));
                        if (!RoleDAO.getInstance().deleteRole(roleIdDelete)) {
                            error = "Failed to delete role. Role may be in use or not exist.";
                        }
                        break;
                    default:
                        error = "Invalid action specified.";
                        break;
                }
            } else {
                error = "No action specified.";
            }
        } catch (NumberFormatException e) {
            error = "Invalid role ID format: " + e.getMessage();
        }

        if (error != null) {
            request.setAttribute("error", error);
            doGet(request, response);
        } else {
            response.sendRedirect(request.getContextPath() + "/admin/roles");
        }
    }

    @Override
    public String getServletInfo() {
        return "Servlet for managing roles in the admin panel";
    }
}