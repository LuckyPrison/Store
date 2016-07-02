package com.ulfric.store.shop;

import com.google.common.collect.Lists;

import java.util.List;

public class Category implements StoreAppliable {

    private String title;
    private List<Package> packages;

    public Category(String title)
    {
        this.title = title;
    }

    public Category withPackage(Package item)
    {
        packages.add(item);
        return this;
    }

    public Category withoutPackage(Package item)
    {
        packages.remove(item);
        return this;
    }

    public List<Package> getPackages()
    {
        return Lists.newArrayList(packages);
    }

}
