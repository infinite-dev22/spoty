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

package org.infinite.spoty.views;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXScrollPane;
import io.github.palexdev.materialfx.utils.ScrollUtils;
import io.github.palexdev.mfxresources.fonts.MFXFontIcon;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.infinite.spoty.components.navigation.Navigation;
import org.infinite.spoty.components.navigation.Pages;
import org.infinite.spoty.utils.SpotyThreader;
import org.infinite.spoty.values.strings.Labels;

import java.net.URL;
import java.util.ResourceBundle;

import static org.infinite.spoty.utils.Utils.createToggle;

public class BaseController implements Initializable {
    public static Navigation navigation;
    private static BaseController instance;
    public final Stage primaryStage;
    @FXML
    public MFXFontIcon closeIcon;
    @FXML
    public MFXFontIcon maximizeIcon;
    @FXML
    public MFXFontIcon minimizeIcon;
    @FXML
    public StackPane contentPane;
    @FXML
    public StackPane navBar;
    @FXML
    public HBox windowHeader;
    @FXML
    public MFXScrollPane scrollPane;
    @FXML
    public AnchorPane rootPane;
    @FXML
    public VBox settingsHolder;
    @FXML
    public Label appNameLabel;
    private double xOffset;
    private double yOffset;

    private BaseController(Stage stage) {
        this.primaryStage = stage;
    }

    public static BaseController getInstance(Stage stage) {
        if (instance == null) instance = new BaseController(stage);
        return instance;
    }

    @FXML
    void closeIconClicked() {
        primaryStage.hide();
        primaryStage.close();
        SpotyThreader.disposeSpotyThreadPool();
        Platform.exit();
        System.exit(0);
    }

    @FXML
    void maximizeIconClicked() {
        Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
//        ((Stage) rootPane.getScene().getWindow())
//                .setMaximized(!((Stage) rootPane.getScene().getWindow()).isMaximized());

//      primaryStage.setFullScreen(!primaryStage.isFullScreen());
        if (primaryStage.getHeight() == primScreenBounds.getHeight() && primaryStage.getWidth() == primScreenBounds.getWidth()) {
            primaryStage.setHeight(primScreenBounds.getHeight() * .9);
            primaryStage.setWidth(primScreenBounds.getWidth() * .8);
            // Center window in screen.
            primaryStage.setX((primScreenBounds.getWidth() - primaryStage.getWidth()) / 2);
            primaryStage.setY((primScreenBounds.getHeight() - primaryStage.getHeight()) / 2);
        } else {
            // Place window at matrix (0, 0) of screen.
            primaryStage.setX(0);
            primaryStage.setY(0);
            // Set window to full screen.
            primaryStage.setHeight(primScreenBounds.getHeight());
            primaryStage.setWidth(primScreenBounds.getWidth());
        }
    }

    @FXML
    void minimizeIconClicked() {
//        ((Stage) rootPane.getScene().getWindow()).setIconified(true);
        primaryStage.setIconified(true);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        appNameLabel.setText(Labels.APP_NAME);
        appNameLabel.setFont(new Font(48));
        windowHeader.setOnMousePressed(
                event -> {
                    xOffset = primaryStage.getX() - event.getScreenX();
                    yOffset = primaryStage.getY() - event.getScreenY();
                });
        windowHeader.setOnMouseDragged(
                event -> {
                    primaryStage.setX(event.getScreenX() + xOffset);
                    primaryStage.setY(event.getScreenY() + yOffset);
                });

        initializeLoader();

        ScrollUtils.animateScrollBars(scrollPane);

        ScrollUtils.addSmoothScrolling(scrollPane);
    }

    public void initializeLoader() {
        navigation = Navigation.getInstance(contentPane);
        navBar.getChildren().add(navigation.createNavigation());

//    navBar.setStyle("-fx-background-color: red;");

        MFXButton settings = createToggle("fas-gears", "Settings");
        settings.setOnAction(e -> navigation.navigate(Pages.getSettingsPane()));
        settingsHolder.getChildren().add(settings);
    }
}
