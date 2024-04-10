# 04_kafka_distributed_tracing

## Add OpenTelemetry Operator

[https://github.com/open-telemetry/opentelemetry-operator](https://github.com/open-telemetry/opentelemetry-operator) を参考に、OpenTelemetry Operator をクラスタ上に展開します。

自動計装を行うための設定を追加します。`OTEL_EXPORTER_OTLP_TRACES_ENDPOINT`, `OTEL_EXPORTER_OTLP_HEADERS`, `OTEL_EXPORTER_OTLP_TRACES_HEADERS` はご自身が使用している OTLP 対応のバックエンドに応じて修正してください。（当日のデモでは、OpenObserve を使用しています。）

```yaml
apiVersion: opentelemetry.io/v1alpha1
kind: Instrumentation
metadata:
  name: instrumentation
  namespace: ochacafe
spec:
  exporter:
    endpoint: http://otel-collector.monitoring.svc.cluster.local:4317
  propagators:
    - tracecontext
    - baggage
    - b3
  sampler:
    type: parentbased_traceidratio
    argument: "0.25"
  java:
    env:
      - name: OTEL_TRACES_EXPORTER
        value: otlp
      - name: OTEL_METRICS_EXPORTER
        value: none
      - name: OTEL_LOGS_EXPORTER
        value: none
      - name: OTEL_EXPORTER_OTLP_TRACES_PROTOCOL
        value: http/protobuf
      - name: OTEL_EXPORTER_OTLP_METRICS_PROTOCOL
        value: http/protobuf
      - name: OTEL_EXPORTER_OTLP_LOGS_PROTOCOL
        value: http/protobuf
      - name: OTEL_EXPORTER_OTLP_TRACES_ENDPOINT
        valueFrom:
          secretKeyRef:
            key: otlp-traces-endpoint
            name: otel-ochacafe-secret
      - name: OTEL_EXPORTER_OTLP_HEADERS
        valueFrom:
          secretKeyRef:
            key: otlp-headers
            name: otel-ochacafe-secret
      - name: OTEL_EXPORTER_OTLP_TRACES_HEADERS
        valueFrom:
          secretKeyRef:
            key: otlp-headers
            name: otel-ochacafe-secret
```

これをクラスタ上に展開します。

```sh
kubectl apply -f kubernetes/kafka-client/instrumenration.yaml
```

## Build the producer and consumer application

### Producer

アプリケーションルートに移動します。

```sh
cd kafka-producer
```

アプリケーションをコンテナイメージにビルドします。

```sh
docker build -t producer:1.0.0 .
```

ご自身が利用しているコンテナレジストリにプッシュしておきます。

```sh
docker push producer:1.0.0
```

### Consumer

アプリケーションルートに移動します。

```sh
cd kafka-consumer
```

アプリケーションをコンテナイメージにビルドします。

```sh
docker build -t consumer:1.0.0 .
```

ご自身が利用しているコンテナレジストリにプッシュしておきます。

```sh
docker push consumer:1.0.0
```

### Deploy producer and consumer application

`kubernetes/kafka-client/kafka-producer.yaml`, `kubernetes/kafka-client/kafka-consumer.yaml` をご自身の環境に合わせて修正します。

修正例

```yaml
kind: Service
apiVersion: v1
metadata:
  name: kafka-producer
  labels:
    app: kafka-producer
spec:
  type: ClusterIP
  selector:
    app: kafka-producer
  ports:
    - port: 8080
      targetPort: 8080
      name: http
---
kind: Deployment
apiVersion: apps/v1
metadata:
  name: kafka-producer
spec:
  replicas: 1
  selector:
    matchLabels:
      app: kafka-producer
  template:
    metadata:
      labels:
        app: kafka-producer
        version: v1
      annotations:
        instrumentation.opentelemetry.io/inject-java: "true"
    spec:
      containers:
        - name: kafka-producer
          image: nrt.ocir.io/orasejapan/shukawam/kafka-producer:1.0.0 # ご自身のコンテナイメージ名に修正する
          imagePullPolicy: IfNotPresent
          ports:
            - containerPort: 8080
          env:
            - name: OTEL_SERVICE_NAME
              value: kafka-producer
            - name: mp.messaging.connector.helidon-kafka.bootstrap.servers
              value: ochacafe-kafka-bootstrap.kafka.svc.cluster.local:9092
      imagePullSecrets:
        - name: ocir-secret
---
##### 必要であれば、Ingress等を追加
apiVersion: networking.k8s.io/v1
kind: Ingress
metadata:
  name: kafka-producer
  annotations:
    kubernetes.io/ingress.class: nginx
    cert-manager.io/cluster-issuer: letsencrypt-prod
spec:
  tls:
    - hosts:
        - producer.shukawam.me
      secretName: shukawam-tls-secret-producer
  rules:
    - host: producer.shukawam.me
      http:
        paths:
          - path: /
            pathType: Prefix
            backend:
              service:
                name: kafka-producer
                port:
                  number: 8080
```
