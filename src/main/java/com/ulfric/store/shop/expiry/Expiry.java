package com.ulfric.store.shop.expiry;

public interface Expiry {

    boolean expired();

    default void onUse() {}

}
