<%-- 
    Document   : ViewRoom
    Created on : 31 thg 5, 2025, 15:50:28
    Author     : Tuan'sPC
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>View Room</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/Css/cssAdmin/ViewRoom.css">
    </head>
    <body>
        <div class="link-insert"><a href="room?choose=insertRoom">Insert new room</a></div>
        <div class="room-table-container">
            <table class="room-table">
                <h2>List room</h2>
                <thead>
                    <tr>
                        <th>Room number</th>
                        <th>Room type</th>
                        <th>Price</th>
                        <th>Service</th>
                        <th>Update</th>
                        <th>Delete</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="room" items="${requestScope.listR}">
                        <tr>
                            <td class="room-id">${room.roomNumber}</td>
                            <td>${room.typeRoom.typeName}</td>
                            <td>${room.typeRoom.price} VND</td>
                            <td>
                                <c:choose>
                                    <c:when test="${empty room.typeRoom.services}">
                                        No services. <br>
                                    </c:when>
                                    <c:otherwise>
                                        <c:forEach var="rns" items="${room.typeRoom.services}">
                                             - ${rns.service.serviceName} x ${rns.quantity} (${rns.service.price} VND)<br/>                                           
                                        </c:forEach>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td><a href="room?choose=updateRoom&roomNumber=${room.roomNumber}&typeRoomId=${room.typeRoom.typeId}">Update</a></td>
                            <td><a href="room?choose=deleteRoom&roomNumber=${room.roomNumber}" onclick="return confirm('Are you sure to delete?');">Delete</a></td>
                        </tr>
                    </c:forEach>

                </tbody>
            </table>
            </div>
    </body>
</html>
