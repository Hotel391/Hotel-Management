<%-- 
    Document   : CheckoutRoom
    Created on : Jun 17, 2025, 9:27:04 PM
    Author     : Hai Long
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c"  uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
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
                    <h1 class="mb-4">Danh sách phòng checkout ngày hôm nay (<fmt:formatDate value="${today}" pattern="dd-mm-yyyy"/>)</h1>
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
                                <th>Họ tên</th>
                                <th>SDT</th>
                                <th>Số phòng</th>

                                <th></th>
                            </tr>
                        </thead>
                        <tbody>
                            
                            <c:forEach var="ckl" items="${checkoutList}">
                            <tr>
                                <td>${ckl.value.fullName}</td>
                                <td>${ckl.value.phoneNumber}</td>
                                <td>${ckl.key.room.roomNumber}</td>
                                <td>
                                    <form action="">
                                        <button type="submit" class="btn btn-sm btn-warning">
                                            Checkout
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
        <%--script for dashboard--%>

        <script src="${pageContext.request.contextPath}/Js/navDashboardJs.js"></script>
        <script src="${pageContext.request.contextPath}/Js/userProfileJs.js"></script>
        <%--another in following--%>
        <script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.9.2/dist/umd/popper.min.js" integrity="sha384-IQsoLXl5PILFhosVNubq5LC7Qb9DXgDA9i+tQ8Zj3iwWAwPtgFTxbJ8NT4GN1R8p" crossorigin="anonymous"></script>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.min.js" integrity="sha384-cVKIPhGWiC2Al4u+LWgxfKTRIcfu0JTxR+EQDz/bgldoEyl4H0zUF0QKbrJ0EcQF" crossorigin="anonymous"></script>
    </body>

</html>
