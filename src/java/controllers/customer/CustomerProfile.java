package controllers.customer;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import models.Customer;
import models.CustomerAccount;
import utility.Encryption;
import utility.Validation;

@WebServlet(name = "CustomerProfile", urlPatterns = {"/customerProfile"})
public class CustomerProfile extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String service = request.getParameter("service");
        String submit = request.getParameter("submit");
        String username = request.getParameter("username");
        HttpSession session = request.getSession();
        String type;
        CustomerAccount ca = dal.CustomerDAO.getInstance().getCustomerAccount(username);

        if (service == null) {
            service = "info";
        }

        if (service.equals("changeUserName")) {
            type = "changeUserName";

            request.setAttribute("customerAccount", ca);

            if (submit == null) {
                request.setAttribute("type", type);
                request.getRequestDispatcher("/View/Customer/UpdateProfile.jsp").forward(request, response);
            } else {
                String customerIdS = request.getParameter("customerId");
                String newUserName = request.getParameter("newUserName");
                int customerId = 0;
                try {
                    customerId = Integer.parseInt(customerIdS);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }

                boolean hasError = false;
                hasError |= Validation.validateField(
                        request, "usernameError", newUserName, "USERNAME", "Username",
                        "Username must be 5–20 characters, letters/numbers/underscores only."
                );

                // Kiểm tra trùng username
                List<String> userName = dal.CustomerAccountDAO.getInstance().getAllUsername();
                for (String un : userName) {
                    if (un.equalsIgnoreCase(newUserName)) {
                        hasError = true;
                        request.setAttribute("usernameError", "username này đã tồn tại");
                        break;
                    }
                }
                
                List<String> employees = dal.AdminDao.getInstance().getAllUsernames();
                for (String un : employees) {
                    if (un.equalsIgnoreCase(username)) {
                        request.setAttribute("usernameError", "Username already exists.");
                        hasError = true;
                        break;
                    }
                }

                if (hasError) {
                    request.setAttribute("type", request.getParameter("type"));
                    request.getRequestDispatcher("/View/Customer/UpdateProfile.jsp").forward(request, response);
                    return;
                }

                // Cập nhật lại customerInfo trong session
                CustomerAccount updatedAccount = dal.CustomerAccountDAO.getInstance().getCustomerAccountById(customerId);
                session.setAttribute("customerInfo", updatedAccount);

                dal.CustomerAccountDAO.getInstance().changeUsername(newUserName, customerId);
                response.sendRedirect(request.getContextPath() + "/customerProfile?service=info&username=" + newUserName);
            }
        }

        if (service.equals("changePass")) {
            type = "changePass";

            request.setAttribute("customerAccount", ca);
            if (submit == null) {
                request.setAttribute("type", type);
                request.getRequestDispatcher("/View/Customer/UpdateProfile.jsp").forward(request, response);
            } else {
                String newPassWord = request.getParameter("newPassWord");
                String oldPass = request.getParameter("oldPass");
                String confirmPW = request.getParameter("confirmPassWord");

                String oldPassSh = Encryption.toSHA256(oldPass);
                String newPassWordSh = Encryption.toSHA256(newPassWord);

                if (oldPass != null) {
                    if (!oldPassSh.equals(ca.getPassword())) {
                        request.setAttribute("type", request.getParameter("type"));
                        request.setAttribute("oldPasswordError", "Mật khẩu cũ không đúng");
                        request.getRequestDispatcher("/View/Customer/UpdateProfile.jsp").forward(request, response);
                        return;
                    }
                }

                boolean hasError = false;
                hasError |= Validation.validateField(
                        request, "passwordError", newPassWord, "PASSWORD", "Password",
                        "Password must be at least 8 characters, include 1 letter, 1 digit, and 1 special character."
                );
                if (newPassWordSh.equals(ca.getPassword())) {
                    hasError = true;
                    request.setAttribute("passwordError", "pass mới trùng với pass cũ");
                }
                if (!newPassWord.equals(confirmPW)) {
                    hasError = true;
                    request.setAttribute("confirmPasswordError", "Mật khẩu confirm không trùng với mật khẩu mới");
                }
                if (hasError) {
                    request.setAttribute("type", request.getParameter("type"));
                    request.getRequestDispatcher("/View/Customer/UpdateProfile.jsp").forward(request, response);
                    return;
                }
                dal.CustomerAccountDAO.getInstance().changePassword(newPassWordSh, username);
                response.sendRedirect(request.getContextPath() + "/customer/profile?service=info&username=" + username);
            }

        }

        if (service.equals("update")) {
            type = "update";
            if (submit == null) {
                request.setAttribute("type", type);
                request.setAttribute("customerAccount", ca);
                request.getRequestDispatcher("/View/Customer/UpdateProfile.jsp").forward(request, response);
            } else {

                String fullName = request.getParameter("fullName");
                String phoneNumber = request.getParameter("phoneNumber");
                String gender = request.getParameter("gender");
                boolean genderBoolean = Boolean.parseBoolean(gender);
                int genderValue = genderBoolean ? 1 : 0;

                boolean hasError = false;
                hasError |= Validation.validateField(request, "fullNameError",
                        fullName, "FULLNAME", "Full Name",
                        "Full name must be 2–100 characters, letters and spaces only.");

                if (phoneNumber != null && !phoneNumber.trim().isEmpty()) {
                    hasError |= Validation.validateField(request, "phoneError",
                            phoneNumber, "PHONE_NUMBER",
                            "Phone Number",
                            "Phone number must start with 0 and have 10–11 digits.");
                    List<String> list = dal.CustomerDAO.getInstance().getAllPhone();
                    for (String phone : list) {
                        if (phone != null && phone.equals(phoneNumber)) {
                            request.setAttribute("phoneError", "Phone đã tồn tại");
                            hasError = true;
                            break;
                        }
                    }
                } else {
                    phoneNumber = null;
                }

                if (hasError) {
                    CustomerAccount caa = new CustomerAccount();
                    caa.setUsername(username);
                    Customer c = new Customer();
                    c.setFullName(fullName);
                    c.setPhoneNumber(phoneNumber);
                    c.setGender(genderBoolean);
                    caa.setCustomer(c);
                    request.setAttribute("customerAccount", caa);
                    request.setAttribute("type", request.getParameter("type"));
                    request.getRequestDispatcher("/View/Customer/UpdateProfile.jsp").forward(request, response);
                    return;
                }
                dal.CustomerDAO.getInstance().updateCustomerInfo(username, fullName, phoneNumber, genderValue);
                response.sendRedirect(request.getContextPath() + "/customerProfile?service=info&username=" + username);
                return;
            }
        }

        if (service.equals("info")) {
            request.setAttribute("customerAccount", ca);
            request.getRequestDispatcher("/View/Customer/Profile.jsp").forward(request, response);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
