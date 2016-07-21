package com.ulfric.store.gui;

import com.ulfric.store.Store;
import com.ulfric.store.manage.player.StorePlayer;
import org.bukkit.event.inventory.InventoryClickEvent;

public abstract class GUIPage {

    protected final Store store;
    protected final StorePlayer player;

    protected StoreInventory inventory;
    protected final String title;

    public GUIPage(Store store, StorePlayer player, String title)
    {
        this.store = store;
        this.player = player;
        this.title = title;
        this.inventory = loadInventory();
    }

    protected abstract StoreInventory loadInventory();

    protected void onOpen()
    {}

    public final void open()
    {
        if (inventory == null)
        {
            throw new IllegalStateException("Inventory has not been initialized!");
        }
        inventory.open();
        onOpen();
    }

    public final void onClose()
    {
        player.onClose();
        if (player.inGUI())
        {
            player.openPage(player.currentPage(), false);
        }
    }

    public final void onClick(InventoryClickEvent event)
    {
        event.setCancelled(true);
        if (inventory.hasAction(event.getSlot()))
        {
            inventory.executeAction(event.getSlot(), event);
        }
        else
        {
            onInventoryClick(event);
        }
    }

    protected void onInventoryClick(InventoryClickEvent event) {}

    protected void set(StoreInventory inventory)
    {
        this.inventory = inventory;
    }

    public final String getTitle()
    {
        return title;
    }

}
