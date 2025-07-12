<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8"/>
        <meta name="viewport" content="width=device-width, initial-scale=1"/>
        <title>Tìm Phòng</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css" rel="stylesheet"/>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/Css/navDashboardStyle.css"/>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/Css/dashboardStyle.css"/>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/Css/mainDashboardStyle.css"/>

        <style>
            .room-row {
                transition: background-color 0.3s ease-in-out;
                position: relative;
            }

            .room-row .selected-icon {
                display: none;
                font-weight: bold;
                color: green;
                position: absolute;
                left: 5px;
                top: 50%;
                transform: translateY(-50%);
            }

            .room-row.table-warning .selected-icon {
                display: inline;
            }

            .room-row td:first-child {
                padding-left: 25px;
            }
        </style>
    </head>
    <body>
        <div class="containerBox">
            <jsp:include page="leftNavReceptionist.jsp"/>
            <div class="right-section">
                <jsp:include page="topNavReceptionist.jsp"/>
                <div class="main-content">
                    <c:if test="${not empty errorMessage}">
                        <div class="alert alert-danger alert-dismissible fade show" role="alert">
                            <strong>Thông báo:</strong> <c:out value="${errorMessage}"/>
                            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                        </div>
                    </c:if>

                    <c:if test="${empty availableRooms and not empty startDateSearch and not empty endDateSearch}">
                        <div class="alert alert-warning" role="alert">
                            Không có phòng nào khả dụng cho khoảng thời gian này.
                        </div>
                    </c:if>

                    <div class="container-fluid p-4">
                        <form id="searchForm" method="get" action="${pageContext.request.contextPath}/receptionist/searchRoom">
                            <div class="row mb-3 align-items-end">
                                <div class="col-md-2">
                                    <label>Ngày nhận phòng:</label>
                                    <input type="date" name="startDate" id="startDate" class="form-control" value="<c:out value='${startDateSearch}'/>" required/>
                                </div>
                                <div class="col-md-2">
                                    <label>Ngày trả phòng:</label>
                                    <input type="date" name="endDate" id="endDate" class="form-control" value="<c:out value='${endDateSearch}'/>" required/>
                                </div>
                                <div class="col-md-2">
                                    <label>Số người lớn:</label>
                                    <input type="number" name="adult" id="adult" class="form-control" value="${empty adultSearch ? 1 : adultSearch}" min="0" required/>
                                </div>
                                <div class="col-md-2">
                                    <label>Số trẻ em:</label>
                                    <input type="number" name="children" id="children" class="form-control" value="${empty childrenSearch ? 1 : childrenSearch}" min="0" required/>
                                </div>
                                <div class="col-md-2">
                                    <label>Loại phòng:</label>
                                    <select name="typeRoomId" class="form-select">
                                        <option value="" ${empty typeRoomIdSearch ? 'selected' : ''}>All Types</option>
                                        <c:forEach var="type" items="${typeRooms}">
                                            <option value="${type.typeId}" ${type.typeId == typeRoomIdSearch ? 'selected' : ''}>
                                                <c:out value="${type.typeName}"/>
                                            </option>
                                        </c:forEach>
                                    </select>
                                </div>
                                <div class="col-md-2">
                                    <button type="submit" class="btn btn-primary w-100">Tìm phòng</button>
                                </div>
                            </div>
                            <input type="hidden" name="page" value="${currentPage}"/>
                        </form>

                        <div id="selectedRoomList" class="mb-3">
                            <c:if test="${not empty selectedRooms}">
                                <div class="card p-2">
                                    <strong>Selected Rooms:</strong>
                                    <ul class="list-group list-group-flush" id="selectedList">
                                        <c:forEach var="roomNum" items="${selectedRooms}">
                                            <li class="list-group-item d-flex justify-content-between align-items-center">
                                                ${roomNum} - ${selectedRoomTypes[roomNum] != null ? selectedRoomTypes[roomNum] : 'Unknown'}
                                                <form method="post" action="${pageContext.request.contextPath}/receptionist/searchRoom" style="display:inline;">
                                                    <input type="hidden" name="removeRoomNumber" value="${roomNum}"/>
                                                    <input type="hidden" name="startDate" value="${startDateSearch}"/>
                                                    <input type="hidden" name="endDate" value="${endDateSearch}"/>
                                                    <input type="hidden" name="typeRoomId" value="${typeRoomIdSearch}"/>
                                                    <input type="hidden" name="adult" value="${adultSearch}"/>
                                                    <input type="hidden" name="children" value="${childrenSearch}"/>
                                                    <input type="hidden" name="page" value="${currentPage}"/>
                                                    <button type="submit" class="btn btn-sm btn-danger">Xóa</button>
                                                </form>
                                            </li>
                                        </c:forEach>
                                    </ul>
                                </div>
                            </c:if>
                        </div>

                        <form method="post" action="${pageContext.request.contextPath}/receptionist/searchRoom" id="multiBookForm">
                            <input type="hidden" name="startDate" value="${startDateSearch}"/>
                            <input type="hidden" name="endDate" value="${endDateSearch}"/>
                            <input type="hidden" name="typeRoomId" value="${typeRoomIdSearch}"/>
                            <input type="hidden" name="adult" value="${adultSearch}"/>
                            <input type="hidden" name="children" value="${childrenSearch}"/>
                            <input type="hidden" name="page" value="${currentPage}"/>
                            <input type="hidden" name="checkIn" value="true"/>

                            <table class="table">
                                <thead class="table-light">
                                    <tr>
                                        <th>Số phòng</th>
                                        <th>Loại phòng</th>
                                        <th>Giá phòng</th>
                                        <th>Xem phòng</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:if test="${not empty availableRooms}">
                                        <c:forEach var="room" items="${availableRooms}">
                                            <c:set var="roomNumStr" value="${room.roomNumber}"/>
                                            <tr class="room-row ${selectedRooms.contains(roomNumStr) ? 'table-warning' : ''}"
                                                onclick="toggleRoomSelectionRow('${roomNumStr}', this)"
                                                style="cursor:pointer;">
                                                <td>
                                                    <span class="selected-icon">✔</span>
                                                    ${room.roomNumber}
                                                </td>
                                                <td>${room.typeRoom.typeName}</td>
                                                <td>${room.typeRoom.price}</td>
                                                <td>
                                                    <button type="button" class="btn btn-sm btn-info"
                                                            data-bs-toggle="modal"
                                                            data-bs-target="#viewRoomModal_${room.roomNumber}"
                                                            onclick="event.stopPropagation();">
                                                        Chi tiết phòng
                                                    </button>
                                                </td>
                                            </tr>

                                            <!-- Modal -->
                                        <div class="modal fade" id="viewRoomModal_${room.roomNumber}" tabindex="-1"
                                             aria-labelledby="viewRoomModalLabel_${room.roomNumber}" aria-hidden="true">
                                            <div class="modal-dialog">
                                                <div class="modal-content">
                                                    <div class="modal-header">
                                                        <h5 class="modal-title" id="viewRoomModalLabel_${room.roomNumber}">
                                                            Room Details - ${room.roomNumber}</h5>
                                                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                                                    </div>
                                                    <div class="modal-body">
                                                        <p><strong>Room Number:</strong> ${room.roomNumber}</p>
                                                        <p><strong>Type:</strong> ${room.typeRoom.typeName}</p>
                                                        <p><strong>Price:</strong> ${room.typeRoom.price}</p>
                                                        <p><strong>Max Adults:</strong> ${room.typeRoom.maxAdult}</p>
                                                        <p><strong>Max Children:</strong> ${room.typeRoom.maxChildren}</p>
                                                        <p><strong>Description:</strong> ${room.typeRoom.description != null ? room.typeRoom.description : 'No description available.'}</p>
                                                    </div>
                                                    <div class="modal-footer">
                                                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Đóng</button>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </c:forEach>
                                </c:if>
                                <c:if test="${empty availableRooms}">
                                    <tr>
                                        <td colspan="4" class="text-center">Không có phòng nào được tìm thấy.</td>
                                    </tr>
                                </c:if>
                                </tbody>
                            </table>

                            <div class="text-end">
                                <button type="submit" class="btn btn-success" ${empty selectedRooms ? 'disabled' : ''}>Đặt phòng</button>
                            </div>
                        </form>

                        <form id="dynamicRoomForm" method="post" action="${pageContext.request.contextPath}/receptionist/searchRoom" style="display: none;">
                            <input type="hidden" name="addRoom"/>
                            <input type="hidden" name="removeRoomNumber"/>
                            <input type="hidden" name="startDate" value="${startDateSearch}"/>
                            <input type="hidden" name="endDate" value="${endDateSearch}"/>
                            <input type="hidden" name="typeRoomId" value="${typeRoomIdSearch}"/>
                            <input type="hidden" name="adult" value="${adultSearch}"/>
                            <input type="hidden" name="children" value="${childrenSearch}"/>
                            <input type="hidden" name="page" value="${currentPage}"/>
                        </form>

                        <nav>
                            <ul class="pagination">
                                <li class="page-item ${currentPage == 1 ? 'disabled' : ''}">
                                    <a class="page-link" href="?page=${currentPage - 1}&startDate=${startDateSearch}&endDate=${endDateSearch}&typeRoomId=${typeRoomIdSearch}&adult=${adultSearch}&children=${childrenSearch}">Previous</a>
                                </li>
                                <c:forEach var="i" begin="1" end="${totalPages}">
                                    <li class="page-item ${i == currentPage ? 'active' : ''}">
                                        <a class="page-link" href="?page=${i}&startDate=${startDateSearch}&endDate=${endDateSearch}&typeRoomId=${typeRoomIdSearch}&adult=${adultSearch}&children=${childrenSearch}">${i}</a>
                                    </li>
                                </c:forEach>
                                <li class="page-item ${currentPage == totalPages ? 'disabled' : ''}">
                                    <a class="page-link" href="?page=${currentPage + 1}&startDate=${startDateSearch}&endDate=${endDateSearch}&typeRoomId=${typeRoomIdSearch}&adult=${adultSearch}&children=${childrenSearch}">Next</a>
                                </li>
                            </ul>
                        </nav>
                    </div>
                </div>
            </div>
        </div>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
        <script src="${pageContext.request.contextPath}/Js/navDashboardJs.js"></script>
        <script src="${pageContext.request.contextPath}/Js/userProfileJs.js"></script>

        <script>
                                                                document.getElementById("searchForm").addEventListener("submit", function () {
                                                                    this.querySelector("input[name='page']").value = 1;
                                                                });

                                                                function toggleRoomSelectionRow(roomNumber, rowElement) {
                                                                    const form = document.getElementById("dynamicRoomForm");
                                                                    const isSelected = rowElement.classList.contains('table-warning');

                                                                    form.querySelector('[name="addRoom"]').value = "";
                                                                    form.querySelector('[name="removeRoomNumber"]').value = "";

                                                                    if (isSelected) {
                                                                        form.querySelector('[name="removeRoomNumber"]').value = roomNumber;
                                                                    } else {
                                                                        form.querySelector('[name="addRoom"]').value = roomNumber;
                                                                    }

                                                                    form.submit();
                                                                }

                                                                window.onload = function () {
                                                                    const today = new Date();
                                                                    const formattedToday = today.toISOString().split('T')[0];

                                                                    const startDate = document.getElementById("startDate");
                                                                    if (startDate && !startDate.value) {
                                                                        startDate.value = formattedToday;
                                                                    }
                                                                };

        </script>
    </body>
</html>