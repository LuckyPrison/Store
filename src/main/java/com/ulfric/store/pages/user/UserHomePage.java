package com.ulfric.store.pages.user;

import com.ulfric.store.Store;
import com.ulfric.store.gui.GUIPage;
import com.ulfric.store.gui.StandardInventory;
import com.ulfric.store.manage.player.StorePlayer;
import com.ulfric.store.util.ItemBuilder;
import com.ulfric.store.util.Items;
import org.bukkit.Material;

public class UserHomePage extends GUIPage {

    public UserHomePage(Store store, StorePlayer player)
    {
        super(store, player, player.getLocaleMessage("gui.home.title"));
    }

    @Override
    protected void loadInventory()
    {
        StandardInventory inventory = new StandardInventory(store, player, 54, title);

        inventory.color(Items.stainedPane(Items.ItemColor.LIGHT_BLUE));

        inventory.setItem(
                11,
                ItemBuilder.of(Material.MINECART)
                        .withName(player.getLocaleMessage("store.cart"))
                        .build(),
                event -> player.openPage(new CartPage(store, player), true)
        );

        inventory.setItem(
                13,
                ItemBuilder.of(Material.CHEST)
                        .withName(player.getLocaleMessage("store.categories"))
                        .build(),
                event -> player.openPage(new CategoriesPage(store, player), true)
        );

        inventory.setItem(
                15,
                ItemBuilder.of(Material.PAPER)
                        .withName(player.getLocaleMessage("store.transactions"))
                        .build(),
                event -> player.openPage(new TransactionsPage(store, player), true)
        );

        inventory.setItem(
                31,
                ItemBuilder.of(Material.ENDER_STONE)
                        .withName(player.getLocaleMessage("store.featured"))
                        .build()
        );

        set(inventory);
    }

}
