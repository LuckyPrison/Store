package com.ulfric.store.pages.user;

import com.google.common.collect.Lists;
import com.ulfric.store.Store;
import com.ulfric.store.gui.GUIPage;
import com.ulfric.store.gui.StandardInventory;
import com.ulfric.store.gui.StoreInventory;
import com.ulfric.store.manage.player.StorePlayer;
import com.ulfric.store.shop.PurchasePackage;
import com.ulfric.store.util.Chat;
import com.ulfric.store.util.ItemBuilder;
import com.ulfric.store.util.Items;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class CartPage extends GUIPage {

    public CartPage(Store store, StorePlayer player)
    {
        super(store, player, player.getLocaleMessage("gui.cart.title"));
    }

    @Override
    protected StoreInventory loadInventory()
    {
        StandardInventory inventory = new StandardInventory(store, player, 54, title);

        inventory.setItem(
                0,
                ItemBuilder.of(Material.ARROW)
                        .withName(player.getLocaleMessage("&c", "generic.back", ""))
                        .build(),
                event -> player.closePage()
        );

        inventory.setItem(
                4,
                ItemBuilder.of(Material.MINECART)
                        .withName(player.getLocaleMessage("store.cart"))
                        .withLore(
                                player.getLocaleMessage("store.current-cost") + player.getCart().calculateCost()
                        )
                        .build()
        );

        List<String> skullLore = Lists.newArrayList();

        player.getCart().getPurchaseFor().forEach(uuid ->
        {
            skullLore.add(Bukkit.getOfflinePlayer(uuid).getName());
            skullLore.add("- " + player.getLocaleMessage("generic.uuid") + ": " + uuid.toString());
        });

        if (skullLore.isEmpty())
        {
            skullLore.add(player.getLocaleMessage("generic.noone"));
            skullLore.add(player.getLocaleMessage("gui.cart.skull.addplayer.1"));
            skullLore.add(player.getLocaleMessage("gui.cart.skull.addplayer.2"));
        }

        inventory.setItem(
                7,
                ItemBuilder.of(Material.SKULL_ITEM)
                        .withDurability(3)
                        .withName(player.getLocaleMessage("store.purchase-for"))
                        .withLore(skullLore)
                        .build()
        );

        inventory.setItem(
                8,
                ItemBuilder.of(Material.NAME_TAG)
                        .withName(player.getLocaleMessage("gui.cart.addplayer.name"))
                        .withLore(
                                player.getLocaleMessage("gui.cart.addplayer.1"),
                                player.getLocaleMessage("gui.cart.addplayer.2")
                        )
                        .build(),
                event ->
                {
                    player.openPage(new AddPlayerCartPage(store, player), true);
                }
        );

        inventory.setItem(
                18,
                ItemBuilder.of(Material.CHEST)
                        .withName(player.getLocaleMessage("gui.cart.packs.name"))
                        .build(),
                event ->
                {
                    player.openPage(new CategoriesPage(store, player), true);
                }
        );

        List<String> confirmLore = Lists.newArrayList();
        boolean success = false;

        if (player.getCart().getPackages().isEmpty())
        {
            confirmLore.add(player.getLocaleMessage("gui.cart.confirm.nopackages.1"));
            confirmLore.add(player.getLocaleMessage("gui.cart.confirm.nopackages.2"));
        }
        else if (player.getCart().getPurchaseFor().isEmpty())
        {
            confirmLore.add(player.getLocaleMessage("gui.cart.confirm.noplayers.1"));
            confirmLore.add(player.getLocaleMessage("gui.cart.confirm.noplayers.2"));
        }
        else
        {
            success = true;
            confirmLore.add(player.getLocaleMessage("gui.cart.confirm.success.1"));
            confirmLore.add(player.getLocaleMessage("gui.cart.confirm.success.2"));
        }

        final boolean finalSuccess = success;

        inventory.setItem(
                22,
                ItemBuilder.of(Material.WOOL)
                        .withDurability(Items.ItemColor.LIME)
                        .withName(success ? player.getLocaleMessage("gui.cart.confirm.success") : player.getLocaleMessage("gui.cart.confirm.error"))
                        .withLore(confirmLore)
                        .build(),
                event ->
                {
                    if (finalSuccess)
                    {
                        player.closeGUI(false);
                        player.openPage(new CartConfirmPage(store, player), true);
                    }
                }
        );

        if (player.getCart().getPackages().size() > 0)
        {
            loadPackages(inventory);
        }

        if (player.getCart().getPurchaseFor().size() > 0)
        {
            loadPlayers(inventory);
        }

        return inventory;
    }

    private void loadPackages(StandardInventory inventory)
    {
        List<PurchasePackage> packages = player.getCart().getPackages();
        for (int i = 0; i < packages.size(); i++)
        {
            PurchasePackage pack = packages.get(i);
            ItemStack icon = pack.getPack().getIcon().getIcon();
            ItemMeta meta = icon.getItemMeta();
            meta.setDisplayName(Chat.color("&b" + pack.getPack().getTitle()));
            meta.setLore(
                    Arrays.asList(
                            player.getLocaleMessage("store.see-description"),
                            player.getLocaleMessage("store.remove-from-cart"),
                            player.getLocaleMessage("&b", "generic.cost", ": &a$" + pack.getPack().getPrice())
                    )
            );
            icon.setItemMeta(meta);
            int index;
            if (i < 6)
            {
                index = i + 27;
            }
            else if (i < 12)
            {
                index = (i - 6) + 36;
            }
            else
            {
                index = (i - 12) + 45;
            }
            inventory.setItem(
                    index,
                    icon,
                    event ->
                    {
                        if (event.getClick() == ClickType.LEFT)
                        {
                            player.showDescription(pack.getPack());
                        }
                        else if (event.getClick() == ClickType.RIGHT)
                        {
                            player.getCart().withoutPackage(pack.getPack());
                            player.openPage(new CartPage(store, player), false);
                        }
                    }
            );
        }
    }

    private void loadPlayers(StandardInventory inventory)
    {
        List<UUID> players = player.getCart().getPurchaseFor();
        for (int i = 0; i < players.size(); i++)
        {
            OfflinePlayer player = Bukkit.getOfflinePlayer(players.get(i));
            ItemStack skull = ItemBuilder.of(Material.SKULL_ITEM)
                    .withOwner(player.getName())
                    .withName(Chat.color("&b" + player.getName()))
                    .withLore(
                            this.player.getLocaleMessage("&bUUID: ", "generic.uuid", ""),
                            this.player.getLocaleMessage("store.remove-player")
                    )
                    .build();

            int index;

            if (i < 5)
            {
                index = ((i + 1) * 9) + 7;
            }
            else
            {
                index = (((i - 5) + 1) * 9) + 8;
            }

            inventory.setItem(
                    index,
                    skull,
                    event ->
                    {
                        this.player.getCart().purchaseWithout(player.getUniqueId());
                        this.player.openPage(new CartPage(store, this.player), false);
                    }
            );
        }
    }

}
