package com.ulfric.store.shop;

import com.google.common.collect.Lists;
import com.ulfric.store.Store;
import com.ulfric.store.manage.LogManager;
import com.ulfric.store.shop.sales.*;
import com.ulfric.store.util.StoreUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class Cart {

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
        this.cartId = RandomStringUtils.randomAlphanumeric(ID_LENGTH);
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

    public Cart withoutPackage(Package item)
    {
        List<PurchasePackage> toRemove = packages.stream()
                .filter(pack -> pack.getPack().getId() == item.getId())
                .collect(Collectors.toList());
        packages.removeAll(toRemove);
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
            price += (item.getPack().getPrice());
        }
        return price * purchaseFor.size();
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

        total *= purchaseFor.size();

        return total;
    }

    public List<Transaction> purchase()
    {
        List<Transaction> transactions = Lists.newArrayList();
        purchaseFor.forEach(uuid ->
        {
            Transaction transaction = new Transaction(
                    store,
                    uuid,
                    packages.stream().map(PurchasePackage::getPack).collect(Collectors.toList())
            );
            transaction.execute();
            transactions.add(transaction);
        });
        log();
        return transactions;
    }

    private void log()
    {
        LogManager log = store.getManager(LogManager.class);
        YamlConfiguration config = log.getCartLog().getConfig();
        config.set(cartId + ".instant", StoreUtils.readableTimestamp(Instant.now()));
        config.set(cartId + ".owner.uuid", owner.getUniqueId().toString());
        config.set(cartId + ".owner.current-name", owner.getName());
        config.set(cartId + ".for", purchaseFor.stream().map(UUID::toString).collect(Collectors.toList()));
        config.set(cartId + ".packages", packages.stream().map(pack -> pack.getPack().getTitle() + ":" + pack.getPack().getId()).collect(Collectors.toList()));
        config.set(cartId + ".full-price", calculateCost());
        config.set(cartId + ".real-price", calculateFinalCost());
        log.getCartLog().save(true);
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
}
