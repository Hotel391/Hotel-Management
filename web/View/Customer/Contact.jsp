<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.Date" %>
<%@ page import="java.text.SimpleDateFormat" %>
<!DOCTYPE html>
<html lang="en">
    <head>
        <title>Contact</title>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="description" content="FPTHotel">
        <meta name="viewport" content="width=device-width, initial-scale=1">

        <!-- CSS -->
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/Css/bootstrap-4.1.2/bootstrap.min.css">
        <link href="${pageContext.request.contextPath}/plugins/font-awesome-4.7.0/css/font-awesome.min.css" rel="stylesheet" type="text/css">
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/plugins/OwlCarousel2-2.3.4/owl.carousel.css">
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/plugins/OwlCarousel2-2.3.4/owl.theme.default.css">
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/plugins/OwlCarousel2-2.3.4/animate.css">
        <link href="${pageContext.request.contextPath}/plugins/jquery-datepicker/jquery-ui.css" rel="stylesheet" type="text/css">
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/Css/contact.css">
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/Css/contact_responsive.css">
        <!-- Leaflet CSS -->
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/leaflet@1.9.4/dist/leaflet.css" />
        <style>
            #map {
                width: 100%;
                height: 400px;
                z-index: 2;
                .contact,
                .contact * {
                    color: white !important;
                }
            }
        </style>
    </head>
    <body>

        <div class="super_container">
            <jsp:include page="Header.jsp" />

            <!-- Home with overlay background -->
            <div class="home">
                <div class="background_image" style="background-image:url('${pageContext.request.contextPath}/Image/blog_2.jpg');"></div>
                <div class="home_container">
                    <div class="container text-center">
                        <div class="home_title">Kết nối với FPT Hotel - Trải nghiệm tuyệt vời bắt đầu từ đây</div>
                    </div>
                </div>
            </div>

            <!-- Contact -->
            <div class="contact" style="background-color: #22313f; padding: 60px 0;">
                <style>
                    .contact :not(#map):not(#map *) {
                        color: white !important;
                    }
                </style>
                <div class="container">
                    <div class="row">

                        <!-- Contact Content -->
                        <div class="col-lg-6">
                            <div class="contact_content">
                                <div class="contact_title"><h2>Liên hệ với chúng tôi</h2></div>
                                <div class="contact_list">
                                    <ul style="list-style: none; padding-left: 0;">
                                        <li>
                                            <a href="https://www.google.com/maps/place/Tr%C6%B0%E1%BB%9Dng+%C4%90%E1%BA%A1i+h%E1%BB%8Dc+FPT+H%C3%A0+N%E1%BB%99i/@21.0124167,105.5227143,17z/data=!3m1!4b1!4m6!3m5!1s0x3135abc60e7d3f19:0x2be9d7d0b5abcbf4!8m2!3d21.0124167!4d105.5252892!16s%2Fm%2F02rsytm?entry=ttu&g_ep=EgoyMDI1MDcwOS4wIKXMDSoASAFQAw%3D%3D" 
                                               target="_blank" 
                                               style="color: white; text-decoration: underline;">
                                                FPT University, Khu CNC Hòa Lạc, Hà Nội
                                            </a>
                                        </li>
                                        <li>+84 345 5667 889</li>
                                        <li>hotel391@gmail.com</li>
                                    </ul>
                                </div>
                            </div>
                        </div>

                        <!-- Map (Leaflet) -->
                        <div class="col-xl-5 col-lg-6 offset-xl-1">
                            <div class="contact_map" style="border-radius: 10px; overflow: hidden;">
                                <div id="map" style="width: 100%; height: 400px;"></div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <jsp:include page="Footer.jsp" />
        </div>

        <!-- JS -->
        <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
        <script src="${pageContext.request.contextPath}/Css/bootstrap-4.1.2/popper.js"></script>
        <script src="${pageContext.request.contextPath}/Css/bootstrap-4.1.2/bootstrap.min.js"></script>
        <script src="${pageContext.request.contextPath}/plugins/greensock/TweenMax.min.js"></script>
        <script src="${pageContext.request.contextPath}/plugins/greensock/TimelineMax.min.js"></script>
        <script src="${pageContext.request.contextPath}/plugins/scrollmagic/ScrollMagic.min.js"></script>
        <script src="${pageContext.request.contextPath}/plugins/greensock/animation.gsap.min.js"></script>
        <script src="${pageContext.request.contextPath}/plugins/greensock/ScrollToPlugin.min.js"></script>
        <script src="${pageContext.request.contextPath}/plugins/OwlCarousel2-2.3.4/owl.carousel.js"></script>
        <script src="${pageContext.request.contextPath}/plugins/easing/easing.js"></script>
        <script src="${pageContext.request.contextPath}/plugins/progressbar/progressbar.min.js"></script>
        <script src="${pageContext.request.contextPath}/plugins/parallax-js-master/parallax.min.js"></script>
        <script src="${pageContext.request.contextPath}/plugins/jquery-datepicker/jquery-ui.js"></script>
        <script src="${pageContext.request.contextPath}/js/contact.js"></script>
        <!-- Leaflet JS -->
        <script src="https://cdn.jsdelivr.net/npm/leaflet@1.9.4/dist/leaflet.js"></script>
        <script>
            document.addEventListener('DOMContentLoaded', function () {
                const fptHanoi = [21.0278, 105.5261]; // Updated coordinates for FPT University, Hòa Lạc
                const map = L.map('map').setView(fptHanoi, 15);
                L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
                    attribution: '© OpenStreetMap contributors'
                }).addTo(map);
                L.marker(fptHanoi).addTo(map)
                        .bindPopup("<b>FPT University Hà Nội</b><br>Hòa Lạc, Hà Nội")
                        .openPopup();
            });
        </script>
    </body>
</html>