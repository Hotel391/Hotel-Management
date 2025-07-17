
<%-- 
    Document   : CartToBooking
    Created on : 12 thg 7, 2025, 21:10:09
    Author     : Tuan'sPC
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"  %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Nhận phòng</title>
        <%--style for dashboard--%>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css"
              rel="stylesheet"
              integrity="sha384-EVSTQN3/azprG1Anm3QDgpJLIm9Nao0Yz1ztcQTwFspd3yD65VohhpuuCOmLASjC"
              crossorigin="anonymous">
        <link rel="stylesheet"
              href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.1/font/bootstrap-icons.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/Css/navDashboardStyle.css" />
        <link rel="stylesheet" href="${pageContext.request.contextPath}/Css/dashboardStyle.css" />
        <%--another in the following--%>
    </head>
    <body>
        <div class="containerBox">
            <jsp:include page="leftNavReceptionist.jsp" />
            <div class="right-section">
                <c:set var="title" value="Khách đến nhận phòng" scope="request" />
                <jsp:include page="topNavReceptionist.jsp" />

                <div class="main-content">
                    <div class="container-fluid p-4">

                        <c:set var="choose" value="${requestScope.choose != null ? requestScope.choose : (param.choose != null ? param.choose : 'viewCustomerFuture')}" />


                        <ul class="nav nav-tabs mb-3">
                            <li class="nav-item">
                                <a class="nav-link ${choose == 'viewCustomerFuture' ? 'active' : ''}" 
                                   href="${pageContext.request.contextPath}/receptionist/cartToBooking?choose=viewCustomerFuture">
                                    Khách đến trong khoảng thời gian đã đặt
                                </a>
                            </li>
                            <li class="nav-item">
                                <a class="nav-link ${choose == 'viewCustomerHasDuaDon' ? 'active' : ''}" 
                                   href="${pageContext.request.contextPath}/receptionist/cartToBooking?choose=viewCustomerHasDuaDon">
                                    Khách sắp đến có dịch vụ đưa đón trong tương lai
                                </a>
                            </li>
                        </ul>


                        <div class="d-flex justify-content-between align-items-center mb-3">
                            <form method="get" action="${pageContext.request.contextPath}/receptionist/cartToBooking" class="d-flex mb-3">
                                <input type="hidden" name="choose" value="search" />
                                <input type="hidden" name="page" value="${requestScope.currentPage}">

                                <c:if test="${requestScope.cartStatus == 'bookFuture'}">
                                    <input type="hidden" name="source" value="viewCustomerFuture" />
                                </c:if>
                                <c:if test="${requestScope.cartStatus == 'view'}">
                                    <input type="hidden" name="source" value="viewCustomerHasDuaDon" />
                                </c:if>

                                <input type="email" name="searchEmail" class="form-control me-2" placeholder="Tìm theo email" required />
                                <button class="btn btn-primary" type="submit">Tìm</button>
                            </form>
                        </div>

                        <!--thông báo thành công-->
                        <c:if test="${param.success == 'true'}">
                            <div id="successAlert" class="alert alert-success alert-dismissible fade show mt-3 mx-auto text-center" role="alert" style="width: fit-content;">
                                <c:choose>
                                    <c:when test="${param.action == 'addBooking'}">Nhận phòng thành công!</c:when>
                                    <c:otherwise>Cập nhật căn cước công dân thành công!</c:otherwise>
                                </c:choose>
                                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                            </div>
                        </c:if>

                        <!--table list cart đã thanh toán thành công-->
                        <div class="table-container">
                            <table class="table align-middle bg-white">
                                <thead class="table-light">
                                    <tr>
                                        <th scope="col">Ngày vào phòng</th>
                                        <th scope="col">Ngày trả phòng</th>
                                        <th scope="col">Tổng giá</th>
                                        <th scope="col">Phương thức thanh toán</th>
                                        <th scope="col">Khách hàng</th>
                                        <th scope="col">Số phòng</th>
                                        <th scope="col">Dịch vụ</th>
                                            <c:if test="${requestScope.cartStatus != 'view'}">
                                            <th scope="">Nhận phòng</th>
                                            </c:if>

                                    </tr>
                                </thead>
                                <tbody>
                                    <c:if test="${empty requestScope.listCartCompleteBank}">
                                        <tr>
                                            <td colspan="6" class="text-center">Không tìm thấy khách nào đã thanh toán đến nhận phòng trong hôm nay.</td>
                                        </tr>
                                    </c:if>
                                    <c:forEach var="c" items="${requestScope.listCartCompleteBank}">
                                        <tr>
                                            <td>${c.startDate}</td>
                                            <td>${c.endDate}</td>
                                            <td><fmt:formatNumber value="${c.totalPrice}" pattern="#,##0 'VNĐ'" />
                                            </td>
                                            <td>${c.paymentMethod.paymentName}</td>
                                            <td>
                                                <button class="btn btn-sm btn-outline-primary"
                                                        onclick="showCustomerInfo('${c.mainCustomer.fullName}', '${c.mainCustomer.email}',
                                                                        '${c.mainCustomer.phoneNumber}', '${c.mainCustomer.gender}', '${c.mainCustomer.CCCD}')">
                                                    <i class="bi bi-person-lines-fill"></i> Xem
                                                </button>
                                            </td>
                                            <td>${c.roomNumber}</td>
                                            <td>
                                                <ul class="list-unstyled mb-0">
                                                    <c:forEach var="s" items="${c.cartServices}">
                                                        <li class="mb-1">
                                                            <span class="fw-bold text-primary">${s.service.serviceName}</span>
                                                            <span class="badge bg-success ms-2">${s.priceAtTime}đ</span>
                                                            <span class="badge bg-info text-dark ms-1">x${s.quantity}</span>
                                                        </li>
                                                    </c:forEach>
                                                </ul>
                                            </td>
                                            <c:if test="${requestScope.cartStatus != 'view'}">
                                                <td>
                                                    <form method="post" action="${pageContext.request.contextPath}/receptionist/cartToBooking">
                                                        <input type="hidden" name="cartId" value="${c.cartId}" />
                                                        <input type="hidden" name="paidAmount" value="${c.totalPrice}" />
                                                        <input type="hidden" name="payDay" value="${c.payDay}" />
                                                        <input type="hidden" name="status" value="${c.status}" />
                                                        <input type="hidden" name="customerId" value="${c.mainCustomer.customerId}" />
                                                        <input type="hidden" name="paymentMethodId" value="${c.paymentMethod.paymentMethodId}" />
                                                        <input type="hidden" name="startDate" value="${c.startDate}" />
                                                        <input type="hidden" name="endDate" value="${c.endDate}" />
                                                        <input type="hidden" name="roomNumber" value="${c.roomNumber}" />
                                                        <input type="hidden" name="choose" value="cartToBooking" />
                                                        <input type="hidden" name="page" value="${requestScope.currentPage}">
                                                        
                                                        <input type="hidden" name="searchEmail" value="${param.searchEmail}"/>

                                                        <c:forEach var="s" items="${c.cartServices}">
                                                            <input type="hidden" name="serviceId" value="${s.service.serviceId}" />
                                                            <input type="hidden" name="quantity" value="${s.quantity}" />
                                                            <input type="hidden" name="priceAtTime" value="${s.priceAtTime}" />
                                                        </c:forEach>

                                                        <button type="submit" class="btn btn-success btn-sm">
                                                            Nhận phòng
                                                        </button>
                                                    </form>
                                                </td>
                                            </c:if>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>

                            <!-- Nút phân trang -->
                            <div class="d-flex justify-content-center mt-3">
                                <nav>
                                    <ul class="pagination">
                                        <c:forEach begin="1" end="${totalPages}" var="i">
                                            <li class="page-item ${i == currentPage ? 'active' : ''}">
                                                <form action="${pageContext.request.contextPath}/receptionist/cartToBooking" method="get">
                                                    <c:if test="${requestScope.source == 'search'}">
                                                        <input type="hidden" name="choose" value="search" />
                                                        <input type="hidden" name="searchEmail" value="${param.searchEmail}"/>
                                                        <c:if test="${requestScope.cartStatus == 'bookFuture'}">
                                                            <input type="hidden" name="source" value="viewCustomerFuture" />
                                                        </c:if>
                                                        <c:if test="${requestScope.cartStatus == 'view'}">
                                                            <input type="hidden" name="source" value="viewCustomerHasDuaDon" />
                                                        </c:if>
                                                    </c:if>
                                                    <c:if test="${requestScope.source != 'search'}">
                                                        <input type="hidden" name="choose" value="${choose}" />
                                                    </c:if>

                                                    <button class="page-link">${i}</button>
                                                    <input type="hidden" name="page" value="${i}" />
                                                </form>
                                            </li>
                                        </c:forEach>
                                    </ul>
                                </nav>
                            </div>
                        </div>
                    </div>

                    <!-- Modal nhập CCCD -->
                    <div class="modal fade" id="cccdModal" tabindex="-1" aria-labelledby="cccdModalLabel" aria-hidden="true">
                        <div class="modal-dialog">
                            <form action="${pageContext.request.contextPath}/receptionist/cartToBooking" method="post">
                                <input type="hidden" name="choose" value="updateCCCD"/>
                                <input type="hidden" name="page" value="${requestScope.currentPage}">
                                <input type="hidden" name="searchEmail" value="${param.searchEmail}"/>

                                <div class="modal-content">
                                    <div class="modal-header">
                                        <h5 class="modal-title" id="cccdModalLabel">Nhập CCCD khách hàng</h5>
                                        <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                                    </div>
                                    <div class="modal-body">
                                        <p>Khách hàng này chưa có thông tin CCCD. Vui lòng nhập để tiếp tục.</p>
                                        <input type="hidden" name="customerId" value="${requestScope.customerId}" />

                                        <div class="mb-3">
                                            <label for="fullnameInput" class="form-label">Tên khách hàng</label>
                                            <input type="text" class="form-control" id="fullnameInput" 
                                                   name="fullName" value="${requestScope.fullname}" readonly />
                                        </div>

                                        <div class="mb-3">
                                            <label for="emailInput" class="form-label">Email khách hàng</label>
                                            <input type="text" class="form-control" id="emailInput" 
                                                   name="email" value="${requestScope.email}" readonly />
                                        </div>

                                        <div class="mb-3">
                                            <label for="cccdInput" class="form-label">CCCD</label>
                                            <input type="text" class="form-control" name="cccd" id="cccdInput" required />
                                        </div>
                                        <c:if test="${not empty requestScope.cccdError}"> ${requestScope.cccdError}</c:if>
                                        </div>
                                        <div class="modal-footer">
                                            <button type="submit" class="btn btn-primary">Cập nhật</button>
                                            <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Hủy</button>
                                        </div>
                                    </div>
                                </form>
                            </div>
                        </div>

                        <!-- Script hiện modal nếu có lỗi -->
                    <c:if test="${requestScope.error == 'errorBookFuture'}">
                        <script>
                            document.addEventListener("DOMContentLoaded", function () {
                                const cccdModalEl = document.getElementById('cccdModal');
                                if (cccdModalEl) {
                                    const cccdModal = new bootstrap.Modal(cccdModalEl);
                                    cccdModal.show();
                                } else {
                                    console.warn("Không tìm thấy modal có id 'cccdModal'");
                                }
                            });
                        </script>
                    </c:if>


                    <!--thông tin khách hàng-->
                    <div class="modal fade" id="customerInfoModal" tabindex="-1" aria-hidden="true">
                        <div class="modal-dialog modal-dialog-centered">
                            <div class="modal-content">
                                <div class="modal-header">
                                    <h5 class="modal-title">Thông tin khách hàng</h5>
                                    <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                                </div>
                                <div class="modal-body">
                                    <p><strong>Họ tên:</strong> <span id="cusName"></span></p>
                                    <p><strong>Email:</strong> <span id="cusEmail"></span></p>
                                    <p><strong>Điện thoại:</strong> <span id="cusPhone"></span></p>
                                    <p><strong>Giới tính:</strong> <span id="cusGender"></span></p>
                                    <p><strong>cccd:</strong> <span id="cusCCCD"></span></p>
                                </div>
                            </div>
                        </div>
                    </div>


                </div>                              
            </div>
        </div>
        <script>
//            hiện thông tin khách hàng
            function showCustomerInfo(name, email, phone, gender, cccd) {
                document.getElementById("cusName").innerText = name;
                document.getElementById("cusEmail").innerText = email;
                document.getElementById("cusPhone").innerText = phone;
                const genderText = (gender === 'true' || gender === true) ? "Nam" : "Nữ";
                document.getElementById("cusGender").innerText = genderText;
                const displayText = cccd && cccd.trim() !== "" ? cccd : "Không có";
                document.getElementById("cusCCCD").innerText = displayText;
                new bootstrap.Modal(document.getElementById("customerInfoModal")).show();
            }


            document.addEventListener('DOMContentLoaded', function () {

                //thông báo thành công
                const alertBox = document.getElementById("successAlert");
                if (alertBox) {
                    setTimeout(() => {
                        // Bootstrap fade out (optional)
                        alertBox.classList.remove("show");
                        alertBox.classList.add("fade");
                        // Xóa phần tử khỏi DOM sau khi fade
                        setTimeout(() => alertBox.remove(), 500);

                        // Xoá param khỏi URL
                        const url = new URL(window.location.href);
                        url.searchParams.delete("success");
                        url.searchParams.delete("action");
                        window.history.replaceState({}, document.title, url.toString());
                    }, 3000); // Hiển thị 3s
                }
            });
        </script>

    </body>
    <%--script for dashboard--%>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js"></script>
    <script src="${pageContext.request.contextPath}/Js/navDashboardJs.js"></script>
    <script src="${pageContext.request.contextPath}/Js/userProfileJs.js"></script>
    <%--another in following--%>
</html>
