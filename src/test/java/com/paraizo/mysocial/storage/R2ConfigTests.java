package com.paraizo.mysocial.storage;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

import software.amazon.awssdk.services.s3.S3Client;

// Plain unit test, no Spring context and no network call: it only proves the S3Client
// builds correctly from R2Properties (endpoint, region, credentials). No bucket access happens here.
public class R2ConfigTests {

    @Test
    void buildsS3ClientFromProperties() {
        R2Properties properties = new R2Properties();
        properties.setAccountId("test-account-id");
        properties.setAccessKeyId("test-access-key");
        properties.setSecretAccessKey("test-secret-key");
        properties.setBucketName("test-bucket");
        properties.setPublicBaseUrl("https://pub-test.r2.dev");

        try (S3Client client = new R2Config().r2Client(properties)) {
            assertNotNull(client);
        }
    }
}
