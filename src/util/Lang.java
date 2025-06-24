package util;

import java.util.Locale;
import java.util.ResourceBundle;

public class Lang {
    public static ResourceBundle bundle;

    public static void setLocale(String lang) {
        Locale locale = new Locale(lang);
        try {
            bundle = ResourceBundle.getBundle("resources.messages", locale);
        } catch (Exception e) {
            System.err.println("Gagal load bundle: " + e.getMessage());
            bundle = ResourceBundle.getBundle("resources.messages", new Locale("id")); // fallback
        }
    }

    public static String get(String key) {
        if (bundle == null) {
            setLocale("id"); // default
        }
        return bundle.getString(key);
    }
}
