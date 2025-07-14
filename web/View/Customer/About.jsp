<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html>
<html lang="vi">
    <head>
        <title>Giới Thiệu</title>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="description" content="Khách sạn The River - Nghỉ dưỡng sang trọng và dịch vụ cao cấp.">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/Css/bootstrap-4.1.2/bootstrap.min.css">
        <link href="${pageContext.request.contextPath}/plugins/font-awesome-4.7.0/css/font-awesome.min.css" rel="stylesheet" type="text/css">
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/plugins/OwlCarousel2-2.3.4/owl.carousel.css">
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/plugins/OwlCarousel2-2.3.4/owl.theme.default.css">
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/plugins/OwlCarousel2-2.3.4/animate.css">
        <link href="${pageContext.request.contextPath}/plugins/jquery-datepicker/jquery-ui.css" rel="stylesheet" type="text/css">
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/Css/about.css">
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/Css/about_responsive.css">
    </head>
    <body>

        <div class="super_container">
            <jsp:include page="Header.jsp" />
            <div>
                <div class="home">
                    <div class="background_image" style="background-image:url(${pageContext.request.contextPath}/Image/about.jpg)"></div>
                    <div class="home_container">
                        <div class="container">
                            <div class="row">
                                <div class="col">
                                    <div class="home_content text-center">
                                        <div class="home_title">Chúng Tôi Là Ai?</div>                                       
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- About -->
                <div class="about">
                    <div class="container">
                        <div class="row">
                            <div class="col-lg-6">
                                <div class="about_title"><h2>The River / 10 năm phát triển vượt bậc</h2></div>
                            </div>
                        </div>
                        <div class="row about_row">

                            <!-- About Content -->
                            <div class="col-lg-6">
                                <div class="about_content">
                                    <div class="about_text">
                                        <p>Khách sạn The River là điểm đến lý tưởng cho du khách trong và ngoài nước. Với hơn 10 năm hoạt động, chúng tôi luôn nỗ lực mang đến những dịch vụ đẳng cấp, không gian sang trọng và trải nghiệm nghỉ dưỡng tuyệt vời nhất. Đội ngũ nhân viên chuyên nghiệp, thân thiện và các tiện nghi hiện đại sẽ làm bạn hài lòng trong từng khoảnh khắc lưu trú.</p>
                                    </div>
                                    <div class="about_sig"><img src="${pageContext.request.contextPath}/Image/sig.png" alt=""></div>
                                </div>
                            </div>

                            <!-- About Images -->
                            <div class="col-lg-6">
                                <div class="about_images d-flex flex-row align-items-start justify-content-between flex-wrap">
                                    <img src="${pageContext.request.contextPath}/Image/about_1.png" alt="">
                                    <img src="${pageContext.request.contextPath}/Image/about_2.png" alt="">
                                    <img src="${pageContext.request.contextPath}/Image/about_3.png" alt="">
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Split Section Right -->
                <div class="split_section_right container_custom">
                    <div class="container">
                        <div class="row row-xl-eq-height">

                            <div class="col-xl-6 order-xl-1 order-2">
                                <div class="split_section_image">
                                    <div class="background_image" style="background-image:url(${pageContext.request.contextPath}/Image/milestones.jpg)"></div>
                                </div>
                            </div>

                            <div class="col-xl-6 order-xl-2 order-1">
                                <div class="split_section_right_content">
                                    <div class="split_section_title"><h1>Khu Nghỉ Dưỡng Cao Cấp</h1></div>
                                    <div class="split_section_text">
                                        <p>Chúng tôi tự hào sở hữu hệ thống phòng nghỉ tiện nghi, hồ bơi ngoài trời, nhà hàng cao cấp và nhiều dịch vụ thư giãn. Mỗi chi tiết đều được chăm chút nhằm đem lại sự thoải mái tối đa cho quý khách. Hãy để The River trở thành nơi bạn tìm thấy sự yên bình và sang trọng trong mỗi kỳ nghỉ.</p>
                                    </div>

                                    <!-- Milestones -->
                                    <div class="milestones_container d-flex flex-row align-items-start justify-content-start flex-wrap">
                                        <div class="milestone d-flex flex-row align-items-start justify-content-start">
                                            <div class="milestone_content">
                                                <div class="milestone_counter" data-end-value="45">0</div>
                                                <div class="milestone_title">Phòng sẵn có</div>
                                            </div>
                                        </div>
                                        <div class="milestone d-flex flex-row align-items-start justify-content-start">
                                            <div class="milestone_content">
                                                <div class="milestone_counter" data-end-value="21" data-sign-after="K">0</div>
                                                <div class="milestone_title">Khách du lịch trong năm</div>
                                            </div>
                                        </div>
                                        <div class="milestone d-flex flex-row align-items-start justify-content-start">
                                            <div class="milestone_content">
                                                <div class="milestone_counter" data-end-value="2">0</div>
                                                <div class="milestone_title">Hồ bơi</div>
                                            </div>
                                        </div>
                                    </div>
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
        <script src="${pageContext.request.contextPath}/js/jquery-3.3.1.min.js"></script>
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
        <script src="${pageContext.request.contextPath}/js/about.js"></script>
    </body>
</html>
