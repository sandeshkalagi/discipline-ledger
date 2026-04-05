const journalForm = document.getElementById("journal-form");
const formMessage = document.getElementById("form-message");

function setFormMessageState(type) {
    if (!formMessage) {
        return;
    }
    formMessage.classList.add("msg");
    formMessage.classList.remove("profit", "loss", "warning");
    formMessage.classList.add(type);
}

if (journalForm) {
    journalForm.addEventListener("submit", async (event) => {
        event.preventDefault();

        const payload = {
            entryDate: document.getElementById("entryDate").value,
            startingCapital: Number(document.getElementById("startingCapital").value),
            endingCapital: Number(document.getElementById("endingCapital").value),
            numberOfTrades: Number(document.getElementById("numberOfTrades").value),
            mistakesMade: document.getElementById("mistakesMade").value,
            whatWentWell: document.getElementById("whatWentWell").value,
            notes: document.getElementById("notes").value,
            moodDisciplineNotes: document.getElementById("moodDisciplineNotes").value
        };

        try {
            const created = await apiSend("/journal-entries", "POST", payload);
            formMessage.textContent = `Saved entry #${created.id} for ${created.entryDate}`;
            setFormMessageState("profit");
            journalForm.reset();
        } catch (error) {
            formMessage.textContent = error.message;
            setFormMessageState("loss");
        }
    });
}
