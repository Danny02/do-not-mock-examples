package dev.nullzwo.examples.mockery.internals;

public interface KafkaStreamsHealthChecker {
    boolean isHealthy(KafkaStreams kafkaStreams);

    void reset();
}
