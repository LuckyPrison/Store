package com.ulfric.store.pages.user;

import com.ulfric.store.Store;
import com.ulfric.store.gui.GUIPage;
import com.ulfric.store.gui.StandardInventory;
import com.ulfric.store.gui.StoreInventory;
import com.ulfric.store.manage.player.StorePlayer;
import com.ulfric.store.util.ItemBuilder;
import com.ulfric.store.util.Items;
import org.bukkit.Material;

import java.util.stream.IntStream;

public class AddPlayerCartErrorPage extends GUIPage {

    private final String error;

    public AddPlayerCartErrorPage(Store store, StorePlayer player, String error)
    {
        super(store, player, player.getLocaleMessage("&c", "generic.error", ""));
        this.error = error;
    }

    @Override
    protected StoreInventory loadInventory()
    {
        StandardInventory inventory = new StandardInventory(store, player, 54, title);
        IntStream.range(0, 54).forEach(i ->
        {
            inventory.setItem(
                    i,
                    ItemBuilder.of(Material.WOOL)
                            .withDurability(Items.ItemColor.RED)
                            .withName(error)
                            .withLore(player.getLocaleMessage("&c", "gui.generic.clicktoclose", ""))
                            .build(),
                    event ->
                    {
                        player.player().closeInventory();
                    }
            );
        });
        return null;
    }

}
