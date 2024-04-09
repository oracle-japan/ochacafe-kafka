#!/bin/bash

KAFKA_HOME=$HOME/bin/kafka_2.12-3.7.0

$KAFKA_HOME/bin/kafka-server-start.sh $KAFKA_HOME/config/server.properties

