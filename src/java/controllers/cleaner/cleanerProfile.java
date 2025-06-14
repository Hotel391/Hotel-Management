package controllers.cleaner;

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
import utility.Encryption;
import utility.Validation;

@WebServlet(name = "CleanerProfileServlet", urlPatterns = "/cleanerProfile")
public class cleanerProfile extends HttpServlet {

    private EmployeeDAO employeeDAO;
    private CustomerAccountDAO customerAccountDAO;
    private CustomerDAO customerDAO;

    @Override
    public void init() throws ServletException {
        employeeDAO = EmployeeDAO.getInstance();
        customerAccountDAO = CustomerAccountDAO.getInstance();
        customerDAO = CustomerDAO.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        Employee cleaner = (Employee) session.getAttribute("employeeInfo");


        boolean isEditing = "updateprofile".equals(request.getParameter("action"));

        request.setAttribute("cleaner", cleaner);
        request.setAttribute("isEditing", isEditing);

        request.getRequestDispatcher("/View/Cleaner/cleanerProfile.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        Employee cleaner = (Employee) session.getAttribute("employeeInfo");

        String action = request.getParameter("action");

        try {
            if ("updateprofile".equals(action)) {
                handleProfileUpdate(request, response, cleaner, session);
                return;
            } else if ("changepassword".equals(action)) {
                handleChangePassword(request, cleaner, session);
                request.setAttribute("cleaner", cleaner);
                request.setAttribute("isEditing", false);
                request.getRequestDispatcher("/View/Cleaner/cleanerProfile.jsp").forward(request, response);
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Lỗi xử lý yêu cầu: " + e.getMessage());
            request.setAttribute("cleaner", cleaner);
            request.setAttribute("isEditing", true);
            request.getRequestDispatcher("/View/Cleaner/cleanerProfile.jsp").forward(request, response);
            return;
        }
        response.sendRedirect(request.getContextPath() + "/cleanerProfile");
    }

    private void handleProfileUpdate(HttpServletRequest request, HttpServletResponse response, Employee cleaner, HttpSession session)
            throws ServletException, IOException {

        String username = request.getParameter("username");
        String fullName = request.getParameter("fullName");
        String address = request.getParameter("address");
        String phoneNumber = request.getParameter("phoneNumber");
        String email = request.getParameter("email");

        boolean hasError = false;

        hasError |= Validation.validateField(request, "usernameError", username, "USERNAME", "Username", "Tên đăng nhập không hợp lệ!");
        hasError |= Validation.validateField(request, "fullNameError", fullName, "FULLNAME", "Họ tên", "Họ tên không hợp lệ!");
        hasError |= Validation.validateField(request, "addressError", address, "ADDRESS", "Địa chỉ", "Địa chỉ không hợp lệ!");
        hasError |= Validation.validateField(request, "phoneNumberError", phoneNumber, "PHONE_NUMBER", "Số điện thoại", "Số điện thoại không hợp lệ!");
        hasError |= Validation.validateField(request, "emailError", email, "EMAIL", "Email", "Email không hợp lệ!");
        
        if (!username.equals(cleaner.getUsername())
                && (employeeDAO.isUsernameExisted(username) || customerAccountDAO.isUsernameExisted(username))) {
            request.setAttribute("usernameError", "Tên đăng nhập đã tồn tại!");
            hasError = true;
        }

        if (!email.equals(cleaner.getEmail())
                && (employeeDAO.isEmailExisted(email, cleaner.getEmployeeId()) || customerDAO.isEmailExisted(email))) {
            request.setAttribute("emailError", "Email đã tồn tại!");
            hasError = true;
        }

        if (!phoneNumber.equals(cleaner.getPhoneNumber())
                && (employeeDAO.isPhoneExisted(phoneNumber, cleaner.getEmployeeId()) || customerDAO.isPhoneExisted(phoneNumber))) {
            request.setAttribute("phoneNumberError", "Số điện thoại đã tồn tại!");
            hasError = true;
        }

        if (hasError) {
            request.setAttribute("cleaner", cleaner);
            request.setAttribute("isEditing", true);
            request.getRequestDispatcher("/View/Cleaner/cleanerProfile.jsp").forward(request, response);
            return;
        }

        cleaner.setUsername(username);
        cleaner.setFullName(fullName);
        cleaner.setAddress(address);
        cleaner.setPhoneNumber(phoneNumber);
        cleaner.setEmail(email);

        employeeDAO.updateEmployee(cleaner);
        session.setAttribute("employeeInfo", cleaner);

        response.sendRedirect(request.getContextPath() + "/cleanerProfile");
    }

    private void handleChangePassword(HttpServletRequest request, Employee cleaner, HttpSession session) {
        String currentPassword = request.getParameter("currentPassword");
        String newPassword = request.getParameter("newPassword");
        String confirmPassword = request.getParameter("confirmPassword");

        String encryptedCurrent = Encryption.toSHA256(currentPassword);

        if (!encryptedCurrent.equals(cleaner.getPassword())) {
            request.setAttribute("error", "Mật khẩu hiện tại không đúng!");
        } else if (!newPassword.equals(confirmPassword)) {
            request.setAttribute("error", "Mật khẩu mới và xác nhận mật khẩu không khớp!");
        } else if (!Validation.checkFormatException(newPassword, "PASSWORD")) {
            request.setAttribute("error", "Mật khẩu mới phải dài ít nhất 8 ký tự, chứa cả chữ, số và ký tự đặc biệt!");
        } else {
            String encryptedNew = Encryption.toSHA256(newPassword);
            employeeDAO.changePassword(cleaner.getEmployeeId(), encryptedNew);
            cleaner.setPassword(encryptedNew);
            session.setAttribute("employeeInfo", cleaner);
            request.setAttribute("success", "Đổi mật khẩu thành công!");
        }
    }
}
