<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="vn">
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
        <link rel="stylesheet" href="${pageContext.request.contextPath}/Css/Authentication/Register.css"/>
    </head>
    <body>
        <!-- Section: Design Block -->
        <section class="text-center text-lg-start">

            <!-- Jumbotron -->
            <div class="container py-4 ">
                <div class="row g-0 align-items-center">
                    <div class="col-lg-6 mb-5 mb-lg-0">
                        <div class="card cascading-right bg-body-tertiary" style="
                             backdrop-filter: blur(30px);
                             ">
                            <div class="card-body p-5 shadow-5 text-center">
                                <h2 class="fw-bold mb-5">Sign up now</h2>
                                <form method="post" action="${pageContext.request.contextPath}/register">
                                    <!-- full name -->
                                    <div data-mdb-input-init class="form-outline mb-4">
                                        <input type="text" name="fullname" class="form-control" />
                                        <label class="form-label">Fullname</label>
                                        <div class="error-message">${errorFullname}</div>
                                    </div>

                                    <!-- Email input -->
                                    <div data-mdb-input-init class="form-outline mb-4">
                                        <input type="text" name="email"" class="form-control" />
                                        <label class="form-label">Email address</label>
                                    </div>

                                    <!-- Password input -->
                                    <div data-mdb-input-init class="form-outline mb-4">
                                        <input type="text" name="username" class="form-control" />
                                        <label class="form-label">Username</label>
                                    </div>

                                    <!-- Password input -->
                                    <div data-mdb-input-init class="form-outline mb-4">
                                        <input type="password" name="password" class="form-control" />
                                        <label class="form-label">Password</label>
                                    </div>

                                    <!-- Confirm Password input -->
                                    <div data-mdb-input-init class="form-outline mb-4">
                                        <input name="confirmPassword" type="password" class="form-control" />
                                        <label class="form-label">Confirm Password</label>
                                    </div>

                                    <div data-mdb-input-init class="form-outline mb-4">
                                        <input type="radio" name="Gender" value="0" checked="checked" /> Male
                                        <input type="radio" name="Gender" value="1" /> Female
                                    </div>

                                    <!-- Submit button -->
                                    <button type="submit" data-mdb-ripple-init class="btn btn-primary btn-block mb-4">
                                        Sign up
                                    </button>

                                    <!-- Register buttons -->
                                    <div class="text-center">
                                        <span>Or sign up with:</span>
                                        <button  type="button" data-mdb-button-init data-mdb-ripple-init 
                                                 class="btn btn-link btn-floating mx-1 margin-buttom">
                                            <i class="fab fa-google"></i>
                                        </button>
                                    </div>
                                </form>
                            </div>
                        </div>
                    </div>

                    <div class="col-lg-6 mb-5 mb-lg-0 hotel-img">
                        <img src="${pageContext.request.contextPath}/Image/HotelView/HotelView2.png" class="w-100 rounded-4 shadow-4"
                             alt="" />
                    </div>
                    <form method="post">
                        <input type="text" name="fullname"/> Fullname
                        <button type="submit">google</button>
                    </form>
                </div>
            </div>
            <!-- Jumbotron -->
        </section>
        <!-- Section: Design Block -->
        <!-- MDB -->
        <script
            type="text/javascript"
            src="https://cdn.jsdelivr.net/npm/mdb-ui-kit@9.0.0/js/mdb.umd.min.js"
        ></script>
    </body>
</html>
