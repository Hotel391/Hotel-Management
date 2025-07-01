package controllers.manager;

import dal.CustomerAccountDAO;
import dal.CustomerDAO;
import dal.EmployeeDAO;
import models.Employee;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Date;
import utility.Encryption;
import utility.Validation;

@WebServlet(name = "ManagerProfileServlet", urlPatterns = "/managerProfile")
public class ManagerProfile extends HttpServlet {

    private EmployeeDAO employeeDAO;
    private int employeeId;

    @Override
    public void init() throws ServletException {
        employeeDAO = EmployeeDAO.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Employee manager = (Employee) session.getAttribute("employeeInfo");

        request.setAttribute("manager", manager);
        request.getRequestDispatcher("/View/Manager/managerProfile.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        Employee manager = (Employee) session.getAttribute("employeeInfo");
        String action = request.getParameter("action");

        try {
            if ("updateprofile".equals(action)) {
                String username = request.getParameter("username");
                String fullName = request.getParameter("fullName");
                String address = request.getParameter("address");
                String phoneNumber = request.getParameter("phoneNumber");
                String email = request.getParameter("email");
                String genderStr = request.getParameter("gender");
                String cccd = request.getParameter("CCCD");
                String dateOfBirth = request.getParameter("dateOfBirth");

                boolean hasError = false;

                if (Validation.validateField(request, "usernameError", username, "USERNAME", "Tên đăng nhập", "Tên đăng nhập không hợp lệ!")) {
                    hasError = true;
                }
                if (Validation.validateField(request, "fullNameError", fullName, "FULLNAME", "Họ tên", "Họ tên không hợp lệ!")) {
                    hasError = true;
                }
                if (Validation.validateField(request, "addressError", address, "ADDRESS", "Địa chỉ", "Địa chỉ không hợp lệ!")) {
                    hasError = true;
                }
                if (Validation.validateField(request, "phoneNumberError", phoneNumber, "PHONE_NUMBER", "Số điện thoại", "Số điện thoại không hợp lệ!")) {
                    hasError = true;
                }
                if (Validation.validateField(request, "emailError", email, "EMAIL", "Email", "Email không hợp lệ!")) {
                    hasError = true;
                }
                if (Validation.validateField(request, "cccdError", cccd, "CCCD", "CCCD", "CCCD không đúng định dạng ###-##-####")) {
                    hasError = true;
                }
                EmployeeDAO employeeDAO = EmployeeDAO.getInstance();
                CustomerAccountDAO customerAccountDAO = CustomerAccountDAO.getInstance();
                CustomerDAO customerDAO = CustomerDAO.getInstance();

                Employee existingEmployee = employeeDAO.getEmployeeById(manager.getEmployeeId());

                if (!username.equals(existingEmployee.getUsername())
                        && (employeeDAO.isUsernameExisted(username) || customerAccountDAO.isUsernameExisted(username))) {
                    request.setAttribute("usernameError", "Tên đăng nhập đã tồn tại!");
                    hasError = true;
                }

                if (!email.equals(existingEmployee.getEmail())
                        && (employeeDAO.getAllString("Email").contains(email) || customerDAO.getInstance().isEmailExisted(email))) {
                    request.setAttribute("emailError", "Email đã được sử dụng!");
                    hasError = true;
                }

                if (!phoneNumber.equals(existingEmployee.getPhoneNumber())
                        && (employeeDAO.getAllString("PhoneNumber").contains(phoneNumber) || customerDAO.getAllString("PhoneNumber").contains(phoneNumber))) {
                    request.setAttribute("phoneNumberError", "Số điện thoại đã được sử dụng!");
                    hasError = true;
                }

                if (hasError) {
                    request.setAttribute("manager", manager);
                    request.getRequestDispatcher("/View/Manager/managerProfile.jsp").forward(request, response);
                    return;
                }

                if (dateOfBirth != null && !dateOfBirth.trim().isEmpty()) {
                    try {
                        java.sql.Date dob = java.sql.Date.valueOf(dateOfBirth.trim());
                        manager.setDateOfBirth(dob);
                    } catch (IllegalArgumentException e) {
                        request.setAttribute("dateOfBirthError", "Ngày sinh không hợp lệ (định dạng yyyy-MM-dd).");
                        hasError = true;
                    }
                }

                manager.setUsername(username);
                manager.setFullName(fullName);
                manager.setAddress(address);
                manager.setPhoneNumber(phoneNumber);
                manager.setEmail(email);
                manager.setGender("Nam".equalsIgnoreCase(genderStr.trim()));
                manager.setCCCD(cccd);

                employeeDAO.updateEmployee(manager);
                session.setAttribute("employeeInfo", manager);

                request.setAttribute("success", "Cập nhật hồ sơ thành công!");
            } else if ("changepassword".equals(action)) {
                String currentPassword = request.getParameter("currentPassword");
                String newPassword = request.getParameter("newPassword");

                String encryptedCurrent = Encryption.toSHA256(currentPassword);
                if (!encryptedCurrent.equals(manager.getPassword())) {
                    request.setAttribute("error", "Mật khẩu hiện tại không đúng!");
                } else if (!Validation.checkFormatException(newPassword, "PASSWORD")) {
                    request.setAttribute("error", "Mật khẩu mới phải dài ít nhất 8 ký tự, chứa cả chữ, số và ký tự đặc biệt!");
                } else {
                    String encryptedNew = Encryption.toSHA256(newPassword);
                    employeeDAO.changePassword(manager.getEmployeeId(), encryptedNew);
                    manager.setPassword(encryptedNew);
                    session.setAttribute("employeeInfo", manager);
                    request.setAttribute("success", "Đổi mật khẩu thành công!");
                }
            }

        } catch (Exception e) {
            request.setAttribute("error", "Lỗi xử lý yêu cầu: " + e.getMessage());
            e.printStackTrace();
        }

        request.setAttribute("manager", manager);
        request.getRequestDispatcher("/View/Manager/managerProfile.jsp").forward(request, response);
    }
}
