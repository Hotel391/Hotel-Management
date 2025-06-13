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

    private Employee addEmployee(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        boolean hasError = false;
        List<Employee> employeeList = EmployeeDAO.getInstance().getAllEmployee();
        Employee emp = new Employee();

        try {
            String username = request.getParameter("username");
            String password = request.getParameter("password");  
            String fullName = request.getParameter("fullName");
            String phoneNumber = request.getParameter("phoneNumber");
            String email = request.getParameter("email");
            int roleId = Integer.parseInt(request.getParameter("roleId"));

            hasError = validateEmployeeInput(request, username, fullName, phoneNumber, email, roleId, hasError);

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

            Integer floor = request.getParameter("floor") != null && !request.getParameter("floor").isEmpty()
                    ? Integer.parseInt(request.getParameter("floor")) : null;
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

            hasError = validateEmployeeInput(request, username, fullName, phoneNumber, email, roleId, hasError);

            if (hasError) {
                return null; 
            }

            emp.setUsername(username);
            emp.setFullName(fullName);
            emp.setPhoneNumber(phoneNumber);
            emp.setEmail(email);

            emp.setRole(RoleDAO.getInstance().getRoleById(roleId));

            if ("Cleaner".equalsIgnoreCase(emp.getRole().getRoleName())) {
                String floorStr = request.getParameter("floor");
                if (floorStr != null && !floorStr.isEmpty()) {
                    Integer floor = Integer.parseInt(floorStr);
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

    private boolean validateEmployeeInput(HttpServletRequest request, String username, String fullName, String phoneNumber,
            String email, int roleId, boolean hasError) {

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

        String existingUsername = employeeDAO.getEmployeeById(Integer.parseInt(request.getParameter("employeeId"))).getUsername();
        if (!username.equals(existingUsername) && (employeeDAO.isUsernameExisted(username) || customerDAO.isUsernameExisted(username))) {
            errorMessages.add("Tên đăng nhập đã tồn tại!");
        }

        String existingEmail = employeeDAO.getEmployeeById(Integer.parseInt(request.getParameter("employeeId"))).getEmail();
        if (!email.equals(existingEmail) && employeeDAO.getAllString("Email").contains(email)) {
            errorMessages.add("Email đã tồn tại!");
        }

        String existingPhone = employeeDAO.getEmployeeById(Integer.parseInt(request.getParameter("employeeId"))).getPhoneNumber();
        if (!phoneNumber.equals(existingPhone) && employeeDAO.getAllString("PhoneNumber").contains(phoneNumber)) {
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

}
