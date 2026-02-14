package br.com.home.lab.softwaretesting.automation.config;


import br.com.home.lab.softwaretesting.automation.selenium.webdriver.test.Browser;
import org.aeonbits.owner.Config;


@Config.Sources({
        "classpath:configuration.properties"
})
public interface Configurations extends Config {

    @Key("app.url")
    String appUrl();

    @Key("api.url")
    String apiUrl();

    boolean headless();

    Browser browser();

    String nameUser();

    String username();

    String password();

    String language();

    @Key("cucumber.name.user")
    String cucumberNameUser();

    @Key("cucumber.username")
    String cucumberUser();

    @Key("cucumber.password")
    String cucumberPassword();


    /*
     * AWS configuration
     */
    @Key("aws.s3.endpoint")
    String awsS3Endpoint();
    String awsS3Bucket();

    String awsS3Folder();

    @Key("aws.secret.manager.access.key.id")
    String awsSecretManagerAccessKeyId();

    @Key("aws.secret.manager.secret.key.id")
    String awsSecretManagerSecretKeyId();

    @Key("aws.region")
    String awsRegion();

    @Key("aws.account-id")
    String awsAccountId();

    @Key("aws.lambda.endpoint")
    String awsLambdaEndpoint();

    @Key("asw.lambda.function.name")
    String awsLambdaFunctionName();

    @Key("aws.secretsmanager.db-credentials")
    String awsSecretsManagerDbCredentials();

    /*
        Kafka properties
     */
    @Key("kafka.incoming.messages.topic.name")
    String kafkaIncomingMessagesTopicName();

    @Key("kafka.bootstrap.servers")
    String kafkaBootstrapServers();

    @Key("kafka.group.id")
    String kafkaGroupId();

    @Key("db.url")
    String dbUrl();

    @Key("db.username")
    String dbUsername();

    @Key("db.password")
    String dbPassword();
}
