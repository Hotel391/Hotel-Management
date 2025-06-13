
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Receptionist Profile</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz" crossorigin="anonymous"></script>
    <style>
        body {
            background-color: #f8f9fa;
        }
        .container {
            max-width: 600px;
            margin-top: 20px;
        }
        .card-header {
            background-color: #007bff;
            color: white;
            text-align: center;
        }
        .readonly-field {
            background-color: #f8f9fa;
            border: none;
        }
        .editable-field {
            border: 1px solid #ced4da;
        }
        .btn-container {
            margin-top: 20px;
        }
        .alert {
            margin-bottom: 15px;
        }
    </style>
</head>
<body class="bg-light">
    <div class="container mt-4">
        <a href="${pageContext.request.contextPath}/receptionistPage" class="btn btn-outline-primary mb-3">Back to Dashboard</a>
        <div class="card shadow-sm">
            <div class="card-header">
                <h3 class="mb-0">Receptionist Profile</h3>
            </div>
            <div class="card-body">
                <c:if test="${not empty error}">
                    <div class="alert alert-danger alert-dismissible fade show" role="alert">
                        ${error}
                        <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                    </div>
                </c:if>
                <c:if test="${not empty success}">
                    <div class="alert alert-success alert-dismissible fade show" role="alert">
                        ${success}
                        <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                    </div>
                </c:if>

                <!-- Profile Form -->
                <form action="${pageContext.request.contextPath}/receptionist/profile" method="post" id="profileForm">
                    <input type="hidden" name="employeeId" value="${receptionist.employeeId}">

                    <!-- Username -->
                    <div class="row mb-3">
                        <label class="col-sm-3 col-form-label">User Name</label>
                        <div class="col-sm-9">
                            <c:choose>
                                <c:when test="${isEditing}">
                                    <input type="text" name="username" value="${receptionist.username}" class="form-control editable-field" required>
                                </c:when>
                                <c:otherwise>
                                    <input type="text" name="username" value="${receptionist.username}" class="form-control readonly-field" readonly required>
                                </c:otherwise>
                            </c:choose>
                            <c:if test="${not empty usernameError}">
                                <small class="text-danger">${usernameError}</small>
                            </c:if>
                        </div>
                    </div>

                    <!-- Full Name -->
                    <div class="row mb-3">
                        <label class="col-sm-3 col-form-label">Full Name</label>
                        <div class="col-sm-9">
                            <c:choose>
                                <c:when test="${isEditing}">
                                    <input type="text" name="fullName" value="${receptionist.fullName}" class="form-control editable-field" required>
                                </c:when>
                                <c:otherwise>
                                    <input type="text" name="fullName" value="${receptionist.fullName}" class="form-control readonly-field" readonly required>
                                </c:otherwise>
                            </c:choose>
                            <c:if test="${not empty fullNameError}">
                                <small class="text-danger">${fullNameError}</small>
                            </c:if>
                        </div>
                    </div>

                    <!-- Address -->
                    <div class="row mb-3">
                        <label class="col-sm-3 col-form-label">Address</label>
                        <div class="col-sm-9">
                            <c:choose>
                                <c:when test="${isEditing}">
                                    <input type="text" name="address" value="${receptionist.address}" class="form-control editable-field" required>
                                </c:when>
                                <c:otherwise>
                                    <input type="text" name="address" value="${receptionist.address}" class="form-control readonly-field" readonly required>
                                </c:otherwise>
                            </c:choose>
                            <c:if test="${not empty addressError}">
                                <small class="text-danger">${addressError}</small>
                            </c:if>
                        </div>
                    </div>

                    <!-- Phone Number -->
                    <div class="row mb-3">
                        <label class="col-sm-3 col-form-label">Phone Number</label>
                        <div class="col-sm-9">
                            <c:choose>
                                <c:when test="${isEditing}">
                                    <input type="text" name="phoneNumber" value="${receptionist.phoneNumber}" class="form-control editable-field" pattern="[0-9]{10,15}" required>
                                </c:when>
                                <c:otherwise>
                                    <input type="text" name="phoneNumber" value="${receptionist.phoneNumber}" class="form-control readonly-field" readonly required>
                                </c:otherwise>
                            </c:choose>
                            <c:if test="${not empty phoneNumberError}">
                                <small class="text-danger">${phoneNumberError}</small>
                            </c:if>
                        </div>
                    </div>

                    <!-- Email -->
                    <div class="row mb-3">
                        <label class="col-sm-3 col-form-label">Email</label>
                        <div class="col-sm-9">
                            <input type="email" name="email" value="${receptionist.email}" class="form-control readonly-field" readonly>
                        </div>
                    </div>

                    <!-- Gender -->
                    <div class="row mb-3">
                        <label class="col-sm-3 col-form-label">Gender</label>
                        <div class="col-sm-9">
                            <input type="text" value="${receptionist.gender ? 'Nam' : 'Nữ'}" class="form-control readonly-field" readonly>
                        </div>
                    </div>

                    <!-- CCCD -->
                    <div class="row mb-3">
                        <label class="col-sm-3 col-form-label">CCCD</label>
                        <div class="col-sm-9">
                            <input type="text" value="${receptionist.CCCD}" class="form-control readonly-field" readonly>
                        </div>
                    </div>

                    <!-- Registration Date -->
                    <div class="row mb-3">
                        <label class="col-sm-3 col-form-label">Registration Date</label>
                        <div class="col-sm-9">
                            <input type="text" value="${receptionist.registrationDate}" class="form-control readonly-field" readonly>
                        </div>
                    </div>

                    <!-- Date of Birth -->
                    <div class="row mb-3">
                        <label class="col-sm-3 col-form-label">Date of Birth</label>
                        <div class="col-sm-9">
                            <input type="text" value="${receptionist.dateOfBirth}" class="form-control readonly-field" readonly>
                        </div>
                    </div>

                    <!-- Role -->
                    <div class="row mb-3">
                        <label class="col-sm-3 col-form-label">Role</label>
                        <div class="col-sm-9">
                            <input type="text" value="${receptionist.role.roleName}" class="form-control readonly-field" readonly>
                        </div>
                    </div>

                    <!-- Action Buttons -->
                    <div class="btn-container d-flex justify-content-end gap-2">
                        <c:if test="${not isEditing}">
                            <a href="${pageContext.request.contextPath}/view/receptionist/profile?action=updateprofile" class="btn btn-primary">Update Profile</a>
                            <button type="button" class="btn btn-outline-secondary" data-bs-toggle="modal" data-bs-target="#passwordModal">Change Password</button>
                        </c:if>
                        <c:if test="${isEditing}">
                            <button type="submit" name="action" value="updateprofile" class="btn btn-success">Save</button>
                            <a href="${pageContext.request.contextPath}/view/receptionist/receptionistProfile" class="btn btn-secondary">Cancel</a>
                        </c:if>
                    </div>
                </form>

                <!-- Password Change Modal -->
                <div class="modal fade" id="passwordModal" tabindex="-1" aria-labelledby="passwordModalLabel" aria-hidden="true">
                    <div class="modal-dialog">
                        <div class="modal-content">
                            <div class="modal-header">
                                <h5 class="modal-title" id="passwordModalLabel">Change Password</h5>
                                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                            </div>
                            <div class="modal-body">
                                <form action="${pageContext.request.contextPath}/receptionist/receptionistProfile" method="post" onsubmit="return validatePasswordForm()">
                                    <input type="hidden" name="employeeId" value="${receptionist.employeeId}">
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
                                    <div class="modal-footer">
                                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                                        <button type="submit" name="action" value="changepassword" class="btn btn-success">Update</button>
                                    </div>
                                </form>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    
</body>
</html>

