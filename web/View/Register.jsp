<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!doctype html>
<html lang="en">

    <head>
        <title>Register</title>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css" rel="stylesheet"/>
        <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet"/>
        <link href="https://fonts.googleapis.com/css?family=Lato:300,400,700&display=swap" rel="stylesheet">

        <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css">

        <link rel="stylesheet" href="${pageContext.request.contextPath}/Css/Authentication/Register.css"/>
        <style>
            .form-group{
                width: 450px;
            }
        </style>
    </head>

    <body class="img js-fullheight" style="background-image: url(${pageContext.request.contextPath}/Image/bg.jpg);">
        <jsp:include page="/View/Customer/Header.jsp" />
        <section class="ftco-section">
            <div class="container">
                <div class="row justify-content-center">
                    <div class="col-md-6 text-center mb-5">
                        <h2 class="heading-section">Register</h2>
                    </div>
                </div>
                <div class="row justify-content-center">
                    <div class="col-md-6 col-lg-4">
                        <div class="login-wrap p-0">
                            <form method="post">
                                <div class="form-group">
                                    <input name="fullname" type="text" class="form-control" placeholder="Full Name" value="${param.fullname}" required>
                                    <div class="error-message">${errorFullName}</div>
                                </div>
                                <div class="form-group">
                                    <input name="email" type="text" class="form-control" placeholder="Email Address" value="${param.email}" required>
                                    <div class="error-message">${errorEmail}</div>
                                </div>
                                <div class="form-group">
                                    <input name="username" type="text" class="form-control" placeholder="Username" value="${param.username}" required>
                                    <div class="error-message">${errorUsername}</div>
                                </div>
                                <div class="form-group">
                                    <input id="password-field" name="password" type="password" class="form-control" placeholder="Password" value="${param.password}" required>
                                    <span toggle="#password-field"
                                          class="fa fa-fw fa-eye field-icon toggle-password"></span>
                                    <div class="error-message">${errorPassword}</div>
                                </div>
                                <div class="form-group">
                                    <input id="confirm-password-field" name="confirmPassword" type="password" class="form-control"
                                           placeholder="Confirm Password" value="${param.confirmPassword}" required>
                                    <span toggle="#confirm-password-field"
                                          class="fa fa-fw fa-eye field-icon toggle-password"></span>
                                    <div class="error-message">${errorConfirmPassword}</div>
                                </div>
                                <div class="form-group">
                                    <div class="custom-control custom-radio custom-control-inline">
                                        <input type="radio" id="gender-male" name="gender" value="0" class="custom-control-input" ${param.gender ne '1' ? 'checked="checked"' : ''}>
                                        <label class="custom-control-label" for="gender-male">Male</label>
                                    </div>
                                    <div class="custom-control custom-radio custom-control-inline">
                                        <input type="radio" id="gender-female" name="gender" value="1" class="custom-control-input" ${param.gender eq '1' ? 'checked="checked"' : ''}>
                                        <label class="custom-control-label" for="gender-female">Female</label>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <button type="submit" class="form-control btn btn-primary submit px-3">Register</button>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </section>

        <script src="Js/jquery.min.js"></script>
        <script src="Js/popper.js"></script>
        <script src="Js/bootstrap.min.js"></script>
        <script src="Js/main.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js"></script>
    </body>

</html>