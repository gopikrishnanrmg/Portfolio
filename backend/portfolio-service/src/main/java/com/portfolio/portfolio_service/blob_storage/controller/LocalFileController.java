package com.portfolio.portfolio_service.blob_storage.controller;

import com.portfolio.portfolio_service.blob_storage.LocalStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Profile("dev")
@RestController
@RequestMapping("/api/v1/files/")
@RequiredArgsConstructor
public class LocalFileController {

    private final LocalStorageService storageService;

    @GetMapping("/{key}")
    public ResponseEntity<byte[]> getFile(@PathVariable String key) {
        byte[] data = storageService.getStore().get(key);
        String mimeType = storageService.getMimeType(key);

        if (data == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, mimeType != null ? mimeType : "application/octet-stream")
                .body(data);
    }
}
