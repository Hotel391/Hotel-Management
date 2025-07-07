<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c"  uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        <!-- Font Awesome -->
        <link href="https://fonts.googleapis.com/css?family=Lato:300,400,700&display=swap" rel="stylesheet">

        <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css">

        <link rel="stylesheet" href="${pageContext.request.contextPath}/Css/Authentication/style.css">
    </head>
    <body class="img js-fullheight" style="background-image: url(${pageContext.request.contextPath}/Image/bg.jpg);">
        <jsp:include page="/View/Customer/Header.jsp" />
        <section class="ftco-section">
            <div class="container">
                <!--                <div class="row justify-content-center">
                                    <div class="col-md-6 text-center mb-5">
                                        <h2 class="heading-section">Login #10</h2>
                                    </div>
                                </div>-->
                <div class="row justify-content-center">
                    <div class="col-md-6 col-lg-4">
                        <div class="login-wrap p-0">
                            <h3 class="mb-4 text-center">Have an account?</h3>
                            <form method="post" action="${pageContext.request.contextPath}/login" class="signin-form">
                                <div class="form-group">
                                    <input type="text" class="form-control" name="username" value="${param.username}" placeholder="Username" required>
                                </div>
                                <div class="form-group">
                                    <input id="password-field" type="password" name="password" value="${param.password}" class="form-control" placeholder="Password" required>
                                    <span toggle="#password-field" class="fa fa-fw fa-eye field-icon toggle-password"></span>
                                </div>
                                <c:if test="${not empty error}">
                                    <p class="alert error">${error}</p>
                                </c:if>
                                <div class="form-group">
                                    <input type="hidden" name="service" value="login">
                                    <input type="hidden" name="submit" value="submit"/>
                                    <button type="submit" class="form-control btn btn-primary submit px-3">Sign In</button>
                                </div>
                                <div class="form-group d-md-flex">
                                    <div class="w-50">
                                        <a href="register" class="link-warning link-offset-2">Register</a>
                                    </div>
                                    <div class="w-50">
                                        <a href="forgotPassword" style="color: #fff">Forgot Password</a>
                                    </div>
                                </div>
                            </form>
                            <p class="w-100 text-center">&mdash; Or Sign In With &mdash;</p>
                            <div class="social d-flex text-center">
                                <a href="https://accounts.google.com/o/oauth2/auth?scope=email%20profile%20openid&redirect_uri=http://localhost:9999/fptHotel/login&response_type=code
                                   &client_id=200776812058-qrg1li14uugvdeb351am8g4savbpjnvo.apps.googleusercontent.com&approval_prompt=force&state=loginGoogle" class="px-2 py-2 mr-md-1 rounded"><span class="ion-logo-google mr-2"></span> Google</a>

                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </section>

        <script src="${pageContext.request.contextPath}/Js/jquery.min.js"></script>
        <script src="${pageContext.request.contextPath}/Js/popper.js"></script>
        <script src="${pageContext.request.contextPath}/Js/bootstrap.min.js"></script>
        <script src="${pageContext.request.contextPath}/Js/main.js"></script>
    </body>

</html>
