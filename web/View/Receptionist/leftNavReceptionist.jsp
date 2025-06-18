<%-- 
    Document   : leftNavReceptionist
    Created on : Jun 6, 2025, 7:35:37 PM
    Author     : SONNAM
--%>




<div class="left-nav">
    <div class="logo">
        <img src="${pageContext.request.contextPath}/Image/Logo.png" alt="Logo">
        <h1>Hotel</h1>
        <h2>Hotel Admin Dashboard</h2>
    </div>
    <div class="nav-navigation">
        <div class="nav-links">
            <ul class="nav-list">
                <li><a href="${pageContext.request.contextPath}/admin/viewBookingList"><span><i class="bi bi-list-ul"></i></span>View Booking List</a></li>
                <li><a href="${pageContext.request.contextPath}/receptionist/checkout"><span><i class="bi bi-door-open"></i></span>View Checkout Room</a></li>
                <li><a href="${pageContext.request.contextPath}/admin/searchRoom"><span><i class="bi bi-search"></i></span>Search Room</a></li>
                <li><a href="${pageContext.request.contextPath}/admin/stayingRoom"><span><i class="bi bi-house-door"></i></span>Staying Room</a></li>
            </ul>
        </div>
        <div class="contact-container">
            <div class="contact-us text-center">
                <img src="${pageContext.request.contextPath}/Image/Contact.jpg" alt="Contact Us">
                <p>Hotel management team</p>
                <p>fpthotel@gmail.com</p>
            </div>
        </div>
        <div class="nav-footer text-center">
            <p>© 2023 Hotel Receptionist Dashboard</p>
        </div>
    </div>
</div>

