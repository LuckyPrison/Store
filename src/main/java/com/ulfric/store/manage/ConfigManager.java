package com.ulfric.store.manage;

import com.ulfric.store.Store;
import com.ulfric.store.config.ConfigFile;
import com.ulfric.store.execute.CommandType;
import com.ulfric.store.execute.StoreCommand;

import java.util.UUID;

public class ConfigManager extends Manager {

    private ConfigFile commandConfig;
    private ConfigFile storeConfig;

    public ConfigManager(Store store)
    {
        super(store);
        load();
    }

    private void load()
    {
        loadConfigs();
        loadCommands();
        loadCategories();
        loadPackages();
        loadCoupons();
        loadSales();
    }

    private void loadConfigs()
    {
        commandConfig = new ConfigFile(store, "commandqueue");
        storeConfig = new ConfigFile(store, "store");
    }

    private void loadCommands()
    {
        commandConfig.getParts("queue").forEach(part ->
        {
            store.getManager(CommandManager.class).addToQueue(
                    UUID.fromString(part.getString("uuid")),
                    new StoreCommand(
                            store,
                            part.getString("command"),
                            CommandType.valueOf(part.getString("type"))
                    )
            );
        });
    }

    private void loadCategories()
    {

    }

    private void loadPackages()
    {

    }

    private void loadCoupons()
    {

    }

    private void loadSales()
    {

    }

}
