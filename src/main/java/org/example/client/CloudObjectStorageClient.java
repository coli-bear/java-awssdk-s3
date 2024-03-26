package org.example.client;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;
import lombok.extern.slf4j.Slf4j;
import org.example.propertires.CloudProperties;

import java.io.File;
import java.util.List;
import java.util.Optional;


@Slf4j
public class CloudObjectStorageClient {
    private final AWSStaticCredentialsProvider credentialsProvider;
    private final AwsClientBuilder.EndpointConfiguration endpointConfiguration;
    private final AmazonS3 amazonS3;

    public CloudObjectStorageClient(String accessKey, String secretKey, String endpoint, String region) {
        final BasicAWSCredentials basicAWSCredentials = new BasicAWSCredentials(accessKey, secretKey);
        this.credentialsProvider = new AWSStaticCredentialsProvider(basicAWSCredentials);
        this.endpointConfiguration = new AwsClientBuilder.EndpointConfiguration(endpoint, region);
        this.amazonS3 = AmazonS3ClientBuilder.standard()
            .withCredentials(this.credentialsProvider)
            .withEndpointConfiguration(this.endpointConfiguration)
            .build();
    }

    public List<Bucket> getBucketList() {
        return this.amazonS3.listBuckets();
    }

    public Bucket createBucket(final String bucketName) {
        Optional<Bucket> bucketOptional = this.getBucketList().stream().filter(bucket -> {
                log.info("createBucket() Find Bucket: {}", bucket.getName());
                return bucket.getName().equals(bucketName);
            })
            .findFirst();

        if (bucketOptional.isPresent()) {
            return bucketOptional.get();
        }


        CreateBucketRequest createBucketRequest = new CreateBucketRequest(bucketName);
        createBucketRequest.withObjectLockEnabledForBucket(true);
        createBucketRequest.putCustomQueryParameter("x-amz-bucket-object-lock-enabled", "true");
        return this.amazonS3.createBucket(createBucketRequest);
    }

    public boolean existsBucket(String bucketName) {
        return this.getBucketList().stream().anyMatch(bucket -> {
            log.info("existsBucket() Find Bucket: {}", bucket.getName());
            return bucket.getName().equals(bucketName);
        });
    }

    public boolean deleteBucket(String bucketName) {
        try {
            // 먼저 버킷 내 객체를 삭제한다.
            this.clearBucketObject(bucketName);

            // 버킷 삭제 전 버전이 지정된 객체또한 삭제해야 한다.
            this.clearBucketVersionObject(bucketName);

            // 버킷을 삭제한다.
            this.amazonS3.deleteBucket(bucketName);
            return true;
        } catch (RuntimeException e) {
            log.error("Error: {}", e.getMessage());
            return false;
        }
    }

    private void clearBucketVersionObject(String bucketName) {
        VersionListing versionListing = this.amazonS3.listVersions(new ListVersionsRequest().withBucketName(bucketName));
        while(true) {
            versionListing.getVersionSummaries()
                .forEach(versionSummary -> this.deleteVersion(bucketName, versionSummary));
            if (versionListing.isTruncated()) {
                versionListing = this.amazonS3.listNextBatchOfVersions(versionListing);
            } else {
                break;
            }
        }
    }

    private void deleteVersion(String bucketName, S3VersionSummary versionSummary) {
        this.amazonS3.deleteVersion(bucketName, versionSummary.getKey(), versionSummary.getVersionId());
    }

    private void clearBucketObject(String bucketName) {
        ObjectListing objectListing = this.amazonS3.listObjects(bucketName);
        while (true) {
            objectListing.getObjectSummaries()
                .forEach(s3ObjectSummary -> this.deleteObject(bucketName, s3ObjectSummary));

            if (objectListing.isTruncated()) {
                this.amazonS3.listNextBatchOfObjects(objectListing);
            } else {
                break;
            }
        }
    }

    private void deleteObject(String bucketName, S3ObjectSummary s3ObjectSummary) {
        this.amazonS3.deleteObject(bucketName, s3ObjectSummary.getKey());

    }

    public PutObjectResult putObject(CloudProperties.CloudObjectStorage.CloudObjectStorageDetails cloudObjectStorageDetails) {
        final ObjectMetadata metadata = new ObjectMetadata();
        metadata.setSSEAlgorithm(ObjectMetadata.AES_256_SERVER_SIDE_ENCRYPTION);
        File uploadFile = new File(cloudObjectStorageDetails.getLocalFile());
        final PutObjectRequest putObjectRequest = new PutObjectRequest(cloudObjectStorageDetails.getBucketName(), cloudObjectStorageDetails.getAlgorithm(), uploadFile);
        return this.amazonS3.putObject(putObjectRequest.withMetadata(metadata)
            .withObjectLockMode(ObjectLockMode.COMPLIANCE));
    }
}
