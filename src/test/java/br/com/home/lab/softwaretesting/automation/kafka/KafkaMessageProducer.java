package br.com.home.lab.softwaretesting.automation.kafka;

import br.com.home.lab.softwaretesting.automation.aws.config.KafkaConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;


@Slf4j
public class KafkaMessageProducer {

    private final Producer<String,String> producer = new KafkaProducer<>(KafkaConfig.producerProperties);

    public void sendMessage(String message){
        ProducerRecord<String, String> producerRecord = new ProducerRecord<>(KafkaConfig.INCOMING_MESSAGES_TOPIC_NAME, message);
        try{
            producer.send(producerRecord, (RecordMetadata metadata, Exception exception) -> {
                if(exception != null){
                    log.error("Fail to send Kafka message: {}", exception.getMessage());
                    System.out.println("message: "+exception.getMessage());
                    System.out.println("**********************************************************");
                    System.out.println("cause: "+exception.getCause());
                }else{
                    System.out.println("message sent");
                }
            });
        }catch (Exception e){
            throw new IllegalStateException("Error duing poducing Kafka message: ", e);
        }
    }

    public void close(){
        producer.close();
    }
}
