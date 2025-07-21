<%@page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%> 
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page import="models.CustomerAccount"%>
<%@page import="models.Customer"%>

<%
    CustomerAccount ca = (CustomerAccount) request.getAttribute("customerAccount");
    String fullName = (ca != null && ca.getCustomer() != null) ? ca.getCustomer().getFullName() : "A";
    String initial = fullName.length() > 0 ? fullName.substring(0, 1).toUpperCase() : "A";
    request.setAttribute("initialLetter", initial);
    request.setAttribute("activeTab", "profile");
%>

<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Thông tin tài khoản</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
        <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.1/font/bootstrap-icons.css" rel="stylesheet">
        <style>
            .setting-card {
                background-color: #ffffff;
                border: 1px solid #e5eaf0;
                border-radius: 12px;
                padding: 24px;
                box-shadow: 0 2px 6px rgba(0, 0, 0, 0.03);
            }

            .form-label {
                font-weight: 500;
                color: #212b36;
                font-size: 14px;
            }

            .form-control {
                border-radius: 8px;
                font-size: 15px;
                color: #212b36;
            }

        </style>
    </head>
    <body style="background-color: #f5f7fa;">
        <jsp:include page="Header.jsp" />

        <div class="container-xl py-4">
            <div class="row justify-content-center">
                <div class="col-lg-10 col-xl-9">
                    <div class="row">
                        <!-- Sidebar -->
                        <div class="col-md-3">
                            <jsp:include page="SidebarProfile.jsp" />
                        </div>

                        <!-- Content -->
                        <div class="col-md-9">
                            <div class="setting-card">
                                <h5 class="mb-4">Thông tin tài khoản</h5>
                                <form method="post" action="${pageContext.request.contextPath}/customerProfile?service=update&username=${customerAccount.username}">
                                    <div class="mb-3">
                                        <label for="fullName" class="form-label">Họ và tên</label>
                                        <input type="text" class="form-control" id="fullName" name="fullName"
                                               value="${customerAccount.customer.fullName}" readonly>
                                    </div>

                                    <div class="row mb-3">
                                        <div class="col-md-6">
                                            <label for="gender" class="form-label">Giới tính</label>
                                            <input type="text" class="form-control" id="gender" name="gender"
                                                   value="${customerAccount.customer.gender ? 'Nam' : 'Nữ'}" readonly>
                                        </div>
                                        <div class="col-md-6">
                                            <label for="phoneNumber" class="form-label">Số điện thoại</label>
                                            <input type="text" class="form-control" id="phoneNumber" name="phoneNumber"
                                                   value="${customerAccount.customer.phoneNumber}"readonly>
                                        </div>
                                    </div>

                                    <div class="mb-3">
                                        <label class="form-label">Email</label>
                                        <input type="email" class="form-control" value="${customerAccount.customer.email}" readonly>
                                    </div>

                                    <div class="text-end">
                                        <button type="submit" class="btn btn-primary">Chỉnh sửa</button>
                                    </div>
                                </form> 
                            </div>
                        </div>
                    </div> 
                </div> 
            </div> 
        </div> 
    </body>

</html>