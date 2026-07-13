package com.capstone.realNest.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CloudinaryService {

    private final Cloudinary cloudinary;

    public Map<?, ?> uploadImage(MultipartFile image) {

        try {
            return cloudinary.uploader().upload(
                    image.getBytes(),
                    ObjectUtils.asMap(
                            "folder", "realnest/properties",
                            "resource_type", "image"));
        } catch (IOException exception) {
            throw new RuntimeException(
                    "Failed to upload image to Cloudinary",
                    exception);
        }
    }

    public void deleteImage(String publicId) {

        try {
            cloudinary.uploader().destroy(
                    publicId,
                    ObjectUtils.emptyMap()
            );
        } catch (IOException exception) {
            throw new RuntimeException(
                    "Failed to delete image from Cloudinary",
                    exception);
        }
    }
}