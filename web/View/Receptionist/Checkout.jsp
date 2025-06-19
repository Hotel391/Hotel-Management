<%-- 
    Document   : Checkout
    Created on : Jun 19, 2025, 7:45:36 AM
    Author     : Hai Long
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css" rel="stylesheet"
              integrity="sha384-EVSTQN3/azprG1Anm3QDgpJLIm9Nao0Yz1ztcQTwFspd3yD65VohhpuuCOmLASjC"
              crossorigin="anonymous">
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.1/font/bootstrap-icons.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/Css/navDashboardStyle.css" />
        <link rel="stylesheet" href="${pageContext.request.contextPath}/Css/dashboardStyle.css" />
        <link rel="stylesheet" href="${pageContext.request.contextPath}/Css/Checkout.css"/>
        <%--another in the following--%>
    </head>
    <body>
        <div class="containerBox">
            <jsp:include page="leftNavReceptionist.jsp" /> 
            <div class="right-section">
                <c:set var="title" value="Checkout" scope="request"/>
                <jsp:include page="topNavReceptionist.jsp" />
                <div class="main-content">

                    <div class="checkout-content">
                        <div class="customer-info">
                            <form action="">
                                <h2 class="info-title">Thông tin khách hàng</h2>
                                <div class="gender">
                                    <div class="gender_content">
                                        <input name="gender" value="Male" ${param.gender.equals("Male") ? 'checked' : ''} type="radio" />
                                        <label for="male">Anh</label>
                                    </div>

                                    <div class="gender_content">
                                        <input name="gender" value="Female" ${param.gender.equals("Female") ? 'checked' : ''} type="radio" />
                                        <label for="female">Chị</label>
                                    </div>

                                </div>

                                <c:if test="${not empty genderError}">
                                    <p class="error">${genderError}</p>
                                </c:if>
                                <div class="info_input">
                                    <div class="form_row">
                                        <div class="checkout_detail">
                                            <label style="display: none" for="fullname">Họ tên</label>
                                            <input placeholder="Họ tên *" 
                                                   type="text" 
                                                   name="fullName" 
                                                   value="${param.fullName}" required/>
                                            
                                                <c:if test="${not empty nameError}">
                                                    <p class="error">${nameError}</p>
                                                </c:if>
                                                
                                        </div>
                                        <div class="checkout_detail">
                                            <label style="display: none" for="phone">SĐT</label>
                                            <input placeholder="SĐT *" 
                                                   type="number" id="phone" 
                                                   name="phone" 
                                                   value="${param.phone}"
                                                   required/>
                                            <c:if test="${not empty phoneError}">
                                                <p class="error">${phoneError}</p>
                                            </c:if>
                                        </div>
                                    </div>

                                    <div class="form_row">
                                        <div class="checkout_detail">
                                            <label style="display: none" for="cccd">CCCD</label>
                                            <input type="text" 
                                                   id="cccd" 
                                                   name="cccd" 
                                                   placeholder="CCCD *" 
                                                   value="${param.cccd}"
                                                   required/>
                                            <c:if test="${not empty cccdError}">
                                                <p class="error">${cccdError}</p>
                                            </c:if>
                                        </div>


                                        <div class="checkout_detail">
                                            <label style="display: none" for="email">Email:</label>
                                            <input type="text" 
                                                   id="email" 
                                                   name="email" 
                                                   placeholder="Email *" 
                                                   value="${param.email}"
                                                   required/>
                                            <c:if test="${not empty emailError}">
                                                <p class="error">${emailError}</p>
                                            </c:if>
                                        </div>
                                    </div>
                                </div>
                                <div class="info_input">

                                    <label style="display: none" for="Address">Address</label>
                                    <input type="text" 
                                           id="address" 
                                           name="address" 
                                           placeholder="Địa chỉ *" 
                                           value="${param.address}"
                                           required/>


                                </div>
                                <c:if test="${not empty addressError}">
                                    <p class="error">${addressError}</p>
                                </c:if>
                                <div class="btn-submit">
                                    <input type="hidden" name="service" value="book">
                                    <button class="btn btn-danger">Đặt phòng</button>
                                </div>
                            </form>
                        </div>
                    </div>

                </div>
            </div>
        </div>        
        <%--script for dashboard--%>

        <script src="${pageContext.request.contextPath}/Js/navDashboardJs.js"></script>
        <script src="${pageContext.request.contextPath}/Js/userProfileJs.js"></script>
        <%--another in following--%>
        <script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.9.2/dist/umd/popper.min.js" integrity="sha384-IQsoLXl5PILFhosVNubq5LC7Qb9DXgDA9i+tQ8Zj3iwWAwPtgFTxbJ8NT4GN1R8p" crossorigin="anonymous"></script>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.min.js" integrity="sha384-cVKIPhGWiC2Al4u+LWgxfKTRIcfu0JTxR+EQDz/bgldoEyl4H0zUF0QKbrJ0EcQF" crossorigin="anonymous"></script>
    </body>
</html>
