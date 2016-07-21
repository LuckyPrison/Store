package com.ulfric.store.shop.sales;

import java.util.ArrayList;
import java.util.List;

public class Discount {

    private double packageTotal;
    private double discountAmount = 0.0;

    private int quantity;
    private int players;

    private List<Coupon> coupons = new ArrayList<>();

    public Discount(double price, int quantity, int players)
    {
        this.packageTotal = price;
        this.quantity = quantity;
        this.players = players;
    }

    public Discount withCoupon(Coupon coupon)
    {
        coupons.add(coupon);

        double newDiscount = 0.0;

        if (coupon.getDiscountType() == DiscountType.AMOUNT)
        {
            newDiscount = coupon.getMagnitude();
        }
        else if (coupon.getDiscountType() == DiscountType.PERCENTAGE)
        {
            newDiscount = (coupon.getMagnitude() / 100) * packageTotal;
        }

        this.discountAmount = Math.min(packageTotal * quantity * players, discountAmount + newDiscount);

        return this;
    }

    public List<Coupon> getCoupons()
    {
        return new ArrayList<>(coupons);
    }

    public double getDiscountAmount()
    {
        return discountAmount;
    }

    public double getCalculatedPrice()
    {
        return (packageTotal * players * quantity) - discountAmount;
    }

}
