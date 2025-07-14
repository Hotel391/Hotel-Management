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
<div class="setting-card text-center mb-4">
    <div class="sidebar-avatar-circle mx-auto">${initialLetter}</div>
    <h6 class="mt-3 mb-1">${customerAccount.customer.fullName}</h6>
    <p class="text-muted mb-0">${customerAccount.username}</p>
</div>

<!-- Sidebar Navigation -->
<div class="setting-card">
    <a href="${pageContext.request.contextPath}/customerProfile?service=info&username=${customerAccount.username}"
       class="sidebar-link ${activeTab == 'profile' ? 'active' : ''}">
        <i class="bi bi-person me-2"></i> Thông tin tài khoản
    </a>
    <a href="${pageContext.request.contextPath}/customerProfile?service=changePass&type=changePass&username=${customerAccount.username}"
       class="sidebar-link ${activeTab == 'changePass' ? 'active' : ''}">
        <i class="bi bi-key me-2"></i> Đổi mật khẩu
    </a>
    <a href="${pageContext.request.contextPath}/customerProfile?service=changeUserName&type=changeUserName&username=${customerAccount.username}"
       class="sidebar-link ${activeTab == 'changeUserName' ? 'active' : ''}">
        <i class="bi bi-person-vcard me-2"></i> Đổi tên đăng nhập
    </a>
    <a href="${pageContext.request.contextPath}/customerProfile?service=booking&type=booking&username=${customerAccount.username}&status=upcoming"
       class="sidebar-link ${activeTab == 'booking' ? 'active' : ''}">
        <i class="bi bi-calendar3 me-2"></i> Đặt phòng của tôi
    </a>

    <a href="${pageContext.request.contextPath}/login?service=logout" class="sidebar-link text-danger">
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

</style>