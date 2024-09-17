package inc.nomard.spoty.core.auto_updater.v2;

import inc.nomard.spoty.core.startup.*;
import inc.nomard.spoty.utils.*;
import inc.nomard.spoty.utils.flavouring.*;

import java.io.*;
import java.net.*;
import java.nio.file.*;

public class AutoUpdater {

    private static final String VERSION_URL = "http://yourserver.com/version.txt";

    private static String getUpdateUrl() {
        var updateUrl = "";
        switch (OSUtil.getOs()) {
            case LINUX -> updateUrl = "http://yourserver.com/releases/application-installer.rpm";
            case MACOS -> updateUrl = "http://yourserver.com/releases/application-installer.dmg";
            case WINDOWS -> updateUrl = "http://yourserver.com/releases/application-installer.exe";
        }
        return updateUrl;
    }

    private static String getDownloadPath() {
        Path downloadPath = null;
        String installerFilePath = null;
        switch (OSUtil.getOs()) {
            case LINUX, MACOS -> downloadPath = Paths.get(
                    System.getProperty("user.home") + "/.config/OpenSaleERP/data/updates");
            case WINDOWS -> downloadPath = Paths.get(System.getProperty("user.home")
                    + "\\AppData\\Local\\OpenSaleERP\\data\\updates");
        }
        try {
            Files.createDirectories(downloadPath);
        } catch (IOException e) {
            SpotyLogger.writeToFile(e, SpotyPaths.class);
        }
        switch (OSUtil.getOs()) {
            case LINUX -> installerFilePath = downloadPath + "/application-installer.rpm";
            case MACOS -> installerFilePath = downloadPath + "/application-installer.dmg";
            case WINDOWS -> installerFilePath = downloadPath + "\\application-installer.exe";
        }
        return installerFilePath;
    }

    private static String getFlagFile() {
        Path flagFilePath = null;

        switch (OSUtil.getOs()) {
            case LINUX, MACOS -> flagFilePath = Paths.get(
                    System.getProperty("user.home") + "/.config/OpenSaleERP/data/updates/flag");
            case WINDOWS -> flagFilePath = Paths.get(System.getProperty("user.home")
                    + "\\AppData\\Local\\OpenSaleERP\\data\\updates\\flag");
        }

        try {
            Files.createDirectories(flagFilePath);
        } catch (IOException e) {
            SpotyLogger.writeToFile(e, SpotyPaths.class);
        }
        return flagFilePath + "update-available.flag";
    }

    public static void checkForUpdates() {
        new Thread(() -> {
            try {
                if (isUpdateAvailable()) {
                    downloadUpdate();
                    markUpdateAvailable();
                    notifyUserForRestart();
                }
            } catch (Exception e) {
                SpotyLogger.writeToFile(new Throwable("Error checking for updates" + e), AutoUpdater.class);
            }
        }).start();
    }

    private static boolean isUpdateAvailable() throws IOException {
        URL url = URI.create(VERSION_URL).toURL();
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            String latestVersion = in.readLine();
            return compareVersions(AppConfig.getAppVersion(), latestVersion) < 0;
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
        try (BufferedInputStream in = new BufferedInputStream(URI.create(getUpdateUrl()).toURL().openStream());
             FileOutputStream fileOutputStream = new FileOutputStream(getDownloadPath())) {
            byte[] dataBuffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
                fileOutputStream.write(dataBuffer, 0, bytesRead);
            }
            SpotyLogger.writeToFile(new Throwable("Update downloaded successfully."), AutoUpdater.class);
        }
    }

    private static void markUpdateAvailable() throws IOException {
        try (FileOutputStream fos = new FileOutputStream(getFlagFile())) {
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
        if (Files.exists(Paths.get(getFlagFile()))) {
            try {
                Runtime.getRuntime().exec(getDownloadPath());
                Files.delete(Paths.get(getFlagFile()));
                System.exit(0); // Exit the current application to allow the update to proceed
            } catch (IOException e) {
                SpotyLogger.writeToFile(new Throwable("Error applying update" + e), AutoUpdater.class);
            }
        }
    }
}