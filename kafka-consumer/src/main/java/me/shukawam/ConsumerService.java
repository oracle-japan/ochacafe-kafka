package me.shukawam;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.reactive.messaging.Incoming;

import io.opentelemetry.instrumentation.kafkaclients.v2_6.TracingConsumerInterceptor;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class ConsumerService {

    private static final Logger logger = Logger.getLogger(ConsumerService.class.getName());

    @Incoming("from-kafka")
    public void consume(String message) {
        logger.info(String.format("Kafka says: %s", message));
    }

    // private final String bootstrapServer;
    // private final String topicName;
    // private final String groupId;
    // private KafkaConsumer<String, String> consumer;

    // @Inject
    // public ConsumerService(@ConfigProperty(name = "kafka.bootstrap.servers")
    // String bootstrapServer,
    // @ConfigProperty(name = "kafka.topic.name") String topicName,
    // @ConfigProperty(name = "kafka.group.id") String groupId) {
    // this.bootstrapServer = bootstrapServer;
    // this.topicName = topicName;
    // this.groupId = groupId;
    // }

    // @PostConstruct
    // public void consume() {
    // var props = new Properties();
    // props.setProperty("bootstrap.servers", bootstrapServer);
    // props.setProperty("group.id", groupId);
    // props.setProperty("enable.auto.commit", "true");
    // props.setProperty("auto.commit.interval.ms", "1000");
    // props.setProperty("key.deserializer", StringDeserializer.class.getName());
    // props.setProperty("value.deserializer", StringDeserializer.class.getName());
    // props.setProperty("interceptor.classes",
    // TracingConsumerInterceptor.class.getName());
    // consumer = new KafkaConsumer<>(props);
    // consumer.subscribe(Arrays.asList(topicName));
    // startKafkaRecordConsumer();
    // }

    // private void startKafkaRecordConsumer() {
    // ExecutorService executorService = Executors.newFixedThreadPool(2);
    // executorService.execute(() -> {
    // logger.info(String.format("starting expensive task thread %s",
    // Thread.currentThread().getName()));
    // listenTopic();
    // });
    // }

    // public void listenTopic() {
    // while (true) {
    // // Poll for records
    // ConsumerRecords<String, String> records =
    // consumer.poll(Duration.ofMillis(200));
    // // Did we get any?
    // if (records.count() == 0) {
    // // timeout/nothing to read
    // } else {
    // // Yes, loop over records
    // for (ConsumerRecord<String, String> record : records) {
    // // Display record and count
    // logger.info("===========Message Received=============");
    // logger.info(String.format("partition= %s, offset = %s, value = %s",
    // record.partition(),
    // record.offset(), record.value()));
    // }
    // }
    // }
    // }

    // @PreDestroy
    // public void cleanUp() {
    // consumer.close();
    // }
}
