const registerForm = document.getElementById("register-form");
const registerMessage = document.getElementById("register-message");

if (registerForm) {
    registerForm.addEventListener("submit", async (event) => {
        event.preventDefault();
        registerMessage.textContent = "";

        const payload = {
            name: document.getElementById("register-name").value.trim(),
            email: document.getElementById("register-email").value.trim(),
            password: document.getElementById("register-password").value
        };

        try {
            const auth = await apiSend("/auth/register", "POST", payload);
            saveAuthSession(auth);
            window.location.href = "dashboard.html";
        } catch (error) {
            registerMessage.textContent = "Registration failed. Try another email.";
            registerMessage.classList.add("loss");
        }
    });
}
