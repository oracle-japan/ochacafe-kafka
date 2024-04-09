#!/bin/bash

KAFKA_HOME=$HOME/bin/kafka_2.12-3.7.0

$KAFKA_HOME/bin/kafka-console-consumer.sh \
    --topic quickstart-event \
    --bootstrap-server localhost:9092 \
    --from-beginning

