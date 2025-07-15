<%-- 
    Document   : ExistedCustomer
    Created on : Jun 19, 2025, 3:11:26 PM
    Author     : Hai Long
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<div class="checkout-content">
    <div class="customer-info">
        <form action="${pageContext.request.contextPath}/receptionist/checkout" method="post">
            <h2 class="info-title">Thông tin khách hàng</h2>
            <div class="gender">
                <div class="gender_content">
                    <input name="gender" value="Male" ${ec.gender ? 'checked' : ''} type="radio" required/>
                    <label for="male">Anh</label>
                </div>

                <div class="gender_content">
                    <input name="gender" value="Female" ${!ec.gender ? 'checked' : ''} type="radio" required/>
                    <label for="female">Chị</label>
                </div>

            </div>

            <div class="info_input">
                <div class="form_row">
                    <div class="checkout_detail">
                        <label style="display: none" for="fullname">Họ tên</label>
                        <input placeholder="Họ tên *" 
                               type="text" 
                               name="fullName" 
                               value="${ec.fullName}" required/>

                        <c:if test="${not empty nameError}">
                            <p class="error">${nameError}</p>
                        </c:if>

                    </div>
                    <div class="checkout_detail">
                        <label style="display: none" for="phone">SĐT</label>
                        <input placeholder="SĐT *" 
                               type="number" id="phone" 
                               name="phone" 
                               value="${ec.phoneNumber}"
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
                               value="${ec.CCCD}"
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
                               value="${ec.email}"
                               required/>
                        <c:if test="${not empty emailError}">
                            <p class="error">${emailError}</p>
                        </c:if>
                    </div>
                </div>
                <div class="form_row">
                    <div class="checkout_detail" style="width: 230px;">
                        <select name="paymentMethod" class="form-select w-160" aria-label="Default select example">
                            <option selected value="default">Chọn phương thức</option>
                            <option value="online">Chuyển khoản</option>
                            <option value="offline">Tiền mặt</option>
                        </select>
                        
                    </div>
                    <div class="checkout_detail" style="width: 500px;">
                        <c:if test="${not empty paymentMethodError}">
                            <p class="alert alert-danger">${paymentMethodError}</p>
                        </c:if>
                    </div>
                </div>
            </div>

            <div class="btn-submit">
                <input type="hidden" name="service" value="addExisted">
                <button class="btn btn-danger">Đặt phòng</button>
            </div>
        </form>
    </div>
</div>

