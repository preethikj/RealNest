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
     * Display only city suggestions matching the
     * value currently entered by the user.
     */
    const showMatchingCities = () => {

        if (!citySuggestions) {
            return;
        }

        const enteredCity = cityInput.value.trim().toLowerCase();

        let matchExists = false;

        cityOptions.forEach((option) => {

            const city = option.textContent.trim().toLowerCase();

            const matches = city.includes(enteredCity);

            option.classList.toggle("hidden", !matches);

            if (matches) {
                matchExists = true;
            }
        });

        citySuggestions.classList.toggle("hidden", !matchExists);
    };


    /*
     * City suggestions
     */
    if (citySuggestions) {

        cityInput.addEventListener("focus", () => {
            showMatchingCities();
        });

        cityInput.addEventListener("input", () => {

            const currentCity = cityInput.value.trim().toLowerCase();

            cityChanged = currentCity !== originalCity;

            if (cityChanged) {
                localityInput.value = "";

                localitySuggestions?.classList.add("hidden");
            }

            showMatchingCities();
        });

        cityOptions.forEach((option) => {

            option.addEventListener("click", () => {

                cityInput.value = option.textContent.trim();

                localityInput.value = "";

                citySuggestions.classList.add("hidden");

                /*
                 * Reload the listing page for the selected city.
                 * This also loads the correct locality suggestions.
                 */
                searchForm.requestSubmit();
            });
        });
    }


    /*
     * Locality suggestions
     */
    if (localitySuggestions) {

        localityInput.addEventListener("focus", () => {

            /*
             * Do not display localities belonging to the old city
             * if the city has been changed but not submitted yet.
             */
            if (!cityChanged) {
                localitySuggestions.classList.remove("hidden");
            }
        });

        localityOptions.forEach((option) => {

            option.addEventListener("click", () => {

                localityInput.value = option.textContent.trim();

                localitySuggestions.classList.add("hidden");

                searchForm.requestSubmit();
            });
        });
    }


    /*
     * Close both suggestion panels when clicking outside them.
     */
    document.addEventListener("click", (event) => {

        const clickedInsideCity = cityInput.contains(event.target) || citySuggestions?.contains(event.target);

        const clickedInsideLocality = localityInput.contains(event.target) || localitySuggestions?.contains(event.target);

        if (!clickedInsideCity) {
            citySuggestions?.classList.add("hidden");
        }

        if (!clickedInsideLocality) {
            localitySuggestions?.classList.add("hidden");
        }
    });


    /*
     * Clear a failed locality search and keep the user
     * on the property-list page.
     */
    if (searchAgainButton) {

        searchAgainButton.addEventListener("click", (event) => {

            event.preventDefault();
            event.stopPropagation();

            localityInput.value = "";
            localityInput.focus();

            if (localitySuggestions && !cityChanged) {
                localitySuggestions.classList.remove("hidden");
            }
        });
    }
});