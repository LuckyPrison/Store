package com.ulfric.store.gui;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.ulfric.store.Store;
import com.ulfric.store.manage.player.StorePlayer;
import com.ulfric.store.util.Chat;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import javax.naming.OperationNotSupportedException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

public class PageInventory implements StoreInventory {

    private static final ItemStack BACK_BUTTON;
    private static final ItemStack FORWARD_BUTTON;

    static
    {
        BACK_BUTTON = new ItemStack(Material.ARROW);
        FORWARD_BUTTON = new ItemStack(Material.ARROW);

        ItemMeta backMeta = BACK_BUTTON.getItemMeta();
        backMeta.setDisplayName(Chat.color("&bBack Page"));
        BACK_BUTTON.setItemMeta(backMeta);

        ItemMeta forwardMeta = FORWARD_BUTTON.getItemMeta();
        forwardMeta.setDisplayName(Chat.color("&bForward Page"));
        FORWARD_BUTTON.setItemMeta(forwardMeta);
    }

    private final Store store;
    private final StorePlayer player;
    private final Map<Integer, InventoryAction> actions = Maps.newHashMap();

    private final List<ItemStack> contents = Lists.newArrayList();
    private final String title;

    private int page = 1;
    private int totalPages = 1;

    public PageInventory(Store store, StorePlayer player, String title)
    {
        this.store = store;
        this.player = player;
        this.title = title;
    }

    public PageInventory addItem(ItemStack item)
    {
        this.contents.add(item);
        this.recalculate();
        return this;
    }

    public PageInventory withItems(Collection<ItemStack> items)
    {
        this.contents.addAll(items);
        this.recalculate();
        return this;
    }

    public PageInventory withItems(ItemStack... items)
    {
        this.contents.addAll(Arrays.asList(items));
        this.recalculate();
        return this;
    }

    @Override
    public void open()
    {
        if (totalPages == 1)
        {
            Inventory inventory = store.getServer().createInventory(null, calcSize(contents.size()), title);
            int slot = 0;
            for (ItemStack item : contents)
            {
                inventory.setItem(slot++, item);
            }
            player.ignore(Ignore.PAGE);
            player.player().openInventory(inventory);
            player.stopIgnore(Ignore.PAGE);
            return;
        }

        int startPoint = (this.page - 1) * 45;
        List<ItemStack> invContents = Lists.newArrayList();

        ItemStack item;
        try
        {
            while ((item = this.contents.get(startPoint++)) != null)
            {
                invContents.add(item);
                if (startPoint - ((this.page - 1) * 45) == 45) break;
            }
        }
        catch (IndexOutOfBoundsException ignored)
        {
        }
        // Resolve this, rather than catching the exception. It'll be much more performant

        Inventory inventory = store.getServer().createInventory(null, 54, this.title);

        int slot = 0;
        for (ItemStack invItem : invContents)
        {
            inventory.setItem(slot++, invItem);
        }

        if (this.page > 1)
        {
            inventory.setItem(45, PageInventory.BACK_BUTTON);
        }
        if (this.page < this.getPages(this.contents.size()))
        {
            inventory.setItem(53, PageInventory.FORWARD_BUTTON);
        }

        player.ignore(Ignore.PAGE);
        player.player().openInventory(inventory);
        player.stopIgnore(Ignore.PAGE);
    }

    private void backPage()
    {
        page--;
        open();
    }

    private void forwardPage()
    {
        page++;
        open();
    }

    private void recalculate()
    {
        this.totalPages = this.contents.size() > 54 ? this.contents.size() / 45 : 1;
    }

    private int calcSize(int size)
    {
        return (((size - 1) / 9) + 1) * 9;
    }

    private int getPages(int size)
    {
        if (size % 45 == 0)
        {
            return size / 45;
        }
        Double d = ((double) size + 1) / 45;
        return (int) Math.ceil(d);
    }

    @Override
    public boolean hasAction(int slot)
    {
        if (totalPages > 1)
        {
            if (slot == 45)
            {
                backPage();
            }
            else if (slot == 53)
            {
                forwardPage();
            }
        }
        return false;
    }

    @Override
    public void executeAction(int slot, InventoryClickEvent event)
    {
        store.getLogger().log(Level.WARNING, "executeAction was called on PageInventory", new OperationNotSupportedException());
    }

    @Override
    public void setItem(int slot, ItemStack item)
    {
        store.getLogger().log(Level.WARNING, "setItem(int, ItemStack) was called on PageInventory", new OperationNotSupportedException());
    }

    @Override
    public void setItem(int slot, ItemStack item, InventoryAction action)
    {
        store.getLogger().log(Level.WARNING, "setItem(int, ItemStack, InventoryAction) was called on PageInventory", new OperationNotSupportedException());
    }

}
