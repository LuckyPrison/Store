package com.ulfric.store.manage;

import com.google.common.collect.Maps;
import com.ulfric.store.Store;
import com.ulfric.store.execute.StoreCommand;

import java.util.Map;
import java.util.UUID;

public class CommandManager extends Manager {

    private Map<StoreCommand, UUID> commandQueue = Maps.newHashMap();

    private final QueueListener queueListener;

    public CommandManager(Store store)
    {
        super(store);

        store.getServer().getScheduler().scheduleSyncRepeatingTask(store, () ->
        {
            store.getManager(ConfigManager.class).saveCommands(true);
        }, 20 * 60 * 5, 20 * 60 * 5);

        queueListener = new QueueListener();
        queueListener.id = store.getServer().getScheduler().scheduleSyncRepeatingTask(store, queueListener, 20 * 60, 20 * 30);
    }

    public void addToQueue(UUID uuid, StoreCommand command)
    {
        this.commandQueue.put(command, uuid);
        store.getManager(ConfigManager.class).newCommand(uuid, command);
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
            for (StoreCommand command : commandQueue.keySet())
            {
                UUID uuid = commandQueue.get(command);
                if (command.canExecute(uuid))
                {
                    if (command.execute(uuid))
                    {
                        configManager.finishedCommand(command);
                        commandQueue.remove(command);
                    }
                    else
                    {
                        store.getLogger().severe("StoreCommand was executed but failed!");
                        store.getLogger().severe("Command -> " + command.toString());
                    }
                }
            }
        }

    }

}
