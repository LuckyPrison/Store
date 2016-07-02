package com.ulfric.store.execute;

import com.ulfric.store.Store;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.util.UUID;

public class StoreCommand {

    private final Store store;

    private String command;
    private CommandType type;

    private long delay = 0;
    private boolean requireOnline = true;

    private boolean executed = false;

    public StoreCommand(Store store, String command, CommandType type)
    {
        this.store = store;
        this.command = command;
        this.type = type;
    }

    public StoreCommand withDelay(long delay)
    {
        if (delay >= 0)
        {
            this.delay = delay;
        }
        return this;
    }

    public StoreCommand withRequireOnline(boolean requireOnline)
    {
        this.requireOnline = requireOnline;
        return this;
    }

    public boolean execute(UUID uuid)
    {
        if (!canExecute(uuid) || executed)
        {
            return false;
        }
        OfflinePlayer player = Bukkit.getOfflinePlayer(uuid);
        String repCommand = this.command
                .replace("{name}", player.getName())
                .replace("{uuid}", uuid.toString())
                .replace("{shortuuid}", uuid.toString().replace("-", ""));
        if (type == CommandType.INITIAL)
        {
            Bukkit.getScheduler().runTaskLater(store, () ->
            {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), repCommand);
            }, delay);
            executed = true;

            return true;
        }
        return false;
    }

    public boolean canExecute(UUID uuid)
    {
        return !requireOnline || Bukkit.getPlayer(uuid) != null;
    }

    public String getCommand()
    {
        return command;
    }

    public CommandType getType()
    {
        return type;
    }

    public long getDelay()
    {
        return delay;
    }

    public boolean isRequireOnline()
    {
        return requireOnline;
    }

    public boolean hasExecuted()
    {
        return executed;
    }

    @Override
    public String toString()
    {
        return String.format("StoreCommand[%s:type->%s:delay->%d:reqonline->%b:executed->%b]", command, type, delay, requireOnline, executed);
    }

}
