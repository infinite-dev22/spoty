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

package inc.nomard.spoty.core.views.forms;

import inc.nomard.spoty.core.components.message.SpotyMessage;
import inc.nomard.spoty.core.components.message.SpotyMessageHolder;
import inc.nomard.spoty.core.components.message.enums.MessageDuration;
import inc.nomard.spoty.core.components.message.enums.MessageVariants;
import inc.nomard.spoty.core.viewModels.BranchViewModel;
import inc.nomard.spoty.core.viewModels.RoleViewModel;
import inc.nomard.spoty.core.viewModels.hrm.employee.UserViewModel;
import inc.nomard.spoty.network_bridge.dtos.Branch;
import inc.nomard.spoty.network_bridge.dtos.Role;
import inc.nomard.spoty.utils.SpotyLogger;
import io.github.palexdev.materialfx.controls.MFXFilterComboBox;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.materialfx.controls.MFXToggleButton;
import io.github.palexdev.materialfx.utils.StringUtils;
import io.github.palexdev.materialfx.utils.others.FunctionalStringConverter;
import io.github.palexdev.materialfx.validation.Constraint;
import io.github.palexdev.materialfx.validation.Severity;
import io.github.palexdev.mfxcomponents.controls.buttons.MFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.util.StringConverter;

import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.function.Function;
import java.util.function.Predicate;

import static inc.nomard.spoty.core.GlobalActions.closeDialog;
import static inc.nomard.spoty.core.Validators.emailValidator;
import static inc.nomard.spoty.core.Validators.lengthValidator;
import static io.github.palexdev.materialfx.validation.Validated.INVALID_PSEUDO_CLASS;

public class UserFormController implements Initializable {
    public static UserFormController instance;
    @FXML
    public MFXButton saveBtn,
            cancelBtn;
    @FXML
    public MFXTextField userFormEmail,
            userFormPhone,
            userFormFirstname,
            userFormLastname,
            userFormUsername;
    @FXML
    public MFXFilterComboBox<Role> userFormRole;
    @FXML
    public MFXFilterComboBox<Branch> userFormBranch;
    @FXML
    public MFXToggleButton userFormActive;
    @FXML
    public Label userFormFirstNameValidationLabel,
            userFormEmailValidationLabel,
            userFormPhoneValidationLabel,
            userFormLastNameValidationLabel,
            userFormUserNameValidationLabel,
            userFormBranchValidationLabel,
            userFormRoleValidationLabel;
    private List<Constraint> firstNameConstraints,
            lastNameConstraints,
            userNameConstraints,
            userBranchConstraints,
            userRoleConstraints;
    private ActionEvent actionEvent = null;

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
        requiredValidator();
        // Email input validation.
        emailValidator(userFormEmail, userFormEmailValidationLabel, saveBtn);
        // Phone input validation.
        lengthValidator(
                userFormPhone, 11, "Invalid Phone length", userFormPhoneValidationLabel, saveBtn);
        dialogOnActions();
    }

    private void dialogOnActions() {
        cancelBtn.setOnAction(
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

                    userFormFirstNameValidationLabel.setManaged(false);
                    userFormLastNameValidationLabel.setManaged(false);
                    userFormUserNameValidationLabel.setManaged(false);
                    userFormBranchValidationLabel.setManaged(false);
                    userFormRoleValidationLabel.setManaged(false);
                    userFormEmailValidationLabel.setManaged(false);
                    userFormPhoneValidationLabel.setManaged(false);

                    userFormFirstname.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
                    userFormLastname.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
                    userFormUsername.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
                    userFormUsername.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
                    userFormBranch.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
                    userFormRole.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
                });
        saveBtn.setOnAction(
                (event) -> {
                    firstNameConstraints = userFormFirstname.validate();
                    lastNameConstraints = userFormLastname.validate();
                    userNameConstraints = userFormUsername.validate();
                    userBranchConstraints = userFormBranch.validate();
                    userRoleConstraints = userFormRole.validate();
                    if (!firstNameConstraints.isEmpty()) {
                        userFormFirstNameValidationLabel.setManaged(true);
                        userFormFirstNameValidationLabel.setVisible(true);
                        userFormFirstNameValidationLabel.setText(firstNameConstraints.getFirst().getMessage());
                        userFormFirstname.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, true);
                    }
                    if (!lastNameConstraints.isEmpty()) {
                        userFormLastNameValidationLabel.setManaged(true);
                        userFormLastNameValidationLabel.setVisible(true);
                        userFormLastNameValidationLabel.setText(lastNameConstraints.getFirst().getMessage());
                        userFormLastname.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, true);
                    }
                    if (!userNameConstraints.isEmpty()) {
                        userFormUserNameValidationLabel.setManaged(true);
                        userFormUserNameValidationLabel.setVisible(true);
                        userFormUserNameValidationLabel.setText(userNameConstraints.getFirst().getMessage());
                        userFormUsername.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, true);
                    }
                    if (!userNameConstraints.isEmpty()) {
                        userFormUserNameValidationLabel.setManaged(true);
                        userFormUserNameValidationLabel.setVisible(true);
                        userFormUserNameValidationLabel.setText(userNameConstraints.getFirst().getMessage());
                        userFormUsername.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, true);
                    }
                    if (!userBranchConstraints.isEmpty()) {
                        userFormBranchValidationLabel.setManaged(true);
                        userFormBranchValidationLabel.setVisible(true);
                        userFormBranchValidationLabel.setText(userBranchConstraints.getFirst().getMessage());
                        userFormBranch.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, true);
                    }
                    if (!userRoleConstraints.isEmpty()) {
                        userFormRoleValidationLabel.setManaged(true);
                        userFormRoleValidationLabel.setVisible(true);
                        userFormRoleValidationLabel.setText(userRoleConstraints.getFirst().getMessage());
                        userFormRole.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, true);
                    }
                    if (firstNameConstraints.isEmpty() &
                            lastNameConstraints.isEmpty() &
                            userNameConstraints.isEmpty() &
                            userBranchConstraints.isEmpty() &
                            userRoleConstraints.isEmpty()
                            && !userFormEmailValidationLabel.isVisible()
                            && !userFormPhoneValidationLabel.isVisible()) {
                        if (UserViewModel.getId() > 0) {
                            try {
                                UserViewModel.updateItem(this::onAction, this::onUpdatedSuccess, this::onFailed);
                                actionEvent = event;
                            } catch (Exception e) {
                                SpotyLogger.writeToFile(e, this.getClass());
                            }
                            return;
                        }
                        try {
                            UserViewModel.saveUser(this::onAction, this::onAddSuccess, this::onFailed);
                            actionEvent = event;
                        } catch (Exception e) {
                            SpotyLogger.writeToFile(e, this.getClass());
                        }
                    }
                });
    }

    private void onAction() {
        cancelBtn.setDisable(true);
        saveBtn.setDisable(true);
//        cancelBtn.setManaged(true);
//        saveBtn.setManaged(true);
    }

    private void onAddSuccess() {
        SpotyMessageHolder notificationHolder = SpotyMessageHolder.getInstance();
        SpotyMessage notification =
                new SpotyMessage.MessageBuilder("User added successfully")
                        .duration(MessageDuration.SHORT)
                        .icon("fas-circle-check")
                        .type(MessageVariants.SUCCESS)
                        .build();
        notificationHolder.addMessage(notification);
        cancelBtn.setDisable(false);
        saveBtn.setDisable(false);

        closeDialog(actionEvent);
        UserViewModel.resetProperties();
        UserViewModel.getAllUserProfiles(null, null, null);
    }

    private void onUpdatedSuccess() {
        SpotyMessageHolder notificationHolder = SpotyMessageHolder.getInstance();
        SpotyMessage notification =
                new SpotyMessage.MessageBuilder("User profile updated successfully")
                        .duration(MessageDuration.SHORT)
                        .icon("fas-circle-check")
                        .type(MessageVariants.SUCCESS)
                        .build();
        notificationHolder.addMessage(notification);
        cancelBtn.setDisable(false);
        saveBtn.setDisable(false);

        closeDialog(actionEvent);
        UserViewModel.resetProperties();
        UserViewModel.getAllUserProfiles(null, null, null);
    }

    private void onFailed() {
        SpotyMessageHolder notificationHolder = SpotyMessageHolder.getInstance();
        SpotyMessage notification =
                new SpotyMessage.MessageBuilder("An error occurred")
                        .duration(MessageDuration.SHORT)
                        .icon("fas-triangle-exclamation")
                        .type(MessageVariants.ERROR)
                        .build();
        notificationHolder.addMessage(notification);
        cancelBtn.setDisable(false);
        saveBtn.setDisable(false);

        UserViewModel.getAllUserProfiles(null, null, null);
    }

    public void requiredValidator() {
        // Name input validation.
        Constraint firstName =
                Constraint.Builder.build()
                        .setSeverity(Severity.ERROR)
                        .setMessage("First name is required")
                        .setCondition(userFormFirstname.textProperty().length().greaterThan(0))
                        .get();
        userFormFirstname.getValidator().constraint(firstName);
        Constraint lastName =
                Constraint.Builder.build()
                        .setSeverity(Severity.ERROR)
                        .setMessage("Last name is required")
                        .setCondition(userFormLastname.textProperty().length().greaterThan(0))
                        .get();
        userFormLastname.getValidator().constraint(lastName);
        Constraint userName =
                Constraint.Builder.build()
                        .setSeverity(Severity.ERROR)
                        .setMessage("Username is required")
                        .setCondition(userFormUsername.textProperty().length().greaterThan(0))
                        .get();
        userFormUsername.getValidator().constraint(userName);
        Constraint userBranch =
                Constraint.Builder.build()
                        .setSeverity(Severity.ERROR)
                        .setMessage("Branch is required")
                        .setCondition(userFormBranch.textProperty().length().greaterThan(0))
                        .get();
        userFormBranch.getValidator().constraint(userBranch);
        Constraint userRole =
                Constraint.Builder.build()
                        .setSeverity(Severity.ERROR)
                        .setMessage("Role is required")
                        .setCondition(userFormRole.textProperty().length().greaterThan(0))
                        .get();
        userFormRole.getValidator().constraint(userRole);
        // Display error.
        userFormFirstname
                .getValidator()
                .validProperty()
                .addListener(
                        (observable, oldValue, newValue) -> {
                            if (newValue) {
                                userFormFirstNameValidationLabel.setManaged(false);
                                userFormFirstNameValidationLabel.setVisible(false);
                                userFormFirstname.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
                            }
                        });
        userFormLastname
                .getValidator()
                .validProperty()
                .addListener(
                        (observable, oldValue, newValue) -> {
                            if (newValue) {
                                userFormLastNameValidationLabel.setManaged(false);
                                userFormLastNameValidationLabel.setVisible(false);
                                userFormLastname.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
                            }
                        });
        userFormUsername
                .getValidator()
                .validProperty()
                .addListener(
                        (observable, oldValue, newValue) -> {
                            if (newValue) {
                                userFormUserNameValidationLabel.setManaged(false);
                                userFormUserNameValidationLabel.setVisible(false);
                                userFormUsername.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
                            }
                        });
        userFormBranch
                .getValidator()
                .validProperty()
                .addListener(
                        (observable, oldValue, newValue) -> {
                            if (newValue) {
                                userFormBranchValidationLabel.setManaged(false);
                                userFormBranchValidationLabel.setVisible(false);
                                userFormBranch.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
                            }
                        });
        userFormRole
                .getValidator()
                .validProperty()
                .addListener(
                        (observable, oldValue, newValue) -> {
                            if (newValue) {
                                userFormRoleValidationLabel.setManaged(false);
                                userFormRoleValidationLabel.setVisible(false);
                                userFormRole.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
                            }
                        });
    }
}
