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
import java.util.function.Function;
import utility.Encryption;
import utility.Validation;
import java.sql.Date;

@WebServlet(name = "ManagerProfileServlet", urlPatterns = "/managerProfile")
public class ManagerProfile extends HttpServlet {

    private EmployeeDAO employeeDAO;

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

                Function<String, String> identity = Function.identity();
                Function<String, Date> dateParser = Date::valueOf;

                if (Validation.validateField(request, "usernameError", "Tên đăng nhập", username, identity, "USERNAME")) {
                    hasError = true;
                }
                if (Validation.validateField(request, "fullNameError", "Họ tên", fullName, identity, "FULLNAME")) {
                    hasError = true;
                }
                if (Validation.validateField(request, "addressError", "Địa chỉ", address, identity, "ADDRESS")) {
                    hasError = true;
                }
                if (Validation.validateField(request, "phoneNumberError", "Số điện thoại", phoneNumber, identity, "PHONE_NUMBER")) {
                    hasError = true;
                }
                if (Validation.validateField(request, "emailError", "Email", email, identity, "EMAIL")) {
                    hasError = true;
                }
                if (Validation.validateField(request, "cccdError", "CCCD", cccd, identity, "CCCD")) {
                    hasError = true;
                }
                if (Validation.validateField(request, "dateOfBirthError", "Ngày sinh", dateOfBirth, dateParser, "DATE_OF_BIRTH")) {
                    hasError = true;
                }

                if (genderStr == null || (!"Nam".equalsIgnoreCase(genderStr.trim()) && !"Nữ".equalsIgnoreCase(genderStr.trim()))) {
                    request.setAttribute("genderError", "Giới tính phải là Nam hoặc Nữ!");
                    hasError = true;
                }

                Employee existingEmployee = employeeDAO.getEmployeeById(manager.getEmployeeId());

                if (!username.equals(existingEmployee.getUsername())
                        && (employeeDAO.isUsernameExisted(username) || CustomerAccountDAO.getInstance().isUsernameExisted(username))) {
                    request.setAttribute("usernameError", "Tên đăng nhập đã tồn tại!");
                    hasError = true;
                }
                if (!email.equals(existingEmployee.getEmail())
                        && (employeeDAO.getAllString("Email").contains(email) || CustomerDAO.getInstance().isEmailExisted(email))) {
                    request.setAttribute("emailError", "Email đã được sử dụng!");
                    hasError = true;
                }
                if (!phoneNumber.equals(existingEmployee.getPhoneNumber())
                        && (employeeDAO.getAllString("PhoneNumber").contains(phoneNumber) || CustomerDAO.getInstance().getAllString("PhoneNumber").contains(phoneNumber))) {
                    request.setAttribute("phoneNumberError", "Số điện thoại đã được sử dụng!");
                    hasError = true;
                }

                if (hasError) {
                    request.setAttribute("manager", manager);
                    request.getRequestDispatcher("/View/Manager/managerProfile.jsp").forward(request, response);
                    return;
                }

                manager.setUsername(username);
                manager.setFullName(fullName);
                manager.setAddress(address);
                manager.setPhoneNumber(phoneNumber);
                manager.setEmail(email);
                manager.setGender("Nam".equalsIgnoreCase(genderStr.trim()));
                manager.setCCCD(cccd);
                if (dateOfBirth != null && !dateOfBirth.trim().isEmpty()) {
                    manager.setDateOfBirth(Date.valueOf(dateOfBirth.trim()));
                }

                employeeDAO.updateEmployee(manager);
                session.setAttribute("employeeInfo", manager);
                request.setAttribute("success", "Cập nhật hồ sơ thành công!");
            } else if ("changepassword".equals(action)) {
                String currentPassword = request.getParameter("currentPassword");
                String newPassword = request.getParameter("newPassword");
                String confirmPassword = request.getParameter("confirmPassword");

                boolean hasError = false;

                String encryptedCurrent = Encryption.toSHA256(currentPassword);
                if (!encryptedCurrent.equals(manager.getPassword())) {
                    request.setAttribute("currentPasswordError", "Mật khẩu hiện tại không đúng!");
                    hasError = true;
                }

                hasError |= Validation.validateField(request, "newPasswordError", "Mật khẩu mới", newPassword, s -> s, "PASSWORD");

                if (!newPassword.equals(confirmPassword)) {
                    request.setAttribute("confirmPasswordError", "Mật khẩu xác nhận không trùng khớp!");
                    hasError = true;
                }

                if (hasError) {
                    request.setAttribute("manager", manager);
                    request.getRequestDispatcher("/View/Manager/managerProfile.jsp").forward(request, response);
                    return;
                }

                String encryptedNew = Encryption.toSHA256(newPassword);
                employeeDAO.changePassword(manager.getEmployeeId(), encryptedNew);
                manager.setPassword(encryptedNew);
                session.setAttribute("employeeInfo", manager);
                request.setAttribute("success", "Đổi mật khẩu thành công!");
            }

        } catch (Exception e) {
            request.setAttribute("passwordError", "Lỗi xử lý yêu cầu: " + e.getMessage());
            e.printStackTrace();
        }

        request.setAttribute("manager", manager);
        request.getRequestDispatcher("/View/Manager/managerProfile.jsp").forward(request, response);
    }
}
