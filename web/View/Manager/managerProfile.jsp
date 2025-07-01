<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<fmt:formatDate value="${manager.dateOfBirth}" pattern="yyyy-MM-dd" var="dobFormatted"/>
<fmt:formatDate value="${manager.registrationDate}" pattern="yyyy-MM-dd" var="regFormatted"/>

<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <title>Manager Profile</title>
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
                <a href="${pageContext.request.contextPath}/manager/dashboard" class="btn btn-outline-primary btn-sm">
                    <i class="bi bi-arrow-left"></i> Back
                </a>
            </div>

            <div class="card shadow-sm p-3">
                <h4 class="text-center text-primary mb-3">Manager Profile</h4>

                <c:if test="${not empty error}">
                    <div class="alert alert-danger">${error}</div>
                </c:if>
                <c:if test="${not empty success}">
                    <div class="alert alert-success">${success}</div>
                </c:if>

                <form action="${pageContext.request.contextPath}/managerProfile" method="post" onsubmit="return confirmSave()">
                    <input type="hidden" name="employeeId" value="${manager.employeeId}">

                    <!-- Username -->
                    <div class="mb-2">
                        <label class="form-label small">User Name</label>
                        <input type="text" name="username"
                               value="${param.username != null ? param.username : manager.username}"
                               class="form-control form-control-sm" required>
                        <c:if test="${not empty usernameError}">
                            <small class="text-danger">${usernameError}</small>
                        </c:if>
                    </div>

                    <!-- Full Name -->
                    <div class="mb-2">
                        <label class="form-label small">Full Name</label>
                        <input type="text" name="fullName"
                               value="${param.fullName != null ? param.fullName : manager.fullName}"
                               class="form-control form-control-sm" required>
                        <c:if test="${not empty fullNameError}">
                            <small class="text-danger">${fullNameError}</small>
                        </c:if>
                    </div>

                    <!-- Address -->
                    <div class="mb-2">
                        <label class="form-label small">Address</label>
                        <input type="text" name="address"
                               value="${param.address != null ? param.address : manager.address}"
                               class="form-control form-control-sm" required>
                        <c:if test="${not empty addressError}">
                            <small class="text-danger">${addressError}</small>
                        </c:if>
                    </div>

                    <!-- Phone Number -->
                    <div class="mb-2">
                        <label class="form-label small">Phone Number</label>
                        <input type="text" name="phoneNumber"
                               value="${param.phoneNumber != null ? param.phoneNumber : manager.phoneNumber}"
                               class="form-control form-control-sm" required>
                        <c:if test="${not empty phoneNumberError}">
                            <small class="text-danger">${phoneNumberError}</small>
                        </c:if>
                    </div>

                    <!-- Email -->
                    <div class="mb-2">
                        <label class="form-label small">Email</label>
                        <input type="email" name="email"
                               value="${param.email != null ? param.email : manager.email}"
                               class="form-control form-control-sm" required>
                        <c:if test="${not empty emailError}">
                            <small class="text-danger">${emailError}</small>
                        </c:if>
                    </div>

                    <!-- Gender -->
                    <div class="mb-2">
                        <label class="form-label small">Gender</label>
                        <select name="gender" class="form-select form-select-sm" required>
                            <option value="Nam"
                                    ${param.gender != null ? (param.gender eq 'Nam' ? 'selected' : '') : (manager.gender ? 'selected' : '')}>
                                Nam
                            </option>
                            <option value="Nữ"
                                    ${param.gender != null ? (param.gender eq 'Nữ' ? 'selected' : '') : (!manager.gender ? 'selected' : '')}>
                                Nữ
                            </option>
                        </select>
                    </div>

                    <!-- CCCD -->
                    <div class="mb-2">
                        <label class="form-label small">CCCD</label>
                        <input type="text" name="CCCD"
                               value="${param.CCCD != null ? param.CCCD : manager.CCCD}"
                               class="form-control form-control-sm" required>
                        <c:if test="${not empty cccdError}">
                            <small class="text-danger">${cccdError}</small>
                        </c:if>
                    </div>

                    <!-- Date Of Birth -->
                    <div class="mb-3">
                        <label class="form-label small">Date Of Birth</label>
                        <input type="date" name="dateOfBirth"
                               value="${param.dateOfBirth != null ? param.dateOfBirth : manager.dateOfBirth}"
                               class="form-control form-control-sm" required>
                        <c:if test="${not empty dateOfBirthError}">
                            <small class="text-danger">${dateOfBirthError}</small>
                        </c:if>
                    </div>

                    <!-- Registration Date -->
                    <div class="mb-2">
                        <label class="form-label small">Registration Date</label>
                        <input type="date" value="${manager.registrationDate}"
                               class="form-control form-control-sm" readonly>
                    </div>

                    <!-- Role -->
                    <div class="mb-3">
                        <label class="form-label small">Role</label>
                        <input type="text" value="${manager.role.roleName}"
                               class="form-control form-control-sm" readonly>
                    </div>

                    <div class="d-flex justify-content-between">
                        <button type="submit" name="action" value="updateprofile" class="btn btn-success btn-sm">
                            <i class="bi bi-save"></i> Save Changes
                        </button>
                        <button type="button" class="btn btn-outline-secondary btn-sm" data-bs-toggle="modal" data-bs-target="#passwordModal">
                            <i class="bi bi-key"></i> Change Password
                        </button>
                    </div>
                </form>


                <!-- Password Modal -->
                <div class="modal fade" id="passwordModal" tabindex="-1" aria-labelledby="passwordModalLabel" aria-hidden="true">
                    <div class="modal-dialog">
                        <div class="modal-content">
                            <form action="${pageContext.request.contextPath}/managerProfile" method="post" onsubmit="return validatePasswordForm()">
                                <div class="modal-header">
                                    <h5 class="modal-title">Change Password</h5>
                                    <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                                </div>
                                <div class="modal-body">
                                    <input type="hidden" name="employeeId" value="${manager.employeeId}">
                                    <div class="mb-3">
                                        <label class="form-label">Current Password</label>
                                        <input type="password" name="currentPassword" class="form-control" required>
                                    </div>
                                    <div class="mb-3">
                                        <label class="form-label">New Password</label>
                                        <input type="password" id="newPassword" name="newPassword" class="form-control" required>
                                    </div>
                                    <div class="mb-3">
                                        <label class="form-label">Confirm New Password</label>
                                        <input type="password" id="confirmPassword" name="confirmPassword" class="form-control" required>
                                    </div>
                                </div>
                                <div class="modal-footer">
                                    <button type="button" class="btn btn-secondary btn-sm" data-bs-dismiss="modal">Cancel</button>
                                    <button type="submit" name="action" value="changepassword" class="btn btn-success btn-sm">Update</button>
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

            function validatePasswordForm() {
                const newPass = document.getElementById("newPassword").value;
                const confirmPass = document.getElementById("confirmPassword").value;
                if (newPass !== confirmPass) {
                    alert("Mật khẩu xác nhận không trùng khớp!");
                    return false;
                }
                return true;
            }
        </script>
    </body>
</html>
