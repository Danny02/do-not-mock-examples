package dev.nullwzo.examples.mockery;

import dev.nullwzo.examples.mockery.internals.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class RestartableStreamBuilderTest {

    private RestartableStreamBuilder restartableStreamBuilder;
    private KafkaStreamsHealthChecker kafkaStreamsHealthChecker;
    private EventLogger eventLogger;
    private StreamsBuilderFactoryBean streamsBuilderFactoryBean;

    @BeforeEach
    public void globalSetUp() {
        kafkaStreamsHealthChecker = mock(KafkaStreamsHealthChecker.class);
        eventLogger = mock(EventLogger.class);
        mockStreamsBuilderFactoryBean();
        restartableStreamBuilder = createRestartableStreamBuilder();
    }

    @Test
    public void constructor_shouldSetUncaughtExceptionHandler() {
        verify(streamsBuilderFactoryBean).setUncaughtExceptionHandler(any());
    }

    @Test
    public void constructor_shouldSetStateListener() {
        verify(streamsBuilderFactoryBean).setStateListener(any());
    }

    @Test
    public void streamHealthCheck_shouldForwardTheCallToInternalHealthChecker() {
        restartableStreamBuilder.streamHealthCheck();
        verify(kafkaStreamsHealthChecker).isHealthy(any());
    }

    @Test
    public void getStreamBuilder_shouldAlwaysReturnTheSameInstance() throws Exception {
        doReturn(new StreamsBuilder()).when(streamsBuilderFactoryBean).getObject();

        StreamsBuilder expectedStreamsBuilder = restartableStreamBuilder.getStreamsBuilder();
        StreamsBuilder actualStreamsBuilder = restartableStreamBuilder.getStreamsBuilder();

        assertThat(actualStreamsBuilder).isEqualTo(expectedStreamsBuilder);
    }

    private void mockStreamsBuilderFactoryBean() {
        streamsBuilderFactoryBean = mock(StreamsBuilderFactoryBean.class);
        KafkaStreams kafkaStreams = mock(KafkaStreams.class);
        doReturn(kafkaStreams).when(streamsBuilderFactoryBean).getKafkaStreams();
    }

    private RestartableStreamBuilder createRestartableStreamBuilder() {
        return new RestartableStreamBuilder(
                streamsBuilderFactoryBean,
                "test",
                kafkaStreamsHealthChecker,
                eventLogger
        );
    }
}