package com.ulfric.store.config;

import org.bukkit.configuration.file.YamlConfiguration;

import java.util.List;

public class ConfigPart {

    private final String section;
    private final String key;
    private final String fullKey;

    private YamlConfiguration config;

    public ConfigPart(YamlConfiguration config, String section, String key)
    {
        this.config = config;
        this.section = section;
        this.key = key;
        this.fullKey = section + '.' + key + '.';
    }

    public String getSection()
    {
        return section;
    }

    public String getKey()
    {
        return key;
    }

    public String getFullKey()
    {
        return fullKey;
    }

    public void set(String path, Object value)
    {
        config.set(path, value);
    }

    public Object get(String path)
    {
        return config.get(fullKey + path);
    }

    public Object get(String path, Object def)
    {
        return config.get(fullKey + path, def);
    }

    public int getInt(String path)
    {
        return config.getInt(fullKey + path);
    }

    public int getInt(String path, int def)
    {
        return config.getInt(fullKey + path, def);
    }

    public boolean getBoolean(String path)
    {
        return config.getBoolean(fullKey + path);
    }

    public boolean getBoolean(String path, boolean def)
    {
        return config.getBoolean(fullKey + path, def);
    }

    public double getDouble(String path)
    {
        return config.getDouble(fullKey + path);
    }

    public double getDouble(String path, double def)
    {
        return config.getDouble(fullKey + path, def);
    }

    public float getFloat(String path)
    {
        return config.getFloat(fullKey + path);
    }

    public float getDouble(String path, float def)
    {
        return config.getFloat(fullKey + path, def);
    }

    public String getString(String path)
    {
        return config.getString(fullKey + path);
    }

    public String getString(String path, String def)
    {
        return config.getString(fullKey + path, def);
    }

    public List<String> getStringList(String path)
    {
        return config.getStringList(fullKey + path);
    }

}
