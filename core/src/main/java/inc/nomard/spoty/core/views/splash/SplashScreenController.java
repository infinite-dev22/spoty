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

package inc.nomard.spoty.core.views.splash;

import fr.brouillard.oss.cssfx.*;
import inc.nomard.spoty.core.*;
import inc.nomard.spoty.core.components.message.*;
import inc.nomard.spoty.core.components.navigation.*;
import inc.nomard.spoty.core.startup.*;
import inc.nomard.spoty.core.values.strings.*;
import inc.nomard.spoty.network_bridge.auth.*;
import inc.nomard.spoty.utils.*;
import io.github.palexdev.materialfx.theming.*;
import java.io.*;
import java.net.*;
import java.util.*;
import javafx.application.*;
import javafx.fxml.*;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.stage.*;
import lombok.extern.java.*;

@Log
public class SplashScreenController implements Initializable {
    @FXML
    public Label applicationName;
    @FXML
    public AnchorPane splashScreenPane;
    @FXML
    public Label companyName;

    public static void checkFunctions() {
        var sysPathCreator = sysPathCreater();

        try {
            sysPathCreator.join();
            startApp();
        } catch (InterruptedException e) {
            SpotyLogger.writeToFile(e, SplashScreenController.class);
        }
    }

    private static Thread sysPathCreater() {
        return SpotyThreader.singleThreadCreator(
                "paths-creator",
                () -> {
                    try {
                        SpotyPaths.createPaths();
                    } catch (Exception e) {
                        SpotyLogger.writeToFile(e, SplashScreenController.class);
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
                        Dialogs.setControllers();
                        Dialogs.setDialogContent();
                        // Base view parent.
                        Parent root = Pages.getLoginPane();
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
                        primaryStage.getIcons().add(new Image(SpotyCoreResourceLoader.load("icon.png")));
                        primaryStage.show();
                        // Set window position to center of screen.
                        // This isn't necessary, just felt like adding it here.
                        primaryStage.setX((primScreenBounds.getWidth() - primaryStage.getWidth()) / 2);
                        primaryStage.setY((primScreenBounds.getHeight() - primaryStage.getHeight()) / 2);
                        // Initialize app notification handler.
                        SpotyMessageHolder.setMessageOwner(primaryStage);
                        // Store login scene's sizes for later use on logout.
                        ProtectedGlobals.loginSceneWidth = scene.getWidth();
                        ProtectedGlobals.loginSceneHeight = scene.getHeight();
                    } catch (IOException e) {
                        SpotyLogger.writeToFile(e, SplashScreenController.class);
                    }
                });
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        companyName.setText("Powered by " + Labels.COMPANY_NAME);
        applicationName.setText(Labels.APP_NAME);
    }
}
