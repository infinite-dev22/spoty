package inc.nomard.spoty.core.auto_updater;

import java.util.*;

public class UpdateScheduler {
    public static void startUpdateChecker() {
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                checkForUpdates();
            }
        };
        // Schedule the task to run every 24 hours (86400000 milliseconds)
        timer.schedule(task, 0, 86400000);
    }

    private static void checkForUpdates() {
        if (UpdateChecker.isUpdateAvailable()) {
            UpdateDownloader.downloadUpdate();
            UpdateApplier.markUpdateAvailable();
        }
    }
}
