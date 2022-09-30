package dev.nullwzo.examples.loggin;

import dev.nullwzo.examples.AbstractLoggingTest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.ResourceLock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.mockito.Mockito.*;

// can not run in parallel with other tests
@ResourceLock("Logger")
class ActionServiceMockTest extends AbstractLoggingTest {

    static MockedStatic<LoggerFactory> mockStatic;
    ActionService service;
    static Logger logger;

    @BeforeAll
    static void mockLogger() {
        mockStatic = mockStatic(LoggerFactory.class);
        logger = mock(Logger.class);
        mockStatic.when(() -> LoggerFactory.getLogger(ActionService.class)).thenReturn(logger);
    }

    @AfterAll
    static void cleanUpMock() {
        mockStatic.close();
    }

    @BeforeEach
    void setup() {
        service = new ActionService();
        Mockito.clearInvocations(logger);
    }

    @Test
    void shouldLogInvocation() {
        service.invokeAction();

        verify(logger).info(any());
    }

    @Test
    void shouldLogInvocationWithParam() {
        service.invokeAction("foo");

        verify(logger).info(any());
    }
}