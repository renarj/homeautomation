package com.oberasoftware.home.api.commands.handlers;

import com.oberasoftware.home.api.commands.Command;
import com.oberasoftware.home.api.model.storage.Item;

/**
 * @author renarj
 */
public interface CommandHandler<T extends Item> {
    void receive(T item, Command command);
}
