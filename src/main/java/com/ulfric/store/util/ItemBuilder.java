package com.ulfric.store.util;

import com.google.common.collect.Lists;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;

public class ItemBuilder {

    public static ItemBuilder of(Material material)
    {
        return new ItemBuilder(material);
    }

    private Material material;
    private int amount = 1;
    private int durability = 0;

    private String name;
    private List<String> lore = Lists.newArrayList();
    private ItemFlag[] flags;

    public ItemBuilder(Material material)
    {
        this.material = material;
    }

    public ItemBuilder withAmount(int amount)
    {
        this.amount = amount;
        return this;
    }

    public ItemBuilder withDurability(int durability)
    {
        this.durability = durability;
        return this;
    }

    public ItemBuilder withName(String name)
    {
        this.name = name;
        return this;
    }

    public ItemBuilder withLore(String... lore)
    {
        this.lore.addAll(Arrays.asList(lore));
        return this;
    }

    public ItemBuilder withFlags(ItemFlag... flags)
    {
        this.flags = flags;
        return this;
    }

    public ItemStack build()
    {
        ItemStack item = new ItemStack(material);
        item.setAmount(amount);
        item.setDurability((short) durability);

        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        meta.setLore(lore);
        meta.addItemFlags(flags);
        item.setItemMeta(meta);

        return item;
    }

}
