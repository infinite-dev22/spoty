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

package org.infinite.spoty.views.settings;

import static org.infinite.spoty.Utils.createToggle;

import io.github.palexdev.materialfx.controls.MFXButton;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import org.infinite.spoty.components.navigation.Navigation;
import org.infinite.spoty.components.navigation.Pages;

public class SettingsController implements Initializable {
  private static SettingsController instance;
  @FXML public VBox settingsNavbar;
  @FXML public StackPane contentPane;

  public static SettingsController getInstance() {
    if (instance == null) instance = new SettingsController();
    return instance;
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    MFXButton system = createToggle("fas-computer", "System");
    MFXButton pos = createToggle("fas-bag-shopping", "POS");
    MFXButton role = createToggle("fas-user-shield", "Roles");
    MFXButton branches = createToggle("fas-store", "Branches");
    MFXButton currency = createToggle("fas-dollar-sign", "Currency");
    MFXButton export = createToggle("fas-arrow-up-from-bracket", "Export");

    system.setOnAction(e -> Navigation.navigate(Pages.getSystemSettingsPane(), contentPane));
    pos.setOnAction(e -> Navigation.navigate(Pages.getPosSettingsPane(), contentPane));
    role.setOnAction(e -> Navigation.navigate(Pages.getRoleSettingsPane(), contentPane));
    branches.setOnAction(e -> Navigation.navigate(Pages.getBranchSettingsPane(), contentPane));
    currency.setOnAction(e -> Navigation.navigate(Pages.getCurrencySettingsPane(), contentPane));
    export.setOnAction(e -> Navigation.navigate(Pages.getExportSettingsPane(), contentPane));

    settingsNavbar.getChildren().setAll(system, pos, role, branches, currency, export);
  }
}
