package org.example.client;

import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.PutObjectResult;
import org.example.propertires.CloudProperties;
import org.example.propertires.CloudProvider;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.TestPropertySources;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@TestPropertySources({
    @TestPropertySource("classpath:.env.s3-buckets"),
    @TestPropertySource("classpath:.env.s3-objects")
})
class ObjectStorageClientTest {

    @Autowired
    CloudProperties cloudProperties;
    CloudObjectStorageClient objectStorageClient;
    CloudProperties.CloudObjectStorage objectStorage;
    private String bucketName;

    @BeforeEach
    void setup() {
        Optional<CloudProperties.CloudObjectStorage> optional = this.cloudProperties
            .getCloud().get(CloudProvider.NCP).stream().findFirst();

        Assertions.assertTrue(optional.isPresent());

        this.objectStorage = this.cloudProperties.getCloud().get(CloudProvider.NCP).get(0);
        this.bucketName = this.objectStorage.getObjectStorage().getBucketName();


        this.objectStorageClient = new CloudObjectStorageClient(
            this.objectStorage.getAuthentication().getAccessKey(),
            this.objectStorage.getAuthentication().getSecretKey(),
            this.objectStorage.getAuthentication().getEndpoint(),
            this.objectStorage.getAuthentication().getRegion()
        );
    }

    @Test
    @Order(0)
    @DisplayName("버킷 테스트 통합")
    void createBucket() {
        Bucket bucket = this.objectStorageClient.createBucket(bucketName);
        assertThat(bucket.getName()).isEqualTo(bucketName);
        assertThat(bucket).isNotNull();

        // 버킷이 잘 생성됐는지 확인한다.
        List<Bucket> bucketList = this.objectStorageClient.getBucketList();
        assertThat(bucketList).isNotEmpty();
        assertThat(this.getExistsBucket(bucketName)).isTrue();
        assertThat(bucketList).extracting(Bucket::getName).contains(bucketName);

        // 버킷을 삭제한다.
        boolean result = this.objectStorageClient.deleteBucket(bucketName);
        // 처기 결과가 정상적으로 처리 됐는지 확인한다.
        assertThat(result).isTrue();

        // 버킷이 삭제됐는지 확인한다.
        assertThat(getExistsBucket(bucketName)).isFalse();
    }

    private boolean getExistsBucket(String bucketName) {
        return this.objectStorageClient.existsBucket(bucketName);
    }
}

