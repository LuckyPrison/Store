package com.ulfric.store.pages.generic;

import com.ulfric.store.Store;
import com.ulfric.store.gui.GUIPage;
import com.ulfric.store.gui.StandardInventory;
import com.ulfric.store.gui.StoreInventory;
import com.ulfric.store.manage.player.StorePlayer;
import com.ulfric.store.util.ItemBuilder;
import com.ulfric.store.util.Items;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.stream.IntStream;

public class LoadingPage extends GUIPage {

    public LoadingPage(Store store, StorePlayer player)
    {
        super(store, player, player.getLocaleMessage("gui.loading.title"));
    }

    @Override
    protected StoreInventory loadInventory()
    {
        StandardInventory inventory = new StandardInventory(store, player, 54, getTitle());
        ItemStack item = ItemBuilder.of(Material.WOOL)
                .withDurability(Items.ItemColor.YELLOW)
                .withName(player.getLocaleMessage("gui.loading.item.name"))
                .build();
        IntStream.range(0, 54).forEach(i -> inventory.setItem(i, item.clone()));
        return inventory;
    }

}
