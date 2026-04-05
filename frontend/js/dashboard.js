const dashboardDate = document.getElementById("dashboard-date");
const dashboardMessage = document.getElementById("dashboard-message");

if (dashboardDate) {
    dashboardDate.textContent = new Date().toLocaleDateString();
}

function setDashboardMessageState(type) {
    if (!dashboardMessage) {
        return;
    }
    dashboardMessage.classList.add("status");
    dashboardMessage.classList.remove("profit", "loss", "warning");
    if (type) {
        dashboardMessage.classList.add(type);
    }
}

async function loadDashboard() {
    const currentCapitalEl = document.getElementById("current-capital");
    const todayPnlEl = document.getElementById("today-pnl");
    const totalPnlEl = document.getElementById("total-pnl");
    const returnPctEl = document.getElementById("return-pct");
    const targetCapitalEl = document.getElementById("target-capital");
    const remainingTargetEl = document.getElementById("remaining-target");
    const motivationEl = document.getElementById("motivation-message");

    if (!currentCapitalEl || !todayPnlEl || !totalPnlEl || !returnPctEl || !targetCapitalEl || !remainingTargetEl || !motivationEl) {
        return;
    }

    try {
        const dashboard = await apiGet("/dashboard");
        const currentCapital = Number(dashboard.currentCapital);
        const todayPnl = Number(dashboard.todayProfitLoss);
        const totalPnl = Number(dashboard.totalProfitLoss);
        const returnPct = Number(dashboard.totalReturnPercentage);
        const targetCapital = Number(dashboard.targetCapital);
        const remainingToTarget = Number(dashboard.remainingToTarget);

        currentCapitalEl.textContent = currentCapital.toFixed(2);
        todayPnlEl.textContent = todayPnl.toFixed(2);
        totalPnlEl.textContent = totalPnl.toFixed(2);
        returnPctEl.textContent = `${returnPct.toFixed(2)}%`;
        targetCapitalEl.textContent = targetCapital.toFixed(2);
        remainingTargetEl.textContent = remainingToTarget.toFixed(2);
        motivationEl.textContent = dashboard.motivationMessage || "Protect capital first.";

        todayPnlEl.classList.toggle("profit", todayPnl >= 0);
        todayPnlEl.classList.toggle("loss", todayPnl < 0);
        totalPnlEl.classList.toggle("profit", totalPnl >= 0);
        totalPnlEl.classList.toggle("loss", totalPnl < 0);

        if (dashboard.warnings && dashboard.warnings.length > 0) {
            dashboardMessage.textContent = dashboard.warnings.join(" | ");
            setDashboardMessageState("warning");
        } else {
            dashboardMessage.textContent = "No warnings today. Keep following your process.";
            setDashboardMessageState("profit");
        }
    } catch (error) {
        dashboardMessage.textContent = error.message;
        setDashboardMessageState("loss");
    }
}

loadDashboard();
