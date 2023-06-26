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

import static org.infinite.spoty.Utils.createToggle;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXScrollPane;
import io.github.palexdev.materialfx.utils.ScrollUtils;
import io.github.palexdev.mfxresources.fonts.MFXFontIcon;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import org.infinite.spoty.components.navigation.Navigation;
import org.infinite.spoty.components.navigation.Pages;
import org.infinite.spoty.values.strings.Labels;

public class BaseController implements Initializable {
  public static Navigation navigation;
  private static BaseController instance;
  public final Stage stage;
  @FXML public MFXFontIcon closeIcon;
  @FXML public MFXFontIcon maximizeIcon;
  @FXML public MFXFontIcon minimizeIcon;
  @FXML public StackPane contentPane;
  @FXML public StackPane navBar;
  @FXML public HBox windowHeader;
  @FXML public MFXScrollPane scrollPane;
  @FXML public AnchorPane rootPane;
  @FXML public VBox settingsHolder;
  @FXML public Label appNameLabel;
  private double xOffset;
  private double yOffset;

  private BaseController(Stage stage) {
    this.stage = stage;
  }

  public static BaseController getInstance(Stage stage) {
    if (instance == null) instance = new BaseController(stage);
    return instance;
  }

  @FXML
  void closeIconClicked() {
    Platform.exit();
    System.exit(0);
  }

  @FXML
  void maximizeIconClicked() {
    ((Stage) rootPane.getScene().getWindow())
        .setMaximized(!((Stage) rootPane.getScene().getWindow()).isMaximized());
  }

  @FXML
  void minimizeIconClicked() {
    ((Stage) rootPane.getScene().getWindow()).setIconified(true);
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    appNameLabel.setText(Labels.APP_NAME);
    appNameLabel.setFont(new Font(48));
    windowHeader.setOnMousePressed(
        event -> {
          xOffset = stage.getX() - event.getScreenX();
          yOffset = stage.getY() - event.getScreenY();
        });
    windowHeader.setOnMouseDragged(
        event -> {
          stage.setX(event.getScreenX() + xOffset);
          stage.setY(event.getScreenY() + yOffset);
        });

    initializeLoader();

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
