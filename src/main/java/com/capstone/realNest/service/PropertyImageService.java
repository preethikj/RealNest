package com.capstone.realNest.service;

import com.capstone.realNest.dto.response.PropertyImageResponse;
import com.capstone.realNest.entity.Property;
import com.capstone.realNest.entity.PropertyImage;
import com.capstone.realNest.exception.PropertyImageNotFoundException;
import com.capstone.realNest.exception.PropertyNotFoundException;
import com.capstone.realNest.repository.PropertyImageRepository;
import com.capstone.realNest.repository.PropertyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PropertyImageService {

    private static final int MAX_IMAGES = 8;

    private final PropertyImageRepository propertyImageRepository;
    private final PropertyRepository propertyRepository;
    private final CloudinaryService cloudinaryService;

    //Rest API
    @Transactional
    public List<PropertyImageResponse> uploadImages(
            Long propertyId,
            Long ownerId,
            List<MultipartFile> images) {

        Property property = propertyRepository.findByIdAndOwnerId(propertyId, ownerId)
                .orElseThrow(() -> new PropertyNotFoundException(propertyId));

        List<PropertyImage> existingImages =
                propertyImageRepository.findByPropertyIdOrderByDisplayOrderAsc(propertyId);

        int remainingSlots = MAX_IMAGES - existingImages.size();

        if (remainingSlots <= 0) {
            return List.of();
        }

        int nextDisplayOrder = existingImages.stream()
                .mapToInt(PropertyImage::getDisplayOrder)
                .max()
                .orElse(0) + 1;

        List<PropertyImage> uploadedImages = new ArrayList<>();

        for (MultipartFile image : images) {

            if (uploadedImages.size() >= remainingSlots) {
                break;
            }

            if (image == null || image.isEmpty()) {
                continue;
            }

            Map<?, ?> uploadResult = cloudinaryService.uploadImage(image);

            PropertyImage propertyImage =
                    PropertyImage.builder().property(property)
                            .imageUrl(uploadResult.get("secure_url").toString())
                            .publicId(uploadResult.get("public_id").toString())
                            .displayOrder(nextDisplayOrder++)
                            .build();

            uploadedImages.add(propertyImage);
        }

        return propertyImageRepository
                .saveAll(uploadedImages)
                .stream()
                .map(this::toResponse)
                .toList();
    }
    private PropertyImageResponse toResponse(PropertyImage propertyImage) {

        return new PropertyImageResponse(
                propertyImage.getId(),
                propertyImage.getProperty().getId(),
                propertyImage.getImageUrl(),
                propertyImage.getPublicId(),
                propertyImage.getDisplayOrder()
        );
    }

    /*Get Images by property ID - Rest API*/
    @Transactional(readOnly = true)
    public List<PropertyImageResponse> getImagesByPropertyId(Long propertyId) {

        if (!propertyRepository.existsById(propertyId)) {
            throw new PropertyNotFoundException(propertyId);
        }

        return propertyImageRepository
                .findByPropertyIdOrderByDisplayOrderAsc(propertyId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public void deleteImage(Long propertyId, Long imageId, Long ownerId) {

        propertyRepository.findByIdAndOwnerId(propertyId, ownerId)
                .orElseThrow(() -> new PropertyNotFoundException(propertyId));

        long imageCount = propertyImageRepository.countByPropertyId(propertyId);

        if (imageCount <= 1) {
            throw new IllegalStateException(
                    "A property must have at least one image");
        }

        PropertyImage propertyImage =
                propertyImageRepository
                        .findByIdAndPropertyId(imageId, propertyId)
                        .orElseThrow(() ->
                                new PropertyImageNotFoundException(
                                        imageId,
                                        propertyId));

        cloudinaryService.deleteImage(propertyImage.getPublicId());
        propertyImageRepository.delete(propertyImage);
    }
}