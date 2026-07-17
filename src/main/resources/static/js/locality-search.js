document.addEventListener("DOMContentLoaded", () => {

    const searchForm =
        document.getElementById("localitySearchForm");

    const localityInput =
        document.getElementById("localityInput");

    const suggestions =
        document.getElementById("localitySuggestions");

    const options =
        document.querySelectorAll(".locality-option");

    const searchAgainButton =
        document.getElementById("searchAgainButton");

    if (!searchForm || !localityInput) {
        return;
    }

    // Display locality suggestions when the input receives focus.
    if (suggestions) {

        localityInput.addEventListener("focus", () => {
            suggestions.classList.remove("hidden");
        });

        // Select a suggested locality and submit the search.
        options.forEach((option) => {
            option.addEventListener("click", () => {

                localityInput.value =
                    option.textContent.trim();

                suggestions.classList.add("hidden");

                searchForm.requestSubmit();
            });
        });

        // Close suggestions when clicking outside the search area.
        document.addEventListener("click", (event) => {

            const clickedInsideInput =
                localityInput.contains(event.target);

            const clickedInsideSuggestions =
                suggestions.contains(event.target);

            if (
                !clickedInsideInput &&
                !clickedInsideSuggestions
            ) {
                suggestions.classList.add("hidden");
            }
        });
    }

    /*
     * Keep the user on the property-list page when no results
     * are found. Clear the previous locality and reopen the
     * available locality suggestions.
     */
    if (searchAgainButton) {

        searchAgainButton.addEventListener("click", (event) => {

            event.preventDefault();
            event.stopPropagation();

            localityInput.value = "";
            localityInput.focus();

            if (suggestions) {
                suggestions.classList.remove("hidden");
            }
        });
    }
});