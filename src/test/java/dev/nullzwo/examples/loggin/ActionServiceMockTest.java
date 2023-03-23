package dev.nullzwo.examples.loggin;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.junit.jupiter.api.parallel.ResourceLock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.parallel.ExecutionMode.SAME_THREAD;
import static org.mockito.Mockito.*;

// can not run in parallel with other tests
@ResourceLock("Logger")
@Execution(SAME_THREAD)
class ActionServiceMockTest {

    static MockedStatic<LoggerFactory> mockStatic;
    ActionService service;
    static Logger logger;

    @BeforeEach
    void mockLogger() {
        mockStatic = mockStatic(LoggerFactory.class);
        logger = mock(Logger.class);
        mockStatic.when(() -> LoggerFactory.getLogger(ActionService.class)).thenReturn(logger);

        service = new ActionService();
        Mockito.clearInvocations(logger);
    }

    @AfterEach
    void cleanUpMock() {
        mockStatic.close();
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