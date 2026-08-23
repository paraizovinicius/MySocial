package com.paraizo.mysocial.storage;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

// fetch information in application.properties so that endpoints can fetch data from the bucket
@Getter
@Setter
@ConfigurationProperties(prefix = "r2")
public class R2Properties {

    private String accountId;
    private String accessKeyId;
    private String secretAccessKey;
    private String bucketName;

    // where uploaded files are publicly reachable from (r2.dev URL or a custom domain), no trailing slash
    private String publicBaseUrl;
}
