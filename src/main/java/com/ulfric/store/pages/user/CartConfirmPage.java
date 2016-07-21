package com.ulfric.store.pages.user;

import com.ulfric.store.Store;
import com.ulfric.store.gui.GUIPage;
import com.ulfric.store.gui.Ignore;
import com.ulfric.store.gui.StoreInventory;
import com.ulfric.store.manage.player.StorePlayer;

public class CartConfirmPage extends GUIPage {

    public CartConfirmPage(Store store, StorePlayer player)
    {
        super(store, player, player.getLocaleMessage("gui.cart-confirm.title"));
    }

    @Override
    protected StoreInventory loadInventory()
    {
        return null;
    }

    @Override
    protected void onOpen()
    {
        player.cancel(Ignore.PAGE_CART_CONFIRM);
    }

}
