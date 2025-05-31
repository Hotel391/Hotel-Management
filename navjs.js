document.addEventListener("DOMContentLoaded", function() {
    const leftNav=document.querySelector(".left-nav");
    const topNav=document.querySelector(".top-navbar");
    const toggleBtn=document.getElementById("menu-toggle");

    toggleBtn.addEventListener('click',()=> {
        leftNav.classList.toggle("collapsed");
        topNav.classList.toggle("collapsed");
        if (leftNav.classList.contains("collapsed")) {
            toggleBtn.innerHTML = '<i class="bi bi-arrow-bar-right"></i>';
        } else {
            toggleBtn.innerHTML = '<i class="bi bi-list"></i>';
        }
    });

    function handleResize() {
        if (window.innerWidth < 1000 && window.innerWidth > 758) {
            leftNav.classList.add("collapsed");
            topNav.classList.add("collapsed");
            toggleBtn.style.display = "none";
        } else {
            leftNav.classList.remove("collapsed");
            topNav.classList.remove("collapsed");
            toggleBtn.style.display = "block";
        }
    }
    handleResize();
    window.addEventListener('resize', handleResize);
});