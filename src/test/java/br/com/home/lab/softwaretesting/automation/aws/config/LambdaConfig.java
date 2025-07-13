package br.com.home.lab.softwaretesting.automation.aws.config;

import br.com.home.lab.softwaretesting.automation.config.Configurations;
import org.aeonbits.owner.ConfigFactory;

public final class LambdaConfig {

    private static final Configurations config = ConfigFactory.create(Configurations.class);
    private static final String AWS_LAMBDA_FUNCTION_NAME = config.awsLambdaFunctionName();
    public static final String AWS_LAMBDA_ENDPOINT = config.awsLambdaEndpoint();

    private LambdaConfig(){}

    public static String getFullAwsLambdaFunctionName(){
        // arn:aws:lambda:us-east-1:000000000000:function:my-lambda
        return String.format("arn:aws:lambda:%s:%s:function:%s",
                AwsClientConfig.AWS_REGION,
                AwsClientConfig.AWS_ACCOUNT_ID,
                AWS_LAMBDA_FUNCTION_NAME);
    }
}
