package com.ulfric.store;

import com.google.common.collect.Lists;
import com.ulfric.store.factory.CouponFactory;
import com.ulfric.store.factory.SaleFactory;
import com.ulfric.store.factory.StoreFactory;
import com.ulfric.store.manage.*;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

public class Store extends JavaPlugin {

    private List<Manager> managers;
    private StoreFactory storeFactory;
    private CouponFactory couponFactory;
    private SaleFactory saleFactory;

    @Override
    public void onEnable()
    {
        getDataFolder().mkdirs();
        new File(getDataFolder(), "logs").mkdirs();
        loadFactories();
        loadManagers();
    }

    @Override
    public void onDisable()
    {
        unloadManagers();
    }

    private void loadFactories()
    {
        this.couponFactory = new CouponFactory(this);
        this.saleFactory = new SaleFactory(this);
    }

    private void loadManagers()
    {
        managers = Lists.newArrayList(
                new StoreManager(this),
                new CategoryManager(this),
                new PackageManager(this),
                new LogManager(this),
                new CommandManager(this),
                new TransactionManager(this),
                new CouponManager(this),
                new SaleManager(this),
                new ConfigManager(this)
        );

        managers.forEach(Manager::onEnable);

        /*managers.add(new StoreManager(this));
        managers.add(new CategoryManager(this));
        managers.add(new PackageManager(this));
        managers.add(new LogManager(this));
        managers.add(new CommandManager(this));
        managers.add(new TransactionManager(this));
        managers.add(new CouponManager(this));
        managers.add(new SaleManager(this));
        managers.add(new ConfigManager(this));*/
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

    public StoreFactory getStoreFactory()
    {
        return storeFactory;
    }

    public void setStoreFactory(StoreFactory storeFactory)
    {
        this.storeFactory = storeFactory;
    }

    public CouponFactory getCouponFactory()
    {
        return couponFactory;
    }

    public SaleFactory getSaleFactory()
    {
        return saleFactory;
    }
}
