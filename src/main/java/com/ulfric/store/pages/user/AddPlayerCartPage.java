package com.ulfric.store.pages.user;

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
            String input = event.getName();
            OfflinePlayer target = null;
            try
            {
                if (input.replace("-", "").length() == 32)
                {
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
                    UUID uuid = UUID.fromString(uuidStr);
                    target = Bukkit.getOfflinePlayer(uuid);
                }
            }
            catch (IllegalArgumentException ignored)
            {}

            if (target == null)
            {
                target = Bukkit.getOfflinePlayer(input);

                if (target == null)
                {
                    player.openPage(new AddPlayerCartErrorPage(store, player, player.getLocaleMessage("gui.add-player-to-cart.notfound")), true);
                    return;
                }
            }

            player.getCart().purchaseFor(target.getUniqueId());
            player.closeGUI(false);
            player.openPage(new UserHomePage(store, player), true);
            player.openPage(new CartPage(store, player), true);
        });
        return inventory;
    }

}
