package inc.nomard.spoty.core.auto_updater;

public class AutoUpdater {

    public static void main(String[] args) {
        UpdateScheduler.startUpdateChecker();
        UpdateApplier.applyUpdateIfAvailable();
    }
}

