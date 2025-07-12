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

        //int cartId = Integer.parseInt(request.getParameter("cartId"));
        int cartId = 4;

        request.setAttribute("cartId", cartId);
        Cart checkCart = CartDAO.getInstance().getCartByCartId(cartId);

        Long expireTime = (Long) session.getAttribute("expireTime");

        if (expireTime == null) {

            //check room available
            CartDAO.getInstance().handleRoomNumberConflict(checkCart, checkCart.getStartDate(), checkCart.getEndDate());

            if (!checkCart.isIsActive()) {
                session.removeAttribute("expireTime");
                request.setAttribute("noAvailableRoom", "Loại phòng này tạm thời đã hết phòng");
                request.getRequestDispatcher("/View/Customer/BookingError.jsp").forward(request, response);
                return;
            }
            
            //set payment time limitation
            expireTime = System.currentTimeMillis() + 3 * 60 * 1000;
            session.setAttribute("expireTime", expireTime);
            long currentTimeMillis = System.currentTimeMillis();
            Timestamp sqlTimestamp = new Timestamp(currentTimeMillis);
            checkCart.setPayDay(sqlTimestamp);

            CartDAO.getInstance().updateCartInCheckout(checkCart);
        }

        request.setAttribute("expireTime", expireTime);

        String service = request.getParameter("service");

        if (service == null) {
            service = "view";
        }

//            Customer customer = (Customer) session.getAttribute("customerInfo");
        CustomerAccount customerAccount = CustomerAccountDAO.getInstance().getCustomerAccountById(46);

        List<Cart> cartOfCustomer = CartDAO.getInstance().getCartByCustomerId(customerAccount.getCustomer().getCustomerId());

        boolean checkTrueCart = true;
        for (Cart cart : cartOfCustomer) {
            if (cart.getCartId() == checkCart.getCartId()) {
                System.out.println(cart);
                checkTrueCart = false;
                System.out.println(checkTrueCart);
            }
        }

        if (checkTrueCart == true) {
            CartDAO.getInstance().updateCartOverTime(checkCart);
            session.removeAttribute("expireTime");
            request.setAttribute("cartNotTrue", "Đây không phải giỏ hàng của bạn");
            request.getRequestDispatcher("/View/Customer/BookingError.jsp").forward(request, response);
            return;
        }

        List<CartService> cartServices = CartServiceDAO.getInstance().getAllCartServiceByCartId(cartId);

        TypeRoom typeRoomInfor = TypeRoomDAO.getInstance().getTypeRoomByRoomNumber(checkCart.getRoomNumber());

        request.setAttribute("typeRoomInfor", typeRoomInfor);

        request.setAttribute("cartInfor", checkCart);

        request.setAttribute("serviceInfor", cartServices);

        if ("view".equals(service)) {

            System.out.println("services: " + cartServices);

            System.out.println("cart before sending: " + checkCart);

            System.out.println("====================================");

            request.getRequestDispatcher("/View/Customer/CheckoutOnline.jsp").forward(request, response);

        }

        if ("confirmInformation".equals(service)) {

            long timeLeft = Long.parseLong(request.getParameter("timeLeft"));
            
            System.out.println("timeLeft: " + timeLeft);

            if (timeLeft == 0) {
                CartDAO.getInstance().updateCartOverTime(checkCart);
                session.removeAttribute("expireTime");
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

                session.setAttribute("timeLeft", timeLeft);

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
