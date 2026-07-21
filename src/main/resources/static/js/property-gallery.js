document.addEventListener("DOMContentLoaded", () => {

    const imageSources =
        document.querySelectorAll(
            ".gallery-image-source"
        );

    const images =
        Array.from(imageSources)
            .map((image) =>
                image.dataset.galleryImage
            )
            .filter(Boolean);

    const galleryModal =
        document.getElementById("galleryModal");

    const modalImage =
        document.getElementById("modalGalleryImage");

    const galleryCounter =
        document.getElementById("galleryCounter");

    const openButtons =
        document.querySelectorAll(
            "[data-gallery-open]"
        );

    const closeButton =
        document.getElementById(
            "closeGalleryModal"
        );

    const previousButton =
        document.getElementById(
            "previousGalleryImage"
        );

    const nextButton =
        document.getElementById(
            "nextGalleryImage"
        );

    if (
        !galleryModal ||
        !modalImage ||
        images.length === 0
    ) {
        return;
    }

    let currentIndex = 0;
    let touchStartX = null;
    let previouslyFocusedElement = null;


    function updateGallery() {

        modalImage.src =
            images[currentIndex];

        modalImage.alt =
            `Property image ${currentIndex + 1}`;

        if (galleryCounter) {

            galleryCounter.textContent =
                `${currentIndex + 1} / ${images.length}`;
        }
    }


    function openGallery(startIndex = 0) {

        const requestedIndex =
            Number(startIndex);

        currentIndex =
            Number.isInteger(requestedIndex) &&
            requestedIndex >= 0 &&
            requestedIndex < images.length
                ? requestedIndex
                : 0;

        previouslyFocusedElement =
            document.activeElement;

        updateGallery();

        galleryModal.classList.remove("hidden");

        document.body.classList.add(
            "overflow-hidden"
        );

        closeButton?.focus();
    }


    function closeGallery() {

        galleryModal.classList.add("hidden");

        document.body.classList.remove(
            "overflow-hidden"
        );

        previouslyFocusedElement?.focus();
    }


    function showNextImage() {

        if (images.length <= 1) {
            return;
        }

        currentIndex =
            (currentIndex + 1) % images.length;

        updateGallery();
    }


    function showPreviousImage() {

        if (images.length <= 1) {
            return;
        }

        currentIndex =
            (currentIndex - 1 + images.length) %
            images.length;

        updateGallery();
    }


    openButtons.forEach((button) => {

        button.addEventListener("click", () => {

            openGallery(
                Number(button.dataset.galleryIndex)
            );
        });
    });


    closeButton?.addEventListener(
        "click",
        closeGallery
    );

    nextButton?.addEventListener(
        "click",
        showNextImage
    );

    previousButton?.addEventListener(
        "click",
        showPreviousImage
    );


    if (images.length === 1) {

        previousButton?.classList.add("hidden");

        nextButton?.classList.add("hidden");
    }


    galleryModal.addEventListener(
        "click",
        (event) => {

            if (event.target === galleryModal) {
                closeGallery();
            }
        }
    );


    modalImage.addEventListener(
        "touchstart",
        (event) => {

            touchStartX =
                event.changedTouches[0].clientX;
        },
        { passive: true }
    );


    modalImage.addEventListener(
        "touchend",
        (event) => {

            if (touchStartX === null) {
                return;
            }

            const touchEndX =
                event.changedTouches[0].clientX;

            const swipeDistance =
                touchStartX - touchEndX;

            if (Math.abs(swipeDistance) >= 50) {

                if (swipeDistance > 0) {
                    showNextImage();
                } else {
                    showPreviousImage();
                }
            }

            touchStartX = null;
        },
        { passive: true }
    );


    document.addEventListener(
        "keydown",
        (event) => {

            if (
                galleryModal.classList.contains(
                    "hidden"
                )
            ) {
                return;
            }

            if (event.key === "Escape") {
                closeGallery();
            }

            if (event.key === "ArrowRight") {
                showNextImage();
            }

            if (event.key === "ArrowLeft") {
                showPreviousImage();
            }
        }
    );
});