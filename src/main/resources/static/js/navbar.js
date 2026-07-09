document.addEventListener("DOMContentLoaded", () => {
    const navbar = document.getElementById("navbar");
    const mobileMenuButton = document.getElementById("mobileMenuButton");
    const mobileMenu = document.getElementById("mobileMenu");

    if (!navbar || !mobileMenuButton || !mobileMenu) {
        return;
    }

    const toggleNavbarShadow = () => {
        if (window.scrollY > 20) {
            navbar.classList.add("shadow-2xl", "bg-slate-950");
        } else {
            navbar.classList.remove("shadow-2xl", "bg-slate-950");
        }
    };

    mobileMenuButton.addEventListener("click", () => {
        const isMenuOpen = !mobileMenu.classList.contains("hidden");

        mobileMenu.classList.toggle("hidden");
        mobileMenuButton.setAttribute("aria-expanded", String(!isMenuOpen));
    });

    window.addEventListener("scroll", toggleNavbarShadow);

    toggleNavbarShadow();
});