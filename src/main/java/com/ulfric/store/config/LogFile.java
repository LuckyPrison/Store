package com.ulfric.store.config;

import com.ulfric.store.Store;
import com.ulfric.store.util.StoreUtils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class LogFile {

    private final Store store;
    private YamlConfiguration config;
    private File file;
    private final Object lock = new Object();

    public LogFile(Store store, String name)
    {
        this(
                store,
                new File(
                        store.getDataFolder() + File.separator + "logs",
                        name.replace(".yml", "") + "_" + StoreUtils.getDateFormat() + ".yml"
                )
        );
    }

    public LogFile(Store store, File file)
    {
        this.store = store;
        if (!file.exists())
        {
            try
            {
                file.createNewFile();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        this.file = file;
        this.config = YamlConfiguration.loadConfiguration(file);
    }

    public YamlConfiguration getConfig()
    {
        return config;
    }

    public void save(boolean async)
    {
        Runnable save = this::saveFile;
        if (async)
        {
            Bukkit.getScheduler().runTaskAsynchronously(store, save);
        }
        else
        {
            save.run();
        }
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
