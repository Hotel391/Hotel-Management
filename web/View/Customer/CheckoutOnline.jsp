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
<!--                    <div class="dots">
                        <div class="dot"></div>
                        <div class="dot"></div>
                        <div class="dot"></div>
                        <div class="dot"></div>
                    </div>-->
                </div>
                <button class="login-btn">Đăng nhập</button>
            </div>
        </div>

        <div class="container">
<!--            <div class="progress-bar">
                <div class="countdown">
                    <div class="countdown-text">Chúng tôi đang giữ giá cho quý khách...</div>
                    <div class="countdown-timer">⏰ 00:19:33</div>
                </div>
            </div>-->

            <div class="main-content">

                <div class="booking-form">
                    <h2 class="form-title">Thông tin khách hàng</h2>
                    <div class="required-text">*Mục bắt buộc</div>

                    <div class="form-row">
                        <div class="form-group">
                            <label class="form-label">Tên *</label>
                            <input type="text" class="form-input" placeholder="Nhập tên">
                        </div>
                        <div class="form-group">
                            <label class="form-label">Họ (vd: Nguyễn) *</label>
                            <input type="text" class="form-input" placeholder="Nhập họ">
                        </div>
                    </div>

                    <div class="form-row">
                        <div class="form-group">
                            <label class="form-label">Email ID *</label>
                            <input type="email" class="form-input" placeholder="Nhập email">
                        </div>
                        <div class="form-group">
                            <label class="form-label">Số điện thoại (không bắt buộc)</label>
                            <input type="tel" class="form-input" placeholder="Nhập số điện thoại">
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
                                                <div class="service-icon">🚗</div>
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


                                <!--                                <div class="service-item">
                                                                    <div class="service-info">
                                                                        <div class="service-icon">🚌</div>
                                                                        <div class="service-details">
                                                                            <div class="service-name">Tour tham quan thành phố</div>
                                                                            <div class="service-price">300,000 VNĐ</div>
                                                                        </div>
                                                                    </div>
                                                                    <div class="service-quantity">
                                                                        <button type="button" class="qty-btn minus" onclick="updateQuantity('city-tour', -1)">-</button>
                                                                        <input type="number" id="city-tour" class="qty-input" value="0" min="0" max="10" readonly>
                                                                        <button type="button" class="qty-btn plus" onclick="updateQuantity('city-tour', 1)">+</button>
                                                                    </div>
                                                                </div>
                                
                                                                <div class="service-item">
                                                                    <div class="service-info">
                                                                        <div class="service-icon"></div>
                                                                        <div class="service-details">
                                                                            <div class="service-name">Bữa sáng buffet</div>
                                                                            <div class="service-price">150,000 VNĐ/người/ngày</div>
                                                                        </div>
                                                                    </div>
                                                                    <div class="service-quantity">
                                                                        <button type="button" class="qty-btn minus" onclick="updateQuantity('breakfast', -1)">-</button>
                                                                        <input type="number" id="breakfast" class="qty-input" value="0" min="0" max="10" readonly>
                                                                        <button type="button" class="qty-btn plus" onclick="updateQuantity('breakfast', 1)">+</button>
                                                                    </div>
                                                                </div>
                                
                                                                <div class="service-item">
                                                                    <div class="service-info">
                                                                        <div class="service-icon">🍷</div>
                                                                        <div class="service-details">
                                                                            <div class="service-name">Minibar miễn phí</div>
                                                                            <div class="service-price">200,000 VNĐ/ngày</div>
                                                                        </div>
                                                                    </div>
                                                                    <div class="service-quantity">
                                                                        <button type="button" class="qty-btn minus" onclick="updateQuantity('minibar', -1)">-</button>
                                                                        <input type="number" id="minibar" class="qty-input" value="0" min="0" max="10" readonly>
                                                                        <button type="button" class="qty-btn plus" onclick="updateQuantity('minibar', 1)">+</button>
                                                                    </div>
                                                                </div>
                                
                                                                <div class="service-item">
                                                                    <div class="service-info">
                                                                        <div class="service-icon">💆</div>
                                                                        <div class="service-details">
                                                                            <div class="service-name">Massage thư giãn 60 phút</div>
                                                                            <div class="service-price">800,000 VNĐ</div>
                                                                        </div>
                                                                    </div>
                                                                    <div class="service-quantity">
                                                                        <button type="button" class="qty-btn minus" onclick="updateQuantity('spa', -1)">-</button>
                                                                        <input type="number" id="spa" class="qty-input" value="0" min="0" max="10" readonly>
                                                                        <button type="button" class="qty-btn plus" onclick="updateQuantity('spa', 1)">+</button>
                                                                    </div>
                                                                </div>
                                
                                                                <div class="service-item">
                                                                    <div class="service-info">
                                                                        <div class="service-icon">🏋️</div>
                                                                        <div class="service-details">
                                                                            <div class="service-name">Sử dụng phòng gym 24/7</div>
                                                                            <div class="service-price">100,000 VNĐ/ngày</div>
                                                                        </div>
                                                                    </div>
                                                                    <div class="service-quantity">
                                                                        <button type="button" class="qty-btn minus" onclick="updateQuantity('gym', -1)">-</button>
                                                                        <input type="number" id="gym" class="qty-input" value="0" min="0" max="10" readonly>
                                                                        <button type="button" class="qty-btn plus" onclick="updateQuantity('gym', 1)">+</button>
                                                                    </div>
                                                                </div>
                                
                                                                <div class="service-item">
                                                                    <div class="service-info">
                                                                        <div class="service-icon">🕐</div>
                                                                        <div class="service-details">
                                                                            <div class="service-name">Trả phòng muộn (18:00)</div>
                                                                            <div class="service-price">200,000 VNĐ</div>
                                                                        </div>
                                                                    </div>
                                                                    <div class="service-quantity">
                                                                        <button type="button" class="qty-btn minus" onclick="updateQuantity('late-checkout', -1)">-</button>
                                                                        <input type="number" id="late-checkout" class="qty-input" value="0" min="0" max="1" readonly>
                                                                        <button type="button" class="qty-btn plus" onclick="updateQuantity('late-checkout', 1)">+</button>
                                                                    </div>
                                                                </div>
                                
                                                                <div class="service-item">
                                                                    <div class="service-info">
                                                                        <div class="service-icon">⬆️</div>
                                                                        <div class="service-details">
                                                                            <div class="service-name">Nâng cấp phòng view biển</div>
                                                                            <div class="service-price">400,000 VNĐ/ngày</div>
                                                                        </div>
                                                                    </div>
                                                                    <div class="service-quantity">
                                                                        <button type="button" class="qty-btn minus" onclick="updateQuantity('room-upgrade', -1)">-</button>
                                                                        <input type="number" id="room-upgrade" class="qty-input" value="0" min="0" max="1" readonly>
                                                                        <button type="button" class="qty-btn plus" onclick="updateQuantity('room-upgrade', 1)">+</button>
                                                                    </div>
                                                                </div>-->
                            </c:forEach>
                        </div>
                    </div>

                    <button class="continue-btn">KẾ TIẾP: BƯỚC CUỐI CÙNG</button>
                    <div class="contact-info">Có liên xác nhận đặt phòng!</div>
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

<!--                            <div class="policy-item policy-red">
                                <span class="policy-icon">❌</span>
                                <span>Chúng tôi chỉ còn 2 phòng có giá này!</span>
                            </div>-->

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
                // Countdown timer
                function updateCountdown() {
                    const countdownEl = document.querySelector('.countdown-timer');
                    let timeLeft = 19 * 60 + 33; // 19:33 in seconds

                    function tick() {
                        const minutes = Math.floor(timeLeft / 60);
                        const seconds = timeLeft % 60;
                        countdownEl.textContent = `⏰ ${minutes.toString().padStart(2, '0')}:${seconds.toString().padStart(2, '0')}`;

                        if (timeLeft > 0) {
                            timeLeft--;
                            setTimeout(tick, 1000);
                        } else {
                            countdownEl.textContent = '⏰ 00:00';
                        }
                    }

                    tick();
                }

                // Form validation
                function validateForm() {
                    const requiredFields = document.querySelectorAll('.form-input[required], .form-select[required]');
                    let isValid = true;

                    requiredFields.forEach(field => {
                        if (!field.value.trim()) {
                            field.style.borderColor = '#ff385c';
                            isValid = false;
                        } else {
                            field.style.borderColor = '#ddd';
                        }
                    });

                    return isValid;
                }

                // Initialize
                document.addEventListener('DOMContentLoaded', function () {
                    updateCountdown();

                    // Form submission
                    document.querySelector('.continue-btn').addEventListener('click', function (e) {
                        e.preventDefault();
                        if (validateForm()) {
                            alert('Tiếp tục đến bước thanh toán!');
                        } else {
                            alert('Vui lòng điền đầy đủ thông tin bắt buộc.');
                        }
                    });

                    // More options toggle
                    document.querySelector('.more-options').addEventListener('click', function () {
                        // Add more special request options here
                        alert('Tính năng đang phát triển!');
                    });
                });
            </script>
    </body>
</html>
