<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Add manager</title>
         <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.1/font/bootstrap-icons.css">
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css" rel="stylesheet"
              integrity="sha384-EVSTQN3/azprG1Anm3QDgpJLIm9Nao0Yz1ztcQTwFspd3yD65VohhpuuCOmLASjC"
              crossorigin="anonymous">
    </head>
    <body>
    <div class="container d-flex justify-content-center align-items-center" style="height: 100vh;">
        <form action="${pageContext.request.contextPath}/developerPage" method="post" class="w-50">
            <input type="hidden" name="service" value="add">

            <div class="input-group mb-3">
                <span class="input-group-text" id="visible-addon">@</span>
                <input type="text" name="userName" class="form-control" placeholder="Username" aria-label="Username" aria-describedby="visible-addon">
            </div>

            <div class="input-group mb-3">
                <span class="input-group-text" id="visible-addon2"><i class="bi bi-lock"></i></span>
                <input type="password" name="password" class="form-control" placeholder="Password" aria-label="Password" aria-describedby="visible-addon2">
            </div>

            <button type="submit" class="btn btn-primary w-100">Add</button>
        </form>
    </div>
</body>

</html>
