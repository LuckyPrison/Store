package com.ulfric.store.shop;

import com.ulfric.store.Store;
import com.ulfric.store.config.ConfigSerializable;
import com.ulfric.store.manage.ConfigManager;
import com.ulfric.store.manage.StoreManager;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.List;
import java.util.stream.Collectors;

public class Category implements StoreAppliable, ConfigSerializable {

    public static void serialize(Store store, Category category, YamlConfiguration config)
    {
        config.set("categories." + category.id + ".title", category.title);
        config.set("categories." + category.id + ".packages", category.packages);
    }

    public static Category deserialize(Store store, YamlConfiguration config, int id)
    {
        Category category = new Category(
                store,
                id,
                config.getString("categories." + id + ".title")
        );
        config.getIntegerList("categories." + id + ".packages").forEach(category::withPackage);
        return category;
    }

    private final Store store;
    private final int id;
    private String title;
    private List<Integer> packages;

    public Category(Store store, int id, String title)
    {
        this.store = store;
        this.id = id;
        this.title = title;
    }

    public Category withPackage(int item)
    {
        packages.add(item);
        return this;
    }

    public Category withoutPackage(int item)
    {
        packages.remove(item);
        return this;
    }

    public List<Package> getPackages()
    {
        StoreManager storeManager = store.getManager(StoreManager.class);
        return packages.stream().map(id -> (Package) storeManager.getById(id)).collect(Collectors.toList());
    }

    @Override
    public int getId()
    {
        return id;
    }

    @Override
    public void save()
    {
        store.getManager(ConfigManager.class).saveCategory(this);
    }
}
