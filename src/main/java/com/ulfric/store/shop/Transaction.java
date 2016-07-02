package com.ulfric.store.shop;

import com.google.common.collect.Lists;
import com.ulfric.store.Store;
import com.ulfric.store.config.ConfigSerializable;
import com.ulfric.store.manage.PackageManager;
import com.ulfric.store.manage.TransactionManager;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class Transaction implements ConfigSerializable {

    public static void serialize(Transaction transaction, YamlConfiguration config, String key)
    {
        config.set(key + transaction.transactionId + ".executed", transaction.executed);
        config.set(key + transaction.transactionId + ".for", transaction.playerFor);
        config.set(
                key + transaction.transactionId + ".packages",
                transaction.items.stream().map(Package::getId).collect(Collectors.toList())
        );
    }

    public static Transaction deserialize(Store store, YamlConfiguration config, String section, UUID transactionId)
    {
        Transaction transaction = new Transaction(
                store,
                UUID.fromString(config.getString(section + "." + transactionId + ".for")),
                config.getIntegerList(section + "." + transactionId + ".packages")
                        .stream()
                        .map(id -> store.getManager(PackageManager.class).getPackage(id))
                        .collect(Collectors.toList())
        );
        transaction.executed = config.getBoolean(section + "." + transactionId + ".executed");
        transaction.transactionId = transactionId;
        return transaction;
    }

    private final Store store;

    private UUID transactionId;
    private boolean executed = false;

    private UUID playerFor;
    private List<Package> items;

    public Transaction(Store store, UUID playerFor, List<Package> items)
    {
        this.store = store;
        this.transactionId = UUID.randomUUID();
        this.playerFor = playerFor;
        this.items = items;
    }

    public void load()
    {
        store.getManager(TransactionManager.class).newTransaction(this);
    }

    public void execute()
    {
        if (executed)
        {
            return;
        }
        store.getManager(TransactionManager.class).executeTransaction(this);
        this.executed = true;
    }

    public UUID getTransactionId()
    {
        return transactionId;
    }

    public UUID getPlayerFor()
    {
        return playerFor;
    }

    public List<Package> getPackages()
    {
        return Lists.newArrayList(items);
    }

    public boolean isExecuted()
    {
        return executed;
    }

}
