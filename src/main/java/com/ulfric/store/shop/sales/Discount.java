package com.ulfric.store.shop.sales;

public class Discount {

    private double packageTotal;
    private double discountAmount = 0.0;

    public Discount(double price)
    {
        this.packageTotal = price;
    }

    public Discount withCoupon(Coupon coupon)
    {
        double newDiscount = 0.0;

        if (coupon.getDiscountType() == DiscountType.AMOUNT)
        {
            newDiscount = coupon.getMagnitude();
        }
        else if (coupon.getDiscountType() == DiscountType.PERCENTAGE)
        {
            newDiscount = (coupon.getMagnitude() / 100) * packageTotal;
        }

        this.discountAmount = Math.min(packageTotal, discountAmount + newDiscount);

        return this;
    }

    public double getDiscountAmount()
    {
        return discountAmount;
    }

    public double getCalculatedPrice()
    {
        return packageTotal - discountAmount;
    }

}
