<%-- 
    Document   : EnterEmailForgotPassword
    Created on : Jun 8, 2025, 4:40:11 PM
    Author     : HieuTT
--%>

<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Enter email</title>
        <link
            href="https://cdn.jsdelivr.net/npm/mdb-ui-kit@9.0.0/css/mdb.min.css"
            rel="stylesheet"
            />
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css" rel="stylesheet"/>
        <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet"/>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/Css/VerifyEmailCss.css"/>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/Css/Authentication/ResetPassword.css"/>
    </head>
    <body>
        <jsp:include page="TopNav.jsp"/>
        <div class="verify-container">
            <h2>Nhập email để đặt lại mật khẩu</h2>
            <form method="post">
                <!-- Email input -->
                <div data-mdb-input-init class="form-outline mb-4">
                    <input type="email" name="email" class="form-control form-control-lg"
                           placeholder="Enter a valid email address" value="${param.email}" require/>
                    <label class="form-label">Email address</label>
                    <div class="error-message">${errorEmail}</div>
                </div>

                <button type="submit" data-mdb-ripple-init class="btn btn-primary btn-block mb-4">
                    Reset
                </button>
            </form>
        </div>
    </body>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js"></script>
    <script
        type="text/javascript"
        src="https://cdn.jsdelivr.net/npm/mdb-ui-kit@9.0.0/js/mdb.umd.min.js"
    ></script>
</html>
