<%--
    Author     : SONNAM
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page import="models.CustomerAccount"%>
<%
    CustomerAccount ca = (CustomerAccount) request.getAttribute("customerAccount");
    String fullName = (ca != null && ca.getCustomer() != null) ? ca.getCustomer().getFullName() : "A";
    String initial = fullName.length() > 0 ? fullName.substring(0, 1).toUpperCase() : "A";
    request.setAttribute("initialLetter", initial);
    request.setAttribute("activeTab", request.getParameter("type") != null ? request.getParameter("type") : "profile");
%>

<!-- Sidebar Avatar + Info -->
<div class="setting-card text-center mb-4 p-4">
    <div class="sidebar-avatar-circle mx-auto mb-3">${initialLetter}</div>
    <h6 class="mb-1 fw-bold">${customerAccount.customer.fullName}</h6>
    <p class="text-muted">${customerAccount.username}</p>
</div>

<!-- Sidebar Navigation -->
<div class="setting-card p-3">
    <a href="${pageContext.request.contextPath}/customerProfile?service=info&type=profile&username=${customerAccount.username}"
       class="sidebar-link d-block py-2 px-3 rounded ${activeTab == 'profile' ? 'active-link' : ''}">
        <i class="bi bi-person me-2"></i> Thông tin tài khoản
    </a>
    <a href="${pageContext.request.contextPath}/customerProfile?service=changePass&type=changePass&username=${customerAccount.username}"
       class="sidebar-link d-block py-2 px-3 rounded ${activeTab == 'changePass' ? 'active-link' : ''}">
        <i class="bi bi-key me-2"></i> Đổi mật khẩu
    </a>
    <a href="${pageContext.request.contextPath}/customerProfile?service=changeUserName&type=changeUserName&username=${customerAccount.username}"
       class="sidebar-link d-block py-2 px-3 rounded ${activeTab == 'changeUserName' ? 'active-link' : ''}">
        <i class="bi bi-person-vcard me-2"></i> Đổi tên đăng nhập
    </a>
    <a href="${pageContext.request.contextPath}/customerProfile?service=booking&type=booking&username=${customerAccount.username}&status=upcoming"
       class="sidebar-link d-block py-2 px-3 rounded ${activeTab == 'booking' ? 'active-link' : ''}">
        <i class="bi bi-calendar3 me-2"></i> Đặt phòng của tôi
    </a>
    <a href="${pageContext.request.contextPath}/login?service=logout"
       class="sidebar-link d-block py-2 px-3 rounded text-danger">
        <i class="bi bi-box-arrow-right me-2"></i> Đăng xuất
    </a>
</div>

<style>
    .sidebar-avatar-circle {
        width: 100px;
        height: 100px;
        background-color: #0071dc;
        color: white;
        border-radius: 50%;
        display: flex;
        align-items: center;
        justify-content: center;
        font-size: 36px;
        font-weight: bold;
    }

    .setting-card {
        background-color: #fff;
        border-radius: 12px;
        box-shadow: 0 0 8px rgba(0, 0, 0, 0.05);
    }

    .sidebar-link {
        text-decoration: none;
        color: #0071dc;
        font-weight: 500;
        transition: background-color 0.2s;
    }

    .sidebar-link:hover {
        background-color: #f0f8ff;
    }

    .active-link {
        background-color: #e7f1ff;
        font-weight: bold;
    }

    .sidebar-link i {
        font-size: 1rem;
    }
</style>
