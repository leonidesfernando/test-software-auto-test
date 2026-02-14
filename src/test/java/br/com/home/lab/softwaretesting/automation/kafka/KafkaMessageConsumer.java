package br.com.home.lab.softwaretesting.automation.kafka;

import br.com.home.lab.softwaretesting.automation.aws.config.KafkaConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;


@Slf4j
public class KafkaMessageConsumer implements AutoCloseable {

    private final KafkaConsumer<String, String> kafkaConsumer;

    private final TopicPartition partition;

    public KafkaMessageConsumer() {
        kafkaConsumer = new KafkaConsumer<>(KafkaConfig.consumerProperties);
        partition = new TopicPartition(KafkaConfig.INCOMING_MESSAGES_TOPIC_NAME, 0);
        kafkaConsumer.assign(List.of(partition));
        kafkaConsumer.seekToBeginning(List.of(partition));
    }


    public List<ConsumerRecord<String, String>> readMessages() {
        log.info("Consuming all messages from beginning on partition {}", partition);
        List<ConsumerRecord<String, String>> allRecords = new ArrayList<>();
        int emptyPolls = 0;
        final int maxEmptyPolls = 5; // stop after 5 empty polls (~4 seconds)
        while(emptyPolls++ < maxEmptyPolls){
            ConsumerRecords<String, String> records = kafkaConsumer.poll(Duration.ofMillis(800));
            if(!records.isEmpty()){
                emptyPolls--;
                records.iterator().forEachRemaining(allRecords::add);
            }
        }
        log.info("Finished consuming. Total messages: {}", allRecords.size());
        return List.copyOf(allRecords);
    }

    public String readOneMessage(){
        log.info("Waiting for the message on partition {} ...", partition);
        ConsumerRecords<String, String> records = kafkaConsumer.poll(Duration.ofMillis(500));
        return records.isEmpty() ? null : records.iterator().next().value();
    }

    /**
     * Closes the consumer. This should be called from your test's @After method.
     */
    @Override
    public void close() {
        if (kafkaConsumer != null) {
            kafkaConsumer.close();
            log.info("KafkaConsumer closed.");
        }
    }
}
