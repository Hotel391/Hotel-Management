<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Admin Page</title>
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
                <c:set var="title" value="Admin" scope="request"/>
                <jsp:include page="topNav.jsp" />

                <c:if test="${adminAccount.role.roleId == 0}">

                    <div class="main-content">
                        <div class="container-fluid p-4">
                            <ul class="nav nav-tabs mb-3">
                                <li class="nav-item">
                                    <a class="nav-link active" href="${pageContext.request.contextPath}/admin/page">Quản lí tài khoản manager</a>
                                </li>
                            </ul>
                            <div class="d-flex justify-content-between align-items-center mb-3">
                                <div class="d-flex gap-2">
                                    <button class="btn btn-success" data-bs-toggle="modal" data-bs-target="#addManagerAccountModal">+ Tạo mới tài khoản quản lí</button>
                                </div>
                            </div>

                            <!--thông báo thành công-->
                            <c:if test="${param.statusAction == 'true'}">
                                <div id="successAlert" class="alert alert-success alert-dismissible fade show text-center mx-auto mt-3" role="alert" style="width: fit-content;">
                                    <c:choose>
                                        <c:when test="${param.action == 'add'}">Thêm tài khoản thành công!</c:when>
                                        <c:when test="${param.action == 'delete'}">Xóa tài khoản thành công thành công!</c:when>
                                        <c:when test="${param.action == 'changeStatus'}"> Chuyển trạng thái tài khoản thành công!</c:when>
                                        <c:when test="${param.action == 'changePass'}"> Thay đổi mật khẩu tài khoản admin thành công!</c:when>
                                        <c:otherwise>Thao tác thành công!</c:otherwise>
                                    </c:choose>
                                    <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                                </div>
                            </c:if>
                            
                            <c:if test="${requestScope.statusAction == 'false'}">
                                <div id="errorAlert" class="alert alert-danger alert-dismissible fade show mt-3 mx-auto text-center" role="alert" style="width: fit-content;">
                                    <c:choose>
                                        <c:when test="${requestScope.action == 'delete'}">Xóa tài khoản thất bại!</c:when>
                                        <c:otherwise>Thao tác thành công!</c:otherwise>
                                    </c:choose>
                                    <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                                </div>
                            </c:if>

                            <div class="table-container">
                                <table class="table align-middle bg-white">
                                    <thead class="table-light">
                                        <tr>
                                            <th>Username</th>
                                            <th>Ngày tạo</th>
                                            <th>Trạng thái</th>
                                            <th>Chức vụ</th>
                                            <th>Chuyển trạng thái</th>
                                            <th>Xóa tài khoản</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach var="e" items="${requestScope.list}">
                                            <tr>
                                                <td>${e.username}</td>
                                                <td>${e.registrationDate}</td>
                                                <td>
                                                    <c:choose>
                                                        <c:when test="${e.activate == true}">
                                                            <i class="bi bi-check2-circle text-success fs-5"></i>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <i class="bi bi-x-circle text-danger fs-5"></i>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </td>
                                                <td>${e.role.roleName}</td>
                                                <td>
                                                    <a href="${pageContext.request.contextPath}/admin/page?service=activateManager&employeeID=${e.employeeId}&activate=${not e.activate}" 
                                                       onclick="return confirm('Bạn chắc chắn muốn đóng tài khoản này?');">
                                                        <button type="submit" class="btn btn-sm ${e.activate ? 'btn-warning' : 'btn-success'}" title="Chuyển trạng thái">
                                                            <i class="bi bi-power"></i>
                                                        </button>
                                                    </a>
                                                </td>
                                                <td>
                                                    <a href="${pageContext.request.contextPath}/admin/page?service=deleteManager&employeeID=${e.employeeId}" 
                                                       onclick="return confirm('Are you sure to delete?');"
                                                       class="btn btn-sm btn-danger">
                                                        <i class="bi bi-trash-fill"></i>
                                                    </a>
                                                </td>
                                            </tr>
                                        </c:forEach>
                                    </tbody>
                                </table>
                            </div>
                        </div>


                        <!-- Add Manager account Modal -->
                        <div class="modal fade" id="addManagerAccountModal" tabindex="-1" aria-labelledby="addManagerAccountModalLabel" aria-hidden="true">
                            <div class="modal-dialog">
                                <div class="modal-content">
                                    <form id="addRoleForm" method="post" action="${pageContext.request.contextPath}/admin/page" novalidate>
                                        <div class="modal-header">
                                            <h5 class="modal-title" id="addRoleModalLabel">Thêm mới tài khoản manager</h5>
                                            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                                        </div>

                                        <div class="modal-body">

                                            <div class="input-group mb-3">
                                                <span class="input-group-text" id="visible-addon">@</span>
                                                <input type="text" name="username" class="form-control" 
                                                       placeholder="Username" aria-label="Username" 
                                                       aria-describedby="visible-addon" id="userNameManager" value="${param.username}">
                                            </div>
                                            <div  style="color: red;">
                                                <c:if test="${not empty usernameError}">
                                                    <c:out value="${usernameError}" />
                                                </c:if>
                                            </div>

                                            <div class="input-group mb-3">
                                                <span class="input-group-text" id="visible-addon2">
                                                    <i class="bi bi-lock"></i>
                                                </span>
                                                <input type="password" name="password" class="form-control" id="passwordField"
                                                       placeholder="Password" aria-label="Password"
                                                       aria-describedby="visible-addon2" value="${param.password}">
                                                <span class="input-group-text" onclick="togglePassword()" style="cursor: pointer;">
                                                    <i class="bi bi-eye-slash" id="toggleIcon"></i>
                                                </span>
                                            </div>
                                            <div  style="color: red;">
                                                <c:if test="${not empty passwordError}">
                                                    <c:out value="${passwordError}" />
                                                </c:if>
                                            </div>

                                        </div>
                                        <div class="modal-footer">
                                            <input type="submit" name="submit" class="btn btn-success" value="Thêm"/>
                                            <input type="reset" name="reset" value="Reset"/>
                                            <!--<input type="hidden" name="page" value="${currentPage}" />-->
                                            <input type="hidden" name="service" value="add">
                                        </div>

                                    </form>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </c:if>
        </div>

        <script>
            document.addEventListener('DOMContentLoaded', function () {
                //thông báo thành công
                const alertBox = document.getElementById("successAlert");
                if (alertBox) {
                    setTimeout(() => {
                        // Bootstrap fade out (optional)
                        alertBox.classList.remove("show");
                        alertBox.classList.add("fade");
                        // Xóa phần tử khỏi DOM sau khi fade
                        setTimeout(() => alertBox.remove(), 500);

                        // Xoá param khỏi URL
                        const url = new URL(window.location.href);
                        url.searchParams.delete("success");
                        url.searchParams.delete("action");
                        window.history.replaceState({}, document.title, url.toString());
                    }, 3000); // Hiển thị 3s
                }
                
                //thông báo lỗi
                const errorAlertBox = document.getElementById("errorAlert");
                if (errorAlertBox) {
                    setTimeout(() => {
                        errorAlertBox.classList.remove("show");
                        errorAlertBox.classList.add("fade");
                        setTimeout(() => errorAlertBox.remove(), 500);
                    }, 3000);
                }
                
                // --- ADD MANAGER ACCOUNT MODAL ---
                const addModalEl = document.getElementById("addManagerAccountModal");
                if (addModalEl) {
                    // Khi modal đóng (click X hoặc click nền)
                    addModalEl.addEventListener('hidden.bs.modal', function () {
                        // Xóa lỗi
                        const errorElements = addModalEl.querySelectorAll("div[style='color: red;']");
                        errorElements.forEach(el => el.remove());

                        // Xóa giá trị input (ghi đè giá trị param trên giao diện)
                        document.getElementById("userNameManager").value = "";
                        document.getElementById("passwordField").value = "";
                    });

                    // Hiển thị modal nếu có lỗi (giá trị do server render)
                    const shouldShowModal = ${not empty requestScope.usernameError or not empty requestScope.passwordError};
                    if (shouldShowModal) {
                        const modal = new bootstrap.Modal(addModalEl);
                        modal.show();
                    }
                }

            });


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
