<%-- 
    Document   : SearchRoom
    Created on : Jun 26, 2025, 9:34:24 PM
    Author     : HieuTT
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
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
            .booking-form .form-btn .submit-btn {
                width: 100%;
                height: 100%;
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
                        <div class="border-bottom h5 text-uppercase">Filter By</div>
                        <div class="box border-bottom">
                            <div class="box-label text-uppercase d-flex align-items-center">Price
                                <button class="btn ms-auto" type="button" data-bs-toggle="collapse" data-bs-target="#inner-box" aria-expanded="false" aria-controls="inner-box">
                                    <span class="fas fa-plus"></span>
                                </button>
                            </div>
                            <div id="inner-box" class="collapse show">
                                <div class="my-1">
                                    <label class="tick">Less than 2000
                                        <input type="checkbox" checked="checked">
                                        <span class="check"></span>
                                    </label>
                                </div>
                                <div class="my-1">
                                    <label class="tick">2000 - 3000
                                        <input type="checkbox">
                                        <span class="check"></span>
                                    </label>
                                </div>
                                <div class="my-1">
                                    <label class="tick">3000 - 4500
                                        <input type="checkbox">
                                        <span class="check"></span>
                                    </label>
                                </div>
                                <div class="my-1">
                                    <label class="tick">4500 - 6000
                                        <input type="checkbox">
                                        <span class="check"></span>
                                    </label>
                                </div>
                                <div class="my-1">
                                    <label class="tick">6000 - 8000
                                        <input type="checkbox">
                                        <span class="check"></span>
                                    </label>
                                </div>
                                <div class="my-1">
                                    <label class="tick">8000 - 10,000
                                        <input type="checkbox" checked>
                                        <span class="check"></span>
                                    </label>
                                </div>
                                <div class="my-1">
                                    <label class="tick">10,000 and Above
                                        <input type="checkbox">
                                        <span class="check"></span>
                                    </label>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <!-- Danh Sách Khách Sạn (Hotel Listings) -->
                <div class="col-md-9">
                    <div id="hotels" class="bg-white p-2 border">
                        <!-- Khách Sạn 1 -->
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
                                            <span class="badge bg-success me-1"><i class="fas fa-comment-dollar"></i> Free Cancellation</span>
                                            <span class="badge bg-info me-1"><i class="fas fa-receipt"></i> Express check-in</span>
                                            <span class="badge bg-primary"><i class="fas fa-concierge-bell"></i> Concierge</span>
                                        </p>
                                        <div class="d-flex justify-content-end">
                                            <button class="btn btn-outline-secondary me-2 text-uppercase">Enquiry</button>
                                            <button class="btn btn-primary text-uppercase">Book Now</button>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </body>
</html>
