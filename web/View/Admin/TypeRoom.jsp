<%-- 
    Document   : TypeRoom
    Created on : May 31, 2025, 11:40:30 PM
    Author     : Hai Long
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.6/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-4Q6Gf2aSP4eDXB8Miphtr37CMZZQ5oXLH2yaXMJ2w8e2ZtHTl7GptT4jmndRuHDT" crossorigin="anonymous">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/Css/TypeRoom.css"/>
    </head>
    <body>
        <div class="containerBox">
            <jsp:include page="leftNav.jsp"/>
            <div class="right-section">
                <jsp:include page="topNav.jsp"/>
                <div class="main-content">
                    <div class="search-add">
                        <form action="${pageContext.request.contextPath}/admin/types">
                            <div class="input-group flex-nowrap">
                                <input type="text" 
                                       name="key" 
                                       value="${param.key != null ? param.key : requestScope.key}"
                                       class="form-control" 
                                       placeholder="Type Room Name" 
                                       aria-label="typeRoomName" 
                                       aria-describedby="addon-wrapping">
                                <button type="submit" class="btn btn-danger">Search</button>
                            </div>
                        </form>
                        <a href="url">Insert</a>
                    </div>
                    <div class="typeRoom-data">
                        <table id="example" class="content-info">
                            <thead>
                                <tr>
                                    <th>Type Room's Name</th>
                                    <th>Price</th>
                                    <th>Services</th>
                                    <th>Description</th>

                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="tr" items="${requestScope.typeRoom}">
                                    <tr>
                                        <td>
                                            <img class="typeroom-img" src="${pageContext.request.contextPath}/Image/DeluxeRoom/DeluxeRoom1.png" alt="${tr.typeName}"/>
                                            ${tr.typeName}</td>
                                        <td>${tr.price}</td>
                                        <td>${tr.typeId}</td>
                                        <td>
                                            <!-- Button trigger modal -->
                                            <button type="button" class="btn btn-danger" data-bs-toggle="modal" data-bs-target="#exampleModal_${tr.typeId}">
                                                View Description
                                            </button>

                                            <!-- Modal -->
                                            <div class="modal fade" id="exampleModal_${tr.typeId}" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
                                                <div class="modal-dialog">
                                                    <div class="modal-content">
                                                        <div class="modal-header">
                                                            <h5 class="modal-title" id="exampleModalLabel">${tr.typeName}</h5>
                                                            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                                                        </div>
                                                        <div class="modal-body">
                                                            ${tr.description}
                                                        </div>
                                                        <div class="modal-footer">
                                                            <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                                                            <button type="button" class="btn btn-danger">Save changes</button>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                        </td>
                                        <td><button type="button" class="btn btn-danger">Update</button></td>
                                        <td><button type="button" class="btn btn-danger">Delete</button></td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>
                    <nav aria-label="Pagination">
                        <ul class="pagination pagination-danger">
                            <c:choose>
                                <c:when test="${key != null && !key.isEmpty()}">
                                    <li class="page-item ${currentPage == 1 ? 'disabled' : ''}">
                                        <a href="?page=${currentPage - 1}&key=${key}" class="page-link">Previous</a>
                                    </li>
                                </c:when>
                                <c:otherwise>
                                    <li class="page-item ${currentPage == 1 ? 'disabled' : ''}">
                                        <a href="?page=${currentPage - 1}" class="page-link">Previous</a>
                                    </li>
                                </c:otherwise>
                            </c:choose>
                            <c:forEach var="i" begin="1" end="${endPage}">
                                <c:choose>
                                    <c:when test="${key != null && !key.isEmpty()}">
                                        <li class="page-item ${i == currentPage ? 'active' : ''}">
                                            <a class="page-link" href="?page=${i}&key=${key}">${i}</a>
                                        </li>

                                    </c:when>
                                    <c:otherwise>
                                        <li class="page-item ${i == currentPage ? 'active' : ''}">
                                            <a class="page-link" href="?page=${i}">${i}</a>
                                        </li>
                                    </c:otherwise>
                                </c:choose>

                            </c:forEach>
                            <c:choose>
                                <c:when test="${key != null && !key.isEmpty()}">

                                    <li class="page-item ${currentPage == endPage ? 'disabled' : ''}">
                                        <a href="?page=${currentPage + 1}&key=${key}" class="page-link">Next</a>
                                    </li>
                                </c:when>
                                <c:otherwise>
                                    <li class="page-item ${currentPage == endPage ? 'disabled' : ''}">
                                        <a href="?page=${currentPage + 1}" class="page-link">Next</a>
                                    </li>
                                </c:otherwise>
                            </c:choose>
                        </ul>
                    </nav>
                </div>
            </div>
        </div>

        <script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.9.2/dist/umd/popper.min.js" integrity="sha384-IQsoLXl5PILFhosVNubq5LC7Qb9DXgDA9i+tQ8Zj3iwWAwPtgFTxbJ8NT4GN1R8p" crossorigin="anonymous"></script>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.min.js" integrity="sha384-cVKIPhGWiC2Al4u+LWgxfKTRIcfu0JTxR+EQDz/bgldoEyl4H0zUF0QKbrJ0EcQF" crossorigin="anonymous"></script>


    </body>
</html>
