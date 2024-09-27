package inc.nomard.spoty.utils;

import lombok.extern.java.Log;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

@Log
public class SpotyLogger {
    public static <T> void writeToFile(Throwable throwable, Class<T> currentClass) {
        Logger logger = Logger.getLogger(currentClass.getName());
        var logFilePath = "";

        switch (OSUtil.getOs()) {
            case LINUX, MACOS -> logFilePath += Paths.get(
                    System.getProperty("user.home") + "/.config/OpenSaleERP/sys-log-data/logs/stack/application.log");
            case WINDOWS -> logFilePath += Paths.get(System.getProperty("user.home")
                    + "\\AppData\\Local\\OpenSaleERP\\sys-log-data\\logs\\stack\\application.log");
        }

        try {
            FileHandler fileHandler = new FileHandler(logFilePath, true);
            fileHandler.setFormatter(new SimpleFormatter());
            logger.addHandler(fileHandler);
            logger.log(
                    Level.ALL,
                    throwable.getMessage()
                            + "\n"
                            + Arrays.toString(throwable.getStackTrace())
                            + "\n\n");
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Failed to initialize logger", e);
        }
        throwable.printStackTrace();
    }
}
