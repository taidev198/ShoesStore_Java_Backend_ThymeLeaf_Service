package com.taidev198.service;

import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

public interface CloudinaryService {
    Map<String, Object> uploadFile(MultipartFile file);

    Map<String, Object> deleteFileByUrl(String url);

    Map<String, Object> deleteFile(String publicId);
}
