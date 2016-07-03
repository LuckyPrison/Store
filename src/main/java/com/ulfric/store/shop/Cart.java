package com.ulfric.store.shop;

import com.google.common.collect.Lists;
import com.ulfric.store.Store;
import com.ulfric.store.shop.sales.Coupon;
import org.apache.commons.lang3.RandomStringUtils;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class Cart {

    private static final int ID_LENGTH = 16;

    private final Store store;

    private final String cartId;

    private Player owner;

    private List<UUID> purchaseFor = Lists.newArrayList();
    private List<Package> packages = Lists.newArrayList();

    private List<Coupon> appliedCoupons = Lists.newArrayList();

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
        this.packages.add(item);
        return this;
    }

    public double calculateCost()
    {
        double price = 0;
        for (Package item : packages)
        {
            price += (item.getPrice() * purchaseFor.size());
        }
        return price;
    }

    public double calculateFinalCost()
    {

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

    public List<Package> getPackages()
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
                packages.stream().map(Package::toString).collect(Collectors.joining())
        );
    }

}
