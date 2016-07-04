package com.ulfric.store.pages.user;

import com.ulfric.store.Store;
import com.ulfric.store.gui.GUIPage;
import com.ulfric.store.manage.player.StorePlayer;

public class AddPlayerCartPage extends GUIPage {

    public AddPlayerCartPage(Store store, StorePlayer player)
    {
        super(store, player, player.getLocaleMessage("gui.add-player-to-cart.title"));
    }

    @Override
    protected void loadInventory()
    {

    }

}
