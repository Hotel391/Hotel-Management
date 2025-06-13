package controllers.admin;

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

@WebServlet(name = "ManagerProfileServlet", urlPatterns = "/profile")
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

        String action = request.getParameter("action");
        boolean isEditing = "updateprofile".equals(action);

        request.setAttribute("manager", manager);
        request.setAttribute("isEditing", isEditing);

        request.getRequestDispatcher("/View/Admin/managerProfile.jsp").forward(request, response);
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

                if (!username.equals(manager.getUsername()) && employeeDAO.isUsernameExisted(username)) {
                    request.setAttribute("usernameError", "Tên đăng nhập đã tồn tại!");
                    hasError = true;
                }

                if (hasError) {
                    request.setAttribute("manager", manager);
                    request.setAttribute("isEditing", true);
                    request.getRequestDispatcher("/View/Admin/managerProfile.jsp").forward(request, response);
                    return;
                }

                manager.setUsername(username);
                manager.setFullName(fullName);
                manager.setAddress(address);
                manager.setPhoneNumber(phoneNumber);

                employeeDAO.updateEmployee(manager);
                session.setAttribute("employeeInfo", manager);
                response.sendRedirect(request.getContextPath() + "/managerProfile");
                return;

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

        doGet(request, response);
    }
}