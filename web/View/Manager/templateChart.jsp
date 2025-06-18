<%--
    request.setAttribute("chartId", "doanhThuChart");
    request.setAttribute("chartType", "bar");
    request.setAttribute("chartLabel", "Doanh thu (tri?u VND)");
    request.setAttribute("axisDirection", "x"); // ho?c "y" n?u c?n bar chart xoay ngang
    request.setAttribute("width", "800");
    request.setAttribute("height", "400");
--%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<canvas id="${chartId}"></canvas>

<script>
    const labels = [<c:forEach var="label" items="${labels}" varStatus="i">
        "${label}"<c:if test="${!i.last}">,</c:if>
    </c:forEach>
    ];

    const data = [<c:forEach var="d" items="${data}" varStatus="i">
        ${d}<c:if test="${!i.last}">,</c:if>
    </c:forEach>
    ];

    const ctx = document.getElementById('${chartId}').getContext('2d');
    new Chart(ctx, {
        type: '${chartType}',
        data: {
            labels: labels,
            datasets: [{
                label: '${chartLabel}',
                data: data,
                backgroundColor: 'rgba(75, 192, 192, 0.7)',
                borderColor: 'rgba(75, 192, 192, 1)',
                borderWidth: 1
            }]
        },
        options: {
            indexAxis: '${axisDirection}',
            scales: {
                y: {
                    beginAtZero: true
                }
            }
        }
    });
</script>