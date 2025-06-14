<%-- 
    Document   : VerifyEmail
    Created on : Jun 7, 2025, 9:57:58 PM
    Author     : TranTrungHieu
--%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8" />
        <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
        <title>Verify email</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/Css/VerifyEmailCss.css"/>
    </head>
    <body>
        <div class="container">
            <h2>Vui lòng xác nhận email của bạn</h2>
            <p>
                Chúng tôi đã gửi một liên kết xác nhận đến địa chỉ email bạn đã đăng ký.<br/>
                Hãy kiểm tra hộp thư đến của bạn (hoặc thư mục spam).
            </p>
            <p class="email">${email}</p>
            <div class="resend-link">
                <span id="resendWrapper">
                    Không nhận được email? 
                    <a href="verifyEmail?email=${email}&resend=true${type eq 'reset' ? '&type=reset' : ''}">Gửi lại</a>
                </span>
                <span id="cooldown" style="display: none; color: gray;">
                    Bạn có thể gửi lại sau <span id="timer">60</span> giây
                </span>
            </div>
        </div>
    </body>
    <script src="${pageContext.request.contextPath}/Js/SetCountDown.js"></script>

</html>
