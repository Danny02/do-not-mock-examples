package dev.nullzwo.examples.mockery.internals;

public interface EventLogger {
    void warn(EventType kafkaStreamError, String format, RuntimeException e);

    void error(EventType kafkaStreamError, String can_not_initialize_restartableStreamBuilder, RuntimeException e);
}
