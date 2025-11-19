package com.portfolio.portfolio_service.blob_storage;

import com.portfolio.portfolio_service.exceptions.StorageException;
import com.portfolio.portfolio_service.blob_storage.dtos.StorageResult;
import lombok.RequiredArgsConstructor;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
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

import java.time.Duration;
import java.util.UUID;

@Profile("prod")
@Service
@RequiredArgsConstructor
public class S3StorageService implements StorageService {

    private final S3Presigner s3Presigner;
    private final String bucket = System.getenv("S3_BUCKET");
    private final Region region = Region.of(System.getenv("AWS_REGION"));
    private final S3Client s3 = S3Client.builder()
            .region(region)
            .credentialsProvider(EnvironmentVariableCredentialsProvider.create())
            .build();

    private final S3Presigner presigner = S3Presigner.builder()
            .region(region)
            .credentialsProvider(EnvironmentVariableCredentialsProvider.create())
            .build();


    @Override
    public StorageResult upload(byte[] data) {
        String key = UUID.randomUUID().toString();

        PutObjectRequest putReq = PutObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .build();

        s3.putObject(putReq, RequestBody.fromBytes(data));

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
    }

    @Override
    public String generatePresignedUrl(String key) {
        try {
            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(bucket)
                    .key(key)
                    .build();

            Duration expiration = Duration.ofMinutes(15);

            PresignedGetObjectRequest presignedRequest = s3Presigner.presignGetObject(
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