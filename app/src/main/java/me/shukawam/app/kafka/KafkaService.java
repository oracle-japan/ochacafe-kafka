package me.shukawam.app.kafka;

import java.util.Collections;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;

import org.apache.kafka.clients.admin.Admin;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
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
    private final Properties props;

    @Inject
    public KafkaService(@ConfigProperty(name = "kafka.bootstrap-server") String bootstrapServer) {
        props = new Properties();
        props.setProperty(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
    }

    /**
     * Admin APIを用いたトピックの作成例
     * 
     * @param topicName         トピック名
     * @param partitions        パーティション数
     * @param replicationFactor レプリケーション・ファクター
     * @return true: 成功
     */
    public boolean createTopic(String topicName, int partitions, short replicationFactor) {
        try (Admin admin = Admin.create(props)) {
            var result = admin.createTopics(Collections.singleton(
                    new NewTopic(topicName, partitions, replicationFactor)));
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

}
