<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Change password</title>
        <%--style for dashbord--%>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css" rel="stylesheet"
              integrity="sha384-EVSTQN3/azprG1Anm3QDgpJLIm9Nao0Yz1ztcQTwFspd3yD65VohhpuuCOmLASjC"
              crossorigin="anonymous">
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.1/font/bootstrap-icons.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/Css/navDashboardStyle.css" />
        <link rel="stylesheet" href="${pageContext.request.contextPath}/Css/dashboardStyle.css" />
        <link rel="stylesheet" href="${pageContext.request.contextPath}/Css/mainDashboardStyle.css" />
        <%--write more in following--%>
        <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
    </head>
    <body>
        <div class="containerBox">
            <jsp:include page="leftNav.jsp" />
            <div class="right-section">

                <!--Information account admin-->
                <c:if test="${type == 'info'}">
                    <c:set var="title" value="Information" scope="request"/>
                    <jsp:include page="topNav.jsp" />

                    <!--Information account admin-->
                    <div class="d-flex justify-content-center align-items-start mt-3" style="min-height: calc(100vh - 60px); padding-top: 10px;">
                        <div class="card shadow-sm text-dark" style="background-color: #ffffff; width: 100%; max-width: 700px;">

                            <div class="card-header bg-primary text-white">
                                <h4 class="mb-0">Customer Profile</h4>
                            </div>
                            <div class="card-body">
                                <dl class="row">
                                    <dt class="col-sm-3">Username:</dt>
                                    <dd class="col-sm-9">${employee.username}</dd>

                                    <dt class="col-sm-3">PassWord:</dt>
                                    <dd class="col-sm-9">
                                        <!-- Trường hiển thị mật khẩu (ẩn ban đầu) -->
                                        <input type="password" id="actualPassword" class="form-control" value="${employee.password}" readonly>

                                        <!-- Nhập mật khẩu để xác minh -->
                                        <div class="mt-2">
                                            <input type="password" id="verifyPass" class="form-control" placeholder="Enter password to reveal">
                                        </div>

                                        <!-- Nút xác minh -->
                                        <div class="mt-2">
                                            <button type="button" class="btn btn-sm btn-primary" onclick="verifyPassword()">Verify</button>
                                        </div>

                                        <!-- Thông báo lỗi -->
                                        <small id="errorMsg" class="text-danger"></small>
                                    </dd>

                                </dl>


                                <!--back home,update profile-->
                                <div class="mt-4 d-flex gap-2">
                                    <a href="${pageContext.request.contextPath}/developerPage" class="btn btn-secondary">
                                        <i class="bi bi-arrow-left-circle"></i> Back to Home
                                    </a>

                                    <a href="${pageContext.request.contextPath}/developerPage?service=changePass&username=${employee.username}" class="btn btn-warning">
                                        <i class="bi bi-key"></i> Change Password
                                    </a>

                                </div>
                            </div>
                        </div>
                    </div>
                </c:if>

                <!--change password-->
                <c:if test="${type == 'changepass'}">
                    <c:set var="title" value="Change Password" scope="request"/>
                    <jsp:include page="topNav.jsp" />

                    <!-- Nút Back -->
                    <div class="d-flex justify-content-end px-4 mt-3">
                        <a href="${pageContext.request.contextPath}/developerPage?service=info" class="btn btn-outline-secondary">
                            <i class="bi bi-arrow-left"></i> Back
                        </a>
                    </div>

                    <div class="container d-flex justify-content-center align-items-center" style="height: 100vh;">
                        <form action="${pageContext.request.contextPath}/developerPage" method="post" class="w-50">
                            <input type="hidden" name="service" value="changePass">
                            <input type="hidden" name="username" value="${sessionScope.username}">

                            <div class="input-group mb-3">
                                <span class="input-group-text" id="visible-addon2">
                                    <i class="bi bi-lock"></i>
                                </span>
                                <input type="password" name="oldpassword" class="form-control" id="oldpasswordField"
                                       placeholder="Old Password" aria-label="Password"
                                       aria-describedby="visible-addon2" value="${param.oldpassword}">
                                <span class="input-group-text" onclick="oldtogglePassword()" style="cursor: pointer;">
                                    <i class="bi bi-eye-slash" id="toggleIcon"></i>
                                </span>
                            </div>
                            <small class="text-danger" style="display: block; min-height: 1.2em;">
                                <c:if test="${not empty oldPasswordError}">
                                    <c:out value="${oldPasswordError}" />
                                </c:if>
                            </small>


                            <div class="input-group mb-3">
                                <span class="input-group-text" id="visible-addon2">
                                    <i class="bi bi-lock"></i>
                                </span>
                                <input type="password" name="password" class="form-control" id="passwordField"
                                       placeholder="New Password" aria-label="Password"
                                       aria-describedby="visible-addon2" value="${param.password}">
                                <span class="input-group-text" onclick="togglePassword()" style="cursor: pointer;">
                                    <i class="bi bi-eye-slash" id="toggleIcon"></i>
                                </span>
                            </div>

                            <small class="text-danger" style="display: block; min-height: 1.2em;">
                                <c:if test="${not empty passwordError}">
                                    <c:out value="${passwordError}" />
                                </c:if>
                            </small>


                            <button type="submit" class="btn btn-primary w-100">Change</button>
                        </form>
                    </div>
                </c:if>


            </div>
        </div>
        <script>
            function verifyPassword() {
                const realPass = '${employee.password}';
                const inputPass = document.getElementById('verifyPass').value;
                const field = document.getElementById('actualPassword');
                const msg = document.getElementById('errorMsg');

                if (inputPass === realPass) {
                    field.type = 'text';
                    msg.textContent = '';
                } else {
                    field.type = 'password';
                    msg.textContent = 'Incorrect password!';
                }
            }

            function oldtogglePassword() {
                const passwordField = document.getElementById("oldpasswordField");
                const toggleIcon = document.getElementById("toggleIcon");

                if (passwordField.type === "password") {
                    passwordField.type = "text";
                    toggleIcon.classList.remove("bi-eye-slash");
                    toggleIcon.classList.add("bi-eye");
                } else {
                    passwordField.type = "password";
                    toggleIcon.classList.remove("bi-eye");
                    toggleIcon.classList.add("bi-eye-slash");
                }
            }
            
            function togglePassword() {
                const passwordField = document.getElementById("passwordField");
                const toggleIcon = document.getElementById("toggleIcon");

                if (passwordField.type === "password") {
                    passwordField.type = "text";
                    toggleIcon.classList.remove("bi-eye-slash");
                    toggleIcon.classList.add("bi-eye");
                } else {
                    passwordField.type = "password";
                    toggleIcon.classList.remove("bi-eye");
                    toggleIcon.classList.add("bi-eye-slash");
                }
            }
        </script>
    </body>

    <%--script for dashbord--%>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js"></script>
    <script src="${pageContext.request.contextPath}/Js/navDashboardJs.js"></script>
    <script src="${pageContext.request.contextPath}/Js/userProfileJs.js"></script>
    <%--write more in following--%>
</html>
