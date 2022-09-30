package dev.nullwzo.examples.loggin;

import ch.qos.logback.classic.spi.ILoggingEvent;
import dev.nullwzo.examples.AbstractLoggingTest;
import org.junit.jupiter.api.Test;

import static ch.qos.logback.classic.Level.INFO;
import static org.assertj.core.api.Assertions.assertThat;

class ActionServiceFakeTest extends AbstractLoggingTest {

    ActionService service = new ActionService();

    @Test
    void shouldLogInvocation() {
        service.invokeAction();

        assertLogEvent();
    }

    @Test
    void shouldLogInvocationWithParam() {
        service.invokeAction("foo");

        assertLogEvent();
    }

    void assertLogEvent() {
        assertThat(loggedEvents()).first().extracting(ILoggingEvent::getLevel).isEqualTo(INFO);
    }
}