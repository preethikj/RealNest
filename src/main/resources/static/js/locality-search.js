document.addEventListener("DOMContentLoaded", () => {

    const searchForm = document.getElementById("localitySearchForm");

    const cityInput = document.getElementById("listingCityInput");

    const citySuggestions = document.getElementById("listingCitySuggestions");

    const cityOptions = document.querySelectorAll(".listing-city-option");

    const localityInput = document.getElementById("localityInput");

    const localitySuggestions = document.getElementById("localitySuggestions");

    const localityOptions = document.querySelectorAll(".locality-option");

    const searchAgainButton = document.getElementById("searchAgainButton");

    if (!searchForm || !cityInput || !localityInput) {
        return;
    }

    const originalCity = (cityInput.dataset.selectedCity || "")
        .trim()
        .toLowerCase();

    let cityChanged = false;


    /*
     * Show every city option.
     */
    const showAllCities = () => {

        if (!citySuggestions || cityOptions.length === 0) {
            return;
        }

        cityOptions.forEach((option) => {
            option.classList.remove("hidden");
        });

        citySuggestions.classList.remove("hidden");
    };


    /*
     * Show only the cities matching the text
     * currently entered by the user.
     */
    const showMatchingCities = () => {

        if (!citySuggestions) {
            return;
        }

        const enteredCity = cityInput.value
            .trim()
            .toLowerCase();

        let matchExists = false;

        cityOptions.forEach((option) => {

            const city = option.textContent
                .trim()
                .toLowerCase();

            const matches = city.includes(enteredCity);

            option.classList.toggle("hidden", !matches);

            if (matches) {
                matchExists = true;
            }
        });

        citySuggestions.classList.toggle("hidden", !matchExists);
    };


    /*
     * Show every locality option.
     */
    const showAllLocalities = () => {

        if (!localitySuggestions || localityOptions.length === 0 || cityChanged) {
            return;
        }

        localityOptions.forEach((option) => {
            option.classList.remove("hidden");
        });

        localitySuggestions.classList.remove("hidden");
    };


    /*
     * Show only localities matching the text
     * currently entered by the user.
     */
    const showMatchingLocalities = () => {

        if (!localitySuggestions || cityChanged) {
            return;
        }

        const enteredLocality = localityInput.value
            .trim()
            .toLowerCase();

        let matchExists = false;

        localityOptions.forEach((option) => {

            const locality = option.textContent
                .trim()
                .toLowerCase();

            const matches = locality.includes(enteredLocality);

            option.classList.toggle("hidden", !matches);

            if (matches) {
                matchExists = true;
            }
        });

        localitySuggestions.classList.toggle("hidden", !matchExists);
    };


    /*
     * City suggestions.
     *
     * Clicking or focusing shows all cities.
     * Typing filters the available cities.
     */
    if (citySuggestions) {

        cityInput.addEventListener("focus", showAllCities);

        cityInput.addEventListener("click", showAllCities);

        cityInput.addEventListener("input", () => {

            const currentCity = cityInput.value
                .trim()
                .toLowerCase();

            cityChanged = currentCity !== originalCity;

            if (cityChanged) {

                localityInput.value = "";

                localitySuggestions
                    ?.classList.add("hidden");
            }

            showMatchingCities();
        });

        cityOptions.forEach((option) => {

            option.addEventListener("click", () => {

                cityInput.value = option.textContent.trim();

                localityInput.value = "";

                citySuggestions.classList.add("hidden");

                /*
                 * Reload the listing page for the selected
                 * city and load its locality suggestions.
                 */
                searchForm.requestSubmit();
            });
        });
    }


    /*
     * Locality suggestions.
     *
     * Clicking or focusing shows all localities.
     * Typing filters the available localities.
     */
    if (localitySuggestions) {

        localityInput.addEventListener("focus", showAllLocalities);

        localityInput.addEventListener("click", showAllLocalities);

        localityInput.addEventListener("input", showMatchingLocalities);

        localityOptions.forEach((option) => {

            option.addEventListener("click", () => {

                localityInput.value = option.textContent.trim();

                localitySuggestions.classList.add("hidden");

                searchForm.requestSubmit();
            });
        });
    }


    /*
     * Close suggestion panels when clicking
     * outside their related controls.
     */
    document.addEventListener("click", (event) => {

        const clickedInsideCity = cityInput.contains(event.target) || citySuggestions?.contains(event.target);

        const clickedInsideLocality = localityInput.contains(event.target) || localitySuggestions?.contains(event.target);

        if (!clickedInsideCity) {

            citySuggestions
                ?.classList.add("hidden");
        }

        if (!clickedInsideLocality) {

            localitySuggestions
                ?.classList.add("hidden");
        }
    });


    /*
     * Escape closes both dropdowns.
     */
    document.addEventListener("keydown", (event) => {

        if (event.key !== "Escape") {
            return;
        }

        citySuggestions
            ?.classList.add("hidden");

        localitySuggestions
            ?.classList.add("hidden");
    });


    /*
     * Clear a failed locality search and keep
     * the user on the property-list page.
     */
    if (searchAgainButton) {

        searchAgainButton.addEventListener("click", (event) => {

            event.preventDefault();
            event.stopPropagation();

            localityInput.value = "";

            localityInput.focus();

            showAllLocalities();
        });
    }
});