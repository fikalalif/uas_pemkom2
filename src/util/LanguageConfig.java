package util;

import java.io.*;
import java.util.Properties;

public class LanguageConfig {
    private static final String CONFIG_FILE = "language.properties";

    public static void saveLanguage(String lang) {
        Properties prop = new Properties();
        prop.setProperty("language", lang);
        try (FileOutputStream out = new FileOutputStream(CONFIG_FILE)) {
            prop.store(out, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String loadLanguage() {
        Properties prop = new Properties();
        try (FileInputStream in = new FileInputStream(CONFIG_FILE)) {
            prop.load(in);
            return prop.getProperty("language", "id"); // default "id"
        } catch (IOException e) {
            return "id"; // default jika file tidak ditemukan
        }
    }
}
