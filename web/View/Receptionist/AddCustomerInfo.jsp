<%-- 
    Document   : AddCustomerInfo
    Created on : Jun 19, 2025, 2:36:56 PM
    Author     : Hai Long
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<div class="checkout-content">
    <div class="customer-info">
        <form action="${pageContext.request.contextPath}/receptionist/checkout" method="post">
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
            <c:if test="${not empty addressError}">
                <p class="error">${addressError}</p>
            </c:if>
            <div class="btn-submit">
                <input type="hidden" name="service" value="addNew">
                <button class="btn btn-danger">Đặt phòng</button>
            </div>
        </form>
    </div>
</div>
