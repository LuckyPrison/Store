package com.ulfric.store.gui.anvil;

import org.bukkit.event.inventory.InventoryClickEvent;

public class AnvilClickEvent {

    private AnvilSlot slot;
    private String name;
    private InventoryClickEvent event;

    private boolean close = true;
    private boolean destroy = true;

    public AnvilClickEvent(AnvilSlot slot, String name, InventoryClickEvent event)
    {
        this.slot = slot;
        this.name = name;
        this.event = event;
    }

    public AnvilSlot getSlot()
    {
        return slot;
    }

    public String getName()
    {
        return name;
    }

    public InventoryClickEvent getEvent()
    {
        return event;
    }

    public boolean isWillClose()
    {
        return close;
    }

    public void setWillClose(boolean close)
    {
        this.close = close;
    }

    public boolean isWillDestroy()
    {
        return destroy;
    }

    public void setWillDestroy(boolean destroy)
    {
        this.destroy = destroy;
    }
}
