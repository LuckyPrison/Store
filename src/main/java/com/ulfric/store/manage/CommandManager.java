package com.ulfric.store.manage;

import com.google.common.collect.Maps;
import com.ulfric.store.Store;
import com.ulfric.store.execute.StoreCommand;

import java.util.Map;
import java.util.UUID;

public class CommandManager extends Manager {

    private Map<StoreCommand, UUID> commandQueue = Maps.newHashMap();

    public CommandManager(Store store)
    {
        super(store);
    }

    public void addToQueue(UUID uuid, StoreCommand command)
    {
        this.commandQueue.put(command, uuid);
    }

    private class QueueListener implements Runnable {

        @Override
        public void run()
        {
            for (StoreCommand command : commandQueue.keySet())
            {
                UUID uuid = commandQueue.get(command);
                if (command.canExecute(uuid))
                {
                    if (command.execute(uuid))
                    {
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
