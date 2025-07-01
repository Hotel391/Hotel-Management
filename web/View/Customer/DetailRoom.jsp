<%-- 
    Document   : DetailRoom
    Created on : Jul 1, 2025, 1:16:28 PM
    Author     : HieuTT
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
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
                <button class="nav-arrow prev" onclick="changeImage(-1)">❮</button>
                <img src="https://bootstrapmade.com/content/demo/LuxuryHotel/assets/img/hotel/room-1.webp" alt="Presidential Suite" class="main">
                <button class="nav-arrow next" onclick="changeImage(1)">❯</button>
                <div class="thumbnails">
                    <img src="https://bootstrapmade.com/content/demo/LuxuryHotel/assets/img/hotel/room-3.webp" alt="Thumbnail 1" onclick="document.querySelector('.main').src = this.src">
                    <img src="https://bootstrapmade.com/content/demo/LuxuryHotel/assets/img/hotel/room-7.webp" alt="Thumbnail 2" onclick="document.querySelector('.main').src = this.src">
                    <img src="https://bootstrapmade.com/content/demo/LuxuryHotel/assets/img/hotel/room-12.webp" alt="Thumbnail 3" onclick="document.querySelector('.main').src = this.src">
                </div>
            </div>
            <div class="room-details">
                <h2>Presidential Suite</h2>
                <div class="rating">★★★★★ (4.9)</div>
                <div class="price">$899/night</div>
                <form>
                    <div class="form-group">
                        <label>Check-in</label>
                        <input type="date" class="form-control">
                    </div>
                    <div class="form-group">
                        <label>Check-out</label>
                        <input type="date" class="form-control">
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
                <p>Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia curae; Mauris viverra veniam sit amet lacus cursus bibendum. Pellentesque non nisi enim.</p>
                <p>Nunc auctor, nisl eget ultricies tincidunt, nunc nisi aliquam mauris, eget aliquam lacus nunc vel nisi. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia curae.</p>
            </div>
            <div class="quick-stats">
                <h3>Quick Stats</h3>
                <div>Floor Level: 25-30th Floor</div>
                <div>Balcony: Private</div>
                <div>Check-in: 12:00 PM</div>
                <div>Check-out: 9:00 AM</div>
            </div>
        </div>
        <div class="overview-section">
            <h3>Room Overview</h3>
            <div class="info-box">
                <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M22 12h-4l-3 9L9 3l-3 9H2"></path></svg>
                <div>Wifi</div>
                <div>Free</div>
            </div>
            <div class="info-box">
                <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M5.5 16.5l5-5 5 5"></path><path d="M12 18V5"></path></svg>
                <div>Air Conditioner</div>
                <div>Yes</div>
            </div>
            <div class="info-box">
                <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><rect x="2" y="7" width="20" height="15" rx="2" ry="2"></rect><polyline points="17 2 12 7 7 2"></polyline></svg>
                <div>TV Cable</div>
                <div>Yes</div>
            </div>
        </div>
        <div class="price-details">
            <h3>Price Details</h3>
            <div>Per Night: $299</div>
            <div>Service Charge: $80</div>
        </div>
        <div class="reviews-section">
            <h3>3 Reviews</h3>
            <div class="review">
                <div class="reviewer-info">
                    <img src="https://via.placeholder.com/40" alt="Lufy Carlson">
                    <div>
                        <strong>Lufy Carlson</strong> <span>Jul 10</span>
                    </div>
                </div>
                <p>Tristique tempis condimentum diam done ullamcoer sit element henddg sit he consequuent.</p>
            </div>
            <div class="review">
                <div class="reviewer-info">
                    <img src="https://via.placeholder.com/40" alt="Lora Leigh">
                    <div>
                        <strong>Lora Leigh</strong> <span>Jul 10</span>
                    </div>
                </div>
                <p>Tristique tempis condimentum diam done ullamcoer sit element henddg sit he consequuent.</p>
            </div>
            <div class="review">
                <div class="reviewer-info">
                    <img src="https://via.placeholder.com/40" alt="Natalie Dormer">
                    <div>
                        <strong>Natalie Dormer</strong> <span>Jul 10</span>
                    </div>
                </div>
                <p>Tristique tempis condimentum diam done ullamcoer sit element henddg sit he consequuent.</p>
            </div>
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
                            "https://bootstrapmade.com/content/demo/LuxuryHotel/assets/img/hotel/room-1.webp",
                            "https://bootstrapmade.com/content/demo/LuxuryHotel/assets/img/hotel/room-3.webp",
                            "https://bootstrapmade.com/content/demo/LuxuryHotel/assets/img/hotel/room-7.webp",
                            "https://bootstrapmade.com/content/demo/LuxuryHotel/assets/img/hotel/room-12.webp"
                        ];

                        function changeImage(direction) {
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
