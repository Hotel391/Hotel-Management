<%-- 
    Document   : DetailRoom
    Created on : Jul 1, 2025, 1:16:28 PM
    Author     : HieuTT
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="vi">

    <head>
        <meta charset="UTF-8" />
        <title>Giỏ hàng</title>
        <meta name="viewport" content="width=device-width, initial-scale=1" />
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.4.2/dist/css/bootstrap.min.css" rel="stylesheet" />
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.5/font/bootstrap-icons.css" />
        <link rel="stylesheet" href="Css/Customer/CartStyle.css"/>
    </head>

    <body>
        <div class="container" style="padding: 0 140px;">
            <h2 class="mb-4">Phòng của quý khách đã chọn (<span id="item-count">2</span>)</h2>

            <div class="cart-wrapper">
                <!-- CART ITEMS -->
                <div class="cart-items" id="cart-list">

                    <!-- Cart Item 1 -->
                    <div class="cart-item" data-price="3147026">
                        <div class="cart-top">
                            <img src="https://pix8.agoda.net/hotelImages/10989/-1/e81a72f0cd6e3a51bdb06194290826ef.jpg"
                                 class="room-image" alt="Rex Hotel">
                            <div class="room-info">
                                <h5>Phòng Cao Cấp</h5>
                                <div class="rating">
                                    <i class="bi bi-star-fill"></i>
                                    <i class="bi bi-star-fill"></i>
                                    <i class="bi bi-star-fill"></i>
                                    <i class="bi bi-star-fill"></i>
                                    <i class="bi bi-star-fill"></i>
                                </div>
                                <div class="review-count">8.283 nhận xét</div>
                            </div>
                            <button class="delete-btn"><i class="bi bi-trash"></i> Xóa</button>
                        </div>

                        <div class="cart-bottom">
                            <div class="cart-bottom-left">
                                <input type="checkbox" class="room-checkbox" />
                                <ul class="details-list">
                                    <li><i class="bi bi-calendar-event"></i> 10/7/2025 — 11/7/2025</li>
                                    <li><i class="bi bi-people"></i> Khách: 2 người lớn</li>
                                    <li><a href="#" class="view-details"><i class="bi bi-eye"></i> Xem chi tiết</a></li>
                                </ul>
                            </div>

                            <div class="room-price">3.147.026 ₫</div>
                        </div>
                    </div>

                    <!-- Cart Item 2 -->
                    <div class="cart-item" data-price="3702458">
                        <div class="cart-top">
                            <img src="https://pix8.agoda.net/hotelImages/10989/-1/e81a72f0cd6e3a51bdb06194290826ef.jpg"
                                 class="room-image" alt="Rex Hotel">
                            <div class="room-info">
                                <h5>Phòng Governor Suite</h5>
                                <div class="rating">
                                    <i class="bi bi-star-fill"></i>
                                    <i class="bi bi-star-fill"></i>
                                    <i class="bi bi-star-fill"></i>
                                    <i class="bi bi-star-fill"></i>
                                    <i class="bi bi-star-fill"></i>
                                </div>
                                <div class="review-count">8.283 nhận xét</div>
                            </div>
                            <button class="delete-btn"><i class="bi bi-trash"></i> Xóa</button>
                        </div>

                        <div class="cart-bottom">
                            <div class="cart-bottom-left">
                                <input type="checkbox" class="room-checkbox" />
                                <ul class="details-list">
                                    <li><i class="bi bi-calendar-event"></i> 10/7/2025 — 11/7/2025</li>
                                    <li><i class="bi bi-people"></i> Khách: 2 người lớn</li>
                                    <li><a href="#" class="view-details"><i class="bi bi-eye"></i> Xem chi tiết</a></li>
                                </ul>
                            </div>

                            <div class="room-price">3.702.458 ₫</div>
                        </div>
                    </div>
                </div>

                <!-- CART SUMMARY -->
                <div class="cart-summary">
                    <h4>Tổng giá</h4>
                    <h4 class="text-danger" id="cart-total">0 ₫</h4>
                    <button id="checkout-btn" disabled>Tiếp theo</button>
                </div>
            </div>
        </div>

        <!-- JS -->
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.4.2/dist/js/bootstrap.bundle.min.js"></script>
        <script>
            const checkboxes = document.querySelectorAll('.room-checkbox');
            const cartItems = document.querySelectorAll('.cart-item');
            const totalElement = document.getElementById('cart-total');
            const checkoutBtn = document.getElementById('checkout-btn');

            function updateTotalPrice() {
                const selected = document.querySelector('.room-checkbox:checked');

                if (selected) {
                    const item = selected.closest('.cart-item');
                    const price = parseInt(item.dataset.price);
                    totalElement.innerText = price.toLocaleString('vi-VN') + ' ₫';
                    checkoutBtn.disabled = false;
                } else {
                    totalElement.innerText = '0 ₫';
                    checkoutBtn.disabled = true;
                }
            }

            // Cập nhật trạng thái khi người dùng tick checkbox
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

            // Bấm vào phần nội dung cũng chọn được
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

            // Xóa item và cập nhật tổng giá
            document.querySelectorAll('.delete-btn').forEach(btn => {
                btn.addEventListener('click', e => {
                    const card = e.target.closest('.cart-item');
                    card.remove();
                    updateTotalPrice();
                });
            });

            // Khởi tạo trạng thái ban đầu
            updateTotalPrice();
        </script>
    </body>

</html>