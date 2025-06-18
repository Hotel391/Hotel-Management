package controllers.manager;

import dal.CustomerAccountDAO;
import dal.EmployeeDAO;
import dal.RoleDAO;
<<<<<<< HEAD
=======
import dal.CustomerDAO;
>>>>>>> origin/HaiLong25
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Date;
<<<<<<< HEAD
import java.util.ArrayList;
=======
>>>>>>> origin/HaiLong25
import java.util.List;
import models.CleanerFloor;
import models.Employee;
import models.Role;
import utility.Encryption;
import utility.Validation;

<<<<<<< HEAD
@WebServlet(name = "EmployeeController", urlPatterns = {"/manager/employees"})
=======
@WebServlet(name = "EmployeeController", urlPatterns = {"/view/admin/employees"})
>>>>>>> origin/HaiLong25
public class EmployeeController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
<<<<<<< HEAD
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

        request.getRequestDispatcher("/View/Manager/Employee.jsp").forward(request, response);
=======
        List<Employee> employeeList = EmployeeDAO.getInstance().getAllEmployee();
        List<Role> roleList = RoleDAO.getInstance().getAllRoles();
        
        request.setAttribute("listEmployee", employeeList);
        request.setAttribute("listRole", roleList);
        if (employeeList.isEmpty()) {
            request.setAttribute("error", "No employees found.");
        }
        request.getRequestDispatcher("/View/Admin/Employee.jsp").forward(request, response);
>>>>>>> origin/HaiLong25
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
<<<<<<< HEAD
                        Employee newEmp = addEmployee(request, response);
=======
                        Employee newEmp = createEmployee(request, response, true);
>>>>>>> origin/HaiLong25
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
<<<<<<< HEAD
                        Employee updateEmp = editEmployee(request, response);
=======
                        Employee updateEmp = createEmployee(request, response, false);
>>>>>>> origin/HaiLong25
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
<<<<<<< HEAD
                    case "toggleStatus":
                        toggleEmployeeStatus(request, response);
                        break;
=======
>>>>>>> origin/HaiLong25
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
<<<<<<< HEAD
            response.sendRedirect(request.getContextPath() + "/Manager/employees");
        }
    }

    private Employee addEmployee(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        boolean hasError = false;
        Employee emp = new Employee();

        try {
=======
            response.sendRedirect(request.getContextPath() + "/view/admin/employees");
        }
    }

    private Employee createEmployee(HttpServletRequest request, HttpServletResponse response, boolean isAdd)
            throws ServletException, IOException {
        boolean hasError = false;
        List<Employee> employeeList = EmployeeDAO.getInstance().getAllEmployee();
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

>>>>>>> origin/HaiLong25
            String username = request.getParameter("username");
            String password = request.getParameter("password");
            String fullName = request.getParameter("fullName");
            String phoneNumber = request.getParameter("phoneNumber");
            String email = request.getParameter("email");
            int roleId = Integer.parseInt(request.getParameter("roleId"));
<<<<<<< HEAD

            request.setAttribute("username", username);
            request.setAttribute("password", password);
            request.setAttribute("fullName", fullName);
            request.setAttribute("phoneNumber", phoneNumber);
            request.setAttribute("email", email);
            request.setAttribute("roleId", roleId);

            hasError = validateAddEmployeeInput(request, username, password, fullName, phoneNumber, email, roleId, hasError);

            if (hasError) {
                return null;
=======
            
            System.out.println("username: " + username);
            
            System.out.println("password: " + password);
            
            System.out.println("fullName: " + fullName);
            
            System.out.println("phoneNumber: " + phoneNumber);
            
            System.out.println("email: " + email);
            
            System.out.println("roleId: " + roleId);

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
            CustomerAccountDAO customerDAO = CustomerAccountDAO.getInstance();

            
            
            if (!hasError) {
                if (employeeDAO.isUsernameExisted(username) || customerDAO.isUsernameExisted(username)) {
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
            
            System.out.println("role: " + role.getRoleName());
            
            if (!hasError && role == null) {
                request.setAttribute("error", "Invalid role selected.");
                hasError = true;
            } else if (!hasError) {
                String roleName = role.getRoleName();
                if (!"Receptionist".equalsIgnoreCase(roleName) && !"Cleaner".equalsIgnoreCase(roleName)) {
                    request.setAttribute("error", "Only Receptionist and Cleaner roles are allowed.");
                    hasError = true;
                }
                System.out.println(hasError);
            }
            
            if (hasError) {
                request.setAttribute("listRole", RoleDAO.getInstance().getAllRoles());
                request.setAttribute("username", username);
                request.setAttribute("fullName", fullName);
                request.setAttribute("phoneNumber", phoneNumber);
                request.setAttribute("email", email);
                request.setAttribute("roleId", roleId);
                request.setAttribute("listEmployee", employeeList);
                System.out.println("1");
                return  null;
>>>>>>> origin/HaiLong25
            }

            emp.setUsername(username);
            emp.setPassword(Encryption.toSHA256(password));
            emp.setFullName(fullName);
            emp.setPhoneNumber(phoneNumber);
            emp.setEmail(email);
            emp.setRegistrationDate(new Date(System.currentTimeMillis()));
            emp.setActivate(true);
<<<<<<< HEAD
            emp.setRole(RoleDAO.getInstance().getRoleById(roleId));

            String startFloorStr = request.getParameter("startFloor");
            String endFloorStr = request.getParameter("endFloor");
            Integer startFloor = null;
            Integer endFloor = null;

            if (startFloorStr != null && !startFloorStr.isEmpty()) {
                try {
                    startFloor = Integer.valueOf(startFloorStr);
                } catch (NumberFormatException e) {
                    request.setAttribute("error", "Invalid start floor number.");
                    hasError = true;
                }
            }

            if (endFloorStr != null && !endFloorStr.isEmpty()) {
                try {
                    endFloor = Integer.valueOf(endFloorStr);
                } catch (NumberFormatException e) {
                    request.setAttribute("error", "Invalid end floor number.");
                    hasError = true;
                }
            }

            if (startFloor != null && endFloor != null && startFloor >= endFloor) {
                request.setAttribute("error", "Start floor must be less than end floor.");
                hasError = true;
            }

            if ("Cleaner".equalsIgnoreCase(emp.getRole().getRoleName())) {
                if (startFloor != null && endFloor != null) {
                    CleanerFloor cf = new CleanerFloor();
                    cf.setStartFloor(startFloor);
                    cf.setEndFloor(endFloor);
                    emp.setCleanerFloor(cf);
                } else {
                    request.setAttribute("error", "Start floor and End floor must be provided for Cleaner role.");
                    hasError = true;
                }
            }

            if (hasError) {
                return null;
            }

            return emp;
        } catch (NumberFormatException e) {
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

            request.setAttribute("username", username);
            request.setAttribute("fullName", fullName);
            request.setAttribute("phoneNumber", phoneNumber);
            request.setAttribute("email", email);
            request.setAttribute("roleId", roleId);

            hasError = validateEditEmployeeInput(request, username, fullName, phoneNumber, email, roleId, emp.getEmployeeId(), hasError);

            if (hasError) {
                return null;
            }

            emp.setUsername(username);
            emp.setFullName(fullName);
            emp.setPhoneNumber(phoneNumber);
            emp.setEmail(email);
            emp.setRole(RoleDAO.getInstance().getRoleById(roleId));
            
            Employee existingEmployee = EmployeeDAO.getInstance().getEmployeeById(emp.getEmployeeId());
            emp.setActivate(existingEmployee.isActivate());

            String startFloorStr = request.getParameter("startFloor");
            String endFloorStr = request.getParameter("endFloor");
            Integer startFloor = null;
            Integer endFloor = null;

            if (startFloorStr != null && !startFloorStr.isEmpty()) {
                try {
                    startFloor = Integer.valueOf(startFloorStr);
                } catch (NumberFormatException e) {
                    request.setAttribute("error", "Invalid start floor number.");
                    hasError = true;
                }
            }

            if (endFloorStr != null && !endFloorStr.isEmpty()) {
                try {
                    endFloor = Integer.valueOf(endFloorStr);
                } catch (NumberFormatException e) {
                    request.setAttribute("error", "Invalid end floor number.");
                    hasError = true;
                }
            }

            if (startFloor != null && endFloor != null && startFloor >= endFloor) {
                request.setAttribute("error", "Start floor must be less than end floor.");
                hasError = true;
            }

            if ("Cleaner".equalsIgnoreCase(emp.getRole().getRoleName())) {
                if (startFloor != null && endFloor != null) {
                    CleanerFloor cleanerFloor = new CleanerFloor();
                    cleanerFloor.setStartFloor(startFloor);
                    cleanerFloor.setEndFloor(endFloor);
                    emp.setCleanerFloor(cleanerFloor);
                } else {
                    emp.setCleanerFloor(null);
                }
            }

            if (hasError) {
                return null;
            }

            return emp;
        } catch (NumberFormatException e) {
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
        } catch (NumberFormatException e) {
            request.setAttribute("error", "An error occurred while updating employee status: " + e.getMessage());
            doGet(request, response);
        }
    }

=======
            emp.setRole(role);
            
            System.out.println("Employ in create func: " + emp.toString());

            Integer floor = request.getParameter("floor") != null && !request.getParameter("floor").isEmpty() ?
                            Integer.parseInt(request.getParameter("floor")) : null;
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
>>>>>>> origin/HaiLong25
}
