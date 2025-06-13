package controllers.admin;

import dal.CustomerAccountDAO;
import dal.EmployeeDAO;
import dal.RoleDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import models.CleanerFloor;
import models.Employee;
import models.Role;
import utility.Encryption;
import utility.Validation;

@WebServlet(name = "EmployeeController", urlPatterns = {"/admin/employees"})
public class EmployeeController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Role> roleList = RoleDAO.getInstance().getAllRoles();
        request.setAttribute("listRole", roleList);

        String key = request.getParameter("key");
        String roleId = request.getParameter("roleId");
        String status = request.getParameter("status");

        int employeeTotal;
        if (key != null && !key.trim().isEmpty()) {
            employeeTotal = EmployeeDAO.getInstance().searchEmployee(key).size();
        } else {
            employeeTotal = EmployeeDAO.getInstance().countEmployee();
        }

        int endPage = employeeTotal / 5;
        if (employeeTotal % 5 != 0) {
            endPage++;
        }

        request.setAttribute("endPage", endPage);

        int currentPage = Integer.parseInt(request.getParameter("page") == null ? "1" : request.getParameter("page"));

        List<Employee> employeeList = EmployeeDAO.getInstance().employeePagination(currentPage, key);
        request.setAttribute("currentPage", currentPage);
        request.setAttribute("listEmployee", employeeList);
        request.setAttribute("key", key);
        request.setAttribute("roleId", roleId);
        request.setAttribute("status", status);

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
                        Employee newEmp = addEmployee(request, response);
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
                        Employee updateEmp = editEmployee(request, response);
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
                    case "toggleStatus":
                        toggleEmployeeStatus(request, response);
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
            response.sendRedirect(request.getContextPath() + "/admin/employees");
        }
    }

    private Employee addEmployee(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        boolean hasError = false;
        Employee emp = new Employee();

        try {
            String username = request.getParameter("username");
            String password = request.getParameter("password");
            String fullName = request.getParameter("fullName");
            String phoneNumber = request.getParameter("phoneNumber");
            String email = request.getParameter("email");
            int roleId = Integer.parseInt(request.getParameter("roleId"));

            hasError = validateAddEmployeeInput(request, username, password, fullName, phoneNumber, email, roleId, hasError);

            if (hasError) {
                return null;
            }

            emp.setUsername(username);
            emp.setPassword(Encryption.toSHA256(password));
            emp.setFullName(fullName);
            emp.setPhoneNumber(phoneNumber);
            emp.setEmail(email);
            emp.setRegistrationDate(new Date(System.currentTimeMillis()));
            emp.setActivate(true);
            emp.setRole(RoleDAO.getInstance().getRoleById(roleId));

            String floorStr = request.getParameter("floor");
            Integer floor = null;

            if (floorStr != null && !floorStr.isEmpty()) {
                try {
                    floor = Integer.parseInt(floorStr);
                } catch (NumberFormatException e) {
                    request.setAttribute("error", "Invalid floor number.");
                    hasError = true;
                }
            }

            if (floor != null && "Cleaner".equalsIgnoreCase(emp.getRole().getRoleName())) {
                CleanerFloor cf = new CleanerFloor();
                cf.setFloor(floor);
                emp.setCleanerFloor(cf);
            }

            return emp;
        } catch (Exception e) {
            request.setAttribute("error", "An error occurred: " + e.getMessage());
            return null;
        }
    }

    private Employee editEmployee(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        boolean hasError = false;
        Employee emp = new Employee();

        try {
            String employeeIdStr = request.getParameter("employeeId");
            if (employeeIdStr == null || employeeIdStr.trim().isEmpty()) {
                request.setAttribute("error", "Employee ID is required for update.");
                hasError = true;
            } else {
                emp.setEmployeeId(Integer.parseInt(employeeIdStr));
            }

            String username = request.getParameter("username");
            String fullName = request.getParameter("fullName");
            String phoneNumber = request.getParameter("phoneNumber");
            String email = request.getParameter("email");
            int roleId = Integer.parseInt(request.getParameter("roleId"));

            hasError = validateEditEmployeeInput(request, username, fullName, phoneNumber, email, roleId, emp.getEmployeeId(), hasError);

            if (hasError) {
                return null;
            }

            emp.setUsername(username);
            emp.setFullName(fullName);
            emp.setPhoneNumber(phoneNumber);
            emp.setEmail(email);

            emp.setRole(RoleDAO.getInstance().getRoleById(roleId));

            // Handle floor for Cleaner role
            String floorStr = request.getParameter("floor");
            Integer floor = null;

            if (floorStr != null && !floorStr.isEmpty()) {
                try {
                    floor = Integer.parseInt(floorStr);
                } catch (NumberFormatException e) {
                    request.setAttribute("error", "Invalid floor number.");
                    hasError = true;
                }
            }

            if ("Cleaner".equalsIgnoreCase(emp.getRole().getRoleName())) {
                if (floor != null) {
                    CleanerFloor cleanerFloor = new CleanerFloor();
                    cleanerFloor.setFloor(floor);
                    emp.setCleanerFloor(cleanerFloor);
                } else {
                    emp.setCleanerFloor(null);
                }
            }

            return emp;
        } catch (Exception e) {
            request.setAttribute("error", "An error occurred: " + e.getMessage());
            return null;
        }
    }

    private boolean validateAddEmployeeInput(HttpServletRequest request, String username, String password,
            String fullName, String phoneNumber, String email, int roleId, boolean hasError) {
        List<String> errorMessages = new ArrayList<>();

        if (Validation.validateField(request, "usernameError", username, "USERNAME", "Username", "Tên đăng nhập không hợp lệ!")) {
            errorMessages.add("Tên đăng nhập không hợp lệ!");
        }
        if (Validation.validateField(request, "passwordError", password, "PASSWORD", "Password", "Mật khẩu không hợp lệ!")) {
            errorMessages.add("Mật khẩu không hợp lệ!");
        }
        if (Validation.validateField(request, "fullNameError", fullName, "FULLNAME", "Họ tên", "Họ tên không hợp lệ!")) {
            errorMessages.add("Họ tên không hợp lệ!");
        }
        if (Validation.validateField(request, "phoneNumberError", phoneNumber, "PHONE_NUMBER", "Số điện thoại", "Số điện thoại không hợp lệ!")) {
            errorMessages.add("Số điện thoại không hợp lệ!");
        }
        if (Validation.validateField(request, "emailError", email, "EMAIL", "Email", "Email không hợp lệ!")) {
            errorMessages.add("Email không hợp lệ!");
        }

        EmployeeDAO employeeDAO = EmployeeDAO.getInstance();
        CustomerAccountDAO customerDAO = CustomerAccountDAO.getInstance();

        if (employeeDAO.isUsernameExisted(username) || customerDAO.isUsernameExisted(username)) {
            errorMessages.add("Tên đăng nhập đã tồn tại!");
        }
        if (employeeDAO.getAllString("Email").contains(email)) {
            errorMessages.add("Email đã tồn tại!");
        }
        if (employeeDAO.getAllString("PhoneNumber").contains(phoneNumber)) {
            errorMessages.add("Số điện thoại đã tồn tại!");
        }

        Role role = RoleDAO.getInstance().getRoleById(roleId);
        if (role == null) {
            errorMessages.add("Vai trò không hợp lệ!");
        }

        if (!errorMessages.isEmpty()) {
            request.setAttribute("errorMessages", errorMessages);
            hasError = true;
        }

        return hasError;
    }

    private boolean validateEditEmployeeInput(HttpServletRequest request, String username, String fullName,
            String phoneNumber, String email, int roleId, int employeeId, boolean hasError) {
        List<String> errorMessages = new ArrayList<>();

        if (Validation.validateField(request, "usernameError", username, "USERNAME", "Username", "Tên đăng nhập không hợp lệ!")) {
            errorMessages.add("Tên đăng nhập không hợp lệ!");
        }
        if (Validation.validateField(request, "fullNameError", fullName, "FULLNAME", "Họ tên", "Họ tên không hợp lệ!")) {
            errorMessages.add("Họ tên không hợp lệ!");
        }
        if (Validation.validateField(request, "phoneNumberError", phoneNumber, "PHONE_NUMBER", "Số điện thoại", "Số điện thoại không hợp lệ!")) {
            errorMessages.add("Số điện thoại không hợp lệ!");
        }
        if (Validation.validateField(request, "emailError", email, "EMAIL", "Email", "Email không hợp lệ!")) {
            errorMessages.add("Email không hợp lệ!");
        }

        EmployeeDAO employeeDAO = EmployeeDAO.getInstance();
        CustomerAccountDAO customerDAO = CustomerAccountDAO.getInstance();

        Employee existingEmployee = employeeDAO.getEmployeeById(employeeId);
        if (!username.equals(existingEmployee.getUsername()) && (employeeDAO.isUsernameExisted(username) || customerDAO.isUsernameExisted(username))) {
            errorMessages.add("Tên đăng nhập đã tồn tại!");
        }
        if (!email.equals(existingEmployee.getEmail()) && employeeDAO.getAllString("Email").contains(email)) {
            errorMessages.add("Email đã tồn tại!");
        }
        if (!phoneNumber.equals(existingEmployee.getPhoneNumber()) && employeeDAO.getAllString("PhoneNumber").contains(phoneNumber)) {
            errorMessages.add("Số điện thoại đã tồn tại!");
        }

        Role role = RoleDAO.getInstance().getRoleById(roleId);
        if (role == null) {
            errorMessages.add("Vai trò không hợp lệ!");
        }

        if (!errorMessages.isEmpty()) {
            request.setAttribute("errorMessages", errorMessages);
            hasError = true;
        }

        return hasError;
    }

    private void toggleEmployeeStatus(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            int employeeId = Integer.parseInt(request.getParameter("employeeId"));
            Employee employee = EmployeeDAO.getInstance().getEmployeeById(employeeId);
            if (employee != null) {
                boolean currentStatus = employee.isActivate();
                employee.setActivate(!currentStatus);
                EmployeeDAO.getInstance().updateEmployeeStatus(employee.getEmployeeId(), employee.isActivate());
            }
        } catch (Exception e) {
            request.setAttribute("error", "An error occurred while updating employee status: " + e.getMessage());
            doGet(request, response);
        }
    }

}
