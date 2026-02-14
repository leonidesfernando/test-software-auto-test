REM # Wait for Kafka to start
sleep 10

REM # Create Kafka topics
REM kafka-topics.sh --create --topic test_software_producer_topic --bootstrap-server localhost:9092 --partitions 3 --replication-factor 1
REM kafka-topics.sh --create --topic test_software_consumer_topic --bootstrap-server localhost:9092 --partitions 2 --replication-factor 1
kafka-topics.sh --create --topic test_software_incoming_topic --bootstrap-server localhost:9092 --partitions 2 --replication-factor 1

echo "Kafka topics created successfully!"
