package com.portfolio.portfolio_service.blob_storage;

import com.portfolio.portfolio_service.blob_storage.dtos.StorageResult;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Profile("test")
public class TestStorageService implements StorageService {

    @Override
    public StorageResult upload(byte[] data) {
        String key = UUID.randomUUID().toString();
        String url = "http://localhost/fake-url/" + key;
        return new StorageResult(key, url);
    }

    @Override
    public String generatePresignedUrl(String key) {
        return "http://localhost/fake-url/" + key;
    }

    @Override
    public void delete(String key) {}
}
