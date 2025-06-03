<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>

    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Dashboard</title>

        <%--style for dashboard--%>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css" rel="stylesheet"
              integrity="sha384-EVSTQN3/azprG1Anm3QDgpJLIm9Nao0Yz1ztcQTwFspd3yD65VohhpuuCOmLASjC"
              crossorigin="anonymous">
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.1/font/bootstrap-icons.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/Css/navDashboardStyle.css" />
        <link rel="stylesheet" href="${pageContext.request.contextPath}/Css/dashboardStyle.css" />
        <link rel="stylesheet" href="${pageContext.request.contextPath}/Css/mainDashboardStyle.css" />
        <%--another in the following--%>

        <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>

    </head>

    <body>
        <div class="containerBox">
            <jsp:include page="leftNav.jsp" />
            <div class="right-section">
                <c:set var="title" value="Dashboard" scope="request"/>
                <jsp:include page="topNav.jsp" />
                <div class="main-content">
                    <div class="total-information">
                        <div class="all-room">
                            <div><i class="bi bi-hospital-fill"></i></div>
                            <div class="retail-information">
                                <h5>${roomCount}</h5>
                                <h6>All Rooms</h6>
                            </div>
                        </div>
                        <div class="all-booking">
                            <span><i class="bi bi-bookmark-fill"></i></span>
                            <div class="retail-information">
                                <h5>${bookingCount}</h5>
                                <h6>All booking</h6>
                            </div>
                        </div>
                        <div class="all-checkin">
                            <span><i class="bi bi-check2-circle"></i></span>
                            <div class="retail-information">
                                <h5>${checkinCount}</h5>
                                <h6>All Checkin</h6>
                            </div>
                        </div>
                        <div class="all-checkouts">
                            <span><i class="bi bi-door-open-fill"></i></span>
                            <div class="retail-information">
                                <h5>${checkoutCount}</h5>
                                <h6>All checkout</h6>
                            </div>
                        </div>
                    </div>
                    <div class="detail-information">
                        <div class="detail-box">
                            <div class="room-status">
                                <div class="available-room">
                                    <span><i class="bi bi-door-closed-fill"></i></span>
                                    <div class="retail-information">
                                        <h6>Available Today:</h6>
                                        <h5>${availableRoomCount}</h5>
                                    </div>
                                </div>
                                <div class="sold-out-room">
                                    <span><i class="bi bi-x-circle-fill"></i></span>
                                    <div class="retail-information">
                                        <h6>Sold out Room Today:</h6>
                                        <h5>${soldOutRoomCount}</h5>
                                    </div>
                                </div>
                            </div>
                            <div class="room-status-title">
                                <div class="total-employee">
                                    <h5>${employeeCount}</h5>
                                    <h6>Total Employee</h6>
                                </div>
                                <div class="total-customer">
                                    <h5>${customerCount}</h5>
                                    <h6>Total Customer</h6>
                                </div>
                                <div class="total-room">
                                    <h5>${roomCount}</h5>
                                    <h6>Total Room</h6>
                                </div>
                                <div class="total-transaction">
                                    <h5>${bookingCount}</h5>
                                    <h6>Total Transaction</h6>
                                </div>
                            </div>
                        </div>
                        <div class="chart-column">
                            <% request.setAttribute("chartId", "doanhThuChart" );
                                request.setAttribute("chartType", "bar" );
                                request.setAttribute("chartLabel", "Revenue" );
                                request.setAttribute("axisDirection", "x" ); request.setAttribute("width", "800" );
                                request.setAttribute("height", "400" ); %>
                            <jsp:include page="templateChart.jsp" />
                        </div>
                    </div>
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