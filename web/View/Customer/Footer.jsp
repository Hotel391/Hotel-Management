<%-- 
    Author     : SONNAM
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!-- Footer -->

<footer class="footer">
    <div class="footer_content">
        <div class="container">
            <div class="row footer_row align-items-start">
                <!-- Logo -->
                <div class="col-lg-3">
                    <div class="footer_logo_container">
                        <div class="footer_logo">
                            <a href="#"></a>
                            <div class="d-flex flex-row align-items-center" style="gap: 10px;">
                                <img src="${pageContext.request.contextPath}/Image/Logo.png" alt="Logo"
                                     style="height: 50px; background-color: transparent;">
                                <div id="hotel-name" style="font-family: 'Playfair Display', serif; font-size: 24px; color: #003366; font-weight: bold;">
                                    FPTHotel
                                </div>
                            </div>
                            <div style="font-size: 16px; font-weight: 600; color: #393939; line-height: 0.75; margin-top: 15px;">
                                since 2025
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Address -->
                <div class="col-lg-3">
                    <div class="footer_title">Địa chỉ</div>
                    <div class="footer_list">
                        <ul>
                            <li>HÀ NỘI</li>
                            <li>Khu Giáo dục và Đào tạo</li>
                            <li>Khu Công nghệ cao Hòa Lạc - Km29 Đại lộ Thăng Long, xã Hòa Lạc, TP. Hà Nội</li>
                        </ul>
                    </div>
                </div>

                <!-- Reservations -->
                <div class="col-lg-3">
                    <div class="footer_title">Liên hệ</div>
                    <div class="footer_list">
                        <ul>
                            <li>Zalo: 345 5667 889</li>
                            <li>Tel: 6783 4567 889</li>
                            <li></li>
                        </ul>
                    </div>
                </div>

                <!-- Footer images -->
                <div class="col-lg-3">
                    <div class="certificates d-flex flex-row align-items-start justify-content-lg-between justify-content-start flex-lg-nowrap flex-wrap">
                        <div class="cert"><img src="${pageContext.request.contextPath}/Image/cert_1.png" alt=""></div>
                        <div class="cert"><img src="${pageContext.request.contextPath}/Image/cert_2.png" alt=""></div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</footer>