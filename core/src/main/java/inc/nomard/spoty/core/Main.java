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
import inc.nomard.spoty.network_bridge.auth.ProtectedGlobals;
import inc.nomard.spoty.utils.SpotyLogger;
import inc.nomard.spoty.utils.SpotyThreader;
import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.application.ColorScheme;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class Main extends Application {

    private static final String LIGHT_THEME_CSS = SpotyCoreResourceLoader.load("styles/theming/light-theme.css");
    private static final String DARK_THEME_CSS = SpotyCoreResourceLoader.load("styles/theming/dark-theme.css");

    private static void initializePrimaryStage(Stage primaryStage) {
//        configureUserAgent();
        CSSFX.start();
        AppManager.setPrimaryStage(primaryStage);

        Scene scene = createPrimaryScene(primaryStage);
        configurePrimaryStage(primaryStage, scene);

        applyFadeTransition(scene.getRoot());
        AppManager.setScene(scene);
    }

//    private static void configureUserAgent() {
//        UserAgentBuilder.builder()
//                .themes(JavaFXThemes.MODENA)
//                .themes(MaterialFXStylesheets.forAssemble(true))
//                .setDeploy(true)
//                .setResolveAssets(true)
//                .build()
//                .setGlobal();
//    }

    private static Scene createPrimaryScene(Stage primaryStage) {
        Parent root = new AuthScreen(primaryStage);
        Scene scene = new Scene(root);
        scene.getStylesheets().add(LIGHT_THEME_CSS);
        applySystemTheme(scene);
        scene.setFill(null);
        return scene;
    }

    private static void configurePrimaryStage(Stage primaryStage, Scene scene) {
        var screenBounds = Screen.getPrimary().getVisualBounds();
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
    }

    private static void applyFadeTransition(Parent root) {
        FadeTransition fadeIn = new FadeTransition(Duration.seconds(1), root);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);
        fadeIn.play();
    }

    private static void applySystemTheme(Scene scene) {
        Platform.Preferences preferences = Platform.getPreferences();
        if (preferences != null) {
            ColorScheme colorScheme = preferences.getColorScheme();
            updateTheme(colorScheme, scene);
            preferences.colorSchemeProperty().addListener(
                    (observable, oldValue, newValue) -> updateTheme(newValue, scene)
            );
        }
    }

    private static void updateTheme(ColorScheme colorScheme, Scene scene) {
        if (colorScheme == ColorScheme.DARK) {
            scene.getStylesheets().set(0, DARK_THEME_CSS);
            Application.setUserAgentStylesheet(new CupertinoDark().getUserAgentStylesheet());
        } else {
            scene.getStylesheets().set(0, LIGHT_THEME_CSS);
            Application.setUserAgentStylesheet(new CupertinoLight().getUserAgentStylesheet());
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void init() {
        PreloadedData.preloadImages();
        try {
            SpotyThreader.singleThreadCreator(
                    "paths-creator",
                    () -> {
                        try {
                            SpotyPaths.createPaths();
                        } catch (Exception e) {
                            SpotyLogger.writeToFile(e, Main.class);
                        }
                    }).join();
        } catch (InterruptedException e) {
            SpotyLogger.writeToFile(e, Main.class);
        }
        AutoUpdater.applyUpdateIfAvailable();
        UpdateScheduler.startUpdateChecker();
    }

    @Override
    public void start(Stage primaryStage) {
        initializePrimaryStage(primaryStage);
    }
}
