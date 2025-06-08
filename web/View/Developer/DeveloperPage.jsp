<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Developer Page</title>
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
                <c:set var="title" value="Developer" scope="request"/>
                <jsp:include page="topNav.jsp" />

                <c:if test="${adminAccount.role.roleId == 0}">

                    <div style="margin: 20px">
                        <div class="d-flex justify-content-between align-items-center mb-4">
                            <h3 style="color: orange; margin: 0;">All Manager Accounts</h3>
                            <a href="developerPage?service=add" class="btn btn-primary">
                                Add New Account Manager <i class="bi bi-person-add"></i>
                            </a>
                        </div>

                        <div class="table-responsive">
                            <table class="table table-bordered table-striped table-hover align-middle">
                                <thead class="table-light">
                                    <tr>
                                        <!--                                    <th>Employee ID</th>-->
                                        <th>Username</th>
                                        <th>Password</th>
                                        <th>Registration Date</th>
                                        <th>Activate</th>
                                        <th>Role Name</th>
                                        <th>Delete</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="e" items="${requestScope.list}">
                                        <tr>
    <!--                                        <td>${e.employeeId}</td>-->
                                            <td>${e.username}</td>
                                            <td>${e.password}</td>
                                            <td>${e.registrationDate}</td>
                                            <td>
                                                <c:choose>
                                                    <c:when test="${e.activate == true}">
                                                        <i class="bi bi-check2-circle text-success fs-5"></i>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <i class="bi bi-x-circle text-danger fs-5"></i>
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>
                                            <td>${e.role.roleName}</td>
                                            <td>
                                                <a href="developerPage?service=deleteManager&employeeID=${e.employeeId}" 
                                                   onclick="return confirm('Are you sure to delete?');"
                                                   class="btn btn-sm btn-danger">
                                                    <i class="bi bi-trash-fill"></i>
                                                </a>
                                            </td>

                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </c:if>
            </div>
        </div>
    </body>
    <%--script for dashbord--%>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js"></script>
    <script src="${pageContext.request.contextPath}/Js/navDashboardJs.js"></script>
    <script src="${pageContext.request.contextPath}/Js/userProfileJs.js"></script>
    <%--write more in following--%>
</html>
