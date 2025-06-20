<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8" />
        <meta name="viewport" content="width=device-width, initial-scale=1" />
        <title>Search Room</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css" rel="stylesheet"
              integrity="sha384-EVSTQN3/azprG1Anm3QDgpJLIm9Nao0Yz1ztcQTwFspd3yD65VohhpuuCOmLASjC"
              crossorigin="anonymous">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/Css/navDashboardStyle.css" />
        <link rel="stylesheet" href="${pageContext.request.contextPath}/Css/dashboardStyle.css" />
        <link rel="stylesheet" href="${pageContext.request.contextPath}/Css/mainDashboardStyle.css" />
    </head>
    <body>
        <div class="containerBox">
            <jsp:include page="leftNavReceptionist.jsp"/>
            <div class="right-section">
                <jsp:include page="topNavReceptionist.jsp"/>
                <div class="main-content">

                    <c:if test="${not empty errorMessage}">
                        <div class="alert alert-danger alert-dismissible fade show" role="alert">
                            <strong>Error:</strong> <c:out value="${errorMessage}"/>
                            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                        </div>
                    </c:if>

                    <div class="container-fluid p-4">
                        <ul class="nav nav-tabs mb-3">
                            <li class="nav-item">
                                <a class="nav-link active" href="${pageContext.request.contextPath}/receptionist/searchRoom">Search Room</a>
                            </li>
                        </ul>

                        <div class="d-flex justify-content-between align-items-center mb-3">
                            <form method="get" action="${pageContext.request.contextPath}/receptionist/searchRoom">
                                <div class="row w-100">
                                    <div class="col-md-3">
                                        <label for="startDate">Start Date:</label>
                                        <input type="date" name="startDate" id="startDate" class="form-control" 
                                               value="<c:out value='${startDateSearch}' default='' />" />
                                    </div>
                                    <div class="col-md-3">
                                        <label for="endDate">End Date:</label>
                                        <input type="date" name="endDate" id="endDate" class="form-control" 
                                               value="<c:out value='${endDateSearch}' default='' />" />
                                    </div>
                                    <div class="col-md-4">
                                        <label for="typeRoomId">Type Room:</label>
                                        <select name="typeRoomId" id="typeRoomId" class="form-select">
                                            <option value="" ${empty typeRoomIdSearch ? 'selected' : ''}>All Types</option>
                                            <c:forEach var="type" items="${typeRooms}">
                                                <option value="${type.typeId}" ${type.typeId == typeRoomIdSearch ? 'selected' : ''}>
                                                    <c:out value="${type.typeName}"/>
                                                </option>
                                            </c:forEach>
                                        </select>
                                        <input type="hidden" name="typeRoomId" value="<c:out value='${typeRoomIdSearch}' default='' />" />
                                    </div>
                                    <div class="col-md-2">
                                        <button type="submit" class="btn btn-primary mt-4 w-100">Search</button>
                                    </div>
                                </div>
                            </form>
                        </div>

                        <table class="table align-middle">
                            <thead class="table-light">
                                <tr>
                                    <th scope="col">Room Number</th>
                                    <th scope="col">Type</th>
                                    <th scope="col">Price</th>
                                    <th scope="col" class="text-center">Actions</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="room" items="${availableRooms}">
                                    <tr>
                                        <td><c:out value="${room.roomNumber}" default="-"/></td>
                                        <td><c:out value="${room.typeRoom.typeName}" default="-"/></td>
                                        <td><c:out value="${room.typeRoom.price}" default="-"/></td>
                                        <td class="text-center">
                                            <!-- View Room Details Button -->
                                            <button class="btn btn-sm btn-outline-info me-1" data-bs-toggle="modal" data-bs-target="#viewRoomModal_${room.roomNumber}">
                                                <i class="bi bi-eye"></i> View Details
                                            </button>
                                            <!-- Book Room Button -->
                                            <a href="${pageContext.request.contextPath}/receptionist/searchRoom?checkIn=checkIn&startDate=<c:out value='${startDateSearch}'/>&endDate=<c:out value='${endDateSearch}'/>&roomNumber=<c:out value='${room.roomNumber}'/>&typeRoomId=<c:out value='${typeRoomIdSearch}'/>" class="btn btn-sm btn-primary" data-action="book-room">
                                                Book Room
                                            </a>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>

                        <!-- Modal for displaying room details -->
                        <c:forEach var="room" items="${availableRooms}">
                            <div class="modal fade" id="viewRoomModal_${room.roomNumber}" tabindex="-1" aria-labelledby="viewRoomModalLabel_${room.roomNumber}" aria-hidden="true">
                                <div class="modal-dialog">
                                    <div class="modal-content">
                                        <div class="modal-header">
                                            <h5 class="modal-title" id="viewRoomModalLabel_${room.roomNumber}">Room Details - <c:out value="${room.roomNumber}"/></h5>
                                            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                                        </div>
                                        <div class="modal-body">
                                            <p><strong>Room Number:</strong> <c:out value="${room.roomNumber}" default="-"/></p>
                                            <p><strong>Type:</strong> <c:out value="${room.typeRoom.typeName}" default="-"/></p>
                                            <p><strong>Price:</strong> <c:out value="${room.typeRoom.price}" default="-"/></p>
                                            <p><strong>Description:</strong> <c:out value="${room.typeRoom.description}" default="No description available." /></p>
                                        </div>
                                        <div class="modal-footer">
                                            <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </c:forEach>

                        <!-- Pagination -->
                        <nav aria-label="Pagination">
                            <ul class="pagination pagination-danger">
                                <li class="page-item ${currentPage == 1 ? 'disabled' : ''}">
                                    <a href="?page=${currentPage - 1}&startDate=<c:out value='${startDateSearch}'/>&endDate=<c:out value='${endDateSearch}'/>&typeRoomId=<c:out value='${typeRoomIdSearch}'/>" class="page-link">Previous</a>
                                </li>
                                <c:forEach var="i" begin="1" end="${totalPages}">
                                    <li class="page-item ${i == currentPage ? 'active' : ''}">
                                        <a class="page-link" href="?page=${i}&startDate=<c:out value='${startDateSearch}'/>&endDate=<c:out value='${endDateSearch}'/>&typeRoomId=<c:out value='${typeRoomIdSearch}'/>">${i}</a>
                                    </li>
                                </c:forEach>
                                <li class="page-item ${currentPage == totalPages ? 'disabled' : ''}">
                                    <a href="?page=${currentPage + 1}&startDate=<c:out value='${startDateSearch}'/>&endDate=<c:out value='${endDateSearch}'/>&typeRoomId=<c:out value='${typeRoomIdSearch}'/>" class="page-link">Next</a>
                                </li>
                            </ul>
                        </nav>
                    </div>
                </div>
            </div>

            <div class="modal fade" id="dateErrorModal" tabindex="-1" aria-labelledby="dateErrorModalLabel" aria-hidden="true">
                <div class="modal-dialog">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h5 class="modal-title" id="dateErrorModalLabel">Missing Dates</h5>
                            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                        </div>
                        <div class="modal-body">
                            Hãy nhập ngày và loại phòng bạn muốn ở.
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-primary" data-bs-dismiss="modal">OK</button>
                        </div>
                    </div>
                </div>
            </div>

            <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js"></script>
            <script src="${pageContext.request.contextPath}/Js/navDashboardJs.js"></script>
            <script src="${pageContext.request.contextPath}/Js/userProfileJs.js"></script>
            <script>
                document.querySelectorAll('.btn-primary[data-action="book-room"]').forEach(button => {
                    button.addEventListener('click', function (event) {
                        const startDate = document.getElementById('startDate').value;
                        const endDate = document.getElementById('endDate').value;
                        if (!startDate || !endDate) {
                            event.preventDefault();
                            const modal = new bootstrap.Modal(document.getElementById('dateErrorModal'));
                            modal.show();
                        }
                    });
                });

                window.onload = function() {
                    const today = new Date().toISOString().split('T')[0];  
                    document.getElementById('startDate').value = today;
                };
            </script>
    </body>
</html>