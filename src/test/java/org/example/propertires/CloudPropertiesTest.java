package org.example.propertires;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:.env.authentication-test")
class CloudPropertiesTest {
    @Autowired
    CloudProperties cloudProperties;

    @Test
    void cloudAuthenticationTest() {
        Assertions.assertNotNull(cloudProperties);
    }

    @Test
    void cloudAuthenticationDetailsTest() {
        Assertions.assertNotNull(cloudProperties.getCloud());
    }

    @Test
    void cloudAuthenticationDetailsContentTest() {
        assertEquals(1, cloudProperties.getCloud().size());
        assertTrue(cloudProperties.getCloud().containsKey(CloudProvider.NCP));
        assertEquals(1, cloudProperties.getCloud().get(CloudProvider.NCP).size());
        CloudProperties.CloudObjectStorage.CloudAuthentication authentication = cloudProperties.getCloud().get(CloudProvider.NCP).get(0).getAuthentication();
        assertEquals("accessKey", authentication.getAccessKey());
        assertEquals("secretKey", authentication.getSecretKey());
        assertEquals("endpoint", authentication.getEndpoint());
        assertEquals("kr-standard", authentication.getRegion());
        CloudProperties.CloudObjectStorage.CloudObjectStorageDetails storageDetails = cloudProperties.getCloud().get(CloudProvider.NCP).get(0).getObjectStorage();
        assertEquals("bucketName", storageDetails.getBucketName());
        assertEquals("objectPath", storageDetails.getObjectPath());
        assertEquals("objectName", storageDetails.getObjectName());
        assertEquals("lockAlgorithmKey", storageDetails.getAlgorithm());
        assertEquals("filePath", storageDetails.getLocalFile());
    }
}