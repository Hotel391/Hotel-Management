<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Checkout - Thanh toán</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css" rel="stylesheet" />
</head>
<body>
    <div class="container mt-4">
        <h2 class="text-center">Thông tin đặt phòng</h2>

        <!-- Hiển thị ngày check-in và check-out -->
        <div class="mb-3">
            <p><strong>Ngày checkin:</strong> ${sessionScope.startDate}</p>
            <p><strong>Ngày checkout:</strong> ${sessionScope.endDate}</p>
        </div>

        <c:forEach var="dt" items="${sessionScope.roomTypeMap}">
                                                    <div class="modal-body">
                                                        <h3>Số phòng: ${dt.key} - ${dt.value}</h3>
                                                        <div class="service-room">
                                                            <h5>Các dịch vụ đi kèm</h5>
                                                            <c:forEach var="sr" items="${sessionScope.roomServicesMap}">
                                                                <c:if test="${sr.key == dt.key}">
                                                                    <c:forEach var="sv" items="${sr.value}">
                                                                        <p>${sv.service.serviceName} - ${sv.quantity}</p>
                                                                    </c:forEach>
                                                                </c:if>
                                                            </c:forEach>
                                                        </div>
                                                    </div>
                                                </c:forEach>

        <!-- Hiển thị tổng tiền cần thanh toán -->
        <div class="mb-3">
            <h4>Tổng tiền cần thanh toán:</h4>
            <p><fmt:formatNumber value="${sessionScope.totalPrice}" type="number" groupingUsed="true"/> VNĐ</p>
        </div>

        <!-- Các nút thanh toán hoặc quay lại -->
        <div class="d-flex justify-content-between">
            <a href="${pageContext.request.contextPath}/payment" class="btn btn-success">Thanh toán</a>
            <a href="${pageContext.request.contextPath}/receptionist/searchRoom" class="btn btn-secondary">Quay lại</a>
        </div>
    </div>

    <!-- Bootstrap JS và Popper.js -->
    <script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.9.2/dist/umd/popper.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.min.js"></script>
</body>
</html>
