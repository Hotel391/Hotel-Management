<%-- Document : StayingRoom Created on : Jun 15, 2025, 2:32:19 PM Author : HieuTT --%>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>

    <head>
        <meta charset="UTF-8">
        <title>Staying Room</title>
        <%--style for dashboard--%>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css"
              rel="stylesheet"
              integrity="sha384-EVSTQN3/azprG1Anm3QDgpJLIm9Nao0Yz1ztcQTwFspd3yD65VohhpuuCOmLASjC"
              crossorigin="anonymous">
        <link rel="stylesheet"
              href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.1/font/bootstrap-icons.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/Css/navDashboardStyle.css" />
        <link rel="stylesheet" href="${pageContext.request.contextPath}/Css/dashboardStyle.css" />
        <%--another in the following--%>
    </head>

    <body>
        <div class="containerBox">
            <jsp:include page="leftNavReceptionist.jsp" />
            <div class="right-section">
                <c:set var="title" value="Staying Room" scope="request" />
                <jsp:include page="topNavReceptionist.jsp" />
                <div class="main-content">
                    <h1 class="mb-4">Danh sách phòng</h1>
                    <form class="mb-3" method="get">
                        <div class="input-group" style="max-width: 400px;">
                            <input type="hidden" name="oldSearch" value="${oldSearch}">
                            <input type="text" class="form-control" name="search" placeholder="Nhập số phòng..."
                                   value="${param.search}">
                            <button class="btn btn-outline-primary" type="submit">Search</button>
                            <a class="btn btn-outline-secondary" href="stayingRoom">Clear</a>
                        </div>
                    </form>
                    <table id="roomTable" class="table table-bordered table-hover text-center align-middle">
                        <thead class="table-primary">
                            <tr>
                                <th>Số phòng</th>
                                <th>Loại phòng</th>
                                <th>Giá</th>
                                <th>Trạng thái</th>
                                <th>Action</th>
                                <th>Service</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="room" items="${stayingRooms}">
                                <tr id="room-${room.roomNumber}">
                                    <td>${room.roomNumber}</td>
                                    <td>${room.typeRoom.typeName}</td>
                                    <td>${room.typeRoom.price}</td>
                                    <td>
                                        <span
                                            class="badge ${room.isCleaner ? 'bg-success' : 'bg-warning text-dark'}">
                                            <c:choose>
                                                <c:when test="${room.isCleaner}">Bình thường</c:when>
                                                <c:otherwise>Cần dọn</c:otherwise>
                                            </c:choose>
                                        </span>
                                    </td>
                                    <td>
                                        <form method="post" class="d-inline">
                                            <input type="hidden" name="roomNumber" value="${room.roomNumber}" />
                                            <input type="hidden" name="status" value="${room.isCleaner}" />
                                            <button type="submit"
                                                    class="btn btn-sm ${room.isCleaner ? 'btn-warning' : 'btn-success'}">
                                                Đổi trạng thái
                                            </button>
                                        </form>
                                    </td>
                                    <td>
                                        <a href="stayingRoom?action=view&roomNumber=${room.roomNumber}&totalPages=${totalPages}&page=${currentPage}&search=${param.search}&oldSearch=${oldSearch}"
                                           class="btn btn-sm btn-outline-info me-1">
                                            <i class="bi bi-eye"></i> View
                                        </a>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                    <c:if test="${action eq 'view'}">
                        <form method="post" onsubmit="return confirm('Bạn có chắc muốn thay đổi?')">
                            <input type="hidden" name="action" value="updateServices" />
                            <input type="hidden" name="roomNumber" value="${param.roomNumber}" />
                            <input type="hidden" name="page" value="${param.page}" />
                            <input type="hidden" name="search" value="${param.search}" />
                            <input type="hidden" name="oldSearch" value="${param.oldSearch}" />
                            <div class="modal fade" id="serviceModal" tabindex="-1" aria-modal="true" role="dialog">
                                <div class="modal-dialog modal-dialog-scrollable modal-lg">
                                    <div class="modal-content">
                                        <div class="modal-header">
                                            <h5 class="modal-title">
                                                Dịch vụ phòng ${param.roomNumber}
                                            </h5>
                                        </div>
                                        <div class="modal-body">
                                            <h6>Dịch vụ hiện tại:</h6>
                                            <c:choose>
                                                <c:when test="${empty detailService}">
                                                    <p>Không có dịch vụ.</p>
                                                </c:when>
                                                <c:otherwise>
                                                    <ul>
                                                        <c:forEach var="ds" items="${detailService}">
                                                            <div class="form-check d-flex align-items-center mb-2">
                                                                <input class="form-check-input me-2"
                                                                       type="checkbox" name="serviceIds"
                                                                       value="${ds.service.serviceId}" checked />
                                                                <label class="form-check-label flex-grow-1">
                                                                    <strong>${ds.service.serviceName}</strong>
                                                                    <span class="text-muted">-
                                                                        ${ds.service.price} VNĐ</span>
                                                                </label>
                                                                <input type="hidden" name="oldQuantity"
                                                                       value="${ds.quantity}" />
                                                                <input type="number" name="quantities"
                                                                       value="${ds.quantity}" min="1"
                                                                       class="form-control ms-2"
                                                                       style="width: 90px;" />
                                                            </div>
                                                        </c:forEach>
                                                    </ul>
                                                </c:otherwise>
                                            </c:choose>

                                            <h6 class="mt-3">Dịch vụ khác:</h6>
                                            <c:choose>
                                                <c:when test="${empty otherServices}">
                                                    <p>Không còn dịch vụ khác.</p>
                                                </c:when>
                                                <c:otherwise>
                                                    <ul>
                                                        <c:forEach var="s" items="${otherServices}">
                                                            <div
                                                                class="form-check d-flex align-items-center mb-2">
                                                                <input class="form-check-input me-2"
                                                                       type="checkbox" name="otherServiceIds"
                                                                       value="${s.serviceId}" />
                                                                <label class="form-check-label flex-grow-1">
                                                                    <strong>${s.serviceName}</strong>
                                                                    <span class="text-muted">- ${s.price}
                                                                        VNĐ</span>
                                                                </label>
                                                                <input type="number" name="otherQuantities"
                                                                       min="1" class="form-control ms-2"
                                                                       style="width: 90px;" />
                                                            </div>
                                                        </c:forEach>
                                                    </ul>
                                                </c:otherwise>
                                            </c:choose>
                                        </div>
                                        <div class="modal-footer">
                                            <button type="submit" class="btn btn-success">
                                                Change
                                            </button>
                                            <button type="button" class="btn btn-secondary"
                                                    data-bs-dismiss="modal">
                                                Đóng
                                            </button>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </form>
                    </c:if>
                    <div class="d-flex justify-content-between align-items-center">
                        <span></span>
                        <nav aria-label="Page navigation">
                            <ul class="pagination mb-0">
                                <c:if test="${currentPage > 1}">
                                    <li class="page-item">
                                        <a class="page-link"
                                           href="?page=${currentPage - 1}&search=${param.search}&oldSearch=${oldSearch}">Previous</a>
                                    </li>
                                </c:if>

                                <c:forEach var="i" begin="1" end="${totalPages}">
                                    <li class="page-item ${i == currentPage ? 'active' : ''}">
                                        <a class="page-link"
                                           href="?page=${i}&search=${param.search}&oldSearch=${oldSearch}">${i}</a>
                                    </li>
                                </c:forEach>

                                <c:if test="${currentPage < totalPages}">
                                    <li class="page-item">
                                        <a class="page-link"
                                           href="?page=${currentPage + 1}&search=${param.search}&oldSearch=${oldSearch}">Next</a>
                                    </li>
                                </c:if>
                            </ul>
                        </nav>
                    </div>
                </div>
            </div>
        </div>
    </body>
    <%--script for dashboard--%>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js"></script>
    <script src="${pageContext.request.contextPath}/Js/navDashboardJs.js"></script>
    <script src="${pageContext.request.contextPath}/Js/userProfileJs.js"></script>
    <%--another in following--%>
    <script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.9.2/dist/umd/popper.min.js"
            integrity="sha384-IQsoLXl5PILFhosVNubq5LC7Qb9DXgDA9i+tQ8Zj3iwWAwPtgFTxbJ8NT4GN1R8p"
    crossorigin="anonymous"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.min.js"
            integrity="sha384-cVKIPhGWiC2Al4u+LWgxfKTRIcfu0JTxR+EQDz/bgldoEyl4H0zUF0QKbrJ0EcQF"
    crossorigin="anonymous"></script>
    <c:if test="${action eq 'view'}">
        <script>
                            document.addEventListener('DOMContentLoaded', function () {
                                const modal = new bootstrap.Modal(document.getElementById('serviceModal'));
                                modal.show();
                            });
        </script>
    </c:if>
    <script>
        document.addEventListener("DOMContentLoaded", function () {
            const socket = new WebSocket("ws://" + location.host + "${pageContext.request.contextPath}/roomStatus");

            socket.onmessage = function (event) {
                const data = JSON.parse(event.data);
                const row = document.getElementById("room-" + data.roomId);

                if (!row) {
                    return;
                }

                const badge = row.querySelector('.badge');
                const button = row.querySelector('form button');
                const statusInput = row.querySelector('input[name="status"]');

                if (data.status === true) {
                    badge.className = 'badge bg-success';
                    badge.textContent = 'Bình thường';
                    button.className = 'btn btn-sm btn-warning';
                    button.textContent = 'Đổi trạng thái';
                    statusInput.value = 'true';
                } else {
                    badge.className = 'badge bg-warning text-dark';
                    badge.textContent = 'Cần dọn';
                    button.className = 'btn btn-sm btn-success';
                    button.textContent = 'Đổi trạng thái';
                    statusInput.value = 'false';
                }
            };
        });
    </script>

</html>