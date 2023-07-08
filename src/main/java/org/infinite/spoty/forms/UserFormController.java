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

package org.infinite.spoty.forms;

import static org.infinite.spoty.GlobalActions.closeDialog;
import static org.infinite.spoty.Validators.emailValidator;
import static org.infinite.spoty.Validators.lengthValidator;
import static org.infinite.spoty.Validators.requiredValidator;

import io.github.palexdev.materialfx.controls.MFXFilterComboBox;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.materialfx.controls.MFXToggleButton;
import io.github.palexdev.mfxcomponents.controls.buttons.MFXButton;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import javafx.util.converter.NumberStringConverter;
import org.infinite.spoty.components.notification.SimpleNotification;
import org.infinite.spoty.components.notification.SimpleNotificationHolder;
import org.infinite.spoty.components.notification.enums.NotificationDuration;
import org.infinite.spoty.components.notification.enums.NotificationVariants;
import org.infinite.spoty.database.models.Branch;
import org.infinite.spoty.database.models.Role;
import org.infinite.spoty.viewModels.BranchViewModel;
import org.infinite.spoty.viewModels.UserViewModel;
import org.infinite.spoty.views.people.users.UserController;

public class UserFormController implements Initializable {
  public static UserFormController instance;
  private final Stage stage;
  public MFXTextField userID = new MFXTextField();
  @FXML public MFXButton userFormSaveBtn;
  @FXML public MFXButton userFormCancelBtn;
  @FXML public Label userFormTitle;
  @FXML public MFXTextField userFormEmail;
  @FXML public MFXTextField userFormPhone;
  @FXML public MFXTextField userFormFirstname;
  @FXML public MFXTextField userFormLastname;
  @FXML public MFXTextField userFormUsername;
  @FXML public MFXFilterComboBox<Role> userFormRole;
  @FXML public MFXFilterComboBox<Branch> userFormBranch;
  @FXML public MFXToggleButton userFormActive;
  @FXML public Label userFormFirstNameValidationLabel;
  @FXML public Label userFormEmailValidationLabel;
  @FXML public Label userFormPhoneValidationLabel;
  @FXML public Label userFormLastNameValidationLabel;
  @FXML public Label userFormUserNameValidationLabel;
  @FXML public Label userFormBranchValidationLabel;
  @FXML public Label userFormRoleValidationLabel;

  public UserFormController(Stage stage) {
    this.stage = stage;
  }

  public static UserFormController getInstance(Stage stage) {
    if (Objects.equals(instance, null)) instance = new UserFormController(stage);
    return instance;
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    // Input listeners.
    userFormPhone
        .textProperty()
        .addListener(
            (observable, oldValue, newValue) -> {
              if (!newValue.matches("\\d*")) userFormPhone.setText(newValue.replaceAll("\\D", ""));
            });
    userFormPhone
        .focusedProperty()
        .addListener(
            (observable, oldValue, newValue) -> {
              if (newValue != oldValue) userFormPhone.setLeadingIcon(new Label("+"));
              System.out.println("newValue oldValue");
            });

    // Input bindings.
    userID
        .textProperty()
        .bindBidirectional(UserViewModel.idProperty(), new NumberStringConverter());
    userFormFirstname.textProperty().bindBidirectional(UserViewModel.firstNameProperty());
    userFormLastname.textProperty().bindBidirectional(UserViewModel.lastNameProperty());
    userFormUsername.textProperty().bindBidirectional(UserViewModel.userNameProperty());
    userFormEmail.textProperty().bindBidirectional(UserViewModel.emailProperty());
    userFormPhone.textProperty().bindBidirectional(UserViewModel.phoneProperty());
    userFormRole.valueProperty().bindBidirectional(UserViewModel.roleProperty());
    //            userFormBranch.valueProperty().bindBidirectional(UserViewModel.roleProperty());
    userFormActive.selectedProperty().bindBidirectional(UserViewModel.activeProperty());

    // Combo box properties.
    //        userFormRole.setItems(UserViewModel.usersList);
    userFormRole.setConverter(
        new StringConverter<>() {
          @Override
          public String toString(Role object) {
            if (object != null) return object.getName();
            else return null;
          }

          @Override
          public Role fromString(String string) {
            return null;
          }
        });

    userFormBranch.setItems(BranchViewModel.getBranchesComboBoxList());
    userFormBranch.setOnShowing(
        e -> userFormBranch.setItems(BranchViewModel.getBranchesComboBoxList()));
    userFormBranch.setConverter(
        new StringConverter<>() {
          @Override
          public String toString(Branch object) {
            if (object != null) return object.getName();
            else return null;
          }

          @Override
          public Branch fromString(String string) {
            return null;
          }
        });
    // Input validations.
    // Name input validation.
    requiredValidator(
        userFormFirstname, "First name is required.", userFormFirstNameValidationLabel);
    requiredValidator(userFormLastname, "Last name is required.", userFormLastNameValidationLabel);
    requiredValidator(userFormUsername, "Username is required.", userFormUserNameValidationLabel);
    requiredValidator(userFormBranch, "Branch is required.", userFormBranchValidationLabel);
    requiredValidator(userFormRole, "User role is required.", userFormRoleValidationLabel);
    // Email input validation.
    emailValidator(userFormEmail, userFormEmailValidationLabel);
    // Phone input validation.
    lengthValidator(userFormPhone, 11, "Invalid Phone length", userFormPhoneValidationLabel);
    dialogOnActions();
  }

  private void dialogOnActions() {
    userFormCancelBtn.setOnAction(
        (e) -> {
          closeDialog(e);
          UserViewModel.resetProperties();

          userFormRole.clearSelection();
          userFormBranch.clearSelection();

          userFormFirstNameValidationLabel.setVisible(false);
          userFormLastNameValidationLabel.setVisible(false);
          userFormUserNameValidationLabel.setVisible(false);
          userFormBranchValidationLabel.setVisible(false);
          userFormRoleValidationLabel.setVisible(false);
          userFormEmailValidationLabel.setVisible(false);
          userFormPhoneValidationLabel.setVisible(false);
        });
    userFormSaveBtn.setOnAction(
        (e) -> {
          SimpleNotificationHolder notificationHolder = SimpleNotificationHolder.getInstance();
          if (!userFormFirstNameValidationLabel.isVisible()
              && !userFormLastNameValidationLabel.isVisible()
              && !userFormUserNameValidationLabel.isVisible()
              && !userFormBranchValidationLabel.isVisible()
              && !userFormRoleValidationLabel.isVisible()
              && !userFormEmailValidationLabel.isVisible()
              && !userFormPhoneValidationLabel.isVisible()) {
            if (Integer.parseInt(userID.getText()) > 0) {
              UserViewModel.updateItem(Integer.parseInt(userID.getText()));
              SimpleNotification notification =
                  new SimpleNotification.NotificationBuilder("User updated successfully")
                      .duration(NotificationDuration.SHORT)
                      .icon("fas-circle-check")
                      .type(NotificationVariants.SUCCESS)
                      .build();
              notificationHolder.addNotification(notification);

              userFormRole.clearSelection();
              userFormBranch.clearSelection();
              UserController.getInstance(stage).userTable.setItems(UserViewModel.usersList);

              closeDialog(e);
              return;
            }
            UserViewModel.saveUser();
            SimpleNotification notification =
                new SimpleNotification.NotificationBuilder("User saved successfully")
                    .duration(NotificationDuration.SHORT)
                    .icon("fas-circle-check")
                    .type(NotificationVariants.SUCCESS)
                    .build();
            notificationHolder.addNotification(notification);

            userFormRole.clearSelection();
            userFormBranch.clearSelection();
            UserController.getInstance(stage).userTable.setItems(UserViewModel.usersList);

            closeDialog(e);
            return;
          }
          SimpleNotification notification =
              new SimpleNotification.NotificationBuilder("Required fields missing")
                  .duration(NotificationDuration.SHORT)
                  .icon("fas-triangle-exclamation")
                  .type(NotificationVariants.ERROR)
                  .build();
          notificationHolder.addNotification(notification);
        });
  }
}
