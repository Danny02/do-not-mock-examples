package dev.nullzwo.examples.loggin;

import ch.qos.logback.classic.spi.ILoggingEvent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.junit.jupiter.api.parallel.ResourceLock;

import static ch.qos.logback.classic.Level.INFO;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.parallel.ExecutionMode.SAME_THREAD;

@ResourceLock("Logger")
@Execution(SAME_THREAD)
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