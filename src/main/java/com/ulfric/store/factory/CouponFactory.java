package com.ulfric.store.factory;

import com.ulfric.store.Store;
import com.ulfric.store.shop.expiry.Expiry;
import com.ulfric.store.shop.expiry.ExpiryFactory;
import com.ulfric.store.shop.sales.Coupon;
import com.ulfric.store.shop.sales.CouponType;
import com.ulfric.store.shop.sales.DiscountType;

public class CouponFactory {

    private static CouponFactory instance;

    public static CouponFactory getInstance()
    {
        return instance;
    }

    private final Store store;

    public CouponFactory(Store store)
    {
        if (instance != null)
        {
            throw new IllegalStateException("Already instantiated!");
        }
        this.store = store;
        instance = this;
    }

    public CouponBuilder buildCoupon(String code)
    {
        return new CouponBuilder(code);
    }

    public class CouponBuilder {

        private String code;
        private CouponType couponType = CouponType.CART;
        private DiscountType discountType = DiscountType.AMOUNT;
        private Double magnitude = 0.0;
        private Expiry expiry = ExpiryFactory.getInstance().neverExpire();
        private Double minValue = 0.0;

        public CouponBuilder(String code)
        {
            this.code = code;
        }

        public CouponBuilder withCouponType(CouponType type)
        {
            this.couponType = type;
            return this;
        }

        public CouponBuilder withDiscountType(DiscountType type)
        {
            this.discountType = type;
            return this;
        }

        public CouponBuilder withMagnitude(Double magnitude)
        {
            this.magnitude = magnitude;
            return this;
        }

        public CouponBuilder withExpiry(Expiry expiry)
        {
            this.expiry = expiry;
            return this;
        }

        public CouponBuilder withMinValue(Double minValue)
        {
            this.minValue = minValue;
            return this;
        }

        public Coupon build()
        {
            return new Coupon(
                    store,
                    code,
                    couponType,
                    discountType,
                    magnitude,
                    expiry,
                    minValue
            );
        }

    }

}
