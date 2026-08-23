package com.paraizo.mysocial.storage;

import org.springframework.web.multipart.MultipartFile;

public interface StorageService {

    // validates and uploads the file under the given folder (e.g. "posts", "avatars"),
    // returning the object's storage key. Callers use publicUrl(key) to build a servable URL,
    // rather than persisting a URL directly, so the public base (r2.dev vs. a future custom domain) can change freely.
    String upload(MultipartFile file, String folder);

    // accepts a key previously returned by upload()
    void delete(String key);

    String publicUrl(String key);
}
