package com.ulfric.store.gui.anvil;

import com.ulfric.store.Store;
import com.ulfric.store.gui.InventoryAction;
import com.ulfric.store.gui.StoreInventory;
import com.ulfric.store.manage.player.StorePlayer;
import com.ulfric.store.reflect.Reflection;
import com.ulfric.store.reflect.v1_8_R3.Anvil_v1_8_R3;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;

public abstract class AnvilGUI implements StoreInventory {

    public static AnvilGUI getAnvilGUI(Store store, StorePlayer player)
    {
        switch (Reflection.getInstance().getVersion())
        {
            case "v1_8_R3":
                return new Anvil_v1_8_R3(store, player);
        }
        return new Anvil_v1_8_R3(store, player);
    }

    public abstract AnvilGUI setItem(AnvilSlot slot, ItemStack item);

    public abstract AnvilGUI setItem(AnvilSlot slot, ItemStack item, InventoryAction action);

    public abstract AnvilGUI withClickConsumer(Consumer<AnvilClickEvent> consumer);

    public abstract AnvilGUI withCloseConsumer(Consumer<AnvilCloseEvent> consumer);

}
