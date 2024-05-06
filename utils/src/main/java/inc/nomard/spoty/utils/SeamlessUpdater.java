package inc.nomard.spoty.utils;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;
import javax.xml.parsers.*;
import org.w3c.dom.*;
import org.xml.sax.*;

public class SeamlessUpdater {

    private static final String UPDATE_URL = "https://your-server.com/version_info.xml";
    private static final String CURRENT_VERSION = "1.0.0";
    private static final String INSTALLER_DOWNLOAD_URL = "https://your-server.com/latest_installer.exe";
    private static final String DOWNLOAD_LOCATION = "new_installer.exe";
    private static final String UPDATE_FLAG_FILE = "update_pending.flag"; // Path relative to app data

    // Update checker - consider running this periodically in a background thread
    // public static void checkForUpdates() {
    //     try {
    //         URL versionUrl = new URL(UPDATE_URL);
    //         URLConnection connection = versionUrl.openConnection();
    //         String latestVersion = new String(connection.getInputStream().readAllBytes()).trim();
    //         if (!latestVersion.equals(CURRENT_VERSION)) {
    //             downloadUpdate();
    //             createUpdatePendingFlag();
    //         }
    //     } catch (IOException e) {
    //         System.out.println("Error checking for updates: " + e.getMessage());
    //     }
    // }
    // public static void checkForUpdates() {
    //     try {
    //         // Download version info XML file
    //         URL versionFileUrl = new URL(UPDATE_URL);
    //         URLConnection connection = versionFileUrl.openConnection();
    //
    //         // Parse XML document (using libraries like DOM or JAXB)
    //         DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    //         DocumentBuilder builder = factory.newDocumentBuilder();
    //         Document doc = builder.parse(connection.getInputStream());
    //
    //         // Extract version and release notes from XML (assuming structure)
    //         NodeList nodes = doc.getElementsByTagName("version");
    //         String latestVersion = nodes.item(0).getTextContent().trim();
    //
    //         nodes = doc.getElementsByTagName("releaseNotes");
    //         String releaseNotes = nodes.getLength() > 0 ? nodes.item(0).getTextContent().trim() : "";
    //
    //         if (!latestVersion.equals(CURRENT_VERSION)) {
    //             System.out.println("Update available: " + latestVersion);
    //             System.out.println("Release notes:\n" + releaseNotes);
    //             //  You can use this information to display a user notification
    //             downloadUpdate();
    //             createUpdatePendingFlag();
    //         }
    //
    //     } catch (IOException | ParserConfigurationException | SAXException e) {
    //         System.out.println("Error checking for updates: " + e.getMessage());
    //     }
    // }
    public static void checkForUpdates() {
        try {
            // Download version info XML file
            URL versionFileUrl = new URL(UPDATE_URL);
            URLConnection connection = versionFileUrl.openConnection();

            // Parse XML document (using libraries like DOM or JAXB)
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(connection.getInputStream());

            // Extract latest version from XML
            NodeList nodes = doc.getElementsByTagName("version");
            String latestVersion = nodes.item(0).getTextContent().trim();

            // Get the currently installed version (replace with your logic)
            String currentVersion = getInstalledVersion(); // Replace with your method to get the local version

            if (compareVersions(latestVersion, currentVersion) > 0) {
                System.out.println("Update available: " + latestVersion);
                // Extract release notes from XML (assuming structure)
                nodes = doc.getElementsByTagName("releaseNotes");
                String releaseNotes = nodes.getLength() > 0 ? nodes.item(0).getTextContent().trim() : "";
                System.out.println("Release notes:\n" + releaseNotes);
                //  You can use this information to display a user notification
                downloadUpdate();
                createUpdatePendingFlag();
            } else {
                System.out.println("Your application is up to date.");
            }

        } catch (IOException | ParserConfigurationException | SAXException e) {
            System.out.println("Error checking for updates: " + e.getMessage());
        }
    }

    // Helper method to compare versions (assuming simple version format)
    private static int compareVersions(String versionA, String versionB) {
        String[] partsA = versionA.split("\\.");
        String[] partsB = versionB.split("\\.");

        int length = Math.min(partsA.length, partsB.length);
        for (int i = 0; i < length; i++) {
            try {
                int numA = Integer.parseInt(partsA[i]);
                int numB = Integer.parseInt(partsB[i]);
                if (numA > numB) {
                    return 1;
                } else if (numA < numB) {
                    return -1;
                }
            } catch (NumberFormatException e) {
                // Handle invalid version format
                return -1;
            }
        }

        // If equal up to the shorter length, the longer version is higher
        if (partsA.length > partsB.length) {
            return 1;
        } else if (partsA.length < partsB.length) {
            return -1;
        }

        // Versions are identical
        return 0;
    }

    // Replace this with your logic to get the installed version of the application
    public static String getInstalledVersion() {
        try {
            ResourceBundle resources = ResourceBundle.getBundle("version");
            return resources.getString("application.version");
        } catch (MissingResourceException e) {
            System.out.println("Version information not found");
            return "Unknown";
        }
    }
    // XML FILE DEMO.
    /*<?xml version="1.0"?>
    <versionInfo>
      <version>1.0.1</version>
      <releaseNotes>
        This is a sample update with some improvements:
            * Bug fix for issue X
            * New feature Y added
      </releaseNotes>
    </versionInfo>*/



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

//    public static void launchInstallerAndExit() throws IOException {
//        // Check if downloaded update exists
//        File downloadedInstaller = getDownloadedInstallerFile();
//        if (downloadedInstaller == null) {
//            System.out.println("No update downloaded. Check for updates first.");
//            return;
//        }
//
//        // Display confirmation prompt before installation (optional)
//        if (confirmUpdate(downloadedInstaller)) {
//            // Run the installer (can be sandboxed if needed)
//            runInstallerInSandbox(downloadedInstaller);
//            deleteUpdateFlagFile();
//            System.exit(0);
//        } else {
//            System.out.println("Update installation cancelled by user.");
//        }
//    }

//    private static File getDownloadedInstallerFile() throws IOException {
//        String downloadDir = "updates"; // Replace with your download directory
//        File dir = new Path(downloadDir).toFile();
//
//        // Filter files by looking for installers (adjust filter logic as needed)
//        File[] files = dir.listFiles((dir1, name) -> name.endsWith(".exe"));
//        if (files != null && files.length > 0) {
//            // Assuming you only have one downloaded installer, return the first file
//            return files[0];
//        } else {
//            return null;
//        }
//    }

//    private static void runInstallerInSandbox(File installerFile) throws IOException {
//        // Check if sandboxing is enabled (replace with your sandboxing logic)
//        if (!isSandboxingEnabled()) {
//            System.out.println("Sandboxing not enabled, running installer directly.");
//            installerFile.toPath().toFile().setExecutable(true); // Set executable permission (if needed)
//            installerFile.toPath().toFile().execute();
//            return;
//        }
//
//        // Sandbox execution logic using a specific library (replace with your implementation)
//        try {
//            Sandbox sandboxingTool = new Sandbox(); // Replace with your sandboxing library object
//            sandboxingTool.run(installerFile.toPath());
//        } catch (SandboxException e) {
//            System.out.println("Error running installer in sandbox: " + e.getMessage());
//            throw new IOException("Failed to run installer in sandbox");
//        }
//    }
}

