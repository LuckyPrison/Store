package com.ulfric.store.manage;

import com.ulfric.store.Store;

public abstract class Manager {

    protected final Store store;

    public Manager(Store store)
    {
        this.store = store;
    }

}
