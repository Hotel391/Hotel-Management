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
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css" rel="stylesheet"/>
        <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet"/>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/Css/VerifyEmailCss.css"/>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/Css/Authentication/ResetPassword.css"/>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/Css/toggle-password.css"/>
    </head>
    <body>
        <jsp:include page="/View/Customer/Header.jsp" />
        <div class="verify-container">
            <c:if test="${success eq 'true'}">
                <h2>Đặt lại mật khẩu</h2>
                <form method="post">
                    <input type="hidden" name="tokenId" value="${tokenId}"/>
                    <input type="hidden" name="oldPassword" value="${oldPassword}"/>
                    <div data-mdb-input-init class="form-outline mb-4">
                        <input type="email" name="email" class="form-control form-control-lg" value="${email}" readonly/>
                    </div>

                    <!-- Password input -->
                    <div  data-mdb-input-init class="form-outline mb-4 position-relative">
                        <input type="password" name="password" id="password" class="form-control" value="${param.password}" required />
                        <label class="form-label">New Password</label>
                        <i class="far fa-eye toggle-password" toggle="#password"></i>
                        <div class="error-message">${errorPassword}</div>
                    </div>

                    <!-- Confirm Password input -->
                    <div  data-mdb-input-init class="form-outline mb-4 position-relative">
                        <input type="password" name="confirmPassword" id="confirmPassword" class="form-control" value="${param.confirmPassword}" required />
                        <label class="form-label">Confirm Password</label>
                        <i class="far fa-eye toggle-password" toggle="#confirmPassword"></i>
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
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js"></script>
    <script
        type="text/javascript"
        src="https://cdn.jsdelivr.net/npm/mdb-ui-kit@9.0.0/js/mdb.umd.min.js"
    ></script>
    <script src="${pageContext.request.contextPath}/Js/toggle-password.js"></script>
</html>
