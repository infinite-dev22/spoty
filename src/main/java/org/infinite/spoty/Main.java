package org.infinite.spoty;

import javafx.application.Application;
import javafx.stage.Stage;
import org.infinite.spoty.views.splash.LaunchPreloader;
import org.infinite.spoty.views.splash.SplashScreenController;

public class Main extends Application {
    public static Stage primaryStage = null;

    public static void runApp(String... args) {
        System.setProperty("javafx.preloader", LaunchPreloader.class.getCanonicalName());
        launch(args);
    }

    @Override
    public void init() {
        SplashScreenController.checkFunctions();
    }

    @Override
    public void start(Stage primaryStage) {
        Main.primaryStage = primaryStage;
    }
}
