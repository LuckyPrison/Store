package com.ulfric.store.manage.player;

import com.google.common.collect.Lists;
import com.ulfric.store.Store;
import com.ulfric.store.gui.GUIPage;
import com.ulfric.store.gui.Ignore;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

import java.util.List;
import java.util.UUID;

public class StorePlayer {

    private final UUID uuid;
    private final Store store;

    private final List<GUIPage> crumb = Lists.newArrayList();
    private final List<Ignore> ignores = Lists.newArrayList();
    private final List<Ignore> cancels = Lists.newArrayList();

    public StorePlayer(Store store, Player player)
    {
        this.store = store;
        this.uuid = player.getUniqueId();
    }

    public boolean inGUI()
    {
        return crumb.size() > 0;
    }

    public void openPage(GUIPage page, boolean doc)
    {
        if (doc)
        {
            crumb.add(page);
        }
        ignores.add(Ignore.INTERNAL);
        page.open();
        ignores.remove(Ignore.INTERNAL);
    }

    public GUIPage currentPage()
    {
        if (!inGUI())
        {
            return null;
        }
        return crumb.get(currentIndex());
    }

    public int currentIndex()
    {
        return crumb.size() - 1;
    }

    public void onClose()
    {
        if (inGUI())
        {
            ignores.remove(currentIndex());
        }
    }

    public void onInventoryClick(InventoryClickEvent event)
    {
        if (!inGUI())
        {
            return;
        }
        if (event.getClickedInventory() == null)
        {
            return;
        }
        if (!event.getClickedInventory().equals(player().getOpenInventory().getTopInventory()))
        {
            return;
        }
        currentPage().onClick(event);
    }

    public void onInventoryClose(InventoryCloseEvent event)
    {
        if (ignoring())
        {
            return;
        }
        if (cancelling())
        {
            openPage(currentPage(), false);
        }
        if (inGUI())
        {
            currentPage().onClose();
        }
    }

    public void closePage()
    {
        currentPage().onClose();
    }

    public void closeGUI()
    {
        crumb.clear();
        player().closeInventory();
    }

    public boolean ignoring()
    {
        return ignores.size() > 0;
    }

    public void ignore(Ignore ignore)
    {
        ignores.add(ignore);
    }

    public void stopIgnore(Ignore ignore)
    {
        ignores.remove(ignore);
    }

    public boolean cancelling()
    {
        return cancels.size() > 0;
    }

    public Player player()
    {
        return store.getServer().getPlayer(uuid);
    }

}
