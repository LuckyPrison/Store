package com.ulfric.store.pages.user;

import com.ulfric.store.Store;
import com.ulfric.store.gui.GUIPage;
import com.ulfric.store.gui.PageInventory;
import com.ulfric.store.gui.StoreInventory;
import com.ulfric.store.manage.StoreManager;
import com.ulfric.store.manage.player.StorePlayer;
import com.ulfric.store.shop.Category;
import com.ulfric.store.shop.Package;
import com.ulfric.store.shop.StoreAppliable;
import com.ulfric.store.util.Chat;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.stream.Collectors;

public class CategoryContentsPage extends GUIPage {

    private Category category;

    public CategoryContentsPage(Store store, StorePlayer player, Category category)
    {
        super(store, player, player.getLocaleMessage("gui.packages.list.title"), category);
    }

    @Override
    protected StoreInventory loadInventory()
    {
        this.category = (Category) params[0];
        PageInventory inventory = new PageInventory(store, player, title);

        List<ItemStack> items = category.getPackages().stream().map(pack ->
        {
            ItemStack item = pack.getIcon().getIcon();
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(Chat.color("&b" + pack.getTitle()));
            meta.setLore(
                    Arrays.asList(
                            Chat.color("&8ID: " + pack.getId()),
                            player.getLocaleMessage("store.see-description"),
                            player.getLocaleMessage("store.add-to-cart")
                    )
            );
            item.setItemMeta(meta);
            return item;
        }).collect(Collectors.toList());

        inventory.withItems(items);

        return inventory;
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
        String idStr = item.getItemMeta().getLore().get(0).substring("&8ID: ".length());
        int id;
        try
        {
            id = Integer.parseInt(idStr);
        }
        catch (NumberFormatException e)
        {
            store.getLogger().log(Level.WARNING, "Could not parse ID in CategoryContentsPage");
            return;
        }
        StoreAppliable apply = store.getManager(StoreManager.class).getById(id);
        if (apply == null)
        {
            store.getLogger().log(Level.WARNING, String.format("StoreAppliable with ID %d in CategoryContentsPage is null", id));
            return;
        }
        if (!(apply instanceof Package))
        {
            store.getLogger().log(Level.WARNING, String.format("StoreAppliable with ID %d in CategoryContentsPage is not a Package", id));
            return;
        }
        if (event.getClick() == ClickType.LEFT)
        {
            player.showDescription((Package) apply);
        }
        else if (event.getClick() == ClickType.RIGHT)
        {
            player.openPage(new AddToCartPage(store, player, (Package) apply), true);
        }
    }

}
