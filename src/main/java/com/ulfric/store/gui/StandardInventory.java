package com.ulfric.store.gui;

import com.google.common.collect.Maps;
import com.ulfric.store.Store;
import com.ulfric.store.manage.player.StorePlayer;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Map;
import java.util.stream.IntStream;

public class StandardInventory implements StoreInventory {

    private final Store store;
    private final StorePlayer player;
    private final Map<Integer, InventoryAction> actions = Maps.newHashMap();

    private final Inventory inventory;

    public StandardInventory(Store store, StorePlayer player, int size, String title)
    {
        this.store = store;
        this.player = player;
        this.inventory = store.getServer().createInventory(null, size, title);
    }

    public void color(ItemStack item)
    {
        IntStream.range(0, inventory.getSize()).forEach(i -> inventory.setItem(i, item.clone()));
    }

    @Override
    public void open()
    {
        player.player().openInventory(inventory);
    }

    @Override
    public boolean hasAction(int slot)
    {
        return actions.containsKey(slot);
    }

    @Override
    public void executeAction(int slot, InventoryClickEvent event)
    {
        if (!hasAction(slot))
        {
            return;
        }
        actions.get(slot).execute(event);
    }

    @Override
    public void setItem(int slot, ItemStack item)
    {
        inventory.setItem(slot, item);
    }

    @Override
    public void setItem(int slot, ItemStack item, InventoryAction action)
    {
        inventory.setItem(slot, item);
        actions.put(slot, action);
    }
}
