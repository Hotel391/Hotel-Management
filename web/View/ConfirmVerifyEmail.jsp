<%-- 
    Document   : VerifyEmail
    Created on : Jun 7, 2025, 9:57:58 PM
    Author     : TranTrungHieu
--%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Xác nhận thành công</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css" rel="stylesheet"/>
        <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet"/>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/Css/VerifyEmailCss.css"/>
    </head>
    <body>
        <jsp:include page="TopNav.jsp"/>
        <div class="verify-container">
            <c:if test="${success eq 'true'}">
                <h2>Email đã được xác nhận thành công<i class="bi bi-check2-square" style="color: green;"></i></h2>
                <p>Bạn có thể đăng nhập tại <a href="login">đây</a>.</p>
            </c:if>
            <c:if test="${success ne 'true'}">
                <h2>Email đã được xác nhận thất bại<i class="bi bi-x-circle-fill" style="color: red"></i></h2>
                <p>Phiên xác nhận đã hết hạn, vui lòng gửi lại.</p>
                <p>Bạn có thể quay lại đăng ký tại <a href="register">đây</a>.</p>
            </c:if>
        </div>
    </body>
</html>