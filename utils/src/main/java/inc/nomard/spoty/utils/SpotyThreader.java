package inc.nomard.spoty.utils;

import lombok.extern.java.Log;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javafx.util.Duration;
@Log
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
//        var thread = new Thread(runnable, name);
//        thread.setDaemon(true);
//        return thread;
        return Thread.ofPlatform().name(name).daemon(true).start(runnable);
    }
}
