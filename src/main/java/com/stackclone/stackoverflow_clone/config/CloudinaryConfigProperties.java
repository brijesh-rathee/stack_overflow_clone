package com.stackclone.stackoverflow_clone.config;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "cloudinary")
public class CloudinaryConfigProperties {
    private String cloudName;
    private String apiKey;
    private String apiSecret;

}