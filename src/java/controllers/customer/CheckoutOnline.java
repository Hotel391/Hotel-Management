/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controllers.customer;

import dal.CartDAO;
import dal.CartServiceDAO;
import dal.CustomerAccountDAO;
import dal.CustomerDAO;
import dal.TypeRoomDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.sql.Timestamp;
import java.util.List;
import models.Customer;
import models.Cart;
import models.CartService;
import models.CustomerAccount;
import models.TypeRoom;
import utility.Validation;

/**
 *
 * @author Hai Long
 */
@WebServlet(name = "CheckoutOnline", urlPatterns = {"/checkout"})
public class CheckoutOnline extends HttpServlet {

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

        HttpSession session = request.getSession();
        CustomerAccount customerAccount = (CustomerAccount) session.getAttribute("customerInfo");

        Boolean fromCart = (Boolean) session.getAttribute("fromCart");
        Integer attempts = (Integer) session.getAttribute("checkoutAttempts");
        System.out.println("attemps version: " + attempts);
        if (attempts == null) {
            attempts = 0;
        }
        session.setAttribute("checkoutAttempts", attempts);

        if (fromCart != null && fromCart) {
            session.setAttribute("fromCart", false); // reset flag để không tính lần reload sau
        }

        int cartId = Integer.parseInt(request.getParameter("cartId"));

        request.setAttribute("cartId", cartId);

        Cart checkCart = CartDAO.getInstance().checkCart(cartId, customerAccount.getCustomer().getCustomerId());

        if (checkCart == null) {
            CartDAO.getInstance().updateCartToFail(checkCart);
            session.removeAttribute("expireTime-" + cartId);
            request.setAttribute("cartNotTrue", "Đây không phải giỏ hàng của bạn");
            request.getRequestDispatcher("/View/Customer/BookingError.jsp").forward(request, response);
            return;
        }

        Long expireTime = (Long) session.getAttribute("expireTime-" + cartId);

        if (expireTime == null) {
            if (!checkCart.isIsActive()) {
                request.setAttribute("nonActive", "Giỏ hàng không hợp lệ");
                request.getRequestDispatcher("/View/Customer/BookingError.jsp").forward(request, response);
                return;
            }

            if (checkCart.isIsPayment()) {
                request.setAttribute("donePayment", "Giỏ hàng đã được thanh toán");
                request.getRequestDispatcher("/View/Customer/BookingError.jsp").forward(request, response);
                return;
            }

            CartDAO.getInstance().changeRoomNumber(checkCart, checkCart.getStartDate(), checkCart.getEndDate());

            if (checkCart.getRoomNumber() == 0) {
                CartDAO.getInstance().updateCartToFail(checkCart);
                session.removeAttribute("expireTime-" + cartId);
                request.setAttribute("noAvailableRoom", "Loại phòng này tạm thời đã hết phòng");
                request.getRequestDispatcher("/View/Customer/BookingError.jsp").forward(request, response);
                return;
            }

            expireTime = System.currentTimeMillis() + 5 * 60 * 1000;
            session.setAttribute("expireTime-" + cartId, expireTime);
            long currentTimeMillis = System.currentTimeMillis();
            Timestamp sqlTimestamp = new Timestamp(currentTimeMillis);

            checkCart.setPayDay(sqlTimestamp);

            CartDAO.getInstance().updateCartInCheckout(checkCart);
        }

        request.setAttribute("expireTime-" + cartId, expireTime);

        System.out.println("done");

        String service = request.getParameter("service");

        if (service == null) {
            service = "view";
        }

        List<CartService> cartServices = CartServiceDAO.getInstance().getAllCartServiceByCartId(cartId);

        TypeRoom typeRoomInfor = TypeRoomDAO.getInstance().getTypeRoomByRoomNumber(checkCart.getRoomNumber());

        request.setAttribute("typeRoomInfor", typeRoomInfor);

        request.setAttribute("cartInfor", checkCart);

        request.setAttribute("serviceInfor", cartServices);

        if ("view".equals(service)) {

            request.getRequestDispatcher("/View/Customer/CheckoutOnline.jsp").forward(request, response);

        }

        if ("confirmInformation".equals(service)) {

            int timeLeft = Integer.parseInt(request.getParameter("timeLeft-" + cartId));
            System.out.println("timeLeft: " + timeLeft);

            System.out.println("timeLeft: " + timeLeft);

            if (timeLeft == 0) {
                if (attempts > 3) {
                    dal.CustomerDAO.getInstance().deactiveSpam(customerAccount);
                    session.removeAttribute("customerInfo");
                    response.sendRedirect("home");
                    return;
                }
                attempts++;
                session.setAttribute("checkoutAttempts", attempts);
                System.out.println("attemps: " + attempts);
                CartDAO.getInstance().updateCartToFail(checkCart);
                session.removeAttribute("expireTime-" + cartId);
                request.setAttribute("overTime", "Đã quá thời gian thanh toán");
                request.getRequestDispatcher("/View/Customer/BookingError.jsp").forward(request, response);
                return;
            }

            String gender = request.getParameter("gender");
            String fullName = request.getParameter("fullName");
            String email = request.getParameter("email");
            String phone = request.getParameter("phone");

            boolean check = false;

            if (gender == null || gender.isEmpty()) {
                check = true;
                request.setAttribute("genderEmpty", "Vui lòng chọn giới tính");
            }

            if (fullName == null || fullName.isEmpty()) {
                check = true;
                request.setAttribute("fullNameEmpty", "Vui lòng điền họ tên");
            }

            if (email == null || email.isEmpty()) {
                check = true;
                request.setAttribute("emailEmpty", "Vui lòng điền email");
            }

            if (phone == null || phone.isEmpty()) {
                check = true;
                request.setAttribute("phoneEmpty", "Vui lòng điền số điện thoại");
            }

            if (Validation.validateField(request, "phoneError", phone, "PHONE_NUMBER", "SĐT", "SDT không hợp lệ")) {
                check = true;
            }

            if (!check) {
                Customer mainCustomer = new Customer();
                mainCustomer.setGender(gender.equals("male"));
                mainCustomer.setFullName(fullName);
                mainCustomer.setEmail(email);
                mainCustomer.setPhoneNumber(phone);

                System.out.println("main customer: " + mainCustomer);

                session.setAttribute("timeLeft-" + cartId, timeLeft);

                session.setAttribute("cartStatus", "cartPayment");

                session.setAttribute("mainCustomer", mainCustomer);

                session.setAttribute("cartId", cartId);

                response.sendRedirect(request.getContextPath() + "/payment");
            } else {
                request.getRequestDispatcher("/View/Customer/CheckoutOnline.jsp").forward(request, response);
            }
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
