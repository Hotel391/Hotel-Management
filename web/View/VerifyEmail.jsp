<%-- 
    Document   : VerifyEmail
    Created on : Jun 7, 2025, 9:57:58 PM
    Author     : TranTrungHieu
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8" />
        <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
        <title>Verify email</title>
        <!--bootstrap-->
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css" rel="stylesheet"/>
        <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet"/>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/Css/VerifyEmailCss.css"/>
    </head>
    <body>
        <jsp:include page="TopNav.jsp"/>
        <div class="verify-container">
            <h2>Vui lòng xác nhận email của bạn</h2>
            <p>
                Chúng tôi đã gửi một liên kết xác nhận đến địa chỉ email bạn đã đăng ký.<br/>
                Hãy kiểm tra hộp thư đến của bạn (hoặc thư mục spam).
            </p>
            <p class="email">${email}</p>
            <div class="resend-link">
                <span id="resendWrapper">
                    Không nhận được email?
                    <form method="post" action="verifyEmail" style="display: inline;">
                        <input type="hidden" name="email" value="${email}" />
                        <c:if test="${type eq 'reset'}">
                            <input type="hidden" name="type" value="reset" />
                        </c:if>
                        <input type="hidden" name="resend" value="true" />
                        <button type="submit" class="resend-button">Gửi lại</button>
                    </form>
                </span>
                <span id="cooldown" style="display: none; color: gray;">
                    Bạn có thể gửi lại sau <span id="timer">60</span> giây
                </span>
            </div>
        </div>
    </body>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js"></script>
    <script src="${pageContext.request.contextPath}/Js/SetCountDown.js"></script>

</html>
