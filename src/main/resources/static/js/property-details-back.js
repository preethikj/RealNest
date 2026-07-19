document.addEventListener("DOMContentLoaded", () => {

    const backButton =
        document.getElementById("propertyDetailsBackButton");

    if (!backButton) {
        return;
    }

    backButton.addEventListener("click", () => {

        const fallbackUrl =
            backButton.dataset.fallbackUrl || "/properties";

        const currentUrl =
            new URL(window.location.href);

        const enquiryWasSubmitted =
            currentUrl.searchParams.get("enquirySent") === "true";

        const cameFromEnquiryForm =
            document.referrer.includes("/enquiry");

        /*
         * Do not navigate backwards through an enquiry POST.
         * Doing that causes the browser's form-resubmission warning.
         */
        if (enquiryWasSubmitted || cameFromEnquiryForm) {
            window.location.href = fallbackUrl;
            return;
        }

        const cameFromRealNest =
            document.referrer.startsWith(window.location.origin);

        if (cameFromRealNest && window.history.length > 1) {
            window.history.back();
            return;
        }

        window.location.href = fallbackUrl;
    });
});