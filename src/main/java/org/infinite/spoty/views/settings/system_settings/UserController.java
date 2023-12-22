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

package org.infinite.spoty.views.settings.system_settings;

import io.github.palexdev.materialfx.controls.*;
import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;
import io.github.palexdev.materialfx.dialogs.MFXGenericDialog;
import io.github.palexdev.materialfx.dialogs.MFXGenericDialogBuilder;
import io.github.palexdev.materialfx.dialogs.MFXStageDialog;
import io.github.palexdev.materialfx.enums.ScrimPriority;
import io.github.palexdev.materialfx.filter.BooleanFilter;
import io.github.palexdev.materialfx.filter.StringFilter;
import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.infinite.spoty.data_source.daos.UserProfile;
import org.infinite.spoty.forms.UserFormController;
import org.infinite.spoty.utils.SpotyThreader;
import org.infinite.spoty.viewModels.UserViewModel;

import java.io.IOException;
import java.net.URL;
import java.util.Comparator;
import java.util.ResourceBundle;

import static org.infinite.spoty.SpotyResourceLoader.fxmlLoader;

@SuppressWarnings("unchecked")
public class UserController implements Initializable {
    private static UserController instance;
    @FXML
    public MFXTextField userSearchBar;
    @FXML
    public HBox userActionsPane;
    @FXML
    public MFXButton userImportBtn;
    @FXML
    public BorderPane userContentPane;
    @FXML
    public MFXTableView<UserProfile> userTable;
    private MFXStageDialog dialog;

    private UserController(Stage stage) {
        Platform.runLater(
                () -> {
                    try {
                        userFormDialogPane(stage);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                });
    }

    public static UserController getInstance(Stage stage) {
        if (instance == null) instance = new UserController(stage);
        return instance;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Platform.runLater(this::setupTable);
    }

    private void setupTable() {
        MFXTableColumn<UserProfile> firstName =
                new MFXTableColumn<>("First Name", false, Comparator.comparing(UserProfile::getFirstName));
        MFXTableColumn<UserProfile> lastName =
                new MFXTableColumn<>("Last Name", false, Comparator.comparing(UserProfile::getLastName));
        MFXTableColumn<UserProfile> userName =
                new MFXTableColumn<>("Other Name", false, Comparator.comparing(UserProfile::getOtherName));
        MFXTableColumn<UserProfile> userEmail =
                new MFXTableColumn<>("Email", false, Comparator.comparing(UserProfile::getEmail));
        MFXTableColumn<UserProfile> userPhone =
                new MFXTableColumn<>("Phone", false, Comparator.comparing(UserProfile::getPhone));
        MFXTableColumn<UserProfile> status =
                new MFXTableColumn<>("Status", false, Comparator.comparing(UserProfile::isActive));

        firstName.setRowCellFactory(user -> new MFXTableRowCell<>(UserProfile::getFirstName));
        lastName.setRowCellFactory(user -> new MFXTableRowCell<>(UserProfile::getLastName));
        userName.setRowCellFactory(user -> new MFXTableRowCell<>(UserProfile::getOtherName));
        userEmail.setRowCellFactory(user -> new MFXTableRowCell<>(UserProfile::getEmail));
        userPhone.setRowCellFactory(user -> new MFXTableRowCell<>(UserProfile::getPhone));
        status.setRowCellFactory(user -> new MFXTableRowCell<>(UserProfile::isActive));

        firstName.prefWidthProperty().bind(userTable.widthProperty().multiply(.167));
        lastName.prefWidthProperty().bind(userTable.widthProperty().multiply(.167));
        userName.prefWidthProperty().bind(userTable.widthProperty().multiply(.167));
        userEmail.prefWidthProperty().bind(userTable.widthProperty().multiply(.167));
        userPhone.prefWidthProperty().bind(userTable.widthProperty().multiply(.167));
        status.prefWidthProperty().bind(userTable.widthProperty().multiply(.167));

        userTable.getTableColumns().addAll(firstName, lastName, userName, userEmail, userPhone, status);
        userTable
                .getFilters()
                .addAll(
                        new StringFilter<>("First Name", UserProfile::getFirstName),
                        new StringFilter<>("Last Name", UserProfile::getLastName),
                        new StringFilter<>("Other Name", UserProfile::getOtherName),
                        new StringFilter<>("Email", UserProfile::getEmail),
                        new StringFilter<>("Phone", UserProfile::getPhone),
                        new BooleanFilter<>("Status", UserProfile::isActive));
        styleUserTable();

        if (UserViewModel.getUserProfiles().isEmpty()) {
            UserViewModel.getUserProfiles()
                    .addListener(
                            (ListChangeListener<UserProfile>) c -> userTable.setItems(UserViewModel.getUserProfiles()));
        } else {
            userTable.itemsProperty().bindBidirectional(UserViewModel.usersProperty());
        }
    }

    private void styleUserTable() {
        userTable.setPrefSize(1000, 1000);
        userTable.features().enableBounceEffect();
        userTable.features().enableSmoothScrolling(0.5);

        userTable.setTableRowFactory(
                t -> {
                    MFXTableRow<UserProfile> row = new MFXTableRow<>(userTable, t);
                    EventHandler<ContextMenuEvent> eventHandler =
                            event -> {
                                showContextMenu((MFXTableRow<UserProfile>) event.getSource())
                                        .show(userTable.getScene().getWindow(), event.getScreenX(), event.getScreenY());
                                event.consume();
                            };
                    row.setOnContextMenuRequested(eventHandler);
                    return row;
                });
    }

    private MFXContextMenu showContextMenu(MFXTableRow<UserProfile> obj) {
        MFXContextMenu contextMenu = new MFXContextMenu(userTable);
        MFXContextMenuItem delete = new MFXContextMenuItem("Delete");
        MFXContextMenuItem edit = new MFXContextMenuItem("Edit");

        // Actions
        // Delete
        delete.setOnAction(
                e -> {
                    SpotyThreader.spotyThreadPool(() -> {
                        try {
                            UserViewModel.deleteItem(obj.getData().getId());
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
                            UserViewModel.getItem(obj.getData().getId());
                        } catch (Exception ex) {
                            throw new RuntimeException(ex);
                        }
                    });
                    dialog.showAndWait();
                    e.consume();
                });

        contextMenu.addItems(edit, delete);

        return contextMenu;
    }

    private void userFormDialogPane(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = fxmlLoader("forms/UserForm.fxml");
        fxmlLoader.setControllerFactory(c -> UserFormController.getInstance());

        MFXGenericDialog dialogContent = fxmlLoader.load();

        dialogContent.setShowMinimize(false);
        dialogContent.setShowAlwaysOnTop(false);

        dialog =
                MFXGenericDialogBuilder.build(dialogContent)
                        .toStageDialogBuilder()
                        .initOwner(stage)
                        .initModality(Modality.WINDOW_MODAL)
                        .setOwnerNode(userContentPane)
                        .setScrimPriority(ScrimPriority.WINDOW)
                        .setScrimOwner(true)
                        .get();

        io.github.palexdev.mfxcomponents.theming.MaterialThemes.PURPLE_LIGHT.applyOn(dialog.getScene());
    }

    public void userCreateBtnClicked() {
        dialog.showAndWait();
    }
}
