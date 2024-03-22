package org.example.propertires;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.context.annotation.PropertySource;

import java.util.List;
import java.util.Map;

@PropertySource("classpath:.env")
@ConfigurationProperties(prefix = "application")
public class CloudProperties {

    @Getter
    private final Map<CloudProvider, List<CloudAuthentication>> cloud;
    @ConstructorBinding
    public CloudProperties(Map<CloudProvider, List<CloudAuthentication>> cloud) {
        this.cloud = cloud;
    }

    @Getter
    public static class CloudAuthentication {
        private final String accessKey;
        private final String secretKey;
        private final String endpoint;

        @ConstructorBinding
        public CloudAuthentication(String accessKey, String secretKey, String endpoint) {
            this.accessKey = accessKey;
            this.secretKey = secretKey;
            this.endpoint = endpoint;
        }
    }
}
