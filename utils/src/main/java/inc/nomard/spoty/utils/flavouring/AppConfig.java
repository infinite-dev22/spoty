package inc.nomard.spoty.utils.flavouring;

import inc.nomard.spoty.utils.*;
import lombok.extern.java.Log;

import java.io.IOException;
import java.util.Properties;

import javafx.util.Duration;
@Log
public class AppConfig {

    private static final String CONFIG_FILE = "config.properties";
    private static final String APP_VERSION_FILE = "version.properties";
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
    };

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
}