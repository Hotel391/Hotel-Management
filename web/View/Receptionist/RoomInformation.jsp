<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8" />
        <title>Thông Tin Đặt Phòng</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet" />
        <link rel="stylesheet" href="${pageContext.request.contextPath}/Css/navDashboardStyle.css" />
        <link rel="stylesheet" href="${pageContext.request.contextPath}/Css/dashboardStyle.css" />
        <link rel="stylesheet" href="${pageContext.request.contextPath}/Css/mainDashboardStyle.css" />
        <style>
            body {
                background: #f8f9fa;
            }
            .section-header {
                font-size: 1.5rem;
                font-weight: 600;
                color: #2c3e50;
                margin-bottom: 1rem;
            }
            .card-hotel {
                border: 2px solid #2c3e50;
                border-radius: 1rem;
                background: #ffffff;
                box-shadow: 0 4px 20px rgba(0,0,0,0.08);
            }
            .table-hotel th {
                background: #2c3e50;
                color: #ffffff;
            }
            .table-hotel tr:hover {
                background: #f0f0f0;
            }
            .price-summary h4 {
                font-size: 1.6rem;
                font-weight: 700;
                color: #c0392b;
            }
            .back-button {
                margin-bottom: 1rem;
            }
            .back-button a {
                display: flex;
                align-items: center;
                color: #2c3e50;
                text-decoration: none;
                font-weight: 500;
            }
            .back-button a:hover {
                color: #1a252f;
            }
            .back-button .arrow {
                margin-right: 0.5rem;
                font-size: 1.2rem;
            }
        </style>
    </head>
    <body>
        <div class="containerBox">
            <jsp:include page="leftNavReceptionist.jsp"/>
            <div class="right-section">
                <jsp:include page="topNavReceptionist.jsp"/>
                <div class="main-content p-4">

                    <c:if test="${not empty errorMessage}">
                        <div class="alert alert-danger alert-dismissible fade show" role="alert">
                            <strong>Lỗi:</strong> <c:out value="${errorMessage}"/>
                            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Đóng"></button>
                        </div>
                    </c:if>

                    <div class="back-button">
                        <form action="${pageContext.request.contextPath}/receptionist/roomInformation" method="post">
                            <input type="hidden" name="action" value="back">
                            <button type="submit" class="btn-back">
                                <i class="fas fa-arrow-left"></i> Quay lại
                            </button>
                        </form>
                    </div>

                    <div class="card card-hotel p-4 mb-4">
                        <div class="section-header">Thông tin đặt phòng</div>
                        <div class="row mb-3">
                            <div class="col-md-4"><strong>Ngày nhận phòng:</strong> <c:out value="${startDate}" /></div>
                            <div class="col-md-4"><strong>Ngày trả phòng:</strong> <c:out value="${endDate}" /></div>
                            <div class="col-md-4"><strong>Số đêm:</strong> <c:out value="${numberOfNights}" /></div>
                        </div>
                        <table class="table table-hotel table-bordered">
                            <thead>
                                <tr>
                                    <th>Số phòng</th>
                                    <th>Loại phòng</th>
                                    <th>Giá (VNĐ)</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="room" items="${roomDetails}">
                                    <tr>
                                        <td>${room.roomNumber}</td>
                                        <td>${room.typeName}</td>
                                        <td><fmt:formatNumber value="${room.price}" type="number" groupingUsed="true"/> đ</td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>

                    <form action="${pageContext.request.contextPath}/receptionist/roomInformation" method="post">
                        <c:forEach var="room" items="${roomDetails}">
                            <div class="card card-hotel p-4 mb-4">
                                <div class="section-header">Dịch vụ thêm cho phòng ${room.roomNumber}</div>

                                <!-- Dịch vụ đi kèm -->
                                <h5 class="mb-2">Dịch vụ đi kèm:</h5>
                                <table class="table table-hotel table-bordered mb-4">
                                    <thead>
                                        <tr>
                                            <th>Tên dịch vụ</th>
                                            <th>Giá (VNĐ)</th>
                                            <th>Số lượng</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach var="service" items="${room.includedServices}">
                                            <tr>
                                                <td>${service.service.serviceName}</td>
                                                <td>
                                                    <c:choose>
                                                        <c:when test="${service.service.price == 0}">Miễn phí</c:when>
                                                        <c:otherwise>
                                                            <fmt:formatNumber value="${service.service.price}" type="number" groupingUsed="true"/> đ
                                                        </c:otherwise>
                                                    </c:choose>
                                                </td>
                                                <td>${service.quantity}</td>
                                            </tr>
                                        </c:forEach>
                                    </tbody>
                                </table>

                                <!-- Dịch vụ tùy chọn -->
                                <h5 class="mb-2">Dịch vụ tùy chọn:</h5>
                                <table class="table table-hotel table-bordered">
                                    <thead>
                                        <tr>
                                            <th>Chọn</th>
                                            <th>Tên dịch vụ</th>
                                            <th>Giá (VNĐ)</th>
                                            <th>Số lượng</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach var="service" items="${room.optionalServices}">
                                            <tr>
                                                <td>
                                                    <input type="checkbox"
                                                           class="service-check"
                                                           name="serviceId_${room.roomNumber}"
                                                           value="${service.serviceId}"
                                                           data-price="${service.price}"
                                                           data-room="${room.roomNumber}"
                                                           data-service-id="${service.serviceId}">
                                                </td>
                                                <td>${service.serviceName}</td>
                                                <td>
                                                    <c:choose>
                                                        <c:when test="${service.price == 0}">Miễn phí</c:when>
                                                        <c:otherwise>
                                                            <fmt:formatNumber value="${service.price}" type="number" groupingUsed="true"/> đ
                                                        </c:otherwise>
                                                    </c:choose>
                                                </td>
                                                <td>
                                                    <input type="number"
                                                           min="1"
                                                           value="1"
                                                           name="quantity_${room.roomNumber}_${service.serviceId}"
                                                           class="form-control quantity-input"
                                                           style="width: 80px;"
                                                           disabled>
                                                </td>
                                            </tr>
                                        </c:forEach>
                                    </tbody>
                                </table>
                            </div>
                        </c:forEach>

                        <!-- Tổng chi phí -->
                        <div class="card card-hotel p-4 mb-4 price-summary">
                            <div class="section-header">Tổng cộng</div>
                            <p><strong>Chi phí phòng:</strong> <span id="basePriceDisplay">
                                    <fmt:formatNumber value="${totalPrice}" type="number" groupingUsed="true" /> đ
                                </span></p>
                            <p><strong>Chi phí dịch vụ:</strong> <span id="serviceCost">0 đ</span></p>
                            <h4><strong>Tổng thanh toán:</strong> <span id="totalWithServices">
                                    <fmt:formatNumber value="${totalPrice}" type="number" groupingUsed="true" /> đ
                                </span></h4>
                        </div>

                        <div class="text-center">
                            <button type="submit" class="btn btn-danger btn-lg px-5 py-2">
                                Tiến hành thanh toán
                            </button>
                        </div>
                    </form>
                </div>
            </div>
        </div>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
        <script src="${pageContext.request.contextPath}/Js/navDashboardJs.js"></script>
        <script src="${pageContext.request.contextPath}/Js/userProfileJs.js"></script>
        <script>
            const basePrice = ${totalPrice != null ? totalPrice : 0};
            const serviceCostElement = document.getElementById('serviceCost');
            const totalPriceElement = document.getElementById('totalWithServices');

            document.querySelectorAll('.service-check').forEach(cb => {
                const qtyInput = cb.closest('tr').querySelector('.quantity-input');
                qtyInput.disabled = !cb.checked;

                cb.addEventListener('change', function () {
                    qtyInput.disabled = !this.checked;
                    calculateTotal();
                });
            });

            document.querySelectorAll('.quantity-input').forEach(input => {
                input.addEventListener('input', function () {
                    if (this.value < 1) {
                        this.value = 1;
                    }
                    calculateTotal();
                });
            });

            function calculateTotal() {
                let optionalServiceCost = 0;
                document.querySelectorAll('.service-check:checked').forEach(cb => {
                    const price = parseFloat(cb.getAttribute('data-price'));
                    const qtyInput = cb.closest('tr').querySelector('.quantity-input');
                    const qty = parseInt(qtyInput.value) || 1;
                    if (price > 0) {
                        optionalServiceCost += price * qty;
                    }
                });
                serviceCostElement.innerText = optionalServiceCost.toLocaleString('vi-VN') + ' đ';
                const total = basePrice + optionalServiceCost;
                totalPriceElement.innerText = total.toLocaleString('vi-VN') + ' đ';
            }

            calculateTotal();
        </script>
    </body>
</html