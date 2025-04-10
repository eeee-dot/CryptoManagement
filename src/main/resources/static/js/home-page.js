function time() {
    const now = new Date();
    const midnight = new Date(now.getFullYear(), now.getMonth(), now.getDate());
    const difference = now - midnight;
    const hours = Math.floor(difference / (1000 * 60 * 60));
    const minutes = Math.floor(difference % (1000 * 60 * 60) / (1000 * 60));

    let timeElapsed = `${hours}h:${minutes}m`;

    document.getElementById("clock").innerText = `${timeElapsed}`;
}

setInterval(time, 60000);
time();