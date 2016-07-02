package com.ulfric.store.config;

import org.bukkit.configuration.file.YamlConfiguration;

import java.util.List;

public class ConfigPart {

    private final String key;
    private YamlConfiguration config;

    public ConfigPart(YamlConfiguration config, String key)
    {
        this.config = config;
        this.key = key + ".";
    }

    public Object get(String path)
    {
        return config.get(key + path);
    }

    public Object get(String path, Object def)
    {
        return config.get(key + path, def);
    }

    public int getInt(String path)
    {
        return config.getInt(key + path);
    }

    public int getInt(String path, int def)
    {
        return config.getInt(key + path, def);
    }

    public boolean getBoolean(String path)
    {
        return config.getBoolean(key + path);
    }

    public boolean getBoolean(String path, boolean def)
    {
        return config.getBoolean(key + path, def);
    }

    public double getDouble(String path)
    {
        return config.getDouble(key + path);
    }

    public double getDouble(String path, double def)
    {
        return config.getDouble(key + path, def);
    }

    public float getFloat(String path)
    {
        return config.getFloat(key + path);
    }

    public float getDouble(String path, float def)
    {
        return config.getFloat(key + path, def);
    }

    public String getString(String path)
    {
        return config.getString(key + path);
    }

    public String getString(String path, String def)
    {
        return config.getString(key + path, def);
    }

    public List<String> getStringList(String path)
    {
        return config.getStringList(key + path);
    }

}
