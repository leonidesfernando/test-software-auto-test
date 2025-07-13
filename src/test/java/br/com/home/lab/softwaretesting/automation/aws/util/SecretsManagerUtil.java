package br.com.home.lab.softwaretesting.automation.aws.util;

import br.com.home.lab.softwaretesting.automation.aws.config.AwsClientConfig;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;
import software.amazon.awssdk.services.secretsmanager.model.DeleteSecretRequest;
import software.amazon.awssdk.services.secretsmanager.model.DeleteSecretResponse;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueRequest;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueResponse;
import software.amazon.awssdk.services.secretsmanager.model.SecretsManagerException;

import java.net.URI;
import java.util.Map;

import static br.com.home.lab.softwaretesting.automation.util.JsonUtils.jsonToKeyValue;

public final class SecretsManagerUtil {

    private SecretsManagerUtil() {}

    public static void createSecret(String secretName, String secretValue){
        try(SecretsManagerClient client = createSecretManager()) {
            client.createSecret(builder -> builder.name(secretName).secretString(secretValue));
        }catch (SecretsManagerException e){
            throw new IllegalStateException("Error creating the secret: ", e);
        }
    }


    public static String getSecret(String secretName){
        try(SecretsManagerClient client = createSecretManager()){
            GetSecretValueRequest request = GetSecretValueRequest
                    .builder()
                    .secretId(secretName)
                    .build();
            GetSecretValueResponse response = client.getSecretValue(request);
            return response.secretString();
        }catch (SecretsManagerException e){
            throw new IllegalStateException("Error retrieving the secret: ", e);
        }
    }

    public static boolean deleteSecret(String secretName){
        try(SecretsManagerClient client = createSecretManager()){
            DeleteSecretRequest request = DeleteSecretRequest
                    .builder()
                    .secretId(secretName)
                    .forceDeleteWithoutRecovery(true)
                    .build();
            DeleteSecretResponse response = client.deleteSecret(request);
            return response.sdkHttpResponse().isSuccessful();
        }catch (SecretsManagerException e){
            throw new IllegalStateException("Error on delete secret: ", e);
        }
    }

    public static Map<String, String> getSecretKeyValue(String secretName){
        String secretJson = getSecret(secretName);
        return jsonToKeyValue(secretJson);
    }

    private static SecretsManagerClient createSecretManager(){
        return SecretsManagerClient.builder()
                .credentialsProvider(StaticCredentialsProvider.create(AwsClientConfig.createAwsBasicCredentials()))
                .region(AwsClientConfig.getAwsRegion())
                .endpointOverride(URI.create(AwsClientConfig.AWS_S3_ENDPOINT))
                .build();
    }
}
