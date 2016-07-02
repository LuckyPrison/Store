package com.ulfric.store.shop.expiry;

import java.time.Instant;

public class ExpiryFactory {

    private static ExpiryFactory instance;

    public static ExpiryFactory getInstance()
    {
        if (instance == null)
        {
            instance = new ExpiryFactory();
        }
        return instance;
    }

    private ExpiryFactory()
    {

    }

    public Expiry neverExpire()
    {
        return new ExpiryNone();
    }

    public Expiry expireAfter(Instant instant)
    {
        return new ExpiryDate(instant);
    }

    public Expiry expireAfter(int uses)
    {
        return new ExpiryLimit(uses);
    }

    public Expiry expireAfter(int uses, int current)
    {
        return new ExpiryLimit(uses, current);
    }

}
