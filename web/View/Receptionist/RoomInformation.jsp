<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

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

                    <!-- Thông tin đặt phòng -->
                    <div class="card card-hotel p-4 mb-4">
                        <div class="section-header">Thông tin đặt phòng</div>
                        <div class="row mb-3">
                            <div class="col-md-4"><strong>Ngày nhận phòng:</strong> <c:out value="${startDate}" /></div>
                            <div class="col-md-4"><strong>Ngày trả phòng:</strong> <c:out value="${endDate}" /></div>
                            <c:set var="roomCount" value="0"/>
                            <c:forEach var="room" items="${roomNumbers}">
                            </c:forEach>


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
                                        <td><c:out value="${room.roomNumber}"/></td>
                                        <td><c:out value="${room.typeName}"/></td>
                                        <td><fmt:formatNumber value="${room.price}" type="number" groupingUsed="true"/> đ</td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>

                    <form action="${pageContext.request.contextPath}/receptionist/roomInformation" method="post">
                        <c:forEach var="roomNumber" items="${roomNumbers}">
                            <div class="card card-hotel p-4 mb-4">
                                <div class="section-header">Dịch vụ thêm cho phòng <c:out value="${roomNumber}"/></div>
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
                                        <c:forEach var="service" items="${services}">
                                            <tr>
                                                <td>
                                                    <input type="checkbox" class="service-check"
                                                           name="serviceId_${roomNumber}"
                                                           value="${service.serviceId}"
                                                           data-price="${service.price}"
                                                           data-room="${roomNumber}">
                                                </td>
                                                <td><c:out value="${service.serviceName}"/></td>
                                                <td><fmt:formatNumber value="${service.price}" type="number" groupingUsed="true"/> đ</td>
                                                <td>
                                                    <input type="number" min="1" value="1"
                                                           name="quantity_${roomNumber}_${service.serviceId}"
                                                           class="form-control quantity-input"
                                                           style="width: 80px;" disabled>
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
                cb.addEventListener('change', function () {
                    const quantityInput = this.closest('tr').querySelector('.quantity-input');
                    quantityInput.disabled = !this.checked;
                    calculateTotal();
                });
            });

            document.querySelectorAll('.quantity-input').forEach(input => {
                input.addEventListener('input', function () {
                    if (this.value < 1)
                        this.value = 1;
                    calculateTotal();
                });
            });

            function calculateTotal() {
                let serviceCost = 0;
                document.querySelectorAll('.service-check:checked').forEach(cb => {
                    const price = parseFloat(cb.getAttribute('data-price'));
                    const qtyInput = cb.closest('tr').querySelector('.quantity-input');
                    const qty = parseInt(qtyInput.value) || 1;
                    serviceCost += price * qty;
                });
                serviceCostElement.innerText = serviceCost.toLocaleString('vi-VN') + " đ";
                const total = basePrice + serviceCost;
                totalPriceElement.innerText = total.toLocaleString('vi-VN') + " đ";
            }
        </script>
    </body>
</html>
