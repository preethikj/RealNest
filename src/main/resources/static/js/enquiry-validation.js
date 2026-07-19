document.addEventListener("DOMContentLoaded", () => {

    const fields = [
        { inputId: "name", errorId: "nameError" },
        { inputId: "email", errorId: "emailError" },
        { inputId: "phone", errorId: "phoneError" },
        { inputId: "message", errorId: "messageError" }
    ];

    fields.forEach(({ inputId, errorId }) => {

        const input = document.getElementById(inputId);

        const error = document.getElementById(errorId);

        if (!input || !error) {
            return;
        }

        input.addEventListener("input", () => {
            error.classList.add("hidden");
            input.classList.remove("border-rose-400");
            input.classList.add("border-white/10");
        });
    });
});