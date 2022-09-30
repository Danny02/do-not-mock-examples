package dev.nullwzo.examples;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class AbstractLoggingTest {
    private final static Logger root = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
    private CollectingAppender appender;

    @BeforeEach
    void setup() {
        appender = new CollectingAppender();
        root.addAppender(appender);
    }

    @AfterEach
    void cleanup() {
        root.detachAppender(appender);
    }

    public List<ILoggingEvent> loggedEvents() {
        return appender.events;
    }

    private static class CollectingAppender extends AppenderBase<ILoggingEvent> {

        public List<ILoggingEvent> events = new ArrayList<>();

        @Override
        protected void append(ILoggingEvent eventObject) {
            events.add(eventObject);
        }
    }
}
