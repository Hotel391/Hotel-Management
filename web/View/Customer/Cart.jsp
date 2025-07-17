<%-- 
    Document   : DetailRoom
    Created on : Jul 1, 2025, 1:16:28 PM
    Author     : HieuTT
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="vi">

    <head>
        <meta charset="UTF-8" />
        <title>Giỏ hàng</title>
        <meta name="viewport" content="width=device-width, initial-scale=1" />
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.4.2/dist/css/bootstrap.min.css" rel="stylesheet" />
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.5/font/bootstrap-icons.css" />
        <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
        <link rel="stylesheet" href="Css/Customer/CartStyle.css"/>
        <style>
            .modal-body-scroll {
                max-height: 60vh;
                overflow-y: auto;
                padding-right: 15px;
            }

            .modal-footer.sticky-footer {
                position: sticky;
                bottom: 0;
                background-color: #fff;
                z-index: 100;
                border-top: 1px solid #dee2e6;
            }
        </style>
    </head>

    <body>
        <jsp:include page="Header.jsp" />
        <div class="container" style="padding: 0 0;">
            <c:set var="carts" value="${carts}" />
            <h2 class="mb-4">Phòng của quý khách đã chọn (<span id="item-count">${carts.size()}</span>)</h2>

            <div class="cart-wrapper">
                <!-- CART ITEMS -->
                <div class="cart-items" id="cart-list">
                    <c:if test="${empty carts}">
                        <div class="alert alert-info" role="alert">
                            Hiện tại giỏ hàng của bạn đang trống.
                        </div>
                    </c:if>
                    <c:forEach var="cart" items="${carts}">
                        <c:set var="isPayment" value="${cart.isPayment}" />
                        <div class="cart-item ${isPayment ? 'paid' : ''}" data-price="${cart.totalPrice}" data-cart-id="${cart.cartId}">
                            <div class="cart-top" 
                                 onclick="window.location.href = 'detailRoom?typeRoomId=${cart.room.typeRoom.typeId}&checkin=${cart.startDate}&checkout=${cart.endDate}&adults=${cart.adults}&children=${cart.children}'">
                                <img src="${pageContext.request.contextPath}/${cart.room.typeRoom.uriContextOfImages}${cart.room.typeRoom.images[0]}"
                                     class="room-image" alt="Rex Hotel">
                                <div class="room-info">
                                    <h5>${cart.room.typeRoom.typeName}</h5>
                                    <div class="rating">
                                        <c:set var="typeRoom" value="${cart.room.typeRoom}" />
                                        <c:set var="rating" value="${typeRoom.averageRating}" />
                                        <c:set var="fullStars" value="${rating - (rating mod 1)}" />
                                        <c:set var="halfStar" value="${(rating - fullStars) >= 0.5}" />
                                        <c:set var="emptyStars" value="${5 - fullStars - (halfStar ? 1 : 0)}" />

                                        <!-- Sao đầy -->
                                        <c:forEach begin="1" end="${fullStars}">
                                            <span class="fas fa-star text-warning"></span>
                                        </c:forEach>

                                        <!-- Sao nửa -->
                                        <c:if test="${halfStar}">
                                            <span class="fas fa-star-half-alt text-warning"></span>
                                        </c:if>

                                        <!-- Sao rỗng -->
                                        <c:forEach begin="1" end="${emptyStars}">
                                            <span class="far fa-star text-warning"></span>
                                        </c:forEach>

                                        <span>(
                                            <fmt:formatNumber value="${typeRoom.averageRating}" type="number" maxFractionDigits="2" />
                                            )</span>
                                    </div>
                                    <div class="review-count"><fmt:formatNumber value="${typeRoom.numberOfReviews}" type="number" /> nhận xét</div>
                                </div>
                                <c:if test="${!isPayment}">
                                    <button class="delete-btn" onclick="deleteCart(event, ${cart.cartId})">
                                        <i class="bi bi-trash"></i> Xóa
                                    </button>
                                </c:if>
                            </div>

                            <c:set var="isActive" value="${cart.isActive}" />

                            <div class="cart-bottom ${!isActive ? 'inactive' : ''}">
                                <div class="cart-bottom-left">
                                    <c:choose>
                                        <c:when test="${isPayment}">
                                            <span class="payment-badge">
                                                <i class="bi bi-check-circle-fill"></i> Đã thanh toán
                                            </span>
                                        </c:when>
                                        <c:otherwise>
                                            <input type="checkbox" class="room-checkbox" ${!isActive ? 'disabled' : ''} />
                                        </c:otherwise>
                                    </c:choose>
                                    <ul class="details-list">
                                        <fmt:formatDate value="${cart.startDate}" pattern="dd" var="startDay"/>
                                        <fmt:formatDate value="${cart.startDate}" pattern="M" var="startMonth"/>
                                        <fmt:formatDate value="${cart.startDate}" pattern="yyyy" var="startYear"/>
                                        <fmt:formatDate value="${cart.endDate}" pattern="dd" var="endDay"/>
                                        <fmt:formatDate value="${cart.endDate}" pattern="M" var="endMonth"/>
                                        <fmt:formatDate value="${cart.endDate}" pattern="yyyy" var="endYear"/>
                                        <li>
                                            <i class="bi bi-calendar-event"></i>
                                            ${startDay} tháng ${startMonth} năm ${startYear} — ${endDay} tháng ${endMonth} năm ${endYear}
                                        </li>
                                        <li><i class="bi bi-people"></i> 
                                            Khách: ${cart.adults} người lớn 
                                            <c:if test="${cart.children > 0}">
                                                , <c:out value="${cart.children}"/> trẻ em
                                            </c:if>
                                        </li>

                                        <li><a href="?action=${isPayment? 'viewDetails' : 'updateCart'}&cartId=${cart.cartId}" class="view-details"><i class="bi bi-eye"></i> Xem chi tiết</a></li>
                                    </ul>
                                </div>
                                <c:if test="${!isActive}">
                                    <div class="room-price">
                                        Phòng này đã không còn khả dụng
                                    </div>
                                </c:if>
                                <c:if test="${isActive}">
                                    <div class="room-price">
                                        <fmt:formatNumber value="${cart.totalPrice}" type="number" groupingUsed="true"/> ₫
                                    </div>
                                </c:if>
                            </div>
                        </div>
                    </c:forEach>
                </div>

                <form id="formCheckSpam" action="${pageContext.request.contextPath}/check" method="post">
                    <input type="hidden" name="cartIdCheck" value="">
                </form>
                <!-- CART SUMMARY -->
                <div class="cart-summary">
                    <h4>Tổng giá</h4>
                    <h4 class="text-danger" id="cart-total">0 ₫</h4>
                    <button id="checkout-btn" disabled>Tiếp theo</button>
                </div>
            </div>
        </div>
        <c:if test="${action eq 'viewDetails'}">
            <div class="modal fade show" id="detailModal" tabindex="-1" style="display:block; background-color: rgba(0,0,0,0.5);" aria-modal="true" role="dialog">
                <c:set var="cartDetail" value="${cartDetails}" />
                <div class="modal-dialog modal-lg">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h5 class="modal-title">Chi tiết đơn đặt phòng</h5>
                            <a href="cart" class="btn-close"></a>
                        </div>
                        <div class="modal-body">
                            <p><strong>Thời gian:</strong>
                                <input type="date" value="${cartDetail.startDate}" /> - 
                                <input type="date" value="${cartDetail.endDate}" />
                            </p>
                            <h6>Dịch vụ đi kèm:</h6>
                            <table class="table table-bordered">
                                <thead>
                                    <tr>
                                        <th>Tên dịch vụ</th>
                                        <th>Giá</th>
                                        <th>Số lượng</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="s" items="${cartDetail.cartServices}">
                                        <tr>
                                            <td><c:out value="${s.service.serviceName}" /></td>
                                            <td><fmt:formatNumber value="${s.service.price}" type="number" groupingUsed="true" /> ₫</td>
                                            <td><input type="number" value="${s.quantity}" /></td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </div>
                        <div class="modal-footer">
                            <a href="cart" class="btn btn-secondary">Đóng</a>
                        </div>
                    </div>
                </div>
            </div>
        </c:if>

        <c:if test="${action eq 'updateCart'}">
            <div class="modal fade show" id="detailModal" tabindex="-1" style="display:block; background-color: rgba(0,0,0,0.5);" aria-modal="true" role="dialog">
                <c:set var="cartDetail" value="${cartDetails}" />
                <div class="modal-dialog modal-lg">
                    <form>
                        <input type="hidden" name="action" value="saveUpdateCart" />
                        <div class="modal-content">
                            <div class="modal-header">
                                <h5 class="modal-title">Chi tiết đơn đặt phòng</h5>
                            </div>
                            <div class="modal-body modal-body-scroll">
                                <p><strong>Thời gian:</strong>
                                    <input name="checkin" type="date" value="${cartDetail.startDate}" min="<%= new java.text.SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date()) %>"/> - 
                                    <input name="checkout" type="date" value="${cartDetail.endDate}" min="<%= new java.text.SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date()) %>" />
                                </p>
                                <h6>Dịch vụ đi kèm:</h6>
                                <table class="table table-bordered">
                                    <thead>
                                        <tr>
                                            <th></th>
                                            <th>Tên dịch vụ</th>
                                            <th>Giá</th>
                                            <th>Số lượng</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:set var="serviceCannotDisable" value="${serviceCannotDisable}"/>
                                        <c:forEach var="s" items="${cartDetail.cartServices}">
                                            <tr>
                                                <td>
                                                    <input name="service_${s.service.serviceId}" type="checkbox" class="service-checkbox" checked 
                                                           <c:if test="${serviceCannotDisable[s.service.serviceId] != null}">
                                                               disabled 
                                                           </c:if>
                                                           />
                                                    <c:if test="${serviceCannotDisable[s.service.serviceId] != null}">
                                                        <input type="hidden" name="service_${s.service.serviceId}" value="on" />
                                                    </c:if>
                                                </td>
                                                <td><c:out value="${s.service.serviceName}" /></td>
                                                <td><fmt:formatNumber value="${s.service.price}" type="number" groupingUsed="true" /> ₫</td>
                                                <td>
                                                    <input type="number" name="quantity_${s.service.serviceId}" value="${s.quantity}" 
                                                           <c:if test="${s.service.price == 0 || s.service.serviceName eq 'Dịch vụ đưa đón'}">
                                                               readonly 
                                                           </c:if>
                                                           min="${serviceCannotDisable[s.service.serviceId] != null ? serviceCannotDisable[s.service.serviceId] : 0}"
                                                           />
                                                </td>
                                            </tr>
                                        </c:forEach>
                                        <c:forEach var="s" items="${otherServices}">
                                            <tr>
                                                <td>
                                                    <input name="oService_${s.serviceId}" type="checkbox" class="service-checkbox" />
                                                </td>
                                                <td><c:out value="${s.serviceName}" /></td>
                                                <td><fmt:formatNumber value="${s.price}" type="number" groupingUsed="true" /> ₫</td>
                                                <td>
                                                    <input type="number" name="oQuantity_${s.serviceId}" value="0" min="0" />
                                                </td>
                                            </tr>
                                        </c:forEach>
                                    </tbody>
                                </table>
                            </div>
                            <div class="modal-footer sticky-footer">
                                <button type="submit" class="btn btn-primary">Save</button>
                                <a href="cart" class="btn btn-secondary">Đóng</a>
                            </div>
                        </div>
                    </form>

                </div>
            </div>
        </c:if>

        <!-- JS -->
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.4.2/dist/js/bootstrap.bundle.min.js"></script>
        <script>
                                        const maxTimeSpan = ${maxTimeSpan};
                                        const maxCheckoutDateStr = '${maxCheckoutDate}';

                                            // === CẬP NHẬT CHECKIN - CHECKOUT ===
                                            const checkinInput = document.querySelector('input[name="checkin"]');
                                            const checkoutInput = document.querySelector('input[name="checkout"]');

                                            if (checkinInput && checkoutInput) {
                                                function updateCheckoutConstraints() {
                                                    if (!checkinInput.value)
                                                        return;

                                                    const checkinDate = new Date(checkinInput.value);
                                                    const minCheckoutDate = new Date(checkinDate);
                                                    minCheckoutDate.setDate(minCheckoutDate.getDate() + 1);

                                                    const maxCheckoutSpanDate = new Date(checkinDate);
                                                    maxCheckoutSpanDate.setDate(maxCheckoutSpanDate.getDate() + maxTimeSpan);

                                                    const maxCheckoutLimitDate = new Date(maxCheckoutDateStr);
                                                    const maxCheckoutDate = maxCheckoutSpanDate < maxCheckoutLimitDate ? maxCheckoutSpanDate : maxCheckoutLimitDate;

                                                    const minStr = minCheckoutDate.toISOString().split("T")[0];
                                                    const maxStr = maxCheckoutDate.toISOString().split("T")[0];

                                                    checkoutInput.setAttribute("min", minStr);
                                                    checkoutInput.setAttribute("max", maxStr);

                                                    const currentCheckout = new Date(checkoutInput.value);
                                                    if (!checkoutInput.value || currentCheckout < minCheckoutDate || currentCheckout > maxCheckoutDate) {
                                                        checkoutInput.value = minStr;
                                                    }
                                                }

                                                const today = new Date().toISOString().split("T")[0];
                                                checkinInput.setAttribute("min", today);
                                                checkinInput.setAttribute("max", maxCheckoutDateStr);
                                                updateCheckoutConstraints();
                                                checkinInput.addEventListener("change", updateCheckoutConstraints);
                                            }

                                            // === CẬP NHẬT TỔNG GIÁ ===
                                            const checkboxes = document.querySelectorAll('.room-checkbox');
                                            const cartItems = document.querySelectorAll('.cart-item');
                                            const totalElement = document.getElementById('cart-total');
                                            const checkoutBtn = document.getElementById('checkout-btn');
                                            const form = document.getElementById('formCheckSpam');
                                            const cartIdInput = form.querySelector('input[name="cartIdCheck"]');

                                            function updateTotalPrice() {
                                                const selected = document.querySelector('.room-checkbox:checked');

                                                if (selected) {
                                                    const item = selected.closest('.cart-item');
                                                    const price = parseInt(item.dataset.price);
                                                    totalElement.innerText = price.toLocaleString('vi-VN') + ' ₫';
                                                    const cartId = item.dataset.cartId;
                                                    checkoutBtn.disabled = false;
                                                    checkoutBtn.onclick = function () {
                                                        cartIdInput.value = cartId;
                                                        form.submit();
                                                    };
                                                } else {
                                                    totalElement.innerText = '0 ₫';
                                                    checkoutBtn.disabled = true;
                                                    checkoutBtn.onclick = null;
                                                }
                                            }

                                            checkboxes.forEach(cb => {
                                                cb.addEventListener('click', function (e) {
                                                    checkboxes.forEach(other => {
                                                        if (other !== this)
                                                            other.checked = false;
                                                    });
                                                    updateTotalPrice();
                                                    e.stopPropagation();
                                                });
                                            });

                                            document.querySelectorAll('.cart-bottom').forEach(bottom => {
                                                bottom.addEventListener('click', function () {
                                                    const thisCheckbox = this.querySelector('.room-checkbox');
                                                    if (!thisCheckbox.checked) {
                                                        checkboxes.forEach(cb => cb.checked = false);
                                                        thisCheckbox.checked = true;
                                                    } else {
                                                        thisCheckbox.checked = false;
                                                    }
                                                    updateTotalPrice();
                                                });
                                            });

                                            document.querySelectorAll('.delete-btn').forEach(btn => {
                                                btn.addEventListener('click', e => {
                                                    const card = e.target.closest('.cart-item');
                                                    card.remove();
                                                    updateTotalPrice();
                                                    e.stopPropagation();
                                                });
                                            });

                                            updateTotalPrice();

                                        function deleteCart(event, cartId) {
                                            event.stopPropagation();
                                            window.location.href = '?action=deleteCart&cartId=' + cartId;
                                        }
        </script>
    </body>

</html>