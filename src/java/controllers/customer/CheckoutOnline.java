/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controllers.customer;

import dal.CartDAO;
import dal.CartServiceDAO;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import models.Customer;
import models.Cart;
import models.CartService;
import models.TypeRoom;

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

        String service = request.getParameter("service");

        if (service == null) {
            service = "view";
        }

        if ("view".equals(service)) {

//            int cartId = Integer.parseInt(request.getParameter("cartId"));
            int cartId = 4;

            Cart checkCart = CartDAO.getInstance().getCartByCartId(cartId);

            System.out.println("checkCart: " + checkCart);

//            Customer customer = (Customer) session.getAttribute("customerInfo");
            Customer customer = CustomerDAO.getInstance().getCustomerByCustomerID(46);

            List<Cart> cartOfCustomer = CartDAO.getInstance().getCartByCustomerId(customer.getCustomerId());

            boolean checkTrueCart = true;
            for (Cart cart : cartOfCustomer) {
                if (cart.getCartId() == checkCart.getCartId()) {
                    System.out.println(cart);
                    checkTrueCart = false;
                    System.out.println(checkTrueCart);
                }
            }

            if (checkTrueCart == true) {
                System.out.println("cartNotTrue");
                request.setAttribute("cartNotTrue", "Đây không phải giỏ hàng của bạn");
                request.getRequestDispatcher("").forward(request, response);
                return;
            }
            System.out.println("status cart before: " + checkCart.isIsActive());
            if (!CartDAO.getInstance().checkRoomNumberStatus(checkCart.getRoomNumber(),
                    checkCart.getStartDate(), checkCart.getEndDate())) {
                System.out.println("number conflict");
                CartDAO.getInstance().handleRoomNumberConflict(checkCart, checkCart.getStartDate(), checkCart.getEndDate());
            }
            System.out.println("status cart after: " + checkCart.isIsActive());
            if (!checkCart.isIsActive()) {
            System.out.println("1");
                request.setAttribute("noAvailableRoom", "Loại phòng này tạm thời đã hết phòng");
                request.getRequestDispatcher("").forward(request, response);
                return;
            }

            List<CartService> cartServices = CartServiceDAO.getInstance().getAllCartServiceByCartId(cartId);

            TypeRoom typeRoomInfor = TypeRoomDAO.getInstance().getTypeRoomByRoomNumber(checkCart.getRoomNumber());

            request.setAttribute("typeRoomInfor", typeRoomInfor);
            
            request.setAttribute("cartInfor", checkCart);
            
            System.out.println("services: " + cartServices);

            request.setAttribute("serviceInfor", cartServices);
            
            System.out.println("cart before sending: " + checkCart);
            
            System.out.println("====================================");

            request.getRequestDispatcher("/View/Customer/CheckoutOnline.jsp").forward(request, response);

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
