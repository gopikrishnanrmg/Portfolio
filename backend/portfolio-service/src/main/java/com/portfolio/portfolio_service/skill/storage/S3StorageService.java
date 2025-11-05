package com.portfolio.portfolio_service.skill.storage;

import com.portfolio.portfolio_service.skill.exceptions.StorageException;
import org.springframework.stereotype.Component;

import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.time.Duration;
import java.util.UUID;

@Component
public class S3StorageService implements StorageService {

    private final String bucket = System.getenv("S3_BUCKET");
    private final Region region = Region.of(System.getenv("AWS_REGION"));
    private final S3Client s3 = S3Client.builder().region(region).build();
    private final S3Presigner presigner = S3Presigner.builder().region(region).build();

    @Override
    public String upload(byte[] data) {
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

        return presigner.presignGetObject(presignReq).url().toString();
    }

    @Override
    public byte[] download(String url) {
        String key = url.substring(url.lastIndexOf("/") + 1);

        InputStream inputStream = s3.getObject(GetObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .build());

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            inputStream.transferTo(baos);
            return baos.toByteArray();
        } catch (Exception e) {
            throw new StorageException("Failed to download object", e);
        }
    }
}