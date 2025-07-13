package br.com.home.lab.softwaretesting.automation.kafka.test;

import br.com.home.lab.softwaretesting.automation.kafka.KafkaMessageConsumer;
import br.com.home.lab.softwaretesting.automation.kafka.KafkaMessageProducer;
import br.com.home.lab.softwaretesting.automation.model.Entry;
import br.com.home.lab.softwaretesting.automation.util.EntryDataUtil;
import br.com.home.lab.softwaretesting.automation.util.JsonUtils;
import org.testng.annotations.AfterTest;
import org.testng.annotations.Test;

public class KafkaProducerTest {

    private final KafkaMessageConsumer consumer = new KafkaMessageConsumer();
    private final KafkaMessageProducer messageProducer = new KafkaMessageProducer();

    @AfterTest
    public void tearDown(){
        consumer.close();
        messageProducer.close();
    }

    @Test
    public void kafkaMessageProducerTest(){
        Entry entry = EntryDataUtil.newValidEntry();
        messageProducer.sendMessage(JsonUtils.convertToString(entry));
        consumer.consumeMessage();
    }
}
