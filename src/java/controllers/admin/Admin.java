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
import utility.Encryption;
import utility.Validation;

@WebServlet(name = "Admin", urlPatterns = {"/admin/page"})
public class Admin extends HttpServlet {

    private String linkInfoAdmin = "/View/Admin/InfoAdmin.jsp";
    private String linkAdminPage = "/View/Admin/AdminPage.jsp";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String service = request.getParameter("service");

        HttpSession session = request.getSession();
        Employee employeeInfo = (Employee) session.getAttribute("employeeInfo");
        String username = employeeInfo.getUsername();
//        session.setAttribute("username", username);

        if (service == null) {
            service = "viewAll";
        }

        if (service.equals("changePass")) {
//            Employee em = dal.EmployeeDAO.getInstance().getAccountAdmin(username);
//            request.setAttribute("employee", em);
            request.getRequestDispatcher(linkInfoAdmin).forward(request, response);
        }

        if (service.equals("activateManager")) { //soft delete
            String pageParam = request.getParameter("page"); 
            int employeeID = Integer.parseInt(request.getParameter("employeeID"));
            boolean activate = Boolean.parseBoolean(request.getParameter("activate"));
            dal.EmployeeDAO.getInstance().updateEmployeeStatus(employeeID, activate);
            response.sendRedirect(request.getContextPath() + "/admin/page?statusAction=true&action=changeStatus&page="+pageParam);
        }

        if (service.equals("deleteManager")) {
            String pageParam = request.getParameter("page"); 
            int employeeID = Integer.parseInt(request.getParameter("employeeID"));
            Employee employee = dal.EmployeeDAO.getInstance().getEmployeeById(employeeID);
            request.setAttribute("action", "delete");
            if (employee.isActivate() == true) {
                request.setAttribute("statusAction", false);
                Employee em = dal.EmployeeDAO.getInstance().getAccountAdmin(username);
                List<Employee> list = dal.AdminDao.getInstance().getAllEmployee();
                paginateServiceList(request, list);
                request.setAttribute("adminAccount", em);
                request.getRequestDispatcher(linkAdminPage).forward(request, response);
                return;
            }
            dal.AdminDao.getInstance().deleteManagerAccount(employeeID);
            response.sendRedirect(request.getContextPath() + "/admin/page?statusAction=true&action=delete&page="+pageParam);
        }

        if (service.equals("viewAll")) {
            Employee em = dal.EmployeeDAO.getInstance().getAccountAdmin(username);
            List<Employee> list = dal.AdminDao.getInstance().getAllEmployee();
            paginateServiceList(request, list);
            request.setAttribute("adminAccount", em);
            request.getRequestDispatcher(linkAdminPage).forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String service = request.getParameter("service");
        String userName = request.getParameter("username");

        if (service.equals("changePass")) {
            handleChangePassword(request, response, userName);
        } else if (service.equals("add")) {
            handleAddNewAccount(request, response, userName);
        }
    }

    private void handleChangePassword(HttpServletRequest request, HttpServletResponse response, String userName)
            throws ServletException, IOException {
        String newPass = request.getParameter("password");
        String newPassSh = Encryption.toSHA256(newPass);
        String confirmPW = request.getParameter("confirmPassword");
        Employee em = dal.EmployeeDAO.getInstance().getAccountAdmin(userName);
        boolean hasError = false;

        String oldPass = request.getParameter("oldpassword");
        String oldPassSh = Encryption.toSHA256(oldPass);
        if (!oldPassSh.equals(em.getPassword())) {
            request.setAttribute("oldPasswordError", "Mật khẩu cũ không đúng");
            request.setAttribute("type", "changepass");
            request.getRequestDispatcher(linkInfoAdmin).forward(request, response);
            return;
        }

        hasError |= Validation.validateField(
                request, "passwordError", newPass, "PASSWORD", "Password",
                "Mật khẩu phải có ít nhất 8 ký tự, bao gồm 1 chữ cái, 1 chữ số và 1 ký tự đặc biệt."
        );

        if (newPassSh.equals(em.getPassword())) {
            hasError = true;
            request.setAttribute("passwordError", "mật khẩu mới đang trùng với mật khẩu cũ");
        }

        if (!confirmPW.equals(newPass)) {
            hasError = true;
            request.setAttribute("confirmPasswordError", "mật khẩu confirm không trùng với mật khẩu mới");
        }

        if (hasError) {
            request.setAttribute("type", "changepass");
            request.getRequestDispatcher(linkInfoAdmin).forward(request, response);
            return;
        }

        dal.EmployeeDAO.getInstance().updatePasswordAdminByUsername(userName, newPassSh);
        response.sendRedirect(request.getContextPath() + "/admin/page?service=viewAll&statusAction=true&action=changePass");
    }

    private void handleAddNewAccount(HttpServletRequest request, HttpServletResponse response, String userNameManager)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        Employee employeeInfo = (Employee) session.getAttribute("employeeInfo");
        String usernameAdmin = employeeInfo.getUsername();

        String password = request.getParameter("password");
        String passwordSh = Encryption.toSHA256(password);
        boolean hasError = false;

        hasError |= Validation.validateField(
                request, "usernameError", userNameManager, "USERNAME", "Username",
                "Tên người dùng phải dài từ 5–20 ký tự, chỉ bao gồm chữ cái/số/dấu gạch dưới."
        );
        hasError |= Validation.validateField(
                request, "passwordError", password, "PASSWORD", "Password",
                "Mật khẩu phải có ít nhất 8 ký tự, bao gồm 1 chữ cái, 1 chữ số và 1 ký tự đặc biệt."
        );

        if (isUsernameTaken(userNameManager)) {
            hasError = true;
            request.setAttribute("usernameError", "Username đã được sử dụng.");
        }

        if (hasError) {
            Employee em = dal.EmployeeDAO.getInstance().getAccountAdmin(usernameAdmin);
            List<Employee> list = dal.AdminDao.getInstance().getAllEmployee();
            paginateServiceList(request, list);
            request.setAttribute("adminAccount", em);
            request.getRequestDispatcher(linkAdminPage).forward(request, response);
            return;
        }
        String page = request.getParameter("page");
        dal.AdminDao.getInstance().addNewAccountManager(userNameManager, passwordSh);
        response.sendRedirect(request.getContextPath() + "/admin/page?service=viewAll&statusAction=true&action=add&page="+page);
    }
    
    private void paginateServiceList(HttpServletRequest request, List<Employee> fullList) {
        String pageStr = request.getParameter("page");
        int page = 1;
        try {
            if (pageStr != null) {
                page = Integer.parseInt(pageStr);
            }
        } catch (NumberFormatException e) {
            // giữ page = 1 nếu sai định dạng
        }

        int recordsPerPage = 2;
        int totalRecords = fullList.size();
        int totalPages = (int) Math.ceil((double) totalRecords / recordsPerPage);

        // Giảm page nếu vượt quá số trang thực tế
        if (page > totalPages && totalPages > 0) {
            page = totalPages;
        }

        int start = (page - 1) * recordsPerPage;
        int end = Math.min(start + recordsPerPage, totalRecords);
        List<Employee> paginatedList = fullList.subList(start, end);

        request.setAttribute("list", paginatedList);
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);
    }

    private boolean isUsernameTaken(String userNameManager) {
        List<String> employees = dal.AdminDao.getInstance().getAllUsernames();
        for (String username : employees) {
            if (username.equalsIgnoreCase(userNameManager)) {
                return true;
            }
        }

        List<String> customerAccount = dal.CustomerAccountDAO.getInstance().getAllUsername();
        for (String usernameca : customerAccount) {
            if (usernameca.equalsIgnoreCase(userNameManager)) {
                return true;
            }
        }
        return false;
    }

}
