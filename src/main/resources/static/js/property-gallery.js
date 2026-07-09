document.addEventListener("DOMContentLoaded", () => {
    const mainImage = document.getElementById("mainPropertyImage");
    const thumbnails = document.querySelectorAll(".gallery-thumbnail");

    if (!mainImage || thumbnails.length === 0) {
        return;
    }

    thumbnails.forEach((thumbnail) => {
        thumbnail.addEventListener("click", () => {
            const selectedImage = thumbnail.dataset.galleryImage;

            if (!selectedImage) {
                return;
            }

            mainImage.src = selectedImage;
            mainImage.alt = "Selected property image";
        });
    });
});