package com.ulfric.store.util;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class StoreUtils {

    private StoreUtils() {}

    public static double getMultiplierFromPercentage(double percentage)
    {
        return ( ( ( 50 - percentage ) * 2 ) + percentage ) / 100;
    }

    public static String getDateFormat()
    {
        Instant timestamp = Instant.now();
        LocalDateTime date = LocalDateTime.ofInstant(timestamp, ZoneId.systemDefault());
        return String.format("%d.%d.%d", date.getYear(), date.getMonthValue(), date.getDayOfMonth());
    }

    public static String readableTimestamp(Instant timestamp)
    {
        LocalDateTime date = LocalDateTime.ofInstant(timestamp, ZoneId.systemDefault());
        return String.format(
                "%d.%d.%d-%d:%d:%d", date.getYear(), date.getMonthValue(), date.getDayOfMonth(),
                date.getHour(), date.getMinute(), date.getSecond()
        );

    }

}
