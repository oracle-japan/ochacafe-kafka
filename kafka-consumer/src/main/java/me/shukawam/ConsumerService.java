package me.shukawam;

import java.util.logging.Logger;

import org.eclipse.microprofile.reactive.messaging.Incoming;

import jakarta.enterprise.context.ApplicationScoped;

/**
 * Kafka consumer implemented by MicroProfile Reactive Messaging
 *
 * @author shukawam
 */
@ApplicationScoped
public class ConsumerService {

    private static final Logger logger = Logger.getLogger(ConsumerService.class.getName());

    @Incoming("from-kafka")
    public void consume(String message) {
        logger.info(String.format("Kafka says: %s", message));
    }

}
