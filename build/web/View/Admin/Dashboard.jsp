<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        <style>
            .containerBox{
                display: flex;
                height: 100vh;
                overflow: hidden;
            }
            .right-section{
                flex: 1;
                display: flex;
                flex-direction: column;
                overflow: hidden;
            }
            .main-content {
                flex: 1;
                overflow-y: auto;
                padding: 20px;
                background-color: #f9f9f9;
                color: black;
            }
        </style>
    </head>
    <body>
        <div class="containerBox">
            <jsp:include page="leftNav.jsp"/>
            <div class="right-section">
                <jsp:include page="topNav.jsp"/>
                <div class="main-content">
                    <table border="1">
                        <thead>
                            <tr>
                                <th>Employee ID</th>
                                <th>Username</th>
                                <th>Password</th>
                                <th>Full Name</th>
                                <th>Address</th>
                                <th>Phone Number</th>
                                <th>Email</th>
                                <th>Gender</th>
                                <th>CCCD</th>
                                <th>Date of Birth</th>
                                <th>Registration Date</th>
                                <th>Activate</th>
                                <th>Role ID</th>
                                <th>Role Name</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="e" items="${listEmployee}">
                                <tr>
                                    <td>${e.employeeId}</td>
                                    <td>${e.username}</td>
                                    <td>${e.password}</td>
                                    <td>${e.fullName}</td>
                                    <td>${e.address}</td>
                                    <td>${e.phoneNumber}</td>
                                    <td>${e.email}</td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${e.gender}">Male</c:when>
                                            <c:otherwise>Female</c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td>${e.CCCD}</td>
                                    <td>${e.dateOfBirth}</td>
                                    <td>${e.registrationDate}</td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${e.activate}">Active</c:when>
                                            <c:otherwise>Inactive</c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td>${e.role.roleId}</td>
                                    <td>${e.role.roleName}</td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </body>
</html>
