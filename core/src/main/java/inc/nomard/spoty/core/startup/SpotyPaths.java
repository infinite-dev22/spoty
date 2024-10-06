package inc.nomard.spoty.core.startup;

import inc.nomard.spoty.utils.OSUtil;
import inc.nomard.spoty.utils.SpotyLogger;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Log4j2
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
