
<%-- 
    Document   : TypeRoom
    Created on : May 31, 2025, 11:40:30 PM
    Author     : Hai Long
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.6/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-4Q6Gf2aSP4eDXB8Miphtr37CMZZQ5oXLH2yaXMJ2w8e2ZtHTl7GptT4jmndRuHDT" crossorigin="anonymous">
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.1/font/bootstrap-icons.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/Css/navDashboardStyle.css"/>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/Css/mainDashboardStyle.css" />
        <link rel="stylesheet" href="${pageContext.request.contextPath}/Css/dashboardStyle.css" />
        <link rel="stylesheet" href="${pageContext.request.contextPath}/Css/TypeRoom.css"/>
    </head>
    <body>
        <div class="containerBox">
            <jsp:include page="leftNav.jsp"/>
            <div class="right-section">
                <jsp:include page="topNav.jsp"/>
                <div class="main-content">

                    <div class="container-fluid p-4">
                        <ul class="nav nav-tabs mb-3">
                            <li class="nav-item">
                                <a class="nav-link active" href="#">Management Type Room</a>
                            </li>
                        </ul>

                        <div class="d-flex justify-content-between align-items-center mb-3">
                            <div class="d-flex gap-2">
                                <button onclick="resetFormAdd()" class="btn btn-success" data-bs-toggle="modal" data-bs-target="#addModal">Add Type Room</button>
                            </div>
                            <!-- Modal -->
                            <div class="modal fade" id="addModal" tabindex="-1" aria-labelledby="addModal" aria-hidden="true">
                                <form action="${pageContext.request.contextPath}/manager/types" method="post">
                                    <div class="modal-dialog">
                                        <div class="modal-content">
                                            <div class="modal-header">
                                                <h1 class="modal-title fs-5" id="exampleModalLabel">Modal title</h1>
                                                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                                            </div>
                                            <div class="modal-body">
                                                <div class="row g-3">
                                                    <div class="col md-6">
                                                        <label for="typeName" class="form-label">Type room's name</label>
                                                        <input class="form-control" 
                                                               value="${param.typeName}" 
                                                               type="text" 
                                                               name="typeName"
                                                               required>
                                                        <c:if test="${not empty nameError}">
                                                            <p class="alert alert-danger">${nameError}</p>
                                                        </c:if>
                                                        <c:if test="${not empty nameExistedError}">
                                                            <p class="alert alert-danger">${nameExistedError}</p>
                                                        </c:if>
                                                    </div>
                                                    <div class="col md-6">
                                                        <label for="price" class="form-label">Type room's price</label>
                                                        <input class="form-control" value="${param.price}" type="text" name="price" required>
                                                        <c:if test="${not empty priceError}">
                                                            <p class="alert alert-danger">${priceError}</p>
                                                        </c:if>
                                                    </div>

                                                </div>
                                                <div class="row g-3">
                                                    <label for="typeName" class="form-label">Type room's description</label>
                                                    <textarea  class="form-control" name="typeDesc" rows="4">
                                                        ${param.typesDesc}
                                                    </textarea>
                                                </div>
                                                <c:if test="${not empty addSuccess}">
                                                    <p class="alert alert-success">${addSuccess}</p>
                                                </c:if>
                                            </div>
                                            <div class="modal-footer">
                                                <input type="hidden" name="service" value="addTypeRoom">
                                                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                                                <button type="submit" class="btn btn-primary">Save changes</button>
                                            </div>
                                        </div>
                                    </div>
                                </form>
                            </div>

                            <form method="get" action="${pageContext.request.contextPath}/manager/types" class="d-flex gap-2">
                                <input type="text" name="key" value="${param.key}" class="form-control search-input" placeholder="Search" />

                                <button type="submit" class="btn btn-primary">Filter</button>
                            </form>
                        </div>

                        <c:if test="${not empty error}">
                            <div class="alert alert-danger mt-3">${error}</div>
                        </c:if>

                        <div class="table-container">
                            <table class="table align-middle">
                                <thead class="table-light">
                                    <tr>
                                        <th scope="col">Type Room's Name</th>
                                        <th scope="col">Price</th>
                                        <th scope="col">Description</th>
                                        <th scope="col">Service</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="trl" items="${requestScope.typeRoomList}">
                                        <tr>
                                            <td><c:out value="${trl.typeName}" default="-"/></td>
                                            <td><c:out value="${trl.price}" default="-"/></td>
                                            <td class="viewTypeRoom">
                                                <!-- View typeroom Modal -->
                                                <button onclick="clearMessage(${trl.typeId})" class="btn btn-sm btn-outline-info me-1" data-bs-toggle="modal" data-bs-target="#viewTypeRoomModal_${trl.typeId}">
                                                    <i class="bi bi-eye"></i> View 
                                                </button>
                                                <!--modal view typeroom-->
                                                <div class="modal fade" id="viewTypeRoomModal_${trl.typeId}" tabindex="-1" aria-labelledby="viewTypeRoomModalLabel_${trl.typeId}" aria-hidden="true">
                                                    <div class="modal-dialog modal-lg">
                                                        <div class="modal-content">
                                                            <form action="${pageContext.request.contextPath}/manager/types" method="post">
                                                                <div class="modal-header">
                                                                    <h5 class="modal-title" id="viewEmployeeModalLabel_${trl.typeId}">${trl.typeName} Description</h5>

                                                                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                                                                </div>
                                                                <div class="modal-body">
                                                                    <!--<input type="type" name="typeDesc" value="${trl.description}" class="form-control">-->
                                                                    <textarea name="typeDesc" class="form-control" spellcheck="false" rows="4">${trl.description}</textarea>
                                                                    <c:if test="${not empty updateMessageDesc and updateDesc == trl.typeId}">
                                                                        <p class="alert alert-success">${updateMessageDesc}</p>
                                                                    </c:if>
                                                                    <c:if test="${not empty descError and updateDesc == trl.typeId}">
                                                                        <p class="alert alert-danger">${descError}</p>
                                                                    </c:if>
                                                                </div>
                                                                <div class="modal-footer">
                                                                    <input type="hidden" name="typeRoomId" value="${trl.typeId}">
                                                                    <input type="hidden" name="page" value="${currentPage}"}>
                                                                    <input type="hidden" name="key" value="${key}"}>
                                                                    <input type="hidden" name="service" value="updateDesc">
                                                                    <button type="submit" class="btn btn-success">Update Description</button>
                                                                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                                                                </div>
                                                            </form>
                                                        </div>
                                                    </div>
                                                </div>
                                            </td>


                                            <td class="viewService">
                                                <button class="btn btn-sm btn-outline-info me-1" data-bs-toggle="modal" data-bs-target="#serviceModal_${trl.typeId}">
                                                    <i class="bi bi-eye"></i> View 
                                                </button>
                                                <form action="${pageContext.request.contextPath}/manager/types" method="post">
                                                    <div class="modal fade services-modal" id="serviceModal_${trl.typeId}" tabindex="-1" aria-labelledby="servicesModalLabel" aria-hidden="true">
                                                        <div class="modal-dialog modal-dialog-scrollable modal-lg">
                                                            <div class="modal-content">
                                                                <div class="modal-header">
                                                                    <h5 class="modal-title" id="servicesModalLabel">
                                                                        <i class="fas fa-concierge-bell me-2"></i>
                                                                        ${trl.typeName} Services
                                                                        <span class="services-count">${trl.services.size()} Services</span>
                                                                    </h5>
                                                                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                                                                </div>
                                                                <div class="modal-body">
                                                                    <c:choose>
                                                                        <c:when test="${trl.services.size() == 0}">
                                                                            <h5 class="modal-title" id="servicesModalLabel">
                                                                                Không có dịch vụ đi kèm với ${trl.typeName}
                                                                            </h5> 
                                                                        </c:when>
                                                                        <c:otherwise>
                                                                            <h5 class="modal-title" id="servicesModalLabel">
                                                                                Dịch vụ đi kèm với ${trl.typeName}
                                                                            </h5> 
                                                                        </c:otherwise>
                                                                    </c:choose>
                                                                    <c:if test="${not empty updateMessageService and typeId == trl.typeId}">
                                                                        <p class="alert alert-success">${updateMessageService}</p>
                                                                    </c:if>
                                                                    <c:if test="${not empty noChangeServiceError and typeId == trl.typeId}">
                                                                        <p class="alert alert-danger">${noChangeServiceError}</p>
                                                                    </c:if>
                                                                    <div class="services-grid services">
                                                                        <c:forEach var="rns" items="${trl.services}">
                                                                            <!-- WiFi Service -->
                                                                            <div class="service-card wifi">
                                                                                <div class="service-icon">
                                                                                    <i class="fas fa-wifi"></i>
                                                                                </div>
                                                                                <div class="service-name">${rns.service.serviceName}</div>
                                                                                <c:if test="${rns.service.price != 0}">
                                                                                    <div class="service-price">${rns.service.price} VNĐ</div>
                                                                                </c:if>
                                                                                <c:if test="${rns.service.price == 0}">
                                                                                    <div class="service-price">Free</div>
                                                                                </c:if>
                                                                                <div class="add-service">
                                                                                    <input name="serviceItem" class="form-check-input mt-0" type="checkbox" value="${rns.service.serviceId}" checked aria-label="Checkbox for following text input">
                                                                                </div>
                                                                            </div>  
                                                                        </c:forEach>

                                                                    </div>

                                                                    <h5 class="modal-title" id="servicesModalLabel">
                                                                        Các dịch vụ khác
                                                                    </h5> 
                                                                    <div class="services-grid other-services">
                                                                        <c:forEach var="os" items="${trl.otherServices}">
                                                                            <!-- WiFi Service -->
                                                                            <div class="service-card wifi">
                                                                                <div class="service-icon">
                                                                                    <i class="fas fa-wifi"></i>
                                                                                </div>
                                                                                <div class="service-name">${os.serviceName}</div>
                                                                                <c:if test="${os.price != 0}">
                                                                                    <div class="service-price">${os.price} VNĐ</div>
                                                                                </c:if>
                                                                                <c:if test="${os.price == 0}">
                                                                                    <div class="service-price">Free</div>
                                                                                </c:if>
                                                                                <div class="add-service">
                                                                                    <input name="serviceItem" class="form-check-input mt-0" type="checkbox" value="${os.serviceId}" aria-label="Checkbox for following text input">
                                                                                </div>

                                                                            </div>  
                                                                        </c:forEach>
                                                                    </div>
                                                                    <!-- Empty State Example -->
                                                                    <!--
                                                                    <div class="no-services">
                                                                        <i class="fas fa-inbox"></i>
                                                                        <h5>No Services Available</h5>
                                                                        <p>This room type doesn't have any additional services configured.</p>
                                                                    </div>
                                                                    -->
                                                                </div>
                                                                <div class="modal-footer">

                                                                    <input type="hidden" name="typeId" value="${trl.typeId}">
                                                                    <input type="hidden" name="service" value="addServiceItem">
                                                                    <input type="hidden" name="key" value="${key}" />
                                                                    <input type="hidden" name="page" value="${currentPage}"}>
                                                                    <button type="submit" class="btn btn-primary" data-bs-toggle="modal">
                                                                        <i class="fas fa-plus me-1"></i> Add Service
                                                                    </button>

                                                                    <button onclick="clearMessage(${trl.typeId})" type="button" class="btn btn-secondary" data-bs-dismiss="modal">
                                                                        <i class="fas fa-times me-1"></i> Close
                                                                    </button>
                                                                </div>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </form>
                                            </td>
                                            <td>
                                                <!-- Edit Employee Modal -->
                                                <button onclick="resetFormData(${trl.typeId})" class="btn btn-sm btn-outline-primary me-1" data-bs-toggle="modal" data-bs-target="#editTypeRoomModal_${trl.typeId}">
                                                    <i class="bi bi-pencil"></i> Edit
                                                </button>

                                                <div class="modal fade" id="editTypeRoomModal_${trl.typeId}" tabindex="-1" aria-labelledby="editTypeRoomModalLabel_${trl.typeId}" aria-hidden="true">
                                                    <div class="modal-dialog modal-lg">
                                                        <div class="modal-content">
                                                            <form method="post" action="${pageContext.request.contextPath}/manager/types">
                                                                <input type="hidden" name="service" value="updatePriceName" />

                                                                <div class="modal-header">
                                                                    <h5 class="modal-title" id="editTypeRoomModalLabel_${trl.typeId}">Edit type room - ${trl.typeName}</h5>
                                                                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                                                                </div>
                                                                <div class="modal-body">

                                                                    <div class="row g-3">
                                                                        <div class="col-md-6">
                                                                            <label for="typeNameEdit_${trl.typeId}" class="form-label">Type room's name</label>
                                                                            <input spellcheck="false" 
                                                                                   type="text" 
                                                                                   id="typeRoomEdit_${trl.typeId}" 
                                                                                   name="typeName"
                                                                                   data-original-id="${trl.typeId}"
                                                                                   data-original-value="${trl.typeName}"
                                                                                   value="${param.typeName != null ? param.typeName : trl.typeName}" 
                                                                                   class="form-control" required />
                                                                            <c:if test="${not empty nameError and updateNameAndPrice == trl.typeId}">
                                                                                <p class="alert alert-danger">${nameError}</p>
                                                                            </c:if>
                                                                        </div>

                                                                        <div class="col-md-6">
                                                                            <label for="typeNameEdit_${trl.typeId}" class="form-label">Price</label>
                                                                            <input spellcheck="false" 
                                                                                   type="text" 
                                                                                   id="typeRoomEdit_${trl.typeId}" 
                                                                                   name="price" 
                                                                                   value="${param.price != null ? param.price : trl.price}" 
                                                                                   data-original-value="${trl.price}"
                                                                                   class="form-control" required />
                                                                            <c:if test="${not empty priceError and updateNameAndPrice == trl.typeId}">
                                                                                <p class="alert alert-danger">${priceError}</p>
                                                                            </c:if>
                                                                        </div>
                                                                        <c:if test="${not empty noChangeError and updateNameAndPrice == trl.typeId}"> 
                                                                            <p class="alert alert-danger">${noChangeError}</p>
                                                                        </c:if>


                                                                    </div>
                                                                    <c:if test="${not empty updateMessageNameAndPrice and updateNameAndPrice == trl.typeId}">
                                                                        <p class="alert alert-success">${updateMessageNameAndPrice}</p>
                                                                    </c:if>
                                                                </div>
                                                                <div class="modal-footer">
                                                                    <input type="hidden" name="typeRoomId" value="${trl.typeId}" />
                                                                    <input type="hidden" name="key" value="${key}" />
                                                                    <input type="hidden" name="page" value="${currentPage}"}>
                                                                    <button type="submit" class="btn btn-success">Update</button>
                                                                    <button onclick="clearMessage(${trl.typeId})" type="button" class="btn btn-secondary" data-bs-dismiss="modal">
                                                                        <i class="fas fa-times me-1"></i> Close
                                                                    </button>
                                                                </div>
                                                            </form>
                                                        </div>
                                                    </div>
                                                </div>

                                            </td>
                                            <td>
                                                <!-- Delete Employee Modal -->
                                                <button class="btn btn-sm btn-outline-danger" data-bs-toggle="modal" data-bs-target="#deleteModal_${trl.typeId}">
                                                    <i class="bi bi-trash"></i> Delete

                                                </button>
                                                <div class="modal fade" id="deleteModal_${trl.typeId}" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
                                                    <div class="modal-dialog">
                                                        <div class="modal-content">
                                                            <div class="modal-header">
                                                                <h1 class="modal-title fs-5" id="exampleModalLabel">Are you sure to delete</h1>
                                                                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                                                            </div>
                                                            <div class="modal-body">
                                                                <p alert alert-primary>Bạn có chắc muốn xóa ${trl.typeName}</p>
                                                            </div>
                                                            <div class="modal-footer">
                                                                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                                                                <input type="hidden" name="typeId" value="${trl.typeId}">
                                                                <input type="hidden" name="service" value="deleleTypeRoom">
                                                                <button type="button" class="btn btn-primary">Delete</button>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </div>
                    </div>
                    <nav aria-label="Pagination">
                        <ul class="pagination pagination-danger">
                            <c:choose>
                                <c:when test="${key != null && !key.isEmpty()}">
                                    <li class="page-item ${currentPage == 1 ? 'disabled' : ''}">
                                        <a href="?page=${currentPage - 1}&key=${key}" class="page-link">Previous</a>
                                    </li>
                                </c:when>
                                <c:otherwise>
                                    <li class="page-item ${currentPage == 1 ? 'disabled' : ''}">
                                        <a href="?page=${currentPage - 1}" class="page-link">Previous</a>
                                    </li>
                                </c:otherwise>
                            </c:choose>
                            <c:forEach var="i" begin="1" end="${endPage}">
                                <c:choose>
                                    <c:when test="${key != null && !key.isEmpty()}">
                                        <li class="page-item ${i == currentPage ? 'active' : ''}">
                                            <a class="page-link" href="?page=${i}&key=${key}">${i}</a>
                                        </li>

                                    </c:when>
                                    <c:otherwise>
                                        <li class="page-item ${i == currentPage ? 'active' : ''}">
                                            <a class="page-link" href="?page=${i}">${i}</a>
                                        </li>
                                    </c:otherwise>
                                </c:choose>

                            </c:forEach>
                            <c:choose>
                                <c:when test="${key != null && !key.isEmpty()}">

                                    <li class="page-item ${currentPage == endPage ? 'disabled' : ''}">
                                        <a href="?page=${currentPage + 1}&key=${key}" class="page-link">Next</a>
                                    </li>
                                </c:when>
                                <c:otherwise>
                                    <li class="page-item ${currentPage == endPage ? 'disabled' : ''}">
                                        <a href="?page=${currentPage + 1}" class="page-link">Next</a>
                                    </li>
                                </c:otherwise>
                            </c:choose>
                        </ul>
                    </nav>
                </div>
            </div>
        </div>

        <script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.9.2/dist/umd/popper.min.js" integrity="sha384-IQsoLXl5PILFhosVNubq5LC7Qb9DXgDA9i+tQ8Zj3iwWAwPtgFTxbJ8NT4GN1R8p" crossorigin="anonymous"></script>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.min.js" integrity="sha384-cVKIPhGWiC2Al4u+LWgxfKTRIcfu0JTxR+EQDz/bgldoEyl4H0zUF0QKbrJ0EcQF" crossorigin="anonymous"></script>
        <c:if test="${not empty showModalDesc}">
            <script>

                                                                        window.addEventListener('load', function () {
                                                                            var modal = new bootstrap.Modal(document.getElementById('viewTypeRoomModal_${showModalDesc}'));
                                                                            modal.show();
                <c:if test="${not empty updateMessageDesc}">
                                                                            console.log(1);
                                                                            setTimeout(function () {

                                                                                clearMessage(${showModalDesc});


                                                                                modal.hide();
                                                                            }, 2000);
                </c:if>

                                                                        });


            </script>
        </c:if>
        <c:if test="${not empty showModalEdit}">
            <script>

                window.addEventListener('load', function () {
                    var modal = new bootstrap.Modal(document.getElementById('editTypeRoomModal_${showModalEdit}'));
                    modal.show();
                <c:if test="${not empty updateMessageNameAndPrice}">
                    setTimeout(function () {

                        clearMessage('${showModalEdit}');


                        modal.hide();
                    }, 2000);
                </c:if>


                });

                function clearMessage(typeId) {
                    // Ẩn message trong modal
                    var messageElement = document.querySelectorAll('#editTypeRoomModal_' + typeId + ' .modal-body p');
                    console.log(messageElement);
                    // Hoặc xóa hoàn toàn
                    if (messageElement) {
                        messageElement.forEach(function (element) {
                            element.remove();
                        });
                    }
                }
                ;


            </script>
        </c:if>
        <c:if test="${not empty showModalService}">
            <script>

                window.addEventListener('load', function () {
                    var modal = new bootstrap.Modal(document.getElementById('serviceModal_${showModalService}'));
                    modal.show();
                <c:if test="${not empty updateMessageService}">
                    setTimeout(function () {

                        clearMessage('${showModalService}');


                        modal.hide();
                    }, 2000);
                </c:if>


                });

                function clearMessage(typeId) {
                    // Ẩn message trong modal
                    var messageElement = document.querySelectorAll('#serviceModal_' + typeId + ' .modal-body p');
                    console.log(messageElement);
                    // Hoặc xóa hoàn toàn
                    if (messageElement) {
                        messageElement.forEach(function (element) {
                            element.remove();
                        });
                    }
                }
                ;
            </script>
        </c:if>
        <c:if test="${not empty showModalAdd}">
            <script>

                window.addEventListener('load', function () {
                    var modal = new bootstrap.Modal(document.getElementById('addModal'));
                    modal.show();
                <c:if test="${not empty addSuccess}">
                    setTimeout(function () {

                        clearMessage();


                        modal.hide();
                    }, 2000);
                </c:if>


                });

                function clearMessage() {
                    // Ẩn message trong modal
                    var messageElement = document.querySelectorAll('#addModal .modal-body p');
                    console.log(messageElement);
                    // Hoặc xóa hoàn toàn
                    if (messageElement) {
                        messageElement.forEach(function (element) {
                            element.remove();
                        });
                    }
                }
                ;


            </script>
        </c:if>
        <script>
            function resetFormAdd() {
                var modal = document.getElementById('addModal');
                var form = modal.querySelector('form');

                if (form) {
                    // Reset type name input
                    var typeNameInput = form.querySelector('input[name="typeName"]');
                    if (typeNameInput) {
                        typeNameInput.value = '';
                    }

                    // Reset price input
                    var priceInput = form.querySelector('input[name="price"]');
                    if (priceInput) {
                        priceInput.value = '';
                    }

                    // Reset description textarea
                    var descTextarea = form.querySelector('textarea[name="typeDesc"]');
                    if (descTextarea) {
                        descTextarea.value = '';
                    }

                    // Clear all error/success messages
                    var messageElements = modal.querySelectorAll('.modal-body p.alert');
                    messageElements.forEach(function (element) {
                        element.remove();
                    });
                }

                console.log('Form reset completed');
            }

            function resetFormData(typeId) {

                setTimeout(function () {
                    var modal = document.getElementById('editTypeRoomModal_' + typeId);
                    var form = modal.querySelector('form');

                    if (form) {

                        var typeNameInput = form.querySelector('input[name="typeName"]');
                        if (typeNameInput) {
                            var originalTypeName = typeNameInput.getAttribute('data-original-value');
                            typeNameInput.value = originalTypeName;
                            console.log('Reset type name to original:', originalTypeName);
                        }


                        var priceInput = form.querySelector('input[name="price"]');
                        if (priceInput) {
                            var originalPrice = priceInput.getAttribute('data-original-value');
                            priceInput.value = originalPrice;
                            console.log('Reset price to original:', originalPrice);
                        }


                        var messageElements = modal.querySelectorAll('.modal-body p.alert');
                        messageElements.forEach(function (element) {
                            element.remove();
                        });
                    }
                }, 100);
            }
            function clearMessage(typeId) {
                // Ẩn message trong modal
                var messageElement = document.querySelectorAll('#viewTypeRoomModal_' + typeId + ' .modal-body p');


                if (messageElement) {
                    messageElement.forEach(function (element) {
                        console.log(element.value);
                        element.remove();
                    });
                }
            }
            ;
        </script>

    </body>
</html>
