<%-- 
    Document   : Employee
    Created on : Jun 1, 2025, 11:47:00 AM
    Author     : SONNAM
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8" />
        <meta name="viewport" content="width=device-width, initial-scale=1" />
        <title>Employee Management</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" />
        <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.5/font/bootstrap-icons.css" rel="stylesheet" />
        <%--style for dashboard--%>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/Css/navDashboardStyle.css" />
        <link rel="stylesheet" href="${pageContext.request.contextPath}/Css/dashboardStyle.css" />
        <link rel="stylesheet" href="${pageContext.request.contextPath}/Css/mainDashboardStyle.css" />
        <%--another in the following--%>

        <style>

            .search-input {
                width: 450px !important;
            }
        </style>
    </head>


    <body>
        <div class="containerBox">
            <jsp:include page="leftNav.jsp"/>
            <div class="right-section">
                <jsp:include page="topNav.jsp"/>
                <div class="main-content">
                    <div class="container-fluid p-4">
                        <ul class="nav nav-tabs mb-3">
                            <li class="nav-item">
                                <a class="nav-link active" href="#">Management Employee</a>
                            </li>
                        </ul>

                        <div class="d-flex justify-content-between align-items-center mb-3">
                            <div class="d-flex gap-2">
                                <button class="btn btn-success" data-bs-toggle="modal" data-bs-target="#addEmployeeModal">+ Add Employee</button>
                            </div>
                            <form method="get" action="${pageContext.request.contextPath}/view/employees" class="d-flex gap-2">
                                <input type="text" name="search" value="${search}" class="form-control search-input" placeholder="Search" />
                                <select name="roleId" class="form-select" onchange="this.form.submit()">
                                    <option value="">All Roles</option>
                                    <c:forEach var="role" items="${listRole}">
                                        <option value="${role.roleId}" ${selectedRoleId == role.roleId ? 'selected' : ''}>${role.roleName}</option>
                                    </c:forEach>
                                </select>
                                <select name="status" class="form-select" onchange="this.form.submit()">
                                    <option value="">All Status</option>
                                    <option value="true" ${selectedStatus == true ? 'selected' : ''}>Active</option>
                                    <option value="false" ${selectedStatus == false ? 'selected' : ''}>Inactive</option>
                                </select>
                                <button type="submit" class="btn btn-primary">Filter</button>
                            </form>
                        </div>

                        <c:if test="${not empty error}">
                            <div class="alert alert-danger mt-3">${error}</div>
                        </c:if>

                        <div class="table-container">
                            <table class="table align-middle">
                                <thead class="table-light">
                                    <tr>
                                        <th scope="col">Full Name</th>
                                        <th scope="col">Phone Number</th>
                                        <th scope="col">Email</th>
                                        <th scope="col">Role</th>
                                        <th scope="col">Floor</th>
                                        <th scope="col" class="text-center">Actions</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="emp" items="${listEmployee}">
                                        <tr>
                                            <td>${fn:escapeXml(emp.fullName != null ? emp.fullName : '-')}</td>
                                            <td>${fn:escapeXml(emp.phoneNumber != null ? emp.phoneNumber : '-')}</td>
                                            <td>${fn:escapeXml(emp.email != null ? emp.email : '-')}</td>
                                            <td>${emp.role != null ? fn:escapeXml(emp.role.roleName) : '-'}</td>
                                            <td>${emp.cleanerFloor != null ? emp.cleanerFloor.floor : '-'}</td>
                                            <td>${emp.activate ? 'Active' : 'Inactive'}</td>
                                            <td class="text-center">
                                                <button class="btn btn-sm btn-outline-info me-1" data-bs-toggle="modal" data-bs-target="#viewEmployeeModal" onclick="openViewModal(${emp.employeeId}, '${fn:escapeXml(emp.fullName != null ? emp.fullName : '')}', '${fn:escapeXml(emp.username != null ? emp.username : '')}', '${fn:escapeXml(emp.address != null ? emp.address : '')}', '${fn:escapeXml(emp.phoneNumber != null ? emp.phoneNumber : '')}', '${fn:escapeXml(emp.email != null ? emp.email : '')}', ${emp.gender}, '${fn:escapeXml(emp.CCCD != null ? emp.CCCD : '')}', '${emp.dateOfBirth != null ? emp.dateOfBirth : ''}', '${emp.registrationDate != null ? emp.registrationDate : ''}', ${emp.activate}, '${emp.role != null ? fn:escapeXml(emp.role.roleName) : ''}', ${emp.cleanerFloor != null ? emp.cleanerFloor.floor : 'null'})">
                                                    <i class="bi bi-eye"></i>
                                                </button>
                                                <button class="btn btn-sm btn-outline-primary me-1" data-bs-toggle="modal" data-bs-target="#editEmployeeModal" onclick="openEditModal(${emp.employeeId}, '${fn:escapeXml(emp.fullName != null ? emp.fullName : '')}', '${fn:escapeXml(emp.username != null ? emp.username : '')}', '${fn:escapeXml(emp.password != null ? emp.password : '')}', '${fn:escapeXml(emp.address != null ? emp.address : '')}', '${fn:escapeXml(emp.phoneNumber != null ? emp.phoneNumber : '')}', '${fn:escapeXml(emp.email != null ? emp.email : '')}', ${emp.gender}, '${fn:escapeXml(emp.CCCD != null ? emp.CCCD : '')}', '${emp.dateOfBirth != null ? emp.dateOfBirth : ''}', '${emp.registrationDate != null ? emp.registrationDate : ''}', ${emp.activate}, ${emp.role != null ? emp.role.roleId : 0}, ${emp.cleanerFloor != null ? emp.cleanerFloor.floor : ''})">
                                                    <i class="bi bi-pencil"></i>
                                                </button>
                                                <button class="btn btn-sm btn-outline-danger" data-bs-toggle="modal" data-bs-target="#deleteEmployeeModal" onclick="openDeleteModal(${emp.employeeId})">
                                                    <i class="bi bi-trash"></i>
                                                </button>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </div>
                    </div>

                    <!-- Add Employee Modal -->
                    <div class="modal fade" id="addEmployeeModal" tabindex="-1" aria-labelledby="addEmployeeModalLabel" aria-hidden="true">
                        <div class="modal-dialog modal-lg">
                            <div class="modal-content">
                                <form method="post" action="${pageContext.request.contextPath}/view/admin/employees">
                                    <input type="hidden" name="action" value="add" />
                                    <div class="modal-header">
                                        <h5 class="modal-title" id="addEmployeeModalLabel">Add New Employee</h5>
                                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                                    </div>
                                    <div class="modal-body">
                                        <div class="row g-3">
                                            <div class="col-md-6">
                                                <label for="usernameAdd" class="form-label">Username</label>
                                                <input type="text" id="usernameAdd" name="username" value="${fn:escapeXml(username)}" class="form-control" required />
                                                <c:if test="${not empty usernameError}"><div class="text-danger">${usernameError}</div></c:if>
                                                </div>
                                                <div class="col-md-6">
                                                    <label for="passwordAdd" class="form-label">Password</label>
                                                    <input type="password" id="passwordAdd" name="password" value="" class="form-control" required />
                                                <c:if test="${not empty passwordError}"><div class="text-danger">${passwordError}</div></c:if>
                                                </div>
                                                <div class="col-md-6">
                                                    <label for="fullNameAdd" class="form-label">Full Name</label>
                                                    <input type="text" id="fullNameAdd" name="fullName" value="${fn:escapeXml(fullName)}" class="form-control" required />
                                                <c:if test="${not empty fullNameError}"><div class="text-danger">${fullNameError}</div></c:if>
                                                </div>
                                                <div class="col-md-6">
                                                    <label for="phoneNumberAdd" class="form-label">Phone Number</label>
                                                    <input type="text" id="phoneNumberAdd" name="phoneNumber" value="${fn:escapeXml(phoneNumber)}" class="form-control" required />
                                                <c:if test="${not empty phoneNumberError}"><div class="text-danger">${phoneNumberError}</div></c:if>
                                                </div>
                                                <div class="col-md-6">
                                                    <label for="emailAdd" class="form-label">Email</label>
                                                    <input type="email" id="emailAdd" name="email" value="${fn:escapeXml(email)}" class="form-control" required />
                                                <c:if test="${not empty emailError}"><div class="text-danger">${emailError}</div></c:if>
                                                </div>
                                                <div class="col-md-6">
                                                    <label for="roleIdAdd" class="form-label">Role</label>
                                                    <select id="roleIdAdd" name="roleId" class="form-select" required onchange="toggleFloorFieldAdd()">
                                                    <c:forEach var="role" items="${listRole}">
                                                        <c:if test="${role.roleName == 'Receptionist' || role.roleName == 'Cleaner'}">
                                                            <option value="${role.roleId}" ${roleId == role.roleId ? 'selected' : ''}>${role.roleName}</option>
                                                        </c:if>
                                                    </c:forEach>
                                                </select>
                                            </div>
                                            <div class="col-md-6" id="floorFieldAdd" style="display:none;">
                                                <label for="floorAdd" class="form-label">Floor</label>
                                                <input type="number" id="floorAdd" name="floor" class="form-control" min="1" />
                                            </div>
                                        </div>
                                    </div>
                                    <div class="modal-footer">
                                        <button type="submit" class="btn btn-success">Save</button>
                                    </div>
                                </form>
                            </div>
                        </div>
                    </div>

                    <!-- Edit Employee Modal -->
                    <div class="modal fade" id="editEmployeeModal" tabindex="-1" aria-labelledby="editEmployeeModalLabel" aria-hidden="true">
                        <div class="modal-dialog modal-lg">
                            <div class="modal-content">
                                <form method="post" action="${pageContext.request.contextPath}/view/admin/employees">
                                    <input type="hidden" name="action" value="update" />
                                    <input type="hidden" id="employeeIdEdit" name="employeeId" />
                                    <div class="modal-header">
                                        <h5 class="modal-title" id="editEmployeeModalLabel">Edit Employee</h5>
                                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                                    </div>
                                    <div class="modal-body">
                                        <div class="row g-3">
                                            <div class="col-md-6">
                                                <label for="usernameEdit" class="form-label">Username</label>
                                                <input type="text" id="usernameEdit" name="username" value="${fn:escapeXml(username)}" class="form-control" required />
                                                <c:if test="${not empty usernameError}"><div class="text-danger">${usernameError}</div></c:if>
                                                </div>
                                                <div class="col-md-6">
                                                    <label for="passwordEdit" class="form-label">Password</label>
                                                    <input type="password" id="passwordEdit" name="password" value="" class="form-control" required />
                                                <c:if test="${not empty passwordError}"><div class="text-danger">${passwordError}</div></c:if>
                                                </div>
                                                <div class="col-md-6">
                                                    <label for="fullNameEdit" class="form-label">Full Name</label>
                                                    <input type="text" id="fullNameEdit" name="fullName" value="${fn:escapeXml(fullName)}" class="form-control" required />
                                                <c:if test="${not empty fullNameError}"><div class="text-danger">${fullNameError}</div></c:if>
                                                </div>
                                                <div class="col-md-6">
                                                    <label for="phoneNumberEdit" class="form-label">Phone Number</label>
                                                    <input type="text" id="phoneNumberEdit" name="phoneNumber" value="${fn:escapeXml(phoneNumber)}" class="form-control" required />
                                                <c:if test="${not empty phoneNumberError}"><div class="text-danger">${phoneNumberError}</div></c:if>
                                                </div>
                                                <div class="col-md-6">
                                                    <label for="emailEdit" class="form-label">Email</label>
                                                    <input type="email" id="emailEdit" name="email" value="${fn:escapeXml(email)}" class="form-control" required />
                                                <c:if test="${not empty emailError}"><div class="text-danger">${emailError}</div></c:if>
                                                </div>
                                                <div class="col-md-6">
                                                    <label for="roleIdEdit" class="form-label">Role</label>
                                                    <select id="roleIdEdit" name="roleId" class="form-select" required onchange="toggleFloorFieldEdit()">
                                                    <c:forEach var="role" items="${listRole}">
                                                        <c:if test="${role.roleName == 'Receptionist' || role.roleName == 'Cleaner'}">
                                                            <option value="${role.roleId}" ${roleId == role.roleId ? 'selected' : ''}>${role.roleName}</option>
                                                        </c:if>
                                                    </c:forEach>
                                                </select>
                                            </div>
                                            <div class="col-md-6" id="floorFieldEdit" style="display:none;">
                                                <label for="floorEdit" class="form-label">Floor</label>
                                                <input type="number" id="floorEdit" name="floor" class="form-control" min="1" />
                                            </div>
                                        </div>
                                    </div>
                                    <div class="modal-footer">
                                        <button type="submit" class="btn btn-primary">Save Changes</button>
                                    </div>
                                </form>
                            </div>
                        </div>
                    </div>

                    <!-- View Employee Modal -->
                    <div class="modal fade" id="viewEmployeeModal" tabindex="-1" aria-labelledby="viewEmployeeModalLabel" aria-hidden="true">
                        <div class="modal-dialog modal-lg">
                            <div class="modal-content">
                                <div class="modal-header">
                                    <h5 class="modal-title" id="viewEmployeeModalLabel">Employee Details</h5>
                                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                                </div>
                                <div class="modal-body">
                                    <div class="row g-3">
                                        <div class="col-md-6">
                                            <label class="form-label fw-bold">Employee ID</label>
                                            <p id="employeeIdView"></p>
                                        </div>
                                        <div class="col-md-6">
                                            <label class="form-label fw-bold">Full Name</label>
                                            <p id="fullNameView"></p>
                                        </div>
                                        <div class="col-md-6">
                                            <label class="form-label fw-bold">Username</label>
                                            <p id="usernameView"></p>
                                        </div>
                                        <div class="col-md-6">
                                            <label class="form-label fw-bold">Address</label>
                                            <p id="addressView"></p>
                                        </div>
                                        <div class="col-md-6">
                                            <label class="form-label fw-bold">Phone Number</label>
                                            <p id="phoneNumberView"></p>
                                        </div>
                                        <div class="col-md-6">
                                            <label class="form-label fw-bold">Email</label>
                                            <p id="emailView"></p>
                                        </div>
                                        <div class="col-md-6">
                                            <label class="form-label fw-bold">Gender</label>
                                            <p id="genderView"></p>
                                        </div>
                                        <div class="col-md-6">
                                            <label class="form-label fw-bold">CCCD</label>
                                            <p id="cccdView"></p>
                                        </div>
                                        <div class="col-md-6">
                                            <label class="form-label fw-bold">Date of Birth</label>
                                            <p id="dateOfBirthView"></p>
                                        </div>
                                        <div class="col-md-6">
                                            <label class="form-label fw-bold">Registration Date</label>
                                            <p id="registrationDateView"></p>
                                        </div>
                                        <div class="col-md-6">
                                            <label class="form-label fw-bold">Status</label>
                                            <p id="activateView"></p>
                                        </div>
                                        <div class="col-md-6">
                                            <label class="form-label fw-bold">Role</label>
                                            <p id="roleView"></p>
                                        </div>
                                        <div class="col-md-6">
                                            <label class="form-label fw-bold">Floor</label>
                                            <p id="floorView"></p>
                                        </div>
                                    </div>
                                </div>
                                <div class="modal-footer">
                                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                                </div>
                            </div>
                        </div>
                    </div>

                    <!-- Delete Employee Modal -->
                    <div class="modal fade" id="deleteEmployeeModal" tabindex="-1" aria-labelledby="deleteEmployeeModalLabel" aria-hidden="true">
                        <div class="modal-dialog">
                            <div class="modal-content">
                                <form method="post" action="${pageContext.request.contextPath}/view/admin/employees?action=delete">
                                    <div class="modal-header">
                                        <h5 class="modal-title" id="deleteEmployeeModalLabel">Confirm Delete</h5>
                                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                                    </div>
                                    <div class="modal-body">
                                        Are you sure you want to delete this employee? (ID: <span id="employeeIdDelete"></span>)
                                        <input type="hidden" id="employeeIdDeleteInput" name="employeeId" />
                                    </div>
                                    <div class="modal-footer">
                                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                                        <button type="submit" class="btn btn-danger">Delete</button>
                                    </div>
                                </form>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>          

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
        <%--script for dashboard--%>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js"></script>
        <script src="${pageContext.request.contextPath}/Js/navDashboardJs.js"></script>
        <script src="${pageContext.request.contextPath}/Js/userProfileJs.js"></script>
        <%--another in following--%>
        <script>
                                                        function toggleFloorFieldAdd() {
                                                            const roleId = document.getElementById('roleIdAdd').value;
                                                            const roleName = document.querySelector('#roleIdAdd option:checked').text;
                                                            const floorField = document.getElementById('floorFieldAdd');
                                                            floorField.style.display = roleName.toLowerCase() === 'cleaner' ? 'block' : 'none';
                                                        }

                                                        function toggleFloorFieldEdit() {
                                                            const roleId = document.getElementById('roleIdEdit').value;
                                                            const roleName = document.querySelector('#roleIdEdit option:checked').text;
                                                            const floorField = document.getElementById('floorFieldEdit');
                                                            floorField.style.display = roleName.toLowerCase() === 'cleaner' ? 'block' : 'none';
                                                        }

                                                        function openEditModal(employeeId, fullName, username, password, address, phoneNumber, email, gender, cccd, dateOfBirth, registrationDate, activate, roleId, floor) {
                                                            document.getElementById('employeeIdEdit').value = employeeId;
                                                            document.getElementById('usernameEdit').value = username;
                                                            document.getElementById('passwordEdit').value = password;
                                                            document.getElementById('fullNameEdit').value = fullName;
                                                            document.getElementById('addressEdit').value = address;
                                                            document.getElementById('phoneNumberEdit').value = phoneNumber;
                                                            document.getElementById('emailEdit').value = email;
                                                            document.getElementById('genderEdit').value = gender;
                                                            document.getElementById('cccdEdit').value = cccd;
                                                            document.getElementById('dateOfBirthEdit').value = dateOfBirth;
                                                            document.getElementById('registrationDateEdit').value = registrationDate;
                                                            document.getElementById('activateEdit').value = activate;
                                                            document.getElementById('roleIdEdit').value = roleId;
                                                            const floorField = document.getElementById('floorFieldEdit');
                                                            if (floor) {
                                                                document.getElementById('floorEdit').value = floor;
                                                                floorField.style.display = 'block';
                                                            } else {
                                                                floorField.style.display = 'none';
                                                            }
                                                            toggleFloorFieldEdit();
                                                        }

                                                        function openViewModal(employeeId, fullName, username, address, phoneNumber, email, gender, cccd, dateOfBirth, registrationDate, activate, roleName, floor) {
                                                            document.getElementById('employeeIdView').textContent = employeeId;
                                                            document.getElementById('fullNameView').textContent = fullName || '-';
                                                            document.getElementById('usernameView').textContent = username || '-';
                                                            document.getElementById('addressView').textContent = address || '-';
                                                            document.getElementById('phoneNumberView').textContent = phoneNumber || '-';
                                                            document.getElementById('emailView').textContent = email || '-';
                                                            document.getElementById('genderView').textContent = gender ? 'Male' : 'Female';
                                                            document.getElementById('cccdView').textContent = cccd || '-';
                                                            document.getElementById('dateOfBirthView').textContent = dateOfBirth || '-';
                                                            document.getElementById('registrationDateView').textContent = registrationDate || '-';
                                                            document.getElementById('activateView').textContent = activate ? 'Active' : 'Inactive';
                                                            document.getElementById('roleView').textContent = roleName || '-';
                                                            document.getElementById('floorView').textContent = floor !== 'null' ? floor : '-';
                                                        }

                                                        function openDeleteModal(employeeId) {
                                                            document.getElementById('employeeIdDelete').textContent = employeeId;
                                                            document.getElementById('employeeIdDeleteInput').value = employeeId;
                                                        }

                                                        document.getElementById('roleIdAdd').addEventListener('change', toggleFloorFieldAdd);
                                                        document.getElementById('roleIdEdit').addEventListener('change', toggleFloorFieldEdit);
                                                        toggleFloorFieldAdd();
        </script>
    </body>
</html>