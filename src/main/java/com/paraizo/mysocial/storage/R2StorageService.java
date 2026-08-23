package com.paraizo.mysocial.storage;

import java.io.IOException;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import software.amazon.awssdk.core.exception.SdkException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Service
public class R2StorageService implements StorageService {

    // keep in sync with spring.servlet.multipart.max-file-size in application.properties,
    // which rejects oversized uploads before we ever read them into memory
    private static final long MAX_FILE_SIZE_BYTES = 10L * 1024 * 1024;

    private final S3Client r2Client;
    private final R2Properties properties;

    // S3Client was built in R2Config and is being instantiated here
    // R2Properties is also a bean. Created in R2Properties
    public R2StorageService(S3Client r2Client, R2Properties properties) {
        this.r2Client = r2Client;
        this.properties = properties;
    }

    @Override
    public String upload(MultipartFile file, String folder) {
        byte[] content = readAndValidate(file);

        // the bucket is public, so the client-supplied filename/Content-Type is never trusted:
        // the format below is sniffed from the file's actual bytes, and the key is generated
        // server-side rather than derived from the client's filename
        ImageFormat format = ImageFormat.detect(content);
        if (format == null) {
            throw new InvalidImageException("File is not a recognized JPEG, PNG, or WEBP image");
        }

        String key = folder + "/" + UUID.randomUUID() + format.extension;

        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(properties.getBucketName())
                .key(key)
                .contentType(format.contentType)
                .build();

        try {
            r2Client.putObject(request, RequestBody.fromBytes(content));
        } catch (SdkException e) {
            throw new StorageException("Failed to upload image to R2", e);
        }

        return key;
    }

    @Override
    public void delete(String key) {
        r2Client.deleteObject(DeleteObjectRequest.builder()
                .bucket(properties.getBucketName())
                .key(key)
                .build());
    }

    @Override
    public String publicUrl(String key) {
        return properties.getPublicBaseUrl() + "/" + key;
    }

    private byte[] readAndValidate(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new InvalidImageException("File is empty");
        }
        if (file.getSize() > MAX_FILE_SIZE_BYTES) {
            throw new InvalidImageException("File exceeds the " + (MAX_FILE_SIZE_BYTES / (1024 * 1024)) + "MB limit");
        }
        try {
            return file.getBytes();
        } catch (IOException e) {
            throw new StorageException("Failed to read uploaded file", e);
        }
    }
}
