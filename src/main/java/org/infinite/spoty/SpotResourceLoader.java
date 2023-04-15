package org.infinite.spoty;

import java.io.InputStream;
import java.net.URL;

public class SpotResourceLoader {
    public SpotResourceLoader() {
    }

    public static URL loadURL(String path) {
        return SpotResourceLoader.class.getResource(path);
    }

    public static String load(String path) {
        return loadURL(path).toString();
    }

    public static InputStream loadStream(String name) {
        return SpotResourceLoader.class.getResourceAsStream(name);
    }

}
