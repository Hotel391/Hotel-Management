<%-- 
    
    Author     : SONNAM
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8" />
        <title>Room Information</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet" />
        <link rel="stylesheet" href="${pageContext.request.contextPath}/Css/navDashboardStyle.css" />
        <link rel="stylesheet" href="${pageContext.request.contextPath}/Css/dashboardStyle.css" />
        <link rel="stylesheet" href="${pageContext.request.contextPath}/Css/mainDashboardStyle.css" />
    </head>
    <body>
        <div class="containerBox">
            <jsp:include page="leftNavReceptionist.jsp"/>
            <div class="right-section">
                <jsp:include page="topNavReceptionist.jsp"/>
                <div class="main-content p-4">

                    <h2 class="mb-4">Room Booking Information</h2>
                    <div class="card shadow-sm p-4 mb-4">
                        <p><strong>Room Number:</strong> <c:out value="${roomNumber}" /></p>
                        <p><strong>Start Date:</strong> <c:out value="${startDate}" /></p>
                        <p><strong>End Date:</strong> <c:out value="${endDate}" /></p>
                        <p><strong>Total Room Price:</strong> <span id="basePrice"><fmt:formatNumber 
                                    value="${totalPrice}" type="number" groupingUsed="true" /></span> VND</p>
                    </div>

                    <h4 class="mb-3">Select Additional Services</h4>
                    <form action="${pageContext.request.contextPath}/receptionist/roomInformation" method="post">
                        <table class="table table-bordered">
                            <thead class="table-light">
                                <tr>
                                    <th>Select</th>
                                    <th>Service Name</th>
                                    <th>Price (VND)</th>
                                    <th>Quantity</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="service" items="${sessionScope.services}">
                                    <tr>
                                        <td>
                                            <input type="checkbox" class="service-check"
                                                   name="serviceId" value="${service.serviceId}"
                                                   data-price="${service.price}">
                                        </td>
                                        <td>${service.serviceName}</td>
                                        <td>${service.price}</td>
                                        <td>
                                            <input type="number" min="1" value="1"
                                                   name="quantity"
                                                   class="form-control quantity-input"
                                                   style="width: 80px;" disabled>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>

                        <div class="mt-4">
                            <h5>Total Price with Services: 
                                <span id="totalWithServices">
                                    <fmt:formatNumber value="${totalPrice}" type="number" groupingUsed="true" />
                                </span> VND
                            </h5>

                        </div>

                        <div class="mt-4 text-end">
                            <button type="submit" class="btn btn-primary btn-lg">
                                Proceed to Checkout
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
            const basePrice = parseFloat(document.getElementById('basePrice').innerText.replace(/\./g, '')) || 0;
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
                let total = basePrice;
                document.querySelectorAll('.service-check:checked').forEach(cb => {
                    const price = parseFloat(cb.getAttribute('data-price'));
                    const qty = parseInt(cb.closest('tr').querySelector('.quantity-input').value) || 1;
                    total += price * qty;
                });
                totalPriceElement.innerText = total.toLocaleString('vi-VN');
            }
        </script>

    </body>
</html>
