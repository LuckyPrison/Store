package com.ulfric.store.util;

import com.google.common.collect.Lists;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.Arrays;
import java.util.List;

public class ItemBuilder {

    public static ItemBuilder of(Material material)
    {
        return new ItemBuilder(material);
    }

    public static ItemBuilder of(ItemStack item, boolean meta)
    {
        ItemBuilder builder = new ItemBuilder(item.getType())
                .withAmount(item.getAmount())
                .withDurability(item.getDurability());
        if (meta)
        {
            if (item.hasItemMeta())
            {
                if (item.getItemMeta().hasDisplayName())
                {
                    builder.withName(item.getItemMeta().getDisplayName());
                }
                if (item.getItemMeta().hasLore())
                {
                    builder.withLore(item.getItemMeta().getLore());
                }
                item.getItemMeta().getItemFlags().forEach(builder::withFlags);
            }
        }
        return builder;
    }

    private Material material;
    private int amount = 1;
    private int durability = 0;

    private String name;
    private List<String> lore = Lists.newArrayList();
    private ItemFlag[] flags;

    // Optionals

    private String owner;

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

    public ItemBuilder withDurability(Items.ItemColor color)
    {
        this.durability = color.getDurability();
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

    public ItemBuilder withLore(List<String> lore)
    {
        this.lore.addAll(lore);
        return this;
    }

    public ItemBuilder withOwner(String owner)
    {
        this.owner = owner;
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

        // Optionals

        if (owner != null)
        {
            SkullMeta skullMeta = (SkullMeta) item.getItemMeta();
            skullMeta.setOwner(owner);
            item.setItemMeta(skullMeta);
        }

        return item;
    }

}
