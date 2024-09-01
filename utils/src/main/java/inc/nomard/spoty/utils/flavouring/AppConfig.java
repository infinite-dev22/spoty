package inc.nomard.spoty.utils.flavouring;

import inc.nomard.spoty.utils.*;

import java.io.*;
import java.util.*;

import lombok.extern.java.*;

@Log
public class AppConfig {

    private static final String CONFIG_FILE = "config.properties";
    private static final String APP_VERSION_FILE = "version.properties";
    private static final String APP_HOST_FILE = "host.properties";
    private static final Properties properties = new Properties();

    public static AppFlavor getActiveFlavor() {
        try {
            properties.load(AppConfig.class.getClassLoader().getResourceAsStream(CONFIG_FILE));
            String flavorString = properties.getProperty("app.flavor");
            return AppFlavor.valueOf(flavorString);
        } catch (IOException e) {
            SpotyLogger.writeToFile(e, AppConfig.class);
            return AppFlavor.MVP;
        }
    }

    public static String getAppVersion() {
        try {
            properties.load(AppConfig.class.getClassLoader().getResourceAsStream(APP_VERSION_FILE));
            String versionString = properties.getProperty("app.version");
            return "v" + versionString;
        } catch (IOException e) {
            SpotyLogger.writeToFile(e, AppConfig.class);
            return "";
        }
    }

    public static String getVersionReleaseNotes() {
        try {
            properties.load(AppConfig.class.getClassLoader().getResourceAsStream(APP_VERSION_FILE));
            return properties.getProperty("app.release-notes");
        } catch (IOException e) {
            SpotyLogger.writeToFile(e, AppConfig.class);
            return "";
        }
    }

    public static String getAppHostUrl() {
        try {
            properties.load(AppConfig.class.getClassLoader().getResourceAsStream(APP_HOST_FILE));
            return properties.getProperty("app.host");
        } catch (IOException e) {
            SpotyLogger.writeToFile(e, AppConfig.class);
            return "http://localhost:8080";
        }
    }
}