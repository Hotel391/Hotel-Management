package controllers.manager;

import dal.RoleDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import models.Role;

@WebServlet(name = "ManagerRoles", urlPatterns = {"/manager/roles"})
public class Roles extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Role> roleList = RoleDAO.getInstance().getAllRoles();
        if (roleList == null || roleList.isEmpty()) {
            request.setAttribute("error", "Không tìm thấy vai trò hoặc có lỗi khi lấy vai trò.");
        }
        request.setAttribute("listRole", roleList);
        request.getRequestDispatcher("/View/Manager/Role.jsp").forward(request, response);
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
                            error = "Tên vai trò không được để trống.";
                            break;
                        }
                        if (roleNameAdd.trim().length() > 50) {
                            error = "Tên vai trò phải ít hơn 50 ký tự.";
                            break;
                        }
                        Role newRole = new Role(0, roleNameAdd.trim());
                        if (!RoleDAO.getInstance().addRole(newRole)) {
                            error = "Thêm vai trò thất bại. Tên vai trò có thể đã tồn tại.";
                        }
                        break;
                    case "update":
                        int roleIdUpdate = Integer.parseInt(request.getParameter("roleId"));
                        String roleNameUpdate = request.getParameter("roleName");
                        if (roleNameUpdate == null || roleNameUpdate.trim().isEmpty()) {
                            error = "Tên vai trò không được để trống.";
                            break;
                        }
                        if (roleNameUpdate.trim().length() > 50) {
                            error = "Tên vai trò phải ít hơn 50 ký tự.";
                            break;
                        }
                        Role updatedRole = new Role(roleIdUpdate, roleNameUpdate.trim());
                        if (!RoleDAO.getInstance().updateRole(updatedRole)) {
                            error = "Cập nhật vai trò thất bại. Vai trò có thể không tồn tại hoặc tên đã tồn tại.";
                        }
                        break;
                    case "delete":
                        int roleIdDelete = Integer.parseInt(request.getParameter("roleId"));
                        if (!RoleDAO.getInstance().deleteRole(roleIdDelete)) {
                            error = "Xóa vai trò thất bại. Vai trò có thể đang được sử dụng hoặc không tồn tại.";
                        }
                        break;
                    default:
                        error = "Hành động không hợp lệ.";
                        break;
                }
            } else {
                error = "Không có hành động được chỉ định.";
            }
        } catch (NumberFormatException e) {
            error = "Định dạng Mã vai trò không hợp lệ: " + e.getMessage();
        }

        if (error != null) {
            request.setAttribute("error", error);
            doGet(request, response);
        } else {
            response.sendRedirect(request.getContextPath() + "/manager/roles");
        }
    }
}
