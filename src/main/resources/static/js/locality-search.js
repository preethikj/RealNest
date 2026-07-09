document.addEventListener("DOMContentLoaded", () => {
    const localityInput = document.getElementById("localityInput");
    const suggestions = document.getElementById("localitySuggestions");
    const options = document.querySelectorAll(".locality-option");

    if (!localityInput || !suggestions) {
        return;
    }

    localityInput.addEventListener("focus", () => {
        suggestions.classList.remove("hidden");
    });

    options.forEach((option) => {
        option.addEventListener("click", () => {
            localityInput.value = option.textContent.trim();
            suggestions.classList.add("hidden");
        });
    });

    document.addEventListener("click", (event) => {
        if (!localityInput.contains(event.target) &&
            !suggestions.contains(event.target)) {
            suggestions.classList.add("hidden");
        }
    });
});