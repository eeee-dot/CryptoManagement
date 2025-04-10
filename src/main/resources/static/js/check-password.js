document.getElementById("registration-form").addEventListener("submit", function(event) {
    let password = document.getElementById('password').value;
    let repeatedPassword = document.getElementById("repeated-password").value;

    if(password !== repeatedPassword) {
        event.preventDefault();
        alert("Passwords do not match!");
    }
});