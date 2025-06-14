<%-- Author : SONNAM --%>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>

    <head>
        <meta charset="UTF-8">
        <title>Cleaner Page</title>
        <%--style for dashboard--%>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css"
              rel="stylesheet"
              integrity="sha384-EVSTQN3/azprG1Anm3QDgpJLIm9Nao0Yz1ztcQTwFspd3yD65VohhpuuCOmLASjC"
              crossorigin="anonymous">
        <link rel="stylesheet"
              href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.1/font/bootstrap-icons.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/Css/navDashboardStyle.css" />
        <link rel="stylesheet" href="${pageContext.request.contextPath}/Css/dashboardStyle.css" />
        <link rel="stylesheet" href="${pageContext.request.contextPath}/Css/mainDashboardStyle.css" />
        <%--another in the following--%>
        <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
        <style>
            /* Thu nhỏ chiều cao và padding của các ô tr */
            .table-cleaner tbody tr td {
                padding-top: 0.3rem;
                padding-bottom: 0.3rem;
                padding-left: 0.5rem;
                padding-right: 0.5rem;
                vertical-align: middle;
                font-size: 30px;
            }

            /* Tăng kích thước checkbox và căn trái */
            .table-cleaner input[type="checkbox"] {
                width: 50px;
                height: 50px;
                margin-left: 0;
                margin-right: 0.5rem;
                accent-color: #198754;
                /* màu xanh của bootstrap success */
                vertical-align: middle;
            }

            .table-cleaner td:first-child {
                text-align: left;
            }
        </style>
    </head>

    <body>
        <div class="containerBox">
            <jsp:include page="leftNavCleaner.jsp" />
            <div class="right-section">
                <c:set var="title" value="Cleaner Page" scope="request" />
                <jsp:include page="topNavCleaner.jsp" />
                <div class="main-content">
                    <h2 class="mb-4">Phòng cần dọn</h2>
                    <form id="cleanForm" method="post">
                        <table class="table table-bordered table-hover text-center table-cleaner">
                            <thead class="table-danger">
                                <tr>
                                    <th style="width: 60px; text-align: left;">Chọn</th>
                                    <th style="width: 100px;">Số phòng</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="room" items="${rooms}">
                                    <tr>
                                        <td>
                                            <input type="checkbox" name="roomIds" value="${room.roomNumber}" />
                                        </td>
                                        <td>Phòng ${room.roomNumber}</td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                        <div class="d-flex justify-content-between align-items-center">
                            <button type="submit" class="btn btn-success">Đánh dấu đã dọn</button>
                            <nav>
                                ssss
                            </nav>
                        </div>
                    </form>
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