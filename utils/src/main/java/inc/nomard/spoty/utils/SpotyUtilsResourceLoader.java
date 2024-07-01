package inc.nomard.spoty.utils;

import lombok.extern.java.Log;

import java.io.InputStream;
import java.net.URL;

@Log
public class SpotyUtilsResourceLoader {
    public SpotyUtilsResourceLoader() {
    }

    public static URL loadURL(String path) {
        return SpotyUtilsResourceLoader.class.getResource(path);
    }

    public static String load(String path) {
        return loadURL(path).toString();
    }

    public static InputStream loadStream(String name) {
        return SpotyUtilsResourceLoader.class.getResourceAsStream(name);
    }
}