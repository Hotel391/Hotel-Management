<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div class="top-navbar">
    <span id="menu-toggle"><i class="bi bi-list"></i></span>
    <h1>${title}</h1>
    <div class="user-profile">
        <img id="user-profile-button" src="${pageContext.request.contextPath}/Image/User.png" alt="User Profile">
        <div class="user-info">
            <c:choose>
                <c:when test="${not empty sessionScope.username}">
                    <a href="${pageContext.request.contextPath}/customer/customerProfile?service=info&username=${sessionScope.username}" class="text-decoration-none text-reset">
                        <i class="bi bi-person-circle"></i> Profile
                    </a><br>
                    <a href="#" class="text-decoration-none text-reset">
                        <i class="bi bi-box-arrow-right"></i>Logout</a>
                </c:when>
                <c:otherwise>
                    <span><i class="bi bi-person-circle"></i> Profile</span><br>
                    <span><i class="bi bi-box-arrow-right"></i> Logout</span><br>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</div>
