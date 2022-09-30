package dev.nullwzo.examples.mockery.internals;

public interface State {
    State PENDING_SHUTDOWN = null;

    boolean isRunningOrRebalancing();
}
