<%-- 
    Document   : Receipt
    Created on : Jun 19, 2025, 4:59:05 PM
    Author     : Hai Long
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
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
        <link rel="stylesheet" href="${pageContext.request.contextPath}/Css/Checkout.css"/>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/Css/TypeRoom.css"/>
        <%--another in the following--%>
    </head>
    <body>
        <div class="containerBox">
            <jsp:include page="leftNavReceptionist.jsp" /> 
            <div class="right-section">
                <jsp:include page="topNavReceptionist.jsp" />
                <div class="main-content">

                    <div class="container-fluid p-4">
                        <ul class="nav nav-tabs mb-3">
                            <li class="nav-item">
                                <a class="nav-link active" href="${pageContext.request.contextPath}/receptionist/receipt">View receipt</a>
                            </li>
                        </ul>

                        <div class="d-flex justify-content-between align-items-center mb-3">


                            <form method="get" action="${pageContext.request.contextPath}/receptionist/receipt" class="d-flex gap-2">
                                <label class="input-group-text" for="startDate">SDT</label>
                                <input type="number" name="searchPhone" value="${param.searchPhone}">
                                <button type="submit" class="btn btn-primary">Filter</button>
                            </form>
                        </div>
                        <c:if test="${not empty error}"> 
                            <p class="alert alert-danger">${error}</p>
                        </c:if>

                        <c:out value="${detailList.key}"></c:out>

                            <div class="table-container">
                                <table class="table align-middle">
                                    <thead class="table-light">
                                        <tr>
                                            <th scope="col">Customer's name</th>
                                            <th scope="col">SDT</th>
                                            <th scope="col">Pay Day</th>
                                            <th scope="col">Total Price</th>
                                            <th scope="col">Status</th>
                                            <th scope="col">payment method</th>


                                        </tr>
                                    </thead>
                                    <tbody>
                                    <c:forEach var="dl" items="${requestScope.detailList}">

                                        <tr>

                                            <td>${dl.key.customer.fullName}</td>
                                            <td>${dl.key.customer.phoneNumber}</td>
                                            <td>${dl.key.payDay}</td>
                                            <td><fmt:formatNumber value="${dl.key.totalPrice}" type="number" groupingUsed="true"/> VNĐ</td>
                                            <td>${dl.key.status}</td>
                                            <td style="width: 160px; text-align: center;">${dl.key.paymentMethod.paymentName}</td>
                                            <td><a class="btn btn-primary" href="${pageContext.request.contextPath}/receptionist/customerReceipt?bookingId=${dl.key.bookingId}">View Detail</a></td>
                                            
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </div>
                    </div>
                    <nav aria-label="Pagination">
                        <ul class="pagination pagination-danger">
                            <c:choose>
                                <c:when test="${search != null && !search.isEmpty()}">
                                    <li class="page-item ${currentPage == 1 ? 'disabled' : ''}">
                                        <a href="?page=${currentPage - 1}&searchPhone=${searchPhone}" class="page-link">Previous</a>
                                    </li>
                                </c:when>
                                <c:otherwise>
                                    <li class="page-item ${currentPage == 1 ? 'disabled' : ''}">
                                        <a href="?page=${currentPage - 1}" class="page-link">Previous</a>
                                    </li>
                                </c:otherwise>
                            </c:choose>
                            <c:forEach var="i" begin="1" end="${endPage}">
                                <c:choose>
                                    <c:when test="${search != null && !search.isEmpty()}">
                                        <li class="page-item ${i == currentPage ? 'active' : ''}">
                                            <a class="page-link" href="?page=${i}&searchPhone=${searchPhone}">${i}</a>
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
                                <c:when test="${search != null && !search.isEmpty()}">

                                    <li class="page-item ${currentPage == endPage ? 'disabled' : ''}">
                                        <a href="?page=${currentPage + 1}&searchPhone=${searchPhone}" class="page-link">Next</a>
                                    </li>
                                </c:when>
                                <c:otherwise>
                                    <li class="page-item ${currentPage == endPage ? 'disabled' : ''}">
                                        <a href="?page=${currentPage + 1}" class="page-link">Next</a>
                                    </li>
                                </c:otherwise>
                            </c:choose>
                        </ul>
                    </nav>
                </div>
            </div>
        </div>
        <script src="${pageContext.request.contextPath}/Js/navDashboardJs.js"></script>
        <script src="${pageContext.request.contextPath}/Js/userProfileJs.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.6/dist/js/bootstrap.bundle.min.js" integrity="sha384-j1CDi7MgGQ12Z7Qab0qlWQ/Qqz24Gc6BM0thvEMVjHnfYGF0rmFCozFSxQBxwHKO" crossorigin="anonymous"></script>
    </body>
</html>
