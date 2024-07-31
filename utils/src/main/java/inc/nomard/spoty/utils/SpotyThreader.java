package inc.nomard.spoty.utils;

import java.util.concurrent.*;
import lombok.extern.java.*;

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
