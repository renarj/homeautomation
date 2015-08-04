package com.oberasoftware.home.core.commands;

import com.oberasoftware.home.api.commands.ItemCommand;
import com.oberasoftware.home.api.commands.GroupCommand;
import com.oberasoftware.home.api.model.storage.GroupItem;

/**
 * @author Renze de Vries
 */
public class GroupCommandImpl implements GroupCommand {

    private final ItemCommand command;
    private final GroupItem groupItem;

    public GroupCommandImpl(ItemCommand command, GroupItem groupItem) {
        this.command = command;
        this.groupItem = groupItem;
    }

    @Override
    public ItemCommand getCommand() {
        return command;
    }

    @Override
    public GroupItem getGroup() {
        return groupItem;
    }
}
