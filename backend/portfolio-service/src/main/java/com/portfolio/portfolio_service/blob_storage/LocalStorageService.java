package com.portfolio.portfolio_service.blob_storage;

import com.portfolio.portfolio_service.blob_storage.dtos.StorageResult;
import com.portfolio.portfolio_service.exceptions.FileProcessingException;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Profile("dev")
@Service
@Getter
public class LocalStorageService implements StorageService<MultipartFile> {

    @Value("${storage.base-url}")
    private String baseUrl;

    private final Map<String, byte[]> store = new ConcurrentHashMap<>();
    private final Map<String, String> mimeTypes = new ConcurrentHashMap<>();

    @Override
    public StorageResult upload(MultipartFile file) {
        String key = UUID.randomUUID().toString();
        try {
            store.put(key, file.getBytes());
            mimeTypes.put(key, file.getContentType());
            return new StorageResult(key, baseUrl + "/" + key);
        } catch (IOException e) {
            throw new FileProcessingException("Failed to process uploaded file", e);
        }
    }

    public String getMimeType(String key) {
        return mimeTypes.get(key);
    }

    @Override
    public String generatePresignedUrl(String key) {
        return baseUrl + "/" + key;
    }

    @Override
    public void delete(String key) {
        store.remove(key);
    }
}

