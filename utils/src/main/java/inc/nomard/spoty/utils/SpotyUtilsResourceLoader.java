package inc.nomard.spoty.utils;

import lombok.extern.log4j.Log4j2;

import java.io.InputStream;
import java.net.URL;

@Log4j2
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