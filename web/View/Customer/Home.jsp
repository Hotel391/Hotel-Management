<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <title>The Hotel391</title>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="description" content="The Hotel391">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/Css/bootstrap-4.1.2/bootstrap.min.css">
        <link href="${pageContext.request.contextPath}/plugins/font-awesome-4.7.0/css/font-awesome.min.css" rel="stylesheet" type="text/css">
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/plugins/OwlCarousel2-2.3.4/owl.carousel.css">
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/plugins/OwlCarousel2-2.3.4/owl.theme.default.css">
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/plugins/OwlCarousel2-2.3.4/animate.css">
        <link href="${pageContext.request.contextPath}/plugins/jquery-datepicker/jquery-ui.css" rel="stylesheet" type="text/css">
        <link href="${pageContext.request.contextPath}/plugins/colorbox/colorbox.css" rel="stylesheet" type="text/css">
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/Css/main_styles.css">
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/Css/responsive.css">
        <style>
            .room-card {
                display: flex;
                flex-direction: column;
                background: #fff;
                border-radius: 15px;
                box-shadow: 0 4px 12px rgba(0,0,0,0.1);
                overflow: hidden;
                text-decoration: none;
                color: inherit;
                width: 300px;
                margin: 15px;
                transition: transform 0.2s ease;
            }
            .room-card:hover {
                transform: translateY(-5px);
            }

            .room-image-wrapper {
                width: 100%;
                height: 200px;
                overflow: hidden;
            }
            .room-main-image {
                width: 100%;
                height: 100%;
                object-fit: cover;
            }

            .room-thumbnails {
                display: flex;
                justify-content: space-between;
                gap: 5px;
                padding: 10px;
                box-sizing: border-box;
            }
            .thumbnail {
                flex: 1;
                height: 70px;
                object-fit: cover;
                border-radius: 5px;
            }
            .see-all {
                flex: 1;
                background-color: #e0e0e0;
                display: flex;
                justify-content: center;
                align-items: center;
                font-size: 14px;
                color: #333;
                border-radius: 5px;
                height: 70px;
            }

            .room-info {
                padding: 10px;
                text-align: center;
            }
            .room-name {
                font-weight: bold;
                font-size: 16px;
                margin-bottom: 5px;
            }
            .room-price {
                font-size: 15px;
                color: #444;
            }
            .avatar-initial {
                width: 60px;
                height: 60px;
                background-color: #007bff;
                color: white;
                font-weight: bold;
                font-size: 24px;
                border-radius: 50%;
                display: flex;
                align-items: center;
                justify-content: center;
                margin: 0 auto 10px auto;
                text-transform: uppercase;
            }
        </style>
    </head>
    <body>
        <div class="super_container">
            <jsp:include page="Header.jsp" />
            <div>
                <!-- Trang chủ -->
                <div class="home">
                    <div class="home_slider_container">
                        <div class="owl-carousel owl-theme home_slider">
                            <!-- Slide -->
                            <div class="slide">
                                <div class="background_image" style="background-image:url(${pageContext.request.contextPath}/Image/index_1.jpg)"></div>
                                <div class="home_container">
                                    <div class="container">
                                        <div class="row">
                                            <div class="col">
                                                <div class="home_content text-center">
                                                    <div class="home_title">Xin chào!</div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <!-- Slide -->
                            <div class="slide">
                                <div class="background_image" style="background-image:url(${pageContext.request.contextPath}/Image/PenthouseSuite/PenthouseSuite1.png)"></div>
                                <div class="home_container">
                                    <div class="container">
                                        <div class="row">
                                            <div class="col">
                                                <div class="home_content text-center">
                                                    <div class="home_title">Sang Trọng, Độc Đáo, Chu Đáo</div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <!-- Slide -->
                            <div class="slide">
                                <div class="background_image" style="background-image:url(${pageContext.request.contextPath}/Image/index_1.jpg)"></div>
                                <div class="home_container">
                                    <div class="container">
                                        <div class="row">
                                            <div class="col">
                                                <div class="home_content text-center">
                                                    <div class="home_title">Phong cách Châu Âu - Đáng Nhớ</div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <!-- Điểm trượt Trang chủ -->
                        <div class="home_slider_dots_container">
                            <div class="home_slider_dots">
                                <ul id="home_slider_custom_dots" class="home_slider_custom_dots d-flex flex-row align-items-start justify-content-start">
                                    <li class="home_slider_custom_dot active">01.</li>
                                    <li class="home_slider_custom_dot">02.</li>
                                    <li class="home_slider_custom_dot">03.</li>
                                </ul>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Top 5 hoa hậu -->
                <div class="booking">
                    <div class="container">
                        <div class="row">
                            <div class="col">
                                <div class="booking_title text-center">
                                    <h2>Top 5 hoa hậu</h2>
                                </div>

                                <div class="booking_slider_container">
                                    <div class="owl-carousel owl-theme booking_slider">
                                        <c:choose>
                                            <c:when test="${not empty topRoomTypes}">
                                                <c:forEach var="roomType" items="${topRoomTypes}">
                                                    <a href="${pageContext.request.contextPath}/detailRoom?typeRoomId=${roomType.typeId}" class="room-card">
                                                        <div class="room-image-wrapper">
                                                            <img class="room-main-image" src="${pageContext.request.contextPath}/${roomType.uriContextOfImages}${roomType.images[0]}" alt="Main Image" />
                                                        </div>

                                                        <div class="room-thumbnails">
                                                            <c:forEach var="i" begin="1" end="3">
                                                                <c:if test="${i < fn:length(roomType.images)}">
                                                                    <img class="thumbnail" src="${pageContext.request.contextPath}/${roomType.uriContextOfImages}${roomType.images[i]}" alt="Thumb ${i}" />
                                                                </c:if>
                                                            </c:forEach>

                                                        </div>

                                                        <div class="room-info">
                                                            <div class="room-name">${roomType.typeName}</div>
                                                            <div class="room-price">
                                                                <fmt:formatNumber value="${roomType.price}" pattern="#,##0"/>đ/<span style="font-weight: normal;">Đêm</span>
                                                            </div>
                                                        </div>
                                                    </a>
                                                </c:forEach>

                                            </c:when>
                                            <c:otherwise>
                                                <div class="booking_item">
                                                    <div class="background_image" style="background-image:url(${pageContext.request.contextPath}/Image/booking_1.jpg)"></div>
                                                    <div class="booking_overlay trans_200"></div>
                                                    <div class="booking_price">Chưa có dữ liệu</div>
                                                    <div class="booking_link"><a href="${pageContext.request.contextPath}/booking">Xem tất cả phòng</a></div>
                                                </div>
                                            </c:otherwise>
                                        </c:choose>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>


                <!-- Tính năng -->
                <div class="features">
                    <div class="container">
                        <div class="row">
                            <!-- Hộp Biểu tượng -->
                            <div class="col-lg-4 icon_box_col">
                                <div class="icon_box d-flex flex-column align-items-center justify-content-start text-center">
                                    <div class="icon_box_icon"><img src="${pageContext.request.contextPath}/Image/icon_1.svg" class="svg" alt="https://www.flaticon.com/authors/monkik"></div>
                                    <div class="icon_box_title"><h2>Khu Nghỉ Dưỡng Tuyệt Vời</h2></div>
                                </div>
                            </div>
                            <!-- Hộp Biểu tượng -->
                            <div class="col-lg-4 icon_box_col">
                                <div class="icon_box d-flex flex-column align-items-center justify-content-start text-center">
                                    <div class="icon_box_icon"><img src="${pageContext.request.contextPath}/Image/icon_2.svg" class="svg" alt="https://www.flaticon.com/authors/monkik"></div>
                                    <div class="icon_box_title"><h2>Hồ Bơi Vô Cực</h2></div>
                                </div>
                            </div>
                            <!-- Hộp Biểu tượng -->
                            <div class="col-lg-4 icon_box_col">
                                <div class="icon_box d-flex flex-column align-items-center justify-content-start text-center">
                                    <div class="icon_box_icon"><img src="${pageContext.request.contextPath}/Image/icon_3.svg" class="svg" alt="https://www.flaticon.com/authors/monkik"></div>
                                    <div class="icon_box_title"><h2>Phòng Sang Trọng</h2></div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Thư viện ảnh -->
                <div class="gallery">
                    <div class="gallery_slider_container">
                        <div class="owl-carousel owl-theme gallery_slider">
                            <!-- Slide -->
                            <div class="gallery_item">
                                <div class="background_image" style="background-image:url(${pageContext.request.contextPath}/Image/gallery_1.jpg)"></div>
                                <a class="colorbox" href="${pageContext.request.contextPath}/Image/gallery_1.jpg"></a>
                            </div>
                            <!-- Slide -->
                            <div class="gallery_item">
                                <div class="background_image" style="background-image:url(${pageContext.request.contextPath}/Image/gallery_2.jpg)"></div>
                                <a class="colorbox" href="${pageContext.request.contextPath}/Image/gallery_2.jpg"></a>
                            </div>
                            <!-- Slide -->
                            <div class="gallery_item">
                                <div class="background_image" style="background-image:url(${pageContext.request.contextPath}/Image/gallery_3.jpg)"></div>
                                <a class="colorbox" href="${pageContext.request.contextPath}/Image/gallery_3.jpg"></a>
                            </div>
                            <!-- Slide -->
                            <div class="gallery_item">
                                <div class="background_image" style="background-image:url(${pageContext.request.contextPath}/Image/gallery_4.jpg)"></div>
                                <a class="colorbox" href="${pageContext.request.contextPath}/Image/gallery_4.jpg"></a>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Giới thiệu -->
                <div class="about">
                    <div class="container">
                        <div class="row">
                            <!-- Nội dung Giới thiệu -->
                            <div class="col-lg-6">
                                <div class="about_content">
                                    <div class="about_title"><h2>Khẳng Định Chất Lượng – Chứng Nhận Từ Những Tên Tuổi Hàng Đầu!</h2></div>
                                </div>
                            </div>
                            <!-- Hình ảnh Giới thiệu -->
                            <div class="col-lg-6">
                                <div class="about_images d-flex flex-row align-items-center justify-content-between flex-wrap">
                                    <img src="${pageContext.request.contextPath}/Image/about_1.png" alt="">
                                    <img src="${pageContext.request.contextPath}/Image/about_2.png" alt="">
                                    <img src="${pageContext.request.contextPath}/Image/about_3.png" alt="">
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Đánh giá -->
                <div class="testimonials">
                    <div class="parallax_background parallax-window" data-parallax="scroll" data-image-src="${pageContext.request.contextPath}/Image/testimonials.jpg" data-speed="0.8"></div>
                    <div class="testimonials_overlay"></div>
                    <div class="container">
                        <div class="row">
                            <div class="col">
                                <div class="testimonials_slider_container">
                                    <!-- Thanh trượt Đánh giá -->
                                    <div class="owl-carousel owl-theme test_slider">
                                        <c:choose>
                                            <c:when test="${not empty topReviews}">
                                                <c:forEach var="review" items="${topReviews}">
                                                    <c:set var="fullName" value="${review.customerAccount.customer.fullName}" />
                                                    <c:set var="initial" value="${fn:substring(fullName, 0, 1)}" />
                                                    <c:set var="typeRoomId" value="${review.bookingDetail.room.typeRoom.typeId}" />

                                                    <div class="test_slider_item text-center">
                                                        <div class="rating rating_5 d-flex flex-row align-items-start justify-content-center">
                                                            <i></i><i></i><i></i><i></i><i></i>
                                                        </div>

                                                        <div class="testimonial_title">
                                                            <a href="${pageContext.request.contextPath}/detailRoom?typeRoomId=${review.bookingDetail.room.typeRoom.typeId}">
                                                                ${review.bookingDetail.room.typeRoom.typeName}
                                                            </a>

                                                        </div>

                                                        <div class="testimonial_text">
                                                            <p>${review.feedBack}</p>
                                                        </div>

                                                        <!-- Avatar bằng chữ cái đầu -->
                                                        <div class="testimonial_image">
                                                            <div class="avatar-initial" style="width: 60px; height: 60px; border-radius: 50%; color: #333; font-weight: bold; font-size: 24px; display: flex; align-items: center; justify-content: center; margin: 0 auto;">
                                                                ${initial}
                                                            </div>
                                                        </div>

                                                        <div class="testimonial_author">
                                                            <a href="${pageContext.request.contextPath}/detailRoom?typeRoomId=${typeRoomId}">${fullName}</a>,
                                                            <fmt:formatDate value="${review.date}" pattern="dd/MM/yyyy"/>
                                                        </div>
                                                    </div>
                                                </c:forEach>
                                            </c:when>
                                            <c:otherwise>
                                                <div class="test_slider_item text-center">
                                                    <div class="testimonial_title"><a href="#">Chưa có đánh giá 5 sao</a></div>
                                                </div>
                                            </c:otherwise>
                                        </c:choose>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <jsp:include page="Footer.jsp" />
        </div>
        <jsp:include page="/View/chatbot.jsp"/>
        <script src="${pageContext.request.contextPath}/Js/jquery-3.3.1.min.js"></script>
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
        <script src="${pageContext.request.contextPath}/plugins/colorbox/jquery.colorbox-min.js"></script>
        <script src="${pageContext.request.contextPath}/Js/custom.js"></script>
    </body>
</html>