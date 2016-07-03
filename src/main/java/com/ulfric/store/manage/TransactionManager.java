package com.ulfric.store.manage;

import com.ulfric.store.Store;
import com.ulfric.store.shop.Transaction;

public class TransactionManager extends ListManager<Transaction> {

    public TransactionManager(Store store)
    {
        super(store);
    }

    public void newTransaction(Transaction transaction, boolean newTransaction)
    {
        this.add(transaction);
        if (newTransaction)
        {
            store.getManager(ConfigManager.class).saveTransaction(transaction);
        }
    }

    public void executeTransaction(Transaction transaction)
    {
        transaction.getPackages().forEach(item ->
                store.getManager(PackageManager.class).execute(item, transaction.getPlayerFor()));
        store.getManager(ConfigManager.class).saveTransaction(transaction);
    }

}
