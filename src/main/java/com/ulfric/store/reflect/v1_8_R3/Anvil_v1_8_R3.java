package com.ulfric.store.reflect.v1_8_R3;

import com.google.common.collect.Maps;
import com.ulfric.store.Store;
import com.ulfric.store.gui.InventoryAction;
import com.ulfric.store.gui.anvil.AnvilClickEvent;
import com.ulfric.store.gui.anvil.AnvilCloseEvent;
import com.ulfric.store.gui.anvil.AnvilGUI;
import com.ulfric.store.gui.anvil.AnvilSlot;
import com.ulfric.store.manage.player.StorePlayer;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Map;
import java.util.function.Consumer;

public class Anvil_v1_8_R3 extends AnvilGUI {

    private class Container extends ContainerAnvil {

        protected Container(EntityHuman entity)
        {
            super(entity.inventory, entity.world, new BlockPosition(0, 0, 0), entity);
        }

        @Override
        public boolean a(EntityHuman human)
        {
            return true;
        }
    }

    private final Store store;
    private final StorePlayer player;

    private Map<Integer, InventoryAction> actions = Maps.newHashMap();

    private Consumer<AnvilClickEvent> clickConsumer;
    private Consumer<AnvilCloseEvent> closeConsumer;

    private Map<AnvilSlot, ItemStack> items = Maps.newHashMap();
    private Inventory inventory;

    private Listener listener;
    private boolean closing = false;

    public Anvil_v1_8_R3(Store store, StorePlayer player)
    {
        this.store = store;
        this.player = player;
        this.listener = new Listener() {

            @EventHandler
            public void onInventoryClick(final InventoryClickEvent event) {

                if (event.getWhoClicked() instanceof Player) {

                    Player clicker = (Player) event.getWhoClicked();

                    if (event.getInventory().equals(inventory)) {
                        event.setCancelled(true);

                        ItemStack item = event.getCurrentItem();
                        int slot = event.getRawSlot();
                        String name = "";

                        if (item != null) {
                            if (item.hasItemMeta()) {
                                ItemMeta meta = item.getItemMeta();

                                if (meta.hasDisplayName()) {
                                    name = meta.getDisplayName();
                                }
                            }
                        }

                        AnvilClickEvent clickEvent = new AnvilClickEvent(AnvilSlot.bySlot(slot), name, event);

                        if (clickConsumer != null)
                        {
                            clickConsumer.accept(clickEvent);
                        }

                        if (clickEvent.isWillClose()) {
                            closing = true;
                            Bukkit.getScheduler().runTask(store, () -> event.getWhoClicked().closeInventory());
                        }

                        if (clickEvent.isWillClose()) {
                            destroy();
                        }
                    }
                }
            }

            @EventHandler(priority = EventPriority.HIGHEST)
            public void onInventoryClose(InventoryCloseEvent event) {
                if (event.getPlayer() instanceof Player) {
                    Player player = (Player) event.getPlayer();
                    Inventory inventory = event.getInventory();

                    if (inventory.equals(Anvil_v1_8_R3.this.inventory)) {
                        AnvilCloseEvent closeEvent = new AnvilCloseEvent(event, closing);
                        if (closeConsumer != null)
                        {
                            closeConsumer.accept(closeEvent);
                        }
                    }
                }
            }

            @EventHandler
            public void onPlayerQuit(PlayerQuitEvent event) {
                if (event.getPlayer().equals(player.player())) {
                    destroy();
                }
            }

        };
    }

    private void destroy()
    {
        HandlerList.unregisterAll(listener);
    }

    @Override
    public AnvilGUI setItem(AnvilSlot slot, ItemStack item)
    {
        setItem(slot.getSlot(), item);
        return this;
    }

    @Override
    public AnvilGUI setItem(AnvilSlot slot, ItemStack item, InventoryAction action)
    {
        setItem(slot.getSlot(), item, action);
        return this;
    }

    @Override
    public AnvilGUI withClickConsumer(Consumer<AnvilClickEvent> consumer)
    {
        this.clickConsumer = consumer;
        return this;
    }

    @Override
    public AnvilGUI withCloseConsumer(Consumer<AnvilCloseEvent> consumer)
    {
        this.closeConsumer = consumer;
        return this;
    }

    @Override
    public void open()
    {
        EntityPlayer p = ((CraftPlayer) player.player()).getHandle();

        Container container = new Container(p);

        //Set the items to the items from the inventory given
        inventory = container.getBukkitView().getTopInventory();

        for (AnvilSlot slot : items.keySet()) {
            inventory.setItem(slot.getSlot(), items.get(slot));
        }

        //Counter stuff that the game uses to keep track of inventories
        int c = p.nextContainerCounter();

        //Send the packet
        p.playerConnection.sendPacket(new PacketPlayOutOpenWindow(c, "minecraft:anvil", new ChatMessage("Repairing"), 0));

        //Set their active container to the container
        p.activeContainer = container;

        //Set their active container window id to that counter stuff
        p.activeContainer.windowId = c;

        //Add the slot listener
        p.activeContainer.addSlotListener(p);
    }

    @Override
    public boolean hasAction(int slot)
    {
        return actions.containsKey(slot);
    }

    @Override
    public void executeAction(int slot, InventoryClickEvent event)
    {
        if (hasAction(slot))
        {
            actions.get(slot).execute(event);
        }
    }

    @Override
    public void setItem(int slot, ItemStack item)
    {
        items.put(AnvilSlot.bySlot(slot), item);
    }

    @Override
    public void setItem(int slot, ItemStack item, InventoryAction action)
    {
        items.put(AnvilSlot.bySlot(slot), item);
        actions.put(slot, action);
    }
}
