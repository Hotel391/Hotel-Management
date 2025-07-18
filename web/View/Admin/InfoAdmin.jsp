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
                <!--change password-->
                    <c:set var="title" value="Change Password" scope="request"/>
                    <jsp:include page="topNav.jsp" />

                    <!-- Nút Back -->
                    <div class="d-flex justify-content-end px-4 mt-3">
                        <a href="${pageContext.request.contextPath}/admin/page?service=viewAll" class="btn btn-outline-secondary">
                            <i class="bi bi-arrow-left"></i> Back
                        </a>
                    </div>

                    <div class="container d-flex justify-content-center align-items-center" style="height: 100vh;">
                        <form action="${pageContext.request.contextPath}/admin/page" method="post" class="w-50">
                            <input type="hidden" name="service" value="changePass">
                            <input type="hidden" name="username" value="${sessionScope.employeeInfo.username}">

                            <div class="input-group mb-3">
                                <span class="input-group-text" id="visible-addon2">
                                    <i class="bi bi-lock"></i>
                                </span>
                                <input type="password" name="oldpassword" class="form-control" id="oldpasswordField"
                                       placeholder="Old Password" aria-label="Password"
                                       aria-describedby="visible-addon2" value="${param.oldpassword}">
                                <span class="input-group-text" onclick="oldtogglePassword()" style="cursor: pointer;">
                                    <i class="bi bi-eye-slash" id="toggleOldIcon"></i>
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
                                
                            <div class="input-group mb-3">
                                <span class="input-group-text" id="visible-addon2">
                                    <i class="bi bi-lock"></i>
                                </span>
                                <input type="password" name="confirmPassword" class="form-control" id="confirmPasswordField"
                                       placeholder="Confirm Password" aria-label="Password"
                                       aria-describedby="visible-addon2" value="${param.confirmPassword}">
                                <span class="input-group-text" onclick="toggleConfirmPassword()" style="cursor: pointer;">
                                    <i class="bi bi-eye-slash" id="toggleConfirmIcon"></i>
                                </span>
                            </div>

                            <small class="text-danger" style="display: block; min-height: 1.2em;">
                                <c:if test="${not empty confirmPasswordError}">
                                    <c:out value="${confirmPasswordError}" />
                                </c:if>
                            </small>


                            <button type="submit" class="btn btn-primary w-100">Change</button>
                        </form>
                    </div>
            </div>
        </div>
        <script>

            function oldtogglePassword() {
                const oldpasswordField = document.getElementById("oldpasswordField");
                const toggleIcon = document.getElementById("toggleOldIcon");

                if (oldpasswordField.type === "password") {
                    oldpasswordField.type = "text";
                    toggleIcon.classList.remove("bi-eye-slash");
                    toggleIcon.classList.add("bi-eye");
                } else {
                    oldpasswordField.type = "password";
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
            
            function toggleConfirmPassword() {
                const confirmPasswordField = document.getElementById("confirmPasswordField");
                const toggleIcon = document.getElementById("toggleConfirmIcon");

                if (confirmPasswordField.type === "password") {
                    confirmPasswordField.type = "text";
                    toggleIcon.classList.remove("bi-eye-slash");
                    toggleIcon.classList.add("bi-eye");
                } else {
                    confirmPasswordField.type = "password";
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
