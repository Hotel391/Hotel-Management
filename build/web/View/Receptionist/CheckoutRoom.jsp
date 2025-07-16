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
                <c:set var="title" value="Checkout Room" scope="request"/>
                <jsp:include page="topNavReceptionist.jsp" />
                <div class="main-content">
                    <h1 class="mb-4">Danh sách phòng checkout</h1>
                    <form class="mb-3" method="get">
                        <div class="input-group" style="max-width: 400px;">
                            <input type="text" class="form-control" name="phoneSearch" placeholder="Nhập số điện thoại" value="${param.phoneSearch}">
                            <button class="btn btn-outline-primary" type="submit">Search</button>
                            <a class="btn btn-outline-secondary" href="${pageContext.request.contextPath}/receptionist/checkoutRoom">Clear</a>
                        </div>
                    </form>
                    <c:if test="${not empty paymentMethodError}">
                        <p class="alert alert-primary">${paymentMethodError}</p>
                    </c:if>
                    <table id="roomTable" class="table table-bordered table-hover text-center align-middle">
                        <thead class="table-primary">
                            <tr>
                                <th>Booking ID</th>
                                <th>Họ tên</th>
                                <th>SDT</th>
                                <th>Số phòng</th>
                                <th>Tổng tiền</th>
                                <th>Số tiền đã thanh toán</th>
                                <th>Số tiền cần thanh toán</th>
                                <th></th>
                            </tr>
                        </thead>
                        <tbody>

                            <c:forEach var="ckl" items="${checkoutList}">
                                <c:set var="totalAmount" value="0"></c:set>
                                    <tr>
                                <form action="${pageContext.request.contextPath}/receptionist/checkoutRoom" method="post">
                                <td>${ckl.key.bookingId}</td>
                                <td>${ckl.key.customer.fullName}</td>
                                <td>${ckl.key.customer.phoneNumber}</td>

                                <td>
                                    <!-- Button trigger modal -->
                                    <button type="button" class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#roomModal_${ckl.key.bookingId}">
                                        Rooms
                                    </button>

                                    <!-- Modal -->
                                    <div class="modal fade" id="roomModal_${ckl.key.bookingId}" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
                                        <div class="modal-dialog">
                                            <div class="modal-content">
                                                <div class="modal-header">
                                                    <h1 class="modal-title fs-5" id="exampleModalLabel">Các phòng được đặt</h1>
                                                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                                                </div>
                                                <div class="modal-body">
                                                    <c:forEach var="bd" items="${ckl.value}">
                                                        <p>Phòng số: ${bd.room.roomNumber}</p>
                                                        <c:set var="totalAmount" value="${totalAmount + bd.totalAmount}"></c:set>
                                                    </c:forEach>
                                                </div>
                                                <div class="modal-footer">
                                                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>

                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </td>
                                <td>${totalAmount} VND</td>
                                <td>${ckl.key.paidAmount} VND</td>
                                <td>${totalAmount - ckl.key.paidAmount} VND</td>
                                <c:if test="${totalAmount - ckl.key.paidAmount > 0}">
                                    <td style="width: 230px;">
                                        <select name="paymentMethod" class="form-select w-160" aria-label="Default select example">
                                            <option selected value="default">Chọn phương thức</option>
                                            <option value="online">Chuyển khoản</option>
                                            <option value="offline">Tiền mặt</option>
                                        </select>
                                    </td>
                                </c:if>

                                <c:if test="${totalAmount - ckl.key.paidAmount == 0}">
                                    <td>Đã thanh toán đủ tiền</td>
                                    <input type="hidden" name="unPaidAmount" value="donePayment">
                                </c:if>

                                <td>
                                    <input type="hidden" name="service" value="checkout">
                                    <input type="hidden" name="customerId" value="${ckl.key.customer.customerId}">
                                    <input type="hidden" name="bookingId" value="${ckl.key.bookingId}">
                                    <button type="submit" class="btn btn-sm btn-warning">
                                        Checkout
                                    </button>
                                </td>
                            </form>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>


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
