package com.ulfric.store;

import com.google.common.collect.Lists;
import com.ulfric.store.manage.*;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

public class Store extends JavaPlugin {

    private List<Manager> managers = Lists.newArrayList(
            new CommandManager(this),
            new CategoryManager(this),
            new PackageManager(this),
            new CouponManager(this),
            new SaleManager(this)
    );

    private CommandManager commandManager;
    private CategoryManager categoryManager;
    private PackageManager packageManager;
    private CouponManager couponManager;
    private SaleManager saleManager;

    @Override
    public void onEnable()
    {
        loadManagers();
    }

    private void loadManagers()
    {
        commandManager = new CommandManager(this);
        categoryManager = new CategoryManager(this);
        packageManager = new PackageManager(this);
        couponManager = new CouponManager(this);
        saleManager = new SaleManager(this);
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
