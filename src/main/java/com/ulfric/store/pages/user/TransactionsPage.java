package com.ulfric.store.pages.user;

import com.ulfric.store.Store;
import com.ulfric.store.gui.GUIPage;
import com.ulfric.store.gui.PageInventory;
import com.ulfric.store.gui.StoreInventory;
import com.ulfric.store.manage.TransactionManager;
import com.ulfric.store.manage.player.StorePlayer;
import com.ulfric.store.shop.Transaction;
import com.ulfric.store.util.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.stream.Collectors;

public class TransactionsPage extends GUIPage {

    public TransactionsPage(Store store, StorePlayer player)
    {
        super(store, player, player.getLocaleMessage("gui.transactions.title"));
    }

    @Override
    protected StoreInventory loadInventory()
    {
        List<Transaction> transactions = store.getManager(TransactionManager.class).get()
                .stream()
                .filter(transaction -> transaction.getPlayerFor().equals(player.player().getUniqueId()))
                .collect(Collectors.toList());
        List<ItemStack> items = transactions
                .stream()
                .map(transaction ->
                {
                    return ItemBuilder.of(Material.PAPER)
                            .withName(
                                    player.getLocaleMessage(
                                            "",
                                            "gui.transactions.name",
                                            transaction.getTransactionId().toString().replace("-", "")
                                    )
                            )
                            .withLore(
                                    transaction.getPackages().stream().map(pack ->
                                    {
                                        return "- " + pack.getTitle();
                                    }).collect(Collectors.toList())
                            )
                            .build();
                }).collect(Collectors.toList());
        PageInventory inventory = new PageInventory(store, player, title);
        inventory.withItems(items);
        return inventory;
    }
}
