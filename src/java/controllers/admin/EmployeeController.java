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

<<<<<<< Updated upstream

            hasError |= Validation.validateField(request, "usernameError", username, "USERNAME", "Username", null);
            hasError |= Validation.validateField(request, "passwordError", password, "PASSWORD", "Password", null);
            hasError |= Validation.validateField(request, "fullNameError", fullName, "FULLNAME", "Full Name", null);
            hasError |= Validation.validateField(request, "phoneNumberError", phoneNumber, "PHONE_NUMBER", "Phone Number", null);
            hasError |= Validation.validateField(request, "emailError", email, "EMAIL", "Email", null);

            EmployeeDAO employeeDAO = EmployeeDAO.getInstance();
            CustomerAccountDAO customerAccountDAO = CustomerAccountDAO.getInstance();
            CustomerDAO customerDAO = CustomerDAO.getInstance();


            
            
            if (!hasError) {

                if (employeeDAO.isUsernameExisted(username) || customerAccountDAO.isUsernameExisted(username)) {

                    request.setAttribute("usernameError", "Tên đăng nhập đã tồn tại!");
                    hasError = true;
                }
                if (employeeDAO.getAllString("Email").contains(email) || customerDAO.isEmailExisted(email)) {
                    request.setAttribute("emailError", "Email đã tồn tại!");
                    hasError = true;
                }
                if (employeeDAO.getAllString("PhoneNumber").contains(phoneNumber) || customerDAO.isPhoneExisted(phoneNumber)) {
                    request.setAttribute("phoneNumberError", "Số điện thoại đã tồn tại!");
                    hasError = true;
                } else {
                }
            }
            
            Role role = RoleDAO.getInstance().getRoleById(roleId);
            
            System.out.println("role: " + role.getRoleName());
            

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
//                request.getRequestDispatcher("/View/Admin/Employee.jsp").forward(request, response);
                System.out.println("1");

                return  null;

=======
            hasError = validateEmployeeInput(request, username, fullName, phoneNumber, email, roleId, hasError);

            if (hasError) {
                return null;
>>>>>>> Stashed changes
            }

            emp.setUsername(username);
            emp.setPassword(Encryption.toSHA256(password));  // Mã hóa mật khẩu mới
            emp.setFullName(fullName);
            emp.setPhoneNumber(phoneNumber);
            emp.setEmail(email);
            emp.setRegistrationDate(new Date(System.currentTimeMillis()));
            emp.setActivate(true);
            emp.setRole(RoleDAO.getInstance().getRoleById(roleId));

<<<<<<< Updated upstream

            System.out.println("Employ in create func: " + emp.toString());

            Integer floor = request.getParameter("floor") != null && !request.getParameter("floor").isEmpty()
                    ? Integer.parseInt(request.getParameter("floor")) : null;

            if (floor != null && role.getRoleName().equalsIgnoreCase("Cleaner")) {
=======
            Integer floor = request.getParameter("floor") != null && !request.getParameter("floor").isEmpty()
                    ? Integer.parseInt(request.getParameter("floor")) : null;
            if (floor != null && "Cleaner".equalsIgnoreCase(emp.getRole().getRoleName())) {
>>>>>>> Stashed changes
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
            // Lấy employeeId từ form
            String employeeIdStr = request.getParameter("employeeId");
            if (employeeIdStr == null || employeeIdStr.trim().isEmpty()) {
                request.setAttribute("error", "Employee ID is required for update.");
                hasError = true;
            } else {
                emp.setEmployeeId(Integer.parseInt(employeeIdStr));
            }

            // Lấy các trường dữ liệu từ form
            String username = request.getParameter("username");
            String fullName = request.getParameter("fullName");
            String phoneNumber = request.getParameter("phoneNumber");
            String email = request.getParameter("email");
            int roleId = Integer.parseInt(request.getParameter("roleId"));

            // Kiểm tra dữ liệu nhập vào
            hasError = validateEmployeeInput(request, username, fullName, phoneNumber, email, roleId, hasError);

            if (hasError) {
                return null;  // Nếu có lỗi, trả về null để không thực hiện update
            }

            emp.setUsername(username);
            emp.setFullName(fullName);
            emp.setPhoneNumber(phoneNumber);
            emp.setEmail(email);

            // Xử lý phần role (Cập nhật role của nhân viên)
            emp.setRole(RoleDAO.getInstance().getRoleById(roleId));

            // Kiểm tra xem nhân viên có vai trò "Cleaner" không, và xử lý Floor
            if ("Cleaner".equalsIgnoreCase(emp.getRole().getRoleName())) {
                // Nếu vai trò là Cleaner, lấy thông tin floor từ form
                String floorStr = request.getParameter("floor");
                if (floorStr != null && !floorStr.isEmpty()) {
                    Integer floor = Integer.parseInt(floorStr);
                    CleanerFloor cleanerFloor = new CleanerFloor();
                    cleanerFloor.setFloor(floor);
                    emp.setCleanerFloor(cleanerFloor);
                } else {
                    // Nếu không có thông tin về floor, gán null
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
        if (Validation.validateField(request, "usernameError", username, "USERNAME", "Username", "Tên đăng nhập không hợp lệ!")) {
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
        if (!hasError && role == null) {
            request.setAttribute("error", "Invalid role selected.");
            hasError = true;
        }

        return hasError;
    }
}
