package me.shukawam.app.kafka;

import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;
import me.shukawam.app.kafka.data.CreateTopicRequest;

@Path("kafka")
public class KafkaResource {

    private final KafkaService kafkaService;

    @Inject
    public KafkaResource(KafkaService kafkaService) {
        this.kafkaService = kafkaService;
    }

    @POST
    @Path("topic")
    @Consumes(MediaType.APPLICATION_JSON)
    public boolean createTopic(@RequestBody CreateTopicRequest createTopicRequest) {
        return kafkaService.createTopic(createTopicRequest.getTopicName(), createTopicRequest.getPartitions(),
                createTopicRequest.getReplicationFactor());
    }

}
