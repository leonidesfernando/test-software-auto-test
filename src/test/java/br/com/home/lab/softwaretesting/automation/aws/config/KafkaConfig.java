package br.com.home.lab.softwaretesting.automation.aws.config;

import br.com.home.lab.softwaretesting.automation.config.Configurations;
import org.aeonbits.owner.ConfigFactory;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;

public final class KafkaConfig {

    private KafkaConfig(){}
    private static final Configurations config = ConfigFactory.create(Configurations.class);
    public static final String INCOMING_MESSAGES_TOPIC_NAME = config.kafkaIncomingMessagesTopicName();
    public static final String BOOTSTRATP_SERVER = config.kafkaBootstrapServers();
    public static final String GROUP_ID = config.kafkaGroupId();
    public static final Properties producerProperties = getProducerProperties();
    public static final Properties consumerProperties = getConsumerProperties();

    private static final int ONE_SEC = 1_000;

    private static Properties getProducerProperties() {
        Properties properties = new Properties();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRATP_SERVER);
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.put(ProducerConfig.RETRIES_CONFIG, Integer.MAX_VALUE);  // FIXED: Must be >0 for idempotence
        properties.put(ProducerConfig.MAX_BLOCK_MS_CONFIG, ONE_SEC);
        properties.put(ProducerConfig.SOCKET_CONNECTION_SETUP_TIMEOUT_MAX_MS_CONFIG, ONE_SEC);
        properties.put(ProducerConfig.SOCKET_CONNECTION_SETUP_TIMEOUT_MS_CONFIG, ONE_SEC);

        return properties;
    }

    private static Properties getConsumerProperties(){
        Properties properties = new Properties();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRATP_SERVER);
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, GROUP_ID);
        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        properties.put(ConsumerConfig.HEARTBEAT_INTERVAL_MS_CONFIG, 1000);  // Faster heartbeats
        properties.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, 30000);   // Default, but ensure
        properties.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, 1);

        return properties;
    }
}
