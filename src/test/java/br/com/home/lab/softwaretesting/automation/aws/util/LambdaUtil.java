package br.com.home.lab.softwaretesting.automation.aws.util;

import br.com.home.lab.softwaretesting.automation.aws.config.AwsClientConfig;
import br.com.home.lab.softwaretesting.automation.aws.config.LambdaConfig;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.services.lambda.LambdaClient;
import software.amazon.awssdk.services.lambda.model.InvokeRequest;
import software.amazon.awssdk.services.lambda.model.InvokeResponse;

import java.net.URI;

@Slf4j
public final class LambdaUtil {

    private LambdaUtil(){}

    public static String callLambdaFunction(LambdaClient lambdaClient){
        InvokeRequest invokeRequest = InvokeRequest.builder()
                .functionName(LambdaConfig.getFullAwsLambdaFunctionName())
                .build();
        InvokeResponse invokeResponse = lambdaClient.invoke(invokeRequest);
        return new String(invokeResponse.payload().asByteArray());
    }

    public static LambdaClient createLambdaClient() {
        AwsBasicCredentials awsBasicCredentials = AwsClientConfig.createAwsBasicCredentials();
        return LambdaClient.builder()
                .endpointOverride(URI.create(LambdaConfig.AWS_LAMBDA_ENDPOINT))
                .region(AwsClientConfig.getAwsRegion())
                .credentialsProvider(StaticCredentialsProvider.create(awsBasicCredentials))
                .build();
    }
}
