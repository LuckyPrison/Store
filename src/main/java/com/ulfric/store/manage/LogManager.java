package com.ulfric.store.manage;

import com.ulfric.store.Store;
import com.ulfric.store.config.LogFile;

public class LogManager extends Manager {

    private LogFile cartsLog;
    private LogFile transactionsLog;
    private LogFile commandsLog;

    public LogManager(Store store)
    {
        super(store);
    }

    @Override
    public void onEnable()
    {
        cartsLog = new LogFile(store, "carts");
        transactionsLog = new LogFile(store, "transactions");
        commandsLog = new LogFile(store, "commands");
    }

    @Override
    public void onDisable()
    {
        getCartLog().save(false);
        getTransactionLog().save(false);
        getCommandLog().save(false);
    }

    public LogFile getCartLog()
    {
        return cartsLog;
    }

    public LogFile getTransactionLog()
    {
        return transactionsLog;
    }

    public LogFile getCommandLog()
    {
        return commandsLog;
    }

}
