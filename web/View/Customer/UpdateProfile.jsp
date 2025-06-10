<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Update Profile</title>
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
                <c:set var="title" value="Update profile" scope="request"/>
                <jsp:include page="topNav.jsp" />
                <div class="d-flex justify-content-center align-items-start mt-3" style="min-height: calc(100vh - 60px); padding-top: 10px;">

                    <div class="card shadow text-dark" style="background-color: #ffffff; width: 100%; max-width: 700px;">

                        <!--update info customer-->
                        <c:if test="${type == 'update'}">

                            <div class="card-header bg-primary text-white">
                                <h4 class="mb-0">Update Profile</h4>
                            </div>
                            <div class="card-body">
                                <form action="customerProfile?service=update" method="post">
                                    <input type="hidden" name="service" value="update"/>
                                    <input type="hidden" name="type" value="update">
                                    <input type="hidden" name="submit" value="submit"/>
                                    <input type="hidden" name="username" value="${customerAccount.username}"/>

                                    <div class="mb-3">
                                        <label class="form-label">Full Name</label>
                                        <input type="text" class="form-control" name="fullName" value="${customerAccount.customer.fullName}" required>
                                        <c:if test="${not empty fullNameError}">
                                            <div class="text-danger">${fullNameError}</div>
                                        </c:if>
                                    </div>

                                    <div class="mb-3">
                                        <label class="form-label">Phone Number</label>
                                        <input type="text" 
                                               class="form-control" 
                                               name="phoneNumber" 
                                               value="${customerAccount.customer.phoneNumber}" >
                                        <c:if test="${not empty phoneError}">
                                            <div class="text-danger">${phoneError}</div>
                                        </c:if>
                                    </div>

                                    <div class="mb-3">
                                        <label class="form-label">Gender</label>
                                        <select class="form-select" name="gender">
                                            <option value="true" ${customerAccount.customer.gender ? "selected" : ""}>Male</option>
                                            <option value="false" ${!customerAccount.customer.gender ? "selected" : ""}>Female</option>
                                        </select>
                                    </div>


                                    <div class="d-flex gap-2">
                                        <button type="submit" class="btn btn-success">
                                            <i class="bi bi-save"></i> Save Changes
                                        </button>
                                        <a href="${pageContext.request.contextPath}/customer/customerProfile?service=info&username=${customerAccount.username}" class="btn btn-secondary">
                                            <i class="bi bi-arrow-left-circle"></i> Cancel
                                        </a>
                                    </div>
                                </form>
                            </div>
                        </c:if>

                        <!--change password-->
                        <c:if test="${type == 'changePass'}">
                            <div class="card-header bg-primary text-white">
                                <h4 class="mb-0">Change password</h4>
                            </div>
                            <div class="card-body">
                                <form action="customerProfile?service=changePass" method="post">
                                    <input type="hidden" name="service" value="changePass"/>
                                    <input type="hidden" name="type" value="changePass">
                                    <input type="hidden" name="submit" value="submit"/>
                                    <input type="hidden" name="username" value="${customerAccount.username}"/>
                                    <div class="mb-3">
                                        <c:if test="${customerAccount.password != null}">
                                            <div class="input-group mb-3">
                                                <span class="input-group-text" id="visible-addon2">
                                                    <i class="bi bi-lock"></i>
                                                </span>
                                                <input type="password" name="oldPass" class="form-control" id="oldpasswordField"
                                                       placeholder="Old Password" aria-label="Password"
                                                       aria-describedby="visible-addon2" value="${param.oldpassword}">
                                                <span class="input-group-text" onclick="oldtogglePassword()" style="cursor: pointer;">
                                                    <i class="bi bi-eye-slash" id="toggleIcon"></i>
                                                </span>
                                            </div>

                                            <c:if test="${not empty oldPasswordError}">
                                                <div class="text-danger">${oldPasswordError}</div>
                                            </c:if>
                                        </c:if>

                                        <div class="input-group mb-3">
                                            <span class="input-group-text" id="visible-addon2">
                                                <i class="bi bi-lock"></i>
                                            </span>
                                            <input type="password" name="newPassWord" class="form-control" id="passwordField"
                                                   placeholder="New Password" aria-label="Password"
                                                   aria-describedby="visible-addon2" value="${param.password}">
                                            <span class="input-group-text" onclick="togglePassword()" style="cursor: pointer;">
                                                <i class="bi bi-eye-slash" id="toggleIcon"></i>
                                            </span>
                                        </div>
                                        <c:if test="${not empty passwordError}">
                                            <div class="text-danger">${passwordError}</div>
                                        </c:if>

                                        <div class="input-group mb-3">
                                            <span class="input-group-text" id="visible-addon2">
                                                <i class="bi bi-lock"></i>
                                            </span>
                                            <input type="password" name="confirmPassWord" class="form-control" id="confirmPasswordField"
                                                   placeholder="Confirm Password" aria-label="Password"
                                                   aria-describedby="visible-addon2" value="${param.confirmPassword}">
                                            <span class="input-group-text" onclick="toggleConfirmPassword()" style="cursor: pointer;">
                                                <i class="bi bi-eye-slash" id="toggleIcon"></i>
                                            </span>
                                        </div>
                                        <c:if test="${not empty confirmPasswordError}">
                                            <div class="text-danger">${confirmPasswordError}</div>
                                        </c:if>
                                        <div class="d-flex gap-2">
                                            <button type="submit" class="btn btn-success">
                                                <i class="bi bi-save"></i> Save Changes
                                            </button>
                                            <a href="${pageContext.request.contextPath}/customer/customerProfile?service=info&username=${customerAccount.username}" class="btn btn-secondary">
                                                <i class="bi bi-arrow-left-circle"></i> Cancel
                                            </a>
                                        </div>
                                    </div>
                                </form>
                            </div>
                        </c:if>

                        <!--change username-->
                        <c:if test="${type == 'changeUserName'}">
                            <div class="card-header bg-primary text-white">
                                <h4 class="mb-0">Change UserName</h4>
                            </div>
                            <div class="card-body">
                                <form action="customerProfile?service=changeUserName" method="post">
                                    <input type="hidden" name="service" value="changeUserName"/>
                                    <input type="hidden" name="type" value="changeUserName">
                                    <input type="hidden" name="submit" value="submit"/>
                                    <input type="hidden" name="customerId" value="${customerAccount.customer.customerId}"/>
                                    <input type="hidden" name="username" value="${customerAccount.username}"/>
                                    <div class="mb-3">
                                        <label class="form-label">New UserName</label>
                                        <input type="text" class="form-control" name="newUserName" value="${param.newUserName}" required>
                                        <c:if test="${not empty usernameError}">
                                            <div class="text-danger">${usernameError}</div>
                                        </c:if>
                                        <div class="d-flex gap-2">
                                            <button type="submit" class="btn btn-success">
                                                <i class="bi bi-save"></i> Save Changes
                                            </button>
                                            <a href="${pageContext.request.contextPath}/customer/customerProfile?service=info&username=${customerAccount.username}" class="btn btn-secondary">
                                                <i class="bi bi-arrow-left-circle"></i> Cancel
                                            </a>
                                        </div>
                                    </div>
                                </form>
                            </div>
                        </c:if>

                    </div>
                </div>
            </div>
        </div>

        <script>

            function oldtogglePassword() {
                const oldpasswordField = document.getElementById("oldpasswordField");
                const toggleIcon = document.getElementById("toggleIcon");

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
                const toggleIcon = document.getElementById("toggleIcon");

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

