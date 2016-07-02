package com.ulfric.store.shop.expiry;

import java.util.Date;

public class ExpiryDate implements Expiry {

    private Date expiryDate;

    public ExpiryDate(Date expiryDate)
    {
        this.expiryDate = expiryDate;
    }

    @Override
    public boolean expired() {
        return new Date().after(expiryDate);
    }
}
