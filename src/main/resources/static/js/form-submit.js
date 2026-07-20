document.addEventListener("DOMContentLoaded", () => {

    const forms = document.querySelectorAll("[data-loading-form]");

    forms.forEach((form) => {

        const cancelControls = form.querySelectorAll("[data-loading-cancel]");

        cancelControls.forEach((cancelControl) => {

            cancelControl.addEventListener("click", (event) => {

                const controlIsDisabled = cancelControl.getAttribute("aria-disabled") === "true";

                if (controlIsDisabled) {
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

            loadingIcon?.classList.remove("hidden");

            disableCancelControls(cancelControls);
        });
    });
});


function disableCancelControls(cancelControls) {

    cancelControls.forEach((cancelControl) => {

        cancelControl.setAttribute("aria-disabled", "true");

        cancelControl.setAttribute("tabindex", "-1");

        cancelControl.classList.add("cursor-not-allowed", "opacity-50");

        /*
         * Inline pointer-events ensures the link
         * is blocked even if Tailwind does not detect
         * a dynamically added class.
         */
        cancelControl.style.pointerEvents = "none";
    });
}