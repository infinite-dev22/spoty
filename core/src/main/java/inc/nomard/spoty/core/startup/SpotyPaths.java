package inc.nomard.spoty.core.startup;

import inc.nomard.spoty.utils.*;
import java.io.*;
import java.nio.file.*;
import lombok.extern.java.*;

@Log
public class SpotyPaths {
    public static void createPaths() {
        // Create DB Location path on system.
        Path logPath = null;

        switch (OSUtil.getOs()) {
            case LINUX, MACOS -> logPath = Paths.get(
                    System.getProperty("user.home") + "/.config/OpenSaleERP/sys-log-data/logs/stack");
            case WINDOWS -> logPath = Paths.get(System.getProperty("user.home")
                    + "\\AppData\\Local\\OpenSaleERP\\sys-log-data\\logs\\stack");
        }

        try {
            Files.createDirectories(logPath);
        } catch (IOException e) {
            SpotyLogger.writeToFile(e, SpotyPaths.class);
        }
    }
}
