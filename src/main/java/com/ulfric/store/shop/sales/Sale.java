package com.ulfric.store.shop.sales;

import com.google.common.collect.Lists;
import com.ulfric.store.shop.StoreAppliable;

import java.util.Date;
import java.util.List;

public class Sale {

    private String name;

    private DiscountType type;
    private Double magnitude;

    private Date start;
    private Date finish;

    private List<StoreAppliable> appliables = Lists.newArrayList();

    public Sale(String name, DiscountType type, Double magnitude, Date start, Date finish)
    {
        this.name = name;
        this.type = type;
        this.magnitude = magnitude;
        this.start = start;
        this.finish = finish;
    }

    public Sale applyFor(StoreAppliable appliable)
    {
        appliables.add(appliable);
        return this;
    }

    public Sale applyNotFor(StoreAppliable appliable)
    {
        appliables.remove(appliable);
        return this;
    }

    public boolean validFor(StoreAppliable appliable)
    {
        for (StoreAppliable poss : appliables)
        {
            if (appliable.appliableTo(poss))
            {
                return true;
            }
        }
        return false;
    }

}
