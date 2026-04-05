const historyTableBody = document.getElementById("history-body");
const historyMessage = document.getElementById("history-message");
const historyCountEl = document.getElementById("history-count");
const historyNetPnlEl = document.getElementById("history-net-pnl");
const historyWinDaysEl = document.getElementById("history-win-days");
const historyLossDaysEl = document.getElementById("history-loss-days");

const editPanel = document.getElementById("history-edit-panel");
const editForm = document.getElementById("history-edit-form");
const cancelEditBtn = document.getElementById("history-cancel-edit");

let historyEntries = [];

function setHistoryMessage(message, type) {
    if (!historyMessage) {
        return;
    }
    historyMessage.textContent = message || "";
    historyMessage.classList.remove("profit", "loss", "warning");
    if (type) {
        historyMessage.classList.add(type);
    }
}

function renderSummary(entries) {
    const netPnl = entries.reduce((sum, entry) => sum + Number(entry.todaysProfitLoss || 0), 0);
    const winDays = entries.filter((entry) => Number(entry.todaysProfitLoss) > 0).length;
    const lossDays = entries.filter((entry) => Number(entry.todaysProfitLoss) < 0).length;

    if (historyCountEl) {
        historyCountEl.textContent = entries.length;
    }
    if (historyNetPnlEl) {
        historyNetPnlEl.textContent = netPnl.toFixed(2);
        historyNetPnlEl.classList.toggle("profit", netPnl >= 0);
        historyNetPnlEl.classList.toggle("loss", netPnl < 0);
    }
    if (historyWinDaysEl) {
        historyWinDaysEl.textContent = winDays;
    }
    if (historyLossDaysEl) {
        historyLossDaysEl.textContent = lossDays;
    }
}

function renderRows(entries) {
    if (!historyTableBody) {
        return;
    }

    if (entries.length === 0) {
        historyTableBody.innerHTML = `<tr><td colspan="6">No entries found yet.</td></tr>`;
        return;
    }

    historyTableBody.innerHTML = entries.map((entry) => {
        const pnl = Number(entry.todaysProfitLoss);
        const pnlClass = pnl >= 0 ? "profit" : "loss";

        return `
            <tr>
                <td>${entry.entryDate}</td>
                <td>${Number(entry.startingCapital).toFixed(2)}</td>
                <td>${Number(entry.endingCapital).toFixed(2)}</td>
                <td class="${pnlClass}">${pnl.toFixed(2)}</td>
                <td>${entry.numberOfTrades}</td>
                <td>
                    <div class="table-actions">
                        <button class="btn-sm btn-secondary history-edit" data-id="${entry.id}" type="button">Edit</button>
                        <button class="btn-sm btn-danger history-delete" data-id="${entry.id}" type="button">Delete</button>
                    </div>
                </td>
            </tr>
        `;
    }).join("");
}

function hideEditPanel() {
    if (editPanel) {
        editPanel.style.display = "none";
    }
}

function showEditPanel() {
    if (editPanel) {
        editPanel.style.display = "block";
        editPanel.scrollIntoView({ behavior: "smooth", block: "start" });
    }
}

function populateEditForm(entry) {
    document.getElementById("edit-id").value = entry.id;
    document.getElementById("edit-entry-date").value = entry.entryDate;
    document.getElementById("edit-starting-capital").value = Number(entry.startingCapital).toFixed(2);
    document.getElementById("edit-ending-capital").value = Number(entry.endingCapital).toFixed(2);
    document.getElementById("edit-number-of-trades").value = entry.numberOfTrades;
    document.getElementById("edit-mistakes-made").value = entry.mistakesMade || "";
    document.getElementById("edit-what-went-well").value = entry.whatWentWell || "";
    document.getElementById("edit-notes").value = entry.notes || "";
    document.getElementById("edit-mood-discipline-notes").value = entry.moodDisciplineNotes || "";
}

async function loadHistory() {
    if (!historyTableBody) {
        return;
    }

    try {
        const entries = await apiGet("/journal-entries");
        historyEntries = entries;
        renderSummary(entries);
        renderRows(entries);
    } catch (error) {
        historyTableBody.innerHTML = `<tr><td colspan="6" class="loss">${error.message}</td></tr>`;
        setHistoryMessage(error.message, "loss");
    }
}

async function handleDelete(entryId) {
    const confirmed = window.confirm("Delete this journal entry?");
    if (!confirmed) {
        return;
    }

    try {
        await apiSend(`/journal-entries/${entryId}`, "DELETE");
        setHistoryMessage("Entry deleted successfully.", "profit");
        hideEditPanel();
        await loadHistory();
    } catch (error) {
        setHistoryMessage(error.message, "loss");
    }
}

function handleEdit(entryId) {
    const entry = historyEntries.find((item) => Number(item.id) === Number(entryId));
    if (!entry) {
        setHistoryMessage("Entry not found.", "loss");
        return;
    }
    populateEditForm(entry);
    setHistoryMessage("Editing selected entry.", "warning");
    showEditPanel();
}

if (historyTableBody) {
    historyTableBody.addEventListener("click", async (event) => {
        const target = event.target;
        if (!(target instanceof HTMLElement)) {
            return;
        }

        if (target.classList.contains("history-edit")) {
            handleEdit(target.dataset.id);
        }
        if (target.classList.contains("history-delete")) {
            await handleDelete(target.dataset.id);
        }
    });
}

if (editForm) {
    editForm.addEventListener("submit", async (event) => {
        event.preventDefault();

        const entryId = document.getElementById("edit-id").value;
        const payload = {
            entryDate: document.getElementById("edit-entry-date").value,
            startingCapital: Number(document.getElementById("edit-starting-capital").value),
            endingCapital: Number(document.getElementById("edit-ending-capital").value),
            numberOfTrades: Number(document.getElementById("edit-number-of-trades").value),
            mistakesMade: document.getElementById("edit-mistakes-made").value,
            whatWentWell: document.getElementById("edit-what-went-well").value,
            notes: document.getElementById("edit-notes").value,
            moodDisciplineNotes: document.getElementById("edit-mood-discipline-notes").value
        };

        try {
            await apiSend(`/journal-entries/${entryId}`, "PUT", payload);
            setHistoryMessage("Entry updated successfully.", "profit");
            hideEditPanel();
            await loadHistory();
        } catch (error) {
            setHistoryMessage(error.message, "loss");
        }
    });
}

if (cancelEditBtn) {
    cancelEditBtn.addEventListener("click", () => {
        hideEditPanel();
        setHistoryMessage("Edit cancelled.", "warning");
    });
}

hideEditPanel();
loadHistory();
