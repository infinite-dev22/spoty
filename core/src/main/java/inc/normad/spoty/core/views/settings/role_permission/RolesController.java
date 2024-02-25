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

package inc.normad.spoty.core.views.settings.role_permission;

import inc.normad.spoty.core.components.navigation.Navigation;
import inc.normad.spoty.core.components.navigation.Pages;
import inc.normad.spoty.core.viewModels.RoleViewModel;
import inc.normad.spoty.network_bridge.dtos.Role;
import inc.normad.spoty.utils.SpotyThreader;
import io.github.palexdev.materialfx.controls.*;
import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;
import io.github.palexdev.materialfx.filter.StringFilter;
import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

import java.net.URL;
import java.util.Comparator;
import java.util.Objects;
import java.util.ResourceBundle;

@SuppressWarnings("unchecked")
public class RolesController implements Initializable {
    private static RolesController instance;
    @FXML
    public MFXButton roleImportBtn;
    @FXML
    public HBox roleActionsPane;
    @FXML
    public MFXTextField roleSearchBar;
    @FXML
    public BorderPane roleContentPane;
    @FXML
    private MFXTableView<Role> roleTable;

    public static RolesController getInstance() {
        if (Objects.equals(instance, null)) instance = new RolesController();
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

        roleMasterRole.prefWidthProperty().bind(roleTable.widthProperty().multiply(.5));
        roleMasterDescription.prefWidthProperty().bind(roleTable.widthProperty().multiply(.5));

        roleTable.getTableColumns().addAll(roleMasterRole, roleMasterDescription);

        roleTable
                .getFilters()
                .addAll(
                        new StringFilter<>("Name", Role::getName),
                        new StringFilter<>("Description", Role::getDescription));

        getRoleTable();

        if (RoleViewModel.getRoles().isEmpty()) {
            RoleViewModel.getRoles()
                    .addListener(
                            (ListChangeListener<Role>) c -> roleTable.setItems(RoleViewModel.getRoles()));
        } else {
            roleTable.itemsProperty().bindBidirectional(RoleViewModel.rolesProperty());
        }
    }

    private void getRoleTable() {
        roleTable.setPrefSize(1200, 1000);
        roleTable.features().enableBounceEffect();
        roleTable.autosizeColumnsOnInitialization();
        roleTable.features().enableSmoothScrolling(0.5);

        roleTable.setTableRowFactory(
                t -> {
                    MFXTableRow<Role> row = new MFXTableRow<>(roleTable, t);
                    EventHandler<ContextMenuEvent> eventHandler =
                            event ->
                                    showContextMenu((MFXTableRow<Role>) event.getSource())
                                            .show(
                                                    roleContentPane.getScene().getWindow(),
                                                    event.getScreenX(),
                                                    event.getScreenY());
                    row.setOnContextMenuRequested(eventHandler);
                    return row;
                });
    }

    private MFXContextMenu showContextMenu(MFXTableRow<Role> obj) {
        MFXContextMenu contextMenu = new MFXContextMenu(roleTable);

        MFXContextMenuItem delete = new MFXContextMenuItem("Delete");
        MFXContextMenuItem edit = new MFXContextMenuItem("Edit");

        // Actions
        // Delete
        delete.setOnAction(
                e -> {
                    SpotyThreader.spotyThreadPool(() -> {
                        try {
                            RoleViewModel.deleteItem(obj.getData().getId());
                        } catch (Exception ex) {
                            throw new RuntimeException(ex);
                        }
                    });
                    e.consume();
                });
        // Edit
        edit.setOnAction(
                e -> {
                    SpotyThreader.spotyThreadPool(() -> {
                        try {
                            RoleViewModel.getItem(obj.getData().getId());
                        } catch (Exception ex) {
                            throw new RuntimeException(ex);
                        }
                    });
                    roleCreateBtnClicked();
                    e.consume();
                });

        contextMenu.addItems(edit, delete);

        return contextMenu;
    }

    public void roleCreateBtnClicked() {
        Navigation.navigate(Pages.getRoleSettingsFormPane(), (StackPane) roleContentPane.getParent());
    }
}
