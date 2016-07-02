package com.ulfric.store.manage;

import com.google.common.collect.Lists;
import com.ulfric.store.Store;

import java.util.List;

public abstract class ListManager<T> extends Manager {

    protected List<T> elements = Lists.newArrayList();

    public ListManager(Store store)
    {
        super(store);
    }

    public void add(T element)
    {
        elements.add(element);
    }

    public List<T> get()
    {
        return Lists.newArrayList(elements);
    }

}
