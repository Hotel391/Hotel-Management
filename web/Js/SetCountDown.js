const urlParams = new URLSearchParams(window.location.search);
    startCooldown(60);

function startCooldown(seconds) {
    const resendLink = document.getElementById("resendWrapper");
    const cooldown = document.getElementById("cooldown");
    const timerSpan = document.getElementById("timer");

    resendLink.style.display="none";
    cooldown.style.display="inline";

    let timeLeft=seconds;
    timerSpan.textContent=timeLeft;

    const countdown = setInterval(()=>{
        timeLeft--;
        timerSpan.textContent=timeLeft;
        
        if(timeLeft<=0){
            clearInterval(countdown);
            cooldown.style.display="none";
            resendLink.style.display="inline";
        }
    }, 1000);
}