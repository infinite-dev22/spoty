package inc.nomard.spoty.core.auto_updater.v2;

import inc.nomard.spoty.core.views.pages.*;
import inc.nomard.spoty.utils.*;
import java.util.concurrent.*;
import javafx.concurrent.*;
import javafx.util.*;

public class UpdateScheduler {
    private static final ScheduledService<Void> scheduledService;

    static {
        scheduledService = new ScheduledService<>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<>() {
                    @Override
                    protected Void call() {
                        CompletableFuture.runAsync(AutoUpdater::checkForUpdates).exceptionally(this::onFailure);
                        return null;
                    }

                    private Void onFailure(Throwable throwable) {
                        SpotyLogger.writeToFile(throwable, AuthScreen.class);
                        return null;
                    }
                };
            }
        };
    }

    public static void startUpdateChecker() {
        scheduledService.setDelay(new Duration(0d));
        scheduledService.setPeriod(Duration.hours(3d));
        scheduledService.start();
    }
}

