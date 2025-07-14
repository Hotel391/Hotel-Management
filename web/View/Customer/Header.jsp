<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%
    String fullName = (session.getAttribute("customerInfo") != null)
        ? ((models.CustomerAccount) session.getAttribute("customerInfo")).getCustomer().getFullName()
        : "A";
    String initialLetter = fullName != null && !fullName.isEmpty() ? fullName.substring(0, 1).toUpperCase() : "A";
%>

<style>
    .avatar-circle {
        width: 32px;
        height: 32px;
        background-color: #f1c40f;
        color: #1a2a44;
        font-weight: bold;
        font-size: 16px;
        border-radius: 50%;
        display: flex;
        align-items: center;
        justify-content: center;
        border: 2px solid #f1c40f;
    }
</style>

<header class="header">
    <div class="header_content d-flex flex-row align-items-center justify-content-between px-4 py-2" style="font-size: 16px;">
        <!-- Logo + Tên khách sạn -->
        <div class="d-flex flex-row align-items-center" style="gap: 10px;">
            <img src="${pageContext.request.contextPath}/Image/Logo.png" alt="Logo"
                 style="height: 50px; background-color: transparent;">
            <div id="hotel-name" style="font-family: 'Playfair Display', serif; font-size: 24px; color: white; font-weight: bold;">
                FPTHotel
            </div>
        </div>

        <!-- Menu điều hướng -->
        <nav class="main_nav mx-4">
            <ul class="d-flex flex-row align-items-center mb-0" style="list-style: none; gap: 25px;">
                <li><a href="home" class="text-white text-decoration-none fw-semibold">Trang chủ</a></li>
                <li><a href="about" class="text-white text-decoration-none fw-semibold">Giới thiệu</a></li>
                <li><a href="searchRoom" class="text-white text-decoration-none fw-semibold">Phòng</a></li>
                <li><a href="contact.html" class="text-white text-decoration-none fw-semibold">Liên hệ</a></li>
            </ul>
        </nav>

        <!-- Nếu đã đăng nhập -->
        <c:if test="${sessionScope.customerInfo != null}">
            <div class="d-flex flex-row align-items-center" style="gap: 24px;">
                <!-- Giỏ hàng -->
                <div class="cart-icon d-flex align-items-center" style="cursor: pointer;" onclick="window.location.href = 'cart'">
                    <span style="position: relative; display: inline-block;">
                        <i class="fa fa-shopping-cart" style="font-size: 22px; color: #f1c40f;"></i>
                        <c:if test="${not empty sessionScope.cartItemCount}">
                            <span style="position: absolute; top: -10px; right: -10px; background: #ff4d4f; color: #fff; font-size: 12px; border-radius: 50%; padding: 2px 6px; font-weight: bold; min-width: 20px; text-align: center;">
                                ${sessionScope.cartItemCount}
                            </span>
                        </c:if>
                    </span>
                    <span class="text-white ms-2">Giỏ hàng</span>
                </div>

                <!-- User Dropdown -->
                <div class="user_dropdown d-flex align-items-center position-relative">
                    <button id="user-profile-button" class="btn p-0 border-0 bg-transparent d-flex align-items-center">
                        <div class="avatar-circle text-center"><%= initialLetter %></div>
                        <span id="user-fullname" class="ms-2 text-white fw-semibold" data-username="${sessionScope.customerInfo.customer.fullName}">
                            ${sessionScope.customerInfo.customer.fullName}
                        </span>
                    </button>
                    <div class="user_dropdown_menu position-absolute"
                         style="display: none; top: 100%; right: 0; background: rgba(0, 0, 0, 0.85); border-radius: 8px; min-width: 180px;
                         box-shadow: 0 4px 15px rgba(0,0,0,0.2); z-index: 1000;">
                        <a href="${pageContext.request.contextPath}/customerProfile?service=info&username=${sessionScope.customerInfo.username}"
                           class="dropdown-item d-block px-3 py-2 text-white text-decoration-none">My Profile</a>
                        <a href="${pageContext.request.contextPath}/login?service=logout"
                           class="dropdown-item d-block px-3 py-2 text-white text-decoration-none">Logout</a>
                    </div>
                </div>

                <!-- Nút CTA -->
                <a href="searchRoom" class="btn"
                   style="background-color: #DAA520; color: white; padding: 10px 20px; border-radius: 6px; font-weight: bold;">
                    Đặt phòng ngay
                </a>
            </div>
        </c:if>

        <!-- Nếu chưa đăng nhập -->
        <c:if test="${sessionScope.customerInfo == null}">
            <div class="d-flex flex-row align-items-center" style="gap: 12px;">
                <a href="login" class="btn btn-outline-light fw-semibold px-3 py-2"
                   style="background-color: #DAA520; border-radius: 20px 0 0 20px;">Đăng nhập</a>
                <a href="register" class="btn btn-outline-light fw-semibold px-3 py-2"
                   style="background-color: #f1c40f; color: #1a2a44; border-radius: 0 20px 20px 0;">Đăng ký</a>
            </div>
        </c:if>

        <!-- Mobile -->
        <div class="hamburger d-lg-none ms-3">
            <i class="fa fa-bars fa-lg text-white" aria-hidden="true"></i>
        </div>
    </div>
</header>

<!-- JS cho dropdown và màu header -->
<script>
    document.addEventListener("DOMContentLoaded", function () {
        const path = window.location.pathname.toLowerCase();
        const isWhitePage = path.includes("home") || path.includes("login") || path.includes("register") || path.includes("about") || path === "/";

        if (!isWhitePage) {
            document.querySelectorAll('.main_nav a').forEach(link => {
                link.classList.remove('text-white');
                link.classList.add('text-dark');
            });
            const hotelName = document.getElementById("hotel-name");
            if (hotelName)
                hotelName.style.setProperty("color", "#003366", "important");

            document.querySelectorAll('.header .text-white').forEach(el => {
                el.classList.remove('text-white');
                el.classList.add('text-dark');
            });

            const cartIcon = document.querySelector('.fa-shopping-cart');
            if (cartIcon)
                cartIcon.style.color = '#333';

            const burger = document.querySelector('.hamburger i');
            if (burger)
                burger.classList.remove('text-white');

            const userImg = document.querySelector('#user-profile-button img');
            if (userImg)
                userImg.style.border = '2px solid #003366';
        }

        // Dropdown user
        const userBtn = document.getElementById("user-profile-button");
        const userMenu = document.querySelector(".user_dropdown_menu");
        if (userBtn && userMenu) {
            userBtn.addEventListener("click", function (e) {
                e.stopPropagation();
                const isVisible = userMenu.style.display === "block";
                userMenu.style.display = isVisible ? "none" : "block";
            });

            document.addEventListener("click", function (e) {
                if (!userBtn.contains(e.target) && !userMenu.contains(e.target)) {
                    userMenu.style.display = "none";
                }
            });
        }
    });
</script>
