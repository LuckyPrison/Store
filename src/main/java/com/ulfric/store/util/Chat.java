package com.ulfric.store.util;

import org.bukkit.ChatColor;

public class Chat {

    private Chat()
    {

    }

    public static String color(String message)
    {
        return message == null ? null : ChatColor.translateAlternateColorCodes('&', message);
    }

}
