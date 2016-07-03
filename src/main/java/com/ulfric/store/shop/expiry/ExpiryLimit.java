package com.ulfric.store.shop.expiry;

public class ExpiryLimit implements Expiry {

    private int limit;
    private int current = 0;

    public ExpiryLimit(int limit)
    {
        this(limit, 0);
    }

    public ExpiryLimit(int limit, int current)
    {
        this.limit = limit;
        this.current = current;
    }

    public void onUse()
    {
        current++;
    }

    @Override
    public boolean expired() {
        return current >= limit;
    }

    public int getLimit()
    {
        return limit;
    }

    public int getCurrent()
    {
        return current;
    }

}
