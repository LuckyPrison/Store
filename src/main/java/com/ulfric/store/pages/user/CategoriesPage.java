package com.ulfric.store.pages.user;

import com.ulfric.store.Store;
import com.ulfric.store.gui.GUIPage;
import com.ulfric.store.gui.PageInventory;
import com.ulfric.store.manage.CategoryManager;
import com.ulfric.store.manage.StoreManager;
import com.ulfric.store.manage.player.StorePlayer;
import com.ulfric.store.shop.Category;
import com.ulfric.store.shop.StoreAppliable;
import com.ulfric.store.util.Chat;
import com.ulfric.store.util.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.logging.Level;
import java.util.stream.Collectors;

public class CategoriesPage extends GUIPage {

    public CategoriesPage(Store store, StorePlayer player)
    {
        super(store, player, player.getLocaleMessage("gui.categories.title"));
    }

    @Override
    protected void loadInventory()
    {
        PageInventory inventory = new PageInventory(store, player, title);

        List<ItemStack> items = store.getManager(CategoryManager.class).get()
                .stream()
                .map(category ->
                {
                    ItemStack item = ItemBuilder.of(Material.CHEST)
                            .withName(Chat.color("&b" + category.getTitle()))
                            .withLore(Chat.color("&8ID: " + category.getId()))
                            .build();
                    return item;
                })
                .collect(Collectors.toList());

        inventory.withItems(items);

        set(inventory);
    }

    @Override
    public void onInventoryClick(InventoryClickEvent event)
    {
        ItemStack item = event.getInventory().getItem(event.getSlot());
        if (item == null)
        {
            return;
        }
        if (!item.hasItemMeta())
        {
            return;
        }
        if (!item.getItemMeta().hasLore())
        {
            return;
        }
        String idStr = item.getItemMeta().getLore().get(1).substring("&8ID: ".length());
        int id;
        try
        {
            id = Integer.parseInt(idStr);
        }
        catch (NumberFormatException e)
        {
            store.getLogger().log(Level.WARNING, "Could not parse ID in CategoriesPage");
            return;
        }
        StoreAppliable apply = store.getManager(StoreManager.class).getById(id);
        if (apply == null)
        {
            store.getLogger().log(Level.WARNING, String.format("StoreAppliable with ID %d in CategoriesPage is null", id));
            return;
        }
        if (!(apply instanceof Category))
        {
            store.getLogger().log(Level.WARNING, String.format("StoreAppliable with ID %d in CategoriesPage is not a Category", id));
            return;
        }
        player.openPage(new CategoryContentsPage(store, player, (Category) apply), true);
    }

}
