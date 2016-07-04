package com.ulfric.store.gui;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public interface StoreInventory {

    void open();

    boolean hasAction(int slot);

    void executeAction(int slot, InventoryClickEvent event);

    void setItem(int slot, ItemStack item);

    void setItem(int slot, ItemStack item, InventoryAction action);

}
