package com.ulfric.store.factory;

import com.ulfric.store.Store;
import com.ulfric.store.manage.ConfigManager;
import com.ulfric.store.shop.Category;
import com.ulfric.store.shop.Package;

import javax.annotation.Nullable;

public class StoreFactory {

    private static StoreFactory instance = null;

    public static StoreFactory getInstance()
    {
        return instance;
    }

    private Store store;
    private int currentId = 0;

    public StoreFactory(Store store, int currentId)
    {
        if (instance != null)
        {
            throw new IllegalStateException("Already instantiated!");
        }
        this.store = store;
        this.currentId = currentId;
        instance = this;
    }

    private int incrementAndGet()
    {
        currentId++;
        store.getManager(ConfigManager.class).incrementId(currentId);
        return currentId;
    }

    public Category createCategory(String title)
    {
        return new Category(store, incrementAndGet(), title);
    }

    public Package createPackage(String title, String description, @Nullable Category parent, Double price)
    {
        return new Package(store, incrementAndGet(), title, description, parent, price);
    }

}
