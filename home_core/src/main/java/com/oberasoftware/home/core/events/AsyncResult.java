package com.oberasoftware.home.core.events;

import com.oberasoftware.home.api.commands.Result;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * @author renarj
 */
public class AsyncResult implements Result<List<?>> {

    private Future<List<?>> future;

    public AsyncResult(Future<List<?>> future) {
        this.future = future;
    }

    @Override
    public Optional<List<?>> get() {
        try {
            return Optional.ofNullable(future.get());
        } catch (InterruptedException | ExecutionException e) {
            return Optional.empty();
        }
    }

    @Override
    public boolean isDone() {
        return future.isDone();
    }

    @Override
    public boolean isCancelled() {
        return future.isCancelled();
    }
}
