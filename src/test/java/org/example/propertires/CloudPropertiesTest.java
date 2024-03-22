package org.example.propertires;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

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
        Assertions.assertEquals(1, cloudProperties.getCloud().size());
        Assertions.assertTrue(cloudProperties.getCloud().containsKey(CloudProvider.NCP));
        Assertions.assertEquals(1, cloudProperties.getCloud().get(CloudProvider.NCP).size());
        Assertions.assertEquals("accessKey", cloudProperties.getCloud().get(CloudProvider.NCP).get(0).getAccessKey());
        Assertions.assertEquals("secretKey", cloudProperties.getCloud().get(CloudProvider.NCP).get(0).getSecretKey());
        Assertions.assertEquals("endpoint", cloudProperties.getCloud().get(CloudProvider.NCP).get(0).getEndpoint());
    }
}