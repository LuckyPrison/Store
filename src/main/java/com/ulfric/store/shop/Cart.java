package com.ulfric.store.shop;

import com.google.common.collect.Lists;
import com.ulfric.store.Store;
import com.ulfric.store.shop.sales.*;
import org.apache.commons.lang3.RandomStringUtils;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class Cart implements Discountable {

    private static final int ID_LENGTH = 16;

    private final Store store;

    private final String cartId;

    private Player owner;

    private List<UUID> purchaseFor = Lists.newArrayList();
    private List<PurchasePackage> packages = Lists.newArrayList();

    private List<Coupon> cartCoupons = Lists.newArrayList();
    private Discount cartDiscount;

    public Cart(Store store, Player player)
    {
        this.cartId = RandomStringUtils.random(ID_LENGTH);
        this.store = store;
        this.owner = player;
    }

    public Cart purchaseFor(UUID uuid)
    {
        this.purchaseFor.add(uuid);
        return this;
    }

    public Cart purchaseWithout(UUID uuid)
    {
        this.purchaseFor.remove(uuid);
        return this;
    }

    public Cart purchaseForNone()
    {
        this.purchaseFor.clear();
        return this;
    }

    public Cart withPackage(Package item)
    {
        this.packages.add(new PurchasePackage(store, item));
        return this;
    }

    public boolean applyCoupon(Coupon coupon)
    {
        if (coupon.getCouponType() == CouponType.CART)
        {
            this.cartCoupons.add(coupon);
            return true;
        }
        else
        {
            List<PurchasePackage> packagesSorted = Lists.newArrayList(packages);
            packagesSorted.sort((a, b) -> Double.compare(a.getPack().getPrice(), b.getPack().getPrice()));

            for (PurchasePackage pack : packagesSorted)
            {
                if (pack.applyCoupon(coupon))
                {
                    return true;
                }
            }
            return false;
        }

    }

    public double calculateCost()
    {
        double price = 0;
        for (PurchasePackage item : packages)
        {
            price += (item.getPack().getPrice() * purchaseFor.size());
        }
        return price;
    }

    public double calculateFinalCost()
    {
        double total = 0.0;

        for (PurchasePackage pack : packages)
        {
            total += pack.getDiscountedPrice();
        }

        this.cartDiscount = new Discount(total);

        List<Coupon> sortedCartCoupons = Lists.newArrayList(cartCoupons);

        sortedCartCoupons.sort((a, b) ->
        {
            if (a.getDiscountType() == DiscountType.AMOUNT) return 1;
            if (b.getDiscountType() == DiscountType.AMOUNT) return -1;
            return 0;
        });

        sortedCartCoupons.forEach(coupon ->
        {
            cartDiscount.withCoupon(coupon);
        });

        total = cartDiscount.getCalculatedPrice();

        return total;
    }

    public String getCartId()
    {
        return cartId;
    }

    public Player getOwner()
    {
        return owner;
    }

    public List<UUID> getPurchaseFor()
    {
        return Lists.newArrayList(purchaseFor);
    }

    public List<PurchasePackage> getPackages()
    {
        return Lists.newArrayList(packages);
    }

    @Override
    public String toString()
    {
        return String.format(
                "Cart[id->%s:owner->%s:for->%s:packages->%s]",
                cartId,
                owner.getUniqueId(),
                purchaseFor.stream().map(UUID::toString).collect(Collectors.joining()),
                packages.stream().map(pack -> pack.getPack().getTitle()).collect(Collectors.joining())
        );
    }

    @Override
    public List<Discount> getDiscounts()
    {
        return null;
    }

    @Override
    public double getDiscountOff()
    {
        return 0;
    }

    @Override
    public double getDiscountedPrice()
    {
        return 0;
    }
}
