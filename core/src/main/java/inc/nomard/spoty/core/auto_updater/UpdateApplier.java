package inc.nomard.spoty.core.auto_updater;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class UpdateApplier {

    private static final String FLAG_FILE = "path/to/download/location/update-available.flag";

    public static void markUpdateAvailable() {
        try (FileWriter writer = new FileWriter(FLAG_FILE)) {
            writer.write("update available");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void applyUpdateIfAvailable() {
        if (Files.exists(Paths.get(FLAG_FILE))) {
            try {
                Runtime.getRuntime().exec("path/to/download/location/application-installer.exe");
                new File(FLAG_FILE).delete();
                System.exit(0); // Exit the current application to allow the update to proceed
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
