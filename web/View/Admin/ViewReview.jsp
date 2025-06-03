<%-- 
    Document   : ViewReview
    Created on : 31 thg 5, 2025, 14:39:14
    Author     : Tuan'sPC
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>View review</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/Css/cssAdmin/ViewReview.css">
        <%--style for dashbord--%>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css" rel="stylesheet"
              integrity="sha384-EVSTQN3/azprG1Anm3QDgpJLIm9Nao0Yz1ztcQTwFspd3yD65VohhpuuCOmLASjC"
              crossorigin="anonymous">
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.1/font/bootstrap-icons.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/Css/navDashboardStyle.css" />
        <link rel="stylesheet" href="${pageContext.request.contextPath}/Css/dashboardStyle.css" />
        <link rel="stylesheet" href="${pageContext.request.contextPath}/Css/mainDashboardStyle.css" />
        <%--write more in following--%>
        <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
    </head>
    <body>
        <div class="containerBox">
            <jsp:include page="leftNav.jsp" />
            <div class="right-section">
                <c:set var="title" value="Dashboard" scope="request"/>
                <jsp:include page="topNav.jsp" />

                <div class="form-wrapper">
                    <form action="review" method="post">
                        <div class="form-container">
                            <input type="text" name="fullName" value="${param.fullName}" placeholder="Enter fullName">

                            <input type="date" name="date" value="${param.date}">

                            <input type="submit" name="submit" value="search">
                            <input type="reset" name="reset" value="reset">

                            <input type="hidden" name="choose" value="listReview">
                        </div>
                    </form>
                </div>


                <div class="room-table-container">
                    <table class="room-table">
                        <thead>
                            <tr>
                                <th class="col-orderid">OrderID</th>
                                <th class="col-name">Full Name</th>
                                <th class="col-rating">Evaluate</th>
                                <th class="col-feedback">Feedback</th>
                                <th class="col-date">Date</th>
                            </tr>
                        </thead>
                        <tbody>

                            <c:forEach var="r" items="${requestScope.list}">
                                <tr>
                                    <td class="col-orderid">${r.bookingDetail.booking.bookingId}</td>
                                    <td class="col-name">${r.customerAccount.customer.fullName}</td>
                                    <td class="col-rating">
                                        <c:forEach var="i" begin="1" end="${r.rating}">
                                            <span class="star">&#9733;</span>
                                        </c:forEach>
                                    </td>
                                    <td class="col-feedback">
                                        <button type="button" class="viewFeedback" onclick="showFeedback('${r.feedBack}')">View</button>
                                    </td>
                                    <td class="col-date">${r.date}</td>
                                </tr>
                            </c:forEach>

                        </tbody>
                    </table>
                </div>
            </div>
        </div>
        <script>
            function showFeedback(feedback) {
                alert("Feedback: " + feedback);
            }
        </script>
    </body>
    <%--script for dashbord--%>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js"></script>
    <script src="${pageContext.request.contextPath}/Js/navDashboardJs.js"></script>
    <script src="${pageContext.request.contextPath}/Js/userProfileJs.js"></script>
    <%--write more in following--%>
</html>
