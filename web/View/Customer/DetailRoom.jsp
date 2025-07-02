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
        <link rel="stylesheet" href="Css/Customer/DetailRoom.css"/>
    </head>
    <body>
        <div class="room-container">
            <div class="room-image">
                <img src="${pageContext.request.contextPath}/${selectedTypeRoom.uriContextOfImages}${selectedTypeRoom.images[0]}" alt="Room Image" class="main">
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
                <div class="rating">★★★★★ (4.9)</div>
                <div class="price"><fmt:formatNumber value="${selectedTypeRoom.price}" type="currency" currencyCode="VND" />/night</div>
                <form>
                    <div class="form-group">
                        <label>Check-in</label>
                        <input name="checkin" value="${param.checkin}" type="date" class="form-control" on-change="this.form.submit()">
                    </div>
                    <div class="form-group">
                        <label>Check-out</label>
                        <input name="checkout" value="${param.checkout}" type="date" class="form-control" on-change="this.form.submit()">
                    </div>
                    <div class="form-group">
                        <label>Rooms</label>
                        <input type="number" class="form-control" value="1" min="1">
                    </div>
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
            <c:forEach var="service" items="${selectedTypeRoom.servicesOfTypeRoom}">
                <div class="feature-item">
                    <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                        <path d="M22 12h-4l-3 9L9 3l-3 9H2"></path>
                    </svg>
                    <span>${service.name}</span>
                </div>
            </c:forEach>
        </div>
        <div class="price-details">
            <h3>Price Details</h3>
            <div>Per Night: <fmt:formatNumber value="${selectedTypeRoom.originPrice}" type="currency" currencyCode="VND" /></div>
            <div>Service Charge: <fmt:formatNumber value="${selectedTypeRoom.servicePrice}" type="currency" currencyCode="VND" /></div>
        </div>
        <div class="reviews-section">
            <h3>${selectedTypeRoom.numberOfReviews} Reviews</h3>
            <c:forEach var="review" items="${selectedTypeRoom.reviews}">
                <div class="review">
                    <div class="reviewer-info">
                        <img src="${pageContext.request.contextPath}/Image/User.png" alt="${review.username}">
                        <div>
                            <strong>${review.username}</strong> <span>${review.date}</span>
                        </div>
                    </div>
                    <p>${review.feedBack}</p>
                </div>
            </c:forEach>
        </div>
        <div class="review-form">
            <h3>Leave a Review</h3>
            <form>
                <div class="form-group">
                    <textarea class="form-control" placeholder="Write your Review here *"></textarea>
                </div>
                <button type="submit" class="btn">Post Comment</button>
            </form>
        </div>
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
        </script>
    </body>
</html>
