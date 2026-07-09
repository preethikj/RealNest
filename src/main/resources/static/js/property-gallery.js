document.addEventListener("DOMContentLoaded", () => {
    const imageSources = document.querySelectorAll(".gallery-image-source");

    const images = Array.from(imageSources)
        .map((image) => image.dataset.galleryImage)
        .filter(Boolean);

    let currentIndex = 0;

    const galleryModal = document.getElementById("galleryModal");
    const modalImage = document.getElementById("modalGalleryImage");
    const galleryCounter = document.getElementById("galleryCounter");

    const openButton = document.getElementById("openGalleryModal");
    const closeButton = document.getElementById("closeGalleryModal");
    const previousButton = document.getElementById("previousGalleryImage");
    const nextButton = document.getElementById("nextGalleryImage");

    if (!galleryModal || !modalImage || images.length === 0) {
        return;
    }

    function updateGallery() {
        galleryCounter.textContent = `${currentIndex + 1} / ${images.length}`;

        modalImage.classList.add("opacity-0");

        setTimeout(() => {
            modalImage.src = images[currentIndex];
            modalImage.alt = `Property image ${currentIndex + 1}`;

            modalImage.onload = () => {
                modalImage.classList.remove("opacity-0");
            };
        }, 150);
    }

    function openGallery() {
        currentIndex = 0;
        updateGallery();
        galleryModal.classList.remove("hidden");
        document.body.classList.add("overflow-hidden");
    }

    function closeGallery() {
        galleryModal.classList.add("hidden");
        document.body.classList.remove("overflow-hidden");
    }

    function showNextImage() {
        currentIndex = (currentIndex + 1) % images.length;
        updateGallery();
    }

    function showPreviousImage() {
        currentIndex = (currentIndex - 1 + images.length) % images.length;
        updateGallery();
    }

    openButton?.addEventListener("click", openGallery);
    closeButton?.addEventListener("click", closeGallery);
    nextButton?.addEventListener("click", showNextImage);
    previousButton?.addEventListener("click", showPreviousImage);

    galleryModal.addEventListener("click", (event) => {
        if (event.target === galleryModal) {
            closeGallery();
        }
    });

    document.addEventListener("keydown", (event) => {
        if (galleryModal.classList.contains("hidden")) {
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
    });
});