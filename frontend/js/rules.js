const rulesForm = document.getElementById("rules-form");
const rulesSaveMessage = document.getElementById("rules-save-message");

function setRulesMessageState(type) {
    if (!rulesSaveMessage) {
        return;
    }
    rulesSaveMessage.classList.add("msg");
    rulesSaveMessage.classList.remove("profit", "loss", "warning");
    rulesSaveMessage.classList.add(type);
}

async function loadRules() {
    if (!rulesForm) {
        return;
    }

    try {
        const data = await apiGet("/rules");
        document.getElementById("tradingRules").value = data.tradingRules || "";
        document.getElementById("disciplineRules").value = data.disciplineRules || "";
        document.getElementById("motivationNotes").value = data.motivationNotes || "";
        document.getElementById("dailyReminders").value = data.dailyReminders || "";
    } catch (error) {
        rulesSaveMessage.textContent = error.message;
        setRulesMessageState("loss");
    }
}

if (rulesForm) {
    rulesForm.addEventListener("submit", async (event) => {
        event.preventDefault();

        const payload = {
            tradingRules: document.getElementById("tradingRules").value,
            disciplineRules: document.getElementById("disciplineRules").value,
            motivationNotes: document.getElementById("motivationNotes").value,
            dailyReminders: document.getElementById("dailyReminders").value
        };

        try {
            await apiSend("/rules", "PUT", payload);
            rulesSaveMessage.textContent = "Rules saved successfully.";
            setRulesMessageState("profit");
        } catch (error) {
            rulesSaveMessage.textContent = error.message;
            setRulesMessageState("loss");
        }
    });
}

loadRules();
