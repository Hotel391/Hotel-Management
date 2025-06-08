package controllers.common;

import dal.AccountGoogleDAO;
import dal.CustomerAccountDAO;
import dal.EmployeeDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import dal.CustomerDAO;
import models.Customer;
import java.util.Random;
import models.AccountGoogle;
import models.CustomerAccount;
import models.Employee;
import models.Role;

public class Login extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");

        HttpSession session = request.getSession(true);

        String service = request.getParameter("service");

        String state = request.getParameter("state");

        System.out.println("state: " + state);

        if (service == null) {
            service = "login";
        }

        if ("login".equals(service) && state == null) {

            String submit = request.getParameter("submit");
            if (submit == null) {
                request.getRequestDispatcher("View/Login.jsp").forward(request, response);
            } else {

                String username = request.getParameter("username");

                String password = request.getParameter("password");

                if (username == null || password == null || username.isEmpty() || password.isEmpty()) {
                    request.setAttribute("error", "Please fill all information");
                    request.getRequestDispatcher("View/Login.jsp").forward(request, response);

                } else if (CustomerAccountDAO.getInstance().checkLogin(username, password) != null) {
                    CustomerAccount customerInfo = CustomerAccountDAO.getInstance().checkLogin(username, password);
                    session.setAttribute("customerInfo", customerInfo);
                    response.sendRedirect("customer/home");
                } else if (EmployeeDAO.getInstance().getEmployeeLogin(username, password) != null) {
                    Employee employeeInfo = EmployeeDAO.getInstance().getEmployeeLogin(username, password);
                    System.out.println(employeeInfo.toString());
                    session.setAttribute("employeeInfo", employeeInfo);
                    switch (employeeInfo.getRole().getRoleId()) {
                        case 0:
                            response.sendRedirect("developerPage");
                            break;
                        case 1:

//                            request.getRequestDispatcher("View/Admin/Dashboard.jsp").forward(request, response);
                            response.sendRedirect("admin/dashboard");
                            break;
                        case 2:
                            response.sendRedirect("receptionistPage");
                            break;
                        case 3:
//                            request.getRequestDispatcher("View/Admin/ViewService.jsp").forward(request, response);
                            break;
                        default:
                            request.getRequestDispatcher("View/Login.jsp").forward(request, response);
                            break;
                    }
                } else {
                    request.setAttribute("error", "Wrong username or password");
                    request.getRequestDispatcher("View/Login.jsp").forward(request, response);
                }
            }
        }

        if ("loginGoogle".equals(state)) {
            String code = request.getParameter("code");

            String accessToken = AccountGoogleDAO.getInstance().getToken(code);

            AccountGoogle userInfo = AccountGoogleDAO.getInstance().getUserInfo(accessToken);

            if (CustomerDAO.getInstance().checkExistedEmail(userInfo.getEmail()) == false) {
                Customer customerInfo = new Customer();

                customerInfo.setFullName(userInfo.getName());

                customerInfo.setActivate(true);

                customerInfo.setRole(new Role(4, "Customer"));

                customerInfo.setEmail(userInfo.getEmail());

                int customerId = CustomerDAO.getInstance().insertCustomer(customerInfo);

                String[] part = userInfo.getEmail().split("@");

                CustomerAccount accInfo = new CustomerAccount();

                //set username for google account
                if (CustomerAccountDAO.getInstance().isUsernameExisted(part[0])) {
                    accInfo.setUsername(generateRandomString(8));
                    while (CustomerAccountDAO.getInstance().isUsernameExisted(accInfo.getUsername())) {
                        accInfo.setUsername(generateRandomString(8));
                    }
                } else {
                    accInfo.setUsername(part[0]);
                }

                accInfo.setCustomer(CustomerDAO.getInstance().getCustomerByCustomerID(customerId));

                CustomerAccountDAO.getInstance().insertCustomerAccount(accInfo);
                session.setAttribute("customerInfo", accInfo);
            } else {
                CustomerAccount accInfo = CustomerAccountDAO.getInstance().checkAccountByEmail(userInfo.getEmail());
                session.setAttribute("customerInfo", accInfo);
            }
            response.sendRedirect("customer/home");
        }

        if ("logout".equals(service)) {
            session.removeAttribute("customerInfo");
            session.removeAttribute("employeeInfo");
            session.invalidate();
            response.sendRedirect("login");
        }

    }

    private String generateRandomString(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        StringBuilder result = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < length; i++) {
            int index = random.nextInt(characters.length());
            result.append(characters.charAt(index));
        }

        return result.toString();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
