package controllers.receptionist;

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

@WebServlet(name = "ReceptionistProfileServlet", urlPatterns = "/receptionist/profile")
public class ReceptionistProfile extends HttpServlet {

    private EmployeeDAO employeeDAO;

    @Override
    public void init() throws ServletException {
        employeeDAO = EmployeeDAO.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Employee receptionist = (Employee) session.getAttribute("employeeInfo");

        request.setAttribute("receptionist", receptionist);
        request.getRequestDispatcher("/View/Receptionist/receptionistProfile.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        Employee receptionist = (Employee) session.getAttribute("employeeInfo");
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

                // Use identity function for string validation
                Function<String, String> identity = Function.identity();
                Function<String, Date> dateParser = Date::valueOf;

                // Validate all fields
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

                Employee existingEmployee = employeeDAO.getEmployeeById(receptionist.getEmployeeId());

                if (!username.equals(existingEmployee.getUsername()) && employeeDAO.isUsernameExisted(username)) {
                    request.setAttribute("usernameError", "Tên đăng nhập đã tồn tại!");
                    hasError = true;
                }
                if (!email.equals(existingEmployee.getEmail()) && employeeDAO.getAllString("Email").contains(email)) {
                    request.setAttribute("emailError", "Email đã được sử dụng!");
                    hasError = true;
                }
                if (!phoneNumber.equals(existingEmployee.getPhoneNumber()) && employeeDAO.getAllString("PhoneNumber").contains(phoneNumber)) {
                    request.setAttribute("phoneNumberError", "Số điện thoại đã được sử dụng!");
                    hasError = true;
                }

                if (hasError) {
                    request.setAttribute("receptionist", receptionist);
                    request.getRequestDispatcher("/View/Receptionist/receptionistProfile.jsp").forward(request, response);
                    return;
                }

                receptionist.setUsername(username);
                receptionist.setFullName(fullName);
                receptionist.setAddress(address);
                receptionist.setPhoneNumber(phoneNumber);
                receptionist.setEmail(email);
                receptionist.setGender("Nam".equalsIgnoreCase(genderStr.trim()));
                receptionist.setCCCD(cccd);
                if (dateOfBirth != null && !dateOfBirth.trim().isEmpty()) {
                    receptionist.setDateOfBirth(Date.valueOf(dateOfBirth.trim()));
                }

                employeeDAO.updateEmployee(receptionist);
                session.setAttribute("employeeInfo", receptionist);
                request.setAttribute("success", "Cập nhật hồ sơ thành công!");
            } else if ("changepassword".equals(action)) {
                String currentPassword = request.getParameter("currentPassword");
                String newPassword = request.getParameter("newPassword");

                String encryptedCurrent = Encryption.toSHA256(currentPassword);
                if (!encryptedCurrent.equals(receptionist.getPassword())) {
                    request.setAttribute("error", "Mật khẩu hiện tại không đúng!");
                } else if (!Validation.checkFormatException(newPassword, "PASSWORD")) {
                    request.setAttribute("error", "Mật khẩu mới phải dài ít nhất 8 ký tự, chứa cả chữ, số và ký tự đặc biệt!");
                } else {
                    String encryptedNew = Encryption.toSHA256(newPassword);
                    employeeDAO.changePassword(receptionist.getEmployeeId(), encryptedNew);
                    receptionist.setPassword(encryptedNew);
                    session.setAttribute("employeeInfo", receptionist);
                    request.setAttribute("success", "Đổi mật khẩu thành công!");
                }
            }

        } catch (Exception e) {
            request.setAttribute("error", "Lỗi xử lý yêu cầu: " + e.getMessage());
            e.printStackTrace();
        }

        request.setAttribute("receptionist", receptionist);
        request.getRequestDispatcher("/View/Receptionist/receptionistProfile.jsp").forward(request, response);
    }
}
