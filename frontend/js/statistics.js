const statsMessage = document.getElementById("stats-message");
const growthHistoryBody = document.getElementById("growth-history-body");

function setStatsMessageState(type) {
    if (!statsMessage) {
        return;
    }
    statsMessage.classList.add("status");
    statsMessage.classList.remove("profit", "loss", "warning");
    if (type) {
        statsMessage.classList.add(type);
    }
}

async function loadStats() {
    if (!statsMessage) {
        return;
    }

    try {
        const stats = await apiGet("/statistics");
        if (!stats.totalDays || stats.totalDays === 0) {
            statsMessage.textContent = "No data yet. Add entries to generate statistics.";
            setStatsMessageState("warning");
            return;
        }

        statsMessage.textContent = `Loaded ${stats.totalDays} trading day(s).`;
        setStatsMessageState("profit");

        document.getElementById("stat-win-rate").textContent = `${Number(stats.winRate).toFixed(2)}%`;
        document.getElementById("stat-avg-profit").textContent = Number(stats.averageProfit).toFixed(2);
        document.getElementById("stat-avg-loss").textContent = Number(stats.averageLoss).toFixed(2);
        document.getElementById("stat-total-trades").textContent = Number(stats.totalTrades);
        document.getElementById("stat-best-day").textContent = Number(stats.bestDay).toFixed(2);
        document.getElementById("stat-worst-day").textContent = Number(stats.worstDay).toFixed(2);

        if (growthHistoryBody) {
            const growthHistory = stats.capitalGrowthHistory || [];
            growthHistoryBody.innerHTML = growthHistory.map((point) => `
                <tr>
                    <td>${point.entryDate}</td>
                    <td>${Number(point.endingCapital).toFixed(2)}</td>
                </tr>
            `).join("");
        }
    } catch (error) {
        statsMessage.textContent = error.message;
        setStatsMessageState("loss");
    }
}

loadStats();
