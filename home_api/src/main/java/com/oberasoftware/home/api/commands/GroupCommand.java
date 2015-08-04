package com.oberasoftware.home.api.commands;

import com.oberasoftware.home.api.model.storage.GroupItem;

/**
 * @author Renze de Vries
 */
public interface GroupCommand extends Command {
    ItemCommand getCommand();

    GroupItem getGroup();
}
