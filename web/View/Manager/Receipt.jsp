<%-- 
    Document   : Receipt
    Created on : Jun 16, 2025, 10:03:30 PM
    Author     : Hai Long
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.6/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-4Q6Gf2aSP4eDXB8Miphtr37CMZZQ5oXLH2yaXMJ2w8e2ZtHTl7GptT4jmndRuHDT" crossorigin="anonymous">
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.1/font/bootstrap-icons.css">
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
                                <a class="nav-link active" href="${pageContext.request.contextPath}/manager/receipt?customerId=${customerId}">View receipt</a>
                            </li>
                        </ul>

                        <div class="d-flex justify-content-between align-items-center mb-3">


                            <form method="get" action="${pageContext.request.contextPath}/manager/receipt" class="d-flex gap-2">
                                <label class="input-group-text" for="startDate">From</label>
                                <input type="date" name="startDate" value="${param.startDate}" class="form-control search-input" />
                                <label class="input-group-text" for="startDate">To</label>
                                <input type="date" name="endDate" value="${param.endDate}" class="form-control search-input" />
                                <input type="hidden" name="search" value="search">
                                <input type="hidden" name="page" value="${currentPage}">
                                <input type="hidden" name="customerId" value="${customerId}">
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
                                            <td>${dl.key.payDay}</td>
                                            <td><fmt:formatNumber value="${dl.key.totalPrice}" type="number" groupingUsed="true"/> VNĐ</td>
                                            <td>${dl.key.status}</td>
                                            <td style="width: 160px; text-align: center;">${dl.key.paymentMethod.paymentName}</td>
                                            <td class="viewTypeRoom">
                                                <!-- Modal -->
                                                <c:forEach var="dt" items="${dl.value}">
                                                    <!-- Button trigger modal -->
                                                    <button type="button" class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#viewDetail_${dt.bookingDetailId}">
                                                        <i class="bi bi-eye"></i> View detail
                                                    </button>
                                                    <div class="modal fade" id="viewDetail_${dt.bookingDetailId}" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
                                                        <div class="modal-dialog">
                                                            <div class="modal-content">
                                                                <div class="modal-header">
                                                                    <h1 class="modal-title fs-5" id="exampleModalLabel">${dl.key.customer.fullName}</h1>
                                                                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                                                                </div>
                                                                <div class="modal-body">
                                                                    <div class="row g-3">
                                                                        <div class="col md-6">
                                                                            <label class="input-group-text" for="roomNumber">Room Number</label>
                                                                            <input type="text" value="${dt.room.roomNumber}" class="form-control">
                                                                        </div>
                                                                        <div class="col md-6">
                                                                            <label class="input-group-text" for="startDate">Start Date</label>
                                                                            <input type="text" value="${dt.startDate}" class="form-control">
                                                                        </div>
                                                                        <div class="col md-6">
                                                                            <label class="input-group-text" for="startDate">End Date</label>
                                                                            <input type="text" value="${dt.endDate}" class="form-control">
                                                                        </div>
                                                                    </div>
                                                                    <div class="row g-3">
                                                                        <h5 class="title">Các dịch vụ đi kèm</h5>
                                                                        <div class="services-grid services">
                                                                            <c:forEach var="ds" items="${dt.services}">
                                                                                <div class="service-card wifi">
                                                                                    <div class="service-icon">
                                                                                        <i class="fas fa-wifi"></i>
                                                                                    </div>
                                                                                    <div class="service-name">${ds.serviceName}</div>
                                                                                    <c:if test="${ds.price != 0}">
                                                                                        <div class="service-price">${ds.price} VNĐ</div>
                                                                                    </c:if>
                                                                                    <c:if test="${ds.price == 0}">
                                                                                        <div class="service-price">Free</div>
                                                                                    </c:if>

                                                                                </div>  
                                                                            </c:forEach>
                                                                        </div>
                                                                    </div>
                                                                </div>
                                                                <div class="modal-footer">
                                                                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>

                                                                </div>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </c:forEach>


                                            </td>
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
                                        <a href="?page=${currentPage - 1}&startDate=${start}&endDate=${end}&search=search&customerId=${customerId}" class="page-link">Previous</a>
                                    </li>
                                </c:when>
                                <c:otherwise>
                                    <li class="page-item ${currentPage == 1 ? 'disabled' : ''}">
                                        <a href="?page=${currentPage - 1}&customerId=${customerId}" class="page-link">Previous</a>
                                    </li>
                                </c:otherwise>
                            </c:choose>
                            <c:forEach var="i" begin="1" end="${endPage}">
                                <c:choose>
                                    <c:when test="${search != null && !search.isEmpty()}">
                                        <li class="page-item ${i == currentPage ? 'active' : ''}">
                                            <a class="page-link" href="?page=${i}&startDate=${start}&endDate=${end}&search=search&customerId=${customerId}">${i}</a>
                                        </li>

                                    </c:when>
                                    <c:otherwise>
                                        <li class="page-item ${i == currentPage ? 'active' : ''}">
                                            <a class="page-link" href="?page=${i}&customerId=${customerId}">${i}</a>
                                        </li>
                                    </c:otherwise>
                                </c:choose>

                            </c:forEach>
                            <c:choose>
                                <c:when test="${search != null && !search.isEmpty()}">

                                    <li class="page-item ${currentPage == endPage ? 'disabled' : ''}">
                                        <a href="?page=${currentPage + 1}&startDate=${start}&endDate=${end}&search=search&customerId=${customerId}" class="page-link">Next</a>
                                    </li>
                                </c:when>
                                <c:otherwise>
                                    <li class="page-item ${currentPage == endPage ? 'disabled' : ''}">
                                        <a href="?page=${currentPage + 1}&customerId=${customerId}" class="page-link">Next</a>
                                    </li>
                                </c:otherwise>
                            </c:choose>
                        </ul>
                    </nav>
                </div>
            </div>
        </div>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.6/dist/js/bootstrap.bundle.min.js" integrity="sha384-j1CDi7MgGQ12Z7Qab0qlWQ/Qqz24Gc6BM0thvEMVjHnfYGF0rmFCozFSxQBxwHKO" crossorigin="anonymous"></script>
    </body>
</html>
