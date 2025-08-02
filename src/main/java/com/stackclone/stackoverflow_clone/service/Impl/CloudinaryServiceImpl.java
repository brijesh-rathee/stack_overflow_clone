package com.stackclone.stackoverflow_clone.service.Impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.cloudinary.utils.ObjectUtils;
import com.stackclone.stackoverflow_clone.service.CloudinaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CloudinaryServiceImpl implements CloudinaryService {
    private static final int IMAGE_MAX_WIDTH = 220;
    private static final int IMAGE_MAX_HEIGHT = 220;

    private final Cloudinary cloudinary;

    public String uploadImageToCloudinary(MultipartFile imageFile) {
        try {
            Map uploadResult = cloudinary.uploader().upload(imageFile.getBytes(),
                    ObjectUtils.asMap(
                            "transformation", new Transformation().width(IMAGE_MAX_WIDTH).height(IMAGE_MAX_HEIGHT)
                                    .crop("fill").gravity("face")
                    ));

            return uploadResult.get("secure_url").toString();
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload image to Cloudinary", e);
        }
    }
}
