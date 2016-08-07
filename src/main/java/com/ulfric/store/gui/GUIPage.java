package com.ulfric.store.gui;

import com.ulfric.store.Store;
import com.ulfric.store.manage.player.StorePlayer;
import org.bukkit.Bukkit;
import org.bukkit.event.inventory.InventoryClickEvent;

public abstract class GUIPage {

    protected final Store store;
    protected final StorePlayer player;

    protected StoreInventory inventory;
    protected final String title;

    protected final Object[] params;

    public GUIPage(Store store, StorePlayer player, String title, Object... params)
    {
        this.store = store;
        this.player = player;
        this.title = title;
        this.params = params;
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
        onInventoryClose();
        player.onClose();
        if (player.inGUI())
        {
            Bukkit.getScheduler().runTask(store, () -> player.openPage(player.currentPage(), false));
        }
    }

    public final void onClick(InventoryClickEvent event)
    {
        System.out.println("GUIPageClick [1]");
        event.setCancelled(true);
        if (inventory.hasAction(event.getSlot()))
        {
            System.out.println("GUIPageClick [2]");
            inventory.executeAction(event.getSlot(), event);
            System.out.println("GUIPageClick [3]");
        }
        else
        {
            System.out.println("GUIPageClick [4]");
            onInventoryClick(event);
            System.out.println("GUIPageClick [5]");
        }
    }

    protected void onInventoryClick(InventoryClickEvent event) {}

    protected void onInventoryClose() {}

    protected void set(StoreInventory inventory)
    {
        this.inventory = inventory;
    }

    public final String getTitle()
    {
        return title;
    }

}
