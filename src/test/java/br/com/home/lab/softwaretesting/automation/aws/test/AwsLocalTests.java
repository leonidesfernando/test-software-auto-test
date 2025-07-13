package br.com.home.lab.softwaretesting.automation.aws.test;

import br.com.home.lab.softwaretesting.automation.aws.config.AwsClientConfig;
import br.com.home.lab.softwaretesting.automation.aws.s3.S3File;
import br.com.home.lab.softwaretesting.automation.aws.util.LambdaUtil;
import br.com.home.lab.softwaretesting.automation.aws.util.S3Util;
import br.com.home.lab.softwaretesting.automation.aws.util.SecretsManagerUtil;
import br.com.home.lab.softwaretesting.automation.util.LoadConfigurationUtil;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.Test;
import software.amazon.awssdk.services.lambda.LambdaClient;
import software.amazon.awssdk.services.s3.S3Client;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class AwsLocalTests {

    private final S3Client s3Client = AwsClientConfig.createS3Client();
    private final LambdaClient lambdaClient = LambdaUtil.createLambdaClient();

    @Test
    public void checkAwsConnectionTest() {
        String healthCheckUrl = "http://localhost:4566/_localstack/health";

        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(healthCheckUrl).openConnection();
            connection.setRequestMethod("GET");
            int responseCode = connection.getResponseCode();

            assertThat(responseCode).isEqualTo(200);
        }catch (IOException e){
            throw new IllegalStateException("Check your connection and you AWS access. If you are running local, check if localstack is running", e);
        }
    }
    /***
    Localstack commands

    list S3 buckets
    awslocal s3 ls

    list all files from a S3 bucket
    awslocal
     */

    @Test
    public void createBucketPutAndGetFilesDeleteFromS3Test() {
        final String bucketName = "initial-step-bucket";

        if(!S3Util.bucketExists(s3Client, bucketName)){
            S3Util.createBucketByName(s3Client, bucketName);
        }else{
            log.info("Bucket {} already exists", bucketName);
        }

        final String fileBucketName = "s3/clients.csv";
        Path filePath = Paths.get("src/test/resources/" + fileBucketName);
        S3Util.putObject(s3Client, bucketName, fileBucketName, filePath);

        final Path fileNameRetrieved = Paths.get(System.getProperty("java.io.tmpdir") + "fileNameRetrieved.csv");
        fileNameRetrieved.toFile().delete();

        S3File s3File = S3Util.getObject(s3Client, bucketName, fileBucketName, fileNameRetrieved);
        log.info("File downloaded from S3: {}", s3File);
        assertThat(s3File.filePathName()).isEqualTo(fileNameRetrieved.toAbsolutePath().toString());

        List<S3File> files =  S3Util.getAll(s3Client, bucketName);
        assertThat(files).hasSize(1);

        assertThat(S3Util.deleteObject(s3Client, bucketName, fileBucketName)).isTrue();
    }

    @Test
    public void callLambdaFunctionTest(){
        String response = LambdaUtil.callLambdaFunction(lambdaClient);
        assertThat(response).contains("200");
    }

    @Test
    public void secretsManagerTest(){
        Map<String, String> dbCredentials = LoadConfigurationUtil.getSecretsDbCredentials();
        log.info("Db credentials: {}", dbCredentials);

        String secretValue = "Localstack secretValue";
        String secretKey = "localstackSecretKey";
        SecretsManagerUtil.createSecret(secretKey, secretValue);
        String readValue =  SecretsManagerUtil.getSecret(secretKey);
        assertThat(secretValue).isEqualTo(readValue);
        assertThat(SecretsManagerUtil.deleteSecret(secretKey)).isTrue();
    }
}
