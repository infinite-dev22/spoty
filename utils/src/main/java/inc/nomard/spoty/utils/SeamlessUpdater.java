package inc.nomard.spoty.utils;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

public class SeamlessUpdater {

    private static final String UPDATE_URL = "https://your-server.com/latest_version.txt";
    private static final String CURRENT_VERSION = "1.0.0";
    private static final String INSTALLER_DOWNLOAD_URL = "https://your-server.com/latest_installer.exe";
    private static final String DOWNLOAD_LOCATION = "new_installer.exe";
    private static final String UPDATE_FLAG_FILE = "update_pending.flag"; // Path relative to app data

    // Update checker - consider running this periodically in a background thread
    public static void checkForUpdates() {
        try {
            URL versionUrl = new URL(UPDATE_URL);
            URLConnection connection = versionUrl.openConnection();
            String latestVersion = new String(connection.getInputStream().readAllBytes()).trim();

            if (!latestVersion.equals(CURRENT_VERSION)) {
                downloadUpdate();
                createUpdatePendingFlag();
            }

        } catch (IOException e) {
            System.out.println("Error checking for updates: " + e.getMessage());
        }
    }

    private static void downloadUpdate() throws IOException {
        URL downloadUrl = new URL(INSTALLER_DOWNLOAD_URL);
        try (BufferedInputStream in = new BufferedInputStream(downloadUrl.openStream());
             FileOutputStream out = new FileOutputStream(DOWNLOAD_LOCATION)) {
            byte[] dataBuffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
                out.write(dataBuffer, 0, bytesRead);
            }
        }
    }

    private static void createUpdatePendingFlag() {
        try {
            File updateFlag = new File(UPDATE_FLAG_FILE);
            updateFlag.createNewFile(); // Creates an empty file
        } catch (IOException e) {
            System.out.println("Error creating update flag: " + e.getMessage());
        }
    }

    public static boolean checkForPendingUpdate() {
        File updateFlag = new File(UPDATE_FLAG_FILE);
        return updateFlag.exists();
    }

    public static void launchInstallerAndExit() {
        try {
            Process process = Runtime.getRuntime()
                    .exec("cmd /c start /wait new_installer.exe /");
            process.waitFor();

            // Delete the flag file
            new File(UPDATE_FLAG_FILE).delete();

            System.exit(0);

        } catch (IOException | InterruptedException e) {
            System.out.println("Error launching the installer: " + e.getMessage());
        }
    }
}

