<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <title>Book Room</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-EVSTQN3/azprG1Anm3QDgpJLIm9Nao0Yz1ztcQTwFspd3yD65VohhpuuCOmLASjC"
          crossorigin="anonymous">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/Css/navDashboardStyle.css" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/Css/dashboardStyle.css" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/Css/mainDashboardStyle.css" />
    <style>
        .card-header {
            background-color: #f8f9fa;
            font-weight: bold;
        }
        .btn-confirm {
            width: 200px;
        }
        .main-content {
            padding: 20px;
        }
        .services-table th, .services-table td {
            vertical-align: middle;
        }
        .confirm-button-container {
            text-align: right;
        }
        .quantity-input {
            width: 80px;
        }
        .error-message {
            color: red;
            font-size: 0.9em;
        }
    </style>
</head>
<body>
    <div class="containerBox">
        <jsp:include page="leftNavReceptionist.jsp"/>
        <div class="right-section">
            <jsp:include page="topNavReceptionist.jsp"/>
            <div class="main-content">
                <h3 class="mb-4">Room Information</h3>

                <c:if test="${not empty errorMessage}">
                    <div class="alert alert-danger">${errorMessage}</div>
                </c:if>

                <div class="container-fluid p-0">
                    <c:set var="startDate" value="${param.startDate}" />
                    <c:set var="endDate" value="${param.endDate}" />
                    <c:set var="roomNumber" value="${param.roomNumber}" />
                    <c:set var="typeRoomId" value="${param.typeRoomId}" />

                    <!-- Room Information Card -->
                    <div class="card mb-4 shadow-sm">
                        <div class="card-header">Selected Room Information</div>
                        <div class="card-body">
                            <div class="row">
                                <div class="col-md-3">
                                    <strong>Room Number:</strong>
                                    <p class="mb-0"><c:out value="${roomNumber}"/></p>
                                </div>
                                <div class="col-md-3">
                                    <strong>Room Type:</strong>
                                    <p class="mb-0"><c:out value="${room.typeRoom.typeName}" default="-"/></p>
                                </div>
                                <div class="col-md-3">
                                    <strong>Start Date:</strong>
                                    <p class="mb-0"><c:out value="${startDate}"/></p>
                                </div>
                                <div class="col-md-3">
                                    <strong>End Date:</strong>
                                    <p class="mb-0"><c:out value="${endDate}"/></p>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="card shadow-sm">
                        <div class="card-header">Available Services</div>
                        <div class="card-body">
                            <form id="bookingForm" method="post" action="${pageContext.request.contextPath}/receptionist/confirmBooking">
                                <div class="form-group mb-4">
                                    <c:choose>
                                        <c:when test="${not empty roomServices}">
                                            <table class="table services-table">
                                                <thead>
                                                    <tr>
                                                        <th>Service</th>
                                                        <th>Price</th>
                                                        <th>Quantity</th>
                                                        <th>Select</th>
                                                    </tr>
                                                </thead>
                                                <tbody>
                                                    <c:forEach var="roomService" items="${roomServices}">
                                                        <c:if test="${roomService.service.price > 0}">
                                                            <tr>
                                                                <td><c:out value="${roomService.service.serviceName}"/></td>
                                                                <td><c:out value="${roomService.service.price}"/></td>
                                                                <td>
                                                                    <input type="number" class="form-control quantity-input"
                                                                           name="serviceQuantities[${roomService.service.serviceId}]"
                                                                           id="quantity${roomService.service.serviceId}"                                                                      
                                                                           min="0" step="1"
                                                                           data-price="${roomService.service.price}">
                                                                </td>
                                                                <td>
                                                                    <input class="form-check-input" type="checkbox"
                                                                           name="selectedServices"
                                                                           id="service${roomService.service.serviceId}"
                                                                           value="${roomService.service.serviceId}"
                                                                           data-service-name="<c:out value='${roomService.service.serviceName}'/>"
                                                                           data-service-price="${roomService.service.price}"
                                                                           onchange="updateTotalPrice()">
                                                                </td>
                                                            </tr>
                                                        </c:if>
                                                    </c:forEach>
                                                </tbody>
                                            </table>
                                        </c:when>
                                        <c:otherwise>
                                            <p class="text-muted">No additional services available for this room.</p>
                                        </c:otherwise>
                                    </c:choose>
                                </div>

                                <!-- Hidden inputs to pass data to the server -->
                                <input type="hidden" name="startDate" value="<c:out value='${startDate}'/>" />
                                <input type="hidden" name="endDate" value="<c:out value='${endDate}'/>" />
                                <input type="hidden" name="roomNumber" value="<c:out value='${roomNumber}'/>" />
                                <input type="hidden" name="typeRoomId" value="<c:out value='${typeRoomId}'/>" />

                                <!-- Display Total Price -->
                                <div class="row">
                                    <div class="col-md-6">
                                        <strong>Total Price:</strong>
                                        <p class="mb-0" id="estimatedTotalPrice">0</p>
                                    </div>
                                </div>

                                <!-- Confirm Booking Button -->
                                <div class="confirm-button-container">
                                    <button type="submit" class="btn btn-success btn-confirm">Confirm Booking</button>
                                </div>
                            </form>
                        </div>
                    </div>

                </div>
            </div>
        </div>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js"></script>
        <script src="${pageContext.request.contextPath}/Js/navDashboardJs.js"></script>
        <script src="${pageContext.request.contextPath}/Js/userProfileJs.js"></script>

        <script>
            function updateTotalPrice() {
                let totalPrice = 0;
                const roomPrice = ${room.typeRoom.price != null ? room.typeRoom.price : 0};
                const startDate = new Date('${startDate}');
                const endDate = new Date('${endDate}');
                const days = (endDate - startDate) / (1000 * 60 * 60 * 24);
                if (days > 0) {
                    totalPrice += roomPrice * days;
                }

                const checkedServices = document.querySelectorAll('input[name="selectedServices"]:checked');
                checkedServices.forEach(service => {
                    const serviceId = service.id.replace('service', '');
                    const price = parseFloat(service.getAttribute('data-service-price')) || 0;
                    if (price > 0) {  
                        const quantityInput = document.getElementById('quantity' + serviceId);
                        const quantity = parseInt(quantityInput.value) || 0;
                        totalPrice += price * quantity;
                    }
                });

                document.getElementById('estimatedTotalPrice').innerText = totalPrice;
            }

        </script>
    </body>
</html>
