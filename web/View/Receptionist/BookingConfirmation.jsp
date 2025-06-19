<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <title>Booking Confirmation</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-EVSTQN3/azprG1Anm3QDgpJLIm9Nao0Yz1ztcQTwFspd3yD65VohhpuuCOmLASjC"
          crossorigin="anonymous">
    <style>
        .card-header {
            background-color: #f8f9fa;
            font-weight: bold;
        }
        .main-content {
            padding: 20px;
        }
        .services-table th, .services-table td {
            vertical-align: middle;
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
                <h3 class="mb-4">Booking Confirmation</h3>

                <c:if test="${not empty errorMessage}">
                    <div class="alert alert-danger">${errorMessage}</div>
                </c:if>

                <c:choose>
                    <c:when test="${empty sessionScope.booking || empty sessionScope.bookingDetails}">
                        <div class="alert alert-danger">No booking information found.</div>
                    </c:when>
                    <c:otherwise>
                        <!-- Booking Details -->
                        <div class="card shadow-sm">
                            <div class="card-header">Booking Details</div>
                            <div class="card-body">
                                <div class="row">
                                    <div class="col-md-6">
                                        <strong>Status:</strong>
                                        <p><c:out value="${sessionScope.booking.status}" default="PENDING"/></p>
                                    </div>
                                    <div class="col-md-6">
                                        <strong>Total Price:</strong>
                                        <p><c:out value="${sessionScope.booking.totalPrice}"/></p>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <!-- Booking Room Details -->
                        <c:forEach var="bookingDetail" items="${sessionScope.bookingDetails}">
                            <div class="card shadow-sm mt-4">
                                <div class="card-header">Room Details</div>
                                <div class="card-body">
                                    <div class="row">
                                        <div class="col-md-3">
                                            <strong>Room Number:</strong>
                                            <p><c:out value="${bookingDetail.room.roomNumber}"/></p>
                                        </div>
                                        <div class="col-md-3">
                                            <strong>Room Type:</strong>
                                            <p><c:out value="${bookingDetail.room.typeRoom.typeName}"/></p>
                                        </div>
                                        <div class="col-md-3">
                                            <strong>Start Date:</strong>
                                            <p><c:out value="${bookingDetail.startDate}"/></p>
                                        </div>
                                        <div class="col-md-3">
                                            <strong>End Date:</strong>
                                            <p><c:out value="${bookingDetail.endDate}"/></p>
                                        </div>
                                    </div>

                                    <!-- Selected Services -->
                                    <h5 class="mt-3">Selected Services</h5>
                                    <c:set var="detailServices" value="${sessionScope.detailServices}"/>

                                    <c:choose>
                                        <c:when test="${not empty detailServices}">
                                            <table class="table services-table">
                                                <thead>
                                                    <tr>
                                                        <th>Service Name</th>
                                                        <th>Price</th>
                                                        <th>Quantity</th>
                                                        <th>Total</th>
                                                    </tr>
                                                </thead>
                                                <tbody>
                                                    <c:forEach var="detailService" items="${detailServices}">
                                                        <tr>
                                                            <td><c:out value="${detailService.service.serviceName}"/></td>
                                                            <td><c:out value="${detailService.service.price}"/></td>
                                                            <td><c:out value="${detailService.quantity}"/></td>
                                                            <td><c:out value="${detailService.service.price * detailService.quantity}"/></td>
                                                        </tr>
                                                    </c:forEach>
                                                </tbody>
                                            </table>
                                        </c:when>
                                        <c:otherwise>
                                            <p class="text-muted">No services selected for this room.</p>
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                            </div>
                        </c:forEach>

                        <!-- Customer and Payment Form -->
                        <div class="card shadow-sm mt-4">
                            <div class="card-header">Customer and Payment Information</div>
                            <div class="card-body">
                                <form id="finalizeBookingForm" method="post" action="${pageContext.request.contextPath}/receptionist/finalizeBooking">
                                    <div class="mb-3">
                                        <label for="customerName" class="form-label">Customer Name</label>
                                        <input type="text" class="form-control" id="customerName" name="customerName" required>
                                    </div>
                                    <div class="mb-3">
                                        <label for="customerId" class="form-label">Customer ID</label>
                                        <input type="text" class="form-control" id="customerId" name="customerId" required>
                                    </div>
                                    <div class="mb-3">
                                        <label for="paymentMethod" class="form-label">Payment Method</label>
                                        <select class="form-select" id="paymentMethod" name="paymentMethodId" required>
                                            <option value="">Select Payment Method</option>
                                            <c:forEach var="paymentMethod" items="${sessionScope.paymentMethods}">
                                                <option value="${paymentMethod.paymentMethodId}"><c:out value="${paymentMethod.paymentMethodName}"/></option>
                                            </c:forEach>
                                        </select>
                                    </div>
                                    <div class="text-end">
                                        <a href="${pageContext.request.contextPath}/receptionist/roomInfomation" class="btn btn-secondary">Back</a>
                                        <button type="button" class="btn btn-success" data-bs-toggle="modal" data-bs-target="#finalizeBookingModal">Confirm Booking</button>
                                    </div>
                                </form>
                            </div>
                        </div>

                        <!-- Finalize Booking Modal -->
                        <div class="modal fade" id="finalizeBookingModal" tabindex="-1" aria-labelledby="finalizeBookingModalLabel" aria-hidden="true">
                            <div class="modal-dialog">
                                <div class="modal-content">
                                    <div class="modal-header">
                                        <h5 class="modal-title" id="finalizeBookingModalLabel">Confirm Booking</h5>
                                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                                    </div>
                                    <div class="modal-body">
                                        <p>Are you sure you want to finalize this booking?</p>
                                        <p><strong>Total Price:</strong> <c:out value="${sessionScope.booking.totalPrice}"/></p>
                                        <p class="error-message" id="modalErrorMessage" style="display: none;"></p>
                                    </div>
                                    <div class="modal-footer">
                                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                                        <button type="button" class="btn btn-success" onclick="submitFinalizeForm()">Confirm</button>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        function submitFinalizeForm() {
            const customerName = document.getElementById('customerName').value;
            const customerId = document.getElementById('customerId').value;
            const paymentMethod = document.getElementById('paymentMethod').value;
            const modalErrorMessage = document.getElementById('modalErrorMessage');

            modalErrorMessage.style.display = 'none';

            if (!customerName || !customerId || !paymentMethod) {
                modalErrorMessage.innerText = 'Please fill in all customer and payment details.';
                modalErrorMessage.style.display = 'block';
                return;
            }

            document.getElementById('finalizeBookingForm').submit();
        }
    </script>
</body>
</html>
