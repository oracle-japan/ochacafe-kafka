package me.shukawam.producer;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;

@Path("kafka")
public class ProducerResource {
    private final ProducerService producerService;

    @Inject
    public ProducerResource(ProducerService producerService) {
        this.producerService = producerService;
    }

    @POST
    @Path("produce")
    @Consumes(MediaType.APPLICATION_JSON)
    public Message produce(Message message) {
        producerService.process(message);
        return new Message("ack");
    }
}
