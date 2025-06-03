<%-- 
    Document   : UpdateService
    Created on : 31 thg 5, 2025, 20:33:21
    Author     : Tuan'sPC
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Update service</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/Css/cssAdmin/styleIU.css">
    </head>
    <body>
        <form action="service?choose=updateService" method="post">
            <div class="container">
                <h2>Update Service</h2>
                <table>
                    <tr>
                        <td>ServiceID</td>
                        <td><input type="text" name="serviceId" value="${requestScope.serviceId}" readonly></td>
                    </tr>
                    <tr>
                        <td>Service name</td>
                        <td><input type="text" name="serviceName" value="${requestScope.serviceName}" required=""></td>
                    </tr>
                    <tr>
                        <td>Price</td>
                        <td><input type="text" name="price" value="${requestScope.price}" required=""></td>
                    </tr>
                </table>
                <c:if test="${not empty requestScope.error}">
                    <div style="color: red;">${requestScope.error}</div>
                </c:if>
                <div class="buttons">
                    <input type="submit" name="submit" value="Update Service"/>
                    <input type="reset" name="reset" value="Reset"/>
                    <input type="hidden" name="choose" value="updateService"/>
                </div>
            </div>
        </form>
    </body>
</html>
