package br.com.home.lab.softwaretesting.automation.aws.config;

import br.com.home.lab.softwaretesting.automation.config.Configurations;
import org.aeonbits.owner.ConfigFactory;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3Configuration;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

public final class AwsClientConfig {

    private static final Configurations config = ConfigFactory.create(Configurations.class);

    public static final String AWS_REGION = config.awsRegion();
    public static final String AWS_S3_ENDPOINT = config.awsS3Endpoint();
    private static final String AWS_SECRET_MANAGER_ACCESS_KEY = config.awsSecretManagerAccessKeyId();
    private static final String AWS_SECRET_MANAGER_SECRET_KEY_ID = config.awsSecretManagerSecretKeyId();
    public static final String AWS_ACCOUNT_ID = config.awsAccountId();

    private AwsClientConfig(){}

    public static S3Client createS3Client(){
        S3Configuration s3Configuration = S3Configuration
                .builder()
                .pathStyleAccessEnabled(true)
                .build();
        return S3Client.builder()
                .region(getAwsRegion())
                .endpointOverride(URI.create(AWS_S3_ENDPOINT))
                .serviceConfiguration(s3Configuration)
                .credentialsProvider(StaticCredentialsProvider.create(createAwsBasicCredentials()))
                .build();
    }

    public static Region getAwsRegion(){
        List<Region> validRegions = Region.regions();
        return validRegions.stream()
                .filter(r -> r.equals(Region.of(AWS_REGION)))
                .findFirst()
                .orElseThrow( () -> new IllegalArgumentException(
                        String.format("The informed region '%s' is not supported. Currently there are only these regions on AWS: %s",
                                AWS_REGION, validRegions.stream().map(Region::toString).collect(Collectors.joining(", "))))
                );
    }

    public static AwsBasicCredentials createAwsBasicCredentials(){
        return AwsBasicCredentials.create(AWS_SECRET_MANAGER_ACCESS_KEY, AWS_SECRET_MANAGER_SECRET_KEY_ID);
    }
}
