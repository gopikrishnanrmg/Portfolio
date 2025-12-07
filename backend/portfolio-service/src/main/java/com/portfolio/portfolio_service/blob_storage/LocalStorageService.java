package com.portfolio.portfolio_service.blob_storage;

import com.portfolio.portfolio_service.blob_storage.dtos.StorageResult;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Profile("dev")
@Service
@Getter
public class LocalStorageService implements StorageService {

    @Value("${storage.base-url}")
    private String baseUrl;

    private final Map<String, byte[]> store = new ConcurrentHashMap<>();

    @Override
    public StorageResult upload(byte[] data) {
        String key = UUID.randomUUID().toString();
        store.put(key, data);
        return new StorageResult(key, baseUrl + "/" + key);
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

