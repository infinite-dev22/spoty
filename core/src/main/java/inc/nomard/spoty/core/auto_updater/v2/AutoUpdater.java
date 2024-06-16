package inc.nomard.spoty.core.auto_updater.v2;

import java.io.*;
import java.net.*;
import java.nio.file.*;
import java.util.logging.*;

public class AutoUpdater {

    private static final String VERSION_URL = "http://yourserver.com/version.txt";
    private static final String UPDATE_URL = "http://yourserver.com/application-installer.exe";
    private static final String DOWNLOAD_PATH = "path/to/download/location/application-installer.exe";
    private static final String CURRENT_VERSION = "1.0.0";
    private static final String FLAG_FILE = "path/to/download/location/update-available.flag";

    public static void checkForUpdates() {
        new Thread(() -> {
            try {
                if (isUpdateAvailable()) {
                    downloadUpdate();
                    markUpdateAvailable();
                    notifyUserForRestart();
                }
            } catch (Exception e) {
                LoggerConfig.LOGGER.log(Level.SEVERE, "Error checking for updates", e);
            }
        }).start();
    }

    private static boolean isUpdateAvailable() throws IOException {
        URL url = new URL(VERSION_URL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            String latestVersion = in.readLine();
            return compareVersions(CURRENT_VERSION, latestVersion) < 0;
        }
    }

    private static int compareVersions(String currentVersion, String latestVersion) {
        String[] currentParts = currentVersion.split("\\.");
        String[] latestParts = latestVersion.split("\\.");
        for (int i = 0; i < Math.max(currentParts.length, latestParts.length); i++) {
            int currentPart = i < currentParts.length ? Integer.parseInt(currentParts[i]) : 0;
            int latestPart = i < latestParts.length ? Integer.parseInt(latestParts[i]) : 0;
            if (currentPart < latestPart) {
                return -1;
            }
            if (currentPart > latestPart) {
                return 1;
            }
        }
        return 0;
    }

    private static void downloadUpdate() throws IOException {
        try (BufferedInputStream in = new BufferedInputStream(new URL(UPDATE_URL).openStream());
             FileOutputStream fileOutputStream = new FileOutputStream(DOWNLOAD_PATH)) {
            byte dataBuffer[] = new byte[1024];
            int bytesRead;
            while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
                fileOutputStream.write(dataBuffer, 0, bytesRead);
            }
            LoggerConfig.LOGGER.info("Update downloaded successfully.");
        }
    }

    private static void markUpdateAvailable() throws IOException {
        try (FileOutputStream fos = new FileOutputStream(FLAG_FILE)) {
            fos.write("update available".getBytes());
        }
    }

    private static void notifyUserForRestart() {
        // Use JavaFX to show a dialog
        javafx.application.Platform.runLater(() -> {
            javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
            alert.setTitle("Update Available");
            alert.setHeaderText(null);
            alert.setContentText("An update has been downloaded and will be applied on the next restart.");
            alert.showAndWait();
        });
    }

    public static void applyUpdateIfAvailable() {
        if (Files.exists(Paths.get(FLAG_FILE))) {
            try {
                Runtime.getRuntime().exec(DOWNLOAD_PATH);
                Files.delete(Paths.get(FLAG_FILE));
                System.exit(0); // Exit the current application to allow the update to proceed
            } catch (IOException e) {
                LoggerConfig.LOGGER.log(Level.SEVERE, "Error applying update", e);
            }
        }
    }
}