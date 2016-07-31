package com.ulfric.store.pages.user;

import com.mojang.authlib.GameProfile;
import com.ulfric.store.Store;
import com.ulfric.store.gui.GUIPage;
import com.ulfric.store.gui.StoreInventory;
import com.ulfric.store.gui.anvil.AnvilGUI;
import com.ulfric.store.gui.anvil.AnvilSlot;
import com.ulfric.store.manage.player.StorePlayer;
import com.ulfric.store.util.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.SkullType;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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
                        .withName("Name/UUID")
                        .build()
        );

        inventory.withClickConsumer(event ->
        {
            System.out.println("Start");
            long start = System.currentTimeMillis();
            event.setWillClose(false);
            System.out.println("AddPlayerClick [1]");
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
                try
                {
                    Method getServer = store.getServer().getClass().getDeclaredMethod("getServer");
                    Object console = getServer.invoke(store.getServer());
                    Method getUserCache = console.getClass().getDeclaredMethod("getUserCache");
                    Object userCache = getUserCache.invoke(console);
                    Method getProfile = userCache.getClass().getDeclaredMethod("getProfile", String.class);
                    Object profile = getProfile.invoke(userCache, input);
                    if (profile != null) {
                        Method getOfflinePlayer = store.getServer().getClass().getDeclaredMethod("getOfflinePlayer", GameProfile.class);
                        Object offlinePlayer = getOfflinePlayer.invoke(profile);
                        if (offlinePlayer != null) {
                            target = (OfflinePlayer) offlinePlayer;
                        }
                    }
                }
                catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e)
                {
                    e.printStackTrace();
                }

                if (target == null)
                {
                    System.out.println("AddPlayerClick [11]");
                    player.openPage(new AddPlayerCartErrorPage(store, player, player.getLocaleMessage("gui.add-player-to-cart.notfound")), true);
                    System.out.println("AddPlayerClick [12]");
                    return;
                }
                else
                {
                    System.out.println(String.format("AddPlayerClick Player[%s, %s, %s, %d, %d]", target.getUniqueId(), target.getName(), target.getBedSpawnLocation(), target.getFirstPlayed(), target.getLastPlayed()));
                }
            }



            System.out.println("AddPlayerClick [13]");
            player.getCart().purchaseFor(target.getUniqueId());
            System.out.println("AddPlayerClick [14]");
            player.closeGUI(false);
            System.out.println("AddPlayerClick [15]");
            player.openPage(new UserHomePage(store, player), true);
            System.out.println("AddPlayerClick [16]");
            player.openPage(new CartPage(store, player), true);
            System.out.println("AddPlayerClick [17]");
            System.out.println(String.format("Finish: Took %dms", (System.currentTimeMillis() - start)));
        });
        return inventory;
    }

}
