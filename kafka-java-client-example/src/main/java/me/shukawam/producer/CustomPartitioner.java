package me.shukawam.producer;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.common.Cluster;
import org.apache.kafka.common.InvalidRecordException;
import org.apache.kafka.common.PartitionInfo;

public class CustomPartitioner implements Partitioner {
    private static final Logger logger = Logger.getLogger(CustomPartitioner.class.getName());

    @Override
    public void close() {
        logger.info("close");
    }

    @Override
    public int partition(String topic, Object key, byte[] keyBytes, Object value, byte[] valueBytes, Cluster cluster) {
        List<PartitionInfo> partitionInfos = cluster.partitionsForTopic(topic);
        int numPartitions = partitionInfos.size();
        if (keyBytes == null || !(key instanceof String)) {
            throw new InvalidRecordException("Unexpected key.");
        }
        String strKey = (String) key;
        // put to partition-0 if key starts with LowerCase
        if (strKey.matches("^[a-z].*")) {
            return numPartitions - 2;
        } else if (strKey.matches("^[A-Z].*")) {
            return numPartitions - 1;
        } else {
            return numPartitions - 3;
        }
    }

    @Override
    public void configure(Map<String, ?> configs) {
    }

}
