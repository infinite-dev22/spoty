package inc.nomard.spoty.core.auto_updater.v2;

import inc.nomard.spoty.core.*;
import java.util.Timer;
import java.util.TimerTask;

public class UpdateScheduler {

    public static void startUpdateChecker() {
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                AutoUpdater.checkForUpdates();
            }
        };
        // Schedule the task to run every 24 hours (86400000 milliseconds)
        timer.schedule(task, 0, 86400000);
    }

    public static void main(String[] args) {
        // Start JavaFX Application
        javafx.application.Application.launch(Main.class, args);
    }
}

