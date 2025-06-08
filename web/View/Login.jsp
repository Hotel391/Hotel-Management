<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c"  uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        <!-- Font Awesome -->
        <link
            href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css"
            rel="stylesheet"
            />
        <!-- Google Fonts -->
        <link
            href="https://fonts.googleapis.com/css?family=Roboto:300,400,500,700&display=swap"
            rel="stylesheet"
            />
        <!-- MDB -->
        <link
            href="https://cdn.jsdelivr.net/npm/mdb-ui-kit@9.0.0/css/mdb.min.css"
            rel="stylesheet"
            />
        <link rel="stylesheet" href="${pageContext.request.contextPath}/Css/Authentication/Login.css"/>
    </head>
    <body>
        <section class="vh-100">
            <div class="container-fluid h-custom login-content">
                <div class="row d-flex justify-content-center align-items-center h-100">
                    <div class="col-md-9 col-lg-6 col-xl-5">
                        <img src="${pageContext.request.contextPath}/Image/HotelView/HotelView2.png"
                             class="img-fluid hotel-img" alt="Hotel image">
                    </div>
                    <div class="col-md-8 col-lg-6 col-xl-4 offset-xl-1">
                        <form action="login" method="get">
                            <div class="d-flex flex-row align-items-center justify-content-center justify-content-lg-start">
                                <p class="lead fw-normal mb-0 me-3">Sign in with</p>

                                <a href="https://accounts.google.com/o/oauth2/auth?scope=email%20profile%20openid&redirect_uri=http://localhost:9999/HotelManagement/login&response_type=code
                                   &client_id=200776812058-qrg1li14uugvdeb351am8g4savbpjnvo.apps.googleusercontent.com&approval_prompt=force&state=loginGoogle">
                                    <button  type="button" class="btn btn-primary btn-floating mx-1">
                                        <i class="fab fa-google"></i>
                                    </button>
                                </a>

                            </div>

                            <div class="divider d-flex align-items-center my-4">
                                <p class="text-center fw-bold mx-3 mb-0">Or</p>
                            </div>

                            <!-- Email input -->
                            <div data-mdb-input-init class="form-outline mb-4">
                                <input type="text" 
                                       name="username" 
                                       id="form3Example3"  
                                       class="form-control form-control-lg"
                                       placeholder="Enter a valid email address"
                                       value="${param.username != null ? param.username : ''}"/>
                                <label class="form-label" for="form3Example3">Email address or username</label>
                            </div>

                            <!-- Password input -->
                            <div data-mdb-input-init class="form-outline mb-3">
                                <input type="password" 
                                       name="password" 
                                       id="form3Example4" 
                                       class="form-control form-control-lg"
                                       placeholder="Enter password"
                                       value="${param.password != null ? param.password  : ''}"/>
                                <label class="form-label" for="form3Example4">Password</label>
                            </div>

                            <c:if test="${requestScope.error != null}">
                                <div class="alert alert-danger" role="alert">
                                    ${requestScope.error}
                                </div>
                            </c:if>

                            <div class="d-flex justify-content-between align-items-center">

                                <a href="#!" class="text-body">Forgot password?</a>
                            </div>



                            <div class="text-center text-lg-start mt-4 pt-2">
                                <input type="hidden" name="service" value="login">
                                <input type="hidden" name="submit" value="submit">
                                <button  type="submit" class="btn btn-primary btn-lg"
                                         style="padding-left: 2.5rem; padding-right: 2.5rem;">Login</button>
                                <p class="small fw-bold mt-2 pt-1 mb-0">Don't have an account? <a href="register"
                                                                                                  class="link-danger">Register</a></p>
                            </div>

                        </form>

                    </div>
                </div>
            </div>
            <div
                class="d-flex flex-column flex-md-row text-center text-md-start justify-content-between py-4 px-4 px-xl-5 bg-primary">
                <!-- Copyright -->
                <div class="text-white mb-3 mb-md-0">
                    Copyright © 2020. All rights reserved.
                </div>
                <!-- Copyright -->

                <!-- Right -->
                <div>
                    <a href="#!" class="text-white me-4">
                        <i class="fab fa-facebook-f"></i>
                    </a>
                    <a href="#!" class="text-white me-4">
                        <i class="fab fa-twitter"></i>
                    </a>
                    <a href="#!" class="text-white me-4">
                        <i class="fab fa-google"></i>
                    </a>
                    <a href="#!" class="text-white">
                        <i class="fab fa-linkedin-in"></i>
                    </a>
                </div>
                <!-- Right -->
            </div>
        </section>
        <!-- MDB -->
        <script
            type="text/javascript"
            src="https://cdn.jsdelivr.net/npm/mdb-ui-kit@9.0.0/js/mdb.umd.min.js"
        ></script>
    </body>

</html>
