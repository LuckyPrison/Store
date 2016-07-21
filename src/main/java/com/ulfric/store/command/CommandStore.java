package com.ulfric.store.command;

import com.ulfric.store.Store;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CommandStore implements CommandExecutor {

    private final Store store;

    public CommandStore(Store store)
    {
        this.store = store;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {

        return false;
    }

}
