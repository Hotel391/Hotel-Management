document.addEventListener("DOMContentLoaded", function () {
    const userBtn= document.getElementById("user-profile-button");
    const userProfile = document.querySelector(".user-info");

    userBtn.addEventListener('click', () => {
        userProfile.classList.toggle("visible");
    });
    userProfile.classList.toggle("visible");
});