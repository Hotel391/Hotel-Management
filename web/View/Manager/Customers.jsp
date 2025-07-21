<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8" />
        <meta name="viewport" content="width=device-width, initial-scale=1" />
        <title>Quản Lý Khách Hàng</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" />
        <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.5/font/bootstrap-icons.css" rel="stylesheet" />
        <link rel="stylesheet" href="${pageContext.request.contextPath}/Css/navDashboardStyle.css" />
        <link rel="stylesheet" href="${pageContext.request.contextPath}/Css/dashboardStyle.css" />
        <link rel="stylesheet" href="${pageContext.request.contextPath}/Css/mainDashboardStyle.css" />
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
                                <a class="nav-link active" href="${pageContext.request.contextPath}/manager/customers">Quản Lý Khách Hàng</a>
                            </li>
                        </ul>

                        <div class="d-flex justify-content-between align-items-center mb-3">
                            <form method="get" action="${pageContext.request.contextPath}/manager/customers" class="d-flex gap-2">
                                <input type="text" name="key" value="${key}" class="form-control search-input" placeholder="Tìm Kiếm" />
                            </form>
                        </div>

                        <c:if test="${not empty error}">
                            <div class="alert alert-danger mt-3">${error}</div>
                        </c:if>
                        <c:if test="${not empty sessionScope.success}">
                            <div class="alert alert-success alert-dismissible fade show" role="alert">
                                ${sessionScope.success}
                                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                            </div>
                            <c:remove var="success" scope="session"/>
                        </c:if>    

                        <div class="table-container">
                            <table class="table align-middle">
                                <thead class="table-light">
                                    <tr>
                                        <th scope="col">Họ và Tên</th>
                                        <th scope="col">Số Điện Thoại</th>
                                        <th scope="col">Email</th>
                                        <th scope="col">Trạng Thái</th>
                                        <th scope="col" class="text-center">Hành Động</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="customer" items="${listCustomer}">
                                        <tr>
                                            <td><c:out value="${customer.fullName}" default="-"/></td>
                                            <td><c:out value="${customer.phoneNumber}" default="-"/></td>
                                            <td><c:out value="${customer.email}" default="-"/></td>
                                            <td>
                                                <form method="post" action="${pageContext.request.contextPath}/manager/customers?action=toggleStatus">
                                                    <input type="hidden" name="customerId" value="${customer.customerId}" />
                                                    <input type="hidden" name="page" value="${currentPage}" />
                                                    <input type="hidden" name="key" value="${key}" />
                                                    <button type="submit" class="btn btn-sm ${customer.activate ? 'btn-outline-danger' : 'btn-outline-success'}">
                                                        ${customer.activate ? 'Mở' : 'Khóa'}
                                                    </button>
                                                </form>
                                            </td>
                                            <td class="text-center">
                                                <button class="btn btn-sm btn-outline-info me-1" data-bs-toggle="modal" data-bs-target="#viewCustomerModal_${customer.customerId}">
                                                    <i class="bi bi-eye"></i> Xem Thông Tin
                                                </button>
                                                <a href="${pageContext.request.contextPath}/manager/receipt?customerId=${customer.customerId}" class="btn btn-sm btn-outline-primary me-1">
                                                    <i class="bi bi-list"></i> Xem Hóa Đơn
                                                </a>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </div>
                    </div>

                    <!-- Modal Xem Chi Tiết Khách Hàng -->
                    <c:forEach var="customer" items="${listCustomer}">
                        <div class="modal fade" id="viewCustomerModal_${customer.customerId}" tabindex="-1" aria-labelledby="viewCustomerModalLabel_${customer.customerId}" aria-hidden="true">
                            <div class="modal-dialog modal-lg">
                                <div class="modal-content">
                                    <div class="modal-header">
                                        <h5 class="modal-title" id="viewCustomerModalLabel_${customer.customerId}">Chi Tiết Khách Hàng - ${customer.fullName}</h5>
                                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Đóng"></button>
                                    </div>
                                    <div class="modal-body">
                                        <ul>
                                            <li><strong>Họ và Tên:</strong> ${customer.fullName}</li>
                                            <li><strong>Tên Đăng Nhập:</strong> ${customer.customerAccount.username}</li> 
                                            <li><strong>Số Điện Thoại:</strong> ${customer.phoneNumber}</li>
                                            <li><strong>Email:</strong> ${customer.email}</li>
                                            <li><strong>Giới Tính:</strong> ${customer.gender ? 'Nam' : 'Nữ'}</li>
                                            <li><strong>CCCD:</strong> ${customer.CCCD}</li>
                                            <li><strong>Trạng Thái:</strong> ${customer.activate ? 'Hoạt Động' : 'Không Hoạt Động'}</li>
                                            <li><strong>Vai Trò:</strong> ${customer.role != null ? customer.role.roleName : '-'}</li>
                                        </ul>
                                    </div>
                                    <div class="modal-footer">
                                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Đóng</button>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </c:forEach>

                    <!-- Phân Trang -->
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
            document.addEventListener('DOMContentLoaded', function () {
                const successAlert = document.querySelector('.alert-success');
                if (successAlert) {
                    setTimeout(() => {
                        successAlert.classList.remove('show');
                        successAlert.style.display = 'none';
                    }, 5000);
                }
            });
        </script>
    </body>
</html>