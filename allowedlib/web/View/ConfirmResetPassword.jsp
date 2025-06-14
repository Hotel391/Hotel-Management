<%-- 
    Document   : ConfirmResetPassword
    Created on : Jun 8, 2025, 5:27:56 PM
    Author     : HieuTT
--%>

<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Reset Password</title>
        <link
            href="https://cdn.jsdelivr.net/npm/mdb-ui-kit@9.0.0/css/mdb.min.css"
            rel="stylesheet"
            />
        <link rel="stylesheet" href="${pageContext.request.contextPath}/Css/VerifyEmailCss.css"/>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/Css/Authentication/Register.css"/>
    </head>
    <body>
        <div class="container">
            <c:if test="${success eq 'true'}">
                <h2>Đặt lại mật khẩu</h2>
                <form method="post">
                    <input type="hidden" name="tokenId" value="${tokenId}"/>
                    <div data-mdb-input-init class="form-outline mb-4">
                        <input type="email" name="email" class="form-control form-control-lg" value="${email}" readonly/>
                    </div>

                    <!-- Password input -->
                    <div data-mdb-input-init class="form-outline mb-4">
                        <input type="password" name="password" class="form-control" value="${param.password}"/>
                        <label class="form-label">New Password</label>
                        <div class="error-message">${errorPassword}</div>
                    </div>

                    <!-- Confirm Password input -->
                    <div data-mdb-input-init class="form-outline mb-4">
                        <input type="password" name="confirmPassword" class="form-control" value="${param.confirmPassword}"/>
                        <label class="form-label">Confirm Password</label>
                        <div class="error-message">${errorConfirmPassword}</div>
                    </div>

                    <button type="submit" data-mdb-ripple-init class="btn btn-primary btn-block mb-4">
                        Reset
                    </button>
                </form>
            </c:if>
            <c:if test="${success ne 'true'}">
                <h2>Phiên đặt lại mật khẩu đã hết hạn</h2>
                <p>Bạn có thể đăng nhập tại <a href="login">đây</a>.</p>
            </c:if>
        </div>
    </body>
    <script
        type="text/javascript"
        src="https://cdn.jsdelivr.net/npm/mdb-ui-kit@9.0.0/js/mdb.umd.min.js"
    ></script>
</html>
