package inc.nomard.spoty.core.auto_updater;

import java.io.*;
import java.net.*;

public class UpdateDownloader {
    private static final String UPDATE_URL = "http://yourserver.com/application-installer.exe";
    private static final String DOWNLOAD_PATH = "path/to/download/location/application-installer.exe";

    public static void downloadUpdate() {
        new Thread(() -> {
            try (BufferedInputStream in = new BufferedInputStream(new URL(UPDATE_URL).openStream());
                 FileOutputStream fileOutputStream = new FileOutputStream(DOWNLOAD_PATH)) {
                byte dataBuffer[] = new byte[1024];
                int bytesRead;
                while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
                    fileOutputStream.write(dataBuffer, 0, bytesRead);
                }
                System.out.println("Update downloaded successfully.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }
}
