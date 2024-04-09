#!/bin/bash

KAFKA_HOME=$HOME/bin/kafka_2.12-3.7.0

$KAFKA_HOME/bin/zookeeper-server-start.sh $KAFKA_HOME/config/zookeeper.properties
