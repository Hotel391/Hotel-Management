<%-- 
    Document   : Employee
    Created on : Jun 1, 2025, 11:47:00 AM
    Author     : SONNAM
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8" />
        <meta name="viewport" content="width=device-width, initial-scale=1" />
        <title>Quản Lý Nhân Viên</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" />
        <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.5/font/bootstrap-icons.css" rel="stylesheet" />
        <%-- Kiểu dáng cho dashboard --%>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/Css/navDashboardStyle.css" />
        <link rel="stylesheet" href="${pageContext.request.contextPath}/Css/dashboardStyle.css" />
        <link rel="stylesheet" href="${pageContext.request.contextPath}/Css/mainDashboardStyle.css" />
        <%-- Các kiểu khác ở dưới --%>

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
                                <a class="nav-link active" href="${pageContext.request.contextPath}/manager/employees">Quản Lý Nhân Viên</a>
                            </li>
                        </ul>

                        <div class="d-flex justify-content-between align-items-center mb-3">
                            <div class="d-flex gap-2">
                                <button class="btn btn-success" data-bs-toggle="modal" data-bs-target="#addEmployeeModal">+ Thêm Nhân Viên</button>
                            </div>
                            <form method="get" action="${pageContext.request.contextPath}/manager/employees" class="d-flex gap-2">
                                <input type="text" name="key" value="${key}" class="form-control search-input" placeholder="Tìm Kiếm" />
                            </form>
                        </div>

                        <c:if test="${not empty error}">
                            <div class="alert alert-danger mt-3">${error}</div>
                        </c:if>

                        <div class="table-container">
                            <table class="table align-middle">
                                <thead class="table-light">
                                    <tr>
                                        <th scope="col">Tên Đăng Nhập</th>
                                        <th scope="col">Họ và Tên</th>
                                        <th scope="col">Số Điện Thoại</th>
                                        <th scope="col">Email</th>
                                        <th scope="col">Vai Trò</th>
                                        <th scope="col" class="text-center">Hành Động</th>
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
                                            <td><!-- Hành Động Kích Hoạt/Hủy Kích Hoạt Nhân Viên -->
                                                <form method="post" action="${pageContext.request.contextPath}/manager/employees?action=toggleStatus">
                                                    <input type="hidden" name="employeeId" value="${emp.employeeId}" />
                                                    <input type="hidden" name="page" value="${currentPage}" />
                                                    <input type="hidden" name="key" value="${key}" />

                                                    <button type="submit" class="btn btn-sm ${emp.activate ? 'btn-outline-danger' : 'btn-outline-success'}">
                                                        ${emp.activate ? 'Mở' : 'Khóa'}
                                                    </button>
                                                </form>
                                            </td>

                                            <td class="text-center">
                                                <!-- Modal Xem Thông Tin Nhân Viên -->
                                                <button class="btn btn-sm btn-outline-info me-1" data-bs-toggle="modal" data-bs-target="#viewEmployeeModal_${emp.employeeId}">
                                                    Xem
                                                </button>

                                                <!-- Modal Sửa Thông Tin Nhân Viên -->
                                                <button class="btn btn-sm btn-outline-primary me-1" data-bs-toggle="modal" data-bs-target="#editEmployeeModal_${emp.employeeId}">
                                                    Sửa
                                                </button>

                                                <!-- Modal Xóa Nhân Viên -->
                                                <button class="btn btn-sm btn-outline-danger" data-bs-toggle="modal" data-bs-target="#deleteEmployeeModal_${emp.employeeId}">
                                                    Xóa
                                                </button>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </div>
                    </div>

                    <!-- Modal Thêm Nhân Viên -->
                    <div class="modal fade" id="addEmployeeModal" tabindex="-1" aria-labelledby="addEmployeeModalLabel" aria-hidden="true">
                        <div class="modal-dialog modal-lg">
                            <div class="modal-content">
                                <form method="post" action="${pageContext.request.contextPath}/manager/employees">
                                    <input type="hidden" name="action" value="add" />
                                    <input type="hidden" name="page" value="${currentPage}" />
                                    <input type="hidden" name="key" value="${key}" />
                                    <div class="modal-header">
                                        <h5 class="modal-title" id="addEmployeeModalLabel">Thêm Nhân Viên Mới</h5>
                                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Đóng"></button>
                                    </div>
                                    <div class="modal-body">
                                        <c:if test="${not empty requestScope.addErrors}">
                                            <div class="alert alert-danger">
                                                <ul>
                                                    <c:forEach var="error" items="${requestScope.addErrors}">
                                                        <li>${error}</li>
                                                        </c:forEach>
                                                </ul>
                                            </div>
                                        </c:if>

                                        <div class="row g-3">
                                            <div class="col-md-6">
                                                <label for="usernameAdd" class="form-label">Tên Đăng Nhập</label>
                                                <input type="text" id="usernameAdd" name="username" value="${param.username != null ? param.username : ''}" class="form-control" required />
                                            </div>
                                            <div class="col-md-6">
                                                <label for="passwordAdd" class="form-label">Mật Khẩu</label>
                                                <input type="password" id="passwordAdd" name="password" value="${param.password != null ? param.password : ''}" class="form-control" required />
                                            </div>
                                            <div class="col-md-6">
                                                <label for="fullNameAdd" class="form-label">Họ và Tên</label>
                                                <input type="text" id="fullNameAdd" name="fullName" value="${param.fullName != null ? param.fullName : ''}" class="form-control" required />
                                            </div>
                                            <div class="col-md-6">
                                                <label for="phoneNumberAdd" class="form-label">Số Điện Thoại</label>
                                                <input type="text" id="phoneNumberAdd" name="phoneNumber" value="${param.phoneNumber != null ? param.phoneNumber : ''}" class="form-control" required />
                                            </div>
                                            <div class="col-md-6">
                                                <label for="emailAdd" class="form-label">Email</label>
                                                <input type="email" id="emailAdd" name="email" value="${param.email != null ? param.email : ''}" class="form-control" required />
                                            </div>
                                            <div class="col-md-6">
                                                <label for="roleIdAdd" class="form-label">Vai Trò</label>
                                                <select id="roleIdAdd" name="roleId" class="form-select" required onchange="toggleFloorFieldAdd()">
                                                    <c:forEach var="role" items="${listRole}">
                                                        <c:if test="${role.roleName == 'Receptionist' || role.roleName == 'Cleaner'}">
                                                            <option value="${role.roleId}" ${param.roleId == role.roleId ? 'selected' : ''}>${role.roleName}</option>
                                                        </c:if>
                                                    </c:forEach>
                                                </select>
                                            </div>

                                            <!-- Tầng Bắt Đầu/Kết Thúc cho Cleaner -->
                                            <div class="col-md-6" id="floorFieldAdd" style="display:${isCleaner ? 'block' : 'none'};">
                                                <label for="startFloor" class="form-label">Tầng Bắt Đầu</label>
                                                <input type="number" id="startFloor" name="startFloor" value="${param.startFloor != null ? param.startFloor : ''}" class="form-control" min="1" max="7" />

                                                <label for="endFloor" class="form-label">Tầng Kết Thúc</label>
                                                <input type="number" id="endFloor" name="endFloor" value="${param.endFloor != null ? param.endFloor : ''}" class="form-control" min="1" max="7" />
                                            </div>
                                        </div>
                                    </div>
                                    <div class="modal-footer">
                                        <button type="submit" class="btn btn-success">Lưu</button>
                                    </div>
                                </form>
                            </div>
                        </div>
                    </div>

                    <c:forEach var="emp" items="${listEmployee}">
                        <div class="modal fade" id="editEmployeeModal_${emp.employeeId}" tabindex="-1" aria-labelledby="editEmployeeModalLabel_${emp.employeeId}" aria-hidden="true">
                            <div class="modal-dialog modal-lg">
                                <div class="modal-content">
                                    <form method="post" action="${pageContext.request.contextPath}/manager/employees">
                                        <input type="hidden" name="action" value="update" />
                                        <input type="hidden" name="employeeId" value="${emp.employeeId}" />
                                        <input type="hidden" name="page" value="${currentPage}" />
                                        <input type="hidden" name="key" value="${key}" />

                                        <div class="modal-header">
                                            <h5 class="modal-title" id="editEmployeeModalLabel_${emp.employeeId}">Sửa Thông Tin Nhân Viên - ${emp.fullName}</h5>
                                            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Đóng"></button>
                                        </div>

                                        <div class="modal-body">
                                            <c:set var="editErrorKey" value="editErrors_${emp.employeeId}" />
                                            <c:if test="${not empty requestScope[editErrorKey]}">
                                                <div class="alert alert-danger">
                                                    <ul>
                                                        <c:forEach var="error" items="${requestScope[editErrorKey]}">
                                                            <li>${error}</li>
                                                            </c:forEach>
                                                    </ul>
                                                </div>
                                            </c:if>



                                            <div class="row g-3">
                                                <div class="col-md-6">
                                                    <label for="usernameEdit_${emp.employeeId}" class="form-label">Tên Đăng Nhập</label>
                                                    <input type="text" id="usernameEdit_${emp.employeeId}" name="username" value="${requestScope.username != null ? requestScope.username : emp.username}" class="form-control" required />
                                                </div>

                                                <div class="col-md-6">
                                                    <label for="fullNameEdit_${emp.employeeId}" class="form-label">Họ và Tên</label>
                                                    <input type="text" id="fullNameEdit_${emp.employeeId}" name="fullName" value="${requestScope.fullName != null ? requestScope.fullName : emp.fullName}" class="form-control" required />
                                                </div>

                                                <div class="col-md-6">
                                                    <label for="phoneNumberEdit_${emp.employeeId}" class="form-label">Số Điện Thoại</label>
                                                    <input type="text" id="phoneNumberEdit_${emp.employeeId}" name="phoneNumber" value="${requestScope.phoneNumber != null ? requestScope.phoneNumber : emp.phoneNumber}" class="form-control" required />
                                                </div>

                                                <div class="col-md-6">
                                                    <label for="emailEdit_${emp.employeeId}" class="form-label">Email</label>
                                                    <input type="email" id="emailEdit_${emp.employeeId}" name="email" value="${requestScope.email != null ? requestScope.email : emp.email}" class="form-control" required />
                                                </div>

                                                <!-- Bộ Lựa Chọn Vai Trò -->
                                                <div class="col-md-6">
                                                    <label for="roleIdEdit_${emp.employeeId}" class="form-label">Vai Trò</label>
                                                    <select id="roleIdEdit_${emp.employeeId}" name="roleId" class="form-select" required onchange="toggleFloorFieldEdit(${emp.employeeId})">
                                                        <c:forEach var="role" items="${listRole}">
                                                            <c:if test="${role.roleName == 'Receptionist' || role.roleName == 'Cleaner'}">
                                                                <option value="${role.roleId}" ${requestScope.roleId == role.roleId || emp.role.roleId == role.roleId ? 'selected' : ''}>${role.roleName}</option>
                                                            </c:if>
                                                        </c:forEach>
                                                    </select>
                                                </div>

                                                <!-- Tầng Bắt Đầu/Kết Thúc cho Cleaner -->
                                                <div class="col-md-6" id="floorFieldEdit_${emp.employeeId}" style="display:${requestScope.roleId == 'Cleaner' || emp.role.roleName == 'Cleaner' ? 'block' : 'none'};">
                                                    <label for="startFloorEdit_${emp.employeeId}" class="form-label">Tầng Bắt Đầu</label>
                                                    <input type="number" id="startFloorEdit_${emp.employeeId}" name="startFloor" value="${requestScope.startFloor != null ? requestScope.startFloor : (emp.cleanerFloor != null ? emp.cleanerFloor.startFloor : '')}" class="form-control" min="1" max="7" />

                                                    <label for="endFloorEdit_${emp.employeeId}" class="form-label">Tầng Kết Thúc</label>
                                                    <input type="number" id="endFloorEdit_${emp.employeeId}" name="endFloor" value="${requestScope.endFloor != null ? requestScope.endFloor : (emp.cleanerFloor != null ? emp.cleanerFloor.endFloor : '')}" class="form-control" min="1" max="7" />
                                                </div>
                                            </div>
                                        </div>
                                        <div class="modal-footer">
                                            <button type="submit" class="btn btn-success">Cập Nhật</button>
                                        </div>
                                    </form>
                                </div>
                            </div>
                        </div>
                    </c:forEach>

                    <c:forEach var="emp" items="${listEmployee}">
                        <div class="modal fade" id="viewEmployeeModal_${emp.employeeId}" tabindex="-1" aria-labelledby="viewEmployeeModalLabel_${emp.employeeId}" aria-hidden="true">
                            <div class="modal-dialog modal-lg">
                                <div class="modal-content">
                                    <div class="modal-header">
                                        <h5 class="modal-title" id="viewEmployeeModalLabel_${emp.employeeId}">Chi Tiết Nhân Viên - ${emp.fullName}</h5>
                                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Đóng"></button>
                                    </div>
                                    <div class="modal-body">
                                        <ul>
                                            <li><strong>Họ và Tên:</strong> ${emp.fullName}</li>
                                            <li><strong>Tên Đăng Nhập:</strong> ${emp.username}</li>
                                            <li><strong>Số Điện Thoại:</strong> ${emp.phoneNumber}</li>
                                            <li><strong>Email:</strong> ${emp.email}</li>
                                            <li><strong>Vai Trò:</strong> ${emp.role != null ? emp.role.roleName : '-'}</li>

                                            <!-- Hiển thị thông tin tầng nếu vai trò là 'Cleaner' -->
                                            <c:if test="${emp.role != null && emp.role.roleName == 'Cleaner'}">
                                                <li><strong>Tầng Bắt Đầu:</strong> ${emp.cleanerFloor != null ? emp.cleanerFloor.startFloor : '-'}</li>
                                                <li><strong>Tầng Kết Thúc:</strong> ${emp.cleanerFloor != null ? emp.cleanerFloor.endFloor : '-'}</li>
                                                </c:if>

                                            <li><strong>Trạng Thái:</strong> ${emp.activate ? 'Hoạt Động' : 'Không Hoạt Động'}</li>
                                            <li><strong>CCCD:</strong> ${emp.CCCD}</li>
                                            <li><strong>Ngày Sinh:</strong> ${emp.dateOfBirth}</li>
                                            <li><strong>Ngày Đăng Ký:</strong> ${emp.registrationDate}</li>
                                        </ul>
                                    </div>
                                    <div class="modal-footer">
                                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Đóng</button>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </c:forEach>

                    <c:forEach var="emp" items="${listEmployee}">
                        <div class="modal fade" id="deleteEmployeeModal_${emp.employeeId}" tabindex="-1" aria-labelledby="deleteEmployeeModalLabel_${emp.employeeId}" aria-hidden="true">
                            <div class="modal-dialog">
                                <div class="modal-content">
                                    <form method="post" action="${pageContext.request.contextPath}/manager/employees?action=delete">
                                        <input type="hidden" name="page" value="${currentPage}" />
                                        <input type="hidden" name="key" value="${key}" />
                                        <div class="modal-header">
                                            <h5 class="modal-title" id="deleteEmployeeModalLabel_${emp.employeeId}">Xác Nhận Xóa</h5>
                                            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Đóng"></button>
                                        </div>
                                        <div class="modal-body">
                                            Bạn có chắc chắn muốn xóa nhân viên này? (Tên Đăng Nhập: <span id="employeeIdDelete_${emp.employeeId}">${emp.username}</span>)
                                            <input type="hidden" name="employeeId" value="${emp.employeeId}" />
                                        </div>
                                        <div class="modal-footer">
                                            <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Hủy</button>
                                            <button type="submit" class="btn btn-danger">Xóa</button>
                                        </div>
                                    </form>
                                </div>
                            </div>
                        </div>
                    </c:forEach>

                    <nav aria-label="Phân Trang">
                        <ul class="pagination pagination-danger">
                            <c:choose>
                                <c:when test="${key != null && !key.isEmpty()}">
                                    <li class="page-item ${currentPage == 1 ? 'disabled' : ''}">
                                        <a href="?page=${currentPage - 1}&key=${key}" class="page-link">Trước</a>
                                    </li>
                                </c:when>
                                <c:otherwise>
                                    <li class="page-item ${currentPage == 1 ? 'disabled' : ''}">
                                        <a href="?page=${currentPage - 1}" class="page-link">Trước</a>
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
                                        <a href="?page=${currentPage + 1}&key=${key}" class="page-link">Tiếp Theo</a>
                                    </li>
                                </c:when>
                                <c:otherwise>
                                    <li class="page-item ${currentPage == endPage ? 'disabled' : ''}">
                                        <a href="?page=${currentPage + 1}" class="page-link">Tiếp Theo</a>
                                    </li>
                                </c:otherwise>
                            </c:choose>
                        </ul>
                    </nav>
                </div>
            </div>
        </div>          

        <%-- Script cho dashboard --%>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js"></script>
        <script src="${pageContext.request.contextPath}/Js/navDashboardJs.js"></script>
        <script src="${pageContext.request.contextPath}/Js/userProfileJs.js"></script>
        <%-- Các script khác ở dưới --%>
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

                                                        document.addEventListener('DOMContentLoaded', function () {
                                                            const showAddModal = ${not empty requestScope.showAddModal};
                                                            if (showAddModal) {
                                                                const addEmployeeModalEl = document.getElementById("addEmployeeModal");
                                                                if (addEmployeeModalEl) {
                                                                    const modal = new bootstrap.Modal(addEmployeeModalEl);
                                                                    modal.show();
                                                                }
                                                            }
                                                        });

                                                        document.addEventListener('DOMContentLoaded', function () {
                                                            const showEditModalId = "<c:out value='${showEditModalId}' default=''/>";
                                                            if (showEditModalId !== "") {
                                                                const editEmployeeModalEl = document.getElementById("editEmployeeModal_" + showEditModalId);
                                                                if (editEmployeeModalEl) {
                                                                    const modal = new bootstrap.Modal(editEmployeeModalEl);
                                                                    modal.show();
                                                                }
                                                            }
                                                        });
        </script>
    </body>
</html>