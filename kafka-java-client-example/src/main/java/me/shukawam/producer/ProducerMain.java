package me.shukawam.producer;

import java.util.Properties;
import java.util.stream.IntStream;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

public class ProducerMain {
    public static void main(String... args) {
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("linger.ms", 1);
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        Producer<String, String> producer = new KafkaProducer<>(props);
        IntStream.range(0, 100).forEach(i -> {
            producer.send(new ProducerRecord<String, String>("my-topic", String.format("key-%s", Integer.toString(i)),
                    String.format("value-%s", Integer.toString(i))));
        });
        producer.close();
    }
}
