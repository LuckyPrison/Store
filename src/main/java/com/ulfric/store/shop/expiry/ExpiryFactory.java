package com.ulfric.store.shop.expiry;

import java.util.Date;

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

    public Expiry expireAfter(Date date)
    {
        return new ExpiryDate(date);
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
