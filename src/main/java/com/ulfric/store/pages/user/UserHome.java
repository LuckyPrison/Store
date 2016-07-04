package com.ulfric.store.pages.user;

import com.ulfric.store.Store;
import com.ulfric.store.gui.GUIPage;
import com.ulfric.store.gui.StandardInventory;
import com.ulfric.store.manage.player.StorePlayer;
import com.ulfric.store.util.Chat;
import com.ulfric.store.util.Items;

public class UserHome extends GUIPage {

    public UserHome(Store store, StorePlayer player)
    {
        super(store, player, Chat.color(player.getLocaleMessage("gui.home.title")));
    }

    @Override
    protected void loadInventory()
    {
        StandardInventory inventory = new StandardInventory(store, player, 54, title);

        inventory.color(Items.stainedPane(Items.PaneColor.LIGHT_BLUE));



        set(inventory);
    }

}
