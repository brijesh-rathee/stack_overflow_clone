package com.stackclone.stackoverflow_clone.config;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class CloudinaryConfig {
    private static final String CLOUD_NAME = "cloud_name";
    private static final String API_KEY = "api_key";
    private static final String API_SECRET = "api_secret";

    private final CloudinaryConfigProperties cloudinaryConfigProperties;

    @Bean
    public Cloudinary getCloudinary() {
        return new Cloudinary(ObjectUtils.asMap(
                CLOUD_NAME, cloudinaryConfigProperties.getCloudName(),
                API_KEY, cloudinaryConfigProperties.getApiKey(),
                API_SECRET, cloudinaryConfigProperties.getApiSecret()));
    }
}
