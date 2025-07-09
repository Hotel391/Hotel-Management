<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<header class="header">
    <div class="header-content d-flex flex-row align-items-center justify-content-between px-5 py-3" style="background: linear-gradient(90deg, #1a2a44, #2e4057); box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);">

        <!-- Logo + Hotel Name -->
        <div class="brand d-flex flex-row align-items-center">
            <img src="${pageContext.request.contextPath}/Image/Logo.png" alt="FPTHotel Logo" style="height: 60px; margin-right: 20px; filter: drop-shadow(0 0 5px rgba(255, 215, 0, 0.3));">
            <h1 style="font-family: 'Playfair Display', serif; font-size: 28px; color: #f1c40f; margin: 0; text-shadow: 1px 1px 3px rgba(0, 0, 0, 0.2);">FPTHotel</h1>
        </div>

        <!-- Navigation Menu -->
        <nav class="main-nav">
            <ul class="nav-links d-flex flex-row align-items-center mb-0" style="list-style: none; gap: 30px;">
                <li><a href="${pageContext.request.contextPath}/home" class="nav-link text-white text-decoration-none fw-medium" style="transition: color 0.3s;">Trang chủ</a></li>
                <li><a href="${pageContext.request.contextPath}/about" class="nav-link text-white text-decoration-none fw-medium" style="transition: color 0.3s;">Giới thiệu</a></li>
                <li><a href="${pageContext.request.contextPath}/searchRoom" class="nav-link text-white text-decoration-none fw-medium" style="transition: color 0.3s;">Phòng</a></li>
                <li><a href="${pageContext.request.contextPath}/contact" class="nav-link text-white text-decoration-none fw-medium" style="transition: color 0.3s;">Liên hệ</a></li>
            </ul>
        </nav>

        <c:if test="${sessionScope.customerInfo != null}">
            <div class="user-section d-flex flex-row align-items-center gap-4">
                <div class="cart-icon d-flex align-items-center" style="cursor: pointer;" onclick="window.location.href = '${pageContext.request.contextPath}/cart'">
                    <span style="position: relative; display: inline-block;">
                        <i class="fa fa-shopping-cart" style="font-size: 24px; color: #f1c40f;"></i>
                        <c:if test="${not empty sessionScope.cartItemCount}">
                            <span style="position: absolute; top: -10px; right: -10px; background: #e74c3c; color: #fff; font-size: 12px; border-radius: 50%; padding: 2px 6px; font-weight: bold; min-width: 20px; text-align: center;">
                                ${sessionScope.cartItemCount}
                            </span>
                        </c:if>
                    </span>
                    <span class="text-white" style="margin-left: 10px; font-size: 14px;">Giỏ hàng</span>
                </div>

                <div class="user-info d-flex align-items-center" style="cursor: pointer; position: relative;">
                    <button id="user-profile-button" class="btn p-0 border-0" style="background: none;">
                        <img src="${pageContext.request.contextPath}/Image/User.png" alt="User" style="width: 40px; height: 40px; border-radius: 50%; border: 2px solid #f1c40f; margin-right: 10px; transition: transform 0.3s;">
                        <span class="text-white" style="font-size: 14px;">${sessionScope.customerInfo.customer.fullName}</span>
                    </button>
                    <div class="user-dropdown" style="display: none; position: absolute; top: 100%; right: 0; background: rgba(255, 255, 255, 0.9); backdrop-filter: blur(10px); border: 1px solid rgba(241, 196, 15, 0.2); border-radius: 8px; padding: 10px; box-shadow: 0 4px 15px rgba(0, 0, 0, 0.2); z-index: 1001; min-width: 180px;">
                        <a href="${pageContext.request.contextPath}/customerProfile?service=info&username=${sessionScope.customerInfo.username}" class="dropdown-item text-dark text-decoration-none d-block py-2" style="font-size: 14px;">My Profile</a>
                        <a href="${pageContext.request.contextPath}/login?service=logout" class="dropdown-item text-dark text-decoration-none d-block py-2" style="font-size: 14px;">Logout</a>
                    </div>
                </div>

                <a href="${pageContext.request.contextPath}/searchRoom" class="btn cta-btn" style="background: linear-gradient(45deg, #f1c40f, #e67e22); color: #1a2a44; padding: 12px 24px; border-radius: 25px; font-weight: bold; font-size: 14px; text-transform: uppercase; transition: transform 0.3s, box-shadow 0.3s;">
                    Đặt phòng ngay
                </a>
            </div>
        </c:if>
        <c:if test="${sessionScope.customerInfo == null}">
            <div class="auth-section d-flex flex-row align-items-center">
                <a href="${pageContext.request.contextPath}/login" class="btn btn-outline-light fw-semibold px-4 py-2" style="border-radius: 20px 0 0 20px; border-color: #f1c40f; color: #f1c40f; transition: background-color 0.3s, color 0.3s;">
                    Đăng nhập
                </a>
                <a href="${pageContext.request.contextPath}/register" class="btn btn-primary fw-bold px-4 py-2" style="background: #f1c40f; color: #1a2a44; border-radius: 0 20px 20px 0; transition: background-color 0.3s;">
                    Đăng ký
                </a>
            </div>
        </c:if>

        <!-- Mobile Menu -->
        <div class="hamburger d-lg-none ms-3">
            <i class="fa fa-bars fa-lg text-white" aria-hidden="true"></i>
        </div>
    </div>
</header>

<script>
    document.addEventListener("DOMContentLoaded", function () {
        const userBtn = document.getElementById("user-profile-button");
        const userProfile = document.querySelector(".user-dropdown");

        if (userBtn && userProfile) {
            console.log("Elements found, attaching event listeners...");
            userBtn.addEventListener('click', (event) => {
                event.preventDefault();
                const isVisible = userProfile.classList.toggle("visible");
                userProfile.style.display = isVisible ? "block" : "none";
                userBtn.querySelector('img').style.transform = isVisible ? "scale(1.1)" : "scale(1)";
                console.log("Toggle clicked, visible class:", isVisible);
            });

            document.addEventListener('click', function (event) {
                if (!userBtn.contains(event.target) && !userProfile.contains(event.target)) {
                    userProfile.classList.remove("visible");
                    userProfile.style.display = "none";
                    userBtn.querySelector('img').style.transform = "scale(1)";
                    console.log("Clicked outside, hiding dropdown.");
                }
            });
        } else {
            console.log("Error: user-profile-button or user-dropdown not found.");
        }
    });
</script>

<style>
    .nav-links .nav-link:hover {
        color: #f1c40f !important;
    }
    .user-info .user-dropdown {
        display: none;
        opacity: 0;
        transition: opacity 0.3s ease, transform 0.3s ease;
        transform: translateY(-10px);
    }
    .user-info .user-dropdown.visible {
        display: block;
        opacity: 1;
        transform: translateY(0);
    }
    .user-info .user-dropdown a.dropdown-item:hover {
        background-color: rgba(241, 196, 15, 0.1);
        color: #1a2a44;
    }
    .cta-btn:hover {
        transform: scale(1.05);
        box-shadow: 0 4px 15px rgba(241, 196, 15, 0.4);
    }
    .auth-section .btn-outline-light:hover {
        background-color: #f1c40f;
        color: #1a2a44;
    }
    .auth-section .btn-primary:hover {
        background-color: #e67e22;
    }
    @media (max-width: 992px) {
        .main-nav, .user-section, .auth-section {
            display: none;
        }
        .hamburger {
            display: block !important;
        }
    }
</style>