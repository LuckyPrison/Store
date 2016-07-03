package com.ulfric.store.shop.sales;

import com.google.common.collect.Lists;
import com.ulfric.store.Store;
import com.ulfric.store.config.ConfigSerializable;
import com.ulfric.store.manage.StoreManager;
import com.ulfric.store.shop.StoreAppliable;
import com.ulfric.store.shop.expiry.Expiry;
import com.ulfric.store.shop.expiry.ExpiryFactory;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.List;
import java.util.stream.Collectors;

public class Coupon implements ConfigSerializable {

    public static void serialize(Store store, Coupon coupon, YamlConfiguration config)
    {
        config.set("coupons." + coupon.code + ".magnitude", coupon.magnitude);
        config.set("coupons." + coupon.code + ".type.coupon", coupon.couponType.toString());
        config.set("coupons." + coupon.code + ".type.discount", coupon.discountType.toString());
        config.set("coupons." + coupon.code + ".min-value", coupon.minValue);
        config.set(
                "coupons." + coupon.code + ".appliables",
                coupon.appliables.stream().map(StoreAppliable::getId).collect(Collectors.toList())
        );
        ExpiryFactory.getInstance().serialize(store, coupon.expiry, config, "coupons." + coupon.code);
    }

    public static Coupon deserialize(Store store, YamlConfiguration config, String code)
    {
        Coupon coupon = new Coupon(
                code,
                CouponType.valueOf(config.getString("coupons." + code + ".type.coupon")),
                DiscountType.valueOf(config.getString("coupons." + code + ".type.discount")),
                config.getDouble("coupons." + code + ".magnitude"),
                ExpiryFactory.getInstance().deserialize(store, config, "coupons." + code),
                config.getDouble("coupons." + code + ".min-value")
        );
        config.getIntegerList("coupons." + code + ".appliables").forEach(id ->
        {
            coupon.applyFor(store.getManager(StoreManager.class).getById(id));
        });
        return coupon;
    }

    private String code;
    private Expiry expiry;

    private Double magnitude;
    private CouponType couponType;
    private DiscountType discountType;

    private Double minValue;

    private List<StoreAppliable> appliables = Lists.newArrayList();

    public Coupon(String code, CouponType couponType, DiscountType discountType, Double magnitude, Expiry expiry, Double minValue)
    {
        this.code = code;
        this.couponType = couponType;
        this.discountType = discountType;
        this.magnitude = magnitude;
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

    public boolean appliesFor(StoreAppliable appliable)
    {
        return appliables.stream().filter(appliable::appliableTo).findAny().isPresent();
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

    public Double getMagnitude()
    {
        return magnitude;
    }

    public CouponType getCouponType()
    {
        return couponType;
    }

    public DiscountType getDiscountType()
    {
        return discountType;
    }
}
