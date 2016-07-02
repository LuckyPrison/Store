package com.ulfric.store.shop.sales;

import com.google.common.collect.Lists;
import com.ulfric.store.shop.StoreAppliable;
import com.ulfric.store.shop.expiry.Expiry;

import java.util.List;

public class Coupon {

    private String code;
    private Expiry expiry;

    private Double minValue;

    private List<StoreAppliable> appliables = Lists.newArrayList();

    public Coupon(String code, Expiry expiry, Double minValue)
    {
        this.code = code;
        this.expiry = expiry;
        this.minValue = minValue == null || minValue < 0 ? 0 : minValue;
    }

    public Coupon applyFor(StoreAppliable appliable)
    {
        appliables.add(appliable);
        return this;
    }

    public Coupon applyNotFor(StoreAppliable appliable)
    {
        appliables.remove(appliable);
        return this;
    }

    public String getCode() {
        return code;
    }

    public Expiry getExpiry() {
        return expiry;
    }

    public Double getMinValue() {
        return minValue;
    }
}
