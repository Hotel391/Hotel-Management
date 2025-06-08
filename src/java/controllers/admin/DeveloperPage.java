package controllers.admin;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import models.Employee;
import utility.Validation;

@WebServlet(name = "DeveloperPage", urlPatterns = {"/developerPage"})
public class DeveloperPage extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String service = request.getParameter("service");
        String submit = request.getParameter("submit");

        HttpSession session = request.getSession();
        String username = (String) session.getAttribute("username");

        if (service == null) {
            service = "viewAll";
        }

        if (service.equals("changePass")) {
            Employee em = dal.EmployeeDAO.getInstance().getAccountAdmin(username);
            request.setAttribute("employee", em);
            request.setAttribute("type", "changepass");
            request.getRequestDispatcher("View/Developer/InfoAdmin.jsp").forward(request, response);
        }

        if (service.equals("info")) {
            Employee em = dal.EmployeeDAO.getInstance().getAccountAdmin(username);
            System.out.println(em);

            request.setAttribute("employee", em);
            request.setAttribute("type", "info");
            request.getRequestDispatcher("View/Developer/InfoAdmin.jsp").forward(request, response);
        }

        if (service.equals("add")) {
            response.sendRedirect("View/Developer/AddManager.jsp");

        }

        if (service.equals("deleteManager")) {
            int employeeID = Integer.parseInt(request.getParameter("employeeID"));
            dal.AdminDao.getInstance().deleteManagerAccount(employeeID);
            response.sendRedirect("developerPage");
        }

        if (service.equals("viewAll")) {
            Employee em = dal.EmployeeDAO.getInstance().getAccountAdmin(username);
            List<Employee> list = dal.AdminDao.getInstance().getAllEmployee();
            request.setAttribute("list", list);
            request.setAttribute("adminAccount", em);
            request.getRequestDispatcher("View/Developer/DeveloperPage.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String service = request.getParameter("service");
        String userName = request.getParameter("userName");

        if (service.equals("changePass")) {
            String newPass = request.getParameter("password");
            Employee em = dal.EmployeeDAO.getInstance().getAccountAdmin(userName);
            boolean hasError = false;

            String oldPass = request.getParameter("oldpassword");
            if (!oldPass.equals(em.getPassword())) {
                request.setAttribute("oldPasswordError", "Mật khẩu cũ không đúng");
                request.setAttribute("type", "changepass");
                request.getRequestDispatcher("View/Developer/InfoAdmin.jsp").forward(request, response);
                return;
            }

            hasError |= Validation.validateField(
                    request, "passwordError", newPass, "PASSWORD", "Password",
                    "Password must be at least 8 characters, include 1 letter, 1 digit, and 1 special character."
            );

            if (newPass.equals(em.getPassword())) {
                hasError = true;
                request.setAttribute("passwordError", "mật khẩu mới đang trùng với mật khẩu cũ");
            }

            if (hasError) {
                request.setAttribute("type", "changepass");
                request.getRequestDispatcher("View/Developer/InfoAdmin.jsp").forward(request, response);
                return;
            }

            dal.EmployeeDAO.getInstance().updatePasswordAdminByUsername(userName, newPass);
            response.sendRedirect(request.getContextPath() + "/developerPage?service=info");
        }

        if ("add".equals(service)) {

            String password = request.getParameter("password");
            boolean hasError = false;
            // Kiểm tra trùng username
            

            // Kiểm tra định dạng username và password
            hasError |= Validation.validateField(
                    request, "usernameError", userName, "USERNAME", "Username",
                    "Username must be 5–20 characters, letters/numbers/underscores only."
            );

            hasError |= Validation.validateField(
                    request, "passwordError", password, "PASSWORD", "Password",
                    "Password must be at least 8 characters, include 1 letter, 1 digit, and 1 special character."
            );
            
            List<Employee> employees = dal.EmployeeDAO.getInstance().getAllEmployee();
            for (Employee employee : employees) {
                if (employee.getUsername().equalsIgnoreCase(userName)) {
                    request.setAttribute("usernameError", "Username already exists.");
                    hasError = true;
                    break;
                }
            }

            if (hasError) {
                request.getRequestDispatcher("View/Developer/AddManager.jsp").forward(request, response);
                return;
            }

            // Nếu hợp lệ: tạo tài khoản
            dal.AdminDao.getInstance().addNewAccountManager(userName, password);
            response.sendRedirect("developerPage");
        }

    }

}
