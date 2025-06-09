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
import java.util.List;
import models.CleanerFloor;
import models.Employee;
import models.Role;
import utility.Encryption;

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
            if (action == null || action.isEmpty()) {
                error = "Invalid action.";
            } else {
                switch (action) {
                    case "add":
                        Employee newEmp = createEmployee(request, true);
                        if (newEmp != null) {
                            EmployeeDAO.getInstance().addEmployee(newEmp);
                        } else {
                            error = (String) request.getAttribute("error");
                            if (error == null) {
                                error = "Invalid employee data.";
                            }
                        }
                        break;
                    case "update":
                        Employee updateEmp = createEmployee(request, false);
                        if (updateEmp != null) {
                            EmployeeDAO.getInstance().updateEmployee(updateEmp);
                        } else {
                            error = "Invalid employee data.";
                        }
                        break;
                    case "delete":
                        int employeeId = Integer.parseInt(request.getParameter("employeeId"));
                        EmployeeDAO.getInstance().deleteEmployee(employeeId);
                        break;
                    default:
                        error = "Invalid action.";
                }
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
            String phoneNumber = request.getParameter("phoneNumber");
            String email = request.getParameter("email");
            int roleId = Integer.parseInt(request.getParameter("roleId"));
            
            if (username == null || username.isEmpty() || password == null || password.isEmpty() ||
                fullName == null || fullName.isEmpty() || phoneNumber == null || phoneNumber.isEmpty() ||
                email == null || email.isEmpty()) {
                return null;
            }
            Role role = RoleDAO.getInstance().getRoleById(roleId);
            if (role == null) {
                return null;
            }

            String roleName = role.getRoleName();
            if (!"Receptionist".equalsIgnoreCase(roleName) && !"Cleaner".equalsIgnoreCase(roleName)) {
                return null;
            }

            EmployeeDAO employeeDAO = EmployeeDAO.getInstance();
            if (employeeDAO.getAllString("Username").contains(username)) {
                request.setAttribute("error", "Username already exists.");
                return null;
            }
            if (employeeDAO.getAllString("Email").contains(email)) {
                request.setAttribute("error", "Email already exists.");
                return null;
            }
            if (employeeDAO.getAllString("PhoneNumber").contains(phoneNumber)) {
                request.setAttribute("error", "Phone number already exists.");
                return null;
            }

            emp.setUsername(username);
            emp.setPassword(Encryption.toSHA256(password));
            emp.setFullName(fullName);
            emp.setPhoneNumber(phoneNumber);
            emp.setEmail(email);
            emp.setRegistrationDate(new Date(System.currentTimeMillis()));
            emp.setActivate(true);
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