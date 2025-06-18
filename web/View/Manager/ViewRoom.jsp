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
                                <a class="nav-link active" href="${pageContext.request.contextPath}/manager/room">Management Room</a>
                            </li>
                        </ul>

                        <!--thông báo thành công-->
                        <c:if test="${param.success == 'true'}">
                            <div id="roomSuccessAlert" class="alert alert-success alert-dismissible fade show text-center mx-auto mt-3" role="alert" style="width: fit-content;">
                                <c:choose>
                                    <c:when test="${param.action == 'add'}">Thêm phòng thành công!</c:when>
                                    <c:when test="${param.action == 'update'}">Cập nhật phòng thành công!</c:when>
                                    <c:when test="${param.action == 'delete'}">Xóa phòng thành công!</c:when>
                                    <c:otherwise>Thao tác thành công!</c:otherwise>
                                </c:choose>
                                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                            </div>
                        </c:if>

                        <div class="d-flex justify-content-between align-items-center mb-3">

                            <!--form add new room-->
                            <div class="d-flex gap-2">
                                <button class="btn btn-success" data-bs-toggle="modal" 
                                        data-bs-target="#addRoomModal">+ Add New room</button>
                            </div>

                            <!--form search-->
                            <form method="get" action="${pageContext.request.contextPath}/manager/room?choose=search" class="d-flex gap-2">
                                <input type="number" name="roomNumberSearch"" 
                                       class="form-control search-input" placeholder="Room number" min="0" />

                                <select id="roomTypeSelect" name="typeRoomIdSearch" class="form-select">
                                    <option value="">-- All room types --</option> <!-- All rooms -->
                                    <c:forEach var="type" items="${requestScope.typeRoom}">
                                        <option value="${type.typeId}"
                                                <c:if test="${param.typeRoomId == type.typeId}">selected</c:if>>
                                            ${type.typeId} - ${type.typeName}
                                        </option>
                                    </c:forEach>
                                </select>

                                <button type="submit" class="btn btn-primary">Filter</button>
                                <a class="btn btn-secondary" href="${pageContext.request.contextPath}/manager/room">Reset</a>
                                <input type="hidden" name="choose" value="search">
                            </form>
                        </div>

                        <!--table list room-->
                        <div class="table-container">
                            <table class="table align-middle bg-white">
                                <thead class="table-light">
                                    <tr>
                                        <th scope="col">Room number</th>
                                        <th scope="col">Room type</th>
                                        <th scope="col">Price</th>
                                        <th scope="col">Service</th>
                                        <th scope="col">Is Active</th>
                                        <th scope="col">Action</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:if test="${empty requestScope.listR}">
                                        <tr>
                                            <td colspan="6" class="text-center">Không tìm thấy phòng.</td>
                                        </tr>
                                    </c:if>
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
                                            <td>
                                                <span class="${room.isActive ? 'bg-success text-white px-2 py-1 rounded' : 'bg-danger text-white px-2 py-1 rounded'}">
                                                    ${room.isActive ? 'Phòng đang mở' : 'Phòng đang đóng'}
                                                </span>
                                            </td>


                                            <td><button class="btn btn-sm btn-outline-primary"
                                                        data-bs-toggle="modal"
                                                        data-bs-target="#updateRoomModal"
                                                        data-room-number="${room.roomNumber}"
                                                        data-type-room-id="${room.typeRoom.typeId}"
                                                        data-is-active="${room.isActive}">
                                                    <i class="bi bi-pencil"></i> </button>


                                                <!--                                                <button type="button"
                                                                                                        class="btn btn-sm btn-danger"
                                                                                                        data-bs-toggle="modal"
                                                                                                        data-bs-target="#deleteRoomModal"
                                                                                                        data-room-number="${room.roomNumber}">
                                                                                                    <i class="bi bi-trash"></i>
                                                                                                </button>-->
                                            </td>
                                        </tr>
                                    </c:forEach>

                                </tbody>
                            </table>
                        </div>
                        <!-- Nút phân trang -->
                        <div class="pagination-container mt-3 text-center">
                            <c:if test="${totalPages > 1}">
                                <nav>
                                    <ul class="pagination justify-content-center">
                                        <c:forEach begin="1" end="${totalPages}" var="i">
                                            <li class="page-item ${i == currentPage ? 'active' : ''}">
                                                <form action="${pageContext.request.contextPath}/manager/room" method="get" style="display:inline;">
                                                    <input type="hidden" name="choose" value="search"/>
                                                    <input type="hidden" name="page" value="${i}"/>
                                                    <c:if test="${not empty param.roomNumberSearch}">
                                                        <input type="hidden" name="roomNumberSearch" value="${param.roomNumberSearch}"/>
                                                    </c:if>
                                                    <c:if test="${not empty param.typeRoomIdSearch}">
                                                        <input type="hidden" name="typeRoomIdSearch" value="${param.typeRoomIdSearch}"/>
                                                    </c:if>
                                                    <button type="submit" class="page-link">${i}</button>
                                                </form>
                                            </li>
                                        </c:forEach>

                                    </ul>
                                </nav>
                            </c:if>
                        </div>

                    </div>


                    <!-- Add Room Modal -->
                    <div class="modal fade" id="addRoomModal" tabindex="-1" aria-labelledby="addRoomModalLabel" aria-hidden="true">
                        <div class="modal-dialog">
                            <div class="modal-content">
                                <form id="addRoleForm" method="post" action="${pageContext.request.contextPath}/manager/room?choose=insertRoom" novalidate>
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
                                        <input type="hidden" name="page" value="${currentPage}" />
                                        <input type="hidden" name="roomNumberSearch" value="${param.roomNumberSearch}" />
                                        <input type="hidden" name="typeRoomIdSearch" value="${param.typeRoomIdSearch}" />

                                    </div>

                                </form>
                            </div>
                        </div>

                    </div>

                    <!--update room-->
                    <div class="modal fade" id="updateRoomModal" tabindex="-1" aria-labelledby="updateRoomModalLabel" aria-hidden="true">
                        <div class="modal-dialog">
                            <div class="modal-content">

                                <form action="${pageContext.request.contextPath}/manager/room?choose=updateRoom" method="post">
                                    <div class="modal-header">
                                        <h5 class="modal-title" id="addRoleModalLabel">Chỉnh sửa phòng</h5>
                                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                                    </div>
                                    <div class="modal-body">
                                        <!-- Room Number -->
                                        <div class="md-3">
                                            <label for="roomNumber" class="form-label">Số phòng</label>
                                            <input type="text" class="form-control" id="roomNumber" name="roomNumber" readonly>

                                        </div>

                                        <!-- Is Active -->
                                        <div class="mt-3">
                                            <label for="isActive" class="form-label">Trạng thái phòng</label>
                                            <select id="isActive" name="isActive" class="form-select" required>
                                                <option value="true">Mở phòng</option>
                                                <option value="false">Đóng phòng</option>
                                            </select>
                                        </div>


                                        <!-- Room Type -->
                                        <div class="md-3">
                                            <label for="typeRoomId" class="form-label">Kiểu phòng</label>
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
                                        <input type="hidden" name="page" value="${currentPage}" />
                                        <input type="hidden" name="roomNumberSearch" value="${param.roomNumberSearch}" />
                                        <input type="hidden" name="typeRoomIdSearch" value="${param.typeRoomIdSearch}" />
                                    </div>
                                </form>

                            </div>
                        </div>
                    </div>



                    <!-- Delete room Modal -->
                    <!--                    <div class="modal fade" id="deleteRoomModal" tabindex="-1" aria-labelledby="deleteRoomModalLabel" aria-hidden="true">
                                            <div class="modal-dialog">
                                                <div class="modal-content">
                                                    <form method="get" action="${pageContext.request.contextPath}/manager/room?choose=deleteRoom">
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
                                                            <input type="hidden" name="page" value="${currentPage}" />
                                                            <input type="hidden" name="roomNumberSearch" value="${param.roomNumberSearch}" />
                                                            <input type="hidden" name="typeRoomIdSearch" value="${param.typeRoomIdSearch}" />
                                                        </div>
                                                    </form>
                                                </div>
                                            </div>
                                        </div>-->
                </div>
            </div>

        </div>
        <script>
            document.addEventListener('DOMContentLoaded', function () {

                //THÔNG BÁO THÀNH CÔNG
                const alertBox = document.getElementById("roomSuccessAlert");
                if (alertBox) {
                    setTimeout(() => {
                        alertBox.classList.remove("show");
                        alertBox.classList.add("fade");
                        setTimeout(() => alertBox.remove(), 500);

                        // Xoá tham số URL
                        const url = new URL(window.location.href);
                        url.searchParams.delete("success");
                        url.searchParams.delete("action");
                        window.history.replaceState({}, document.title, url.toString());
                    }, 3000);
                }

                // UPDATE ROOM MODAL
                const updateRoomModal = document.getElementById('updateRoomModal');
                if (updateRoomModal) {
                    updateRoomModal.addEventListener('show.bs.modal', function (event) {
                        const button = event.relatedTarget;
                        const roomNumber = button.getAttribute('data-room-number');
                        const typeRoomId = button.getAttribute('data-type-room-id');
                        const isActive = button.getAttribute('data-is-active');

                        const inputRoomNumber = document.getElementById('roomNumber');
                        const selectTypeRoomId = document.getElementById('typeRoomId');
                        const selectIsActive = document.getElementById('isActive');

                        if (inputRoomNumber)
                            inputRoomNumber.value = roomNumber;

                        if (selectTypeRoomId) {
                            const options = selectTypeRoomId.options;
                            for (let i = 0; i < options.length; i++) {
                                options[i].selected = options[i].value === typeRoomId;
                            }
                        }
                        if (selectIsActive) {
                            if (isActive === 'true' || isActive === '1') {
                                selectIsActive.value = 'true';
                            } else {
                                selectIsActive.value = 'false';
                            }
                        }
                    });
                }

                // DELETE ROOM MODAL
//                const deleteRoomModal = document.getElementById('deleteRoomModal');
//                if (deleteRoomModal) {
//                    deleteRoomModal.addEventListener('show.bs.modal', function (event) {
//                        const button = event.relatedTarget;
//                        const roomNumber = button.getAttribute('data-room-number');
//
//                        const roomNumberDisplay = document.getElementById('roomNumberDelete');
//                        const roomNumberInput = document.getElementById('roomNumberDeleteInput');
//
//                        if (roomNumberDisplay)
//                            roomNumberDisplay.textContent = roomNumber;
//                        if (roomNumberInput)
//                            roomNumberInput.value = roomNumber;
//                    });
//                }


                // ADD ROOM MODAL
                const addRoomModalEl = document.getElementById("addRoomModal");
                if (addRoomModalEl) {
                    // Khi modal bị đóng (ấn nút X hoặc click nền)
                    addRoomModalEl.addEventListener('hidden.bs.modal', function () {
                        // 1. Xóa các thông báo lỗi (theo style)
                        const errorEls = addRoomModalEl.querySelectorAll("div[style='color: red;']");
                        errorEls.forEach(el => el.remove());

                        // 2. Xóa dữ liệu input qua ID
                        const roomNumberInput = document.getElementById("newRoomNumber");
                        if (roomNumberInput)
                            roomNumberInput.value = "";

                        // 3. Reset dropdown về option đầu tiên (nếu muốn)
                        const roomTypeSelect = document.getElementById("roomTypeSelect");
                        if (roomTypeSelect)
                            roomTypeSelect.selectedIndex = 0;
                    });

                    // Nếu có lỗi từ server thì hiển thị lại modal (chỉ khi server render ra requestScope.error)
                    const shouldShowModal = ${not empty requestScope.error};
                    if (shouldShowModal) {
                        const modal = new bootstrap.Modal(addRoomModalEl);
                        modal.show();
                    }
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
