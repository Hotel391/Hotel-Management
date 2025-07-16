<%-- 
    Document   : Checkout
    Created on : Jun 19, 2025, 7:45:36 AM
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
        <%--another in the following--%>
    </head>
    <body>
        <div class="containerBox">
            <jsp:include page="leftNavReceptionist.jsp" /> 
            <div class="right-section">
                <c:set var="title" value="Checkout" scope="request"/>
                <jsp:include page="topNavReceptionist.jsp" />
                <div class="main-content">
                    <div class="d-flex justify-content-between align-items-center mb-3">
                        <a class="btn btn-primary" href="${pageContext.request.contextPath}/receptionist/roomInformation?service=backToServicePage">Back</a>
                        <form method="post" action="${pageContext.request.contextPath}/receptionist/checkout" class="d-flex gap-2">
                            <label class="input-group-text" for="startDate">Email</label>
                            <input type="text" name="emailSearch" value="${param.emailSearch != null ? param.emailSearch : requestScope.emailSearch}" class="form-control search-input" required/>
                            <input type="hidden" name="search" value="searchEmail">
                            <button type="submit" class="btn btn-primary">Check</button>
                            <a href="${pageContext.request.contextPath}/receptionist/checkout" class="btn btn-primary">Reset</a>
                        </form>

                    </div>
                    <c:if test="${not empty searchError}">
                        <div class="d-flex justify-content-end align-items-center mb-3">
                            <p class="alert alert-danger">${searchError}</p>
                        </div>
                    </c:if>

                    <c:if test="${not empty newCustomer}">
                        <div class="d-flex justify-content-end align-items-center mb-3">
                            <p class="alert alert-success">${newCustomer}</p>
                        </div>
                    </c:if>
                    <h2 class="info-title">Thông tin đặt phòng</h2>
                    <div class="booking-content">
                        <div class="booking-detail">
                            <div class="date-info">
                                <p>Ngày checkin: ${sessionScope.startDate}</p>
                                <p>Ngày checkout: ${sessionScope.endDate}</p>
                            </div>
                            <div class="roomNService">

                                <div class="service">
                                    <!-- Button trigger modal -->
                                    <button type="button" class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#exampleModal">
                                        Dịch vụ
                                    </button>
                                    <!-- Modal -->
                                    <div class="modal fade" id="exampleModal" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
                                        <div class="modal-dialog">
                                            <div class="modal-content">
                                                <div class="modal-header">
                                                    <h1 class="modal-title fs-5" id="exampleModalLabel">Các dịch vụ đi kèm</h1>
                                                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                                                </div>
                                                <c:forEach var="dt" items="${sessionScope.roomTypeMap}">
                                                    <div class="modal-body">
                                                        <h3>Số phòng: ${dt.key} - ${dt.value}</h3>
                                                        <div class="service-room">
                                                            <h5>Các dịch vụ đi kèm</h5>
                                                            <c:forEach var="sr" items="${sessionScope.roomServicesMap}">
                                                                <c:if test="${sr.key == dt.key}">
                                                                    <c:forEach var="sv" items="${sr.value}">
                                                                        <p>${sv.service.serviceName} - ${sv.quantity}</p>
                                                                    </c:forEach>
                                                                </c:if>
                                                            </c:forEach>
                                                        </div>
                                                    </div>
                                                </c:forEach>
                                                <div class="modal-footer">
                                                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="totalPrice">Tổng tiền cần thanh toán: <fmt:formatNumber value="${sessionScope.totalPrice}" type="number" groupingUsed="true"/> VNĐ</div>
                        </div>
                    </div>
                    <c:choose>
                        <c:when test="${not empty existedCustomer}">
                            <c:set value="${existedCustomer}" var="ec" scope="request"/>
                            <jsp:include page="ExistedCustomer.jsp" />
                        </c:when>
                        <c:otherwise>
                            <jsp:include page="AddCustomerInfo.jsp"/>
                        </c:otherwise>
                    </c:choose>
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
