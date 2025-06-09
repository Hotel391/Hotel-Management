package controllers.cleaner;

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

    @Override
    public void init() throws ServletException {
        employeeDAO = EmployeeDAO.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        Employee cleaner = (Employee) session.getAttribute("employeeInfo");

        if (cleaner == null) {
            response.sendRedirect("/View/Login.jsp");
            return;
        }

        if (cleaner.getRole().getRoleId() != 3) {
            request.getRequestDispatcher("/View/Login.jsp").forward(request, response);
            return;
        }

        String action = request.getParameter("action");
        boolean isEditing = "updateprofile".equals(action);

        request.setAttribute("cleaner", cleaner);
        request.setAttribute("isEditing", isEditing);

        request.getRequestDispatcher("/View/Cleaner/cleanerProfile.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        Employee cleaner = (Employee) session.getAttribute("employeeInfo");

        if (cleaner == null) {
            response.sendRedirect("/View/Login.jsp");
            return;
        }

        
        if (cleaner.getRole().getRoleId() != 3) {
            request.getRequestDispatcher("/View/Login.jsp").forward(request, response);
            return;
        }

        String action = request.getParameter("action");

        try {
            if ("updateprofile".equals(action)) {
                String username = request.getParameter("username");
                String fullName = request.getParameter("fullName");
                String address = request.getParameter("address");
                String phoneNumber = request.getParameter("phoneNumber");

                boolean hasError = false;

                if (Validation.validateField(request, "usernameError", username, "USERNAME", "Username", "Tên đăng nhập không hợp lệ!")) {
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

                if (!username.equals(cleaner.getUsername()) && employeeDAO.getAllString("Username").contains(username)) {
                    request.setAttribute("usernameError", "Tên đăng nhập đã tồn tại!");
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

                employeeDAO.updateEmployee(cleaner);
                session.setAttribute("employeeInfo", cleaner);
                response.sendRedirect(request.getContextPath() + "/cleanerProfile");
                return;

            } else if ("changepassword".equals(action)) {
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

        } catch (Exception e) {
            request.setAttribute("error", "Lỗi xử lý yêu cầu: " + e.getMessage());
            e.printStackTrace();
        }

        doGet(request, response);
    }
}