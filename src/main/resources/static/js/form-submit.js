document.addEventListener("DOMContentLoaded", () => {

    const forms = document.querySelectorAll("[data-loading-form]");

    forms.forEach((form) => {

        const cancelLinks = form.querySelectorAll("[data-submit-cancel]");

        cancelLinks.forEach((cancelLink) => {

            cancelLink.addEventListener("click", (event) => {

                if (cancelLink.getAttribute("aria-disabled") === "true") {
                    event.preventDefault();
                }
            });
        });

        form.addEventListener("submit", () => {

            const submitButton = form.querySelector("[data-loading-submit]");

            if (!submitButton || submitButton.disabled) {
                return;
            }

            const buttonText = submitButton.querySelector("[data-button-text]");

            const loadingIcon = submitButton.querySelector("[data-loading-icon]");

            submitButton.disabled = true;

            if (buttonText) {
                buttonText.textContent = submitButton.dataset.loadingText || "Submitting...";
            }

            if (loadingIcon) {
                loadingIcon.classList.remove("hidden");
            }

            disableCancelLinks(cancelLinks);
        });
    });
});


function disableCancelLinks(cancelLinks) {

    cancelLinks.forEach((cancelLink) => {

        cancelLink.setAttribute("aria-disabled", "true");

        cancelLink.setAttribute("tabindex", "-1");

        cancelLink.classList.add("pointer-events-none", "cursor-not-allowed", "opacity-50");
    });
}