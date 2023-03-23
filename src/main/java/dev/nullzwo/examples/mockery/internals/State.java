package dev.nullzwo.examples.mockery.internals;

public interface State {
    State PENDING_SHUTDOWN = null;

    boolean isRunningOrRebalancing();
}
