/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controllers.receptionist;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import models.Customer;
import models.Role;
import utility.Validation;
import dal.CustomerDAO;
import jakarta.servlet.http.HttpSession;

/**
 *
 * @author Hai Long
 */
@WebServlet(name = "Checkout", urlPatterns = {"/receptionist/checkout"})
public class Checkout extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        String service = request.getParameter("service");

        HttpSession session = request.getSession(true);

        if (service == null) {
            service = "view";
        }

        if ("view".equals(service)) {
            String search = request.getParameter("search");

            System.out.println(search);

            if (search != null && !search.trim().isEmpty()) {

                String phone = request.getParameter("phoneSearch");

                if (!Validation.validateField(request, "searchError", phone, "PHONE_NUMBER", "Search", "SDT không hợp lệ")) {

                    Customer existedCustomer = CustomerDAO.getInstance().getCustomerByPhoneNumber(phone);

                    if (existedCustomer != null) {
                        request.setAttribute("existedCustomer", existedCustomer);
                    } else if (phone != null && !phone.isEmpty()) {
                        request.setAttribute("newCustomer", "Khách hàng mới");
                    }
                }
            }
            request.getRequestDispatcher("/View/Receptionist/Checkout.jsp").forward(request, response);

        }

        if ("addNew".equals(service)) {

            boolean check = false;

            String genderValue = request.getParameter("gender");

            if (genderValue == null || genderValue.isEmpty()) {
                check = true;
                request.setAttribute("genderError", "Vui lòng chọn giới tính");
            }

            String fullName = request.getParameter("fullName");

            if (Validation.validateField(request, "nameError", fullName, "FULLNAME", "Họ tên", "Họ tên không hợp lệ")) {
                check = true;
            }

            String cccd = request.getParameter("cccd");

            if (Validation.validateField(request, "cccdError", cccd, "CCCD", "CCCD", "CCCD không hợp lệ")) {
                check = true;
            } else if (CustomerDAO.getInstance().checkcccd(cccd)) {
                check = true;
                request.setAttribute("cccdError", "CCCD đã tồn tại");
            }

            String phone = request.getParameter("phone");

            if (Validation.validateField(request, "phoneError", phone, "PHONE_NUMBER", "SĐT", "SDT không hợp lệ")) {
                check = true;
            } else if (CustomerDAO.getInstance().isPhoneExisted(phone)) {
                check = true;
                request.setAttribute("phoneError", "Số điện thoại đã tồn tại");
            }

            String email = request.getParameter("email");

            if (Validation.validateField(request, "emailError", email, "EMAIL", "email", "Email không hợp lệ")) {
                check = true;
            } else if (CustomerDAO.getInstance().checkExistedEmail(email)) {
                check = true;
                request.setAttribute("emailError", "Email đã tồn tại");
            }

            if (!check) {
                Customer customer = new Customer();
                customer.setFullName(fullName);
                customer.setCCCD(cccd);
                customer.setEmail(email);
                customer.setPhoneNumber(phone);
                customer.setRole(new Role(2));
                customer.setGender(genderValue.equals("Male"));
                customer.setActivate(true);

                int checkUpdate = CustomerDAO.getInstance().insertCustomerExceptionId(customer);

                session.setAttribute("customerId", checkUpdate);
                
                session.setAttribute("status", "checkIn");
                
                response.sendRedirect(request.getContextPath() + "/payment");
            } else {
                request.getRequestDispatcher("/View/Receptionist/Checkout.jsp").forward(request, response);
            }

        }
        
        if("addExisted".equals(service)){
            String phone = request.getParameter("phone");
            
            Customer existedCustomer = CustomerDAO.getInstance().getCustomerByPhoneNumber(phone);
            
            session.setAttribute("customerId", existedCustomer.getCustomerId());
            
            session.setAttribute("status", "checkIn");
            
            response.sendRedirect(request.getContextPath() + "/payment");
        }
        
        if("backToServicePage".equals(service)){
            session.removeAttribute("roomServiceMap");
            
            response.sendRedirect("receptionist/roomInformation");
        }
        
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}