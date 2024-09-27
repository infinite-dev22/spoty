package inc.nomard.spoty.core;

import javafx.fxml.FXMLLoader;
import lombok.extern.java.Log;

import java.io.InputStream;
import java.net.URL;

@Log
public class SpotyCoreResourceLoader {
    public SpotyCoreResourceLoader() {
    }

    public static FXMLLoader fxmlLoader(String path) {
        return new FXMLLoader(loadURL(path));
    }

    public static URL loadURL(String path) {
        return SpotyCoreResourceLoader.class.getResource(path);
    }

    public static String load(String path) {
        return loadURL(path).toString();
    }

    public static InputStream loadStream(String name) {
        return SpotyCoreResourceLoader.class.getResourceAsStream(name);
    }
}