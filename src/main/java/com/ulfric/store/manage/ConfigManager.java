package com.ulfric.store.manage;

import com.ulfric.store.Store;
import com.ulfric.store.config.ConfigFile;
import com.ulfric.store.execute.CommandType;
import com.ulfric.store.execute.StoreCommand;
import com.ulfric.store.shop.Transaction;

import java.util.UUID;

public class ConfigManager extends Manager {

    private ConfigFile transactionConfig;
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
        loadTransactions();
        loadCommands();
        loadCategories();
        loadPackages();
        loadCoupons();
        loadSales();
    }

    private void loadConfigs()
    {
        transactionConfig = new ConfigFile(store, "transactions");
        commandConfig = new ConfigFile(store, "commandqueue");
        storeConfig = new ConfigFile(store, "store");
    }

    private void loadTransactions()
    {
        TransactionManager transactionManager = store.getManager(TransactionManager.class);
        transactionConfig.getParts("transactions").forEach(part ->
        {
            Transaction transaction = Transaction.deserialize(
                    store,
                    transactionConfig.getConfig(),
                    part.getSection(),
                    UUID.fromString(part.getKey())
            );
            transactionManager.newTransaction(transaction);
        });
    }

    private void loadCommands()
    {
        CommandManager commandManager = store.getManager(CommandManager.class);
        commandConfig.getParts("queue").forEach(part ->
        {
            commandManager.addToQueue(
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

    public void newCommand(UUID uuid, StoreCommand storeCommand)
    {
        UUID key = storeCommand.getCommandUUID();
        commandConfig.getConfig().set("queue." + key + ".uuid", uuid);
        commandConfig.getConfig().set("queue." + key + ".command", storeCommand.getCommand());
        commandConfig.getConfig().set("queue." + key + ".type", storeCommand.getType());
        saveCommands(true);
    }

    public void finishedCommand(StoreCommand storeCommand)
    {
        commandConfig.getConfig().set("queue." + storeCommand.getCommandUUID(), null);
        saveCommands(true);
    }

    public void saveTransaction(Transaction transaction)
    {
        Transaction.serialize(transaction, transactionConfig.getConfig(), "transactions.");
        saveTransactions(true);
    }

    public void saveCommands(boolean async)
    {
        commandConfig.save(async);
    }

    public void saveStore(boolean async)
    {
        storeConfig.save(async);
    }

    public void saveTransactions(boolean async)
    {
        transactionConfig.save(async);
    }

}
