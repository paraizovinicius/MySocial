package com.paraizo.mysocial.storage;

import java.util.Arrays;

// Detects image format from the actual file bytes (magic numbers), not from the
// client-supplied filename or Content-Type header, which are trivially spoofable.
public enum ImageFormat {

    JPEG(".jpg", "image/jpeg") {
        @Override
        boolean matches(byte[] header) {
            return header.length >= 3
                    && (header[0] & 0xFF) == 0xFF
                    && (header[1] & 0xFF) == 0xD8
                    && (header[2] & 0xFF) == 0xFF;
        }
    },
    PNG(".png", "image/png") {
        @Override
        boolean matches(byte[] header) {
            byte[] signature = { (byte) 0x89, 'P', 'N', 'G', 0x0D, 0x0A, 0x1A, 0x0A };
            return startsWith(header, signature);
        }
    },
    WEBP(".webp", "image/webp") {
        @Override
        boolean matches(byte[] header) {
            // RIFF <4-byte size> WEBP
            return header.length >= 12
                    && header[0] == 'R' && header[1] == 'I' && header[2] == 'F' && header[3] == 'F'
                    && header[8] == 'W' && header[9] == 'E' && header[10] == 'B' && header[11] == 'P';
        }
    };

    final String extension;
    final String contentType;

    ImageFormat(String extension, String contentType) {
        this.extension = extension;
        this.contentType = contentType;
    }

    abstract boolean matches(byte[] header);

    static ImageFormat detect(byte[] bytes) {
        return Arrays.stream(values())
                .filter(format -> format.matches(bytes))
                .findFirst()
                .orElse(null);
    }

    private static boolean startsWith(byte[] data, byte[] prefix) {
        if (data.length < prefix.length) {
            return false;
        }
        for (int i = 0; i < prefix.length; i++) {
            if (data[i] != prefix[i]) {
                return false;
            }
        }
        return true;
    }
}
