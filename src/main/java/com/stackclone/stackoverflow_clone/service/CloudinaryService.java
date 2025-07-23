package com.stackclone.stackoverflow_clone.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface CloudinaryService {
    String uploadImageToCloudinary(MultipartFile file);
}
