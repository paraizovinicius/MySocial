package com.paraizo.mysocial.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

// Unit tests only: S3Client is mocked, so nothing here ever reaches the real R2 bucket.
@ExtendWith(MockitoExtension.class)
class R2StorageServiceTests {

    private static final byte[] PNG_BYTES = {
            (byte) 0x89, 'P', 'N', 'G', 0x0D, 0x0A, 0x1A, 0x0A, 0, 0, 0, 0
    };

    @Mock
    private S3Client r2Client;

    private R2StorageService service() {
        R2Properties properties = new R2Properties();
        properties.setBucketName("test-bucket");
        properties.setPublicBaseUrl("https://pub-test.r2.dev");
        return new R2StorageService(r2Client, properties);
    }

    @Test
    void rejectsEmptyFile() {
        MockMultipartFile file = new MockMultipartFile("file", "a.png", "image/png", new byte[0]);

        assertThrows(InvalidImageException.class, () -> service().upload(file, "posts"));
        verifyNoInteractions(r2Client);
    }

    @Test
    void rejectsFileOverSizeLimit() {
        byte[] tooLarge = new byte[11 * 1024 * 1024];
        MockMultipartFile file = new MockMultipartFile("file", "a.png", "image/png", tooLarge);

        assertThrows(InvalidImageException.class, () -> service().upload(file, "posts"));
        verifyNoInteractions(r2Client);
    }

    @Test
    void rejectsContentThatIsNotARealImageRegardlessOfDeclaredContentType() {
        // declares image/png but the bytes are plain text - the sniff must catch this
        byte[] fakeContent = "<script>alert(1)</script>".getBytes();
        MockMultipartFile file = new MockMultipartFile("file", "innocent.png", "image/png", fakeContent);

        assertThrows(InvalidImageException.class, () -> service().upload(file, "posts"));
        verifyNoInteractions(r2Client);
    }

    @Test
    void ignoresClientSuppliedFilenameAndGeneratesItsOwnKey() {
        MockMultipartFile file = new MockMultipartFile(
                "file", "../../etc/passwd.png", "image/png", PNG_BYTES);

        String key = service().upload(file, "posts");

        assertTrue(key.startsWith("posts/"));
        assertTrue(key.endsWith(".png"));
        assertTrue(!key.contains(".."));
    }

    @Test
    void uploadsRealPngAndReturnsItsKey() {
        org.mockito.Mockito.when(r2Client.putObject(any(PutObjectRequest.class), any(RequestBody.class)))
                .thenReturn(PutObjectResponse.builder().build());

        MockMultipartFile file = new MockMultipartFile("file", "photo.png", "image/png", PNG_BYTES);

        String key = service().upload(file, "posts");

        assertTrue(key.matches("posts/[0-9a-fA-F-]{36}\\.png"));
        verify(r2Client).putObject(any(PutObjectRequest.class), any(RequestBody.class));
    }

    @Test
    void publicUrlPrependsConfiguredBase() {
        assertEquals("https://pub-test.r2.dev/posts/abc.png", service().publicUrl("posts/abc.png"));
    }
}
