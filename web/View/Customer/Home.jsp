<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <title>The Hotel391</title>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="description" content="The hotel391 project">
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
    </head>
    <body>

        <div class="super_container">
            <jsp:include page="Header.jsp" />
            <div>
                <jsp:include page="Menu.jsp" />


                <!-- Home -->

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
                                                    <div class="home_title">A Luxury Stay</div>
                                                    <div class="booking_form_container">
                                                        <form action="#" class="booking_form">
                                                            <div class="d-flex flex-xl-row flex-column align-items-start justify-content-start">
                                                                <div class="booking_input_container d-flex flex-lg-row flex-column align-items-start justify-content-start">
                                                                    <div><input type="text" class="datepicker booking_input booking_input_a booking_in" placeholder="Check in" required="required"></div>
                                                                    <div><input type="text" class="datepicker booking_input booking_input_a booking_out" placeholder="Check out" required="required"></div>
                                                                    <div><input type="number" class="booking_input booking_input_b" placeholder="Children" required="required"></div>
                                                                    <div><input type="number" class="booking_input booking_input_b" placeholder="Room" required="required"></div>
                                                                </div>
                                                                <div><button class="booking_button trans_200">Book Now</button></div>
                                                            </div>
                                                        </form>
                                                    </div>
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
                                                    <div class="home_title">A Luxury Stay</div>
                                                    <div class="booking_form_container">
                                                        <form action="#" class="booking_form">
                                                            <div class="d-flex flex-xl-row flex-column align-items-start justify-content-start">
                                                                <div class="booking_input_container d-flex flex-lg-row flex-column align-items-start justify-content-start">
                                                                    <div><input type="text" class="datepicker booking_input booking_input_a booking_in" placeholder="Check in" required="required"></div>
                                                                    <div><input type="text" class="datepicker booking_input booking_input_a booking_out" placeholder="Check out" required="required"></div>
                                                                    <div><input type="number" class="booking_input booking_input_b" placeholder="Children" required="required"></div>
                                                                    <div><input type="number" class="booking_input booking_input_b" placeholder="Room" required="required"></div>
                                                                </div>
                                                                <div><button class="booking_button trans_200">Book Now</button></div>
                                                            </div>
                                                        </form>
                                                    </div>
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
                                                    <div class="home_title">A Luxury Stay</div>
                                                    <div class="booking_form_container">
                                                        <form action="#" class="booking_form">
                                                            <div class="d-flex flex-xl-row flex-column align-items-start justify-content-start">
                                                                <div class="booking_input_container d-flex flex-lg-row flex-column align-items-start justify-content-start">
                                                                    <div><input type="text" class="datepicker booking_input booking_input_a booking_in" placeholder="Check in" required="required"></div>
                                                                    <div><input type="text" class="datepicker booking_input booking_input_a booking_out" placeholder="Check out" required="required"></div>
                                                                    <div><input type="number" class="booking_input booking_input_b" placeholder="Children" required="required"></div>
                                                                    <div><input type="number" class="booking_input booking_input_b" placeholder="Room" required="required"></div>
                                                                </div>
                                                                <div><button class="booking_button trans_200">Book Now</button></div>
                                                            </div>
                                                        </form>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>

                        </div>

                        <!-- Home Slider Dots -->
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

                <!-- Features -->

                <div class="features">
                    <div class="container">
                        <div class="row">

                            <!-- Icon Box -->
                            <div class="col-lg-4 icon_box_col">
                                <div class="icon_box d-flex flex-column align-items-center justify-content-start text-center">
                                    <div class="icon_box_icon"><img src="${pageContext.request.contextPath}/Image/icon_1.svg" class="svg" alt="https://www.flaticon.com/authors/monkik"></div>
                                    <div class="icon_box_title"><h2>Fabulous Resort</h2></div>
                                    <div class="icon_box_text">
                                        <p>Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia Curae; Suspendisse nec faucibus velit. Quisque eleifend orci ipsum, a bibendum.</p>
                                    </div>
                                </div>
                            </div>

                            <!-- Icon Box -->
                            <div class="col-lg-4 icon_box_col">
                                <div class="icon_box d-flex flex-column align-items-center justify-content-start text-center">
                                    <div class="icon_box_icon"><img src="${pageContext.request.contextPath}/Image/icon_2.svg" class="svg" alt="https://www.flaticon.com/authors/monkik"></div>
                                    <div class="icon_box_title"><h2>Infinity Pool</h2></div>
                                    <div class="icon_box_text">
                                        <p>Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia Curae; Suspendisse nec faucibus velit. Quisque eleifend orci ipsum, a bibendum.</p>
                                    </div>
                                </div>
                            </div>

                            <!-- Icon Box -->
                            <div class="col-lg-4 icon_box_col">
                                <div class="icon_box d-flex flex-column align-items-center justify-content-start text-center">
                                    <div class="icon_box_icon"><img src="${pageContext.request.contextPath}/Image/icon_3.svg" class="svg" alt="https://www.flaticon.com/authors/monkik"></div>
                                    <div class="icon_box_title"><h2>Luxury Rooms</h2></div>
                                    <div class="icon_box_text">
                                        <p>Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia Curae; Suspendisse nec faucibus velit. Quisque eleifend orci ipsum, a bibendum.</p>
                                    </div>
                                </div>
                            </div>

                        </div>
                    </div>
                </div>

                <!-- Gallery -->

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

                <!-- About -->

                <div class="about">
                    <div class="container">
                        <div class="row">

                            <!-- About Content -->
                            <div class="col-lg-6">
                                <div class="about_content">
                                    <div class="about_title"><h2>The River / 10 years of excellence</h2></div>
                                    <div class="about_text">
                                        <p>Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia Curae; Suspendisse nec faucibus velit. Quisque eleifend orci ipsum, a bibendum lacus suscipit sit. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia Curae; Suspendisse nec faucibus velit. Quisque eleifend orci ipsum, a bibendum lacus suscipit sit. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia Curae; Suspendisse nec faucibus velit.</p>
                                    </div>
                                </div>
                            </div>

                            <!-- About Images -->
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

                <!-- Testimonials -->

                <div class="testimonials">
                    <div class="parallax_background parallax-window" data-parallax="scroll" data-image-src="${pageContext.request.contextPath}/Image/testimonials.jpg" data-speed="0.8"></div>
                    <div class="testimonials_overlay"></div>
                    <div class="container">
                        <div class="row">
                            <div class="col">
                                <div class="testimonials_slider_container">

                                    <!-- Testimonials Slider -->
                                    <div class="owl-carousel owl-theme test_slider">

                                        <!-- Slide -->
                                        <div  class="test_slider_item text-center">
                                            <div class="rating rating_5 d-flex flex-row align-items-start justify-content-center"><i></i><i></i><i></i><i></i><i></i></div>
                                            <div class="testimonial_title"><a href="#">Perfect Stay</a></div>
                                            <div class="testimonial_text">
                                                <p>Etiam nec odio vestibulum est mattis effic iturut magna. Pellentesque sit amet tellus blandit. Etiam nec odio vestibulum est mattis effic.</p>
                                            </div>
                                            <div class="testimonial_image"><img src="${pageContext.request.contextPath}/Image/user_1.jpg" alt=""></div>
                                            <div class="testimonial_author"><a href="#">Samantha Smith</a>, Greece</div>
                                        </div>

                                        <!-- Slide -->
                                        <div  class="test_slider_item text-center">
                                            <div class="rating rating_5 d-flex flex-row align-items-start justify-content-center"><i></i><i></i><i></i><i></i><i></i></div>
                                            <div class="testimonial_title"><a href="#">Nice place</a></div>
                                            <div class="testimonial_text">
                                                <p>Etiam nec odio vestibulum est mattis effic iturut magna. Pellentesque sit amet tellus blandit. Etiam nec odio vestibulum est mattis effic.</p>
                                            </div>
                                            <div class="testimonial_image"><img src="${pageContext.request.contextPath}/Image/user_2.jpg" alt=""></div>
                                            <div class="testimonial_author"><a href="#">Michael Doe</a>, Italy</div>
                                        </div>

                                        <!-- Slide -->
                                        <div  class="test_slider_item text-center">
                                            <div class="rating rating_5 d-flex flex-row align-items-start justify-content-center"><i></i><i></i><i></i><i></i><i></i></div>
                                            <div class="testimonial_title"><a href="#">We loved it</a></div>
                                            <div class="testimonial_text">
                                                <p>Etiam nec odio vestibulum est mattis effic iturut magna. Pellentesque sit amet tellus blandit. Etiam nec odio vestibulum est mattis effic.</p>
                                            </div>
                                            <div class="testimonial_image"><img src="${pageContext.request.contextPath}/Image/user_3.jpg" alt=""></div>
                                            <div class="testimonial_author"><a href="#">Luis Garcia</a>, Spain</div>
                                        </div>

                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Booking -->

                <div class="booking">
                    <div class="container">
                        <div class="row">
                            <div class="col">
                                <div class="booking_title text-center"><h2>Book a room</h2></div>
                                <div class="booking_text text-center">
                                    <p>Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia Curae; Suspendisse nec faucibus velit. Quisque eleifend orci ipsum, a bibendum lacus suscipit sit. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia Curae; Suspendisse nec faucibus velit. Quisque eleifend orci ipsum, a bibendum lacus suscipit sit.</p>
                                </div>

                                <!-- Booking Slider -->
                                <div class="booking_slider_container">
                                    <div class="owl-carousel owl-theme booking_slider">

                                        <!-- Slide -->
                                        <div class="booking_item">
                                            <div class="background_image" style="background-image:url(${pageContext.request.contextPath}/Image/booking_1.jpg)"></div>
                                            <div class="booking_overlay trans_200"></div>
                                            <div class="booking_price">$120/Night</div>
                                            <div class="booking_link"><a href="booking.html">Family Room</a></div>
                                        </div>

                                        <!-- Slide -->
                                        <div class="booking_item">
                                            <div class="background_image" style="background-image:url(${pageContext.request.contextPath}/Image/booking_2.jpg)"></div>
                                            <div class="booking_overlay trans_200"></div>
                                            <div class="booking_price">$120/Night</div>
                                            <div class="booking_link"><a href="booking.html">Deluxe Room</a></div>
                                        </div>

                                        <!-- Slide -->
                                        <div class="booking_item">
                                            <div class="background_image" style="background-image:url(${pageContext.request.contextPath}/Image/booking_3.jpg)"></div>
                                            <div class="booking_overlay trans_200"></div>
                                            <div class="booking_price">$120/Night</div>
                                            <div class="booking_link"><a href="booking.html">Single Room</a></div>
                                        </div>

                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Blog -->

                <div class="blog">

                    <!-- Blog Slider -->
                    <div class="blog_slider_container">
                        <div class="owl-carousel owl-theme blog_slider">

                            <!-- Slide -->
                            <div class="blog_slide">
                                <div class="background_image" style="background-image:url(${pageContext.request.contextPath}/Image/index_blog_1.jpg)"></div>
                                <div class="blog_content">
                                    <div class="blog_date"><a href="#">Oct 20, 2018</a></div>
                                    <div class="blog_title"><a href="#">How to book your stay</a></div>
                                </div>
                            </div>

                            <!-- Slide -->
                            <div class="blog_slide">
                                <div class="background_image" style="background-image:url(${pageContext.request.contextPath}/Image/index_blog_2.jpg)"></div>
                                <div class="blog_content">
                                    <div class="blog_date"><a href="#">Oct 20, 2018</a></div>
                                    <div class="blog_title"><a href="#">10 restaurants in town</a></div>
                                </div>
                            </div>

                            <!-- Slide -->
                            <div class="blog_slide">
                                <div class="background_image" style="background-image:url(${pageContext.request.contextPath}/Image/index_blog_3.jpg)"></div>
                                <div class="blog_content">
                                    <div class="blog_date"><a href="#">Oct 20, 2018</a></div>
                                    <div class="blog_title"><a href="#">A perfect wedding</a></div>
                                </div>
                            </div>

                        </div>
                    </div>
                </div>
            </div>
                <jsp:include page="Footer.jsp" />
        </div>


    </div>

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