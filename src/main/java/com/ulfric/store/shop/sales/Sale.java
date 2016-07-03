package com.ulfric.store.shop.sales;

import com.google.common.collect.Lists;
import com.ulfric.store.Store;
import com.ulfric.store.config.ConfigSerializable;
import com.ulfric.store.manage.SaleManager;
import com.ulfric.store.manage.StoreManager;
import com.ulfric.store.shop.StoreAppliable;
import org.bukkit.configuration.file.YamlConfiguration;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class Sale implements ConfigSerializable, Discountable {

    public static void serialize(Store store, Sale sale, YamlConfiguration config)
    {
        config.set("sales." + sale.name + ".type", sale.type);
        config.set("sales." + sale.name + ".magnitude", sale.magnitude);
        config.set("sales." + sale.name + ".start", sale.start.toString());
        config.set("sales." + sale.name + ".finish", sale.finish.toString());
        config.set(
                "sales." + sale.name + ".applications",
                sale.appliables.stream().map(StoreAppliable::getId).collect(Collectors.toList())
        );
    }

    public static Sale deserialize(Store store, YamlConfiguration config, String name)
    {
        Sale sale = new Sale(
                store,
                name,
                DiscountType.valueOf(config.getString("sales." + name + ".type")),
                config.getDouble("sales." + name + ".magnitude"),
                Instant.parse(config.getString("sales." + name + ".start")),
                Instant.parse(config.getString("sales." + name + ".finish"))
        );
        config.getIntegerList("sales." + name + ".applications").stream().map(id -> store.getManager(StoreManager.class).getById(id)).forEach(sale::applyFor);
        return sale;
    }

    private UUID saleUUID;

    private final Store store;

    private String name;

    private DiscountType type;
    private Double magnitude;

    private Instant start;
    private Instant finish;

    private List<StoreAppliable> appliables = Lists.newArrayList();

    public Sale(Store store, String name, DiscountType type, Double magnitude, Instant start, Instant finish)
    {
        this.store = store;
        this.name = name;
        this.type = type;
        this.magnitude = magnitude;
        this.start = start;
        this.finish = finish;
    }

    public void activate()
    {
        store.getManager(SaleManager.class).add(this, true);
    }

    public Sale applyFor(StoreAppliable appliable)
    {
        appliables.add(appliable);
        return this;
    }

    public Sale applyNotFor(StoreAppliable appliable)
    {
        appliables.remove(appliable);
        return this;
    }

    public boolean validFor(StoreAppliable appliable)
    {
        for (StoreAppliable poss : appliables)
        {
            if (appliable.appliableTo(poss))
            {
                return true;
            }
        }
        return false;
    }

}
