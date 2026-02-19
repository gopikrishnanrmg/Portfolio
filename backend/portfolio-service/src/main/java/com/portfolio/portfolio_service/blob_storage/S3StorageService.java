package com.portfolio.portfolio_service.blob_storage;

import com.portfolio.portfolio_service.exceptions.StorageException;
import com.portfolio.portfolio_service.blob_storage.dtos.StorageResult;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.auth.credentials.EnvironmentVariableCredentialsProvider;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

import java.io.IOException;
import java.time.Duration;
import java.util.UUID;

@Profile("prod")
@Service
@RequiredArgsConstructor
public class S3StorageService implements StorageService<MultipartFile> {

    @Value("${S3_BUCKET}")
    private String bucket;
    private final S3Client s3;
    private final S3Presigner presigner;

    @Override
    public StorageResult upload(MultipartFile file) {
        try {
            String key = UUID.randomUUID().toString();

            PutObjectRequest putReq = PutObjectRequest.builder()
                    .bucket(bucket)
                    .key(key)
                    .contentType(file.getContentType())
                    .build();

            s3.putObject(putReq, RequestBody.fromBytes(file.getBytes()));

            GetObjectRequest getReq = GetObjectRequest.builder()
                    .bucket(bucket)
                    .key(key)
                    .build();

            GetObjectPresignRequest presignReq = GetObjectPresignRequest.builder()
                    .signatureDuration(Duration.ofMinutes(15))
                    .getObjectRequest(getReq)
                    .build();

            String url = presigner.presignGetObject(presignReq).url().toString();

            return new StorageResult(key, url);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String generatePresignedUrl(String key) {
        try {
            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(bucket)
                    .key(key)
                    .build();

            Duration expiration = Duration.ofMinutes(15);

            PresignedGetObjectRequest presignedRequest = presigner.presignGetObject(
                    GetObjectPresignRequest.builder()
                            .signatureDuration(expiration)
                            .getObjectRequest(getObjectRequest)
                            .build()
            );

            return presignedRequest.url().toString();

        } catch (Exception e) {
            throw new StorageException("Failed to generate presigned URL", e);
        }
    }

    @Override
    public void delete(String key) {
        try {
            s3.deleteObject(DeleteObjectRequest.builder()
                    .bucket(bucket)
                    .key(key)
                    .build());
        } catch (Exception e) {
            throw new StorageException("Failed to delete object", e);
        }
    }

}