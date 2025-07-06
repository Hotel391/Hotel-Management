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
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Room Details</title>
        <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
        <link rel="stylesheet" href="Css/Customer/DetailRoom.css"/>
    </head>
    <body>
        <jsp:include page="Header.jsp" />
        <div class="room-container">
            <div class="room-image">
                <img src="${pageContext.request.contextPath}/${selectedTypeRoom.uriContextOfImages}${selectedTypeRoom.images[0]}" alt="Room" class="main">
                <button class="nav-arrow prev" onclick="changeImage(-1)">❮</button>
                <button class="nav-arrow next" onclick="changeImage(1)">❯</button>
                <div class="thumbnails">
                    <c:forEach var="index" begin="1" end="${selectedTypeRoom.images.size()-1}">
                        <img src="${pageContext.request.contextPath}/${selectedTypeRoom.uriContextOfImages}${selectedTypeRoom.images[index]}" 
                             alt="Thumbnail" 
                             onclick="document.querySelector('.main').src = this.src">
                    </c:forEach>
                </div>
            </div>
            <div class="room-details">
                <h2>${selectedTypeRoom.typeName}</h2>
                <c:if test="${selectedTypeRoom.numberOfReviews > 0}">
                    <div class="rating mb-2">
                        <c:set var="rating" value="${selectedTypeRoom.averageRating}" />
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
                            <fmt:formatNumber value="${selectedTypeRoom.averageRating}" type="number" maxFractionDigits="2" />
                            )</span>
                    </div>
                    <p class="card-text">${selectedTypeRoom.numberOfReviews} bài đánh giá</p>
                </c:if>
                <c:if test="${selectedTypeRoom.numberOfReviews eq 0}">
                    <p class="card-text">Chưa có đánh giá</p>
                </c:if>
                <div class="price"><fmt:formatNumber value="${selectedTypeRoom.price}" type="currency" currencyCode="VND" />/night</div>
                <form>
                    <input type="hidden" name="typeRoomId" value="${selectedTypeRoom.typeId}">
                    <div class="form-group">
                        <label for="checkin">Check-in</label>
                        <input name="checkin" value="${param.checkin}" type="date" class="form-control" onchange="this.form.submit()">
                    </div>
                    <div class="form-group">
                        <label for="checkout">Check-out</label>
                        <input name="checkout" value="${param.checkout}" type="date" class="form-control" onchange="this.form.submit()">
                    </div>
                    <div class="form-group">
                        <label for="rooms">Rooms</label>
                        <input type="number" class="form-control" value="${empty param.rooms ? 1 : param.rooms}" min="1" max="${selectedTypeRoom.numberOfAvailableRooms}" name="rooms"">
                    </div>
                </form>
                <form method="post">
                    <button type="submit" class="btn btn-primary">Add to cart</button>
                </form>
            </div>
        </div>
        <div class="description-section">
            <div class="description">
                <h3>Room Description</h3>
                <p>${selectedTypeRoom.description}</p>
            </div>
            <div class="quick-stats">
                <h3>Quick Stats</h3>
                <div>Balcony: Private</div>
                <div>Check-in: 12:00 PM</div>
                <div>Check-out: 9:00 AM</div>
            </div>
        </div>
        <div class="overview-section">
            <h3>Room Overview</h3>
            <c:forEach var="roomNService" items="${selectedTypeRoom.services}">
                <div class="feature-item">
                    <c:choose>
                        <c:when test="${roomNService.service.serviceName.toLowerCase().contains('wifi') || roomNService.service.serviceName.toLowerCase().contains('wi-fi')}">
                            <i class="fas fa-wifi"></i>
                        </c:when>
                        <c:when test="${roomNService.service.serviceName.toLowerCase().contains('đưa đón')}">
                            <i class="fas fa-shuttle-van"></i>
                        </c:when>
                        <c:when test="${roomNService.service.serviceName.toLowerCase().contains('spa')}">
                            <i class="fas fa-spa"></i>
                        </c:when>
                        <c:when test="${roomNService.service.serviceName.toLowerCase().contains('bể bơi')}">
                            <i class="fas fa-swimming-pool"></i>
                        </c:when>
                        <c:when test="${roomNService.service.serviceName.toLowerCase().contains('thuê xe')}">
                            <i class="fas fa-car"></i>
                        </c:when>
                        <c:when test="${roomNService.service.serviceName.toLowerCase().contains('ăn sáng')}">
                            <i class="fas fa-coffee"></i>
                        </c:when>
                        <c:when test="${roomNService.service.serviceName.toLowerCase().contains('lễ tân')}">
                            <i class="fas fa-concierge-bell"></i>
                        </c:when>
                        <c:otherwise>
                            <i class="fas fa-check-circle"></i>
                        </c:otherwise>
                    </c:choose>
                    <span>${roomNService.service.serviceName}</span>
                </div>
            </c:forEach>
        </div>
        <div class="price-details">
            <h3>Price Details</h3>
            <div>Per Night: <fmt:formatNumber value="${selectedTypeRoom.originPrice}" type="currency" currencyCode="VND" /></div>
            <div>Service Charge: <fmt:formatNumber value="${selectedTypeRoom.servicePrice}" type="currency" currencyCode="VND" /></div>
        </div>
        <div class="reviews-section">
            <div class="review-header">
                <h3>${selectedTypeRoom.numberOfReviews} Reviews</h3>
                <form class="review-form-sort" id="reviews-section">
                    <input type="hidden" name="typeRoomId" value="${selectedTypeRoom.typeId}">
                    <input type="hidden" name="checkin" value="${param.checkin}">
                    <input type="hidden" name="checkout" value="${param.checkout}">
                    <input type="hidden" name="rooms" value="${param.rooms}">
                    <label for="sortBy">Sắp xếp theo:</label>
                    <select name="sortBy" class="form-control mb-3" onchange="this.form.submit()">
                        <option value="recent" ${param.sortBy == 'recent' ? 'selected' : ''}>Gần đây nhất</option>
                        <option value="rating_high" ${param.sortBy == 'rating_high' ? 'selected' : ''}>Đánh giá, cao đến thấp</option>
                        <option value="rating_low" ${param.sortBy == 'rating_low' ? 'selected' : ''}>Đánh giá, thấp đến cao</option>
                        <option value="helpful" ${param.sortBy ne 'recent' && param.sortBy ne 'rating_high' && param.sortBy ne 'rating_low' ? 'selected' : ''}>Hữu ích nhất</option>
                    </select>
                </form>
            </div>
            <c:forEach var="review" items="${selectedTypeRoom.reviews}">
                <div class="review">
                    <div class="reviewer-info">
                        <img src="${pageContext.request.contextPath}/Image/User.png" alt="${review.username}">
                        <div>
                            <strong>${review.username}</strong> <span><fmt:formatDate value="${review.date}" pattern="dd/MM/yyyy" /></span>
                            <span>
                                <c:set var="rating" value="${review.rating}" />
                                <c:set var="fullStars" value="${rating - (rating mod 1)}" />
                                <c:set var="emptyStars" value="${5 - fullStars}" />

                                <!-- Full stars -->
                                <c:forEach begin="1" end="${fullStars}">
                                    <span class="fas fa-star text-warning"></span>
                                </c:forEach>
                                <!-- Empty stars -->
                                <c:forEach begin="1" end="${emptyStars}">
                                    <span class="far fa-star text-warning"></span>
                                </c:forEach>
                            </span>
                        </div>
                    </div>
                    <p>${review.feedBack}</p>
                </div>
            </c:forEach>
            <nav aria-label="Page navigation">
                <ul class="pagination mb-0">
                    <c:if test="${currentPage > 1}">
                        <li class="page-item">
                            <a class="page-link" href="?page=${currentPage - 1}&typeRoomId=${selectedTypeRoom.typeId}&checkin=${param.checkin}&checkout=${param.checkout}&rooms=${param.rooms}&sortBy=${param.sortBy}">Previous</a>
                        </li>
                    </c:if>

                    <c:forEach var="i" begin="1" end="${totalPages}">
                        <li class="page-item ${i == currentPage ? 'active' : ''}">
                            <a class="page-link" href="?page=${i}&typeRoomId=${selectedTypeRoom.typeId}&checkin=${param.checkin}&checkout=${param.checkout}&rooms=${param.rooms}&sortBy=${param.sortBy}">${i}</a>
                        </li>
                    </c:forEach>

                    <c:if test="${currentPage < totalPages}">
                        <li class="page-item">
                            <a class="page-link" href="?page=${currentPage + 1}&typeRoomId=${selectedTypeRoom.typeId}&checkin=${param.checkin}&checkout=${param.checkout}&rooms=${param.rooms}&sortBy=${param.sortBy}">Next</a>
                        </li>
                    </c:if>
                </ul>
            </nav>
        </div>
        <c:if test="${sessionScope.customerInfo != null && canFeedBack}">
            <div class="review-form">
                <h3>Leave a Review</h3>
                <form method="post" action="${pageContext.request.contextPath}/addReview">
                    <input type="hidden" name="typeRoomId" value="${selectedTypeRoom.typeId}">
                    <input type="hidden" name="checkin" value="${param.checkin}">
                    <input type="hidden" name="checkout" value="${param.checkout}">
                    <input type="hidden" name="rooms" value="${param.rooms}">
                    <div class="form-group">
                        <textarea class="form-control" placeholder="Write your Review here *"></textarea>
                    </div>
                    <button type="submit" class="btn">Post Comment</button>
                </form>
            </div>
        </c:if>
        <script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@4.5.2/dist/js/bootstrap.bundle.min.js"></script>
        <script>
                        let currentImage = 0;
                        const images = [
            <c:forEach var="image" items="${selectedTypeRoom.images}" varStatus="status">
                        "${pageContext.request.contextPath}/${selectedTypeRoom.uriContextOfImages}${image}"<c:if test="${!status.last}">,</c:if>
            </c:forEach>
                            ];

                            function changeImage(direction) {
                                if (images.length === 0)
                                    return;
                                currentImage += direction;
                                if (currentImage < 0)
                                    currentImage = images.length - 1;
                                if (currentImage >= images.length)
                                    currentImage = 0;
                                document.querySelector('.main').src = images[currentImage];
                            }
                            window.addEventListener('load', function () {
                                const urlParams = new URLSearchParams(window.location.search);
                                if (urlParams.has('sortBy')) {
                                    const section = document.getElementById('reviews-section');
                                    if (section) {
                                        section.scrollIntoView({behavior: 'smooth'});
                                    }
                                }
                            });
        </script>
    </body>
</html>
