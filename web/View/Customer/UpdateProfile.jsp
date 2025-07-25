<%@page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page import="models.CustomerAccount"%>
<%@page import="models.Customer"%>

<%
    CustomerAccount ca = (CustomerAccount) request.getAttribute("customerAccount");
    String fullName = (ca != null && ca.getCustomer() != null) ? ca.getCustomer().getFullName() : "A";
    String initial = fullName.length() > 0 ? fullName.substring(0, 1).toUpperCase() : "A";
    request.setAttribute("initialLetter", initial);
    request.setAttribute("activeTab", "profile");
%>

<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Thông tin tài khoản</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
        <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.1/font/bootstrap-icons.css" rel="stylesheet">
        <style>
            body {
                background-color: #f5f7fa;
                font-family: 'Segoe UI', sans-serif;
            }

            .setting-card {
                background-color: #ffffff;
                border: 1px solid #e5eaf0;
                border-radius: 12px;
                padding: 24px;
                box-shadow: 0 2px 6px rgba(0, 0, 0, 0.03);
            }

            h5 {
                font-size: 20px;
                font-weight: 600;
                color: #212b36;
            }

            .form-label {
                font-weight: 500;
                color: #212b36;
                font-size: 14px;
            }

            .form-control,
            .form-select {
                border-radius: 8px;
                font-size: 15px;
                border: 1px solid #d1d5db;
                color: #212b36;
                background-color: #ffffff;
            }

            .form-control:focus,
            .form-select:focus {
                box-shadow: 0 0 0 0.15rem rgba(0, 113, 220, 0.25);
                border-color: #0071dc;
            }

            .btn-primary {
                background-color: #0071dc;
                border-color: #0071dc;
                font-weight: 500;
                border-radius: 8px;
                padding: 8px 24px;
            }

            .btn-primary:hover {
                background-color: #005dbb;
                border-color: #005dbb;
            }

            .btn-secondary {
                background-color: #6c757d;
                border-color: #6c757d;
                font-weight: 500;
                border-radius: 8px;
                padding: 8px 24px;
            }

            .btn-secondary:hover {
                background-color: #5c636a;
                border-color: #5c636a;
            }

            .sidebar-link {
                display: block;
                padding: 10px 12px;
                border-radius: 6px;
                color: #0071dc;
                text-decoration: none;
                font-weight: 500;
            }

            .sidebar-link:hover,
            .sidebar-link.active {
                background-color: #e8f0ff;
                font-weight: bold;
            }

            .avatar-circle {
                width: 70px;
                height: 70px;
                background-color: #0071dc;
                color: white;
                border-radius: 50%;
                display: flex;
                align-items: center;
                justify-content: center;
                font-size: 24px;
                font-weight: bold;
            }
        </style>
    </head>
    <body style="background-color: #f5f7fa;">
        <jsp:include page="Header.jsp" />

        <div class="container-xl py-4">
            <div class="row justify-content-center">
                <div class="col-lg-10 col-xl-9">
                    <div class="row">
                        <!-- Sidebar -->
                        <div class="col-md-3">
                            <jsp:include page="SidebarProfile.jsp" />
                        </div>

                        <!-- Content -->
                        <div class="col-md-9">
                            <div class="setting-card">
                                <c:if test="${type == 'update'}">
                                    <h5 class="mb-4">Cập nhật thông tin tài khoản</h5>
                                    <form method="post" action="${pageContext.request.contextPath}/customerProfile?service=update">
                                        <input type="hidden" name="service" value="update"/>
                                        <input type="hidden" name="type" value="update">
                                        <input type="hidden" name="submit" value="submit"/>
                                        <input type="hidden" name="username" value="${customerAccount.username}"/>

                                        <div class="mb-3">
                                            <label for="fullName" class="form-label">Họ và tên</label>
                                            <input type="text" class="form-control" id="fullName" name="fullName"
                                                   value="${customerAccount.customer.fullName}" required>
                                            <c:if test="${not empty fullNameError}">
                                                <div class="text-danger">${fullNameError}</div>
                                            </c:if>
                                        </div>

                                        <div class="row mb-3">
                                            <div class="col-md-6">
                                                <label for="gender" class="form-label">Giới tính</label>
                                                <select class="form-select" id="gender" name="gender">
                                                    <option value="true" ${customerAccount.customer.gender ? "selected" : ""}>Nam</option>
                                                    <option value="false" ${!customerAccount.customer.gender ? "selected" : ""}>Nữ</option>
                                                </select>
                                            </div>
                                            <div class="col-md-6">
                                                <label for="phoneNumber" class="form-label">Số điện thoại</label>
                                                <input type="text" class="form-control" id="phoneNumber" name="phoneNumber"
                                                       value="${customerAccount.customer.phoneNumber}">
                                                <c:if test="${not empty phoneError}">
                                                    <div class="text-danger">${phoneError}</div>
                                                </c:if>
                                            </div>
                                        </div>

                                        <div class="text-end">
                                            <button type="submit" class="btn btn-primary">
                                                <i class="bi bi-save"></i> Lưu thay đổi
                                            </button>
                                            <a href="${pageContext.request.contextPath}/customerProfile?service=info&username=${customerAccount.username}" class="btn btn-secondary">
                                                <i class="bi bi-arrow-left-circle"></i> Hủy
                                            </a>
                                        </div>
                                    </form>
                                </c:if>

                                <c:if test="${type == 'changePass'}">
                                    <h5 class="mb-4">Đổi mật khẩu</h5>
                                    <form method="post" action="${pageContext.request.contextPath}/customerProfile?service=changePass">
                                        <input type="hidden" name="service" value="changePass"/>
                                        <input type="hidden" name="type" value="changePass">
                                        <input type="hidden" name="submit" value="submit"/>
                                        <input type="hidden" name="username" value="${customerAccount.username}"/>

                                        <c:if test="${customerAccount.password != null}">
                                            <div class="mb-3">
                                                <label for="oldPass" class="form-label">Mật khẩu cũ</label>
                                                <div class="input-group">
                                                    <span class="input-group-text"><i class="bi bi-lock"></i></span>
                                                    <input type="password" name="oldPass" class="form-control" id="oldpasswordField"
                                                           placeholder="Nhập mật khẩu cũ" value="${param.oldPass}">
                                                    <span class="input-group-text" onclick="oldtogglePassword()" style="cursor: pointer;">
                                                        <i class="bi bi-eye-slash" id="oldToggleIcon"></i>
                                                    </span>
                                                </div>
                                                <c:if test="${not empty oldPasswordError}">
                                                    <div class="text-danger">${oldPasswordError}</div>
                                                </c:if>
                                            </div>
                                        </c:if>

                                        <div class="mb-3">
                                            <label for="newPassWord" class="form-label">Mật khẩu mới</label>
                                            <div class="input-group">
                                                <span class="input-group-text"><i class="bi bi-lock"></i></span>
                                                <input type="password" name="newPassWord" class="form-control" id="passwordField"
                                                       placeholder="Nhập mật khẩu mới" value="${param.newPassWord}">
                                                <span class="input-group-text" onclick="togglePassword()" style="cursor: pointer;">
                                                    <i class="bi bi-eye-slash" id="toggleIcon"></i>
                                                </span>
                                            </div>
                                            <c:if test="${not empty newPasswordError}">
                                                <div class="text-danger">${newPasswordError}</div>
                                            </c:if>
                                            <c:if test="${not empty passwordError}">
                                                <div class="text-danger">${passwordError}</div>
                                            </c:if>
                                        </div>

                                        <div class="mb-3">
                                            <label for="confirmPassWord" class="form-label">Xác nhận mật khẩu</label>
                                            <div class="input-group">
                                                <span class="input-group-text"><i class="bi bi-lock"></i></span>
                                                <input type="password" name="confirmPassWord" class="form-control" id="confirmPasswordField"
                                                       placeholder="Xác nhận mật khẩu mới" value="${param.confirmPassWord}">
                                                <span class="input-group-text" onclick="toggleConfirmPassword()" style="cursor: pointer;">
                                                    <i class="bi bi-eye-slash" id="confirmToggleIcon"></i>
                                                </span>
                                            </div>
                                            <c:if test="${not empty confirmPasswordError}">
                                                <div class="text-danger">${confirmPasswordError}</div>
                                            </c:if>
                                        </div>

                                        <div class="text-end">
                                            <button type="submit" class="btn btn-primary">
                                                <i class="bi bi-save"></i> Lưu thay đổi
                                            </button>
                                            <a href="${pageContext.request.contextPath}/customerProfile?service=info&username=${customerAccount.username}" class="btn btn-secondary">
                                                <i class="bi bi-arrow-left-circle"></i> Hủy
                                            </a>
                                        </div>
                                    </form>
                                </c:if>

                                <c:if test="${type == 'changeUserName'}">
                                    <h5 class="mb-4">Đổi tên người dùng</h5>
                                    <form method="post" action="${pageContext.request.contextPath}/customerProfile?service=changeUserName">
                                        <input type="hidden" name="service" value="changeUserName"/>
                                        <input type="hidden" name="type" value="changeUserName">
                                        <input type="hidden" name="submit" value="submit"/>
                                        <input type="hidden" name="customerId" value="${customerAccount.customer.customerId}"/>
                                        <input type="hidden" name="username" value="${customerAccount.username}"/>

                                        <div class="mb-3">
                                            <label for="newUserName" class="form-label">Tên người dùng mới</label>
                                            <input type="text" class="form-control" id="newUserName" name="newUserName"
                                                   value="${param.newUserName}" required>
                                            <c:if test="${not empty usernameError}">
                                                <div class="text-danger">${usernameError}</div>
                                            </c:if>
                                        </div>

                                        <div class="text-end">
                                            <button type="submit" class="btn btn-primary">
                                                <i class="bi bi-save"></i> Lưu thay đổi
                                            </button>
                                            <a href="${pageContext.request.contextPath}/customerProfile?service=info&username=${customerAccount.username}" class="btn btn-secondary">
                                                <i class="bi bi-arrow-left-circle"></i> Hủy
                                            </a>
                                        </div>
                                    </form>
                                </c:if>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <script>
            function oldtogglePassword() {
                const oldpasswordField = document.getElementById("oldpasswordField");
                const toggleIcon = document.getElementById("oldToggleIcon");
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
                const confirmToggleIcon = document.getElementById("confirmToggleIcon");
                if (confirmPasswordField.type === "password") {
                    confirmPasswordField.type = "text";
                    confirmToggleIcon.classList.remove("bi-eye-slash");
                    confirmToggleIcon.classList.add("bi-eye");
                } else {
                    confirmPasswordField.type = "password";
                    confirmToggleIcon.classList.remove("bi-eye");
                    confirmToggleIcon.classList.add("bi-eye-slash");
                }
            }
        </script>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>