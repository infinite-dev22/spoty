package inc.nomard.spoty.core.views.splash;

import fr.brouillard.oss.cssfx.*;
import inc.nomard.spoty.core.*;
import inc.nomard.spoty.core.startup.*;
import inc.nomard.spoty.core.values.*;
import inc.nomard.spoty.core.values.strings.*;
import inc.nomard.spoty.core.views.*;
import inc.nomard.spoty.core.views.layout.*;
import inc.nomard.spoty.core.views.layout.navigation.*;
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
