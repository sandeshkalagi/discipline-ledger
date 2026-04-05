const API_BASE_URL = (() => {
    const configured = window.DL_API_BASE_URL || localStorage.getItem("dl_api_base_url");
    if (configured) {
        return configured.replace(/\/+$/, "");
    }
    const host = window.location.hostname;
    if (host === "127.0.0.1" || host === "localhost") {
        return "http://localhost:8081/api";
    }
    return `${window.location.origin}/api`;
})();
const AUTH_TOKEN_KEY = "dl_auth_token";
const AUTH_USER_ID_KEY = "dl_auth_user_id";
const AUTH_USER_NAME_KEY = "dl_auth_user_name";
const AUTH_USER_EMAIL_KEY = "dl_auth_user_email";

function getAuthToken() {
    return localStorage.getItem(AUTH_TOKEN_KEY);
}

function getCurrentUserId() {
    const value = localStorage.getItem(AUTH_USER_ID_KEY);
    return value ? Number(value) : null;
}

function getCurrentUserName() {
    return localStorage.getItem(AUTH_USER_NAME_KEY);
}

function saveAuthSession(authResponse) {
    localStorage.setItem(AUTH_TOKEN_KEY, authResponse.token);
    localStorage.setItem(AUTH_USER_ID_KEY, String(authResponse.userId));
    localStorage.setItem(AUTH_USER_NAME_KEY, authResponse.name || "");
    localStorage.setItem(AUTH_USER_EMAIL_KEY, authResponse.email || "");
}

function clearAuthSession() {
    localStorage.removeItem(AUTH_TOKEN_KEY);
    localStorage.removeItem(AUTH_USER_ID_KEY);
    localStorage.removeItem(AUTH_USER_NAME_KEY);
    localStorage.removeItem(AUTH_USER_EMAIL_KEY);
}

async function apiGet(path) {
    const token = getAuthToken();
    const response = await fetch(`${API_BASE_URL}${path}`, {
        headers: token ? { Authorization: `Bearer ${token}` } : {}
    });
    if (!response.ok) {
        throw new Error(`GET ${path} failed with ${response.status}`);
    }
    return response.json();
}

async function apiSend(path, method, payload) {
    const token = getAuthToken();
    const headers = {
        "Content-Type": "application/json"
    };
    if (token) {
        headers.Authorization = `Bearer ${token}`;
    }

    const response = await fetch(`${API_BASE_URL}${path}`, {
        method,
        headers,
        body: JSON.stringify(payload)
    });

    if (!response.ok) {
        const body = await response.text();
        throw new Error(`${method} ${path} failed: ${body}`);
    }

    if (response.status === 204) {
        return null;
    }

    return response.json();
}
