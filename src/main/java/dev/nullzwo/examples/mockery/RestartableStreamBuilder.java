package dev.nullzwo.examples.mockery;

import dev.nullzwo.examples.mockery.internals.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;

import java.text.MessageFormat;
import java.util.Optional;

import static org.slf4j.event.Level.*;

public class RestartableStreamBuilder implements ApplicationListener {

    private final StreamsBuilderFactoryBean streamsBuilderFactoryBean;
    private static final Logger logger = LoggerFactory.getLogger(RestartableStreamBuilder.class);
    private final String name;
    private final KafkaStreamsHealthChecker healthChecker;
    private final EventLogger eventLogger;

    public RestartableStreamBuilder(StreamsBuilderFactoryBean streamsBuilderFactoryBean,
                                    String name,
                                    KafkaStreamsHealthChecker healthChecker,
                                    EventLogger eventLogger) {
        this.streamsBuilderFactoryBean = streamsBuilderFactoryBean;
        this.name = name;
        this.healthChecker = healthChecker;
        this.eventLogger = eventLogger;

        init();
    }

    public void streamHealthCheck() {
        Optional.ofNullable(streamsBuilderFactoryBean.getKafkaStreams()).ifPresent(kafkaStreams -> {
            if (!healthChecker.isHealthy(kafkaStreams)) {
                logWarning("KafkaStream is NOT HEALTHY !!");
                restartStreams();
            }
        });
    }

    public StreamsBuilder getStreamsBuilder() throws Exception {
        return streamsBuilderFactoryBean.getObject();
    }

    private void init() {

        streamsBuilderFactoryBean.setUncaughtExceptionHandler((thread, throwable) -> {
            logger.error("An uncaught exception has been occurred: thread id = {}, throwable = {}", thread.getId(), throwable);
            restartStreams();
        });

        streamsBuilderFactoryBean.setStateListener((newState, oldState) -> {

            logInfo(MessageFormat.format("kafka-streams state changed from {0} to {1}", oldState, newState));

            if (oldState.isRunningOrRebalancing() && !newState.isRunningOrRebalancing() && !State.PENDING_SHUTDOWN.equals(newState)) {
                eventLogger.warn(
                        EventType.KAFKA_STREAM_ERROR,
                        MessageFormat.format("kafka-streams state changed from {0}(running) to {1}(not-running!). Restarting kafka-streams", oldState, newState),
                        new RuntimeException()
                );

                restartStreams();
            }
        });

        try {
            streamsBuilderFactoryBean.afterPropertiesSet();
        } catch (Exception e) {
            eventLogger.error(
                    EventType.KAFKA_STREAM_ERROR,
                    "can not initialize RestartableStreamBuilder",
                    new RuntimeException(e.getMessage())
            );
        }
    }

    @Override
    public void onApplicationEvent(ApplicationEvent applicationEvent) {
        if (applicationEvent instanceof ApplicationReadyEvent) {
            logInfo(String.format("kafka streams started"));
            streamsBuilderFactoryBean.start();
        }
    }

    private void restartStreams() {
        logInfo("stopping kafka-streams...");
        streamsBuilderFactoryBean.stop();
        logInfo("{0}: stopping kafka-streams... DONE");

        healthChecker.reset();

        logInfo("starting kafka-streams...");
        streamsBuilderFactoryBean.start();
        logInfo("starting kafka-streams... DONE");
    }

    private void logInfo(String message) {
        logMessageWithLevel(message, INFO);
    }

    private void logWarning(String message) {
        logMessageWithLevel(message, WARN);
    }

    private void logMessageWithLevel(String message, Level level) {
        String messageToLog = MessageFormat.format("{0}: {1}", name, message);
        switch (level) {
            case ERROR:
                logger.error(messageToLog);
                break;
            case INFO:
                logger.info(messageToLog);
                break;
            case WARN:
                logger.warn(messageToLog);
                break;
        }
    }
}
