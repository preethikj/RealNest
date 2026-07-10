document.addEventListener("DOMContentLoaded", () => {

    const mobileMenuButton =
        document.getElementById("mobileMenuButton");

    const publicMobileMenu =
        document.getElementById("publicMobileMenu");

    const customerMobileMenu =
        document.getElementById("customerMobileMenu");

    const mobileMenu =
        publicMobileMenu || customerMobileMenu;

    if (!mobileMenuButton || !mobileMenu) {
        return;
    }

    mobileMenuButton.addEventListener("click", () => {

        const menuIsHidden =
            mobileMenu.classList.toggle("hidden");

        mobileMenuButton.setAttribute(
            "aria-expanded",
            String(!menuIsHidden)
        );
    });

});