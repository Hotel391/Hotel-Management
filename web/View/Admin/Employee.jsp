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
                                <a class="nav-link active" href="${pageContext.request.contextPath}/admin/employees">Management Employee</a>
                            </li>
                        </ul>

                        <div class="d-flex justify-content-between align-items-center mb-3">
                            <div class="d-flex gap-2">
                                <button class="btn btn-success" data-bs-toggle="modal" data-bs-target="#addEmployeeModal">+ Add Employee</button>
                            </div>
                            <form method="get" action="${pageContext.request.contextPath}/admin/employees" class="d-flex gap-2">
                                <input type="text" name="key" value="${key}" class="form-control search-input" placeholder="Search" />


                            </form>
                        </div>

                        <c:if test="${not empty error}">
                            <div class="alert alert-danger mt-3">${error}</div>
                        </c:if>

                        <div class="table-container">
                            <table class="table align-middle">
                                <thead class="table-light">
                                    <tr>
                                        <th scope="col">Use Name</th>
                                        <th scope="col">Full Name</th>
                                        <th scope="col">Phone Number</th>
                                        <th scope="col">Email</th>
                                        <th scope="col">Role</th>
                                        <th scope="col" class="text-center">Actions</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="emp" items="${listEmployee}">
                                        <tr>
                                            <td><c:out value="${emp.username}" default="-"/></td>
                                            <td><c:out value="${emp.fullName}" default="-"/></td>
                                            <td><c:out value="${emp.phoneNumber}" default="-"/></td>
                                            <td><c:out value="${emp.email}" default="-"/></td>
                                            <td><c:out value="${emp.role != null ? emp.role.roleName : '-'}"/></td>
                                            <td><!-- Active/Inactive Employee Action -->
                                                <form method="post" action="${pageContext.request.contextPath}/admin/employees?action=toggleStatus">
                                                    <input type="hidden" name="employeeId" value="${emp.employeeId}" />
                                                    <button type="submit" class="btn btn-sm ${emp.activate ? 'btn-outline-danger' : 'btn-outline-success'}">
                                                        ${emp.activate ? 'activate' : 'Inactive'}
                                                    </button>
                                                </form></td>

                                            <td class="text-center">

                                                <!-- View Employee Modal -->
                                                <button class="btn btn-sm btn-outline-info me-1" data-bs-toggle="modal" data-bs-target="#viewEmployeeModal_${emp.employeeId}">
                                                    View 
                                                </button>

                                                <!-- Edit Employee Modal -->
                                                <button class="btn btn-sm btn-outline-primary me-1" data-bs-toggle="modal" data-bs-target="#editEmployeeModal_${emp.employeeId}">
                                                     Edit
                                                </button>

                                                <!-- Delete Employee Modal -->
                                                <button class="btn btn-sm btn-outline-danger" data-bs-toggle="modal" data-bs-target="#deleteEmployeeModal_${emp.employeeId}">
                                                     Delete

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
                                <form method="post" action="${pageContext.request.contextPath}/admin/employees">
                                    <input type="hidden" name="action" value="add" />
                                    <div class="modal-header">
                                        <h5 class="modal-title" id="addEmployeeModalLabel">Add New Employee</h5>
                                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                                    </div>
                                    <div class="modal-body">
                                        <!-- Display all errors if any -->
                                        <c:if test="${not empty errorMessages}">
                                            <div class="alert alert-danger">
                                                <ul>
                                                    <c:forEach var="error" items="${errorMessages}">
                                                        <li>${error}</li>
                                                        </c:forEach>
                                                </ul>
                                            </div>
                                        </c:if>
                                        <div class="row g-3">
                                            <div class="col-md-6">
                                                <label for="usernameAdd" class="form-label">Username</label>
                                                <input type="text" id="usernameAdd" name="username" value="${param.username}" class="form-control" required />
                                            </div>
                                            <div class="col-md-6">
                                                <label for="passwordAdd" class="form-label">Password</label>
                                                <input type="password" id="passwordAdd" name="password" value="${param.password}" class="form-control" required />
                                            </div>
                                            <div class="col-md-6">
                                                <label for="fullNameAdd" class="form-label">Full Name</label>
                                                <input type="text" id="fullNameAdd" name="fullName" value="${param.fullName}" class="form-control" required />
                                            </div>
                                            <div class="col-md-6">
                                                <label for="phoneNumberAdd" class="form-label">Phone Number</label>
                                                <input type="text" id="phoneNumberAdd" name="phoneNumber" value="${param.phoneNumber}" class="form-control" required />
                                            </div>
                                            <div class="col-md-6">
                                                <label for="emailAdd" class="form-label">Email</label>
                                                <input type="email" id="emailAdd" name="email" value="${param.email}" class="form-control" required />
                                            </div>
                                            <div class="col-md-6">
                                                <label for="roleIdAdd" class="form-label">Role</label>
                                                <select id="roleIdAdd" name="roleId" class="form-select" required onchange="toggleFloorFieldAdd()">
                                                    <c:forEach var="role" items="${listRole}">
                                                        <c:if test="${role.roleName == 'Receptionist' || role.roleName == 'Cleaner'}">
                                                            <option value="${role.roleId}" ${param.roleId == role.roleId ? 'selected' : ''}>${role.roleName}</option>
                                                        </c:if>
                                                    </c:forEach>
                                                </select>
                                            </div>
                                            <div class="col-md-6" id="floorFieldAdd" style="display:none;">
                                                <label for="floorAdd" class="form-label">Floor</label>
                                                <input type="number" id="floorAdd" name="floor" value="${param.floor}" class="form-control" min="1" max="6" />
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
                    <c:forEach var="emp" items="${listEmployee}">
                        <div class="modal fade" id="editEmployeeModal_${emp.employeeId}" tabindex="-1" aria-labelledby="editEmployeeModalLabel_${emp.employeeId}" aria-hidden="true">
                            <div class="modal-dialog modal-lg">
                                <div class="modal-content">
                                    <form method="post" action="${pageContext.request.contextPath}/admin/employees">
                                        <input type="hidden" name="action" value="update" />
                                        <input type="hidden" name="employeeId" value="${emp.employeeId}" />
                                        <div class="modal-header">
                                            <h5 class="modal-title" id="editEmployeeModalLabel_${emp.employeeId}">Edit Employee - ${emp.fullName}</h5>
                                            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                                        </div>
                                        <div class="modal-body">
                                            <!-- Display all errors if any -->
                                            <c:if test="${not empty errorMessages}">
                                                <div class="alert alert-danger">
                                                    <ul>
                                                        <c:forEach var="error" items="${errorMessages}">
                                                            <li>${error}</li>
                                                            </c:forEach>
                                                    </ul>
                                                </div>
                                            </c:if>

                                            <div class="row g-3">
                                                <div class="col-md-6">
                                                    <label for="usernameEdit_${emp.employeeId}" class="form-label">Username</label>
                                                    <input type="text" id="usernameEdit_${emp.employeeId}" name="username" value="${emp.username}" class="form-control" required />
                                                </div>

                                                <div class="col-md-6">
                                                    <label for="fullNameEdit_${emp.employeeId}" class="form-label">Full Name</label>
                                                    <input type="text" id="fullNameEdit_${emp.employeeId}" name="fullName" value="${emp.fullName}" class="form-control" required />
                                                </div>

                                                <div class="col-md-6">
                                                    <label for="phoneNumberEdit_${emp.employeeId}" class="form-label">Phone Number</label>
                                                    <input type="text" id="phoneNumberEdit_${emp.employeeId}" name="phoneNumber" value="${emp.phoneNumber}" class="form-control" required />
                                                </div>

                                                <div class="col-md-6">
                                                    <label for="emailEdit_${emp.employeeId}" class="form-label">Email</label>
                                                    <input type="email" id="emailEdit_${emp.employeeId}" name="email" value="${emp.email}" class="form-control" required />
                                                </div>

                                                <div class="col-md-6">
                                                    <label for="roleIdEdit_${emp.employeeId}" class="form-label">Role</label>
                                                    <select id="roleIdEdit_${emp.employeeId}" name="roleId" class="form-select" required onchange="toggleFloorFieldEdit(${emp.employeeId})">
                                                        <c:forEach var="role" items="${listRole}">
                                                            <c:if test="${role.roleName == 'Receptionist' || role.roleName == 'Cleaner'}">
                                                                <option value="${role.roleId}" ${emp.role.roleId == role.roleId ? 'selected' : ''}>${role.roleName}</option>
                                                            </c:if>
                                                        </c:forEach>
                                                    </select>
                                                </div>

                                                <div class="col-md-6" id="floorFieldEdit_${emp.employeeId}" style="display:${emp.role.roleName == 'Cleaner' ? 'block' : 'none'};">
                                                    <label for="floorEdit_${emp.employeeId}" class="form-label">Floor</label>
                                                    <input type="number" id="floorEdit_${emp.employeeId}" name="floor" value="${emp.cleanerFloor != null ? emp.cleanerFloor.floor : ''}" class="form-control" min="1" max="6" />
                                                </div>

                                            </div>
                                        </div>
                                        <div class="modal-footer">
                                            <button type="submit" class="btn btn-success">Update</button>
                                        </div>
                                    </form>
                                </div>
                            </div>
                        </div>
                    </c:forEach>

                    <!-- View Employee Modal -->
                    <c:forEach var="emp" items="${listEmployee}">
                        <div class="modal fade" id="viewEmployeeModal_${emp.employeeId}" tabindex="-1" aria-labelledby="viewEmployeeModalLabel_${emp.employeeId}" aria-hidden="true">
                            <div class="modal-dialog modal-lg">
                                <div class="modal-content">
                                    <div class="modal-header">
                                        <h5 class="modal-title" id="viewEmployeeModalLabel_${emp.employeeId}">Employee Details - ${emp.fullName}</h5>
                                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                                    </div>
                                    <div class="modal-body">
                                        <ul>
                                            <li><strong>Full Name:</strong> ${emp.fullName}</li>
                                            <li><strong>Username:</strong> ${emp.username}</li>
                                            <li><strong>Phone Number:</strong> ${emp.phoneNumber}</li>
                                            <li><strong>Email:</strong> ${emp.email}</li>
                                            <li><strong>Role:</strong> ${emp.role != null ? emp.role.roleName : '-'}</li>
                                            <li><strong>Floor:</strong> ${emp.cleanerFloor != null ? emp.cleanerFloor.floor : '-'}</li>
                                            <li><strong>Status:</strong> ${emp.activate ? 'Active' : 'Inactive'}</li>
                                            <li><strong>CCCD:</strong> ${emp.CCCD}</li>
                                            <li><strong>Date of Birth:</strong> ${emp.dateOfBirth}</li>
                                            <li><strong>Registration Date:</strong> ${emp.registrationDate}</li>
                                        </ul>
                                    </div>
                                    <div class="modal-footer">
                                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </c:forEach>

                    <!-- Delete Employee Modal -->
                    <c:forEach var="emp" items="${listEmployee}">
                        <div class="modal fade" id="deleteEmployeeModal_${emp.employeeId}" tabindex="-1" aria-labelledby="deleteEmployeeModalLabel_${emp.employeeId}" aria-hidden="true">
                            <div class="modal-dialog">
                                <div class="modal-content">
                                    <form method="post" action="${pageContext.request.contextPath}/admin/employees?action=delete">
                                        <div class="modal-header">
                                            <h5 class="modal-title" id="deleteEmployeeModalLabel_${emp.employeeId}">Confirm Delete</h5>
                                            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                                        </div>
                                        <div class="modal-body">
                                            Are you sure you want to delete this employee? (Username: <span id="employeeIdDelete_${emp.employeeId}">${emp.username}</span>)
                                            <input type="hidden" name="employeeId" value="${emp.employeeId}" />
                                        </div>
                                        <div class="modal-footer">
                                            <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                                            <button type="submit" class="btn btn-danger">Delete</button>
                                        </div>
                                    </form>
                                </div>
                            </div>
                        </div>
                    </c:forEach>

                    <nav aria-label="Pagination">
                        <ul class="pagination pagination-danger">
                            <c:choose>
                                <c:when test="${key != null && !key.isEmpty()}">
                                    <li class="page-item ${currentPage == 1 ? 'disabled' : ''}">
                                        <a href="?page=${currentPage - 1}&key=${key}" class="page-link">Previous</a>
                                    </li>
                                </c:when>
                                <c:otherwise>
                                    <li class="page-item ${currentPage == 1 ? 'disabled' : ''}">
                                        <a href="?page=${currentPage - 1}" class="page-link">Previous</a>
                                    </li>
                                </c:otherwise>
                            </c:choose>
                            <c:forEach var="i" begin="1" end="${endPage}">
                                <c:choose>
                                    <c:when test="${key != null && !key.isEmpty()}">
                                        <li class="page-item ${i == currentPage ? 'active' : ''}">
                                            <a class="page-link" href="?page=${i}&key=${key}">${i}</a>
                                        </li>

                                    </c:when>
                                    <c:otherwise>
                                        <li class="page-item ${i == currentPage ? 'active' : ''}">
                                            <a class="page-link" href="?page=${i}">${i}</a>
                                        </li>
                                    </c:otherwise>
                                </c:choose>

                            </c:forEach>
                            <c:choose>
                                <c:when test="${key != null && !key.isEmpty()}">

                                    <li class="page-item ${currentPage == endPage ? 'disabled' : ''}">
                                        <a href="?page=${currentPage + 1}&key=${key}" class="page-link">Next</a>
                                    </li>
                                </c:when>
                                <c:otherwise>
                                    <li class="page-item ${currentPage == endPage ? 'disabled' : ''}">
                                        <a href="?page=${currentPage + 1}" class="page-link">Next</a>
                                    </li>
                                </c:otherwise>
                            </c:choose>
                        </ul>
                    </nav>

                </div>
            </div>
        </div>          

        <%--script for dashboard--%>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js"></script>
        <script src="${pageContext.request.contextPath}/Js/navDashboardJs.js"></script>
        <script src="${pageContext.request.contextPath}/Js/userProfileJs.js"></script>
        <%--another in following--%>
        <script>
                                                        function toggleFloorFieldAdd() {
                                                            const roleName = document.querySelector('#roleIdAdd option:checked').text;
                                                            const floorField = document.getElementById('floorFieldAdd');
                                                            floorField.style.display = roleName.toLowerCase() === 'cleaner' ? 'block' : 'none';
                                                        }

                                                        function toggleFloorFieldEdit(employeeId) {
                                                            const roleSelect = document.querySelector(`#roleIdEdit_${employeeId}`);
                                                            const roleName = roleSelect.options[roleSelect.selectedIndex].text;
                                                            const floorField = document.getElementById(`floorFieldEdit_${employeeId}`);
                                                            if (roleName.toLowerCase() === 'cleaner') {
                                                                floorField.style.display = 'block';
                                                            } else {
                                                                floorField.style.display = 'none';
                                                            }
                                                        }

        </script>
    </body>
</html>
