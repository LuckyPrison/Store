package com.ulfric.store;

import com.google.common.collect.Lists;
import com.ulfric.store.manage.*;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

public class Store extends JavaPlugin {

    private List<Manager> managers;

    @Override
    public void onEnable()
    {
        loadManagers();
    }

    @Override
    public void onDisable()
    {
        unloadManagers();
    }

    private void loadManagers()
    {
        managers = Lists.newArrayList(
                new CategoryManager(this),
                new PackageManager(this),
                new StoreManager(this),
                new CommandManager(this),
                new CouponManager(this),
                new SaleManager(this),
                new ConfigManager(this)
        );
    }

    private void unloadManagers()
    {
        managers.forEach(Manager::onDisable);
    }

    public <T extends Manager> T getManager(Class<T> type)
    {
        for (Manager manager : managers)
        {
            if (manager.getClass().equals(type))
            {
                return (T) manager;
            }
        }
        try
        {
            getLogger().severe(String.format("Process attempted to retrieve Manager of type %s but it was not registered!", type));
            getLogger().severe("Dumping Thread stack!");
            Thread.dumpStack();
            Manager manager = type.getDeclaredConstructor(Store.class).newInstance();
            managers.add(manager);
            return (T) manager;
        }
        catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException ignored) {}

        return null;
    }

}
