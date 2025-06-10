package controllers.admin;

import dal.CustomerAccountDAO;
import dal.EmployeeDAO;
import dal.RoleDAO;
import dal.CustomerDAO;
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
import utility.Validation;

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
                        Employee newEmp = createEmployee(request, response, true);
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
                        Employee updateEmp = createEmployee(request, response, false);
                        if (updateEmp != null) {
                            EmployeeDAO.getInstance().updateEmployee(updateEmp);
                        } else {
                            error = (String) request.getAttribute("error");
                            if (error == null) {
                                error = "Invalid employee data.";
                            }
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

    private Employee createEmployee(HttpServletRequest request, HttpServletResponse response, boolean isAdd)
            throws ServletException, IOException {
        boolean hasError = false;
        try {
            Employee emp = new Employee();
            if (!isAdd) {
                String employeeIdStr = request.getParameter("employeeId");
                if (employeeIdStr == null || employeeIdStr.trim().isEmpty()) {
                    request.setAttribute("error", "Employee ID is required for update.");
                    hasError = true;
                } else {
                    emp.setEmployeeId(Integer.parseInt(employeeIdStr));
                }
            }

            String username = request.getParameter("username");
            String password = request.getParameter("password");
            String fullName = request.getParameter("fullName");
            String phoneNumber = request.getParameter("phoneNumber");
            String email = request.getParameter("email");
            int roleId = Integer.parseInt(request.getParameter("roleId"));

            if (Validation.validateField(request, "usernameError", username, "USERNAME", "Username", "Tên đăng nhập không hợp lệ!")) {
                hasError = true;
            }
            if (Validation.validateField(request, "passwordError", password, "PASSWORD", "Password", "Mật khẩu không hợp lệ!")) {
                hasError = true;
            }
            if (Validation.validateField(request, "fullNameError", fullName, "FULLNAME", "Họ tên", "Họ tên không hợp lệ!")) {
                hasError = true;
            }
            if (Validation.validateField(request, "phoneNumberError", phoneNumber, "PHONE_NUMBER", "Số điện thoại", "Số điện thoại không hợp lệ!")) {
                hasError = true;
            }
            if (Validation.validateField(request, "emailError", email, "EMAIL", "Email", "Email không hợp lệ!")) {
                hasError = true;
            }

            EmployeeDAO employeeDAO = EmployeeDAO.getInstance();

            if (!hasError) {
                if (employeeDAO.isUsernameExisted(username) || CustomerAccountDAO.getInstance().isUsernameExisted(username)) {
                    request.setAttribute("usernameError", "Tên đăng nhập đã tồn tại!");
                    hasError = true;
                }
                if (employeeDAO.getAllString("Email").contains(email)) {
                    request.setAttribute("emailError", "Email đã tồn tại!");
                    hasError = true;
                }
                if (employeeDAO.getAllString("PhoneNumber").contains(phoneNumber)) {
                    request.setAttribute("phoneNumberError", "Số điện thoại đã tồn tại!");
                    hasError = true;
                }
            }

            Role role = RoleDAO.getInstance().getRoleById(roleId);
            if (!hasError && role == null) {
                request.setAttribute("error", "Invalid role selected.");
                hasError = true;
            } else if (!hasError) {
                String roleName = role.getRoleName();
                if (!"Receptionist".equalsIgnoreCase(roleName) && !"Cleaner".equalsIgnoreCase(roleName)) {
                    request.setAttribute("error", "Only Receptionist and Cleaner roles are allowed.");
                    hasError = true;
                }
            }

            if (hasError) {
                request.setAttribute("listRole", RoleDAO.getInstance().getAllRoles());
                request.setAttribute("username", username);
                request.setAttribute("fullName", fullName);
                request.setAttribute("phoneNumber", phoneNumber);
                request.setAttribute("email", email);
                request.setAttribute("roleId", roleId);
                //request.getRequestDispatcher("/View/Admin/Employee.jsp").forward(request, response);
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

            Integer floor = request.getParameter("floor") != null && !request.getParameter("floor").isEmpty()
                    ? Integer.parseInt(request.getParameter("floor")) : null;
            if (floor != null && role.getRoleName().equalsIgnoreCase("Cleaner")) {
                CleanerFloor cf = new CleanerFloor();
                cf.setFloor(floor);
                emp.setCleanerFloor(cf);
            }

            return emp;
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Invalid role ID or employee ID.");
            request.setAttribute("listRole", RoleDAO.getInstance().getAllRoles());
            request.getRequestDispatcher("/View/Admin/Employee.jsp").forward(request, response);
            return null;
        } catch (Exception e) {
            request.setAttribute("error", "An error occurred: " + e.getMessage());
            request.setAttribute("listRole", RoleDAO.getInstance().getAllRoles());
            request.getRequestDispatcher("/View/Admin/Employee.jsp").forward(request, response);
            return null;
        }
    }
}
