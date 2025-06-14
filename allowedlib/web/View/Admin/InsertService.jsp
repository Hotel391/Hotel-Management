<%-- 
    Document   : InsertService
    Created on : 31 thg 5, 2025, 20:43:02
    Author     : Tuan'sPC
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Insert new service</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/Css/cssAdmin/styleIU.css">
    </head>
    <body>
        <form action="service?choose=insertService" method="post">
            <div class="container">
                <h2>Insert New Service</h2>
                <table>
                    <tr>
                        <td>Service Name</td>
                        <td><input type="text" name="serviceName" value="${serviceName}" required=""></td>                  

                    </tr>
                    <tr>
                        <td>Price</td>
                        <td><input type="text" name="priceService"  value="${priceService}" required=""></td>
                    </tr>
                </table>

                <c:if test="${not empty requestScope.nameError}">
                    <div style="color: red;">${requestScope.nameError}</div>
                </c:if>    
                <c:if test="${not empty requestScope.priceError}">
                    <div style="color: red;">${requestScope.priceError}</div>
                </c:if>

                <div class="buttons">
                    <input type="submit" name="submit" value="Insert Service"/>
                    <input type="reset" name="reset" value="Reset"/>
                    <input type="hidden" name="choose" value="insertService"/>
                </div>
            </div>
        </form>
    </body>
</html>
