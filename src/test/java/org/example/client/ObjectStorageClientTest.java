package org.example.client;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.ActiveProfiles;
import software.amazon.awssdk.services.s3.S3Client;

@SpringBootTest
@PropertySource("classpath:.env.s3-test")
@ActiveProfiles("test")
public class ObjectStorageClientTest {

    @Test
    void createS3Client() {

        Assertions.assertNotNull(httpClient);
        S3Client s3Client = S3Client.builder()
            .httpClient(httpClient)
            .build();



    }
}

