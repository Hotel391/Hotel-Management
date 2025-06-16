<%-- 
    Document   : Receipt
    Created on : Jun 16, 2025, 10:03:30 PM
    Author     : Hai Long
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.6/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-4Q6Gf2aSP4eDXB8Miphtr37CMZZQ5oXLH2yaXMJ2w8e2ZtHTl7GptT4jmndRuHDT" crossorigin="anonymous">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/Css/navDashboardStyle.css"/>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/Css/mainDashboardStyle.css" />
        <link rel="stylesheet" href="${pageContext.request.contextPath}/Css/dashboardStyle.css" />
        <link rel="stylesheet" href="${pageContext.request.contextPath}/Css/TypeRoom.css"/>
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
                                <a class="nav-link active" href="#">View receipt</a>
                            </li>
                        </ul>

                        <div class="d-flex justify-content-between align-items-center mb-3">
                            

                            <form method="get" action="${pageContext.request.contextPath}/admin/types" class="d-flex gap-2">
                                <input type="text" name="key" value="${param.key}" class="form-control search-input" placeholder="Search" />

                                <button type="submit" class="btn btn-primary">Filter</button>
                            </form>
                        </div>

                        <c:if test="${not empty error}">
                            <div class="alert alert-danger mt-3">${error}</div>
                        </c:if>

                        <div class="table-container">
                            <table class="table align-middle">
                                <thead class="table-light">
                                    <tr>
                                        <th scope="col">Customer's name</th>
                                        <th scope="col">Pay Day</th>
                                        <th scope="col">Total Price</th>
                                        <th scope="col">Status</th>
                                        <th scope="col">payment method</th>
                                    </tr>
                                </thead>
                                <tbody>
                                <c:forEach var="bl" items="${requestScope.bookList}">
                                    
                                        <tr>
                                            <td>${bl.customer.fullName}</td>
                                            <td>${bl.payDay}</td>
                                            <td>${bl.totalPrice}</td>
                                            <td>${bl.status}</td>
                                            <td>${bl.paymentMethod.paymentName}</td>
<!--                                            <td class="viewTypeRoom">
                                                 View typeroom Modal 
                                                <button onclick="clearMessage(${trl.typeId})" class="btn btn-sm btn-outline-info me-1" data-bs-toggle="modal" data-bs-target="#viewTypeRoomModal_${trl.typeId}">
                                                    <i class="bi bi-eye"></i> View 
                                                </button>
                                                
                                            </td>


                                            <td class="viewService">
                                                <button class="btn btn-sm btn-outline-info me-1" data-bs-toggle="modal" data-bs-target="#serviceModal_${trl.typeId}">
                                                    <i class="bi bi-eye"></i> View 
                                                </button>
                                               
                                            </td>
                                            <td>
                                                 Edit Employee Modal 
                                                <button onclick="resetFormData(${trl.typeId})" class="btn btn-sm btn-outline-primary me-1" data-bs-toggle="modal" data-bs-target="#editTypeRoomModal_${trl.typeId}">
                                                    <i class="bi bi-pencil"></i> Edit
                                                </button>

                                                

                                            </td>
                                            <td>
                                                 Delete Employee Modal 
                                                <button class="btn btn-sm btn-outline-danger" data-bs-toggle="modal" data-bs-target="#deleteModal_${trl.typeId}">
                                                    <i class="bi bi-trash"></i> Delete

                                                </button>
                                                
                                            </td>-->
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </div>
                    </div>
                    <nav aria-label="Pagination">
                        <ul class="pagination pagination-danger">
                            <c:choose>
                                <c:when test="${key != null && !key.isEmpty()}">
                                    <li class="page-item ${currentPage == 1 ? 'disabled' : ''}">
                                        <a href="?page=${currentPage - 1}&key=${key}" class="page-link">Previous</a>
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
                                        <a href="?page=${currentPage + 1}&key=${key}" class="page-link">Next</a>
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
    </body>
</html>
