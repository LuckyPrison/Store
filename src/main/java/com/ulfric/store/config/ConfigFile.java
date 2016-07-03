package com.ulfric.store.config;

import com.google.common.collect.Lists;
import com.ulfric.store.Store;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ConfigFile {

    private final Store store;

    private YamlConfiguration config;
    private File file;
    private final Object lock = new Object();

    public ConfigFile(Store store, String file)
    {
        this(store, new File(store.getDataFolder(), file.replace(".yml", "") + ".yml"));
    }

    public ConfigFile(Store store, File file)
    {
        this.store = store;
        this.file = file;
        if (!file.exists())
        {
            try
            {
                System.out.println("Creating file " + file.toString());
                file.createNewFile();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        this.config = YamlConfiguration.loadConfiguration(file);
    }

    public YamlConfiguration getConfig()
    {
        return config;
    }

    public File getFile()
    {
        return file;
    }

    public void save(boolean async)
    {
        Runnable save = this::saveFile;
        if (async)
        {
            store.getServer().getScheduler().runTaskAsynchronously(store, save);
        }
        else
        {
            save.run();
        }
    }

    public Stream<ConfigPart> getParts(String section)
    {
        if (!config.contains(section))
        {
            config.createSection(section);
        }
        if (!config.isConfigurationSection(section))
        {
            return Stream.empty();
        }
        List<ConfigPart> parts = Lists.newArrayList();
        parts.addAll(
                config.getConfigurationSection(section).getKeys(false)
                        .stream()
                        .map(key -> new ConfigPart(config, section, key))
                        .collect(Collectors.toList())
        );
        return parts.stream();
    }

    private void saveFile()
    {
        synchronized (lock)
        {
            try
            {
                config.save(file);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

}
