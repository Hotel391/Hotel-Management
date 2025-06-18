<%-- 
    Document   : Role
    Created on : May 31, 2025, 3:10:57 PM
    Author     : SONNAM
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8" />
        <meta name="viewport" content="width=device-width, initial-scale=1" />
        <title>Role Management</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" />
        <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.5/font/bootstrap-icons.css" rel="stylesheet" />
        <link rel="stylesheet" href="${pageContext.request.contextPath}/Css/navDashboardStyle.css" />
        <link rel="stylesheet" href="${pageContext.request.contextPath}/Css/dashboardStyle.css" />
        <link rel="stylesheet" href="${pageContext.request.contextPath}/Css/mainDashboardStyle.css" />
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
                                <a class="nav-link active" href="#">Management Role</a>
                            </li>
                        </ul>

                        <div class="d-flex justify-content-between align-items-center mb-3">
                            <div class="d-flex gap-2">
                                <button class="btn btn-success" data-bs-toggle="modal" data-bs-target="#addRoleModal">+ Add Role</button>
                            </div>
                        </div>

                        <c:if test="${not empty error}">
                            <div class="alert alert-danger mt-3">${error}</div>
                        </c:if>

                        <div class="table-container">
                            <table class="table align-middle">
                                <thead class="table-light">
                                    <tr>
                                        <th scope="col">Role ID</th>
                                        <th scope="col">Role Name</th>
                                        <th scope="col" class="text-center">Actions</th>
                                    </tr>
                                </thead>
                                <tbody id="roleTableBody">
                                    <c:forEach var="role" items="${listRole}">
                                        <tr>
                                            <td>${role.roleId}</td>
                                            <td><span class="badge bg-secondary badge-role">${role.roleName}</span></td>
                                            <td class="text-center">
                                                <button class="btn btn-sm btn-outline-info me-1" onclick="viewRole(${role.roleId}, '${role.roleName}')">
                                                    <i class="bi bi-eye"></i>
                                                </button>
                                                <button class="btn btn-sm btn-outline-primary me-1" onclick="openEditModal(${role.roleId}, '${role.roleName}')">
                                                    <i class="bi bi-pencil"></i>
                                                </button>
                                                <button class="btn btn-sm btn-outline-danger" onclick="openDeleteModal(${role.roleId})">
                                                    <i class="bi bi-trash"></i>
                                                </button>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </div>
                    </div>

                    <!-- View Role Modal -->
                    <div class="modal fade" id="viewRoleModal" tabindex="-1" aria-labelledby="viewRoleModalLabel" aria-hidden="true">
                        <div class="modal-dialog">
                            <div class="modal-content">
                                <div class="modal-header">
                                    <h5 class="modal-title" id="viewRoleModalLabel">Role Details</h5>
                                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                                </div>
                                <div class="modal-body">
                                    <p><strong>Role ID:</strong> <span id="viewRoleId"></span></p>
                                    <p><strong>Role Name:</strong> <span id="viewRoleName"></span></p>
                                </div>
                                <div class="modal-footer">
                                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                                </div>
                            </div>
                        </div>
                    </div>

                    <!-- Add Role Modal -->
                    <div class="modal fade" id="addRoleModal" tabindex="-1" aria-labelledby="addRoleModalLabel" aria-hidden="true">
                        <div class="modal-dialog">
                            <div class="modal-content">
                                <form id="addRoleForm" method="post" action="${pageContext.request.contextPath}/view/roles?action=add" novalidate>
                                    <div class="modal-header">
                                        <h5 class="modal-title" id="addRoleModalLabel">Add New Role</h5>
                                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                                    </div>
                                    <div class="modal-body">
                                        <div class="mb-3">
                                            <label for="newRoleName" class="form-label">Role Name</label>
                                            <input type="text" id="newRoleName" name="roleName" class="form-control" maxlength="50" required />
                                        </div>
                                    </div>
                                    <div class="modal-footer">
                                        <button type="submit" class="btn btn-success">Add Role</button>
                                    </div>
                                </form>
                            </div>
                        </div>
                    </div>

                    <!-- Edit Role Modal -->
                    <div class="modal fade" id="editRoleModal" tabindex="-1" aria-labelledby="editRoleModalLabel" aria-hidden="true">
                        <div class="modal-dialog">
                            <form id="editRoleForm" method="post" action="${pageContext.request.contextPath}/view/roles?action=update" class="modal-content">
                                <div class="modal-header">
                                    <h5 class="modal-title" id="editRoleModalLabel">Edit Role</h5>
                                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                                </div>
                                <div class="modal-body">
                                    <label for="editRoleName" class="form-label">Role Name</label>
                                    <input type="text" id="editRoleName" name="roleName" class="form-control" maxlength="50" required />
                                    <input type="hidden" id="editRoleId" name="roleId" />
                                </div>
                                <div class="modal-footer">
                                    <button type="submit" class="btn btn-primary">Save Changes</button>
                                </div>
                            </form>
                        </div>
                    </div>

                    <!-- Delete Role Modal -->
                    <div class="modal fade" id="deleteRoleModal" tabindex="-1" aria-labelledby="deleteRoleModalLabel" aria-hidden="true">
                        <div class="modal-dialog">
                            <form id="deleteRoleForm" method="post" action="${pageContext.request.contextPath}/view/roles?action=delete" class="modal-content">
                                <div class="modal-header">
                                    <h5 class="modal-title" id="deleteRoleModalLabel">Confirm Delete</h5>
                                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                                </div>
                                <div class="modal-body">
                                    Are you sure you want to delete this role?
                                    <input type="hidden" id="deleteRoleId" name="roleId" />
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
        <%--script for dashboard--%>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js"></script>
        <script src="${pageContext.request.contextPath}/Js/navDashboardJs.js"></script>
        <script src="${pageContext.request.contextPath}/Js/userProfileJs.js"></script>
        <%--another in following--%>
        <script
            <script>
                                        // Xem Role
                                        function viewRole(id, name) {
                                            document.getElementById('viewRoleId').innerText = id;
                                            document.getElementById('viewRoleName').innerText = name;
                                            new bootstrap.Modal(document.getElementById('viewRoleModal')).show();
                                        }

                                        // Sửa Role
                                        function openEditModal(id, name) {
                                            document.getElementById('editRoleId').value = id;
                                            document.getElementById('editRoleName').value = name;
                                            new bootstrap.Modal(document.getElementById('editRoleModal')).show();
                                        }

                                        // Xóa Role
                                        function openDeleteModal(id) {
                                            document.getElementById('deleteRoleId').value = id;
                                            new bootstrap.Modal(document.getElementById('deleteRoleModal')).show();
                                        }
        </script>

    </body>
</html>