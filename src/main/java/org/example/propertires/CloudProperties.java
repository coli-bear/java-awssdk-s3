package org.example.propertires;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.context.annotation.PropertySource;

import java.util.List;
import java.util.Map;

@Getter
@PropertySource("classpath:.env")
@ConfigurationProperties(prefix = "application")
public class CloudProperties {

    private final Map<CloudProvider, List<CloudObjectStorage>> cloud;

    @ConstructorBinding
    public CloudProperties(Map<CloudProvider, List<CloudObjectStorage>> cloud) {
        this.cloud = cloud;
    }

    @Getter
    public static class CloudObjectStorage {
        private final CloudAuthentication authentication;
        private final CloudObjectStorageDetails objectStorage;

        @ConstructorBinding
        public CloudObjectStorage(CloudAuthentication authentication, CloudObjectStorageDetails objectStorage) {
            this.authentication = authentication;
            this.objectStorage = objectStorage;
        }

        @Getter
        public static class CloudAuthentication {
            private final String accessKey;
            private final String secretKey;
            private final String endpoint;
            private final String region;

            @ConstructorBinding
            public CloudAuthentication(String accessKey, String secretKey, String endpoint, String region) {
                this.accessKey = accessKey;
                this.secretKey = secretKey;
                this.endpoint = endpoint;
                this.region = region;
            }
        }

        @Getter
        public static class CloudObjectStorageDetails {
            private final String localFile;
            private final String bucketName;
            private final String objectPath;
            private final String objectName;
            private final String algorithm;

            @ConstructorBinding
            private CloudObjectStorageDetails(String localFile, String bucketName, String objectPath, String objectName, String algorithm) {
                this.localFile = localFile;
                this.bucketName = bucketName;
                this.objectPath = objectPath;
                this.objectName = objectName;
                this.algorithm = algorithm;
            }
        }
    }
}
