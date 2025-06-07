<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Profile</title>
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.1/font/bootstrap-icons.css">
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css" rel="stylesheet"
              integrity="sha384-EVSTQN3/azprG1Anm3QDgpJLIm9Nao0Yz1ztcQTwFspd3yD65VohhpuuCOmLASjC"
              crossorigin="anonymous">
    </head>
    <body class="bg-light">
        <div class="container mt-5">
            <div class="card shadow-sm">
                <div class="card-header bg-primary text-white">
                    <h4 class="mb-0">Customer Profile</h4>
                </div>
                <div class="card-body">
                    <dl class="row">
                        <dt class="col-sm-3">Username:</dt>
                        <dd class="col-sm-9">${customerAccount.username}</dd>

                        <dt class="col-sm-3">Full Name:</dt>
                        <dd class="col-sm-9">${customerAccount.customer.fullName}</dd>

                        <dt class="col-sm-3">Email:</dt>
                        <dd class="col-sm-9">${customerAccount.customer.email}</dd>

                        <dt class="col-sm-3">Phone Number:</dt>
                        <dd class="col-sm-9">${customerAccount.customer.phoneNumber}</dd>

                        <dt class="col-sm-3">Gender:</dt>
                        <dd class="col-sm-9">
                            <c:choose>
                                <c:when test="${customerAccount.customer.gender}">Male</c:when>
                                <c:otherwise>Female</c:otherwise>
                            </c:choose>
                        </dd>

                        <dt class="col-sm-3">CCCD:</dt>
                        <dd class="col-sm-9">${customerAccount.customer.CCCD}</dd>

                        <dt class="col-sm-3">Activate:</dt>
                        <dd class="col-sm-9">
                            <c:choose>
                                <c:when test="${customerAccount.customer.activate}">
                                    <i class="bi bi-check-circle-fill text-success"></i> Active
                                </c:when>
                                <c:otherwise>
                                    <i class="bi bi-x-circle-fill text-danger"></i> Inactive
                                </c:otherwise>
                            </c:choose>
                        </dd>

                        <dt class="col-sm-3">Role:</dt>
                        <dd class="col-sm-9">${customerAccount.customer.role.roleName}</dd>
                    </dl>

                    <!--back home,update profile-->
                    <div class="mt-4 d-flex gap-2">
                        <a href="${pageContext.request.contextPath}/customer/home" class="btn btn-secondary">
                            <i class="bi bi-arrow-left-circle"></i> Back to Home
                        </a>

                        <a href="${pageContext.request.contextPath}/customer/customerProfile?service=update&username=${customerAccount.username}" class="btn btn-primary">
                            <i class="bi bi-pencil-square"></i> Update Profile
                        </a>
                    </div>
                </div>
            </div>
        </div>

<!--        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js"></script>-->
    </body>
</html>
