<%-- 
    Document   : UpdateRoom
    Created on : 31 thg 5, 2025, 20:52:38
    Author     : Tuan'sPC
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Update Room</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/Css/cssAdmin/styleIU.css">
    </head>
    <body>
        <div class="container">
            <h2>Update Room</h2>
            <form action="AdminRoom?choose=updateRoom" method="Post">
                <table>
                    <tr>
                        <td>Room number</td>
                        <td><input type="text" name="roomNumber" value="${requestScope.roomNumber}" readonly></td>
                    </tr>
                    <tr>
                        <td>Room Type</td>
                        <td>
                            <select name="typeRoomId" required>
                                <c:forEach var="type" items="${requestScope.typeRoom}">
                                    <option value="${type.typeId}" 
                                            <c:if test="${type.typeId == requestScope.typeRoomId}">selected</c:if>>
                                        ${type.typeId} - ${type.typeName}
                                    </option>
                                </c:forEach>
                            </select>
                        </td>
                    </tr>
                </table>
                <div class="buttons">
                    <input type="submit" name="submit" value="Update Room"/>
                    <input type="reset" name="reset" value="Reset"/>
                    <input type="hidden" name="choose" value="updateRoom"/>
                </div>
            </form>


        </div>
    </body>
</html>
