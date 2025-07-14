<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Statistics</title>
        <%--style for dashboard--%>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css" rel="stylesheet"
              integrity="sha384-EVSTQN3/azprG1Anm3QDgpJLIm9Nao0Yz1ztcQTwFspd3yD65VohhpuuCOmLASjC"
              crossorigin="anonymous">
        <link rel="stylesheet"
              href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.1/font/bootstrap-icons.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/Css/navDashboardStyle.css" />
        <link rel="stylesheet" href="${pageContext.request.contextPath}/Css/dashboardStyle.css" />
        <link rel="stylesheet" href="${pageContext.request.contextPath}/Css/mainDashboardStyle.css" />
        <%--another in the following--%>
        <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
        <style>
            .search-input {
                width: 450px !important;
            }
        </style>
    </head>
    <body>
        <div class="containerBox">
            <jsp:include page="leftNav.jsp" />
            <div class="right-section">
                <c:set var="title" value="Statistics" scope="request" />
                <jsp:include page="topNav.jsp" />
                <div class="main-content">
                    <form method="post" class="mb-4" style="margin-top: -30px">
                        <div class="row g-3 align-items-center mb-3">

                            <div class="col-auto" style="display: none;">
                                <label for="typeData" class="form-label mb-0">Type Data</label>
                                <select id="typeData" name="typeData" class="form-select">
                                    <option value="allType">All Type</option>
                                    <option value="oneType" ${param.typeData eq 'oneType' ? 'selected' : '' }>One
                                        Type</option>
                                </select>
                            </div>
                            <div class="col-auto">
                                <label for="typeX" class="form-label mb-0">Group By</label>
                                <select id="typeX" name="typeX" class="form-select">
                                    <option value="year">Year</option>
                                    <option value="quarter" ${param.typeX eq 'quarter' ? 'selected' : '' }>Quarter
                                    </option>
                                    <option value="month" ${param.typeX eq 'month' ? 'selected' : '' }>Month
                                    </option>
                                </select>
                            </div>
                        </div>

                        <div class="row g-3 mb-3">
                            <div class="col-md-6">
                                <fieldset id="startFieldset" class="border p-3 rounded">
                                    <legend class="float-none w-auto px-2 small text-muted">Start Date</legend>
                                    <div class="d-flex gap-2 align-items-center flex-wrap" style="margin-top: -20px">
                                        <div id="startYearGroup">
                                            <label class="form-label mb-0 small">Year</label>
                                            <select name="startYear" class="form-select" style="width: 100px;">
                                                <c:forEach var="y" begin="2015" end="${currentYear}">
                                                    <option value="${y}" ${param.startYear==y ? 'selected' : '' }>
                                                        ${y}</option>
                                                    </c:forEach>
                                            </select>
                                        </div>
                                        <div id="startQuarterGroup">
                                            <label class="form-label mb-0 small">Quarter</label>
                                            <select id="startQuarter" name="startQuarter" class="form-select"
                                                    style="width: 100px;">
                                                <option value="1" ${param.startQuarter=='1' ? 'selected' : '' }>Q1
                                                </option>
                                                <option value="2" ${param.startQuarter=='2' ? 'selected' : '' }>Q2
                                                </option>
                                                <option value="3" ${param.startQuarter=='3' ? 'selected' : '' }>Q3
                                                </option>
                                                <option value="4" ${param.startQuarter=='4' ? 'selected' : '' }>Q4
                                                </option>
                                            </select>
                                        </div>
                                        <div id="startMonthGroup">
                                            <label class="form-label mb-0 small">Month</label>
                                            <select id="startMonth" name="startMonth" class="form-select"
                                                    style="width: 100px;">
                                                <c:forEach var="m" begin="1" end="12">
                                                    <option value="${m}" ${param.startMonth==m ? 'selected' : '' }>
                                                        ${m}</option>
                                                    </c:forEach>
                                            </select>
                                        </div>
                                    </div>
                                </fieldset>
                            </div>

                            <div class="col-md-6">
                                <fieldset id="endFieldset" class="border p-3 rounded">
                                    <legend class="float-none w-auto px-2 small text-muted">End Date</legend>
                                    <div class="d-flex gap-2 align-items-center flex-wrap" style="margin-top: -20px">
                                        <div id="endYearGroup">
                                            <label class="form-label mb-0 small">Year</label>
                                            <select name="endYear" class="form-select" style="width: 100px;">
                                                <c:forEach var="y" begin="2015" end="${currentYear}">
                                                    <option value="${y}" ${param.endYear==y ? 'selected' : '' }>${y}
                                                    </option>
                                                </c:forEach>
                                            </select>
                                        </div>
                                        <div id="endQuarterGroup">
                                            <label class="form-label mb-0 small">Quarter</label>
                                            <select id="endQuarter" name="endQuarter" class="form-select"
                                                    style="width: 100px;">
                                                <option value="1" ${param.endQuarter=='1' ? 'selected' : '' }>Q1
                                                </option>
                                                <option value="2" ${param.endQuarter=='2' ? 'selected' : '' }>Q2
                                                </option>
                                                <option value="3" ${param.endQuarter=='3' ? 'selected' : '' }>Q3
                                                </option>
                                                <option value="4" ${param.endQuarter=='4' ? 'selected' : '' }>Q4
                                                </option>
                                            </select>
                                        </div>
                                        <div id="endMonthGroup">
                                            <label class="form-label mb-0 small">Month</label>
                                            <select id="endMonth" name="endMonth" class="form-select"
                                                    style="width: 100px;">
                                                <c:forEach var="m" begin="1" end="12">
                                                    <option value="${m}" ${param.endMonth==m ? 'selected' : '' }>
                                                        ${m}</option>
                                                    </c:forEach>
                                            </select>
                                        </div>
                                    </div>
                                </fieldset>
                            </div>
                        </div>

                        <div class="text-end">
                            <button type="submit" class="btn btn-primary">Apply</button>
                        </div>
                    </form>
                    <div class="chart-column">
                        <% 
                            request.setAttribute("chartId", "doanhThuChart" );
                            request.setAttribute("chartType", "bar" ); request.setAttribute("chartLabel", "Revenue"); 
                            request.setAttribute("axisDirection", "x" ); request.setAttribute("width", "800" );
                            request.setAttribute("height", "800" ); 
                        %>
                        <jsp:include page="templateChart.jsp" />
                    </div>
                </div>
            </div>
        </div>
    </body>
    <%--script for dashboard--%>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js"></script>
    <script src="${pageContext.request.contextPath}/Js/navDashboardJs.js"></script>
    <script src="${pageContext.request.contextPath}/Js/userProfileJs.js"></script>
    <%--another in following--%>

    <script>
        function showFields(typeXValue, targetPrefix, alwaysShow = false) {
            const year = document.getElementById(targetPrefix + 'YearGroup');
            const quarter = document.getElementById(targetPrefix + 'QuarterGroup');
            const month = document.getElementById(targetPrefix + 'MonthGroup');

            if (alwaysShow) {
                year.style.display = 'block';
                quarter.style.display = 'block';
                month.style.display = 'block';
                return;
            }

            year.style.display = (typeXValue === 'year' || typeXValue === 'quarter' || typeXValue === 'month') ? 'block' : 'none';
            quarter.style.display = (typeXValue === 'quarter' || typeXValue === 'month') ? 'block' : 'none';
            month.style.display = (typeXValue === 'month') ? 'block' : 'none';
        }

        function getMonthOptions(select) {
            return Array.from(select.options).filter(opt => opt.value !== "");
        }

        function filterMonthsByQuarter(quarterSelectId, monthSelectId) {
            const quarter = document.getElementById(quarterSelectId).value;
            const monthSelect = document.getElementById(monthSelectId);
            const options = getMonthOptions(monthSelect);

            let start = 1, end = 12;
            if (quarter === "1") {
                start = 1;
                end = 3;
            } else if (quarter === "2") {
                start = 4;
                end = 6;
            } else if (quarter === "3") {
                start = 7;
                end = 9;
            } else if (quarter === "4") {
                start = 10;
                end = 12;
            }

            let firstEnabled = null;
            options.forEach(opt => {
                const val = parseInt(opt.value);
                const inRange = val >= start && val <= end;
                opt.disabled = !inRange;
                if (inRange && firstEnabled === null)
                    firstEnabled = opt.value;
            });

            if (firstEnabled)
                monthSelect.value = firstEnabled;
        }

        function updateUI() {
            const typeData = document.getElementById('typeData').value;
            const typeX = document.getElementById('typeX').value;

            const typeXSelect = document.getElementById('typeX');
            const disableTypeX = (typeData === 'oneType');

            typeXSelect.disabled = disableTypeX;

            const forceShowAll = disableTypeX;

            showFields(typeX, 'start', forceShowAll);
            showFields(typeX, 'end', forceShowAll);

            filterMonthsByQuarter("startQuarter", "startMonth");
            filterMonthsByQuarter("endQuarter", "endMonth");
        }

        function validateStartEndDate(e) {
            const sy = parseInt(document.querySelector('[name="startYear"]').value);
            const sq = parseInt(document.querySelector('[name="startQuarter"]').value);
            const sm = parseInt(document.querySelector('[name="startMonth"]').value);

            const ey = parseInt(document.querySelector('[name="endYear"]').value);
            const eq = parseInt(document.querySelector('[name="endQuarter"]').value);
            const em = parseInt(document.querySelector('[name="endMonth"]').value);

            const startScore = sy * 10000 + sq * 100 + sm;
            const endScore = ey * 10000 + eq * 100 + em;

            if (startScore > endScore) {
                e.preventDefault();
                alert("End date must be after Start date.");
            }
        }

        document.addEventListener("DOMContentLoaded", () => {
            updateUI();

            document.getElementById('typeX').addEventListener('change', updateUI);
            document.getElementById('typeData').addEventListener('change', updateUI);

            document.getElementById("startQuarter").addEventListener("change", () =>
                filterMonthsByQuarter("startQuarter", "startMonth"));
            document.getElementById("endQuarter").addEventListener("change", () =>
                filterMonthsByQuarter("endQuarter", "endMonth"));

            document.querySelector("form").addEventListener("submit", validateStartEndDate);
        });
    </script>
    
</html>
