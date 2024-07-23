package inc.nomard.spoty.utils.flavouring;

import lombok.extern.java.Log;

import java.io.IOException;
import java.util.Properties;

import javafx.util.Duration;
@Log
public class AppConfig {

    private static final String CONFIG_FILE = "config.properties";
    private static final Properties properties = new Properties();

    public static AppFlavor getActiveFlavor() {
        try {
            properties.load(AppConfig.class.getClassLoader().getResourceAsStream(CONFIG_FILE));
            String flavorString = properties.getProperty("app.flavor");
            return AppFlavor.valueOf(flavorString);
        } catch (IOException e) {
            // Handle error loading configuration file (e.g., use default flavor)
            e.printStackTrace();
            return AppFlavor.MVP; // Or any default flavor
        }
    }
}