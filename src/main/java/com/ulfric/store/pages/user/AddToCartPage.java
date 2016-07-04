package com.ulfric.store.pages.user;

import com.ulfric.store.Store;
import com.ulfric.store.gui.GUIPage;
import com.ulfric.store.manage.player.StorePlayer;
import com.ulfric.store.shop.Package;

public class AddToCartPage extends GUIPage {

    private final Package pack;

    public AddToCartPage(Store store, StorePlayer player, Package pack)
    {
        super(store, player, player.getLocaleMessage("gui.add-to-cart.title"));
        this.pack = pack;
    }

    @Override
    protected void loadInventory()
    {

    }

}
