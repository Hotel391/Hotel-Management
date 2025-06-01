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
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/cssAdmin/ViewReview.css">

    </head>
    <body>
        <div class="room-table-container">
            <table class="room-table">
                <thead>
                    <tr>
                        <th class="col-orderid">OrderID</th>
                        <th class="col-name">Full Name</th>
                        <th class="col-rating">Evaluate</th>
                        <th class="col-feedback">Feedback</th>
                        <th class="col-date">Date</th>
                    </tr>
                </thead>
                <tbody>

                    <c:forEach var="r" items="${requestScope.list}">
                        <tr>
                            <td class="col-orderid">${r.reviewId}</td>
                            <td class="col-name">${r.customerAccount.customer.fullName}</td>
                            <td class="col-rating">
                                <c:forEach var="i" begin="1" end="${r.rating}">
                                    <span class="star">&#9733;</span>
                                </c:forEach>
                            </td>
                            <td class="col-feedback">
                                ${r.feedBack}
                            </td>
                            <td class="col-date">${r.date}</td>
                        </tr>
                    </c:forEach>

                </tbody>
            </table>
        </div>
    </body>
</html>
