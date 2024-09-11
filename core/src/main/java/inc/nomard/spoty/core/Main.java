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

import atlantafx.base.theme.CupertinoDark;
import atlantafx.base.theme.CupertinoLight;
import fr.brouillard.oss.cssfx.CSSFX;
import inc.nomard.spoty.core.auto_updater.v2.AutoUpdater;
import inc.nomard.spoty.core.auto_updater.v2.UpdateScheduler;
import inc.nomard.spoty.core.startup.SpotyPaths;
import inc.nomard.spoty.core.values.PreloadedData;
import inc.nomard.spoty.core.values.strings.Labels;
import inc.nomard.spoty.core.views.layout.AppManager;
import inc.nomard.spoty.core.views.pages.AuthScreen;
import inc.nomard.spoty.core.views.splash.SplashScreen;
import inc.nomard.spoty.network_bridge.auth.ProtectedGlobals;
import inc.nomard.spoty.utils.SpotyLogger;
import inc.nomard.spoty.utils.SpotyThreader;
import io.github.palexdev.materialfx.theming.JavaFXThemes;
import io.github.palexdev.materialfx.theming.MaterialFXStylesheets;
import io.github.palexdev.materialfx.theming.UserAgentBuilder;
import io.github.palexdev.mfxcomponents.theming.MaterialThemes;
import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.application.ColorScheme;
import javafx.application.Platform;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import lombok.extern.java.Log;

import java.io.IOException;

@Log
public class Main extends Application {
    private static final String lightThemeCSS = SpotyCoreResourceLoader.load("styles/theming/light-theme.css");
    private static final String darkThemeCSS = SpotyCoreResourceLoader.load("styles/theming/dark-theme.css");
    public static Stage primaryStage = null;

    public static void main(String... args) {
        AutoUpdater.applyUpdateIfAvailable();
        UpdateScheduler.startUpdateChecker();
        Application.launch(Main.class, args);
    }

    public static void checkFunctions() {
        var sysPathCreator = createSysPathThread();

        try {
            sysPathCreator.join();
            startApp();
        } catch (InterruptedException e) {
            SpotyLogger.writeToFile(e, SplashScreen.class);
        }
    }

    private static Thread createSysPathThread() {
        return SpotyThreader.singleThreadCreator(
                "paths-creator",
                () -> {
                    try {
                        SpotyPaths.createPaths();
                    } catch (Exception e) {
                        SpotyLogger.writeToFile(e, SplashScreen.class);
                    }
                });
    }

    private static void startApp() {
        UserAgentBuilder.builder()
                .themes(JavaFXThemes.MODENA)
                .themes(MaterialFXStylesheets.forAssemble(true))
                .setDeploy(true)
                .setResolveAssets(true)
                .build()
                .setGlobal();

        Platform.runLater(() -> {
            var screenBounds = Screen.getPrimary().getVisualBounds();
            try {
                CSSFX.start();
                var primaryStage = new Stage();
                AppManager.setPrimaryStage(primaryStage);

                // Initialize and show the main application scene
                initializePrimaryStage(primaryStage, screenBounds);
            } catch (IOException e) {
                SpotyLogger.writeToFile(e, SplashScreen.class);
            }
        });
    }

    private static void initializePrimaryStage(Stage primaryStage, Rectangle2D screenBounds) throws IOException {
        var root = new AuthScreen(primaryStage);
        var scene = new Scene(root);
        scene.getStylesheets().add(lightThemeCSS);
        getSystemTheme(scene);
        MaterialThemes.PURPLE_LIGHT.applyOn(scene);
        scene.setFill(null);

        primaryStage.setScene(scene);
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        primaryStage.setTitle(Labels.APP_NAME);
        primaryStage.getIcons().addAll(
                PreloadedData.icon16,
                PreloadedData.icon32,
                PreloadedData.icon64,
                PreloadedData.icon128,
                PreloadedData.icon256,
                PreloadedData.icon512
        );
        primaryStage.show();
        primaryStage.setX((screenBounds.getWidth() - primaryStage.getWidth()) / 2);
        primaryStage.setY((screenBounds.getHeight() - primaryStage.getHeight()) / 2);

        ProtectedGlobals.loginSceneWidth = scene.getWidth();
        ProtectedGlobals.loginSceneHeight = scene.getHeight();

        applyFadeTransition(root);

        AppManager.setScene(scene);
    }

    private static void applyFadeTransition(Parent root) {
        var fadeIn = new FadeTransition(Duration.seconds(1), root);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);
        fadeIn.play();
    }

    private static void getSystemTheme(Scene scene) {
        // Apply platform color scheme preferences
        Platform.Preferences preferences = Platform.getPreferences();
        if (preferences == null) {
            return;
        }

        ColorScheme colorScheme = preferences.getColorScheme();
        updateTheme(colorScheme, scene);

        // Add a listener to update on color scheme changes
        preferences.colorSchemeProperty().addListener((observable, oldValue, newValue) -> updateTheme(newValue, scene));
    }

    // Instantiate the css files and hold them in a variable which you add to or remove from style sheets.
    // the styles are held in memory and easily referenced.
    private static void updateTheme(ColorScheme colorScheme, Scene scene) {
        if (colorScheme == ColorScheme.DARK) {
            // Remove light stylesheet, add dark stylesheet
//            scene.getStylesheets().remove(lightThemeCSS);
            scene.getStylesheets().set(0, darkThemeCSS);
            Application.setUserAgentStylesheet(new CupertinoDark().getUserAgentStylesheet());
            MaterialThemes.PURPLE_DARK.applyOn(scene);
        } else {
            // Remove dark stylesheet, add light stylesheet
//            scene.getStylesheets().remove(darkThemeCSS);
            scene.getStylesheets().set(0, lightThemeCSS);
            Application.setUserAgentStylesheet(new CupertinoLight().getUserAgentStylesheet());
            MaterialThemes.PURPLE_LIGHT.applyOn(scene);
        }
    }

    @Override
    public void init() {
        PreloadedData.preloadImages();
        checkFunctions();
    }

    @Override
    public void start(Stage primaryStage) {
        Main.primaryStage = primaryStage;
    }
}
