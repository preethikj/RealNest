document.addEventListener("DOMContentLoaded", () => {

    const fileInput = document.getElementById("propertyImages");
    const previewSection = document.getElementById("imagePreviewSection");
    const previewGrid = document.getElementById("imagePreviewGrid");
    const clearImagesButton = document.getElementById("clearImagesButton");
    const uploadMessage = document.getElementById("imageUploadMessage");

    if (
        !fileInput ||
        !previewSection ||
        !previewGrid ||
        !clearImagesButton ||
        !uploadMessage
    ) {
        return;
    }

    const maxImages = 8;
    const maxFileSize = 10 * 1024 * 1024;

    const allowedTypes = [
        "image/jpeg",
        "image/png",
        "image/webp"
    ];

    let selectedFiles = [];

    fileInput.addEventListener("change", () => {

        hideMessage();

        const newFiles = Array.from(fileInput.files);

        if (newFiles.length === 0) {
            return;
        }

        const invalidTypeFiles = newFiles.filter(
            (file) => !allowedTypes.includes(file.type)
        );

        if (invalidTypeFiles.length > 0) {
            showMessage(
                "Only JPG, PNG and WEBP images are allowed.",
                "error"
            );

            resetFileInput();
            return;
        }

        const oversizedFiles = newFiles.filter(
            (file) => file.size > maxFileSize
        );

        if (oversizedFiles.length > 0) {
            showMessage(
                "Each image must be smaller than 10 MB.",
                "error"
            );

            resetFileInput();
            return;
        }

        if (selectedFiles.length >= maxImages) {
            showMessage(
                "Maximum 8 images allowed. Remove an existing image before adding another.",
                "warning"
            );

            updateFileInput();
            return;
        }

        const remainingSlots = maxImages - selectedFiles.length;

        if (newFiles.length > remainingSlots) {
            selectedFiles.push(...newFiles.slice(0, remainingSlots));

            showMessage(
                `You can upload a maximum of 8 images. Only ${remainingSlots} image${remainingSlots === 1 ? "" : "s"} were added.`,
                "warning"
            );
        } else {
            selectedFiles.push(...newFiles);
        }

        updateFileInput();
        renderPreviews();
    });

    clearImagesButton.addEventListener("click", () => {
        selectedFiles = [];
        hideMessage();
        updateFileInput();
        renderPreviews();
    });

    function removeImage(index) {
        selectedFiles.splice(index, 1);
        hideMessage();
        updateFileInput();
        renderPreviews();
    }

    function resetFileInput() {
        fileInput.value = "";
    }

    function updateFileInput() {
        const dataTransfer = new DataTransfer();

        selectedFiles.forEach((file) => {
            dataTransfer.items.add(file);
        });

        fileInput.files = dataTransfer.files;
    }

    function showMessage(message, type) {
        uploadMessage.textContent = message;

        uploadMessage.className =
            "mt-4 rounded-xl border px-4 py-3 text-sm font-medium";

        if (type === "error") {
            uploadMessage.classList.add(
                "border-rose-200",
                "bg-rose-50",
                "text-rose-700"
            );
        } else {
            uploadMessage.classList.add(
                "border-amber-200",
                "bg-amber-50",
                "text-amber-700"
            );
        }
    }

    function hideMessage() {
        uploadMessage.textContent = "";
        uploadMessage.className =
            "mt-4 hidden rounded-xl border px-4 py-3 text-sm font-medium";
    }

    function renderPreviews() {
        previewGrid.innerHTML = "";

        if (selectedFiles.length === 0) {
            previewSection.classList.add("hidden");
            return;
        }

        previewSection.classList.remove("hidden");

        selectedFiles.forEach((file, index) => {

            const imageUrl = URL.createObjectURL(file);

            const previewCard = document.createElement("article");

            previewCard.className =
                "group relative overflow-hidden rounded-2xl " +
                "border border-slate-200 bg-slate-100";

            previewCard.innerHTML = `
                <img
                    src="${imageUrl}"
                    alt="Property preview ${index + 1}"
                    class="h-36 w-full object-cover"
                >

                ${
                index === 0
                    ? `
                            <span
                                class="absolute left-3 top-3 rounded-full
                                       bg-emerald-500 px-3 py-1
                                       text-xs font-bold text-slate-950">
                                Cover
                            </span>
                          `
                    : ""
            }

                <button
                    type="button"
                    aria-label="Remove image ${index + 1}"
                    class="absolute right-3 top-3 inline-flex h-9 w-9
                           items-center justify-center rounded-full
                           bg-slate-950/80 text-white backdrop-blur
                           transition hover:bg-rose-500">
                    <svg xmlns="http://www.w3.org/2000/svg"
                         fill="none"
                         viewBox="0 0 24 24"
                         stroke-width="1.8"
                         stroke="currentColor"
                         class="h-5 w-5">
                        <path stroke-linecap="round"
                              stroke-linejoin="round"
                              d="M6 18 18 6M6 6l12 12"/>
                    </svg>
                </button>
            `;

            previewCard
                .querySelector("button")
                .addEventListener("click", () => {
                    URL.revokeObjectURL(imageUrl);
                    removeImage(index);
                });

            previewGrid.appendChild(previewCard);
        });
    }

});