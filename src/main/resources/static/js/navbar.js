document.addEventListener("DOMContentLoaded", () => {

    const mobileMenuButton =
        document.getElementById("mobileMenuButton");

    const publicMobileMenu =
        document.getElementById("publicMobileMenu");

    const customerMobileMenu =
        document.getElementById("customerMobileMenu");

    const adminMobileMenu =
        document.getElementById("adminMobileMenu");

    const mobileMenu =
        publicMobileMenu ||
        customerMobileMenu ||
        adminMobileMenu;

    if (!mobileMenuButton || !mobileMenu) {
        return;
    }

    const closeMobileMenu = () => {

        mobileMenu.classList.add("hidden");

        mobileMenuButton.setAttribute(
            "aria-expanded",
            "false"
        );
    };

    mobileMenuButton.addEventListener("click", () => {

        const menuIsHidden =
            mobileMenu.classList.toggle("hidden");

        mobileMenuButton.setAttribute(
            "aria-expanded",
            String(!menuIsHidden)
        );
    });

    document.addEventListener("keydown", (event) => {

        if (event.key === "Escape") {
            closeMobileMenu();
        }
    });

    window.addEventListener("resize", () => {

        if (window.innerWidth >= 1024) {
            closeMobileMenu();
        }
    });

});