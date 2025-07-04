<%-- 
    Document   : ViewReview
    Created on : 31 thg 5, 2025, 14:39:14
    Author     : Tuan'sPC
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>View review</title>
        <%--style for dashbord--%>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css" rel="stylesheet"
              integrity="sha384-EVSTQN3/azprG1Anm3QDgpJLIm9Nao0Yz1ztcQTwFspd3yD65VohhpuuCOmLASjC"
              crossorigin="anonymous">
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.1/font/bootstrap-icons.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/Css/navDashboardStyle.css" />
        <link rel="stylesheet" href="${pageContext.request.contextPath}/Css/dashboardStyle.css" />
        <link rel="stylesheet" href="${pageContext.request.contextPath}/Css/mainDashboardStyle.css" />
        <%--write more in following--%>
        <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
    </head>
    <body>
        <div class="containerBox">
            <jsp:include page="leftNav.jsp" />
            <div class="right-section">
                <c:set var="title" value="Dashboard" scope="request"/>
                <jsp:include page="topNav.jsp" />

                <div class="main-content">
                    <div class="container-fluid p-4">
                        <ul class="nav nav-tabs mb-3">
                            <li class="nav-item">
                                <a class="nav-link active" href="${pageContext.request.contextPath}/manager/review">List review</a>
                            </li>
                        </ul>

                        <div class="d-flex justify-content-between align-items-center mb-3">
                            <div class="d-flex gap-2"></div>

                            <!-- SEARCH FORM -->
                            <div class="d-flex justify-content-between align-items-center mb-3">
                                <form method="post" action="${pageContext.request.contextPath}/manager/review" class="d-flex gap-2">
                                    <!--<input type="text" name="fullName" class="form-control search-input" placeholder="Enter fullName"/>-->

                                    <div class="form-group">
                                        <label for="starFilter" class="form-label">Tìm kiếm theo sao</label>
                                        <select id="starFilter" name="starFilter" class="form-select" style="color: orange">
                                            <option value="">--Tất cả loại &#9733--</option>
                                            <option value="5">&#9733;&#9733;&#9733;&#9733;&#9733; (5 sao)</option>
                                            <option value="4">&#9733;&#9733;&#9733;&#9733; (4 sao)</option>
                                            <option value="3">&#9733;&#9733;&#9733; (3 sao)</option>
                                            <option value="2">&#9733;&#9733; (2 sao)</option>
                                            <option value="1">&#9733; (1 sao)</option>
                                        </select>
                                    </div>

                                    <!-- Month filter -->
                                    <div class="form-group">
                                        <label for="month" class="form-label">Tháng</label>
                                        <select name="month" id="month" class="form-select">
                                            <option value="">-- Chọn tháng --</option>
                                            <c:forEach var="i" begin="1" end="12">
                                                <option value="${i}">Tháng ${i}</option>
                                            </c:forEach>
                                        </select>
                                    </div>

                                    <!-- Year filter -->
                                    <div class="form-group">
                                        <label for="year" class="form-label">Năm</label>
                                        <select name="year" id="year" class="form-select">
                                            <option value="">-- Chọn năm --</option>
                                            <%
                                                int currentYear = java.time.Year.now().getValue();
                                                for (int i = currentYear; i >= 2015; i--) {
                                            %>
                                            <option value="<%= i %>"><%= i %></option>
                                            <%}%>

                                        </select>
                                    </div>


                                    <!-- Hidden action -->
                                    <input type="hidden" name="action" value="search"/>

                                    <!-- Buttons -->
                                    <div class="form-group d-flex gap-2 align-items-end">
                                        <button type="submit" class="btn btn-primary">Search</button>
                                        <a class="btn btn-secondary" href="${pageContext.request.contextPath}/manager/review">Reset</a>
                                    </div>
                                </form>

                            </div>
                        </div>

                        <!--table review-->
                        <div class="table-container">
                            <table class="table align-middle bg-white">
                                <thead class="table-light">
                                    <tr>
                                        <th scope="col">OrderID</th>
                                        <th scope="col">Full Name</th>
                                        <th scope="col">Evaluate</th>
                                        <th scope="col">Feedback</th>
                                        <th scope="col">Date</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:if test="${empty requestScope.list}">
                                        <tr>
                                            <td colspan="6" class="text-center">Không tìm thấy review.</td>
                                        </tr>
                                    </c:if>
                                    <c:forEach var="r" items="${requestScope.list}">
                                        <tr>
                                            <td>${r.bookingDetail.booking.bookingId}</td>
                                            <td>${r.customerAccount.customer.fullName}</td>
                                            <td>
                                                <c:forEach var="i" begin="1" end="${r.rating}">
                                                    <span style="color: orange">&#9733;</span>
                                                </c:forEach>
                                            </td>
                                            <td >
                                                <button class="btn btn-sm btn-outline-info me-1"
                                                        data-feedback="${r.feedBack}"
                                                        onclick="viewFeedback(this.getAttribute('data-feedback'))">
                                                    <i class="bi bi-eye"></i>
                                                </button>
                                            </td>
                                            <td>${r.date}</td>
                                        </tr>
                                    </c:forEach>

                                </tbody>
                            </table>
                            <!-- Nút phân trang -->
                            <div class="pagination-container mt-3 text-center">
                                <c:if test="${totalPages > 1}">
                                    <nav>
                                        <ul class="pagination justify-content-center">

                                            <!-- First Page -->
                                            <c:if test="${currentPage > 1}">
                                                <li class="page-item">
                                                    <form action="${pageContext.request.contextPath}/manager/review" method="post" style="display:inline;">
                                                        <input type="hidden" name="action" value="search"/>
                                                        <input type="hidden" name="starFilter" value="${param.starFilter}"/>
                                                        <input type="hidden" name="date" value="${param.date}"/>
                                                        <input type="hidden" name="page" value="1"/>
                                                        <button type="submit" class="page-link">&laquo;</button>
                                                    </form>
                                                </li>
                                            </c:if>

                                            <!-- Dấu ... trước -->
                                            <c:if test="${currentPage > 3}">
                                                <li class="page-item disabled"><span class="page-link">...</span></li>
                                                </c:if>

                                            <!-- Các nút trang -->
                                            <c:forEach var="i" begin="1" end="${totalPages}">
                                                <c:if test="${i >= currentPage - 2 && i <= currentPage + 2}">
                                                    <li class="page-item ${i == currentPage ? 'active' : ''}">
                                                        <form action="${pageContext.request.contextPath}/manager/review" method="post" style="display:inline;">
                                                            <input type="hidden" name="action" value="search"/>
                                                            <input type="hidden" name="starFilter" value="${param.starFilter}"/>
                                                            <input type="hidden" name="date" value="${param.date}"/>
                                                            <input type="hidden" name="page" value="${i}"/>
                                                            <button type="submit" class="page-link">${i}</button>
                                                        </form>
                                                    </li>
                                                </c:if>
                                            </c:forEach>

                                            <!-- Dấu ... sau -->
                                            <c:if test="${currentPage < totalPages - 2}">
                                                <li class="page-item disabled"><span class="page-link">...</span></li>
                                                </c:if>

                                            <!-- Last Page -->
                                            <c:if test="${currentPage < totalPages}">
                                                <li class="page-item">
                                                    <form action="${pageContext.request.contextPath}/manager/review" method="post" style="display:inline;">
                                                        <input type="hidden" name="action" value="search"/>
                                                        <input type="hidden" name="starFilter" value="${param.starFilter}"/>
                                                        <input type="hidden" name="date" value="${param.date}"/>
                                                        <input type="hidden" name="page" value="${totalPages}"/>
                                                        <button type="submit" class="page-link">&raquo;</button>
                                                    </form>
                                                </li>
                                            </c:if>

                                        </ul>
                                    </nav>
                                </c:if>
                            </div>


                        </div>

                        <!--view feedback model-->
                        <div class="modal fade" id="viewFeedbackModal" tabindex="-1" aria-labelledby="viewFeedbackModalLabel" aria-hidden="true">
                            <div class="modal-dialog">
                                <div class="modal-content">
                                    <div class="modal-header">
                                        <h5 class="modal-title">Feedback Detail</h5>
                                        <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                                    </div>
                                    <div class="modal-body">
                                        <p id="viewfeedback">No feedback</p>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

            </div>
        </div>
        <script>

            document.querySelector("form").addEventListener("submit", function (e) {
                const month = document.getElementById("month").value;
                const year = document.getElementById("year").value;

                if ((month && !year) || (!month && year)) {
                    alert("Vui lòng chọn cả tháng và năm nếu bạn muốn lọc theo thời gian.");
                    e.preventDefault();
                }
            });

            function viewFeedback(feedback) {
                document.getElementById('viewfeedback').innerText = feedback;
                new bootstrap.Modal(document.getElementById('viewFeedbackModal')).show();
            }
        </script>

    </body>
    <%--script for dashbord--%>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js"></script>
    <script src="${pageContext.request.contextPath}/Js/navDashboardJs.js"></script>
    <script src="${pageContext.request.contextPath}/Js/userProfileJs.js"></script>
    <%--write more in following--%>
</html>
