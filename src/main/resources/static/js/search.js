document.addEventListener("DOMContentLoaded", () => {
    const searchForm = document.getElementById("propertySearchForm");
    const cityInput = document.getElementById("searchCity");

    if (!searchForm || !cityInput) {
        return;
    }

    searchForm.addEventListener("submit", (event) => {
        const city = cityInput.value.trim();

        if (city.length < 2) {
            event.preventDefault();

            cityInput.classList.add("ring-2", "ring-red-400");
            cityInput.focus();

            return;
        }

        cityInput.classList.remove("ring-2", "ring-red-400");
    });

    cityInput.addEventListener("input", () => {
        cityInput.classList.remove("ring-2", "ring-red-400");
    });
});