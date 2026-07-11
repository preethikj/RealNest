document.addEventListener("DOMContentLoaded", () => {

    const showRejectFormButton =
        document.getElementById("showRejectFormButton");

    const rejectPropertyForm =
        document.getElementById("rejectPropertyForm");

    const cancelRejectButton =
        document.getElementById("cancelRejectButton");

    const rejectionReason =
        document.getElementById("rejectionReason");

    if (!showRejectFormButton ||
        !rejectPropertyForm ||
        !cancelRejectButton ||
        !rejectionReason) {
        return;
    }

    showRejectFormButton.addEventListener("click", () => {

        rejectPropertyForm.classList.remove("hidden");
        showRejectFormButton.classList.add("hidden");

        showRejectFormButton.setAttribute(
            "aria-expanded",
            "true"
        );

        rejectionReason.focus();
    });

    cancelRejectButton.addEventListener("click", () => {

        rejectPropertyForm.classList.add("hidden");
        showRejectFormButton.classList.remove("hidden");

        showRejectFormButton.setAttribute(
            "aria-expanded",
            "false"
        );

        rejectionReason.value = "";
        showRejectFormButton.focus();
    });

});