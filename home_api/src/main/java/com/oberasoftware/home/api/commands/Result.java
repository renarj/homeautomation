package com.oberasoftware.home.api.commands;

import java.util.Optional;

/**
 * @author renarj
 */
public interface Result<T> {
    Optional<T> get();

    boolean isDone();

    boolean isCancelled();
}
