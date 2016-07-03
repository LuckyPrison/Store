package com.ulfric.store.factory;

import com.ulfric.store.Store;
import com.ulfric.store.shop.sales.DiscountType;
import com.ulfric.store.shop.sales.Sale;

import java.time.Instant;

public class SaleFactory {

    private static SaleFactory instance;

    public static SaleFactory getInstance()
    {
        return instance;
    }

    private final Store store;

    public SaleFactory(Store store)
    {
        if (instance != null)
        {
            throw new IllegalStateException("Already instantiated!");
        }
        this.store = store;
        instance = this;
    }

    public SaleBuilder buildSale(String name)
    {
        return new SaleBuilder(name);
    }

    public class SaleBuilder {

        private String name;
        private DiscountType discountType = DiscountType.AMOUNT;
        private Double magnitude = 0.0;
        private Instant start = Instant.now();
        private Instant finish = Instant.MAX;
        private boolean global = true;

        public SaleBuilder(String name)
        {
            this.name = name;
        }

        public SaleBuilder withDiscountType(DiscountType type)
        {
            this.discountType = type;
            return this;
        }

        public SaleBuilder withMagnitude(Double magnitude)
        {
            this.magnitude = magnitude;
            return this;
        }

        public SaleBuilder withStart(Instant start)
        {
            this.start = start;
            return this;
        }

        public SaleBuilder withFinish(Instant finish)
        {
            this.finish = finish;
            return this;
        }

        public SaleBuilder withGlobal(boolean global)
        {
            this.global = global;
            return this;
        }

        public Sale build()
        {
            return new Sale(
                    store,
                    name,
                    discountType,
                    magnitude,
                    start,
                    finish,
                    global
            );
        }

    }

}
