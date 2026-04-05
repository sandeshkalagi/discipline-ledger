const loginForm = document.getElementById("login-form");
const loginMessage = document.getElementById("login-message");

if (loginForm) {
    loginForm.addEventListener("submit", async (event) => {
        event.preventDefault();
        loginMessage.textContent = "";

        const payload = {
            email: document.getElementById("login-email").value.trim(),
            password: document.getElementById("login-password").value
        };

        try {
            const auth = await apiSend("/auth/login", "POST", payload);
            saveAuthSession(auth);
            window.location.href = "dashboard.html";
        } catch (error) {
            loginMessage.textContent = "Login failed. Check email and password.";
            loginMessage.classList.add("loss");
        }
    });
}
