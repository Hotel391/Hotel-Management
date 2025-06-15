<%-- 
    Document   : StayingRoom
    Created on : Jun 15, 2025, 2:32:19 PM
    Author     : HieuTT
--%>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Staying Room</title>
        <%--style for dashboard--%>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css" rel="stylesheet"
              integrity="sha384-EVSTQN3/azprG1Anm3QDgpJLIm9Nao0Yz1ztcQTwFspd3yD65VohhpuuCOmLASjC"
              crossorigin="anonymous">
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.1/font/bootstrap-icons.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/Css/navDashboardStyle.css" />
        <link rel="stylesheet" href="${pageContext.request.contextPath}/Css/dashboardStyle.css" />
        <%--another in the following--%>
    </head>
    <body>
        <div class="containerBox">
            <jsp:include page="leftNavReceptionist.jsp" /> 
            <div class="right-section">
                <c:set var="title" value="Staying Room" scope="request"/>
                <jsp:include page="topNavReceptionist.jsp" />
                <div class="main-content">
                    <h1 class="mb-4">Danh sách phòng</h1>
                    <form class="mb-3" method="get">
                        <div class="input-group" style="max-width: 400px;">
                            <input type="hidden" name="oldSearch" value="${oldSearch}">
                            <input type="text" class="form-control" name="search" placeholder="Nhập số phòng..." value="${param.search}">
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
                                <th>View</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="room" items="${stayingRooms}">
                                <tr id="room-${room.roomNumber}">
                                    <td>${room.roomNumber}</td>
                                    <td>${room.typeRoom.typeName}</td>
                                    <td>${room.typeRoom.price}</td>
                                    <td>
                                        <span class="badge ${room.isCleaner ? 'bg-success' : 'bg-warning text-dark'}">
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
                                            <button type="submit" class="btn btn-sm ${room.isCleaner ? 'btn-warning' : 'btn-success'}">
                                                Đổi trạng thái
                                            </button>
                                        </form>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                    <div class="d-flex justify-content-between align-items-center">
                        <span></span>
                        <nav aria-label="Page navigation">
                            <ul class="pagination mb-0">
                                <c:if test="${currentPage > 1}">
                                    <li class="page-item">
                                        <a class="page-link" href="?page=${currentPage - 1}&search=${param.search}&oldSearch=${oldSearch}">Previous</a>
                                    </li>
                                </c:if>

                                <c:forEach var="i" begin="1" end="${totalPages}">
                                    <li class="page-item ${i == currentPage ? 'active' : ''}">
                                        <a class="page-link" href="?page=${i}&search=${param.search}&oldSearch=${oldSearch}">${i}</a>
                                    </li>
                                </c:forEach>

                                <c:if test="${currentPage < totalPages}">
                                    <li class="page-item">
                                        <a class="page-link" href="?page=${currentPage + 1}&search=${param.search}&oldSearch=${oldSearch}">Next</a>
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
</html>
