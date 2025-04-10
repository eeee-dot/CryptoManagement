function time() {
    const now = new Date();
    const midnight = new Date(now.getFullYear(), now.getMonth(), now.getDate());
    const difference = now - midnight;
    const hours = Math.floor(difference / (1000 * 60 * 60));
    const minutes = Math.floor(difference % (1000 * 60 * 60) / (1000 * 60));
    let hourFormat;
    let minuteFormat;
    if (minutes < 10) {
        minuteFormat = `0${minutes}`
    } else {
        minuteFormat= `${minutes}`;
    }
    if (hours < 10) {
        hourFormat = `0${hours}`
    } else {
        hourFormat = `${hours}`
    }
    let timeElapsed = `${hourFormat}h:${minuteFormat}m`;

    document.getElementById("clock").innerText = `${timeElapsed}`;
}

setInterval(time, 60000);
time();