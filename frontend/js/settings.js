const settingsForm = document.getElementById("settings-form");
const settingsSaveMessage = document.getElementById("settings-save-message");

function setSettingsMessageState(type) {
    if (!settingsSaveMessage) {
        return;
    }
    settingsSaveMessage.classList.add("msg");
    settingsSaveMessage.classList.remove("profit", "loss", "warning");
    if (type) {
        settingsSaveMessage.classList.add(type);
    }
}

async function loadSettings() {
    if (!settingsForm) {
        return;
    }

    try {
        const settings = await apiGet("/settings");
        document.getElementById("initialCapital").value = Number(settings.initialCapital).toFixed(2);
        document.getElementById("targetReturnPercentage").value = Number(settings.targetReturnPercentage).toFixed(2);
        document.getElementById("maxTradesPerDay").value = Number(settings.maxTradesPerDay);
        document.getElementById("maxDailyLossPercentage").value = Number(settings.maxDailyLossPercentage).toFixed(2);
        document.getElementById("motivationMessage").value = settings.motivationMessage || "";
    } catch (error) {
        settingsSaveMessage.textContent = error.message;
        setSettingsMessageState("loss");
    }
}

if (settingsForm) {
    settingsForm.addEventListener("submit", async (event) => {
        event.preventDefault();

        const payload = {
            initialCapital: Number(document.getElementById("initialCapital").value),
            targetReturnPercentage: Number(document.getElementById("targetReturnPercentage").value),
            maxTradesPerDay: Number(document.getElementById("maxTradesPerDay").value),
            maxDailyLossPercentage: Number(document.getElementById("maxDailyLossPercentage").value),
            motivationMessage: document.getElementById("motivationMessage").value
        };

        try {
            await apiSend("/settings", "PUT", payload);
            settingsSaveMessage.textContent = "Settings saved successfully.";
            setSettingsMessageState("profit");
        } catch (error) {
            settingsSaveMessage.textContent = error.message;
            setSettingsMessageState("loss");
        }
    });
}

loadSettings();
