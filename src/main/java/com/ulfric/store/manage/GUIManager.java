package com.ulfric.store.manage;

import com.ulfric.store.Store;
import com.ulfric.store.manage.player.PlayerManager;
import com.ulfric.store.manage.player.StorePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;

public class GUIManager extends Manager {

    public GUIManager(Store store)
    {
        super(store);
        store.getServer().getPluginManager().registerEvents(new GUIListener(), store);
    }

    public class GUIListener implements Listener {

        @EventHandler
        public void on(InventoryOpenEvent event)
        {
            if (!(event.getPlayer() instanceof Player))
            {
                return;
            }
            StorePlayer player = store.getManager(PlayerManager.class).getPlayer((Player) event.getPlayer());

            if (player.isWatchingPackage())
            {
                player.player().closeInventory();
            }
        }

        @EventHandler
        public void on(InventoryClickEvent event)
        {
            if (!(event.getWhoClicked() instanceof Player))
            {
                return;
            }

            Player player = (Player) event.getWhoClicked();
            StorePlayer storePlayer = store.getManager(PlayerManager.class).getPlayer(player);

            if (storePlayer.isWatchingPackage())
            {
                return;
            }

            if (storePlayer.inGUI())
            {
                storePlayer.onInventoryClick(event);
            }
        }

        @EventHandler
        public void on(InventoryCloseEvent event)
        {
            if (!(event.getPlayer() instanceof Player))
            {
                return;
            }

            Player player = (Player) event.getPlayer();
            StorePlayer storePlayer = store.getManager(PlayerManager.class).getPlayer(player);

            if (storePlayer.isWatchingPackage())
            {
                return;
            }

            if (storePlayer.inGUI())
            {
                storePlayer.onInventoryClose(event);
            }
        }

    }

}
