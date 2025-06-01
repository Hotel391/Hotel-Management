document.addEventListener("DOMContentLoaded", function () {
    const leftNav = document.querySelector(".left-nav");
    const topNav = document.querySelector(".top-navbar");
    const toggleBtn = document.getElementById("menu-toggle");

    let isMobileNavVisible = false;

    toggleBtn.addEventListener('click', () => {
        if (window.innerWidth < 768) {
            isMobileNavVisible = !isMobileNavVisible;
            if (isMobileNavVisible) {
                leftNav.classList.toggle("collapsed");
                leftNav.classList.add("mobile-visible");
                toggleBtn.innerHTML = '<i class="bi bi-arrow-bar-left"></i>';
            } else {
                leftNav.classList.toggle("collapsed");
                leftNav.classList.remove("mobile-visible");
                toggleBtn.innerHTML = '<i class="bi bi-arrow-bar-right"></i>';
            }
        } else {
            leftNav.classList.toggle("collapsed");
            topNav.classList.toggle("collapsed");
            if (leftNav.classList.contains("collapsed")) {
                toggleBtn.innerHTML = '<i class="bi bi-arrow-bar-right"></i>';
            } else {
                toggleBtn.innerHTML = '<i class="bi bi-list"></i>';
            }
        }


    });

    function handleResize() {
        if (window.innerWidth < 768) {
            leftNav.classList.remove("collapsed", "mobile-visible");
            topNav.classList.remove("collapsed");
            leftNav.style.display = "none";
            isMobileNavVisible = false;
            toggleBtn.innerHTML = '<i class="bi bi-arrow-bar-right"></i>';
            toggleBtn.style.display = "block";
        } else if (window.innerWidth < 1000) {
            leftNav.classList.remove("mobile-visible");
            leftNav.classList.add("collapsed");
            topNav.classList.add("collapsed");
            leftNav.style.display = "";
            toggleBtn.style.display = "none";
        } else {
            leftNav.classList.remove("collapsed", "mobile-visible");
            topNav.classList.remove("collapsed");
            toggleBtn.style.display = "block";
            toggleBtn.innerHTML = '<i class="bi bi-list"></i>';
            leftNav.style.display = "";
        }
    }

    handleResize();
    window.addEventListener('resize', handleResize);
});