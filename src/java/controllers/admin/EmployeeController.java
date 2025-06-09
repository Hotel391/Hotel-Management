package controllers.admin;

import dal.EmployeeDAO;
import dal.RoleDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.List;
import models.CleanerFloor;
import models.Employee;
import models.Role;


@WebServlet(name = "EmployeeController", urlPatterns = {"/view/admin/employees"})
public class EmployeeController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Employee> employeeList = EmployeeDAO.getInstance().getAllEmployee();
        List<Role> roleList = RoleDAO.getInstance().getAllRoles();
        
        request.setAttribute("listEmployee", employeeList);
        request.setAttribute("listRole", roleList);
        if (employeeList.isEmpty()) {
            request.setAttribute("error", "No employees found.");
        }
        request.getRequestDispatcher("/View/Admin/Employee.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        String error = null;

        try {
            switch (action) {
                case "add":
                    Employee newEmp = createEmployee(request, true);
                    if (newEmp != null) EmployeeDAO.getInstance().addEmployee(newEmp);
                    else error = "Invalid employee data.";
                    break;
                case "update":
                    Employee updateEmp = createEmployee(request, false);
                    if (updateEmp != null) EmployeeDAO.getInstance().updateEmployee(updateEmp);
                    else error = "Invalid employee data.";
                    break;
                case "delete":
                    int employeeId = Integer.parseInt(request.getParameter("employeeId"));
                    EmployeeDAO.getInstance().deleteEmployee(employeeId);
                    break;
                default:
                    error = "Invalid action.";
            }
        } catch (NumberFormatException e) {
            error = "Error: " + e.getMessage();
        }

        if (error != null) {
            request.setAttribute("error", error);
            doGet(request, response);
        } else {
            response.sendRedirect(request.getContextPath() + "/view/admin/employees");
        }
    }

    private Employee createEmployee(HttpServletRequest request, boolean isAdd) {
        try {
            Employee emp = new Employee();
            if (!isAdd) emp.setEmployeeId(Integer.parseInt(request.getParameter("employeeId")));

            String username = request.getParameter("username");
            String password = request.getParameter("password");
            String fullName = request.getParameter("fullName");
            int roleId = Integer.parseInt(request.getParameter("roleId"));
            if (username == null || username.isEmpty() || password == null || password.isEmpty() || 
                fullName == null || fullName.isEmpty()) return null;

            emp.setUsername(username);
            emp.setPassword(password);
            emp.setFullName(fullName);
            emp.setAddress(request.getParameter("address"));
            emp.setPhoneNumber(request.getParameter("phoneNumber"));
            emp.setEmail(request.getParameter("email"));
            emp.setGender(Boolean.parseBoolean(request.getParameter("gender")));
            emp.setCCCD(request.getParameter("cccd"));
            emp.setDateOfBirth(Date.valueOf(request.getParameter("dateOfBirth")));
            emp.setRegistrationDate(isAdd ? Date.valueOf(new SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date())) : 
                                            Date.valueOf(request.getParameter("registrationDate")));
            emp.setActivate(Boolean.parseBoolean(request.getParameter("activate")));

            Role role = RoleDAO.getInstance().getRoleById(roleId);
            if (role == null) return null;
            emp.setRole(role);

            Integer floor = request.getParameter("floor") != null && !request.getParameter("floor").isEmpty() ? 
                            Integer.parseInt(request.getParameter("floor")) : null;
            if (floor != null && role.getRoleName().equalsIgnoreCase("Cleaner")) {
                CleanerFloor cf = new CleanerFloor();
                cf.setFloor(floor);
                emp.setCleanerFloor(cf);
            }

            return emp;
        } catch (Exception e) {
            return null;
        }
    }
}