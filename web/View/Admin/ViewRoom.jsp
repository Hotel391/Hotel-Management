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
                <c:set var="title" value="Dashboard" scope="request"/>
                <jsp:include page="topNav.jsp" />

                <div class="main-content">
                    <div class="container-fluid p-4">
                        <ul class="nav nav-tabs mb-3">
                            <li class="nav-item">
                                <a class="nav-link active" href="#">Management Room</a>
                            </li>
                        </ul>
                        <div class="d-flex justify-content-between align-items-center mb-3">
                            <div class="d-flex gap-2">
                                <button class="btn btn-success" data-bs-toggle="modal" data-bs-target="#addRoomModal">+ Add New room</button>
                            </div>
                        </div>
                        <div class="table-container">
                            <table class="table align-middle">
                                <thead class="table-light">
                                    <tr>
                                        <th scope="col">Room number</th>
                                        <th scope="col">Room type</th>
                                        <th scope="col">Price</th>
                                        <th scope="col">Service</th>
                                        <th scope="col">Update</th>
                                        <th scope="col">Delete</th>
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
                                            <td><button class="btn btn-sm btn-outline-primary"
                                                        data-bs-toggle="modal"
                                                        data-bs-target="#updateRoomModal"
                                                        data-room-number="${room.roomNumber}"
                                                        data-type-room-id="${room.typeRoom.typeId}">
                                                    <i class="bi bi-pencil"></i> </button>
                                            </td>
                                            <td>
                                                <button type="button"
                                                        class="btn btn-danger"
                                                        data-bs-toggle="modal"
                                                        data-bs-target="#deleteRoomModal"
                                                        data-room-number="${room.roomNumber}">
                                                    <i class="bi bi-trash"></i>
                                                </button>
                                            </td>
                                        </tr>
                                    </c:forEach>

                                </tbody>
                            </table>
                        </div>
                    </div>


                    <!-- Add Room Modal -->
                    <div class="modal fade" id="addRoomModal" tabindex="-1" aria-labelledby="addRoomModalLabel" aria-hidden="true">
                        <div class="modal-dialog">
                            <div class="modal-content">
                                <form id="addRoleForm" method="post" action="${pageContext.request.contextPath}/admin/room?choose=insertRoom" novalidate>
                                    <div class="modal-header">
                                        <h5 class="modal-title" id="addRoleModalLabel">Add New Room</h5>
                                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                                    </div>

                                    <div class="modal-body">
                                        <div class="mb-3">
                                            <label for="roomNumberer" class="form-label">Room Number</label>
                                            <input type="text" id="newRoomNumber" name="roomNumber" class="form-control" value="${requestScope.roomNumber}" required="">
                                        </div>
                                        <div class="mb-3">
                                            <label for="roomTypeSelect" class="form-label">Room Type</label>
                                            <select id="roomTypeSelect" name="typeRoomId" class="form-select" required>
                                                <c:forEach var="type" items="${requestScope.typeRoom}">
                                                    <option value="${type.typeId}">
                                                        ${type.typeId} - ${type.typeName}
                                                    </option>
                                                </c:forEach>
                                            </select>
                                        </div>
                                        <c:if test="${not empty requestScope.error}">
                                            <div style="color: red;">${requestScope.error}</div>
                                        </c:if>
                                    </div>
                                    <div class="modal-footer">
                                        <input type="submit" name="submit" class="btn btn-success" value="Add Room"/>
                                        <input type="reset" name="reset" value="Reset"/>
                                        <input type="hidden" name="choose" value="insertRoom"/>
                                    </div>

                                </form>
                            </div>
                        </div>
                    </div>

                    <!--update room-->
                    <div class="modal fade" id="updateRoomModal" tabindex="-1" aria-labelledby="updateRoomModalLabel" aria-hidden="true">
                        <div class="modal-dialog">
                            <div class="modal-content">

                                <form action="${pageContext.request.contextPath}/admin/room?choose=updateRoom" method="post">
                                    <div class="modal-header">
                                        <h5 class="modal-title" id="addRoleModalLabel">Updtate Room</h5>
                                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                                    </div>
                                    <div class="modal-body">
                                        <!-- Room Number -->
                                        <div class="md-3">
                                            <label for="roomNumber" class="form-label">Room Number</label>
                                            <input type="text" class="form-control" id="roomNumber" name="roomNumber" readonly>

                                        </div>

                                        <!-- Room Type -->
                                        <div class="md-3">
                                            <label for="typeRoomId" class="form-label">Room Type</label>
                                            <select id="typeRoomId" name="typeRoomId" class="form-select" required>
                                                <c:forEach var="type" items="${requestScope.typeRoom}">
                                                    <option value="${type.typeId}">${type.typeId} - ${type.typeName}</option>
                                                </c:forEach>
                                            </select>
                                        </div>
                                    </div>

                                    <!-- Buttons -->
                                    <div class="modal-footer">
                                        <input type="submit" name="submit" class="btn btn-success" value="Update Room"/>
                                        <input type="hidden" name="choose" value="updateRoom"/>
                                    </div>
                                </form>

                            </div>
                        </div>
                    </div>



                    <!-- Delete room Modal -->
                    <div class="modal fade" id="deleteRoomModal" tabindex="-1" aria-labelledby="deleteRoomModalLabel" aria-hidden="true">
                        <div class="modal-dialog">
                            <div class="modal-content">
                                <form method="get" action="${pageContext.request.contextPath}/admin/room?choose=deleteRoom">
                                    <div class="modal-header">
                                        <h5 class="modal-title">Confirm Delete</h5>
                                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                                    </div>

                                    <div class="modal-body">
                                        Are you sure you want to delete this room?
                                        <br/>
                                        Room Number: <strong><span id="roomNumberDelete"></span></strong>
                                        <input type="hidden" id="roomNumberDeleteInput" name="roomNumber"/>
                                    </div>

                                    <div class="modal-footer">
                                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                                        <button type="submit" class="btn btn-danger">Delete</button>
                                        <input type="hidden" name="choose" value="deleteRoom"/>
                                    </div>
                                </form>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

        </div>
        <script>
            document.addEventListener('DOMContentLoaded', function () {
                // UPDATE ROOM MODAL
                const updateRoomModal = document.getElementById('updateRoomModal');
                if (updateRoomModal) {
                    updateRoomModal.addEventListener('show.bs.modal', function (event) {
                        const button = event.relatedTarget;
                        const roomNumber = button.getAttribute('data-room-number');
                        const typeRoomId = button.getAttribute('data-type-room-id');

                        const inputRoomNumber = document.getElementById('roomNumber');
                        const selectTypeRoomId = document.getElementById('typeRoomId');

                        if (inputRoomNumber)
                            inputRoomNumber.value = roomNumber;

                        if (selectTypeRoomId) {
                            const options = selectTypeRoomId.options;
                            for (let i = 0; i < options.length; i++) {
                                options[i].selected = options[i].value === typeRoomId;
                            }
                        }
                    });
                }

                // DELETE ROOM MODAL
                const deleteRoomModal = document.getElementById('deleteRoomModal');
                if (deleteRoomModal) {
                    deleteRoomModal.addEventListener('show.bs.modal', function (event) {
                        const button = event.relatedTarget;
                        const roomNumber = button.getAttribute('data-room-number');

                        const roomNumberDisplay = document.getElementById('roomNumberDelete');
                        const roomNumberInput = document.getElementById('roomNumberDeleteInput');

                        if (roomNumberDisplay)
                            roomNumberDisplay.textContent = roomNumber;
                        if (roomNumberInput)
                            roomNumberInput.value = roomNumber;
                    });
                }
            });
        </script>



    </body>
    <%--script for dashbord--%>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js"></script>
    <script src="${pageContext.request.contextPath}/Js/navDashboardJs.js"></script>
    <script src="${pageContext.request.contextPath}/Js/userProfileJs.js"></script>
    <%--write more in following--%>
</html>
