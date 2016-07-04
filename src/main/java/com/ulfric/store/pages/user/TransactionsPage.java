package com.ulfric.store.pages.user;

import com.ulfric.store.Store;
import com.ulfric.store.gui.GUIPage;
import com.ulfric.store.manage.player.StorePlayer;

public class TransactionsPage extends GUIPage {

    public TransactionsPage(Store store, StorePlayer player)
    {
        super(store, player, player.getLocaleMessage("gui.transactions.title"));
    }

    @Override
    protected void loadInventory()
    {

    }
}
