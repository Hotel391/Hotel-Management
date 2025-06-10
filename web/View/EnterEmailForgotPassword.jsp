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
        <link rel="stylesheet" href="${pageContext.request.contextPath}/Css/VerifyEmailCss.css"/>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/Css/Authentication/Register.css"/>
    </head>
    <body>
        <div class="container">
            <h2>Nhập email để đặt lại mật khẩu</h2>
            <form method="post">
                <!-- Email input -->
                <div data-mdb-input-init class="form-outline mb-4">
                    <input type="email" name="email" class="form-control form-control-lg"
                           placeholder="Enter a valid email address" />
                    <label class="form-label">Email address</label>
                    <div class="error-message">${errorEmail}</div>
                </div>

                <button type="submit" data-mdb-ripple-init class="btn btn-primary btn-block mb-4">
                    Reset
                </button>
                <div>
                    Back to login: 
                    <a href="login">
                        <button  type="button" >
                            Login
                        </button>
                    </a>
                </div>
                
            </form>
        </div>
    </body>
    <script
        type="text/javascript"
        src="https://cdn.jsdelivr.net/npm/mdb-ui-kit@9.0.0/js/mdb.umd.min.js"
    ></script>
</html>
