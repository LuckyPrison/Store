package com.ulfric.store.shop.expiry;

import java.time.Instant;

public class ExpiryDate implements Expiry {

    private Instant expiry;

    public ExpiryDate(Instant expiry)
    {
        this.expiry = expiry;
    }

    @Override
    public boolean expired() {
        return Instant.now().isAfter(expiry);
    }

    public Instant getExpiry()
    {
        return expiry;
    }

}
