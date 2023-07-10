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

package org.infinite.spoty.views.people.users;

import static org.infinite.spoty.SpotResourceLoader.fxmlLoader;

import io.github.palexdev.materialfx.controls.*;
import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;
import io.github.palexdev.materialfx.dialogs.MFXGenericDialog;
import io.github.palexdev.materialfx.dialogs.MFXGenericDialogBuilder;
import io.github.palexdev.materialfx.dialogs.MFXStageDialog;
import io.github.palexdev.materialfx.enums.ScrimPriority;
import io.github.palexdev.materialfx.filter.BooleanFilter;
import io.github.palexdev.materialfx.filter.StringFilter;
import java.io.IOException;
import java.net.URL;
import java.util.Comparator;
import java.util.ResourceBundle;
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
import org.infinite.spoty.database.models.User;
import org.infinite.spoty.forms.UserFormController;
import org.infinite.spoty.viewModels.UserViewModel;

@SuppressWarnings("unchecked")
public class UserController implements Initializable {
  private static UserController instance;
  @FXML public MFXTextField userSearchBar;
  @FXML public HBox userActionsPane;
  @FXML public MFXButton userImportBtn;
  @FXML public BorderPane userContentPane;
  @FXML public MFXTableView<User> userTable;
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
    MFXTableColumn<User> firstName =
        new MFXTableColumn<>("First Name", false, Comparator.comparing(User::getFirstName));
    MFXTableColumn<User> lastName =
        new MFXTableColumn<>("Last Name", false, Comparator.comparing(User::getLastName));
    MFXTableColumn<User> userName =
        new MFXTableColumn<>("User Name", false, Comparator.comparing(User::getUserName));
    MFXTableColumn<User> userEmail =
        new MFXTableColumn<>("Email", false, Comparator.comparing(User::getEmail));
    MFXTableColumn<User> userPhone =
        new MFXTableColumn<>("Phone", false, Comparator.comparing(User::getPhone));
    MFXTableColumn<User> status =
        new MFXTableColumn<>("Status", false, Comparator.comparing(User::isActive));

    firstName.setRowCellFactory(user -> new MFXTableRowCell<>(User::getFirstName));
    lastName.setRowCellFactory(user -> new MFXTableRowCell<>(User::getLastName));
    userName.setRowCellFactory(user -> new MFXTableRowCell<>(User::getUserName));
    userEmail.setRowCellFactory(user -> new MFXTableRowCell<>(User::getEmail));
    userPhone.setRowCellFactory(user -> new MFXTableRowCell<>(User::getPhone));
    status.setRowCellFactory(user -> new MFXTableRowCell<>(User::isActive));

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
            new StringFilter<>("First Name", User::getFirstName),
            new StringFilter<>("Last Name", User::getLastName),
            new StringFilter<>("User Name", User::getUserName),
            new StringFilter<>("Email", User::getEmail),
            new StringFilter<>("Phone", User::getPhone),
            new BooleanFilter<>("Status", User::isActive));
    styleUserTable();

    if (UserViewModel.getUsers().isEmpty()) {
      UserViewModel.getUsers()
          .addListener(
              (ListChangeListener<User>) c -> userTable.setItems(UserViewModel.getUsers()));
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
          MFXTableRow<User> row = new MFXTableRow<>(userTable, t);
          EventHandler<ContextMenuEvent> eventHandler =
              event -> {
                showContextMenu((MFXTableRow<User>) event.getSource())
                    .show(userTable.getScene().getWindow(), event.getScreenX(), event.getScreenY());
                event.consume();
              };
          row.setOnContextMenuRequested(eventHandler);
          return row;
        });
  }

  private MFXContextMenu showContextMenu(MFXTableRow<User> obj) {
    MFXContextMenu contextMenu = new MFXContextMenu(userTable);
    MFXContextMenuItem delete = new MFXContextMenuItem("Delete");
    MFXContextMenuItem edit = new MFXContextMenuItem("Edit");

    // Actions
    // Delete
    delete.setOnAction(
        e -> {
          UserViewModel.deleteItem(obj.getData().getId());
          e.consume();
        });
    // Edit
    edit.setOnAction(
        e -> {
          UserViewModel.getItem(obj.getData().getId());
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
