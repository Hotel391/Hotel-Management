<%-- 
    Document   : ViewService
    Created on : 31 thg 5, 2025, 16:51:56
    Author     : Tuan'sPC
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>View service</title>
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
                <c:set var="title" value="Dashboard" scope="request"/>
                <jsp:include page="topNav.jsp" />

                <div class="main-content">
                    <div class="container-fluid p-4">
                        <ul class="nav nav-tabs mb-3">
                            <li class="nav-item">
                                <a class="nav-link active" href="${pageContext.request.contextPath}/manager/service">Quản lí dịch vụ</a>
                            </li>
                        </ul>

                        <!--thông báo thành công-->
                        <c:if test="${param.success == 'true'}">
                            <div id="successAlert" class="alert alert-success alert-dismissible fade show mt-3 mx-auto text-center" role="alert" style="width: fit-content;">
                                <c:choose>
                                    <c:when test="${param.action == 'add'}">Thêm dịch vụ thành công!</c:when>
                                    <c:when test="${param.action == 'update'}">Cập nhật dịch vụ thành công!</c:when>
                                    <c:when test="${param.action == 'isActive'}">Đổi trạng thái dịch vụ thành công!</c:when>
                                    <c:when test="${param.action == 'delete'}">Xóa dịch vụ thành công!</c:when>
                                    <c:otherwise>Thao tác thành công!</c:otherwise>
                                </c:choose>
                                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                            </div>
                        </c:if>
                        <!-- Thông báo lỗi -->
                        <c:if test="${not empty requestScope.canNotUpdate || not empty requestScope.canNotDelete}">
                            <div id="errorAlert" class="alert alert-danger alert-dismissible fade show mt-3 mx-auto text-center" role="alert" style="width: fit-content;">
                                <c:out value="${requestScope.canNotUpdate}" default="${requestScope.canNotDelete}" />
                                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                            </div>
                        </c:if>


                        <div class="d-flex justify-content-between align-items-center mb-3">
                            <div class="d-flex gap-2">
                                <button class="btn btn-success" data-bs-toggle="modal" data-bs-target="#addServiceModal">+ Thêm dịch vụ</button>
                            </div>
                            <!--form search-->
                            <form method="get" action="${pageContext.request.contextPath}/manager/service?choose=search" class="d-flex gap-2">
                                <input type="text" name="serviceNameSearch"" 
                                       class="form-control search-input" placeholder="Tên dịch vụ" value="${param.serviceNameSearch}"/>
                                <button type="submit" class="btn btn-primary">Tìm</button>
                                <a class="btn btn-secondary" href="${pageContext.request.contextPath}/manager/service">Reset</a>
                                <input type="hidden" name="choose" value="search">
                            </form>
                        </div>

                        <!--Danh sách service-->
                        <div class="table-container">
                            <table class="table align-middle bg-white">
                                <thead class="table-light">
                                    <tr>
                                        <th scope="col">Tên dịch vụ</th>
                                        <th scope="col">Giá</th>
                                        <th scope="col">Trạng thái</th>
                                        <th scope="col">Cập nhật</th>
                                        <th scope="col">Xóa</th>
                                        <th scope="col">Chuyển trạng thái</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="s" items="${requestScope.listS}">
                                        <tr>
                                            <td>${s.serviceName}</td>
                                            <td>${s.price}</td>
                                            <td>
                                                <span class="${s.isActive ? 'bg-success text-white px-2 py-1 rounded' : 'bg-danger text-white px-2 py-1 rounded'}">
                                                    ${s.isActive ? 'Dịch vụ đang mở' : 'Dịch vụ đang đóng'}
                                                </span>
                                            </td>

                                            <td>
                                                <button 
                                                    class="btn btn-sm btn-outline-primary"
                                                    data-bs-toggle="modal"
                                                    data-bs-target="#updateServiceModal"
                                                    data-service-id="${s.serviceId}"
                                                    data-service-name="${s.serviceName}"
                                                    data-service-price="${s.price}">
                                                    <i class="bi bi-pencil"></i>
                                                </button>
                                            </td> 
                                            <td>
                                                <button
                                                    class="btn btn-danger"
                                                    data-bs-toggle="modal"
                                                    data-bs-target="#deleteServiceModal"
                                                    data-service-id="${s.serviceId}"
                                                    data-service-name="${s.serviceName}">
                                                    <i class="bi bi-trash"></i>
                                                </button>
                                            </td>  

                                            <td><form method="post" action="${pageContext.request.contextPath}/manager/service" style="display:inline;">
                                                    <input type="hidden" name="serviceId" value="${s.serviceId}" />
                                                    <input type="hidden" name="choose" value="toggleStatus">
                                                    <input type="hidden" name="serviceNameSearch" value="${param.serviceNameSearch}">
                                                    <input type="hidden" name="page" value="${currentPage}" />
                                                    <button type="submit" class="btn btn-sm ${s.isActive ? 'btn-warning' : 'btn-success'}" title="Chuyển trạng thái">
                                                        <i class="bi bi-power"></i>
                                                    </button>
                                                </form>
                                            </td>

                                        </tr>
                                    </c:forEach>

                                </tbody>
                            </table>
                            <!-- Nút phân trang -->
                            <div class="d-flex justify-content-center mt-3">
                                <nav>
                                    <ul class="pagination">
                                        <c:forEach begin="1" end="${totalPages}" var="i">
                                            <li class="page-item ${i == currentPage ? 'active' : ''}">
                                                <form action="${pageContext.request.contextPath}/manager/service" method="get">
                                                    <input type="hidden" name="choose" value="search">
                                                    <button class="page-link">${i}</button>
                                                    <input type="hidden" name="page" value="${i}" />
                                                </form>
                                            </li>
                                        </c:forEach>
                                    </ul>
                                </nav>
                            </div>

                        </div>
                    </div>


                    <!-- Update Service Modal -->
                    <div class="modal fade" id="updateServiceModal" tabindex="-1" aria-labelledby="updateServiceModalLabel" aria-hidden="true">
                        <div class="modal-dialog">
                            <div class="modal-content">
                                <form action="${pageContext.request.contextPath}/manager/service?choose=updateService" method="post">
                                    <div class="modal-header">
                                        <h5 class="modal-title" id="updateServiceModalLabel">Cập nhật dịch vụ</h5>
                                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                                    </div>

                                    <div class="modal-body">
                                        <!-- Service ID -->
                                        <div class="mb-3">
                                            <!--<label for="serviceId" class="form-label">Service ID</label>-->
                                            <input type="hidden" class="form-control" id="serviceId" name="serviceId" readonly
                                                   value="${param.serviceId}" />
                                        </div>

                                        <!-- Service Name -->
                                        <div class="mb-3">
                                            <label for="serviceName" class="form-label">Tên dịch vụ</label>
                                            <input type="text" class="form-control" id="serviceName" name="serviceName" required
                                                   value="${param.serviceName}" />
                                        </div>

                                        <!-- Price -->
                                        <div class="mb-3">
                                            <label for="price" class="form-label">Giá (VND)</label>
                                            <input type="number" class="form-control" id="price" name="price" required min="0" max="50000000"
                                                   value="${param.price}" />
                                        </div>
                                        <c:if test="${not empty requestScope.serviceNameUpdateError}">
                                            <div style="color: red;">${requestScope.serviceNameUpdateError}</div>
                                        </c:if>    
                                        <c:if test="${not empty requestScope.priceUpdateError}">
                                            <div style="color: red;">${requestScope.priceUpdateError}</div>
                                        </c:if>
                                    </div>

                                    <div class="modal-footer">
                                        <button type="submit" class="btn btn-success" value="submit" name="submit">Cập nhật</button>
                                        <input type="reset" name="reset" value="Reset"/>
                                        <input type="hidden" name="serviceNameSearch" value="${param.serviceNameSearch}">
                                        <input type="hidden" name="choose" value="updateService"/>
                                        <input type="hidden" name="page" value="${currentPage}" />
                                    </div>
                                </form>
                            </div>
                        </div>
                    </div>


                    <!-- Add Service Modal -->
                    <div class="modal fade" id="addServiceModal" tabindex="-1" aria-labelledby="addServiceModalLabel" aria-hidden="true">
                        <div class="modal-dialog">
                            <div class="modal-content">
                                <form id="addRoleForm" method="post" action="${pageContext.request.contextPath}/manager/service?choose=insertService" novalidate>
                                    <div class="modal-header">
                                        <h5 class="modal-title" id="addRoleModalLabel">Thêm dịch vụ mới</h5>
                                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                                    </div>

                                    <div class="modal-body">
                                        <div class="mb-3">
                                            <label for="roomNumberer" class="form-label">Tên dịch vụ</label>
                                            <input type="text" id="newServiceNameAdd" name="serviceNameAdd" class="form-control" value="${param.serviceNameAdd}" required="">
                                        </div>
                                        <div class="mb-3">
                                            <label for="roomTypeSelect" class="form-label">Giá (VNĐ)</label>
                                            <input type="number" id="newServicePriceAdd" name="priceServiceAdd" class="form-control"  value="${param.priceServiceAdd}" 
                                                   min="0" max="50000000" required="">
                                        </div>
                                        <c:if test="${not empty requestScope.nameAddError}">
                                            <div style="color: red;">${requestScope.nameAddError}</div>
                                        </c:if>    
                                        <c:if test="${not empty requestScope.priceAddError}">
                                            <div style="color: red;">${requestScope.priceAddError}</div>
                                        </c:if>
                                    </div>
                                    <div class="modal-footer">
                                        <input type="submit" name="submit" class="btn btn-success" value="Thêm dịch vụ"/>
                                        <input type="reset" name="reset" value="Reset"/>
                                        <input type="hidden" name="choose" value="insertService"/>
                                        <input type="hidden" name="serviceNameSearch" value="${param.serviceNameSearch}">
                                        <input type="hidden" name="page" value="${currentPage}" />

                                    </div>

                                </form>
                            </div>
                        </div>
                    </div>

                    <!-- Delete Service Modal -->
                    <div class="modal fade" id="deleteServiceModal" tabindex="-1" aria-labelledby="deleteServiceModalLabel" aria-hidden="true">
                        <div class="modal-dialog">
                            <div class="modal-content">
                                <form method="get" action="${pageContext.request.contextPath}/manager/service?choose=deleteService">
                                    <div class="modal-header">
                                        <h5 class="modal-title" id="deleteServiceModalLabel">Confirm Delete</h5>
                                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                                    </div>
                                    <div class="modal-body">
                                        Are you sure you want to delete this service?<br>
                                        <!--<strong>ID:</strong> <span id="serviceIdDisplay"></span><br>-->
                                        <strong>Name:</strong> <span id="serviceNameDisplay"></span>
                                        <input type="hidden" id="serviceIdDeleteInput" name="serviceId" />
                                    </div>
                                    <div class="modal-footer">
                                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Đóng</button>
                                        <button type="submit" class="btn btn-danger">Xóa</button>
                                        <input type="hidden" name="choose" value="deleteService"/>
                                        <input type="hidden" name="serviceNameSearch" value="${param.serviceNameSearch}">
                                        <input type="hidden" name="page" value="${currentPage}" />

                                    </div>
                                </form>
                            </div>
                        </div>
                    </div>

                </div>
            </div>
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

                // --- UPDATE MODAL ---
                const updateModalEl = document.getElementById("updateServiceModal");

                if (updateModalEl) {
                    // Khi modal mở ra: set dữ liệu
                    updateModalEl.addEventListener('show.bs.modal', function (event) {
                        const button = event.relatedTarget;
                        const serviceId = button.getAttribute('data-service-id');
                        const serviceName = button.getAttribute('data-service-name');
                        const price = button.getAttribute('data-service-price');

                        document.getElementById('serviceId').value = serviceId;
                        document.getElementById('serviceName').value = serviceName;
                        document.getElementById('price').value = price;
                    });

                    // Khi modal đóng lại: xóa lỗi
                    updateModalEl.addEventListener('hidden.bs.modal', function () {
                        const errorElements = updateModalEl.querySelectorAll("div[style='color: red;']");
                        errorElements.forEach(el => el.remove());
                    });

                    // Nếu có lỗi từ server, mở modal ra
                    const shouldShowModal = ${not empty requestScope.priceUpdateError or not empty requestScope.serviceNameUpdateError};
                    if (shouldShowModal) {
                        const modal = new bootstrap.Modal(updateModalEl);
                        modal.show();
                    }
                }

                // --- DELETE MODAL ---
                const deleteServiceModal = document.getElementById('deleteServiceModal');
                if (deleteServiceModal) {
                    deleteServiceModal.addEventListener('show.bs.modal', function (event) {
                        const button = event.relatedTarget;
                        const serviceId = button.getAttribute('data-service-id');
                        const serviceName = button.getAttribute('data-service-name');
                        document.getElementById('serviceIdDeleteInput').value = serviceId;
                        document.getElementById('serviceNameDisplay').textContent = serviceName;
                        document.getElementById('serviceIdDisplay').textContent = serviceId;
                    });
                }


                // --- ADD MODAL ---
                const addModalEl = document.getElementById("addServiceModal");
                if (addModalEl) {
                    // Khi modal đóng (click X hoặc click nền)
                    addModalEl.addEventListener('hidden.bs.modal', function () {
                        // Xóa lỗi
                        const errorElements = addModalEl.querySelectorAll("div[style='color: red;']");
                        errorElements.forEach(el => el.remove());

                        // Xóa giá trị input (ghi đè giá trị param trên giao diện)
                        document.getElementById("newServiceNameAdd").value = "";
                        document.getElementById("newServicePriceAdd").value = "";
                    });

                    // Hiển thị modal nếu có lỗi (giá trị do server render)
                    const shouldShowModal = ${not empty requestScope.nameAddError or not empty requestScope.priceAddError};
                    if (shouldShowModal) {
                        const modal = new bootstrap.Modal(addModalEl);
                        modal.show();
                    }
                }
            });
        </script>

    </body>
    <%--script for dashbord--%>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js"></script>
    <script src="${pageContext.request.contextPath}/Js/navDashboardJs.js"></script>
    <script src="${pageContext.request.contextPath}/Js/userProfileJs.js"></script>
    <%--write more in following--%>
</html>
