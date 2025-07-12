<%-- 
    Document   : CheckoutOnline
    Created on : Jul 10, 2025, 4:12:23 PM
    Author     : Hai Long
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html>
<html>
    <%
        long expireTime = (long) request.getAttribute("expireTime");
    %>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/Css/Customer/CheckoutOnline.css"/>
    </head>
    <body>
        <div class="header">
            <div class="header-content">
                <div style="display: flex; align-items: center; gap: 10px;">
                    <div class="logo">
                        <img src="${pageContext.request.contextPath}/Image/Logo.png" alt="FPT Hotel Logo"/>
                        FPT Hotel
                    </div>
                </div>
                <button class="login-btn">Đăng nhập</button>
            </div>
        </div>

        <div class="container">
            <div class="progress-bar">
                <div class="countdown">
                    <div class="countdown-text">Chúng tôi đang giữ phòng cho quý khách...</div>
                    <div class="countdown-timer" id="countdown-timer"></div>
                </div>
            </div>

            <div class="main-content">

                <div class="booking-form">
                    <form action="${pageContext.request.contextPath}/checkout" id="checkout-form" method="post">
                        <h2 class="form-title">Thông tin khách hàng</h2>
                        <div class="required-text">*Mục bắt buộc</div>
                        <div class="warning-text">( Trong trường hợp đặt phòng hộ, 
                            quý khách vui lòng điền đúng thông tin của người sẽ đến ở )</div>

                        <div class="form-row">
                            <div class="form-group">
                                <input type="radio" name="gender" value="male" ${param.gender.equals("male") ? "checked" : ''}>
                                <label>Anh</label>
                            </div>
                            <div class="form-group">
                                <input type="radio" name="gender" value="female" ${param.gender.equals("female") ? "checked" : ''}>
                                <label>Chị</label>
                            </div>
                        </div>
                        <c:if test="${not empty genderEmpty}">
                            <p class="warning-text">${genderEmpty}</p>
                        </c:if>
                        <div class="form-row">
                            <div class="form-group">
                                <label class="form-label">Họ Tên *</label>
                                <input type="text" name="fullName" value="${param.fullName}" class="form-input" placeholder="Nhập họ tên">
                                <c:if test="${not empty fullNameEmpty}">
                                    <p class="warning-text">${fullNameEmpty}</p>
                                </c:if>
                            </div>
                        </div>
                        <div class="form-row">
                            <div class="form-group">
                                <label class="form-label">Email ID *</label>
                                <input type="email" name="email" value="${param.email}" class="form-input" placeholder="Nhập email">
                                <c:if test="${not empty emailEmpty}">
                                    <p class="warning-text">${emailEmpty}</p>
                                </c:if>
                            </div>
                            <div class="form-group">
                                <label class="form-label">Số điện thoại *</label>
                                <input type="tel" name="phone" value="${param.phone}" class="form-input" placeholder="Nhập số điện thoại">
                                <c:if test="${not empty phoneEmpty}">
                                    <p class="warning-text">${phoneEmpty}</p>
                                </c:if>
                                <c:if test="${not empty phoneError}">
                                    <p class="warning-text">${phoneError}</p>
                                </c:if>
                            </div>
                        </div>

                        <div class="special-requests">
                            <h3>Các dịch vụ được sử dụng</h3>


                            <div class="services-list">
                                <c:forEach var="ls" items="${serviceInfor}">
                                    <c:choose>
                                        <c:when test="${ls.service.serviceId == 1}">
                                            <div class="service-item">
                                                <div class="service-info">
                                                    <div class="service-icon">📶</div>
                                                    <div class="service-details">
                                                        <div class="service-name">${ls.service.serviceName}</div>
                                                        <div class="service-price">Free</div>
                                                    </div>
                                                </div>
                                            </div>
                                        </c:when>
                                        <c:when test="${ls.service.serviceId == 2}">
                                            <div class="service-item">
                                                <div class="service-info">
                                                    <div class="service-icon">🚌</div>
                                                    <div class="service-details">
                                                        <div class="service-name">${ls.service.serviceName}</div>
                                                        <div class="service-price">${ls.service.price}</div>
                                                    </div>
                                                </div>
                                            </div>
                                        </c:when>

                                        <c:when test="${ls.service.serviceId == 3}">
                                            <div class="service-item">
                                                <div class="service-info">
                                                    <div class="service-icon">💆</div>
                                                    <div class="service-details">
                                                        <div class="service-name">${ls.service.serviceName}</div>
                                                        <div class="service-price">${ls.service.price}</div>
                                                    </div>
                                                </div>
                                                <div class="service-quantity">
                                                    <input type="number" id="airport-pickup" class="qty-input" value="${ls.quantity}" readonly>
                                                </div>
                                            </div>
                                        </c:when>

                                        <c:when test="${ls.service.serviceId == 4}">
                                            <div class="service-item">
                                                <div class="service-info">
                                                    <div class="service-icon">🏊</div>
                                                    <div class="service-details">
                                                        <div class="service-name">${ls.service.serviceName}</div>
                                                        <div class="service-price">Free</div>
                                                    </div>
                                                </div>
                                            </div>
                                        </c:when>

                                        <c:when test="${ls.service.serviceId == 5}">
                                            <div class="service-item">
                                                <div class="service-info">
                                                    <div class="service-icon">🚗</div>
                                                    <div class="service-details">
                                                        <div class="service-name">${ls.service.serviceName}</div>
                                                        <div class="service-price">${ls.service.price}</div>
                                                    </div>
                                                </div>
                                                <div class="service-quantity">
                                                    <input type="number" id="airport-pickup" class="qty-input" value="${ls.quantity}" readonly>
                                                </div>
                                            </div>
                                        </c:when>

                                        <c:when test="${ls.service.serviceId == 6}">
                                            <div class="service-item">
                                                <div class="service-info">
                                                    <div class="service-icon">🍳</div>
                                                    <div class="service-details">
                                                        <div class="service-name">${ls.service.serviceName}</div>
                                                        <div class="service-price">${ls.service.price}</div>
                                                    </div>
                                                </div>
                                                <div class="service-quantity">
                                                    <input type="number" id="airport-pickup" class="qty-input" value="${ls.quantity}" readonly>
                                                </div>
                                            </div>
                                        </c:when>

                                        <c:when test="${ls.service.serviceId == 7}">
                                            <div class="service-item">
                                                <div class="service-info">
                                                    <div class="service-icon">🛎️</div>
                                                    <div class="service-details">
                                                        <div class="service-name">${ls.service.serviceName}</div>
                                                    </div>
                                                </div>
                                            </div>
                                        </c:when>

                                    </c:choose>
                                </c:forEach>
                            </div>
                        </div>
                        <input type="hidden" name="service" value="confirmInformation">
                        <input type="hidden" name="timeLeft" id="timeLeft" value="">
                        <input type="hidden" name="cartId" value="${requestScope.cartId}">
                        <div class="contact-info">Hệ thống sẽ gửi email xác nhận đặt phòng ngay sau khi quý khách hoàn thành bước thanh toán!</div>
                        <div class="contact-info" style="color: red;">⚠️Quý khách vui lòng kiểm tra chính xác địa chỉ email của mình một lần nữa!⚠️</div>
                        <button class="continue-btn">KẾ TIẾP: BƯỚC THANH TOÁN</button>
                    </form>
                </div>

                <div class="sidebar">
                    <div class="hotel-card">
                        <div class="hotel-dates">
                            <div class="date-info">
                                <div class="date-label">Nhận phòng</div>
                                <div class="date-value">${cartInfor.startDate}</div>
                            </div>
                            <div class="nights-count">➡️</div>
                            <div class="date-info">
                                <div class="date-label">Trả phòng</div>
                                <div class="date-value">${cartInfor.endDate}</div>
                            </div>
                        </div>

                        <div class="room-type-card">
                            <div class="room-type-header">
                                <img src="${pageContext.request.contextPath}/Image/${fn:replace(typeRoomInfor.typeName, ' ', '')}/${typeRoomInfor.roomImages[0].image}" alt="${typeRoomInfor.typeName}" class="room-type-image">
                                <div class="room-type-details-header">
                                    <div class="room-type-name">${typeRoomInfor.typeName}</div>
                                    <div class="room-type-specs">
                                        Tối đa: ${typeRoom.maxAdult} người lớn, ${typeRoom.maxChildren} Trẻ em
                                    </div>
                                </div>
                            </div>

                            <div class="policy-item policy-green">
                                <span class="policy-icon">🏠</span>
                                <span>Có nơi giữ hành lý</span>
                            </div>

                            <div class="policy-item policy-green">
                                <span class="policy-icon">🕐</span>
                                <span>Nhận phòng 24 giờ</span>
                            </div>

                            <div class="policy-item policy-green">
                                <span class="policy-icon">✓</span>
                                <span>Đặt và trả tiền ngay</span>
                            </div>

                            <div class="policy-item policy-green">
                                <span class="policy-icon">🏊</span>
                                <span>Bãi đậu xe</span>
                            </div>

                            <div class="policy-item policy-green">
                                <span class="policy-icon">📶</span>
                                <span>WiFi miễn phí</span>
                            </div>
                        </div>

                        <div class="success-message">
                            <strong>👍 Xin chúc mừng!</strong> Bạn đã tìm đặt được phòng tại FPT Hotel
                        </div>

                        <div class="warning-message">
                            <strong>⚠️ Hãy nhanh tay đặt phòng để có một căn phòng vừa ý của bạn</strong>
                        </div>
                    </div>
                </div>
            </div>

            <script>
                // Thời gian hết hạn từ server (miliseconds)
                const expireTime = <%= expireTime%>;
                const countdownElement = document.getElementById("countdown-timer");

                console.log(expireTime);

                function updateCountdown() {
                    const now = new Date().getTime();
                    const distance = expireTime - now;
                    if (distance <= 0) {
                        document.getElementById("countdown-timer").innerText = "Đã hết thời gian giữ phòng!";
                        document.getElementById("timeLeft").value = 0;
                        document.getElementById("checkout-form").submit();
                        return;
                    }

                    const minutes = Math.floor((distance % (1000 * 60 * 60)) / (1000 * 60));
                    const seconds = Math.floor((distance % (1000 * 60)) / 1000);
                    document.getElementById("countdown-timer").innerText = "⏰" +
                            (minutes < 10 ? '0' : '') + minutes + ":" + (seconds < 10 ? '0' : '') + seconds;
                    
                    document.getElementById("timeLeft").value = Math.floor(distance / 1000);
                }

                // Gọi mỗi giây
                setInterval(updateCountdown, 1000);
                // Gọi ngay khi trang tải xong
                window.onload = updateCountdown;
            </script>
    </body>
</html>
