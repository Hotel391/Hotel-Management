<%-- 
    Document   : BookingError
    Created on : Jul 11, 2025, 4:25:26 PM
    Author     : Hai Long
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/Css/Customer/BookingError.css"/>
    </head>
    <body>
        <div class="header">
            <div class="header-content">
                <div style="display: flex; align-items: center; gap: 10px;">
                    <div class="logo">
                        <img src="${pageContext.request.contextPath}/Image/Logo.png" alt="FPT Hotel Logo"/>
                        FPT Hotel
                    </div>
                    <!--                    <div class="dots">
                                            <div class="dot"></div>
                                            <div class="dot"></div>
                                            <div class="dot"></div>
                                            <div class="dot"></div>
                                        </div>-->
                </div>
                <button class="logout-btn">Đăng xuất</button>
            </div>
        </div>
        <div class="error-content">
            <div class="error-container">

                <div class="error-icon"></div>

                <h1 class="error-title">Đặt phòng thất bại!</h1>

                <div class="error-message">
                    <c:if test="${not empty cartNotTrue}">
                        ${cartNotTrue}
                    </c:if>
                    <c:if test="${not empty noAvailableRoom}">
                        ${noAvailableRoom}
                    </c:if>
                </div>

                <div class="button-group">
                    <a href="${pageContext.request.contextPath}/cart" class="btn btn-primary" onclick="showLoading(this, event)">
                        <span class="loading-spinner"></span>
                        <span class="btn-text">🛒 Quay lại giỏ hàng</span>
                    </a>
                </div>
            </div>
        </div>
    </body>
</html>
