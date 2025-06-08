<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Profile</title>
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
                <c:set var="title" value="Home Page" scope="request"/>
                <jsp:include page="topNav.jsp" />
                <div class="d-flex justify-content-center align-items-start mt-3" style="min-height: calc(100vh - 60px); padding-top: 10px;">
                    <div class="card shadow-sm text-dark" style="background-color: #ffffff; width: 100%; max-width: 700px;">

                        <div class="card-header bg-primary text-white">
                            <h4 class="mb-0">Customer Profile</h4>
                        </div>
                        <div class="card-body">
                            <dl class="row">
                                <dt class="col-sm-3">Username:</dt>
                                <dd class="col-sm-9">${customerAccount.username}</dd>

                                <dt class="col-sm-3">Full Name:</dt>
                                <dd class="col-sm-9">${customerAccount.customer.fullName}</dd>

                                <dt class="col-sm-3">Email:</dt>
                                <dd class="col-sm-9">${customerAccount.customer.email}</dd>

                                <dt class="col-sm-3">Phone Number:</dt>
                                <dd class="col-sm-9">${customerAccount.customer.phoneNumber}</dd>

                                <dt class="col-sm-3">Gender:</dt>
                                <dd class="col-sm-9">
                                    <c:choose>
                                        <c:when test="${customerAccount.customer.gender}">Male</c:when>
                                        <c:otherwise>Female</c:otherwise>
                                    </c:choose>
                                </dd>
                            </dl>

                            <!--back home,update profile-->
                            <div class="mt-4 d-flex gap-2">
                                <a href="${pageContext.request.contextPath}/customer/home" class="btn btn-secondary">
                                    <i class="bi bi-arrow-left-circle"></i> Back to Home
                                </a>

                                <a href="${pageContext.request.contextPath}/customer/customerProfile?service=update&username=${customerAccount.username}" class="btn btn-primary">
                                    <i class="bi bi-pencil-square"></i> Update Profile
                                </a>

                                <a href="${pageContext.request.contextPath}/customer/customerProfile?service=changePass&username=${customerAccount.username}" class="btn btn-warning">
                                    <i class="bi bi-key"></i> Change Password
                                </a>

                                <a href="${pageContext.request.contextPath}/customer/customerProfile?service=changeUserName&username=${customerAccount.username}" class="btn btn-primary">
                                    <i class="bi bi-pencil-square"></i> Change username
                                </a>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <%--script for dashbord--%>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js"></script>
        <script src="${pageContext.request.contextPath}/Js/navDashboardJs.js"></script>
        <script src="${pageContext.request.contextPath}/Js/userProfileJs.js"></script>
        <%--write more in following--%>
    </body>
</html>
