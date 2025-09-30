package br.com.home.lab.softwaretesting.automation.kafka.test;

import br.com.home.lab.softwaretesting.automation.kafka.KafkaMessageConsumer;
import br.com.home.lab.softwaretesting.automation.kafka.KafkaMessageProducer;
import br.com.home.lab.softwaretesting.automation.model.Entry;
import br.com.home.lab.softwaretesting.automation.util.EntryDataUtil;
import br.com.home.lab.softwaretesting.automation.util.JsonUtils;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.testng.annotations.AfterTest;
import org.testng.annotations.Test;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.notNullValue;
import static org.assertj.core.api.Assertions.assertThat;

public class KafkaProducerTest {

    private final  KafkaMessageConsumer consumer = new KafkaMessageConsumer();
    private final KafkaMessageProducer messageProducer = new KafkaMessageProducer();


    @AfterTest
    public void tearDown(){
        messageProducer.close();
    }

    @Test(priority = 1)
    public void kafkaProducerOneMessageTest(){
        Entry entry = EntryDataUtil.newValidEntry();
        messageProducer.sendMessage(JsonUtils.convertToString(entry));
    }

    @Test(dependsOnMethods = {"kafkaProducerOneMessageTest"}, priority = 2)
    public void kafkaConsumerOneMessageTest() {
        String receivedMessage = await().atMost(20, TimeUnit.SECONDS)
                .until(consumer::consumeMessage, notNullValue());
        assertThat(receivedMessage)
                .as("Received message should not be empty")
                .isNotEmpty()
                .isNotNull();
    }

    @Test
    public void kafkaProducerMultipleMessagesTest() {
        for(int i = 0; i < 10; i++) {
            kafkaProducerOneMessageTest();
        }

        List<ConsumerRecord<String, String>> receivedMessages = await().atMost(20, TimeUnit.SECONDS)
                .until(() -> {
                    var messages = consumer.consumeMessages();
                    return messages.size() >= 10 ? messages : null;
                }, notNullValue());
        assertThat(receivedMessages)
                .as("Received messages should be at least 10")
                .isNotEmpty()
                .hasSizeGreaterThanOrEqualTo(10);
    }
}
