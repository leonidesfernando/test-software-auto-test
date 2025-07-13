# test-software-auto-test
Project to explorer several tests such as API, e2e, integration, load and stress with: TestNG, Mockito, REST Assured,
Cucumber, Selenium Webdriver
, [JMeter](src/test/jmeter/README-JMETER.md) and [Postman](src/test/postman/README-POSTMAN.md).
Also added some features to explore _AWS_ resources like _Lambda Function, S3, and Secrets Manager_.
To run _AWS_ check the optional section.

Also, it's possible generate the [Allure report](https://docs.qameta.io/allure/#_java) first run `mvn allure:install` next
just run: `mvn allure:report` to generate and finally `mvn allure:serve` to open the report.

All tests were built to run against a simple web application in the [teste-software-api](https://github.com/leonidesfernando/teste-software-api) and [teste-software-ui](https://github.com/leonidesfernando/teste-software-ui) repositories.

## Requirements

- Maven
- JDK 17+
## Optional 

This section is designed for some extra validations with _AWS_ and _Apache Kafka_.
To run them, you will need:

- Docker

In the first time, the first run, you must execute this command: `docker-compose up`, or the script: `config/dev-local/container-start.bat` or just use the Docker plugin into Intellij or your desired IDE. 
After that you can enable the testing classes
```java 
AwsLocalTests
KafkaProducerTest
```
since they are optional, they are disabled by default into the TestNG suite.

## Structure
This project uses some frameworks such as Spring Boot and Lombok, in case you come to use an IDE that has plugins to them, it's recommended that you install it.

For IntelijIDEA:
1. Enable annotation processing:
    1. Settings->Compiler->Annotation Processors: "Enable annotation processing"
2. Install plugins to Spring Boot and Lombok(via Marketplace)
3. Restart IDEA and enjoy it.

---
[Português](README.pt_br.md)