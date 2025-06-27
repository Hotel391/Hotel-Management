<%-- 
    Document   : SearchRoom
    Created on : Jun 26, 2025, 9:34:24 PM
    Author     : HieuTT
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
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
            #content {
                width: 100%;
            }
            /* Đảm bảo hình ảnh trong card hiển thị tốt */
            .card img {
                object-fit: cover;
                height: 100%;
            }
            /* Tùy chỉnh để phù hợp với layout hiện tại */
            .booking-form {
                background: #fff;
                -webkit-box-shadow: 0px 2px 5px -2px rgba(0, 0, 0, 0.3);
                box-shadow: 0px 2px 5px -2px rgba(0, 0, 0, 0.3);
                border: 1px solid rgba(60, 64, 101, 0.1);
                padding: 15px;
                margin-bottom: 20px;
            }
            .booking-form .form-control {
                font-family: 'Alegreya', serif;
                background-color: transparent;
                border-radius: 0px;
                border: none;
                height: 50px;
                -webkit-box-shadow: none;
                box-shadow: none;
                padding: 0;
                font-size: 28px;
                color: #3c404a;
                font-weight: 700;
                width: 60%;
            }
            .booking-form select.form-control + .select-arrow {
                position: absolute;
                right: 10px;
                top: 50%;
                transform: translateY(-50%);
                width: 20px;
                line-height: 20px;
                height: 20px;
                text-align: center;
                pointer-events: none;
                color: #818390;
                font-size: 12px;
            }
            .booking-form .form-label {
                color: #818390;
                font-weight: 400;
                font-size: 14px;
            }
            .booking-form .submit-btn {
                background: #9a8067;
                color: #fff;
                border: none;
                font-weight: 400;
                text-transform: uppercase;
                font-size: 14px;
                height: 40px;
                width: 100%;
            }
            .booking-form .form-header h2 {
                font-family: 'Alegreya', serif;
                margin: 0;
                display: inline-block;
                font-size: 52px;
                color: #9a8067;
            }
            .booking-form .form-btn{
                width: 100%;
                height: 100%;
            }
            .booking-form .form-btn .submit-btn {
                width: 100%;
                height: 100%;
            }

            .price-filter-container {
                display: flex;
                align-items: center;
                gap: 12px;
                flex-wrap: wrap;
                margin: 10px 0;
            }

            .price-input {
                padding: 8px 12px;
                width: 40%;
                border: 1px solid #ccc;
                border-radius: 6px;
                font-size: 14px;
            }

            .price-separator {
                font-size: 18px;
                color: #555;
            }

            .apply-button {
                padding: 8px 16px;
                background-color: #E14924;
                color: white;
                border: none;
                border-radius: 6px;
                font-size: 14px;
                cursor: pointer;
                width: 92%;
                transition: background-color 0.3s ease;
            }

            .apply-button:hover {
                background-color: #c93c1f;
            }
        </style>
    </head>
    <body>
        <div class="container my-sm-5 border p-0 bg-light main-content">
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
                                <div class="col-md-6">
                                    <div class="form-group">
                                        <span class="form-label">Check In</span>
                                        <input class="form-control" type="date">
                                    </div>
                                </div>
                                <div class="col-md-6">
                                    <div class="form-group">
                                        <span class="form-label">Check out</span>
                                        <input class="form-control" type="date">
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

        <!-- Nội Dung Chính -->
        <div class="container my-sm-5 border p-0 bg-light main-content">
            <div class="row">
                <!-- Bộ Lọc (Filter Sidebar) -->
                <div class="col-md-3">
                    <div id="filter" class="p-2 bg-light border">
                        <div class="border-bottom h5 text-uppercase">Chọn bộ lọc theo:</div>
                        <div class="box border-bottom">
                            <div class="box-label text-uppercase d-flex align-items-center">Price
                            </div>
                            <div id="inner-box" class="collapse show">
                                <div class="price-filter-container">
                                    <input type="text" maxlength="13" class="price-input" placeholder="₫ TỪ" />
                                    <span class="price-separator">–</span>
                                    <input type="text" maxlength="13" class="price-input" placeholder="₫ ĐẾN" />
                                    <button class="apply-button" onclick="applyFilters()">Áp Dụng</button>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <!-- Danh Sách Khách Sạn (Hotel Listings) -->
                <div class="col-md-9">
                    <div id="hotels" class="bg-white p-2 border">
                        <!-- Khách Sạn 1 -->
                        <c:forEach var="typeRoom" items="typeRoomList">
                            <div class="card mb-3">
                                <div class="row g-0">
                                    <div class="col-md-4">
                                        <img src="https://images.unsplash.com/photo-1580835845971-a393b73bf370?ixid=MXwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHw%3D&ixlib=rb-1.2.1&auto=format&fit=crop&w=967&q=80" class="img-fluid rounded-start" alt="Mayflower Hibiscus Inn">
                                    </div>
                                    <div class="col-md-8">
                                        <div class="card-body">
                                            <h5 class="card-title">Mayflower Hibiscus Inn <small class="text-muted">Bandra, Mumbai</small></h5>
                                            <div class="rating">
                                                <span class="fas fa-star text-warning"></span>
                                                <span class="fas fa-star text-warning"></span>
                                                <span class="fas fa-star text-warning"></span>
                                                <span class="fas fa-star text-warning"></span>
                                                <span class="far fa-star text-warning"></span>
                                            </div>
                                            <p class="card-text">
                                                <span class="badge bg-info me-1"><i class="fas fa-receipt"></i> Express check-in</span>
                                                <span class="badge bg-success me-1"><i class="fas fa-check-circle"></i> Available</span>
                                            </p>
                                            <div class="d-flex justify-content-end">
                                                <button class="btn btn-primary text-uppercase">Book Now</button>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </c:forEach>
                    </div>
                </div>
            </div>
        </div>
    </body>
</html>
