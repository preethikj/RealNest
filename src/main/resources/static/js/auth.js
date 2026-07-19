document.addEventListener("DOMContentLoaded", () => {

    initializePasswordToggles();
    initializePasswordMatchValidation();
    initializeAuthenticationForms();
    initializeForgotPasswordModal();
});


function initializePasswordToggles() {

    const passwordToggleButtons =
        document.querySelectorAll("[data-password-toggle]");

    passwordToggleButtons.forEach((toggleButton) => {

        toggleButton.addEventListener("click", () => {

            const targetId =
                toggleButton.dataset.target;

            const passwordInput =
                document.getElementById(targetId);

            if (!passwordInput) {
                return;
            }

            const passwordIsCurrentlyVisible =
                passwordInput.type === "text";

            passwordInput.type =
                passwordIsCurrentlyVisible
                    ? "password"
                    : "text";

            const eyeOpenIcon =
                toggleButton.querySelector("[data-eye-open]");

            const eyeClosedIcon =
                toggleButton.querySelector("[data-eye-closed]");

            eyeOpenIcon?.classList.toggle(
                "hidden",
                !passwordIsCurrentlyVisible
            );

            eyeClosedIcon?.classList.toggle(
                "hidden",
                passwordIsCurrentlyVisible
            );

            toggleButton.setAttribute(
                "aria-label",
                passwordIsCurrentlyVisible
                    ? "Show password"
                    : "Hide password"
            );

            toggleButton.setAttribute(
                "aria-pressed",
                String(!passwordIsCurrentlyVisible)
            );
        });
    });
}


function initializePasswordMatchValidation() {

    const passwordInput =
        document.getElementById("password");

    const confirmPasswordInput =
        document.getElementById("confirmPassword");

    const passwordMismatchError =
        document.getElementById("passwordMismatchError");

    if (
        !passwordInput ||
        !confirmPasswordInput ||
        !passwordMismatchError
    ) {
        return;
    }

    const validatePasswordsMatch = () => {

        const confirmationHasValue =
            confirmPasswordInput.value.length > 0;

        const passwordsMatch =
            passwordInput.value === confirmPasswordInput.value;

        if (confirmationHasValue && !passwordsMatch) {

            confirmPasswordInput.setCustomValidity(
                "Passwords do not match."
            );

            passwordMismatchError.classList.remove("hidden");

            confirmPasswordInput.classList.add(
                "border-rose-400"
            );

            return false;
        }

        confirmPasswordInput.setCustomValidity("");

        passwordMismatchError.classList.add("hidden");

        confirmPasswordInput.classList.remove(
            "border-rose-400"
        );

        return true;
    };

    passwordInput.addEventListener(
        "input",
        validatePasswordsMatch
    );

    confirmPasswordInput.addEventListener(
        "input",
        validatePasswordsMatch
    );
}


function initializeAuthenticationForms() {

    const authenticationForms =
        document.querySelectorAll("[data-auth-form]");

    authenticationForms.forEach((form) => {

        form.addEventListener("submit", (event) => {

            const passwordInput =
                form.querySelector("#password");

            const confirmPasswordInput =
                form.querySelector("#confirmPassword");

            const passwordMismatchError =
                form.querySelector("#passwordMismatchError");

            if (
                passwordInput &&
                confirmPasswordInput &&
                passwordInput.value !== confirmPasswordInput.value
            ) {
                event.preventDefault();

                confirmPasswordInput.setCustomValidity(
                    "Passwords do not match."
                );

                passwordMismatchError?.classList.remove(
                    "hidden"
                );

                confirmPasswordInput.classList.add(
                    "border-rose-400"
                );

                confirmPasswordInput.focus();

                return;
            }

            if (!form.checkValidity()) {
                return;
            }

            const submitButton =
                event.submitter ||
                form.querySelector('button[type="submit"]');

            if (!submitButton) {
                return;
            }

            const buttonText =
                submitButton.querySelector("[data-button-text]");

            const loadingIcon =
                submitButton.querySelector("[data-loading-icon]");

            const loadingText =
                submitButton.dataset.loadingText;

            submitButton.disabled = true;

            if (buttonText && loadingText) {
                buttonText.textContent = loadingText;
            }

            loadingIcon?.classList.remove("hidden");
        });
    });
}

function initializeForgotPasswordModal() {

    const openModalButton =
        document.getElementById("openForgotPasswordModal");

    const closeModalButton =
        document.getElementById("closeForgotPasswordModal");

    const modal =
        document.getElementById("forgotPasswordModal");

    const emailInput =
        document.getElementById("forgotPasswordEmail");

    if (
        !openModalButton ||
        !closeModalButton ||
        !modal
    ) {
        return;
    }

    const openModal = () => {

        modal.classList.remove("hidden");
        modal.classList.add("flex");

        document.body.classList.add("overflow-hidden");

        emailInput?.focus();
    };

    const closeModal = () => {

        modal.classList.add("hidden");
        modal.classList.remove("flex");

        document.body.classList.remove("overflow-hidden");

        openModalButton.focus();
    };

    openModalButton.addEventListener(
        "click",
        openModal
    );

    closeModalButton.addEventListener(
        "click",
        closeModal
    );

    modal.addEventListener("click", (event) => {

        const clickedOutsideModalContent =
            event.target === modal;

        if (clickedOutsideModalContent) {
            closeModal();
        }
    });

    document.addEventListener("keydown", (event) => {

        const escapeWasPressed =
            event.key === "Escape";

        const modalIsOpen =
            !modal.classList.contains("hidden");

        if (escapeWasPressed && modalIsOpen) {
            closeModal();
        }
    });
}