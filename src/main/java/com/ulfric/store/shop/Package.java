package com.ulfric.store.shop;

import com.google.common.collect.Lists;
import com.ulfric.store.Store;
import com.ulfric.store.config.ConfigSerializable;
import com.ulfric.store.execute.StoreCommand;
import com.ulfric.store.manage.ConfigManager;
import com.ulfric.store.manage.StoreManager;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

public class Package implements StoreAppliable, ConfigSerializable {

    public static void serialize(Store store, Package pack, YamlConfiguration config)
    {
        config.set("packages." + pack.getId() + ".title", pack.title);
        config.set("packages." + pack.getId() + ".description", pack.getDescription());
        config.set("packages." + pack.getId() + ".parent", pack.parent == null ? -1 : pack.parent.getId());
        config.set("packages." + pack.getId() + ".price", pack.price);
        config.set("packages." + pack.getId() + ".permission", pack.permission);
        Icon.serialize(store, config, "packages." + pack.getId(), pack.getIcon());
        pack.commands.forEach(command -> StoreCommand.serialize(store, command, config, "packages." + pack.getId()));
    }

    public static Package deserialize(Store store, YamlConfiguration config, int id)
    {
        StoreManager storeManager = store.getManager(StoreManager.class);
        StoreAppliable appliable = storeManager.getById(config.getInt("packages." + id + ".parent"));
        Package pack = new Package(
                store,
                id,
                config.getString("packages." + id + ".title"),
                config.getString("packages." + id + ".description"),
                appliable == null ? null : (Category) appliable,
                config.getDouble("packages." + id + ".price"),
                config.getString("packages." + id + ".permission", null),
                Icon.deserialize(store, config, "packages." + id)
        );
        if (config.contains("packages." + id + ".commands"))
        {
            config.getConfigurationSection("packages." + id + ".commands").getKeys(false).forEach(key ->
            {
                UUID uuid = UUID.fromString(key);
                StoreCommand command = StoreCommand.deserialize(store, config, uuid, "packages." + id);
                pack.withCommand(command);
            });
        }
        return pack;
    }

    private final Store store;
    private final int id;
    private String title;
    private String description;
    private Category parent;

    private String permission;

    private Double price;

    private Icon icon;

    private List<StoreCommand> commands = Lists.newArrayList();

    public Package(Store store, int id, String title, String description, Category parent, Double price, String permission, Icon icon)
    {
        this.store = store;
        this.id = id;
        this.title = title;
        this.description = description;
        this.parent = parent;
        this.permission = permission;
        this.icon = icon;
        store.getManager(StoreManager.class).addItem(this);
        if (parent != null && !parent.getPackages().stream().filter(pack -> pack.getId() == this.id).findAny().isPresent())
        {
            parent.withPackage(this.id);
        }
        this.price = price;
    }

    public Package withCommand(StoreCommand command)
    {
        commands.add(command);
        return this;
    }

    public Package withoutCommand(StoreCommand command)
    {
        commands.remove(command);
        return this;
    }

    @Override
    public int getId()
    {
        return id;
    }

    @Override
    public void save()
    {
        store.getManager(ConfigManager.class).savePackage(this);
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Category getParent()
    {
        return parent;
    }

    public Double getPrice() {
        return price;
    }

    public List<StoreCommand> getCommands() {
        return commands;
    }

    public String getPermission()
    {
        return permission;
    }

    public boolean hasPermission(Player player)
    {
        return permission == null || player.hasPermission(permission);
    }

    public Icon getIcon()
    {
        return icon;
    }
}
