package com.ulfric.store.pages.user;

import com.ulfric.store.Store;
import com.ulfric.store.gui.GUIPage;
import com.ulfric.store.gui.Ignore;
import com.ulfric.store.gui.StoreInventory;
import com.ulfric.store.gui.anvil.AnvilGUI;
import com.ulfric.store.gui.anvil.AnvilSlot;
import com.ulfric.store.manage.player.StorePlayer;
import com.ulfric.store.pages.generic.LoadingPage;
import com.ulfric.store.util.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.SkullType;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;

import java.util.UUID;

public class AddPlayerCartPage extends GUIPage {

    public AddPlayerCartPage(Store store, StorePlayer player)
    {
        super(store, player, player.getLocaleMessage("gui.add-player-to-cart.title"));
    }

    @Override
    protected StoreInventory loadInventory()
    {
        AnvilGUI inventory = AnvilGUI.getAnvilGUI(store, player);

        inventory.setItem(
                AnvilSlot.INPUT_LEFT,
                ItemBuilder.of(Material.SKULL_ITEM)
                        .withDurability(SkullType.PLAYER.ordinal())
                        .withName(player.getLocaleMessage("gui.add-player-to-cart.name"))
                        .withLore(player.getLocaleMessage("gui.add-player-to-cart.lore"))
                        .build()
        );

        inventory.withClickConsumer(event ->
        {
            event.setWillClose(false);
            System.out.println("AddPlayerClick [1]");
            player.openPage(new LoadingPage(store, player), true);
            player.cancel(Ignore.ADD_PLAYER);System.out.println("close Called 1!");

            Bukkit.getScheduler().runTask(store, () -> {
                System.out.println("close Called 2!");
                player.player().getNearbyEntities(5, 5, 5).stream()
                        .filter(entity -> entity.getType() == EntityType.DROPPED_ITEM)
                        .map(entity -> (Item) entity)
                        .filter(item -> item.getItemStack().getType() == Material.SKULL_ITEM &&
                                item.getItemStack().hasItemMeta() &&
                                item.getItemStack().getItemMeta().hasLore() &&
                                item.getItemStack().getItemMeta().getLore().get(0).equals(player.getLocaleMessage("gui.add-player-to-cart.lore")))
                        .forEach(item ->
                        {
                            System.out.println(String.format("Removing item %s", item.getItemStack().toString()));
                            item.remove();
                        });
            });

            Bukkit.getScheduler().runTaskAsynchronously(store, () ->
            {
                String input = event.getName();
                OfflinePlayer target = null;
                try
                {
                    System.out.println("AddPlayerClick [2]");
                    if (input.replace("-", "").length() == 32)
                    {
                        System.out.println("AddPlayerClick [3]");
                        String uuidStr = input.replace("-", "");
                        uuidStr = uuidStr.substring(0, 8) +
                                "-" +
                                uuidStr.substring(8, 12) +
                                "-" +
                                uuidStr.substring(12, 16) +
                                "-" +
                                uuidStr.substring(16, 20) +
                                "-" +
                                uuidStr.substring(20, 32);
                        System.out.println("AddPlayerClick [4]");
                        UUID uuid = UUID.fromString(uuidStr);
                        System.out.println("AddPlayerClick [5]");
                        target = Bukkit.getOfflinePlayer(uuid);
                        System.out.println("AddPlayerClick [6]");
                    }
                }
                catch (Exception ignored)
                {
                    System.out.println("AddPlayerClick [7]");
                }

                System.out.println("AddPlayerClick [8]");

                if (target == null)
                {
                    System.out.println("AddPlayerClick [9]");
                    target = Bukkit.getOfflinePlayer(input);
                    System.out.println("AddPlayerClick [10]");

                    if (target == null)
                    {
                        System.out.println("AddPlayerClick [11]");
                        Bukkit.getScheduler().runTask(store, () ->
                        {
                            player.stopCancel(Ignore.ADD_PLAYER);
                            player.openPage(new AddPlayerCartErrorPage(store, player, player.getLocaleMessage("gui.add-player-to-cart.notfound")), true);
                            System.out.println("AddPlayerClick [12]");
                        });
                        return;
                    }
                }
                OfflinePlayer finalTarget = target;
                Bukkit.getScheduler().runTask(store, () ->
                {
                    System.out.println("AddPlayerClick [13]");
                    player.getCart().purchaseFor(finalTarget.getUniqueId());
                    player.stopCancel(Ignore.ADD_PLAYER);
                    System.out.println("AddPlayerClick [14]");
                    player.closeGUI(false);
                    System.out.println("AddPlayerClick [15]");
                    player.openPage(new UserHomePage(store, player), true);
                    System.out.println("AddPlayerClick [16]");
                    player.openPage(new CartPage(store, player), true);
                    System.out.println("AddPlayerClick [17]");

                });

            });
            /*String input = event.getName();
            OfflinePlayer target = null;
            try
            {
                System.out.println("AddPlayerClick [2]");
                if (input.replace("-", "").length() == 32)
                {
                    System.out.println("AddPlayerClick [3]");
                    String uuidStr = input.replace("-", "");
                    uuidStr = uuidStr.substring(0, 8) +
                            "-" +
                            uuidStr.substring(8, 12) +
                            "-" +
                            uuidStr.substring(12, 16) +
                            "-" +
                            uuidStr.substring(16, 20) +
                            "-" +
                            uuidStr.substring(20, 32);
                    System.out.println("AddPlayerClick [4]");
                    UUID uuid = UUID.fromString(uuidStr);
                    System.out.println("AddPlayerClick [5]");
                    target = Bukkit.getOfflinePlayer(uuid);
                    System.out.println("AddPlayerClick [6]");
                }
            }
            catch (Exception ignored)
            {
                System.out.println("AddPlayerClick [7]");
            }

            System.out.println("AddPlayerClick [8]");

            if (target == null)
            {
                System.out.println("AddPlayerClick [9]");
                target = Bukkit.getOfflinePlayer(input);
                System.out.println("AddPlayerClick [10]");

                if (target == null)
                {
                    System.out.println("AddPlayerClick [11]");
                    player.stopCancel(Ignore.ADD_PLAYER);
                    player.openPage(new AddPlayerCartErrorPage(store, player, player.getLocaleMessage("gui.add-player-to-cart.notfound")), true);
                    System.out.println("AddPlayerClick [12]");
                    return;
                }
            }



            System.out.println("AddPlayerClick [13]");
            player.getCart().purchaseFor(target.getUniqueId());
            player.stopCancel(Ignore.ADD_PLAYER);
            System.out.println("AddPlayerClick [14]");
            player.closeGUI(false);
            System.out.println("AddPlayerClick [15]");
            player.openPage(new UserHomePage(store, player), true);
            System.out.println("AddPlayerClick [16]");
            player.openPage(new CartPage(store, player), true);
            System.out.println("AddPlayerClick [17]");*/
        });
        return inventory;
    }

}
