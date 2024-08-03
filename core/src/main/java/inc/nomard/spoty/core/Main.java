/*
 * Copyright (c) 2023, Jonathan Mark Mwigo. All rights reserved.
 *
 * The computer system code contained in this file is the property of Jonathan Mark Mwigo and is protected by copyright law. Any unauthorized use of this code is prohibited.
 *
 * This copyright notice applies to all parts of the computer system code, including the source code, object code, and any other related materials.
 *
 * The computer system code may not be modified, translated, or reverse-engineered without the express written permission of Jonathan Mark Mwigo.
 * 0750802611
 * Jonathan Mark Mwigo reserves the right to update, modify, or discontinue the computer system code at any time.
 *
 * Jonathan Mark Mwigo makes no warranties, express or implied, with respect to the computer system code. Jonathan Mark Mwigo shall not be liable for any damages, including, but not limited to, direct, indirect, incidental, special, consequential, or punitive damages, arising out of or in connection with the use of the computer system code.
 */

package inc.nomard.spoty.core;

import inc.nomard.spoty.core.auto_updater.v2.*;
import inc.nomard.spoty.core.views.splash.*;
import javafx.animation.*;
import javafx.application.*;
import javafx.concurrent.*;
import javafx.stage.*;
import javafx.util.*;
import lombok.extern.java.*;

@Log
public class Main extends Application {

    public static Stage primaryStage = null;

    public static void main(String... args) {
        System.setProperty("javafx.preloader", LaunchPreloader.class.getCanonicalName());
        AutoUpdater.applyUpdateIfAvailable();
        UpdateScheduler.startUpdateChecker();
        Application.launch(Main.class, args);
    }

    @Override
    public void init() {
        delaySplashScreen();
        SplashScreen.checkFunctions();
    }

    private void delaySplashScreen() {
        // Notify pre-loader that application initialization is about to start
        notifyPreloader(new Preloader.ProgressNotification(0));

        // Simulate some long startup process
        var task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                Thread.sleep(2000); // Delay for 3 seconds
                return null;
            }

            @Override
            protected void succeeded() {
                // Notify pre-loader that initialization is complete
                notifyPreloader(new Preloader.StateChangeNotification(
                        Preloader.StateChangeNotification.Type.BEFORE_START));
            }
        };
        new Thread(task).start();

        try {
            task.get(); // Wait for the task to complete
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void start(Stage primaryStage) {
        Main.primaryStage = primaryStage;
    }
}
