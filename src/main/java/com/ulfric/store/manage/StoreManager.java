package com.ulfric.store.manage;

import com.google.common.collect.Maps;
import com.ulfric.store.Store;
import com.ulfric.store.shop.StoreAppliable;

import java.util.Map;

public class StoreManager extends Manager {

    private final Map<Integer, StoreAppliable> items = Maps.newHashMap();

    public StoreManager(Store store)
    {
        super(store);
    }

    public void addItem(StoreAppliable item)
    {
        items.put(item.getId(), item);
    }

    public StoreAppliable getById(int id)
    {
        return items.get(id);
    }

}
