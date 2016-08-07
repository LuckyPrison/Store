package com.ulfric.store.pages.user;

import com.ulfric.store.Store;
import com.ulfric.store.gui.GUIPage;
import com.ulfric.store.gui.StandardInventory;
import com.ulfric.store.gui.StoreInventory;
import com.ulfric.store.manage.player.StorePlayer;
import com.ulfric.store.shop.Package;
import com.ulfric.store.util.Chat;
import com.ulfric.store.util.ItemBuilder;
import com.ulfric.store.util.Items;
import org.bukkit.Material;

public class AddToCartPage extends GUIPage {

    private Package pack;
    private int amount;

    public AddToCartPage(Store store, StorePlayer player, Package pack)
    {
        this(store, player, pack, 1);
    }

    public AddToCartPage(Store store, StorePlayer player, Package pack, int amount)
    {
        super(store, player, player.getLocaleMessage("gui.add-to-cart.title"), pack, amount);
    }

    @Override
    protected StoreInventory loadInventory()
    {
        this.pack = (Package) params[0];
        this.amount = (int) params[1] > 0 ? (int) params[1] : 1;
        StandardInventory inventory = new StandardInventory(store, player, 45, title);

        inventory.setItem(
                22,
                ItemBuilder.of(pack.getIcon().getIcon(), true)
                        .withName(Chat.color("&d" + pack.getTitle()))
                        .withLore(
                                player.getLocaleMessage("", "chat.package.quantity", String.valueOf(amount)),
                                player.getLocaleMessage("", "chat.package.price", String.valueOf(pack.getPrice() * amount)),
                                player.getLocaleMessage("gui.add-to-cart.confirm")
                        )
                        .build(),
                event ->
                {
                    player.getCart().withPackage(pack);
                    player.closeGUI(false);
                    player.openPage(new UserHomePage(store, player), true);
                    player.openPage(new CartPage(store, player), true);

                }
        );

        inventory.setItem(
                13,
                ItemBuilder.of(Material.WOOL)
                        .withDurability(Items.ItemColor.LIME)
                        .withName(player.getLocaleMessage("gui.add-to-cart.quantity.add"))
                        .build(),
                event ->
                {
                    System.out.println(String.format("Amount: %d, Amount + 1: %d", amount, amount + 1));
                    player.openPage(new AddToCartPage(store, player, pack, amount + 1), false);
                }
        );

        if (amount > 1)
        {
            inventory.setItem(
                    31,
                    ItemBuilder.of(Material.WOOL)
                            .withDurability(Items.ItemColor.RED)
                            .withName(player.getLocaleMessage("gui.add-to-cart.quantity.minus"))
                            .build(),
                    event ->
                    {
                        player.openPage(new AddToCartPage(store, player, pack, amount - 1), false);
                    }
            );
        }

        return inventory;
    }

}
