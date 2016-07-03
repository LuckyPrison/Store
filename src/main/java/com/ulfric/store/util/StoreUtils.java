package com.ulfric.store.util;

public class StoreUtils {

    private StoreUtils() {}

    public static double getMultiplierFromPercentage(double percentage)
    {
        return ( ( ( 50 - percentage ) * 2 ) + percentage ) / 100;
    }

}
