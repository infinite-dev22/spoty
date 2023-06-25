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
import static org.infinite.spoty.Validators.*;

import io.github.palexdev.materialfx.controls.MFXFilterComboBox;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.materialfx.controls.MFXToggleButton;
import io.github.palexdev.mfxcomponents.controls.buttons.MFXFilledButton;
import io.github.palexdev.mfxcomponents.controls.buttons.MFXOutlinedButton;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.util.StringConverter;
import javafx.util.converter.NumberStringConverter;
import org.infinite.spoty.database.models.Branch;
import org.infinite.spoty.database.models.Role;
import org.infinite.spoty.viewModels.BranchViewModel;
import org.infinite.spoty.viewModels.UserViewModel;

public class UserFormController implements Initializable {
  public MFXTextField userID = new MFXTextField();
  @FXML public MFXFilledButton userFormSaveBtn;
  @FXML public MFXOutlinedButton userFormCancelBtn;
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
    //        userFormBranch.valueProperty().bindBidirectional(UserViewModel.roleProperty());
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
    userFormBranch.setItems(BranchViewModel.branchesList);
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
          if (!userFormFirstNameValidationLabel.isVisible()
              && !userFormLastNameValidationLabel.isVisible()
              && !userFormUserNameValidationLabel.isVisible()
              && !userFormBranchValidationLabel.isVisible()
              && !userFormRoleValidationLabel.isVisible()
              && !userFormEmailValidationLabel.isVisible()
              && !userFormPhoneValidationLabel.isVisible()) {
            if (Integer.parseInt(userID.getText()) > 0)
              UserViewModel.updateItem(Integer.parseInt(userID.getText()));
            else UserViewModel.saveUser();
            closeDialog(e);
            UserViewModel.resetProperties();
          }
        });
  }
}
