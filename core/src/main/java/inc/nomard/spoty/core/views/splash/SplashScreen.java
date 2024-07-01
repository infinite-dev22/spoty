package inc.nomard.spoty.core.views.splash;

import fr.brouillard.oss.cssfx.*;
import inc.nomard.spoty.core.*;
import inc.nomard.spoty.core.startup.*;
import inc.nomard.spoty.core.values.*;
import inc.nomard.spoty.core.values.strings.*;
import inc.nomard.spoty.core.views.*;
import inc.nomard.spoty.core.views.layout.navigation.*;
import inc.nomard.spoty.core.views.util.*;
import inc.nomard.spoty.network_bridge.auth.*;
import inc.nomard.spoty.utils.*;
import io.github.palexdev.materialfx.theming.*;
import java.io.*;
import java.time.*;
import javafx.application.*;
import javafx.fxml.*;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.*;
import lombok.extern.java.*;

@Log
public class SplashScreen extends BorderPane {
    @FXML
    public Label applicationName,
            companyName,
            copyRight;
    @FXML
    public AnchorPane splashScreenPane;

    public SplashScreen() {
        init();
    }

    public static void checkFunctions() {
        var sysPathCreator = sysPathCreater();

        try {
            sysPathCreator.join();
            startApp();
        } catch (InterruptedException e) {
            SpotyLogger.writeToFile(e, SplashScreen.class);
        }
    }

    private static Thread sysPathCreater() {
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

        Platform.runLater(
                () -> {
                    Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
                    try {
                        // handle CSS dynamically.
                        CSSFX.start();
                        Stage primaryStage = new Stage();
                        // Load app views.
                        Pages.setControllers(primaryStage);
                        Pages.setPanes();
                        // Load dialog views.
                        Dialogs.setControllers(primaryStage);
                        Dialogs.setDialogContent();
                        // Base view parent.
                        Parent root = new AuthScreen(primaryStage);
                        Scene scene = new Scene(root);
                        // Set application scene theme to MFX modern themes.
                        io.github.palexdev.mfxcomponents.theming.MaterialThemes.PURPLE_LIGHT.applyOn(scene);
                        // Fixes black edges showing in main app scene.
                        scene.setFill(null);
                        primaryStage.setScene(scene);
                        primaryStage.initStyle(StageStyle.TRANSPARENT);
                        // Set window title name, this name will only be seen when cursor hovers over app icon in
                        // taskbar. Not necessary too but added since other apps also do this.
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
                        // Set window position to center of screen.
                        // This isn't necessary, just felt like adding it here.
                        primaryStage.setX((primScreenBounds.getWidth() - primaryStage.getWidth()) / 2);
                        primaryStage.setY((primScreenBounds.getHeight() - primaryStage.getHeight()) / 2);
                        // Store login scene's sizes for later use on logout.
                        ProtectedGlobals.loginSceneWidth = scene.getWidth();
                        ProtectedGlobals.loginSceneHeight = scene.getHeight();
                    } catch (IOException e) {
                        SpotyLogger.writeToFile(e, SplashScreen.class);
                    }
                });
    }

    private void init() {
        this.getStyleClass().add("rounded");
        this.setPadding(new Insets(5d, 0d, 0d, 0d));
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
        applicationName = new Label();
        applicationName.getStyleClass().add("splashName");
        applicationName.setText(Labels.APP_NAME);

        var vbox = new VBox(applicationName);
        vbox.setAlignment(Pos.CENTER);
        NodeUtils.setAnchors(vbox, new Insets(0d));

        splashScreenPane = new AnchorPane(vbox);
        splashScreenPane.setPrefHeight(350d);
        splashScreenPane.setPrefWidth(550d);
        return splashScreenPane;
    }

    private AnchorPane buildBottom() {
        companyName = new Label();
        companyName.getStyleClass().add("company-label");
        UIUtils.anchor(companyName, 0d, 0d, 0d, null);
        companyName.setPadding(new Insets(0d, 10d, 5d, 0d));
        companyName.setText("Powered by " + Labels.COMPANY_NAME);

        copyRight = new Label();
        copyRight.getStyleClass().add("company-label");
        UIUtils.anchor(copyRight, 0d, null, 0d, 0d);
        copyRight.setPadding(new Insets(0d, 0d, 5d, 10d));
        copyRight.setText("©" + Year.now() + " nomard® Labs");

        var pane = new AnchorPane();
        BorderPane.setAlignment(pane, Pos.CENTER);
        pane.getChildren().addAll(companyName, copyRight);
        return pane;
    }
}
