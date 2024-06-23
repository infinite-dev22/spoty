package inc.nomard.spoty.utils;

import java.util.*;

public class OSUtil {
    private static OS os = null;

    public static OS getOs() {
        var osName = System.getProperty("os.name").toLowerCase();
        if (Objects.isNull(os)) {
            if (osName.contains("windows")) {
                os = OS.WINDOWS;
            } else if (osName.contains("linux")) {
                os = OS.LINUX;
            } else if (osName.contains("osx") || osName.contains("mac") || osName.contains("darwin")) {
                os = OS.MACOS;
            }
        }
        return os;
    }

    public enum OS {
        LINUX, MACOS, WINDOWS
    }
}
