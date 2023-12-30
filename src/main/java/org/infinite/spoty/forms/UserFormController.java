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

import io.github.palexdev.materialfx.controls.MFXFilterComboBox;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.materialfx.controls.MFXToggleButton;
import io.github.palexdev.materialfx.utils.StringUtils;
import io.github.palexdev.materialfx.utils.others.FunctionalStringConverter;
import io.github.palexdev.mfxcomponents.controls.buttons.MFXButton;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.util.StringConverter;
import org.infinite.spoty.components.notification.SimpleNotification;
import org.infinite.spoty.components.notification.SimpleNotificationHolder;
import org.infinite.spoty.components.notification.enums.NotificationDuration;
import org.infinite.spoty.components.notification.enums.NotificationVariants;
import org.infinite.spoty.data_source.daos.Branch;
import org.infinite.spoty.data_source.daos.Role;
import org.infinite.spoty.utils.SpotyLogger;
import org.infinite.spoty.utils.SpotyThreader;
import org.infinite.spoty.viewModels.BranchViewModel;
import org.infinite.spoty.viewModels.RoleViewModel;
import org.infinite.spoty.viewModels.UserViewModel;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.function.Function;
import java.util.function.Predicate;

import static org.infinite.spoty.GlobalActions.closeDialog;
import static org.infinite.spoty.Validators.*;

public class UserFormController implements Initializable {
    public static UserFormController instance;
    @FXML
    public MFXButton userFormSaveBtn;
    @FXML
    public MFXButton userFormCancelBtn;
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
    public MFXFilterComboBox<Role> userFormRole;
    @FXML
    public MFXFilterComboBox<Branch> userFormBranch;
    @FXML
    public MFXToggleButton userFormActive;
    @FXML
    public Label userFormFirstNameValidationLabel;
    @FXML
    public Label userFormEmailValidationLabel;
    @FXML
    public Label userFormPhoneValidationLabel;
    @FXML
    public Label userFormLastNameValidationLabel;
    @FXML
    public Label userFormUserNameValidationLabel;
    @FXML
    public Label userFormBranchValidationLabel;
    @FXML
    public Label userFormRoleValidationLabel;

    public static UserFormController getInstance() {
        if (Objects.equals(instance, null)) instance = new UserFormController();
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
        userFormFirstname.textProperty().bindBidirectional(UserViewModel.firstNameProperty());
        userFormLastname.textProperty().bindBidirectional(UserViewModel.lastNameProperty());
        userFormUsername.textProperty().bindBidirectional(UserViewModel.userNameProperty());
        userFormEmail.textProperty().bindBidirectional(UserViewModel.emailProperty());
        userFormPhone.textProperty().bindBidirectional(UserViewModel.phoneProperty());
        userFormRole.valueProperty().bindBidirectional(UserViewModel.roleProperty());
        userFormBranch.valueProperty().bindBidirectional(UserViewModel.branchProperty());
        userFormActive.selectedProperty().bindBidirectional(UserViewModel.activeProperty());

        // ComboBox Converters.
        StringConverter<Role> roleConverter =
                FunctionalStringConverter.to(role -> (role == null) ? "" : role.getName());

        StringConverter<Branch> branchConverter =
                FunctionalStringConverter.to(branch -> (branch == null) ? "" : branch.getName());

        // ComboBox Filter Functions.
        Function<String, Predicate<Role>> roleFilterFunction =
                searchStr ->
                        role -> StringUtils.containsIgnoreCase(roleConverter.toString(role), searchStr);

        Function<String, Predicate<Branch>> branchFilterFunction =
                searchStr ->
                        branch -> StringUtils.containsIgnoreCase(branchConverter.toString(branch), searchStr);

        // Combo box properties.
        userFormRole.setItems(RoleViewModel.getRoles());
        userFormRole.setConverter(roleConverter);
        userFormRole.setFilterFunction(roleFilterFunction);

        userFormBranch.setItems(BranchViewModel.getBranches());
        userFormBranch.setConverter(branchConverter);
        userFormBranch.setFilterFunction(branchFilterFunction);

        // Input validations.
        // Name input validation.
        requiredValidator(
                userFormFirstname,
                "First name is required.",
                userFormFirstNameValidationLabel,
                userFormSaveBtn);
        requiredValidator(
                userFormLastname,
                "Last name is required.",
                userFormLastNameValidationLabel,
                userFormSaveBtn);
        requiredValidator(
                userFormUsername,
                "Username is required.",
                userFormUserNameValidationLabel,
                userFormSaveBtn);
        requiredValidator(
                userFormBranch, "Branch is required.", userFormBranchValidationLabel, userFormSaveBtn);
        requiredValidator(
                userFormRole, "User role is required.", userFormRoleValidationLabel, userFormSaveBtn);
        // Email input validation.
        emailValidator(userFormEmail, userFormEmailValidationLabel, userFormSaveBtn);
        // Phone input validation.
        lengthValidator(
                userFormPhone, 11, "Invalid Phone length", userFormPhoneValidationLabel, userFormSaveBtn);
        dialogOnActions();
    }

    private void dialogOnActions() {
        userFormCancelBtn.setOnAction(
                (event) -> {
                    closeDialog(event);
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
                (event) -> {
                    SimpleNotificationHolder notificationHolder = SimpleNotificationHolder.getInstance();
                    if (!userFormFirstNameValidationLabel.isVisible()
                            && !userFormLastNameValidationLabel.isVisible()
                            && !userFormUserNameValidationLabel.isVisible()
                            && !userFormBranchValidationLabel.isVisible()
                            && !userFormRoleValidationLabel.isVisible()
                            && !userFormEmailValidationLabel.isVisible()
                            && !userFormPhoneValidationLabel.isVisible()) {
                        if (UserViewModel.getId() > 0) {
                            SpotyThreader.spotyThreadPool(() -> {
                                try {
                                    UserViewModel.updateItem();
                                } catch (Exception e) {
                                    SpotyLogger.writeToFile(e, this.getClass());
                                }
                            });
                            SimpleNotification notification =
                                    new SimpleNotification.NotificationBuilder("User updated successfully")
                                            .duration(NotificationDuration.SHORT)
                                            .icon("fas-circle-check")
                                            .type(NotificationVariants.SUCCESS)
                                            .build();
                            notificationHolder.addNotification(notification);

                            userFormRole.clearSelection();
                            userFormBranch.clearSelection();

                            closeDialog(event);
                            return;
                        }
                        SpotyThreader.spotyThreadPool(() -> {
                            try {
                                UserViewModel.saveUser();
                            } catch (Exception e) {
                                SpotyLogger.writeToFile(e, this.getClass());
                            }
                        });
                        SimpleNotification notification =
                                new SimpleNotification.NotificationBuilder("User saved successfully")
                                        .duration(NotificationDuration.SHORT)
                                        .icon("fas-circle-check")
                                        .type(NotificationVariants.SUCCESS)
                                        .build();
                        notificationHolder.addNotification(notification);

                        userFormRole.clearSelection();
                        userFormBranch.clearSelection();

                        closeDialog(event);
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
