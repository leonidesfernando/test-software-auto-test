package br.com.home.lab.softwaretesting.automation.kafka;

import br.com.home.lab.softwaretesting.automation.aws.config.KafkaConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.CreateTopicsResult;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.errors.TopicExistsException;

import java.util.Collections;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

import static br.com.home.lab.softwaretesting.automation.aws.config.KafkaConfig.BOOTSTRATP_SERVER;


@Slf4j
public class KafkaMessageProducer implements AutoCloseable {

    private final Producer<String,String> producer;

    public KafkaMessageProducer(){
        try{
            createTopicIfMissing();
            producer = new KafkaProducer<>(KafkaConfig.producerProperties);
        }catch (Exception e){
            throw new IllegalStateException("Error during topic creation: ", e);
        }
    }

    public void sendMessage(String message){
        ProducerRecord<String, String> producerRecord = new ProducerRecord<>(KafkaConfig.INCOMING_MESSAGES_TOPIC_NAME, message);
        try{
            producer.send(producerRecord, (RecordMetadata metadata, Exception exception) -> {
                if(exception != null){
                    log.error("Fail to send Kafka message: {} ", exception.getMessage());
                }else{
                    log.info("Message sent");
                }
            });
        }catch (Exception e){
            throw new IllegalStateException("Error during producing Kafka message: ", e);
        }
    }

    public void close(){
        producer.close();
    }

    private static void createTopicIfMissing() throws Exception {
        Properties adminProps = new Properties();
        adminProps.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRATP_SERVER);

        try (AdminClient admin = AdminClient.create(adminProps)) {
            NewTopic newTopic = new NewTopic(KafkaConfig.INCOMING_MESSAGES_TOPIC_NAME, 1, (short) 1);
            CreateTopicsResult result = admin.createTopics(Collections.singleton(newTopic));
            try {
                result.all().get();
               log.info("Topic '{}' created.", KafkaConfig.INCOMING_MESSAGES_TOPIC_NAME);
            } catch (ExecutionException e) {
                if (!(e.getCause() instanceof TopicExistsException)) {
                    throw e;
                }
                log.info("Topic '{}' already exists.",KafkaConfig.INCOMING_MESSAGES_TOPIC_NAME);
            }
        }
    }
}
