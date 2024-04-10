# OCHaCafe Season8 - Kafka

2024/04/10 に行われた OCHaCafe Season 8 - シン・Kafka のデモリソースです。

## Overview

```sh
tree -D -L 1
```

実行結果

```sh
.
├── docs # デモの実施手順の説明
├── kafka-consumer # Kafka の Consumer のサンプル実装（MicroProfile Reactive Messaging）
├── kafka-java-client-example # Kafka Java Client を用いたサンプル実装（Producer/Consumer）
├── kafka-producer # Kafka の Producer のサンプル実装（MicroProfile Reactive Messaging）
├── kubernetes # Kubernetes にデプロイするためのサンプル Manifest ファイル
├── lib # [Optional] OpenTelemetry による分散トレーシングを実施する場合は、OpenTelemetry Java Agent を lib/ に配置しておく
└── scripts # デモ実施用のスクリプトファイル
```

### CLI を用いた Kafka の基本操作

[docs/01_getting_started_kafka](https://github.com/oracle-japan/ochacafe-kafka/blob/main/docs/01_getting_started_kafka.md) をご参照ください。

### Kafka Java Client を用いた 基本操作

TODO: 書きかけです！
[docs/02_getting_started_kafka_with_java_client](https://github.com/oracle-japan/ochacafe-kafka/blob/main/docs/02_getting_started_kafka_with_java_client.md) をご参照ください。

### Strimzi を用いた Kubernetes 上への Kafka クラスタの展開

TODO: 書きかけです！
[docs/03_kafka_on_kubernetes](https://github.com/oracle-japan/ochacafe-kafka/blob/main/docs/03_kafka_on_kubernetes.md) をご参照ください。

### Kafka を介した Producer/Subscriber 間の分散トレーシング

TODO: 書きかけです！
[docs/04_kafka_distributed_tracing](https://github.com/oracle-japan/ochacafe-kafka/blob/main/docs/04_kafka_distributed_tracing.md) をご参照ください。
