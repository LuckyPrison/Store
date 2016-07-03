package com.ulfric.store.shop;

import com.google.common.collect.Lists;
import com.ulfric.store.Store;
import com.ulfric.store.manage.SaleManager;
import com.ulfric.store.shop.sales.*;
import com.ulfric.store.util.StoreUtils;

import java.util.List;

public class PurchasePackage implements Discountable {

    private Package pack;
    private Discount discount;

    public PurchasePackage(Store store, Package pack)
    {
        double price = pack.getPrice();
        List<Sale> sales = store.getManager(SaleManager.class).getSales();
        sales.sort((a, b) ->
        {
            if (a.getType() == DiscountType.AMOUNT) return 1;
            if (b.getType() == DiscountType.AMOUNT) return -1;
            return 0;
        });
        for (Sale sale : sales)
        {
            if (sale.validFor(pack))
            {
                switch (sale.getType())
                {
                    case AMOUNT:
                        price = Math.max(0, price - sale.getMagnitude());
                        break;
                    case PERCENTAGE:
                        double mult = StoreUtils.getMultiplierFromPercentage(sale.getMagnitude());
                        price = Math.max(0, price * mult);
                        break;
                }
            }
        }
        this.pack = pack;
        this.discount = new Discount(price);
    }

    public boolean applyCoupon(Coupon coupon)
    {
        if (!coupon.appliesFor(pack))
        {
            return false;
        }
        discount.withCoupon(coupon);
        return true;
    }

    public Package getPack()
    {
        return pack;
    }

    @Override
    public List<Discount> getDiscounts()
    {
        return Lists.newArrayList(discount);
    }

    @Override
    public double getDiscountOff()
    {
        return discount.getDiscountAmount();
    }

    @Override
    public double getDiscountedPrice()
    {
        return discount.getCalculatedPrice();
    }
}
