<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Update Profile</title>
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
                <c:set var="title" value="Update profile" scope="request"/>
                <jsp:include page="topNav.jsp" />
                <div class="d-flex justify-content-center align-items-start mt-3" style="min-height: calc(100vh - 60px); padding-top: 10px;">

                    <div class="card shadow text-dark" style="background-color: #fffff; width: 100%; max-width: 700px;">

                        <div class="card-header bg-primary text-white">
                            <h4 class="mb-0">Update Profile</h4>
                        </div>
                        <div class="card-body">
                            <form action="customerProfile?service=update" method="post">
                                <input type="hidden" name="service" value="update"/>
                                <input type="hidden" name="submit" value="submit"/>
                                <input type="hidden" name="username" value="${customerAccount.username}"/>

                                <div class="mb-3">
                                    <label class="form-label">Full Name</label>
                                    <input type="text" class="form-control" name="fullName" value="${customerAccount.customer.fullName}" required>
                                    <c:if test="${not empty fullNameError}">
                                        <div class="text-danger">${fullNameError}</div>
                                    </c:if>
                                </div>

                                <div class="mb-3">
                                    <label class="form-label">Email</label>
                                    <input type="email" class="form-control" name="email" value="${customerAccount.customer.email}" required>
                                    <c:if test="${not empty emailError}">
                                        <div class="text-danger">${emailError}</div>
                                    </c:if>
                                </div>

                                <div class="mb-3">
                                    <label class="form-label">Phone Number</label>
                                    <input type="text" class="form-control" name="phoneNumber" value="${customerAccount.customer.phoneNumber}" required>
                                    <c:if test="${not empty phoneError}">
                                        <div class="text-danger">${phoneError}</div>
                                    </c:if>
                                </div>

                                <div class="mb-3">
                                    <label class="form-label">Gender</label>
                                    <select class="form-select" name="gender">
                                        <option value="true" ${customerAccount.customer.gender ? "selected" : ""}>Male</option>
                                        <option value="false" ${!customerAccount.customer.gender ? "selected" : ""}>Female</option>
                                    </select>
                                </div>

                                <div class="mb-3">
                                    <label class="form-label">CCCD</label>
                                    <input type="text" class="form-control" name="cccd" value="${customerAccount.customer.CCCD}" required>
                                </div>

                                <div class="d-flex gap-2">
                                    <button type="submit" class="btn btn-success">
                                        <i class="bi bi-save"></i> Save Changes
                                    </button>
                                    <a href="${pageContext.request.contextPath}/customer/customerProfile?service=info&username=${customerAccount.username}" class="btn btn-secondary">
                                        <i class="bi bi-arrow-left-circle"></i> Cancel
                                    </a>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </body>
    <%--script for dashbord--%>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js"></script>
    <script src="${pageContext.request.contextPath}/Js/navDashboardJs.js"></script>
    <script src="${pageContext.request.contextPath}/Js/userProfileJs.js"></script>
    <%--write more in following--%>
</html>

