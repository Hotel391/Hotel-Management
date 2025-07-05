<%-- 
    Document   : SearchRoom
    Created on : Jun 26, 2025, 9:34:24 PM
    Author     : HieuTT
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Hotel Booking</title>
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0-alpha1/dist/css/bootstrap.min.css"/>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0-alpha1/dist/js/bootstrap.bundle.min.js"></script>
        <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
        <link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.7.2/css/all.css"/>
        <!-- Liên kết đến các tệp CSS của thanh search mới -->
        <link type="text/css" rel="stylesheet" href="Css/searchRoom.css" />
        <style>
            .sort-label {
                font-size: 14px;
                color: #333;
                font-weight: bold;
            }
            .sort-options {
                /* border-radius: 70px;
                border: #000 1px solid; */
                height: 70px;
                max-width: 900px;
            }
            .sort-option {
                font-size: 14px;
                color: #333;
                cursor: pointer;
                padding: 5px 10px;
                transition: all 0.3s ease;
                width: calc(100% / 3);
                text-align: center;
                align-content: center;
                height: 70px;
                border: #000 1px solid;
                font-weight: 700;
                color: #2067da;
            }
            .sort-option:hover {
                color: #007bff;
            }
            .sort-option.active {
                background-color: #E6F0FA; 
                color: #000;
                border-bottom: 2px solid #0071C2;
                border-radius: 0;
            }
            .promo-text {
                font-size: 14px;
                color: #007bff;
                font-weight: bold;
            }
        </style>
    </head>
    <body>
        <jsp:include page="Header.jsp" />
        <div class="container my-sm-5 border p-0 bg-light">
            <div class="booking-form">
                <form>
                    <div class="row no-margin">
                        <div class="col-md-3">
                            <div class="form-header">
                                <h2>Book Now</h2>
                            </div>
                        </div>
                        <div class="col-md-7">
                            <div class="row no-margin">
                                <div class="col-md-3">
                                    <div class="form-group">
                                        <span class="form-label">Check In</span>
                                        <input class="form-control" type="date" name="checkin" value="${checkin}">
                                    </div>
                                </div>
                                <div class="col-md-3">
                                    <div class="form-group">
                                        <span class="form-label">Check out</span>
                                        <input class="form-control" type="date" name="checkout" value="${checkout}">
                                    </div>
                                </div>
                                <div class="col-md-3">
                                    <div class="form-group">
                                        <span class="form-label">Adult</span>
                                        <input class="form-control" type="number" name="adults" value="<c:out value='${adults}' default='1' />" min="1">
                                    </div>
                                </div>
                                <div class="col-md-3">
                                    <div class="form-group">
                                        <span class="form-label">Children</span>
                                        <input class="form-control" type="number" name="children" value="<c:out value='${children}' default='0' />" min="0">
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="col-md-2">
                            <div class="form-btn">
                                <button class="submit-btn">Check availability</button>
                            </div>
                        </div>
                    </div>
                </form>
            </div>
        </div>
        <!-- Form Sắp Xếp -->
        <div class="container my-sm-5 p-0">
            <div class="sorting-form bg-white p-2 d-flex justify-content-between align-items-center" style="border-radius: 10px 0 0 10px;">
                <div class="sort-label text-uppercase fw-bold me-3">Sắp xếp theo:</div>
                <div class="sort-options d-flex flex-grow-1 align-items-center">
                    <span class="sort-option" 
                        style="border-top-left-radius: 70px; border-bottom-left-radius: 70px;
                        ${empty sortOrder || sortOrder eq 'price-low' ? "background-color: #2067da; color: #fff": ""}"
                        onclick="window.location.href='?sort=price-low&checkin=${checkin}&checkout=${checkout}&adults=${adults}&children=${children}&minPrice=${param.minPrice}&maxPrice=${param.maxPrice}'">Giá thấp nhất</span>
                    <span class="sort-option" style="${sortOrder eq 'price-high'? "background-color: #2067da; color: #fff" : ""}" 
                        onclick="window.location.href='?sort=price-high&checkin=${checkin}&checkout=${checkout}&adults=${adults}&children=${children}&minPrice=${param.minPrice}&maxPrice=${param.maxPrice}'">Giá cao nhất</span>
                    <span class="sort-option" 
                        style="border-top-right-radius: 70px; border-bottom-right-radius: 70px;
                        ${sortOrder eq 'rating-high'? "background-color: #2067da; color: #fff": ""}" 
                        onclick="window.location.href='?sort=rating-high&checkin=${checkin}&checkout=${checkout}&adults=${adults}&children=${children}&minPrice=${param.minPrice}&maxPrice=${param.maxPrice}'">Đánh giá cao nhất</span>
                </div>
            </div>
        </div>

        <!-- Nội Dung Chính -->
        <div class="container my-sm-5 p-0">
            <div class="row">
                <!-- Bộ Lọc (Filter Sidebar) -->
                <div class="col-md-3">
                    <div id="filter" class="p-2 bg-light">
                        <div class="border-bottom h5 text-uppercase">Chọn bộ lọc theo:</div>
                        <div class="box border-bottom">
                            <div class="box-label text-uppercase d-flex align-items-center">Giá mỗi đêm
                            </div>
                            <div id="inner-box" class="collapse show">
                                <form method="get" onsubmit="return validatePriceRange()">
                                    <div class="price-filter-container">
                                        <input type="number" maxlength="13" name="minPrice" class="price-input" min="0"
                                               placeholder="₫ TỪ" value="${param.minPrice}" id="minPrice"/>
                                        <span class="price-separator">–</span>
                                        <input type="number" maxlength="13" name="maxPrice" class="price-input" min="0"
                                               placeholder="₫ ĐẾN" value="${param.maxPrice}" id="maxPrice"/>
                                        <input type="hidden" name="checkin" value="${param.checkin}" />
                                        <input type="hidden" name="checkout" value="${param.checkout}" />
                                        <p>${errorPrice}</p>
                                        <button class="apply-button">Áp Dụng</button>
                                    </div>
                                </form>
                            </div>
                        </div>
                    </div>
                </div>
                <!-- Danh Sách Khách Sạn (Hotel Listings) -->
                <div class="col-md-9">
                    <div id="hotels" class="bg-white p-2 border">
                        <c:forEach var="typeRoom" items="${typeRooms}">
                            <div class="card mb-3 room-card p-2" 
                                 draggable="true"
                                 ondragstart="event.dataTransfer.setData('text/plain', '${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}${pageContext.request.contextPath}/detailRoom?typeRoomId=${typeRoom.typeId}&checkin=${checkin}&checkout=${checkout}')"
                                 onclick="location.href = '${pageContext.request.contextPath}/detailRoom?typeRoomId=${typeRoom.typeId}&checkin=${checkin}&checkout=${checkout}'">
                                <div class="row g-0">
                                    <div class="col-md-4">
                                        <img src="${pageContext.request.contextPath}/${typeRoom.uriContextOfImages}${typeRoom.images[0]}" alt="${typeRoom.typeName}" class="main-image" />

                                        <div class="thumbnail-images">
                                            <c:forEach var="index" begin="1" end="3">
                                                <c:if test="${index lt 4}">
                                                    <img src="${pageContext.request.contextPath}/${typeRoom.uriContextOfImages}${typeRoom.images[index]}" alt="Room Thumb" />
                                                </c:if>
                                            </c:forEach>
                                            <div class="see-all">See all</div>
                                        </div>
                                    </div>

                                    <div class="col-md-8">
                                        <div class="card-body">
                                            <h5 class="card-title">${typeRoom.typeName}</h5>
                                            <c:if test="${typeRoom.numberOfReviews > 0}">
                                                <div class="rating mb-2">
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
                                                <p class="card-text">${typeRoom.numberOfReviews} bài đánh giá</p>
                                            </c:if>
                                            <c:if test="${typeRoom.numberOfReviews eq 0}">
                                                <p class="card-text">Chưa có đánh giá</p>
                                            </c:if>
                                            <p class="card-text">
                                                <span class="badge bg-success me-1"><i class="fas fa-check-circle"></i> Available: ${typeRoom.numberOfAvailableRooms}</span>
                                            </p>
                                            <p class="" style="margin-left: 86%; margin-top: 17%;">
                                                <span class="badge bg-success me-1"><fmt:formatNumber value="${typeRoom.price}" type="currency" currencyCode="VND" /></span>
                                            </p>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </c:forEach>
                    </div>
                    <div class="d-flex justify-content-between align-items-center">
                        <span></span>
                        <nav aria-label="Page navigation">
                            <ul class="pagination mb-0">
                                <c:if test="${currentPage > 1}">
                                    <li class="page-item">
                                        <a class="page-link"
                                           href="?page=${currentPage - 1}&checkin=${checkin}&checkout=${checkout}">Previous</a>
                                    </li>
                                </c:if>

                                <c:forEach var="i" begin="1" end="${totalPages}">
                                    <li class="page-item ${i == currentPage ? 'active' : ''}">
                                        <a class="page-link"
                                           href="?page=${i}&checkin=${checkin}&checkout=${checkout}">${i}</a>
                                    </li>
                                </c:forEach>

                                <c:if test="${currentPage < totalPages}">
                                    <li class="page-item">
                                        <a class="page-link"
                                           href="?page=${currentPage + 1}&checkin=${checkin}&checkout=${checkout}">Next</a>
                                    </li>
                                </c:if>
                            </ul>
                        </nav>
                    </div>
                </div>
            </div>
        </div>
    </body>
    <script>
        function validatePriceRange() {
            const min = document.getElementById("minPrice").value;
            const max = document.getElementById("maxPrice").value;

            const minVal = min ? parseInt(min) : null;
            const maxVal = max ? parseInt(max) : null;

            if ((minVal !== null && minVal < 0) || (maxVal !== null && maxVal < 0)) {
                alert("Giá không được nhỏ hơn 0.");
                return false;
            }

            if (minVal !== null && maxVal !== null && maxVal <= minVal) {
                alert("Giá ĐẾN phải lớn hơn giá TỪ.");
                return false;
            }

            return true;
        }
        document.addEventListener("DOMContentLoaded", function () {
            const checkinInput = document.querySelector('input[name="checkin"]');
            const checkoutInput = document.querySelector('input[name="checkout"]');

            function updateCheckoutMin() {
                if (!checkinInput.value)
                    return;

                const checkinDate = new Date(checkinInput.value);
                const checkoutMinDate = new Date(checkinDate);
                checkoutMinDate.setDate(checkinDate.getDate() + 1);

                const minCheckoutStr = checkoutMinDate.toISOString().split("T")[0];
                checkoutInput.setAttribute("min", minCheckoutStr);

                // Auto-update checkout value if invalid
                if (!checkoutInput.value || new Date(checkoutInput.value) <= checkinDate) {
                    checkoutInput.value = minCheckoutStr;
                }
            }

            // Set initial min
            const today = new Date().toISOString().split("T")[0];
            checkinInput.setAttribute("min", today);
            updateCheckoutMin();

            // Re-check when user changes checkin date
            checkinInput.addEventListener("change", updateCheckoutMin);
        });
    </script>
</html>
