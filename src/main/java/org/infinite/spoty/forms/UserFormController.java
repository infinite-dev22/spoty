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

import io.github.palexdev.materialfx.controls.*;
import io.github.palexdev.mfxcomponents.controls.buttons.MFXFilledButton;
import io.github.palexdev.mfxcomponents.controls.buttons.MFXOutlinedButton;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.util.StringConverter;
import javafx.util.converter.NumberStringConverter;
import org.infinite.spoty.database.models.Branch;
import org.infinite.spoty.database.models.Role;
import org.infinite.spoty.viewModels.BranchViewModel;
import org.infinite.spoty.viewModels.UserViewModel;

import java.net.URL;
import java.util.ResourceBundle;

import static org.infinite.spoty.GlobalActions.closeDialog;

public class UserFormController implements Initializable {
    public MFXTextField userID = new MFXTextField();
    @FXML
    public MFXFilledButton userFormSaveBtn;
    @FXML
    public MFXOutlinedButton userFormCancelBtn;
    @FXML
    public Label userFormTitle;
    @FXML
    public MFXTextField userFormEmail;
    @FXML
    public MFXTextField userFormPhone;
    @FXML
    public MFXTextField userFormFirstname;
    @FXML
    public MFXTextField userFormLastname;
    @FXML
    public MFXTextField userFormUsername;
    @FXML
    public MFXPasswordField userFormPassword;
    @FXML
    public MFXFilterComboBox<Role> userFormRole;
    @FXML
    public MFXFilterComboBox<Branch> userFormBranch;
    @FXML
    public MFXToggleButton userFormActive;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Input listeners.
        userFormFirstname.textProperty().addListener((observable, oldValue, newValue) -> userFormFirstname.setTrailingIcon(null));
        userFormLastname.textProperty().addListener((observable, oldValue, newValue) -> userFormLastname.setTrailingIcon(null));
        userFormUsername.textProperty().addListener((observable, oldValue, newValue) -> userFormUsername.setTrailingIcon(null));
//        userFormEmail.textProperty().addListener((observable, oldValue, newValue) -> userFormEmail.setTrailingIcon(null));
//        userFormPhone.textProperty().addListener((observable, oldValue, newValue) -> userFormPhone.setTrailingIcon(null));
//        userFormTown.textProperty().addListener((observable, oldValue, newValue) -> userFormTown.setTrailingIcon(null));
//        userFormCity.textProperty().addListener((observable, oldValue, newValue) -> userFormCity.setTrailingIcon(null));
//        userFormTaxNumber.textProperty().addListener((observable, oldValue, newValue) -> userFormTaxNumber.setTrailingIcon(null));
//        userFormAddress.textProperty().addListener((observable, oldValue, newValue) -> userFormAddress.setTrailingIcon(null));
        // Input bindings.
        userID.textProperty().bindBidirectional(UserViewModel.idProperty(), new NumberStringConverter());
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
        userFormRole.setConverter(new StringConverter<>() {
            @Override
            public String toString(Role object) {
                if (object != null)
                    return object.getName();
                else
                    return null;
            }

            @Override
            public Role fromString(String string) {
                return null;
            }
        });
        userFormBranch.setItems(BranchViewModel.branchesList);
        userFormBranch.setConverter(new StringConverter<>() {
            @Override
            public String toString(Branch object) {
                if (object != null)
                    return object.getName();
                else
                    return null;
            }

            @Override
            public Branch fromString(String string) {
                return null;
            }
        });
        dialogOnActions();
    }

    private void dialogOnActions() {
        userFormCancelBtn.setOnAction((e) -> {
            closeDialog(e);
            userFormFirstname.setText("");
            userFormLastname.setText("");
            userFormUsername.setText("");
            userFormEmail.setText("");
            userFormPhone.setText("");
            userFormPassword.setText("");
            userFormRole.setText("");
            userFormBranch.setText("");
            userFormActive.setSelected(true);

            userFormFirstname.setTrailingIcon(null);
            userFormLastname.setTrailingIcon(null);
            userFormUsername.setTrailingIcon(null);
            userFormEmail.setTrailingIcon(null);
            userFormPhone.setTrailingIcon(null);
            userFormPassword.setLeadingIcon(null);
            userFormRole.setLeadingIcon(null);
            userFormBranch.setLeadingIcon(null);
        });
        userFormSaveBtn.setOnAction((e) -> {
            MFXIconWrapper icon = new MFXIconWrapper("fas-circle-exclamation", 20, Color.RED, 20);

            if (userFormFirstname.getText().length() == 0) {
                userFormFirstname.setTrailingIcon(icon);
            }
            if (userFormLastname.getText().length() == 0) {
                userFormLastname.setTrailingIcon(icon);
            }
            if (userFormUsername.getText().length() == 0) {
                userFormUsername.setTrailingIcon(icon);
            }
//            if (userFormEmail.getText().length() == 0) {
//                userFormEmail.setTrailingIcon(icon);
//            }
//            if (userFormPhone.getText().length() == 0) {
//                userFormPhone.setTrailingIcon(icon);
//            }
            // Should be set by user i.e.
            // User is created by admin, User tries to log in and is asked to set a password.
            if (userFormPassword.getText().length() == 0) {
                userFormPassword.setLeadingIcon(icon);
            }
            if (userFormRole.getText().length() == 0) {
                userFormRole.setLeadingIcon(icon);
            }
            if (userFormBranch.getText().length() == 0) {
                userFormBranch.setLeadingIcon(icon);
            }
            if (userFormFirstname.getText().length() > 0
                    && userFormLastname.getText().length() > 0
                    && userFormUsername.getText().length() > 0
                    && userFormEmail.getText().length() > 0
                    && userFormPhone.getText().length() > 0
                    && userFormPassword.getText().length() > 0
                    && userFormRole.getText().length() > 0
                    && userFormBranch.getText().length() > 0) {
                if (Integer.parseInt(userID.getText()) > 0)
                    UserViewModel.updateItem(Integer.parseInt(userID.getText()));
                else
                    UserViewModel.saveUser();
                closeDialog(e);
                UserViewModel.resetProperties();
            }
        });
    }
}
