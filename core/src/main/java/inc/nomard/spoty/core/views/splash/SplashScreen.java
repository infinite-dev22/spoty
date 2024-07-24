package inc.nomard.spoty.core.views.splash;

import atlantafx.base.theme.*;
import fr.brouillard.oss.cssfx.*;
import inc.nomard.spoty.core.*;
import inc.nomard.spoty.core.startup.*;
import inc.nomard.spoty.core.values.*;
import inc.nomard.spoty.core.values.strings.*;
import inc.nomard.spoty.core.views.layout.*;
import inc.nomard.spoty.core.views.pages.*;
import inc.nomard.spoty.core.views.util.*;
import inc.nomard.spoty.network_bridge.auth.*;
import inc.nomard.spoty.utils.*;
import io.github.palexdev.materialfx.theming.*;
import java.io.*;
import java.time.*;
import javafx.animation.*;
import javafx.application.*;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.*;
import javafx.util.Duration;
import lombok.extern.java.*;

@Log
public class SplashScreen extends BorderPane {
    private static final String lightThemeCSS = SpotyCoreResourceLoader.load("styles/theming/light-theme.css");
    private static final String darkThemeCSS = SpotyCoreResourceLoader.load("styles/theming/dark-theme.css");

    public SplashScreen() {
        initUI();
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
        io.github.palexdev.mfxcomponents.theming.MaterialThemes.PURPLE_LIGHT.applyOn(scene);
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

        // final OsThemeDetector detector = OsThemeDetector.getDetector();
        // final boolean isDarkThemeUsed = detector.isDark();
        // if (isDarkThemeUsed) {
        //     //The OS uses a dark theme
        //     updateTheme(ColorScheme.DARK, scene);
        // } else {
        //     //The OS uses a light theme
        //     updateTheme(ColorScheme.LIGHT, scene);
        // }
        // detector.registerListener(isDark -> Platform.runLater(() -> {
        //     if (isDark) {
        //         // The OS switched to a dark theme
        //         updateTheme(ColorScheme.DARK, scene);
        //     } else {
        //         // The OS switched to a light theme
        //         updateTheme(ColorScheme.LIGHT, scene);
        //     }
        // }));
    }

    // Instantiate the css files and hold them in a variable which you add to or remove from style sheets.
    // the styles are held in memory and easily referenced.
    private static void updateTheme(ColorScheme colorScheme, Scene scene) {
        if (colorScheme == ColorScheme.DARK) {
            // Remove light stylesheet, add dark stylesheet
//            scene.getStylesheets().remove(lightThemeCSS);
            scene.getStylesheets().set(0, darkThemeCSS);
            Application.setUserAgentStylesheet(new CupertinoDark().getUserAgentStylesheet());
        } else {
            // Remove dark stylesheet, add light stylesheet
//            scene.getStylesheets().remove(darkThemeCSS);
            scene.getStylesheets().set(0, lightThemeCSS);
            Application.setUserAgentStylesheet(new CupertinoLight().getUserAgentStylesheet());
        }
    }

    private void initUI() {
        this.getStyleClass().add("rounded");
        this.setPadding(new Insets(5, 0, 0, 0));
        this.getStylesheets().addAll(
                SpotyCoreResourceLoader.load("styles/base.css"),
                SpotyCoreResourceLoader.load("styles/Splash.css"),
                SpotyCoreResourceLoader.load("styles/Common.css"),
                SpotyCoreResourceLoader.load("styles/TextFields.css")
        );
        this.setCenter(buildCenter());
        this.setBottom(buildBottom());
    }

    private AnchorPane buildCenter() {
        var applicationName = new Label(Labels.APP_NAME);
        applicationName.getStyleClass().add("splashName");

        var vbox = new VBox(applicationName);
        vbox.setAlignment(Pos.CENTER);
        NodeUtils.setAnchors(vbox, new Insets(0));

        var splashScreenPane = new AnchorPane(vbox);
        splashScreenPane.setPrefHeight(350);
        splashScreenPane.setPrefWidth(550);
        return splashScreenPane;
    }

    private AnchorPane buildBottom() {
        var companyName = new Label("Powered by " + Labels.COMPANY_NAME);
        companyName.getStyleClass().add("company-label");
        UIUtils.anchor(companyName, 0d, 0d, 0d, null);
        companyName.setPadding(new Insets(0, 10, 5, 0));

        var copyRight = new Label("©" + Year.now() + " nomard® Labs");
        copyRight.getStyleClass().add("company-label");
        UIUtils.anchor(copyRight, 0d, null, 0d, 0d);
        copyRight.setPadding(new Insets(0, 0, 5, 10));

        var pane = new AnchorPane();
        BorderPane.setAlignment(pane, Pos.CENTER);
        pane.getChildren().addAll(companyName, copyRight);
        return pane;
    }
}
