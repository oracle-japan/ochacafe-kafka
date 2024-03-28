# 01_getting_started_kafka

## local machine

### Install

最新の Kafka をダウンロードし、展開します。

```sh
wget wget https://dlcdn.apache.org/kafka/3.7.0/kafka_2.12-3.7.0.tgz
tar -xzf kafka_2.13-3.7.0.tgz
cd kafka_2.13-3.7.0
```

ZooKeeper を起動します。

```sh
bin/zookeeper-server-start.sh config/zookeeper.properties
```

Kafka broker を起動します。

```sh
bin/kafka-server-start.sh config/server.properties
```

### Getting started w/ local machine

トピックを作成します。

```sh
bin/kafka-topics.sh --create --topic quickstart-events --bootstrap-server localhost:9092
```

作成したトピックを参照します。

```sh
bin/kafka-topics.sh --describe --topic quickstart-events --bootstrap-server localhost:9092
```

実行結果

```sh
Topic: quickstart-event TopicId: 3Ulbae19Q_a5YGccG1kTpQ PartitionCount: 1       ReplicationFactor: 1    Configs: segment.bytes=1073741824
        Topic: quickstart-event Partition: 0    Leader: 1       Replicas: 1     Isr: 1
```

作成したトピックにレコードを書き込みます。

```sh
bin/kafka-console-producer.sh \
    --topic quickstart-events \
    --bootstrap-server localhost:9092
###
> This is my first event
> This is my second event
> This is my third event
```

作成したトピックからレコードを読み込みます。

```sh
bin/kafka-console-consumer.sh \
    --topic quickstart-events \
    --from-beginning \
    --bootstrap-server localhost:9092
```

実行結果

```sh
This is my first event
This is my second event
This is my third event
```

```sh
bin/kafka-console-consumer.sh --topic quickstart-event \
    --partition 0 --offset 2 --bootstrap-server localhost:9092
```

実行結果

```sh
This is my second event
This is my third event
```

## Docker (Compose)

### Install

```sh
docker compose up -d
```

### Getting Started w/ Docker (Compose)

トピックを作成する。

```sh
docker compose exec kafka \
    /opt/kafka/bin/kafka-topics.sh \
    --create \
    --topic quickstart-event \
    --bootstrap-server localhost:9092
```

作成したトピックの詳細を確認する。

```sh
docker compose exec kafka \
    /opt/kafka/bin/kafka-topics.sh \
    --describe \
    --topic quickstart-event \
    --bootstrap-server localhost:9092
```

実行結果

```sh
Topic: quickstart-event TopicId: 3Ulbae19Q_a5YGccG1kTpQ PartitionCount: 1       ReplicationFactor: 1    Configs: segment.bytes=1073741824
        Topic: quickstart-event Partition: 0    Leader: 1       Replicas: 1     Isr: 1
```

レコードを Kafka に書き込みます。

```sh
docker compose exec kafka \
    /opt/kafka/bin/kafka-console-producer.sh \
    --topic quickstart-event \
    --bootstrap-server localhost:9092
###
> This is my first event
> This is my second event
> This is my third event
```

書き込んだレコードを読み込みます。

```sh
docker compose exec kafka \
    /opt/kafka/bin/kafka-console-consumer.sh \
    --topic quickstart-event \
    --from-beginning \
    --bootstrap-server localhost:9092
```

パーティション、オフセットを指定した読み込み。

```sh
docker compose exec kafka \
    /opt/kafka/bin/kafka-console-consumer.sh \
    --topic quickstart-event \
    --partition 0 \
    --offset 1 \
    --bootstrap-server localhost:9092
```
