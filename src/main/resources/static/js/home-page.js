function time() {

    const lastUpdateString = document.getElementById('lastUpdate').value;
    const lastUpdate = parseDateString(lastUpdateString);
    const now = new Date();
    const difference = now - lastUpdate;

    const hours = Math.floor(difference / (1000 * 60 * 60));
    const minutes = Math.floor(difference % (1000 * 60 * 60) / (1000 * 60));
    let hourFormat = hours < 10 ? `0${hours}` : `${hours}`;
    let minuteFormat = minutes < 10 ? `0${minutes}` : `${minutes}`;

    let timeElapsed = `${hourFormat}h:${minuteFormat}m`;

    document.getElementById("clock").innerText = `${timeElapsed}`;
}

function parseDateString(dateString) {
    const parts = dateString.split(/[-T:.]/);
    return new Date(parts[0], parts[1] - 1, parts[2], parts[3], parts[4], parts[5]);
}

setInterval(time, 60000);
time();