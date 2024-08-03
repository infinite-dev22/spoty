package inc.nomard.spoty.core;

import java.io.*;
import java.net.*;
import javafx.fxml.*;
import lombok.extern.java.*;

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