package com.portfolio.portfolio_service.blob_storage;

import com.portfolio.portfolio_service.blob_storage.dtos.StorageResult;
import org.springframework.web.multipart.MultipartFile;

public interface StorageService<T extends MultipartFile> {
    StorageResult upload(T data);
    String generatePresignedUrl(String key);
    void delete(String key);
}
