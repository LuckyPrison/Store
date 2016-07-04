package com.ulfric.store.gui;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class AnvilInventory implements StoreInventory {

    @Override
    public void open()
    {

    }

    @Override
    public boolean hasAction(int slot)
    {
        return false;
    }

    @Override
    public void executeAction(int slot, InventoryClickEvent event)
    {

    }

    @Override
    public void setItem(int slot, ItemStack item)
    {

    }

    @Override
    public void setItem(int slot, ItemStack item, InventoryAction action)
    {

    }

}
