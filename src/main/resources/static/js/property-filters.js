document.addEventListener("DOMContentLoaded", () => {

    const filterForm =
        document.getElementById("propertyFilterForm");

    const listingTypeFilter =
        document.getElementById("listingTypeFilter");

    const budgetFilter =
        document.getElementById("budgetFilter");

    const minPriceInput =
        document.getElementById("minPriceFilter");

    const maxPriceInput =
        document.getElementById("maxPriceFilter");

    if (
        !filterForm ||
        !listingTypeFilter ||
        !budgetFilter ||
        !minPriceInput ||
        !maxPriceInput
    ) {
        return;
    }

    /*
     * Restore the selected budget after the server reloads
     * the page with minPrice and maxPrice parameters.
     */
    const restoreSelectedBudget = () => {

        const selectedMin =
            minPriceInput.value.trim();

        const selectedMax =
            maxPriceInput.value.trim();

        const matchingOption =
            Array.from(budgetFilter.options).find((option) => {

                if (option.disabled) {
                    return false;
                }

                return (
                    option.dataset.min === selectedMin &&
                    option.dataset.max === selectedMax
                );
            });

        if (matchingOption) {
            budgetFilter.value = matchingOption.value;
        }
    };

    /*
     * Convert the selected budget into the minPrice and
     * maxPrice parameters expected by the backend.
     */
    const updatePriceInputs = () => {

        const selectedOption =
            budgetFilter.options[budgetFilter.selectedIndex];

        minPriceInput.value =
            selectedOption.dataset.min || "";

        maxPriceInput.value =
            selectedOption.dataset.max || "";
    };

    budgetFilter.addEventListener(
        "change",
        updatePriceInputs
    );

    /*
     * Sale and Rent require different budget ranges.
     * Clear an old budget and reload immediately when
     * the listing type changes.
     */
    listingTypeFilter.addEventListener("change", () => {

        minPriceInput.value = "";
        maxPriceInput.value = "";
        budgetFilter.value = "";

        filterForm.requestSubmit();
    });

    filterForm.addEventListener("submit", () => {
        updatePriceInputs();
    });

    restoreSelectedBudget();
});