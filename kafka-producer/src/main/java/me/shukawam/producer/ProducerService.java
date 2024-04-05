package me.shukawam.producer;

import java.util.concurrent.SubmissionPublisher;

import org.eclipse.microprofile.reactive.messaging.Outgoing;
import org.eclipse.microprofile.reactive.streams.operators.ReactiveStreams;
import org.reactivestreams.FlowAdapters;
import org.reactivestreams.Publisher;

import io.helidon.common.reactive.Multi;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ProducerService {

    private final SubmissionPublisher<String> publisher = new SubmissionPublisher<>();

    @Outgoing("to-kafka")
    public Publisher<String> produce() {
        return ReactiveStreams
                .fromPublisher(FlowAdapters.toPublisher(Multi.create(publisher)))
                .buildRs();
    }

    public void process(Event event) {
        publisher.submit(event.toString());
    }
}
