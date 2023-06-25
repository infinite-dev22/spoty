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

package org.infinite.spoty.views.settings.roles;

import static org.infinite.spoty.data.SampleData.roleMasterSampleData;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTableColumn;
import io.github.palexdev.materialfx.controls.MFXTableView;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;
import io.github.palexdev.materialfx.filter.StringFilter;
import java.net.URL;
import java.util.Comparator;
import java.util.Objects;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import org.infinite.spoty.components.navigation.Navigation;
import org.infinite.spoty.components.navigation.Pages;
import org.infinite.spoty.database.models.Role;

public class RoleController implements Initializable {
  private static RoleController instance;
  @FXML public MFXButton roleImportBtn;
  @FXML public HBox roleActionsPane;
  @FXML public MFXTextField roleSearchBar;
  @FXML public BorderPane roleContentPane;
  @FXML private MFXTableView<Role> roleMasterTable;

  public static RoleController getInstance() {
    if (Objects.equals(instance, null)) instance = new RoleController();
    return instance;
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    Platform.runLater(this::setupTable);
  }

  private void setupTable() {
    MFXTableColumn<Role> roleMasterRole =
        new MFXTableColumn<>("Name", true, Comparator.comparing(Role::getName));
    MFXTableColumn<Role> roleMasterDescription =
        new MFXTableColumn<>("Description", true, Comparator.comparing(Role::getDescription));

    roleMasterRole.setRowCellFactory(roleMaster -> new MFXTableRowCell<>(Role::getName));
    roleMasterDescription.setRowCellFactory(
        roleMaster -> new MFXTableRowCell<>(Role::getDescription));

    roleMasterTable.getTableColumns().addAll(roleMasterRole, roleMasterDescription);
    roleMasterTable
        .getFilters()
        .addAll(
            new StringFilter<>("Name", Role::getName),
            new StringFilter<>("Description", Role::getDescription));
    getNameTable();
    roleMasterTable.setItems(roleMasterSampleData());
  }

  private void getNameTable() {
    roleMasterTable.setPrefSize(1200, 1000);
    roleMasterTable.features().enableBounceEffect();
    roleMasterTable.autosizeColumnsOnInitialization();
    roleMasterTable.features().enableSmoothScrolling(0.5);
  }

  public void roleCreateBtnClicked() {
    Navigation.navigate(Pages.getRoleSettingsFormPane(), (StackPane) roleContentPane.getParent());
  }
}
