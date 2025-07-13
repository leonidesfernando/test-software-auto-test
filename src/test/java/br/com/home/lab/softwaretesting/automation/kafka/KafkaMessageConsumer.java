package br.com.home.lab.softwaretesting.automation.kafka;

import br.com.home.lab.softwaretesting.automation.aws.config.KafkaConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.time.Duration;
import java.util.Collections;

public class KafkaMessageConsumer {

    private final KafkaConsumer<String, String> consumer;

    public KafkaMessageConsumer() {
        this.consumer = new KafkaConsumer<>(KafkaConfig.consumerProperties);
        this.consumer.subscribe(Collections.singletonList(KafkaConfig.INCOMING_MESSAGES_TOPIC_NAME));
    }

    public void close(){
        consumer.close();
    }

    public void consumeMessage(){
        try {
            consumer.subscribe(Collections.singletonList(KafkaConfig.INCOMING_MESSAGES_TOPIC_NAME));
            int i = 0;
            while (i < 10) {
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(1000));
                for (ConsumerRecord<String, String> record : records) {
                    System.out.printf("Received message: key = %s, value = %s, partition = %d, offset = %d%n",
                            record.key(), record.value(), record.partition(), record.offset());
                    i = 10;
                }
                i++;
            }
        }catch (Exception e){
            throw new IllegalStateException("Fail during consuming message: ", e);
        }
    }
}
