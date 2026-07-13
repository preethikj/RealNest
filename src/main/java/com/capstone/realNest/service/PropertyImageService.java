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

    @Transactional
    public List<PropertyImageResponse> uploadImages(Long propertyId, List<MultipartFile> images) {

        Property property = propertyRepository.findById(propertyId)
                .orElseThrow(() ->
                        new PropertyNotFoundException(propertyId)
                );

        List<PropertyImage> existingImages = propertyImageRepository.findByPropertyIdOrderByDisplayOrderAsc(propertyId);

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

            PropertyImage propertyImage = new PropertyImage();
            propertyImage.setProperty(property);
            propertyImage.setImageUrl(uploadResult.get("secure_url").toString());
            propertyImage.setPublicId(uploadResult.get("public_id").toString());
            propertyImage.setDisplayOrder(nextDisplayOrder++);

            uploadedImages.add(propertyImage);
        }

        return propertyImageRepository.saveAll(uploadedImages)
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

    /*Get Images by property ID*/
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
    public void deleteImage(Long propertyId, Long imageId) {

        if (!propertyRepository.existsById(propertyId)) {
            throw new PropertyNotFoundException(propertyId);
        }

        PropertyImage propertyImage = propertyImageRepository
                        .findByIdAndPropertyId(imageId, propertyId)
                        .orElseThrow(() ->
                                new PropertyImageNotFoundException(imageId, propertyId));

        cloudinaryService.deleteImage(propertyImage.getPublicId());
        propertyImageRepository.delete(propertyImage);
    }
}