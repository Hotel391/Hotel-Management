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
                                <a class="nav-link active" href="${pageContext.request.contextPath}/admin/service">Management service</a>
                            </li>
                        </ul>
                        <div class="d-flex justify-content-between align-items-center mb-3">
                            <div class="d-flex gap-2">
                                <button class="btn btn-success" data-bs-toggle="modal" data-bs-target="#addServiceModal">+ Add New Service</button>
                            </div>
                        </div>

                        <!--Danh sách service-->
                        <div class="table-container">
                            <table class="table align-middle bg-white">
                                <thead class="table-light">
                                    <tr>
                                        <th scope="col">ServiceID</th>
                                        <th scope="col">Service name</th>
                                        <th scope="col">Price</th>
                                        <th scope="col">Action</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="s" items="${requestScope.listS}">
                                        <tr>
                                            <td>${s.serviceId}</td>
                                            <td>${s.serviceName}</td>
                                            <td>${s.price}</td>
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

                                                <button
                                                    class="btn btn-danger"
                                                    data-bs-toggle="modal"
                                                    data-bs-target="#deleteServiceModal"
                                                    data-service-id="${s.serviceId}"
                                                    data-service-name="${s.serviceName}">
                                                    <i class="bi bi-trash"></i>
                                                </button>
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
                                                <form action="${pageContext.request.contextPath}/admin/service" method="post">
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


                    <!-- Delete Service Modal -->
                    <div class="modal fade" id="deleteServiceModal" tabindex="-1" aria-labelledby="deleteServiceModalLabel" aria-hidden="true">
                        <div class="modal-dialog">
                            <div class="modal-content">
                                <form method="get" action="${pageContext.request.contextPath}/admin/service?choose=deleteService">
                                    <div class="modal-header">
                                        <h5 class="modal-title" id="deleteServiceModalLabel">Confirm Delete</h5>
                                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                                    </div>
                                    <div class="modal-body">
                                        Are you sure you want to delete this service?<br>
                                        <strong>ID:</strong> <span id="serviceIdDisplay"></span><br>
                                        <strong>Name:</strong> <span id="serviceNameDisplay"></span>
                                        <input type="hidden" id="serviceIdDeleteInput" name="serviceId" />
                                    </div>
                                    <div class="modal-footer">
                                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                                        <button type="submit" class="btn btn-danger">Delete</button>
                                        <input type="hidden" name="choose" value="deleteService"/>
                                        <input type="hidden" name="page" value="${currentPage}" />

                                    </div>
                                </form>
                            </div>
                        </div>
                    </div>


                    <!-- Update Service Modal -->
                    <div class="modal fade" id="updateServiceModal" tabindex="-1" aria-labelledby="updateServiceModalLabel" aria-hidden="true">
                        <div class="modal-dialog">
                            <div class="modal-content">
                                <form action="${pageContext.request.contextPath}/admin/service?choose=updateService" method="post">
                                    <div class="modal-header">
                                        <h5 class="modal-title" id="updateServiceModalLabel">Update Service</h5>
                                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                                    </div>

                                    <div class="modal-body">
                                        <!-- Service ID -->
                                        <div class="mb-3">
                                            <label for="serviceId" class="form-label">Service ID</label>
                                            <input type="text" class="form-control" id="serviceId" name="serviceId" readonly
                                                   value="${param.serviceId}" />
                                        </div>

                                        <!-- Service Name -->
                                        <div class="mb-3">
                                            <label for="serviceName" class="form-label">Service Name</label>
                                            <input type="text" class="form-control" id="serviceName" name="serviceName" required
                                                   value="${param.serviceName}" />
                                        </div>

                                        <!-- Price -->
                                        <div class="mb-3">
                                            <label for="price" class="form-label">Price (VND)</label>
                                            <input type="number" class="form-control" id="price" name="price" required min="0"
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
                                        <button type="submit" class="btn btn-success" value="submit" name="submit">Update</button>
                                        <input type="reset" name="reset" value="Reset"/>
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
                                <form id="addRoleForm" method="post" action="${pageContext.request.contextPath}/admin/service?choose=insertService" novalidate>
                                    <div class="modal-header">
                                        <h5 class="modal-title" id="addRoleModalLabel">Add New Service</h5>
                                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                                    </div>

                                    <div class="modal-body">
                                        <div class="mb-3">
                                            <label for="roomNumberer" class="form-label">Service Name</label>
                                            <input type="text" id="newServiceNameAdd" name="serviceNameAdd" class="form-control" value="${param.serviceNameAdd}" required="">
                                        </div>
                                        <div class="mb-3">
                                            <label for="roomTypeSelect" class="form-label">Price</label>
                                            <input type="text" id="newServicePriceAdd" name="priceServiceAdd" class="form-control"  value="${param.priceServiceAdd}" required="">
                                        </div>
                                        <c:if test="${not empty requestScope.nameAddError}">
                                            <div style="color: red;">${requestScope.nameAddError}</div>
                                        </c:if>    
                                        <c:if test="${not empty requestScope.priceAddError}">
                                            <div style="color: red;">${requestScope.priceAddError}</div>
                                        </c:if>
                                    </div>
                                    <div class="modal-footer">
                                        <input type="submit" name="submit" class="btn btn-success" value="Add new service"/>
                                        <input type="reset" name="reset" value="Reset"/>
                                        <input type="hidden" name="choose" value="insertService"/>
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
                        document.getElementById('serviceIdDeleteInput').value = serviceId;
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
