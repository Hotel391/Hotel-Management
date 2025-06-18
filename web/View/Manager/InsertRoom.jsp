<%-- 
    Document   : InsertRoom
    Created on : 31 thg 5, 2025, 21:31:46
    Author     : Tuan'sPC
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Insert Room</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/Css/cssAdmin/styleIU.css">
    </head>
    <body>
        <form action="room?choose=insertRoom" method="post">
            <div class="container">
                <h2>Insert New Room</h2>
                <table>
                    <tr>
                        <td>Room Number</td>
                        <td><input type="text" name="roomNumber" value="${requestScope.roomNumber}" required=""></td>                  
                    </tr>
                    <tr>
                        <td>Room Type</td>
                        <td>
                            <select name="typeRoomId" required>
                                <c:forEach var="type" items="${requestScope.typeRoom}">
                                    <option value="${type.typeId}">
                                        ${type.typeId} - ${type.typeName}
                                    </option>
                                </c:forEach>
                            </select>
                        </td>
                    </tr>
                </table>
                <c:if test="${not empty requestScope.error}">
                    <div style="color: red;">${requestScope.error}</div>
                </c:if>
                <div class="buttons">
                    <input type="submit" name="submit" value="Insert Room"/>
                    <input type="reset" name="reset" value="Reset"/>
                    <input type="hidden" name="choose" value="insertRoom"/>
                </div>
            </div>
        </form>
    </body>
</html>
