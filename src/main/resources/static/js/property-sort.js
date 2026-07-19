document.addEventListener("DOMContentLoaded", () => {

    const sortForm =
        document.getElementById("propertySortForm");

    const sortFilter =
        document.getElementById("sortFilter");

    if (!sortForm || !sortFilter) {
        return;
    }

    /*
     * Submit immediately when the sorting method changes.
     * The sort form contains page=0, so a new sorting method
     * always begins from the first page.
     */
    sortFilter.addEventListener("change", () => {
        sortForm.requestSubmit();
    });
});