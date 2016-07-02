package com.ulfric.store.shop.expiry;

public class ExpiryNone implements Expiry {

    @Override
    public boolean expired() {
        return false;
    }

}
