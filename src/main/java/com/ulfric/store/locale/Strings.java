package com.ulfric.store.locale;

public class Strings {

    public static String format(String format, Object[] objects)
    {
        if (format == null) return "";
        if (objects == null) return format;

        int length = objects.length;
        if (length == 0) return format;

        String reformat = Strings.format(0, format, objects[0]);
        if (length == 1) return reformat;

        for (int x = 1; x < length; x++)
        {
            reformat = Strings.format(x, reformat, objects[x]);
        }

        return reformat;
    }

    public static String format(String format, Object value)
    {
        if (format == null) return "";

        return format.replace("{0}", String.valueOf(value));
    }

    public static String format(int position, String format, Object value)
    {
        if (format == null) return "";

        return format.replace("{#}".replace("#", String.valueOf(position)), String.valueOf(value));
    }

    public static String format(String format, Object value, Object... objects)
    {
        if (format == null) return "";

        String reformat = Strings.format(format, value);
        if (objects == null) return reformat;

        int length = objects.length;
        if (length == 0) return reformat;

        for (int x = 0; x < length; x++)
        {
            reformat = Strings.format(x + 1, reformat, objects[x]);
        }

        return reformat;
    }

}
