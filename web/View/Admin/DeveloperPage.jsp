<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Developer Page</title>
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.1/font/bootstrap-icons.css">
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css" rel="stylesheet"
              integrity="sha384-EVSTQN3/azprG1Anm3QDgpJLIm9Nao0Yz1ztcQTwFspd3yD65VohhpuuCOmLASjC"
              crossorigin="anonymous">
    </head>
    <body class="bg-light">
        <div class="container mt-5">
            <h3 class="mb-4 text-center">All Employee Accounts</h3>
              <p><a href="developerPage?service=add" class="btn btn-primary mb-3">Add New Account Manager <i class="bi bi-person-add"></i></a></p>
            <div class="table-responsive">
                <table class="table table-bordered table-striped table-hover align-middle">
                    <thead class="table-dark">
                        <tr>
                            <th>Employee ID</th>
                            <th>Username</th>
                            <th>Password</th>
                            <th>Registration Date</th>
                            <th>Activate</th>
                            <th>Role Name</th>
                            <th>Delete</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="e" items="${requestScope.list}">
                            <tr>
                                <td>${e.employeeId}</td>
                                <td>${e.username}</td>
                                <td>${e.password}</td>
                                <td>${e.registrationDate}</td>
                                <td>
                                    <c:choose>
                                        <c:when test="${e.activate == true}">
                                            <i class="bi bi-check2-circle text-success fs-5"></i>
                                        </c:when>
                                        <c:otherwise>
                                            <i class="bi bi-x-circle text-danger fs-5"></i>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                                <td>${e.role.roleName}</td>
                                <td>
                                    <a href="developerPage?service=deleteManager&employeeID=${e.employeeId}" 
                                       onclick="return confirm('Are you sure to delete?');"
                                       class="btn btn-sm btn-danger">
                                        <i class="bi bi-trash-fill"></i>
                                    </a>
                                </td>

                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>

    </body>
</html>
