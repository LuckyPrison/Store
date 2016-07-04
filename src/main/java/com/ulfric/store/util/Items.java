package com.ulfric.store.util;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class Items {

    public enum PaneColor {

        WHITE(0),
        ORANGE(1),
        MAGENTA(2),
        LIGHT_BLUE(3),
        YELLOW(4),
        LIME(5),
        PINK(6),
        GRAY(7),
        LIGHT_GRAY(8),
        CYAN(9),
        PURPLE(10),
        BLUE(11),
        BROWN(12),
        GREEN(13),
        RED(14),
        BLACK(15);

        private short durability;

        PaneColor(int durability)
        {
            this.durability = (short) durability;
        }
    }

    public static ItemStack stainedPane(PaneColor color)
    {
        return ItemBuilder.of(Material.STAINED_GLASS_PANE)
                .withDurability(color.durability)
                .withName(" ")
                .build();
    }

}
