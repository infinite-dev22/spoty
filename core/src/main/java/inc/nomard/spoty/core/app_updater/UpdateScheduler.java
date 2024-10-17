package inc.nomard.spoty.core.app_updater;

import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;
import javafx.util.Duration;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CompletableFuture;

@Slf4j
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
                        log.error(throwable.getMessage(), throwable);
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

