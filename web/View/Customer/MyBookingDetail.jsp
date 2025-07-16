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

<%
    CustomerAccount ca = (CustomerAccount) session.getAttribute("customerAccount");
    Booking booking = (Booking) request.getAttribute("booking");
    List<BookingDetail> details = (List<BookingDetail>) request.getAttribute("details");
    request.setAttribute("customerAccount", ca);
    request.setAttribute("type", "booking");
%>

<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <title>Chi tiết đơn đặt phòng</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.6/dist/css/bootstrap.min.css" rel="stylesheet">
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.1/font/bootstrap-icons.css">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
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
            .detail-row {
                padding: 10px 0;
                border-bottom: 1px solid #e9ecef;
            }
            .detail-row:last-child {
                border-bottom: none;
            }
            .detail-label {
                font-weight: 500;
                color: #333;
            }
            .badge-status {
                font-size: 0.9rem;
            }
            .service-card {
                display: flex;
                align-items: center;
                padding: 10px;
                margin: 5px 0;
                border: 1px solid #e9ecef;
                border-radius: 5px;
                background-color: #f8f9fa;
            }
            .service-icon {
                margin-right: 10px;
            }
            .service-name {
                font-weight: 500;
            }
            .service-quantity, .service-price {
                margin-left: 10px;
                color: #555;
            }
        </style>
    </head>
    <body>
        <jsp:include page="Header.jsp" />

        <div class="container-xl py-4">
            <div class="row">
                <!-- Sidebar -->
                <div class="col-md-3">
                    <jsp:include page="SidebarProfile.jsp" />
                </div>

                <!-- Main Content -->
                <div class="col-md-9">
                    <div class="content-card">
                        <h4 class="mb-4">Chi tiết đơn đặt phòng</h4>

                        <c:if test="${not empty error}">
                            <p class="text-danger">${error}</p>
                        </c:if>
                        <c:if test="${booking == null}">
                            <p class="text-danger">Không tìm thấy thông tin đơn đặt phòng.</p>
                        </c:if>
                        <c:if test="${booking != null}">
                            <div class="row mb-3">
                                <div class="col-md-6">
                                    <p class="mb-2"><strong>Mã đơn:</strong> ${booking.bookingId}</p>
                                    <p class="mb-2"><strong>Ngày thanh toán:</strong> <fmt:formatDate value="${booking.payDay}" pattern="dd/MM/yyyy" /></p>
                                </div>
                                <div class="col-md-6">
                                    <p class="mb-2"><strong>Trạng thái:</strong> <span class="badge ${booking.status == 'Upcoming' ? 'bg-warning text-dark' : 'bg-success'} badge-status">${booking.status}</span></p>
                                    <p class="mb-2"><strong>Phương thức thanh toán:</strong> ${booking.paymentMethod.paymentName != null ? booking.paymentMethod.paymentName : 'Tiền mặt'}</p>
                                </div>
                            </div>

                            <h5 class="mt-4 mb-3">Chi tiết phòng</h5>
                            <div class="table-responsive">
                                <table class="table table-bordered">
                                    <thead class="table-light">
                                        <tr>
                                            <th>Phòng</th>
                                            <th>Loại phòng</th>
                                            <th>Ngày check-in</th>
                                            <th>Ngày check-out</th>
                                            <th>Tổng tiền</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:if test="${empty details}">
                                            <tr>
                                                <td colspan="5" class="text-center">Không có chi tiết phòng nào.</td>
                                            </tr>
                                        </c:if>
                                        <c:forEach var="d" items="${details}">
                                            <tr>
                                                <td>${d.room != null ? d.room.roomNumber : 'N/A'}</td>
                                                <td>${d.room != null && d.room.typeRoom != null ? d.room.typeRoom.typeName : 'N/A'}</td>
                                                <td><fmt:formatDate value="${d.startDate}" pattern="dd/MM/yyyy" /></td>
                                                <td><fmt:formatDate value="${d.endDate}" pattern="dd/MM/yyyy" /></td>
                                                <td><fmt:formatNumber value="${d.totalAmount}" type="currency" currencySymbol="đ" /></td>
                                            </tr>
                                            <tr>
                                                <td colspan="5">
                                                    <c:choose>
                                                        <c:when test="${empty d.services}">
                                                            <p>Không có dịch vụ</p>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <c:forEach var="ds" items="${d.services}">
                                                                <div class="service-card wifi">
                                                                    <div class="service-name">${ds.service.serviceName != null ? ds.service.serviceName : 'N/A'}</div>
                                                                    <c:if test="${ds.service.price != 0}">
                                                                        <div class="service-quantity">${ds.quantity} thẻ</div>
                                                                        <div class="service-price"><fmt:formatNumber value="${ds.service.price}" type="currency" currencySymbol="đ" /></div>
                                                                    </c:if>
                                                                    <c:if test="${ds.service.price == 0}">
                                                                        <div class="service-price">Free</div>
                                                                    </c:if>
                                                                </div>
                                                            </c:forEach>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </td>
                                            </tr>
                                        </c:forEach>
                                    </tbody>
                                </table>
                            </div>
                        </c:if>

                        <div class="text-end mt-4">
                            <a href="${pageContext.request.contextPath}/customerProfile?service=booking&type=booking&username=${customerAccount.username}&status=upcoming" class="btn btn-secondary">Quay lại</a>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.6/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>
