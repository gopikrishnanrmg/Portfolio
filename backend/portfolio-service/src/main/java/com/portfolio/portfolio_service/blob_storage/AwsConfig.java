package com.portfolio.portfolio_service.blob_storage;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

@Profile("prod")
@Configuration
public class AwsConfig {

    @Bean
    public S3Presigner s3Presigner() {
        return S3Presigner.builder()
                .credentialsProvider(ProfileCredentialsProvider.create())
                .build();
    }
}
