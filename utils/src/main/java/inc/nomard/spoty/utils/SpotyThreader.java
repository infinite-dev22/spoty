package inc.nomard.spoty.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SpotyThreader {
    private static final ExecutorService executorService = Executors.newFixedThreadPool(5);

    public static void spotyThreadPool(Runnable runnable) {
        executorService.execute(runnable);
    }

    public static void disposeSpotyThreadPool() {
        if (!executorService.isShutdown())
            executorService.shutdownNow();
    }

    public static Thread singleThreadCreator(String name, Runnable runnable) {
        return Thread.ofVirtual().name(name).start(runnable);
    }
}
