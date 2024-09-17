FROM maven:3.9.7-amazoncorretto-17-al2023

WORKDIR /app

COPY . /app
COPY opentelemetry /usr/local/lib/opentelemetry

RUN mvn clean package

EXPOSE 8080

ENTRYPOINT ["java", "-javaagent:/usr/local/lib/opentelemetry/opentelemetry-javaagent.jar", "-Dotel.service.name=grupo2", "-Dotel.traces.exporter=otlp", "-Dotel.metrics.exporter=none", "-Dotel.exporter.otlp.endpoint=http://collector-api:4318", "-Dotel.exporter.otlp.protocol=http/protobuf", "-jar", "target/queijos_finos-0.0.1-SNAPSHOT.jar"]