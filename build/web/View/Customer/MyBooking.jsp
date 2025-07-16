<%-- 
    
    Author     : SONNAM
--%>

<%@page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@page import="models.CustomerAccount"%>
<%@page import="models.Booking"%>
<%@page import="models.BookingDetail"%>
<%@page import="java.util.List"%>
<%@page import="java.util.HashMap"%>

<%
    CustomerAccount ca = (CustomerAccount) request.getSession().getAttribute("customerAccount");
    String status = request.getParameter("status");
    if (status == null || (!status.equals("upcoming") && !status.equals("completed"))) {
        status = "upcoming";
    }
    java.util.Date now = new java.util.Date();
    request.setAttribute("now", now);
%>

<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <title>Lịch sử đặt phòng</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.6/dist/css/bootstrap.min.css" rel="stylesheet">
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.1/font/bootstrap-icons.css">
        <link rel="stylesheet" href="Css/Customer/Profile.css"/>
        <style>
            body {
                background-color: #f5f7fa;
                font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            }
            .container-xl {
                max-width: 1200px;
            }
            .content-card {
                background-color: #fff;
                border-radius: 8px;
                padding: 20px;
                box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
            }
            .booking-card {
                padding: 15px;
                border-bottom: 1px solid #e9ecef;
            }
            .booking-card:last-child {
                border-bottom: none;
            }
            .booking-title {
                font-size: 1.1rem;
                font-weight: 500;
                margin-bottom: 5px;
            }
            .booking-info {
                color: #6c757d;
                font-size: 0.9rem;
                margin-bottom: 3px;
            }
            .btn-secondary {
                background-color: #6c757d;
                border: none;
                color: #fff;
            }
            .btn-secondary:hover {
                background-color: #5c636a;
            }
            .nav-tabs .nav-link {
                color: #333;
                border: none;
                border-bottom: 2px solid transparent;
                padding: 10px 15px;
            }
            .nav-tabs .nav-link.active {
                color: #007bff;
                border-bottom-color: #007bff;
                font-weight: 500;
            }
            .pagination .page-link {
                color: #007bff;
            }
        </style>
    </head>
    <body>
        <jsp:include page="Header.jsp" />

        <div class="container-xl py-4">
            <div class="row">
                <div class="col-md-3">
                    <jsp:include page="SidebarProfile.jsp">
                        <jsp:param name="type" value="booking"/>
                    </jsp:include>
                </div>
                <div class="col-md-9">
                    <div class="content-card">
                        <h4 class="mb-4">Lịch sử đặt phòng của tôi</h4>
                        <ul class="nav nav-tabs mb-4">
                            <li class="nav-item">
                                <a class="nav-link ${status == 'upcoming' ? 'active' : ''}" href="${pageContext.request.contextPath}/customerProfile?service=booking&status=upcoming">Sắp tới</a>
                            </li>
                            <li class="nav-item">
                                <a class="nav-link ${status == 'completed' ? 'active' : ''}" href="${pageContext.request.contextPath}/customerProfile?service=booking&status=completed">Hoàn thành</a>
                            </li>
                        </ul>

                        <div class="booking-list">
                            <c:forEach var="dl" items="${detailList}">
                                <div class="booking-card">
                                    <div class="booking-details">
                                        <div class="booking-title">Mã đơn: ${dl.key.bookingId}</div>
                                        <div class="booking-info">Ngày thanh toán: <fmt:formatDate value="${dl.key.payDay}" pattern="dd/MM/yyyy" /></div>
                                        <div class="booking-info">Trạng thái: <span class="badge ${dl.key.status == 'Upcoming' ? 'bg-warning text-dark' : 'bg-success'}">${dl.key.status}</span></div>
                                        <div class="booking-info">Phương thức thanh toán: ${dl.key.paymentMethod.paymentName != null ? dl.key.paymentMethod.paymentName : 'Tiền mặt'}</div>
                                    </div>
                                    <div class="d-flex justify-content-end mt-2">
                                        <a href="${pageContext.request.contextPath}/customerProfile?service=bookingDetail&id=${dl.key.bookingId}" class="btn btn-primary">Xem chi tiết</a>
                                        <c:if test="${dl.key.status == 'Completed CheckOut'}">
                                            <a href="#" class="btn btn-secondary ms-2">Gửi bài đánh giá</a>
                                        </c:if>
                                    </div>
                                </div>
                            </c:forEach>
                        </div>
                    </div>

                    <nav aria-label="Pagination" class="mt-4">
                        <ul class="pagination justify-content-center">
                            <li class="page-item ${currentPage == 1 ? 'disabled' : ''}">
                                <a class="page-link" href="${pageContext.request.contextPath}/customerProfile?service=booking&page=${currentPage - 1}&status=${status}">Previous</a>
                            </li>
                            <c:forEach var="i" begin="1" end="${endPage}">
                                <li class="page-item ${i == currentPage ? 'active' : ''}">
                                    <a class="page-link" href="${pageContext.request.contextPath}/customerProfile?service=booking&page=${i}&status=${status}">${i}</a>
                                </li>
                            </c:forEach>
                            <li class="page-item ${currentPage == endPage ? 'disabled' : ''}">
                                <a class="page-link" href="${pageContext.request.contextPath}/customerProfile?service=booking&page=${currentPage + 1}&status=${status}">Next</a>
                            </li>
                        </ul>
                    </nav>
                </div>
            </div>
        </div>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.6/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>