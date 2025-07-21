<%-- 
    Document   : Role
    Created on : May 31, 2025, 3:10:57 PM
    Author     : SONNAM
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8" />
        <meta name="viewport" content="width=device-width, initial-scale=1" />
        <title>Quản Lý Vai Trò</title>
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
                                <a class="nav-link active" href="${pageContext.request.contextPath}/manager/roles">Quản Lý Vai Trò</a>
                            </li>
                        </ul>

                        <div class="d-flex justify-content-between align-items-center mb-3">
                            <div class="d-flex gap-2">
                                <button class="btn btn-success" data-bs-toggle="modal" data-bs-target="#addRoleModal">+ Thêm Vai Trò</button>
                            </div>
                        </div>

                        <c:if test="${not empty error}">
                            <div class="alert alert-danger mt-3">${error}</div>
                        </c:if>

                        <div class="table-container">
                            <table class="table align-middle">
                                <thead class="table-light">
                                    <tr>
                                        <th scope="col">Mã Vai Trò</th>
                                        <th scope="col">Tên Vai Trò</th>
                                        <th scope="col" class="text-center">Hành Động</th>
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
<!--                                                </button>
                                                <button class="btn btn-sm btn-outline-danger" onclick="openDeleteModal(${role.roleId})">
                                                    <i class="bi bi-trash"></i>
                                                </button>-->
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </div>
                    </div>

                    <!-- Modal Xem Chi Tiết Vai Trò -->
                    <div class="modal fade" id="viewRoleModal" tabindex="-1" aria-labelledby="viewRoleModalLabel" aria-hidden="true">
                        <div class="modal-dialog">
                            <div class="modal-content">
                                <div class="modal-header">
                                    <h5 class="modal-title" id="viewRoleModalLabel">Chi Tiết Vai Trò</h5>
                                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Đóng"></button>
                                </div>
                                <div class="modal-body">
                                    <p><strong>Mã Vai Trò:</strong> <span id="viewRoleId"></span></p>
                                    <p><strong>Tên Vai Trò:</strong> <span id="viewRoleName"></span></p>
                                </div>
                                <div class="modal-footer">
                                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Đóng</button>
                                </div>
                            </div>
                        </div>
                    </div>

                    <!-- Modal Thêm Vai Trò -->
                    <div class="modal fade" id="addRoleModal" tabindex="-1" aria-labelledby="addRoleModalLabel" aria-hidden="true">
                        <div class="modal-dialog">
                            <div class="modal-content">
                                <form id="addRoleForm" method="post" action="${pageContext.request.contextPath}/manager/roles?action=add" novalidate>
                                    <div class="modal-header">
                                        <h5 class="modal-title" id="addRoleModalLabel">Thêm Vai Trò Mới</h5>
                                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Đóng"></button>
                                    </div>
                                    <div class="modal-body">
                                        <div class="mb-3">
                                            <label for="newRoleName" class="form-label">Tên Vai Trò</label>
                                            <input type="text" id="newRoleName" name="roleName" class="form-control" maxlength="50" required />
                                        </div>
                                    </div>
                                    <div class="modal-footer">
                                        <button type="submit" class="btn btn-success">Thêm Vai Trò</button>
                                    </div>
                                </form>
                            </div>
                        </div>
                    </div>

                    <!-- Modal Sửa Vai Trò -->
                    <div class="modal fade" id="editRoleModal" tabindex="-1" aria-labelledby="editRoleModalLabel" aria-hidden="true">
                        <div class="modal-dialog">
                            <form id="editRoleForm" method="post" action="${pageContext.request.contextPath}/manager/roles?action=update" class="modal-content">
                                <div class="modal-header">
                                    <h5 class="modal-title" id="editRoleModalLabel">Sửa Vai Trò</h5>
                                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Đóng"></button>
                                </div>
                                <div class="modal-body">
                                    <label for="editRoleName" class="form-label">Tên Vai Trò</label>
                                    <input type="text" id="editRoleName" name="roleName" class="form-control" maxlength="50" required />
                                    <input type="hidden" id="editRoleId" name="roleId" />
                                </div>
                                <div class="modal-footer">
                                    <button type="submit" class="btn btn-primary">Lưu Thay Đổi</button>
                                </div>
                            </form>
                        </div>
                    </div>

                    <!-- Modal Xóa Vai Trò -->
                    <div class="modal fade" id="deleteRoleModal" tabindex="-1" aria-labelledby="deleteRoleModalLabel" aria-hidden="true">
                        <div class="modal-dialog">
                            <form id="deleteRoleForm" method="post" action="${pageContext.request.contextPath}/manager/roles?action=delete" class="modal-content">
                                <div class="modal-header">
                                    <h5 class="modal-title" id="deleteRoleModalLabel">Xác Nhận Xóa</h5>
                                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Đóng"></button>
                                </div>
                                <div class="modal-body">
                                    Bạn có chắc chắn muốn xóa vai trò này?
                                    <input type="hidden" id="deleteRoleId" name="roleId" />
                                </div>
                                <div class="modal-footer">
                                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Hủy</button>
                                    <button type="submit" class="btn btn-danger">Xóa</button>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <%-- Script cho dashboard --%>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js"></script>
        <script src="${pageContext.request.contextPath}/Js/navDashboardJs.js"></script>
        <script src="${pageContext.request.contextPath}/Js/userProfileJs.js"></script>
        <%-- Các script khác ở dưới --%>
        <script>
                                                    function viewRole(id, name) {
                                                        document.getElementById('viewRoleId').innerText = id;
                                                        document.getElementById('viewRoleName').innerText = name;
                                                        new bootstrap.Modal(document.getElementById('viewRoleModal')).show();
                                                    }
                                                    function openEditModal(id, name) {
                                                        document.getElementById('editRoleId').value = id;
                                                        document.getElementById('editRoleName').value = name;
                                                        new bootstrap.Modal(document.getElementById('editRoleModal')).show();
                                                    }
                                                    function openDeleteModal(id) {
                                                        document.getElementById('deleteRoleId').value = id;
                                                        new bootstrap.Modal(document.getElementById('deleteRoleModal')).show();
                                                    }
        </script>
    </body>
</html>