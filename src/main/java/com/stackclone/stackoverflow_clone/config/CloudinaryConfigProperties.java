package com.stackclone.stackoverflow_clone.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
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