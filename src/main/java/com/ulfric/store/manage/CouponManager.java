package com.ulfric.store.manage;

import com.ulfric.store.Store;
import com.ulfric.store.shop.sales.Coupon;

import java.util.List;

public class CouponManager extends ListManager<Coupon> {

    public CouponManager(Store store)
    {
        super(store);
    }

    public void add(Coupon coupon, boolean newCoupon)
    {
        super.add(coupon);
        if (newCoupon)
        {
            store.getManager(ConfigManager.class).newCoupon(coupon);
        }
    }

    public void remove(Coupon coupon)
    {
        super.remove(coupon);
    }

    public List<Coupon> getCoupons()
    {
        return get();
    }

}
