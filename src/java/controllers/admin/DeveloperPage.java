package controllers.admin;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
        if (service == null) {
            service = "viewAll";
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
            List<Employee> list = dal.AdminDao.getInstance().getAllEmployee();
            request.setAttribute("list", list);
            request.getRequestDispatcher("View/Developer/DeveloperPage.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String service = request.getParameter("service");

        if ("add".equals(service)) {
            String userName = request.getParameter("userName");
            String password = request.getParameter("password");
            boolean hasError = false;
            // Kiểm tra trùng username
            List<Employee> employees = dal.EmployeeDAO.getInstance().getAllEmployee();
            for (Employee employee : employees) {
                if (employee.getUsername().equalsIgnoreCase(userName)) {
                    request.setAttribute("usernameError", "Username already exists.");
                    hasError = true;
                    break;
                }
            }

            // Kiểm tra định dạng username và password
            hasError |= Validation.validateField(
                    request, "usernameError", userName, "USERNAME", "Username",
                    "Username must be 5–20 characters, letters/numbers/underscores only."
            );

            hasError |= Validation.validateField(
                    request, "passwordError", password, "PASSWORD", "Password",
                    "Password must be at least 8 characters, include 1 letter, 1 digit, and 1 special character."
            );

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
