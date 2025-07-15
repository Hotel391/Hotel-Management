<%-- 
    Document   : DetailBooking
    Created on : Jul 8, 2025, 8:18:00 PM
    Author     : Hai Long
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
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
    </head>
    <body>
        <div class="containerBox">
            <jsp:include page="leftNav.jsp" /> 
            <div class="right-section">
                <jsp:include page="topNav.jsp" />
                <div class="main-content">
                    <h3>Chi tiết hóa đơn của khách hàng ${requestScope.bookingInfo.customer.fullName} ngày ${requestScope.bookingInfo.payDay}</h3>
                    <div class="container-fluid p-4">
                        <ul class="nav nav-tabs mb-3">
                            <li class="nav-item">
                                <a class="nav-link active" href="${pageContext.request.contextPath}/manager/receipt?customerId=${customerId}">View receipt</a>
                            </li>
                        </ul>

                        <div class="d-flex justify-content-between align-items-center mb-3">
                            

                            <form method="get" action="${pageContext.request.contextPath}/manager/detailBooking" class="d-flex gap-2">
                                <label class="input-group-text" for="startDate">Số phòng</label>
                                <input type="hidden" name="bookingId" value="${requestScope.bookingInfo.bookingId}">
                                <input type="number" name="searchRoom" value="${param.searchRoom}">
                                <button type="submit" class="btn btn-primary">Filter</button>
                            </form>
                        </div>
                        <c:if test="${not empty error}"> 
                            <p class="alert alert-danger">${error}</p>
                        </c:if>



                        <div class="table-container">
                            <table class="table align-middle">
                                <thead class="table-light">
                                    <tr>
                                        <th scope="col">Số phòng </th>
                                        <th scope="col">Loại phòng</th>
                                        <th scope="col">Ngày đến</th>
                                        <th scope="col">Ngày về</th>
                                        <th scope="col">Các dịch vụ đã sử dụng</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="dl" items="${requestScope.detailList}">

                                        <tr>

                                            <td>${dl.room.roomNumber}</td>
                                            <td>${dl.room.typeRoom.typeName}</td>
                                            <td>${dl.startDate}</td>
                                            <td>${dl.endDate}</td>


                                            <td class="viewTypeRoom">
                                                <!-- Modal -->

                                                <!-- Button trigger modal -->
                                                <button type="button" class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#viewDetail_${dl.room.roomNumber}">
                                                    <i class="bi bi-eye"></i> View services 
                                                </button>
                                                <div class="modal fade" id="viewDetail_${dl.room.roomNumber}" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
                                                    <div class="modal-dialog">
                                                        <div class="modal-content">
                                                            <div class="modal-header">
                                                                
                                                                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                                                            </div>
                                                            <div class="modal-body">

                                                                <div class="row g-3">
                                                                    <h5 class="title">Các dịch vụ đi kèm</h5>
                                                                    <div class="services-grid services">
                                                                        <c:forEach var="ds" items="${dl.services}">
                                                                            <div class="service-card wifi">
                                                                                <div class="service-icon">
                                                                                    <i class="fas fa-wifi"></i>
                                                                                </div>
                                                                                <div class="service-name">${ds.service.serviceName}</div>
                                                                                <c:if test="${ds.service.price != 0}">
                                                                                    <div class="service-quantity">${ds.quantity} thẻ</div>
                                                                                    <div class="service-price">${ds.service.price} VNĐ</div>
                                                                                </c:if>
                                                                                <c:if test="${ds.service.price == 0}">
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
        <script src="${pageContext.request.contextPath}/Js/navDashboardJs.js"></script>
        <script src="${pageContext.request.contextPath}/Js/userProfileJs.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.6/dist/js/bootstrap.bundle.min.js" integrity="sha384-j1CDi7MgGQ12Z7Qab0qlWQ/Qqz24Gc6BM0thvEMVjHnfYGF0rmFCozFSxQBxwHKO" crossorigin="anonymous"></script>
    </body>
</html>
