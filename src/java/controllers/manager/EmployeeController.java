package controllers.manager;

import dal.CustomerAccountDAO;
import dal.CustomerDAO;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import models.CleanerFloor;
import models.Employee;
import models.Role;
import utility.EmailService;
import utility.Encryption;
import utility.Validation;
import utility.email_factory.EmailTemplateFactory;

@WebServlet(name = "ManagerEmployees", urlPatterns = {"/manager/employees"})
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
        if (currentPage > endPage) {
            currentPage = endPage;
        }

        List<Employee> employeeList = EmployeeDAO.getInstance().employeePagination(currentPage, key);
        request.setAttribute("currentPage", currentPage);
        request.setAttribute("listEmployee", employeeList);
        request.setAttribute("key", key);
        request.setAttribute("roleId", roleId);
        request.setAttribute("status", status);

        request.getRequestDispatcher("/View/Manager/Employee.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        String page = request.getParameter("page");
        String key = request.getParameter("key");

        try {
            switch (action) {
                case "add":
                    Employee newEmp = addEmployee(request, response);
                    if (newEmp != null) {
                        EmployeeDAO.getInstance().addEmployee(newEmp);
                    } else {
                        request.setAttribute("showAddModal", true);
                        if (request.getAttribute("addErrors") != null) {
                            request.setAttribute("addErrors", request.getAttribute("addErrors"));
                        }
                        doGet(request, response);
                        return;
                    }
                    break;
                case "update":
                    Employee updateEmp = editEmployee(request, response);
                    if (updateEmp != null) {
                        EmployeeDAO.getInstance().updateEmployee(updateEmp);
                        request.getSession().setAttribute("success", "Cập nhật thông tin nhân viên thành công.");
                    } else {
                        request.setAttribute("showEditModalId", request.getParameter("employeeId"));
                        doGet(request, response);
                        return;
                    }
                    break;
                case "delete":
                    int employeeId = Integer.parseInt(request.getParameter("employeeId"));
                    if (EmployeeDAO.getInstance().getEmployeeById(employeeId).isActivate() == false) {
                        EmployeeDAO.getInstance().deleteEmployee(employeeId);
                        request.getSession().setAttribute("success", "Xóa nhân viên thành công.");
                    } else {
                        request.getSession().setAttribute("error", "Không xóa nhân viên đang hoạt động!");
                    }
                    break;
                case "toggleStatus":
                    int empId = Integer.parseInt(request.getParameter("employeeId"));
                    Employee emp = EmployeeDAO.getInstance().getEmployeeById(empId);
                    if (emp != null) {
                        boolean newStatus = !emp.isActivate();
                        EmployeeDAO.getInstance().updateEmployeeStatus(empId, newStatus);
                        request.getSession().setAttribute("success", "Cập nhật trạng thái nhân viên thành công.");
                    }
                    break;
                default:
                    request.setAttribute("error", "Invalid action.");
                    doGet(request, response);
                    return;
            }
        } catch (Exception e) {
            request.setAttribute("error", "Error: " + e.getMessage());
            doGet(request, response);
            return;
        }

        String redirectUrl = request.getContextPath() + "/manager/employees";
        if (page != null || key != null) {
            redirectUrl += "?";
            if (page != null) {
                redirectUrl += "page=" + page;
            }
            if (page != null && key != null) {
                redirectUrl += "&";
            }
            if (key != null) {
                redirectUrl += "key=" + key;
            }
        }
        response.sendRedirect(redirectUrl);
    }

    private Employee addEmployee(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        boolean hasError = false;
        List<String> errorMessages = new ArrayList<>();
        Employee emp = new Employee();

        try {
            String username = request.getParameter("username");
            String password = request.getParameter("password");
            String fullName = request.getParameter("fullName");
            String phoneNumber = request.getParameter("phoneNumber");
            String email = request.getParameter("email");
            int roleId = Integer.parseInt(request.getParameter("roleId"));
            String startFloorStr = request.getParameter("startFloor");
            String endFloorStr = request.getParameter("endFloor");

            request.setAttribute("username", username);
            request.setAttribute("password", password);
            request.setAttribute("fullName", fullName);
            request.setAttribute("phoneNumber", phoneNumber);
            request.setAttribute("email", email);
            request.setAttribute("roleId", roleId);
            request.setAttribute("startFloor", startFloorStr);
            request.setAttribute("endFloor", endFloorStr);

            hasError = validateAddEmployeeInput(request, username, password, fullName, phoneNumber, email, roleId, startFloorStr, endFloorStr, errorMessages);

            if (hasError) {
                request.setAttribute("addErrors", errorMessages);
                request.setAttribute("showAddModal", true);
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

            if ("Cleaner".equalsIgnoreCase(emp.getRole().getRoleName())) {
                Integer startFloor = Integer.valueOf(startFloorStr);
                Integer endFloor = Integer.valueOf(endFloorStr);
                CleanerFloor cf = new CleanerFloor();
                cf.setStartFloor(startFloor);
                cf.setEndFloor(endFloor);
                emp.setCleanerFloor(cf);
            }

            EmailService emailService = new EmailService();
            Map<String, Object> emailData = new HashMap<>();
            emailData.put("fullname", fullName);
            emailData.put("username", username);
            emailData.put("password", password);
            emailData.put("loginLink", "http://localhost:9999/vn_pay/login");

            try {
                emailService.sendEmail(email, "Confirm account employee FPTHotel", EmailTemplateFactory.EmailType.EMPLOYEE_ACCOUNT, emailData);
                request.getSession().setAttribute("success", "Thêm nhân viên và gửi email xác nhận thành công.");
            } catch (Exception e) {
                System.out.println("Email sending failed: " + e.getMessage());
                errorMessages.add("Không thể gửi email xác nhận. Vui lòng kiểm tra địa chỉ email và thử lại.");
                request.setAttribute("addErrors", errorMessages);
                request.setAttribute("showAddModal", true);
                return null;
            }
            return emp;
        } catch (NumberFormatException e) {
            errorMessages.add("Đã xảy ra lỗi: " + e.getMessage());
            request.setAttribute("addErrors", errorMessages);
            request.setAttribute("showAddModal", true);
            return null;
        }
    }

    private Employee editEmployee(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        boolean hasError = false;
        List<String> errorMessages = new ArrayList<>();
        Employee emp = new Employee();

        try {
            String employeeIdStr = request.getParameter("employeeId");
            if (employeeIdStr == null || employeeIdStr.trim().isEmpty()) {
                errorMessages.add("Employee ID is required for update.");
                request.setAttribute("editErrors_" + employeeIdStr, errorMessages);
                return null;
            }
            emp.setEmployeeId(Integer.parseInt(employeeIdStr));

            String username = request.getParameter("username");
            String fullName = request.getParameter("fullName");
            String phoneNumber = request.getParameter("phoneNumber");
            String email = request.getParameter("email");
            String startFloorStr = request.getParameter("startFloor");
            String endFloorStr = request.getParameter("endFloor");

            request.setAttribute("username", username);
            request.setAttribute("fullName", fullName);
            request.setAttribute("phoneNumber", phoneNumber);
            request.setAttribute("email", email);
            request.setAttribute("startFloor", startFloorStr);
            request.setAttribute("endFloor", endFloorStr);

            Employee existingEmployee = EmployeeDAO.getInstance().getEmployeeById(emp.getEmployeeId());
            if (existingEmployee == null) {
                errorMessages.add("Nhân viên không tồn tại!");
                request.setAttribute("editErrors_" + emp.getEmployeeId(), errorMessages);
                return null;
            }

            emp.setRole(existingEmployee.getRole());

            hasError = validateEditEmployeeInput(request, username, fullName, phoneNumber, email, existingEmployee.getRole().getRoleId(), emp.getEmployeeId(), startFloorStr, endFloorStr, errorMessages);

            if (hasError) {
                request.setAttribute("editErrors_" + emp.getEmployeeId(), errorMessages);
                request.setAttribute("showEditModalId", emp.getEmployeeId());
                return null;
            }

            emp.setUsername(username);
            emp.setFullName(fullName);
            emp.setPhoneNumber(phoneNumber);
            emp.setEmail(email);
            emp.setActivate(existingEmployee.isActivate());

            if ("Cleaner".equalsIgnoreCase(emp.getRole().getRoleName())) {
                Integer startFloor = Integer.valueOf(startFloorStr);
                Integer endFloor = Integer.valueOf(endFloorStr);
                CleanerFloor cf = new CleanerFloor();
                cf.setStartFloor(startFloor);
                cf.setEndFloor(endFloor);
                emp.setCleanerFloor(cf);
            } else {
                emp.setCleanerFloor(null);
            }

            return emp;
        } catch (NumberFormatException e) {
            errorMessages.add("Đã xảy ra lỗi: " + e.getMessage());
            request.setAttribute("editErrors_" + emp.getEmployeeId(), errorMessages);
            request.setAttribute("showEditModalId", emp.getEmployeeId());
            return null;
        }
    }

    private boolean validateAddEmployeeInput(HttpServletRequest request, String username, String password,
            String fullName, String phoneNumber, String email, int roleId, String startFloorStr, String endFloorStr, List<String> errorMessages) {
        boolean hasError = false;

        if (Validation.validateField(request, "usernameError", "Tên đăng nhập", username, s -> s, "USERNAME")) {
            errorMessages.add((String) request.getAttribute("usernameError"));
            hasError = true;
        }

        if (Validation.validateField(request, "passwordError", "Mật khẩu", password, s -> s, "PASSWORD")) {
            errorMessages.add((String) request.getAttribute("passwordError"));
            hasError = true;
        }

        if (Validation.validateField(request, "fullNameError", "Họ tên", fullName, s -> s, "FULLNAME")) {
            errorMessages.add((String) request.getAttribute("fullNameError"));
            hasError = true;
        }

        if (Validation.validateField(request, "phoneNumberError", "Số điện thoại", phoneNumber, s -> s, "PHONE_NUMBER")) {
            errorMessages.add((String) request.getAttribute("phoneNumberError"));
            hasError = true;
        }

        if (Validation.validateField(request, "emailError", "Email", email, s -> s, "EMAIL")) {
            errorMessages.add((String) request.getAttribute("emailError"));
            hasError = true;
        }

        EmployeeDAO employeeDAO = EmployeeDAO.getInstance();
        CustomerAccountDAO customerDAO = CustomerAccountDAO.getInstance();
        CustomerDAO customer = CustomerDAO.getInstance();

        if (employeeDAO.isUsernameExisted(username) || customerDAO.isUsernameExisted(username)) {
            errorMessages.add("Tên đăng nhập đã tồn tại!");
            hasError = true;
        }
        if (employeeDAO.getAllString("Email").contains(email) || customer.checkExistedEmail(email)) {
            errorMessages.add("Email đã tồn tại!");
            hasError = true;
        }
        if (employeeDAO.getAllString("PhoneNumber").contains(phoneNumber) || customer.isPhoneExisted(phoneNumber)) {
            errorMessages.add("Số điện thoại đã tồn tại!");
            hasError = true;
        }

        Role role = RoleDAO.getInstance().getRoleById(roleId);
        if (role == null) {
            errorMessages.add("Vai trò không hợp lệ!");
            hasError = true;
        }

        if (role != null && "Cleaner".equalsIgnoreCase(role.getRoleName())) {
            Integer startFloor = null;
            Integer endFloor = null;

            if (startFloorStr == null || startFloorStr.trim().isEmpty()) {
                errorMessages.add("Vui lòng nhập tầng bắt đầu cho nhân viên dọn phòng.");
                hasError = true;
            } else {
                try {
                    startFloor = Integer.valueOf(startFloorStr);
                } catch (NumberFormatException e) {
                    errorMessages.add("Tầng bắt đầu không hợp lệ.");
                    hasError = true;
                }
            }

            if (endFloorStr == null || endFloorStr.trim().isEmpty()) {
                errorMessages.add("Vui lòng nhập tầng kết thúc cho vai trò nhân viên dọn phòng.");
                hasError = true;
            } else {
                try {
                    endFloor = Integer.valueOf(endFloorStr);
                } catch (NumberFormatException e) {
                    errorMessages.add("Tầng kết thúc không hợp lệ.");
                    hasError = true;
                }
            }

            if (startFloor != null && endFloor != null && startFloor > endFloor) {
                errorMessages.add("Tầng bắt đầu phải nhỏ hơn hoặc bằng tầng kết thúc.");
                hasError = true;
            }
        }

        return hasError;
    }

    private boolean validateEditEmployeeInput(HttpServletRequest request, String username, String fullName,
            String phoneNumber, String email, int roleId, int employeeId, String startFloorStr, String endFloorStr, List<String> errorMessages) {
        boolean hasError = false;

        if (Validation.validateField(request, "usernameError", "Tên đăng nhập", username, s -> s, "USERNAME")) {
            errorMessages.add((String) request.getAttribute("usernameError"));
            hasError = true;
        }
        if (Validation.validateField(request, "fullNameError", "Họ tên", fullName, s -> s, "FULLNAME")) {
            errorMessages.add((String) request.getAttribute("fullNameError"));
            hasError = true;
        }
        if (Validation.validateField(request, "phoneNumberError", "Số điện thoại", phoneNumber, s -> s, "PHONE_NUMBER")) {
            errorMessages.add((String) request.getAttribute("phoneNumberError"));
            hasError = true;
        }
        if (Validation.validateField(request, "emailError", "Email", email, s -> s, "EMAIL")) {
            errorMessages.add((String) request.getAttribute("emailError"));
            hasError = true;
        }

        EmployeeDAO employeeDAO = EmployeeDAO.getInstance();
        CustomerAccountDAO customerDAO = CustomerAccountDAO.getInstance();
        CustomerDAO customer = CustomerDAO.getInstance();

        Employee existingEmployee = employeeDAO.getEmployeeById(employeeId);
        if (!username.equals(existingEmployee.getUsername()) && (employeeDAO.isUsernameExisted(username) || customerDAO.isUsernameExisted(username))) {
            errorMessages.add("Tên đăng nhập đã tồn tại!");
            hasError = true;
        }
        if (!email.equals(existingEmployee.getEmail()) && (employeeDAO.getAllString("Email").contains(email) || customer.checkExistedEmail(email))) {
            errorMessages.add("Email đã tồn tại!");
            hasError = true;
        }
        if (!phoneNumber.equals(existingEmployee.getPhoneNumber()) && (employeeDAO.getAllString("PhoneNumber").contains(phoneNumber) || customer.isPhoneExisted(phoneNumber))) {
            errorMessages.add("Số điện thoại đã tồn tại!");
            hasError = true;
        }

        Role role = RoleDAO.getInstance().getRoleById(roleId);
        if (role == null) {
            errorMessages.add("Vai trò không hợp lệ!");
            hasError = true;
        }

        if (role != null && "Cleaner".equalsIgnoreCase(role.getRoleName())) {
            if (startFloorStr == null || startFloorStr.trim().isEmpty()) {
                errorMessages.add("Vui lòng nhập tầng bắt đầu cho nhân viên dọn phòng.");
                hasError = true;
            } else {
                try {
                    Integer startFloor = Integer.valueOf(startFloorStr);
                    if (startFloor < 1 || startFloor > 7) {
                        errorMessages.add("Tầng bắt đầu phải từ 1 đến 7.");
                        hasError = true;
                    }
                } catch (NumberFormatException e) {
                    errorMessages.add("Tầng bắt đầu không hợp lệ.");
                    hasError = true;
                }
            }

            if (endFloorStr == null || endFloorStr.trim().isEmpty()) {
                errorMessages.add("Vui lòng nhập tầng kết thúc cho nhân viên dọn phòng.");
                hasError = true;
            } else {
                try {
                    Integer endFloor = Integer.valueOf(endFloorStr);
                    if (endFloor < 1 || endFloor > 7) {
                        errorMessages.add("Tầng kết thúc phải từ 1 đến 7.");
                        hasError = true;
                    }
                } catch (NumberFormatException e) {
                    errorMessages.add("Tầng kết thúc không hợp lệ.");
                    hasError = true;
                }
            }

            if (!hasError) {
                try {
                    Integer startFloor = Integer.valueOf(startFloorStr);
                    Integer endFloor = Integer.valueOf(endFloorStr);
                    if (startFloor > endFloor) {
                        errorMessages.add("Tầng bắt đầu phải nhỏ hơn hoặc bằng tầng kết thúc.");
                        hasError = true;
                    }
                } catch (NumberFormatException e) {
                }
            }
        }

        return hasError;
    }

}
