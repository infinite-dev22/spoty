/*
 * Copyright (c) 2023, Jonathan Mark Mwigo. All rights reserved.
 *
 * The computer system code contained in this file is the property of Jonathan Mark Mwigo and is protected by copyright law. Any unauthorized use of this code is prohibited.
 *
 * This copyright notice applies to all parts of the computer system code, including the source code, object code, and any other related materials.
 *
 * The computer system code may not be modified, translated, or reverse-engineered without the express written permission of Jonathan Mark Mwigo.
 *
 * Jonathan Mark Mwigo reserves the right to update, modify, or discontinue the computer system code at any time.
 *
 * Jonathan Mark Mwigo makes no warranties, express or implied, with respect to the computer system code. Jonathan Mark Mwigo shall not be liable for any damages, including, but not limited to, direct, indirect, incidental, special, consequential, or punitive damages, arising out of or in connection with the use of the computer system code.
 */

package inc.nomard.spoty.core;

import inc.nomard.spoty.core.views.splash.LaunchPreloader;
import inc.nomard.spoty.core.views.splash.SplashScreenController;
import inc.nomard.spoty.seamless_updater.SeamlessUpdater;
import javafx.application.Application;
import javafx.stage.Stage;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Main extends Application {

    private ScheduledExecutorService scheduler;
    public static Stage primaryStage = null;

    public static void main(String... args) {
        System.setProperty("javafx.preloader", LaunchPreloader.class.getCanonicalName());

        if (SeamlessUpdater.checkForPendingUpdate()) {
            SeamlessUpdater.launchInstallerAndExit();
        }
        SeamlessUpdater.checkForUpdates();

        Application.launch(Main.class, args);
    }

    @Override
    public void init() {
        SplashScreenController.checkFunctions();
    }

    @Override
    public void start(Stage primaryStage) {
        Main.primaryStage = primaryStage;

        // Schedule periodic checks
        scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(SeamlessUpdater::checkForUpdates, 0, 3, TimeUnit.HOURS);
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        scheduler.shutdown(); // Shutdown the scheduler on application exit
    }
}
