package inc.nomard.spoty.utils.flavouring;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Properties;

@Slf4j
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
            log.error(e.getMessage());
            return AppFlavor.PROD;
        }
    }

    public static String getAppVersion() {
        try {
            properties.load(AppConfig.class.getClassLoader().getResourceAsStream(APP_VERSION_FILE));
            String versionString = properties.getProperty("app.version");
            return "v" + versionString;
        } catch (IOException e) {
            log.error(e.getMessage());
            return "";
        }
    }

    public static String getVersionReleaseNotes() {
        try {
            properties.load(AppConfig.class.getClassLoader().getResourceAsStream(APP_VERSION_FILE));
            return properties.getProperty("app.release-notes");
        } catch (IOException e) {
            log.error(e.getMessage());
            return "";
        }
    }

    public static String getAppHostUrl() {
        try {
            properties.load(AppConfig.class.getClassLoader().getResourceAsStream(APP_HOST_FILE));
            return properties.getProperty("app.host");
        } catch (IOException e) {
            log.error(e.getMessage());
            return "http://localhost:8080";
        }
    }
}