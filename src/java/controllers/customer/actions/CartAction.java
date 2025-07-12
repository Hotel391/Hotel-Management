package controllers.customer.actions;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 *
 * @author HieuTT
 */
public interface CartAction {
    void execute(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException;
}