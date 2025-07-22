# test-software-auto-test

Projeto para explorar diversos tipos de testes como API, e2e, integração, carga e estresse com: TestNG, Mockito, REST Assured,
Cucumber, Selenium Webdriver
, [JMeter](src/test/jmeter/README-JMETER.md) e [Postman](src/test/postman/README-POSTMAN.md).
Também foram adicionadas algumas funcionalidades para explorar recursos do _Apache Kafka_ para produzir e consumir mensagens, e da _AWS_ como _Lambda Function, S3 e Secrets Manager_.
Para rodar recursos da _AWS_, verifique a seção opcional.

Também é possível gerar o [relatório Allure](https://docs.qameta.io/allure/#_java). Primeiro execute `mvn allure:install`, depois
basta rodar: `mvn allure:report` para gerar e, por fim, `mvn allure:serve` para abrir o relatório.

Todos os testes foram construídos para rodar contra uma aplicação web simples disponível nos repositórios [teste-software-api](https://github.com/leonidesfernando/teste-software-api) e [teste-software-ui](https://github.com/leonidesfernando/teste-software-ui).

## Requisitos

- Maven
- JDK 17+
## Opcional

Esta seção é destinada a algumas validações extras com _AWS_ e _Apache Kafka_.
Para executá-las, você vai precisar de:

- Docker

Na primeira vez, execute o comando: `docker-compose up`, ou o script: `config/dev-local/container-start.bat` ou apenas utilize o plugin Docker no Intellij na visualização _**Services**_, ou na IDE de sua preferência.
Após isso, você pode habilitar as classes de teste
```java 
AwsLocalTests
KafkaProducerTest
```
como são opcionais, estão desabilitadas por padrão na suíte do TestNG.

## Estrutura
Este projeto utiliza alguns frameworks como Spring Boot e Lombok. Caso utilize uma IDE que possua plugins para eles, recomenda-se a instalação.

Para o IntelliJ IDEA:
1. Habilite o processamento de anotações:
    1. Configurações->Compilador->Processadores de Anotação: "Habilitar processamento de anotações"
2. Instale os plugins para Spring Boot e Lombok (via Marketplace)
3. Reinicie o IDEA e aproveite.

---
[English](README.md)
```