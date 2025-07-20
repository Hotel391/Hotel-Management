<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<fmt:formatDate value="${receptionist.dateOfBirth}" pattern="yyyy-MM-dd" var="dobFormatted"/>
<fmt:formatDate value="${receptionist.registrationDate}" pattern="yyyy-MM-dd" var="regFormatted"/>

<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <title>Hồ Sơ Lễ Tân</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.5/font/bootstrap-icons.css" rel="stylesheet">
        <style>
            .card {
                max-width: 600px;
                margin: auto;
                border-radius: 15px;
            }
            input[readonly] {
                background-color: #f9f9f9;
            }
        </style>
    </head>
    <body class="bg-light">
        <div class="container py-4">
            <div class="mb-3 text-center">
                <a href="${pageContext.request.contextPath}/receptionist/page" class="btn btn-outline-primary btn-sm">
                    <i class="bi bi-arrow-left"></i> Quay Lại
                </a>
            </div>

            <div class="card shadow-sm p-3">
                <h4 class="text-center text-primary mb-3">Hồ Sơ Lễ Tân</h4>

                <c:if test="${not empty error}">
                    <div class="alert alert-danger">${error}</div>
                </c:if>
                <c:if test="${not empty success}">
                    <div class="alert alert-success">${success}</div>
                </c:if>

                <form action="${pageContext.request.contextPath}/receptionist/profile" method="post" onsubmit="return confirmSave()">
                    <input type="hidden" name="employeeId" value="${receptionist.employeeId}">

                    <!-- Tên Đăng Nhập -->
                    <div class="mb-2">
                        <label class="form-label small">Tên Đăng Nhập</label>
                        <input type="text" name="username"
                               value="${param.username != null ? param.username : receptionist.username}"
                               class="form-control form-control-sm" required>
                        <c:if test="${not empty usernameError}">
                            <small class="text-danger">${usernameError}</small>
                        </c:if>
                    </div>

                    <!-- Họ và Tên -->
                    <div class="mb-2">
                        <label class="form-label small">Họ và Tên</label>
                        <input type="text" name="fullName"
                               value="${param.fullName != null ? param.fullName : receptionist.fullName}"
                               class="form-control form-control-sm" required>
                        <c:if test="${not empty fullNameError}">
                            <small class="text-danger">${fullNameError}</small>
                        </c:if>
                    </div>

                    <!-- Địa Chỉ -->
                    <div class="mb-2">
                        <label class="form-label small">Địa Chỉ</label>
                        <input type="text" name="address"
                               value="${param.address != null ? param.address : receptionist.address}"
                               class="form-control form-control-sm" required>
                        <c:if test="${not empty addressError}">
                            <small class="text-danger">${addressError}</small>
                        </c:if>
                    </div>

                    <!-- Số Điện Thoại -->
                    <div class="mb-2">
                        <label class="form-label small">Số Điện Thoại</label>
                        <input type="text" name="phoneNumber"
                               value="${param.phoneNumber != null ? param.phoneNumber : receptionist.phoneNumber}"
                               class="form-control form-control-sm" required>
                        <c:if test="${not empty phoneNumberError}">
                            <small class="text-danger">${phoneNumberError}</small>
                        </c:if>
                    </div>

                    <!-- Email -->
                    <div class="mb-2">
                        <label class="form-label small">Email</label>
                        <input type="email" name="email"
                               value="${param.email != null ? param.email : receptionist.email}"
                               class="form-control form-control-sm" required>
                        <c:if test="${not empty emailError}">
                            <small class="text-danger">${emailError}</small>
                        </c:if>
                    </div>

                    <!-- Giới Tính -->
                    <div class="mb-2">
                        <label class="form-label small">Giới Tính</label>
                        <select name="gender" class="form-select form-select-sm" required>
                            <option value="Nam"
                                    ${param.gender != null ? (param.gender eq 'Nam' ? 'selected' : '') : (receptionist.gender ? 'selected' : '')}>
                                Nam
                            </option>
                            <option value="Nữ"
                                    ${param.gender != null ? (param.gender eq 'Nữ' ? 'selected' : '') : (!receptionist.gender ? 'selected' : '')}>
                                Nữ
                            </option>
                        </select>
                        <c:if test="${not empty genderError}">
                            <small class="text-danger">${genderError}</small>
                        </c:if>
                    </div>

                    <!-- CCCD -->
                    <div class="mb-2">
                        <label class="form-label small">CCCD</label>
                        <input type="text" name="CCCD"
                               value="${param.CCCD != null ? param.CCCD : receptionist.CCCD}"
                               class="form-control form-control-sm" required>
                        <c:if test="${not empty cccdError}">
                            <small class="text-danger">${cccdError}</small>
                        </c:if>
                    </div>

                    <!-- Ngày Sinh -->
                    <div class="mb-3">
                        <label class="form-label small">Ngày Sinh</label>
                        <input type="date" name="dateOfBirth"
                               value="${param.dateOfBirth != null ? param.dateOfBirth : dobFormatted}"
                               class="form-control form-control-sm" required>
                        <c:if test="${not empty dateOfBirthError}">
                            <small class="text-danger">${dateOfBirthError}</small>
                        </c:if>
                    </div>

                    <!-- Ngày Đăng Ký -->
                    <div class="mb-2">
                        <label class="form-label small">Ngày Đăng Ký</label>
                        <input type="date" value="${regFormatted}"
                               class="form-control form-control-sm" readonly>
                    </div>

                    <!-- Vai Trò -->
                    <div class="mb-3">
                        <label class="form-label small">Vai Trò</label>
                        <input type="text" value="${receptionist.role.roleName}"
                               class="form-control form-control-sm" readonly>
                    </div>

                    <div class="d-flex justify-content-between">
                        <button type="submit" name="action" value="updateprofile" class="btn btn-success btn-sm">
                            <i class="bi bi-save"></i> Lưu Thay Đổi
                        </button>
                        <button type="button" class="btn btn-outline-secondary btn-sm" data-bs-toggle="modal" data-bs-target="#passwordModal">
                            <i class="bi bi-key"></i> Đổi Mật Khẩu
                        </button>
                    </div>
                </form>

                <!-- Modal Đổi Mật Khẩu -->
                <div class="modal fade" id="passwordModal" tabindex="-1" aria-labelledby="passwordModalLabel" aria-hidden="true">
                    <div class="modal-dialog">
                        <div class="modal-content">
                            <form action="${pageContext.request.contextPath}/managerProfile" method="post" onsubmit="return validatePasswordForm()">
                                <div class="modal-header">
                                    <h5 class="modal-title">Đổi Mật Khẩu</h5>
                                    <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                                </div>
                                <div class="modal-body">
                                    <input type="hidden" name="employeeId" value="${manager.employeeId}">

                                    <c:if test="${not empty passwordError}">
                                        <div class="alert alert-danger alert-dismissible fade show" role="alert">
                                            ${passwordError}
                                            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                                        </div>
                                    </c:if>

                                    <div class="mb-3 password-toggle">
                                        <label class="form-label">Mật Khẩu Hiện Tại</label>
                                        <div class="input-group">
                                            <input type="password" name="currentPassword" id="currentPassword" 
                                                   class="form-control" value="${param.currentPassword}" required>
                                            <span class="input-group-text">
                                                <i class="bi bi-eye-slash" onclick="togglePassword('currentPassword')"></i>
                                            </span>
                                        </div>
                                        <c:if test="${not empty currentPasswordError}">
                                            <small class="text-danger">${currentPasswordError}</small>
                                        </c:if>
                                    </div>
                                    <div class="mb-3 password-toggle">
                                        <label class="form-label">Mật Khẩu Mới</label>
                                        <div class="input-group">
                                            <input type="password" id="newPassword" name="newPassword" 
                                                   class="form-control" value="${param.newPassword}" required>
                                            <span class="input-group-text">
                                                <i class="bi bi-eye-slash" onclick="togglePassword('newPassword')"></i>
                                            </span>
                                        </div>
                                        <c:if test="${not empty newPasswordError}">
                                            <small class="text-danger">${newPasswordError}</small>
                                        </c:if>
                                    </div>
                                    <div class="mb-3 password-toggle">
                                        <label class="form-label">Xác Nhận Mật Khẩu Mới</label>
                                        <div class="input-group">
                                            <input type="password" id="confirmPassword" name="confirmPassword" 
                                                   class="form-control" value="${param.confirmPassword}" required>
                                            <span class="input-group-text">
                                                <i class="bi bi-eye-slash" onclick="togglePassword('confirmPassword')"></i>
                                            </span>
                                        </div>
                                        <c:if test="${not empty confirmPasswordError}">
                                            <small class="text-danger">${confirmPasswordError}</small>
                                        </c:if>
                                    </div>
                                </div>
                                <div class="modal-footer">
                                    <button type="button" class="btn btn-secondary btn-sm" data-bs-dismiss="modal">Hủy</button>
                                    <button type="submit" name="action" value="changepassword" class="btn btn-success btn-sm">Cập Nhật</button>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <script>
            function confirmSave() {
                return confirm("Bạn có chắc chắn muốn lưu thay đổi?");
            }
            function togglePassword(fieldId) {
                const input = document.getElementById(fieldId);
                const icon = input.nextElementSibling.querySelector('i');
                if (input.type === "password") {
                    input.type = "text";
                    icon.classList.remove("bi-eye-slash");
                    icon.classList.add("bi-eye");
                } else {
                    input.type = "password";
                    icon.classList.remove("bi-eye");
                    icon.classList.add("bi-eye-slash");
                }
            }

            <c:if test="${not empty passwordError || not empty currentPasswordError || 
                          not empty newPasswordError || not empty confirmPasswordError}">
            document.addEventListener('DOMContentLoaded', function () {
                const passwordModal = new bootstrap.Modal(document.getElementById('passwordModal'));
                passwordModal.show();
            });
            </c:if>
        </script>
    </body>
</html>