package com.ulfric.store.locale;

import com.ulfric.store.Store;
import com.ulfric.store.util.Chat;
import org.apache.commons.collections4.map.CaseInsensitiveMap;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.Map;

public class Locale {

    private static ILocale IMPL;

    public static final Locale ENGLISH_US = new Locale("en_US");

    public static void load(Store store)
    {
        store.saveResource("locale" + File.separator + "en_US.yml", false);

        IMPL = new ILocale() {

            private final Map<String, Locale> locales = new CaseInsensitiveMap<>();

            {
                String ignore = "locale.yml";

                File folder = new File(store.getDataFolder() + File.separator + "locale");
                folder.mkdirs();

                if (folder.listFiles() == null)
                {
                    throw new RuntimeException("Folder isn't a folder...?");
                }

                for (File file : folder.listFiles())
                {
                    YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

                    String name = config.getName();

                    if (name == null) continue;

                    if (name.equals(ignore))
                    {
                        continue;
                    }

                    String nonYamlName = name.replace(".yml", "");

                    Locale locale;

                    if ("en_US".equals(nonYamlName))
                    {
                        locale = Locale.ENGLISH_US;
                    }
                    else
                    {
                        locale = new Locale(nonYamlName);

                        this.locales.put(nonYamlName, locale);
                    }

                    config.getKeys(false).forEach(key -> locale.registerMessage(key, config.getString(key)));
                }
            }

            @Override
            public Locale getLocale(String code)
            {
                Locale locale = this.locales.get(code);
                if (locale == null)
                {
                    locale = Locale.ENGLISH_US;
                }
                return locale;
            }
        };
    }

    private final String code;
    private final CaseInsensitiveMap<String, String> messages;

    public Locale(Locale locale)
    {
        this.code = locale.code;
        this.messages = locale.messages.clone();
    }

    Locale(String code)
    {
        this.code = code;
        this.messages = new CaseInsensitiveMap<>();
    }

    public static Locale getLocale(String code)
    {
        return Locale.IMPL.getLocale(code);
    }

    public String getCode()
    {
        return this.code;
    }

    public void registerMessage(String key, String message)
    {
        this.messages.put(key, Chat.color(message));
    }

    public String getRawMessage(String key)
    {
        String message = this.messages.get(key);

        if (message == null)
        {
            message = Locale.ENGLISH_US.messages.get(key);
            if (message == null) return key;
        }

        return message;
    }

    public String getFormattedMessage(String key, Object... objects)
    {
        return Strings.format(this.getRawMessage(key), objects);
    }

    @Override
    public boolean equals(Object object)
    {
        return object == this || object instanceof Locale && ((Locale) object).code.equals(this.code);
    }

    @Override
    public String toString()
    {
        return this.code;
    }

    void clear()
    {
        this.messages.clear();
    }

    protected interface ILocale {
        Locale getLocale(String code);
    }

}
