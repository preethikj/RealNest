document.addEventListener("DOMContentLoaded", () => {
    const searchForm =
        document.getElementById("propertySearchForm");

    const cityInput =
        document.getElementById("searchCity");

    const suggestions =
        document.getElementById("citySuggestions");

    const cityOptions =
        document.querySelectorAll(".city-option");

    if (!searchForm || !cityInput) {
        return;
    }

    const showMatchingCities = () => {
        if (!suggestions) {
            return;
        }

        const enteredCity =
            cityInput.value.trim().toLowerCase();

        let matchingCityExists = false;

        cityOptions.forEach((option) => {
            const city =
                option.textContent.trim().toLowerCase();

            const matches =
                city.includes(enteredCity);

            option.classList.toggle("hidden", !matches);

            if (matches) {
                matchingCityExists = true;
            }
        });

        suggestions.classList.toggle(
            "hidden",
            !matchingCityExists
        );
    };

    cityInput.addEventListener("focus", () => {
        showMatchingCities();
    });

    cityInput.addEventListener("input", () => {
        cityInput.classList.remove(
            "ring-2",
            "ring-red-400"
        );

        showMatchingCities();
    });

    cityOptions.forEach((option) => {
        option.addEventListener("click", () => {
            cityInput.value =
                option.textContent.trim();

            suggestions?.classList.add("hidden");
        });
    });

    document.addEventListener("click", (event) => {
        if (
            suggestions &&
            !cityInput.contains(event.target) &&
            !suggestions.contains(event.target)
        ) {
            suggestions.classList.add("hidden");
        }
    });

    searchForm.addEventListener("submit", (event) => {
        const city = cityInput.value.trim();

        if (city.length < 2) {
            event.preventDefault();

            cityInput.classList.add(
                "ring-2",
                "ring-red-400"
            );

            cityInput.focus();
            return;
        }

        cityInput.classList.remove(
            "ring-2",
            "ring-red-400"
        );
    });
});