package org.infinite.spotmarkpos.loader;

import java.io.InputStream;
import java.net.URL;

public class SpotMarkResourceLoader {
    public SpotMarkResourceLoader() {
    }

    public static URL loadURL(String path) {
        return SpotMarkResourceLoader.class.getResource(path);
    }

    public static String load(String path) {
        return loadURL(path).toString();
    }

    public static InputStream loadStream(String name) {
        return SpotMarkResourceLoader.class.getResourceAsStream(name);
    }

}
