const PUBLIC_PAGES = ["login.html", "register.html"];

function getCurrentPage() {
    const parts = window.location.pathname.split("/");
    return parts[parts.length - 1] || "dashboard.html";
}

function ensureAuthGuard() {
    const currentPage = getCurrentPage();
    const token = getAuthToken();
    const isPublicPage = PUBLIC_PAGES.includes(currentPage);

    if (!token && !isPublicPage) {
        window.location.href = "login.html";
        return;
    }

    if (token && isPublicPage) {
        window.location.href = "dashboard.html";
        return;
    }

    if (token && !isPublicPage) {
        attachSessionUi();
    }
}

function attachSessionUi() {
    const nav = document.querySelector(".nav");
    if (!nav || nav.querySelector(".logout-link")) {
        return;
    }

    const logoutLink = document.createElement("a");
    logoutLink.href = "#";
    logoutLink.className = "logout-link";
    logoutLink.textContent = "Logout";
    logoutLink.addEventListener("click", (event) => {
        event.preventDefault();
        clearAuthSession();
        window.location.href = "login.html";
    });

    nav.appendChild(logoutLink);
}

ensureAuthGuard();
