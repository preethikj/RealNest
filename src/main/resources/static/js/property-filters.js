document.addEventListener("DOMContentLoaded", () => {

    const filterForm =
        document.getElementById("propertyFilterForm");

    const budgetFilter =
        document.getElementById("budgetFilter");

    const minPriceInput =
        document.getElementById("minPriceFilter");

    const maxPriceInput =
        document.getElementById("maxPriceFilter");

    if (
        !filterForm ||
        !budgetFilter ||
        !minPriceInput ||
        !maxPriceInput
    ) {
        return;
    }

    /*
     * Restore the selected budget after the server reloads
     * the page with minPrice and maxPrice query parameters.
     */
    const restoreSelectedBudget = () => {

        const selectedMin =
            minPriceInput.value.trim();

        const selectedMax =
            maxPriceInput.value.trim();

        const matchingOption =
            Array.from(budgetFilter.options).find((option) => {

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
     * Convert the selected budget range into the hidden
     * minPrice and maxPrice fields used by Spring MVC.
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

    filterForm.addEventListener("submit", () => {
        updatePriceInputs();
    });

    restoreSelectedBudget();
});