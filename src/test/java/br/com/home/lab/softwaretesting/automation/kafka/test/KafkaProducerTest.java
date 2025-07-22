package br.com.home.lab.softwaretesting.automation.kafka.test;

import br.com.home.lab.softwaretesting.automation.kafka.KafkaMessageConsumer;
import br.com.home.lab.softwaretesting.automation.kafka.KafkaMessageProducer;
import br.com.home.lab.softwaretesting.automation.model.Entry;
import br.com.home.lab.softwaretesting.automation.util.EntryDataUtil;
import br.com.home.lab.softwaretesting.automation.util.JsonUtils;
import org.testng.annotations.AfterTest;
import org.testng.annotations.Test;

import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;

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
        await().atMost(20, TimeUnit.SECONDS)
                .until(() -> consumer.consumeMessage() != null);
    }

    @Test
    public void kafkaProducerMultipleMessagesTest() {
        for(int i = 0; i < 10; i++) {
            kafkaProducerOneMessageTest();
        }
        await().atMost(20, TimeUnit.SECONDS)
                .until(() -> consumer.consumeMessages().size() >= 10);
    }
}
