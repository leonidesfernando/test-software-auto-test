package br.com.home.lab.softwaretesting.automation.kafka;

import br.com.home.lab.softwaretesting.automation.aws.config.KafkaConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;


@Slf4j
public class KafkaMessageConsumer {

    public List<ConsumerRecord<String,String>> consumeMessages(){
        try (KafkaConsumer<String, String> consumer = getConsumerInstance()) {

            int i = 0;
            List<ConsumerRecord<String, String>> recordsList = new ArrayList<>();
            while (i < 10) {
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
                for (ConsumerRecord<String, String> consumerRecord : records) {
                    logMessaageReceived(consumerRecord);
                    recordsList.add(consumerRecord);
                    i = 10;
                }
                consumer.commitSync(); // Commit offset after successful consumption
                i++;
            }
            log.info("Consumed messages: " + recordsList.size());

            consumer.close();
            return recordsList;
        }catch (Exception e){
            throw new IllegalStateException("Fail during consuming message: ", e);
        }
    }

    public String consumeMessage(){
        int attempts = 10;
        try (KafkaConsumer<String, String> consumer = getConsumerInstance()) {
            for (int i = 0; i < attempts; i++) {
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(1000));
                if (records.isEmpty()) {
                    log.info("No messages received in this poll. Attempt {}/{}", i + 1, attempts);
                } else {
                    var consumerRecord = records.iterator().next();
                    logMessaageReceived(consumerRecord);
                    consumer.commitSync();
                    return consumerRecord.value();
                }
            }
        } catch (Exception e) {
            throw new IllegalStateException("Fail during consuming message: ", e);
        }
        log.info("No messages consumed after `{}` attempts.", attempts);
        return null;
    }

    private static void logMessaageReceived(ConsumerRecord<String, String> consumerRecord) {
        log.info("Received message: key = {}, value = {}, partition = {}, offset = {}",
                consumerRecord.key(), consumerRecord.value(), consumerRecord.partition(), consumerRecord.offset());
    }

    private KafkaConsumer<String, String> getConsumerInstance(){
        final KafkaConsumer<String,String> consumer = new KafkaConsumer<>(KafkaConfig.consumerProperties);
        consumer.subscribe(Collections.singletonList(KafkaConfig.INCOMING_MESSAGES_TOPIC_NAME));
        // Assign partitions and seek to beginning to ensure all messages are read from the start
        // This is crucial when using a shared group ID in tests
        consumer.poll(Duration.ofMillis(100)); // Poll once to get partition assignments
        Set<TopicPartition> partitions = consumer.assignment();
        consumer.seekToBeginning(partitions);
        log.info("KafkaMessageConsumer initialized and seeking to beginning of partitions: {}", partitions);
        return consumer;
    }
}
