package dev.nullwzo.examples.mockery.internals;

import java.util.function.BiConsumer;

public interface StreamsBuilderFactoryBean {
    KafkaStreams getKafkaStreams();

    StreamsBuilder getObject();

    void setUncaughtExceptionHandler(BiConsumer<Thread, Throwable> o);

    void setStateListener(BiConsumer<State, State> o);

    void afterPropertiesSet();

    void start();

    void stop();
}
