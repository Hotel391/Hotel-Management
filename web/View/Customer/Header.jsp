<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<header class="header">
    <div class="header_content d-flex flex-row align-items-center justify-content-between px-4 py-2" style="background-color: #ffffff; border-bottom: 1px solid #ddd;">

        <!-- Logo + Tên khách sạn -->
        <div class="d-flex flex-row align-items-center">
            <img src="${pageContext.request.contextPath}/Image/Logo.png" alt="Logo" style="height: 50px; margin-right: 15px;">
            <div style="font-family: 'Playfair Display', serif; font-size: 24px; color: #003366;">FPTHotel</div>
        </div>

        <!-- Menu điều hướng -->
        <nav class="main_nav mx-4">
            <ul class="d-flex flex-row align-items-center mb-0" style="list-style: none; gap: 25px;">
                <li><a href="home" class="text-dark text-decoration-none fw-semibold">Trang chủ</a></li>
                <li><a href="about.html" class="text-dark text-decoration-none fw-semibold">Giới thiệu</a></li>
                <li><a href="searchRoom" class="text-dark text-decoration-none fw-semibold">Phòng</a></li>
                <li><a href="contact.html" class="text-dark text-decoration-none fw-semibold">Liên hệ</a></li>
            </ul>
        </nav>
        <c:if test="${sessionScope.customerInfo != null}">
            <div class="d-flex flex-row align-items-center gap-4">

                <div class="cart-icon d-flex align-items-center" style="cursor: pointer;">
                    <span style="position: relative; display: inline-block;">
                        <i class="fa fa-shopping-cart" style="font-size: 22px; color: #DAA520;"></i>
                        <c:if test="${not empty sessionScope.cartItemCount}">
                            <span style="position: absolute; top: -10px; right: -10px; background: #ff4d4f; color: #fff; font-size: 12px; border-radius: 50%; padding: 2px 6px; font-weight: bold; min-width: 20px; text-align: center;">
                                ${sessionScope.cartItemCount}
                            </span>
                        </c:if>
                    <span class="text-dark" style="margin-right: 10px; margin-left: 10px;">Giỏ hàng</span>
                </div>

                <div class="user-info d-flex align-items-center" style="cursor: pointer;">
                    <img src="${pageContext.request.contextPath}/Image/User.png" alt="User" style="width: 32px; height: 32px; border-radius: 50%; margin-right: 8px;">
                    <span class="text-dark" style="margin-right: 10px;">Nguyễn Văn A</span>
                </div>

                <!-- Nút CTA -->
                <a href="searchRoom" class="btn" style="background-color: #DAA520; color: white; padding: 10px 16px; border-radius: 4px; font-weight: bold;">
                    Đặt phòng ngay
                </a>
            </div>
        </c:if>
        <c:if test="${sessionScope.customerInfo == null}">
            <div class="d-flex flex-row align-items-center">
                <!-- Nút Đăng nhập -->
                <a href="login" 
                   class="btn btn-outline-dark fw-semibold px-3 py-2"
                   style="border-radius: 20px 0 0 20px; transition: background-color 0.3s, color 0.3s;">
                    Đăng nhập
                </a>

                <!-- Nút Đăng ký -->
                <a href="register" 
                   class="btn fw-bold px-4 py-2"
                   style="background-color: #DAA520; color: white; border-radius: 0 20px 20px 0; transition: background-color 0.3s;">
                    Đăng ký
                </a>
            </div>
        </c:if>

        <!-- Menu Mobile -->
        <div class="hamburger d-lg-none ms-3">
            <i class="fa fa-bars fa-lg text-dark" aria-hidden="true"></i>
        </div>
    </div>
</header>