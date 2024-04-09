package me.shukawam.producer;

import java.util.Properties;
import java.util.concurrent.Future;
import java.util.stream.IntStream;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

public class ProducerCustomPartitionerMain {
    public static void main(String[] args) {
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("linger.ms", 1);
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put(ProducerConfig.PARTITIONER_CLASS_CONFIG, CustomPartitioner.class.getName());
        Producer<String, String> producer = new KafkaProducer<>(props);
        IntStream.range(0, 100).forEach(i -> {
            try {
                Future<RecordMetadata> send = producer.send(
                        new ProducerRecord<String, String>("my-topic", "0",
                                String.format("value-%s", Integer.toString(i))));
                RecordMetadata recordMetadata = send.get();
                System.out.print("partition: " + recordMetadata.partition() + ", ");
                System.out.print("topic: " + recordMetadata.topic() + ", ");
                System.out.println("value: " + Integer.toString(i));
            } catch (Exception e) {
                e.printStackTrace();
            }

        });
        producer.close();
    }
}
