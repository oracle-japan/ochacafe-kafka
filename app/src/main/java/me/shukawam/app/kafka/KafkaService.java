package me.shukawam.app.kafka;

import java.time.Duration;
import java.util.Arrays;
import java.util.Collections;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;
import java.util.stream.IntStream;

import org.apache.kafka.clients.admin.Admin;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.KafkaFuture;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

/**
 * Kafka Java Clientを用いたデモ実装
 *
 * @author shukawam
 */
@ApplicationScoped
public class KafkaService {
    private static final Logger LOGGER = Logger.getLogger(KafkaService.class.getName());
    private final String bootstrapServer;
    private final String groupId;

    @Inject
    public KafkaService(@ConfigProperty(name = "kafka.bootstrap.servers") String bootstrapServer,
            @ConfigProperty(name = "kafka.group.id") String groupId) {
        this.bootstrapServer = bootstrapServer;
        this.groupId = groupId;
    }

    /**
     * Admin APIを用いたトピックの作成例
     *
     * @param topicName
     *            トピック名
     * @param partitions
     *            パーティション数
     * @param replicationFactor
     *            レプリケーション・ファクター
     *
     * @return true: 成功
     */
    public boolean createTopic(String topicName, int partitions, short replicationFactor) {
        var props = new Properties();
        props.setProperty(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
        try (Admin admin = Admin.create(props)) {
            var result = admin
                    .createTopics(Collections.singleton(new NewTopic(topicName, partitions, replicationFactor)));
            KafkaFuture<Void> future = result.values().get(topicName);
            try {
                future.get();
            } catch (InterruptedException | ExecutionException e) {
                LOGGER.info(e.getMessage());
                throw new RuntimeException(e.getMessage());
            }
            return true;
        }
    }

    /**
     * Producer APIを用いたダミーレコードの書き込み例
     *
     * @param topicName
     *            トピック名
     */
    public void publishRecord(String topicName) {
        var props = new Properties();
        props.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
        props.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
        props.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
                "org.apache.kafka.common.serialization.StringSerializer");
        props.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                "org.apache.kafka.common.serialization.StringSerializer");
        try (KafkaProducer<String, String> producer = new KafkaProducer<>(props)) {
            IntStream.range(1, 100).forEach(i -> {
                try {
                    producer.send(new ProducerRecord<>(topicName, "key" + i, "value" + i)).get();
                } catch (InterruptedException | ExecutionException e) {
                    LOGGER.info(e.getMessage());
                    throw new RuntimeException(e.getMessage());
                }
            });
        }
    }

    /**
     * Consumer APIを用いたダミーデータの読み込み例
     *
     * @param topicName
     *            トピック名
     */
    public void consumeRecord(String topicName) {
        var props = new Properties();
        props.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
        props.setProperty(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        props.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
                "org.apache.kafka.common.serialization.StringDeserializer");
        props.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
                "org.apache.kafka.common.serialization.StringDeserializer");
        try (KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props)) {
            consumer.subscribe(Arrays.asList(topicName));
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(10));
            records.forEach(record -> {
                LOGGER.info(
                        String.format("offset: %d, key=%s, value=%s", record.offset(), record.key(), record.value()));
            });
        }
    }

}
