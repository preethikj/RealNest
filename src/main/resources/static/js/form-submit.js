document.addEventListener("DOMContentLoaded", () => {

    const forms =
        document.querySelectorAll("[data-loading-form]");

    forms.forEach((form) => {

        form.addEventListener("submit", () => {

            const submitButton =
                form.querySelector("[data-loading-submit]");

            if (!submitButton || submitButton.disabled) {
                return;
            }

            const buttonText =
                submitButton.querySelector(
                    "[data-button-text]"
                );

            const loadingIcon =
                submitButton.querySelector(
                    "[data-loading-icon]"
                );

            submitButton.disabled = true;

            if (buttonText) {
                buttonText.textContent =
                    submitButton.dataset.loadingText
                    || "Submitting...";
            }

            if (loadingIcon) {
                loadingIcon.classList.remove("hidden");
            }
        });
    });
});