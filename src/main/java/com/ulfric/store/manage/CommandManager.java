package com.ulfric.store.manage;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.ulfric.store.Store;
import com.ulfric.store.execute.StoreCommand;
import com.ulfric.store.util.StoreUtils;
import org.bukkit.configuration.file.YamlConfiguration;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class CommandManager extends Manager {

    private Map<StoreCommand, UUID> commandQueue = Maps.newHashMap();

    private QueueListener queueListener;

    public CommandManager(Store store)
    {
        super(store);
    }

    @Override
    public void onEnable()
    {
        store.getServer().getScheduler().scheduleSyncRepeatingTask(store, () ->
        {
            store.getManager(ConfigManager.class).saveCommands(true);
        }, 20 * 60, 20 * 60);

        queueListener = new QueueListener();
        queueListener.id = store.getServer().getScheduler().scheduleSyncRepeatingTask(store, queueListener, 20 * 60, 20 * 30);

    }

    public void addToQueue(UUID uuid, StoreCommand command, boolean newCommand)
    {
        this.commandQueue.put(command, uuid);
        if (newCommand)
        {
            store.getManager(ConfigManager.class).newCommand(uuid, command);

            LogManager log = store.getManager(LogManager.class);
            YamlConfiguration config = log.getCommandLog().getConfig();
            config.set(command.getCommandUUID() + ".command", command.getCommand());
            config.set(command.getCommandUUID() + ".instant", StoreUtils.readableTimestamp(Instant.now()));
            config.set(command.getCommandUUID() + ".for", uuid.toString());
            config.set(command.getCommandUUID() + ".type", command.getType().toString());
            config.set(command.getCommandUUID() + ".delay", command.getDelay());
            config.set(command.getCommandUUID() + ".require-online", command.isRequireOnline());
            config.set(command.getCommandUUID() + ".executed", command.hasExecuted());
            log.getCommandLog().save(true);

        }
    }

    private class QueueListener implements Runnable {

        private int id;

        public void cancel()
        {
            store.getServer().getScheduler().cancelTask(id);
        }

        @Override
        public void run()
        {
            ConfigManager configManager = store.getManager(ConfigManager.class);
            List<StoreCommand> finished = Lists.newArrayList();
            for (StoreCommand command : commandQueue.keySet())
            {
                UUID uuid = commandQueue.get(command);
                if (command.canExecute(uuid))
                {
                    if (command.execute(uuid))
                    {
                        System.out.println("Finished executing command " + command.getCommand() + ":" + command.getCommandUUID());
                        configManager.finishedCommand(command);
                        finished.add(command);
                    }
                    else
                    {
                        store.getLogger().severe("CommandStore was executed but failed!");
                        store.getLogger().severe("Command -> " + command.toString());
                    }
                }
            }
            finished.forEach(commandQueue::remove);
        }

    }

}
