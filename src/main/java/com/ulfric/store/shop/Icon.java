package com.ulfric.store.shop;

import com.ulfric.store.Store;
import com.ulfric.store.config.ConfigSerializable;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.stream.Collectors;

public class Icon implements ConfigSerializable {

    public static void serialize(Store store, YamlConfiguration config, String section, Icon icon)
    {
        ItemStack item = icon.getIcon();
        config.set(section + ".icon.material", item.getType().toString());
        config.set(section + ".icon.amount", item.getAmount());
        config.set(section + ".icon.durability", item.getDurability());
        ItemMeta meta = item.getItemMeta();
        config.set(section + ".icon.meta.name", meta.getDisplayName());
        config.set(section + ".icon.meta.lore", meta.getLore());
        config.set(section + ".icon.meta.flags", meta.getItemFlags().stream().map(Enum::toString).collect(Collectors.toList()));
    }

    public static Icon deserialize(Store store, YamlConfiguration config, String section)
    {
        ItemStack item = new ItemStack(
                Material.valueOf(config.getString(section + ".icon.material")),
                config.getInt(section + ".icon.amount"),
                (short) config.getInt(section + ".icon.durability")
        );

        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(config.getString(section + ".icon.meta.name"));
        meta.setLore(config.getStringList(section + ".icon.meta.lore"));

        List<ItemFlag> flags = config.getStringList(section + ".icon.meta.flags")
                .stream()
                .map(ItemFlag::valueOf)
                .collect(Collectors.toList());
        ItemFlag[] flagArray = new ItemFlag[flags.size()];
        flagArray = flags.toArray(flagArray);
        meta.addItemFlags(flagArray);


        item.setItemMeta(meta);

        return new Icon(item);
    }

    private ItemStack icon;

    public Icon(ItemStack icon)
    {
        this.icon = icon;
    }

    public ItemStack getIcon()
    {
        return icon.clone();
    }

    @Override
    public String toString()
    {
        return String.format("[Icon,icon=%s,meta=%s]", icon.toString(), icon.getItemMeta() == null ? "null" : icon.getItemMeta().toString());
    }

}
