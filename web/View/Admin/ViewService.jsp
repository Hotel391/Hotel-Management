<%-- 
    Document   : ViewService
    Created on : 31 thg 5, 2025, 16:51:56
    Author     : Tuan'sPC
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>View service</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/Css/cssAdmin/ViewService.css">
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

                <div class="link-insert"><a href="service?choose=insertService">Insert new service</a></div>
                <div class="room-table-container">
                    <h2>List service</h2>
                    <table class="room-table">
                        <thead>
                            <tr>
                                <th>ServiceID</th>
                                <th>Service name</th>
                                <th>Price</th>
                                <th>Update</th>
                                <th>Delete</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="s" items="${requestScope.listS}">
                                <tr>
                                    <td>${s.serviceId}</td>
                                    <td>${s.serviceName}</td>
                                    <td>${s.price}</td>
                                    <td><a href="service?choose=updateService&serviceId=${s.serviceId}&serviceName=${s.serviceName}&price=${s.price}">Update</a></td>
                                    <td><a href="service?choose=deleteService&serviceId=${s.serviceId}" onclick="return confirm('Are you sure to delete?');">Delete</a></td>
                                </tr>
                            </c:forEach>

                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </body>
    <%--script for dashbord--%>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js"></script>
    <script src="${pageContext.request.contextPath}/Js/navDashboardJs.js"></script>
    <script src="${pageContext.request.contextPath}/Js/userProfileJs.js"></script>
    <%--write more in following--%>
</html>
