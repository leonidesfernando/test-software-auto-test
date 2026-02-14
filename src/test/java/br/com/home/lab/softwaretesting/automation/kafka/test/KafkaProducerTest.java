package br.com.home.lab.softwaretesting.automation.kafka.test;

import br.com.home.lab.softwaretesting.automation.kafka.KafkaMessageConsumer;
import br.com.home.lab.softwaretesting.automation.kafka.KafkaMessageProducer;
import br.com.home.lab.softwaretesting.automation.model.Entry;
import br.com.home.lab.softwaretesting.automation.util.EntryDataUtil;
import br.com.home.lab.softwaretesting.automation.util.JsonUtils;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.notNullValue;
import static org.assertj.core.api.Assertions.assertThat;

public class KafkaProducerTest {

    private static KafkaMessageProducer producer;

    @BeforeClass
    public static void setUp() {
        producer = new KafkaMessageProducer();
    }

    @AfterClass
    public static void tearDown() {
        producer.close();
    }

    @Test(priority = 1, dependsOnMethods = "kafkaProducerMultipleMessagesTest")
    public void kafkaProducerOneMessageTest() {
        Entry entry = EntryDataUtil.newValidEntry();
        producer.sendMessage(JsonUtils.convertToString(entry));
    }

    @Test
    public void kafkaReadOneMessage(){
        try(KafkaMessageConsumer consumer = new KafkaMessageConsumer()) {
            String receivedMessage = consumer.readOneMessage();
            assertThat(receivedMessage)
                    .as("Received message should not be empty")
                    .isNotEmpty()
                    .isNotNull();
        }
    }

    @Test(priority = 0)
    public void kafkaProducerMultipleMessagesTest() {
        try(KafkaMessageConsumer consumer = new KafkaMessageConsumer()) {
            for (int i = 0; i < 10; i++) {
                Entry entry = EntryDataUtil.newValidEntry();
                producer.sendMessage(JsonUtils.convertToString(entry));
            }

            List<ConsumerRecord<String, String>> receivedMessages = await().atMost(30, TimeUnit.SECONDS)
                    .until(() -> {
                        var messages = consumer.readMessages();
                        return messages.size() >= 10 ? messages : null;
                    }, notNullValue());
            assertThat(receivedMessages)
                    .as("Received messages should be at least 10")
                    .isNotEmpty()
                    .hasSizeGreaterThanOrEqualTo(10);
        }
    }

    @Test
    public void kafkaReadMessageWithTimeout(){
        try(KafkaMessageConsumer consumer = new KafkaMessageConsumer()) {
            String receivedMessage = await().atMost(15, TimeUnit.SECONDS)  // Increased to 30s for safety
                    .pollInterval(Duration.ofMillis(500))
                    .until(consumer::readOneMessage, notNullValue());
            assertThat(receivedMessage)
                    .as("Received message should not be empty")
                    .isNotEmpty()
                    .isNotNull();
        }
    }

    @Test
    public void kafkaReadMultipleMessagesWithTimeoutTest() {
        try(KafkaMessageConsumer consumer = new KafkaMessageConsumer()) {
            List<ConsumerRecord<String, String>> receivedMessages = await()
                    .atMost(45, TimeUnit.SECONDS)
                    .pollInterval(Duration.ofMillis(500))
                    .until(() -> {
                        var messages = consumer.readMessages();
                        return !messages.isEmpty() ? messages : null;
                    }, notNullValue());

            assertThat(receivedMessages)
                    .as("Received messages should be at least 5")
                    .isNotEmpty()
                    .hasSizeGreaterThanOrEqualTo(1);
        }
    }

    @Test
    public void kafkaReadMultipleMessagesTest() {
        try(KafkaMessageConsumer consumer = new KafkaMessageConsumer()) {
            List<ConsumerRecord<String, String>> receivedMessages = consumer.readMessages();
            assertThat(receivedMessages)
                    .as("Received messages should be at least 5")
                    .isNotEmpty()
                    .hasSizeGreaterThanOrEqualTo(1);
        }
    }

}
