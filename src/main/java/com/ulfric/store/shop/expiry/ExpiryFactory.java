package com.ulfric.store.shop.expiry;

import com.ulfric.store.Store;
import org.bukkit.configuration.file.YamlConfiguration;

import java.time.Instant;

public class ExpiryFactory {

    private static ExpiryFactory instance;

    public static ExpiryFactory getInstance()
    {
        if (instance == null)
        {
            instance = new ExpiryFactory();
        }
        return instance;
    }

    private ExpiryFactory()
    {

    }

    public void serialize(Store store, Expiry expiry, YamlConfiguration config, String section)
    {
        if (expiry instanceof ExpiryNone)
        {
            config.set(section + ".expiry.type", "none");
        }
        if (expiry instanceof ExpiryDate)
        {
            config.set(section + ".expiry.type", "date");
            config.set(section + ".expiry.value", ((ExpiryDate) expiry).getExpiry().toString());
        }
        if (expiry instanceof ExpiryLimit)
        {
            config.set(section + ".expiry.type", "limit");
            config.set(section + ".expiry.value.limit", ((ExpiryLimit) expiry).getLimit());
            config.set(section + ".expiry.value.current", ((ExpiryLimit) expiry).getCurrent());
        }
    }

    public Expiry deserialize(Store store, YamlConfiguration config, String section)
    {
        String type = config.getString(section + ".expiry.type").toLowerCase();
        if (type.equals("none"))
        {
            return new ExpiryNone();
        }
        if (type.equals("date"))
        {
            return new ExpiryDate(Instant.parse(config.getString(section + ".expiry.value")));
        }
        if (type.equals("limit"))
        {
            return new ExpiryLimit(
                    config.getInt(section + ".expiry.limit"),
                    config.getInt(section + ".expiry.current", 0)
            );
        }
        return new ExpiryLimit(-1, -1);
    }

    public Expiry neverExpire()
    {
        return new ExpiryNone();
    }

    public Expiry expireAfter(Instant instant)
    {
        return new ExpiryDate(instant);
    }

    public Expiry expireAfter(int uses)
    {
        return new ExpiryLimit(uses);
    }

    public Expiry expireAfter(int uses, int current)
    {
        return new ExpiryLimit(uses, current);
    }

}
