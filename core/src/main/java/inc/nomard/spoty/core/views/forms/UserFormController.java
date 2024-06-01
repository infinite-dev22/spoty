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

import static inc.nomard.spoty.core.GlobalActions.*;
import static inc.nomard.spoty.core.Validators.*;
import inc.nomard.spoty.core.components.message.*;
import inc.nomard.spoty.core.components.message.enums.*;
import inc.nomard.spoty.core.viewModels.*;
import inc.nomard.spoty.core.viewModels.hrm.employee.*;
import inc.nomard.spoty.network_bridge.dtos.*;
import inc.nomard.spoty.network_bridge.dtos.hrm.employee.*;
import io.github.palexdev.materialfx.controls.*;
import io.github.palexdev.materialfx.dialogs.*;
import io.github.palexdev.materialfx.utils.*;
import io.github.palexdev.materialfx.utils.others.*;
import io.github.palexdev.materialfx.validation.*;
import static io.github.palexdev.materialfx.validation.Validated.*;
import io.github.palexdev.mfxcomponents.controls.buttons.MFXButton;
import java.net.*;
import java.util.*;
import java.util.function.*;
import javafx.collections.*;
import javafx.event.*;
import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.util.*;
import lombok.extern.java.*;

@Log
public class UserFormController implements Initializable {
    public static UserFormController instance;
    @FXML
    public MFXButton saveBtn,
            cancelBtn;
    @FXML
    public MFXTextField email,
            phone,
            firstname,
            lastname,
            username;
    @FXML
    public MFXFilterComboBox<Role> role;
    @FXML
    public MFXToggleButton status;
    @FXML
    public Label firstNameValidationLabel,
            emailValidationLabel,
            phoneValidationLabel,
            lastNameValidationLabel,
            userNameValidationLabel,
            roleValidationLabel,
            departmentValidationLabel,
            designationValidationLabel,
            employmentStatusValidationLabel,
            workShiftValidationLabel;
    public MFXDatePicker dateOfBirth;
    public MFXFilterComboBox<Department> department;
    public MFXFilterComboBox<Designation> designation;
    public MFXFilterComboBox<EmploymentStatus> employmentStatus;
    public MFXComboBox<String> workShift;
    private List<Constraint> firstNameConstraints,
            lastNameConstraints,
            userNameConstraints,
            userRoleConstraints;
    private ActionEvent actionEvent = null;

    public static UserFormController getInstance() {
        if (Objects.equals(instance, null)) instance = new UserFormController();
        return instance;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Input listeners.
        phone
                .textProperty()
                .addListener(
                        (observable, oldValue, newValue) -> {
                            if (!newValue.matches("\\d*")) phone.setText(newValue.replaceAll("\\D", ""));
                        });
        phone
                .focusedProperty()
                .addListener(
                        (observable, oldValue, newValue) -> {
                            if (newValue != oldValue) phone.setLeadingIcon(new Label("+"));
                            System.out.println("newValue oldValue");
                        });

        // Input bindings.
        firstname.textProperty().bindBidirectional(UserViewModel.firstNameProperty());
        lastname.textProperty().bindBidirectional(UserViewModel.lastNameProperty());
        username.textProperty().bindBidirectional(UserViewModel.userNameProperty());
        email.textProperty().bindBidirectional(UserViewModel.emailProperty());
        phone.textProperty().bindBidirectional(UserViewModel.phoneProperty());
        role.valueProperty().bindBidirectional(UserViewModel.roleProperty());
        status.selectedProperty().bindBidirectional(UserViewModel.activeProperty());
        department.valueProperty().bindBidirectional(UserViewModel.departmentProperty());
        designation.valueProperty().bindBidirectional(UserViewModel.designationProperty());
        employmentStatus.valueProperty().bindBidirectional(UserViewModel.employmentStatusProperty());
        workShift.valueProperty().bindBidirectional(UserViewModel.workShiftProperty());

        // ComboBox Converters.
        StringConverter<Role> roleConverter =
                FunctionalStringConverter.to(role -> (role == null) ? "" : role.getName());
        StringConverter<Department> departmentConverter =
                FunctionalStringConverter.to(department -> (department == null) ? "" : department.getName());
        StringConverter<Designation> designationConverter =
                FunctionalStringConverter.to(designation -> (designation == null) ? "" : designation.getName());
        StringConverter<EmploymentStatus> employmentStatusConverter =
                FunctionalStringConverter.to(employmentStatus -> (employmentStatus == null) ? "" : employmentStatus.getName());

        // ComboBox Filter Functions.
        Function<String, Predicate<Role>> roleFilterFunction =
                searchStr ->
                        role -> StringUtils.containsIgnoreCase(roleConverter.toString(role), searchStr);
        Function<String, Predicate<Department>> departmentFilterFunction =
                searchStr ->
                        department -> StringUtils.containsIgnoreCase(departmentConverter.toString(department), searchStr);
        Function<String, Predicate<Designation>> designationFilterFunction =
                searchStr ->
                        designation -> StringUtils.containsIgnoreCase(designationConverter.toString(designation), searchStr);
        Function<String, Predicate<EmploymentStatus>> employmentStatusFilterFunction =
                searchStr ->
                        employmentStatus -> StringUtils.containsIgnoreCase(employmentStatusConverter.toString(employmentStatus), searchStr);

        // Combo box properties.
        workShift.setItems(UserViewModel.getWorkShiftsList());
        role.setConverter(roleConverter);
        role.setFilterFunction(roleFilterFunction);
        if (RoleViewModel.getRoles().isEmpty()) {
            RoleViewModel.getRoles()
                    .addListener(
                            (ListChangeListener<Role>)
                                    c -> role.setItems(RoleViewModel.getRoles()));
        } else {
            role.itemsProperty().bindBidirectional(RoleViewModel.rolesProperty());
        }
        department.setConverter(departmentConverter);
        department.setFilterFunction(departmentFilterFunction);
        if (DepartmentViewModel.getDepartments().isEmpty()) {
            DepartmentViewModel.getDepartments()
                    .addListener(
                            (ListChangeListener<Department>)
                                    c -> department.setItems(DepartmentViewModel.getDepartments()));
        } else {
            department.itemsProperty().bindBidirectional(DepartmentViewModel.departmentsProperty());
        }
        designation.setConverter(designationConverter);
        designation.setFilterFunction(designationFilterFunction);
        if (DesignationViewModel.getDesignations().isEmpty()) {
            DesignationViewModel.getDesignations()
                    .addListener(
                            (ListChangeListener<Designation>)
                                    c -> designation.setItems(DesignationViewModel.getDesignations()));
        } else {
            designation.itemsProperty().bindBidirectional(DesignationViewModel.designationsProperty());
        }
        employmentStatus.setConverter(employmentStatusConverter);
        employmentStatus.setFilterFunction(employmentStatusFilterFunction);
        if (EmploymentStatusViewModel.getEmploymentStatuses().isEmpty()) {
            EmploymentStatusViewModel.getEmploymentStatuses()
                    .addListener(
                            (ListChangeListener<EmploymentStatus>)
                                    c -> employmentStatus.setItems(EmploymentStatusViewModel.getEmploymentStatuses()));
        } else {
            employmentStatus.itemsProperty().bindBidirectional(EmploymentStatusViewModel.employmentStatusesProperty());
        }

        // Input validations.
        requiredValidator();
        // Email input validation.
        emailValidator(email, emailValidationLabel, saveBtn);
        // Phone input validation.
        lengthValidator(
                phone, 11, "Invalid Phone length", phoneValidationLabel, saveBtn);
        dialogOnActions();
    }

    private void dialogOnActions() {
        cancelBtn.setOnAction(
                (event) -> {
                    closeDialog(event);
                    UserViewModel.resetProperties();

                    role.clearSelection();

                    firstNameValidationLabel.setVisible(false);
                    lastNameValidationLabel.setVisible(false);
                    userNameValidationLabel.setVisible(false);
                    roleValidationLabel.setVisible(false);
                    emailValidationLabel.setVisible(false);
                    phoneValidationLabel.setVisible(false);

                    firstNameValidationLabel.setManaged(false);
                    lastNameValidationLabel.setManaged(false);
                    userNameValidationLabel.setManaged(false);
                    roleValidationLabel.setManaged(false);
                    emailValidationLabel.setManaged(false);
                    phoneValidationLabel.setManaged(false);

                    role.clearSelection();

                    firstname.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
                    lastname.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
                    username.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
                    username.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
                    role.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
                });
        saveBtn.setOnAction(
                (event) -> {
                    firstNameConstraints = firstname.validate();
                    lastNameConstraints = lastname.validate();
                    userNameConstraints = username.validate();
                    userRoleConstraints = role.validate();
                    if (!firstNameConstraints.isEmpty()) {
                        firstNameValidationLabel.setManaged(true);
                        firstNameValidationLabel.setVisible(true);
                        firstNameValidationLabel.setText(firstNameConstraints.getFirst().getMessage());
                        firstname.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, true);
                        MFXStageDialog dialog = (MFXStageDialog) firstname.getScene().getWindow();
                        dialog.sizeToScene();
                    }
                    if (!lastNameConstraints.isEmpty()) {
                        lastNameValidationLabel.setManaged(true);
                        lastNameValidationLabel.setVisible(true);
                        lastNameValidationLabel.setText(lastNameConstraints.getFirst().getMessage());
                        lastname.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, true);
                        MFXStageDialog dialog = (MFXStageDialog) lastname.getScene().getWindow();
                        dialog.sizeToScene();
                    }
                    if (!userNameConstraints.isEmpty()) {
                        userNameValidationLabel.setManaged(true);
                        userNameValidationLabel.setVisible(true);
                        userNameValidationLabel.setText(userNameConstraints.getFirst().getMessage());
                        username.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, true);
                        MFXStageDialog dialog = (MFXStageDialog) username.getScene().getWindow();
                        dialog.sizeToScene();
                    }
                    if (!userRoleConstraints.isEmpty()) {
                        roleValidationLabel.setManaged(true);
                        roleValidationLabel.setVisible(true);
                        roleValidationLabel.setText(userRoleConstraints.getFirst().getMessage());
                        role.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, true);
                        MFXStageDialog dialog = (MFXStageDialog) role.getScene().getWindow();
                        dialog.sizeToScene();
                    }
                    if (firstNameConstraints.isEmpty() &
                            lastNameConstraints.isEmpty() &
                            userNameConstraints.isEmpty() &
                            userRoleConstraints.isEmpty()
                            && !emailValidationLabel.isVisible()
                            && !phoneValidationLabel.isVisible()) {
                        if (UserViewModel.getId() > 0) {
                            UserViewModel.updateItem(this::onSuccess, this::successMessage, this::errorMessage);
                            actionEvent = event;
                            return;
                        }
                        UserViewModel.saveUser(this::onSuccess, this::successMessage, this::errorMessage);
                        actionEvent = event;
                    }
                });
    }

    private void onSuccess() {
        role.clearSelection();
        closeDialog(actionEvent);
    }

    public void requiredValidator() {
        // Name input validation.
        Constraint firstName =
                Constraint.Builder.build()
                        .setSeverity(Severity.ERROR)
                        .setMessage("First name is required")
                        .setCondition(firstname.textProperty().length().greaterThan(0))
                        .get();
        firstname.getValidator().constraint(firstName);
        Constraint lastName =
                Constraint.Builder.build()
                        .setSeverity(Severity.ERROR)
                        .setMessage("Last name is required")
                        .setCondition(lastname.textProperty().length().greaterThan(0))
                        .get();
        lastname.getValidator().constraint(lastName);
        Constraint userName =
                Constraint.Builder.build()
                        .setSeverity(Severity.ERROR)
                        .setMessage("Username is required")
                        .setCondition(username.textProperty().length().greaterThan(0))
                        .get();
        username.getValidator().constraint(userName);
        Constraint userRole =
                Constraint.Builder.build()
                        .setSeverity(Severity.ERROR)
                        .setMessage("Role is required")
                        .setCondition(role.textProperty().length().greaterThan(0))
                        .get();
        role.getValidator().constraint(userRole);
        // Display error.
        firstname
                .getValidator()
                .validProperty()
                .addListener(
                        (observable, oldValue, newValue) -> {
                            if (newValue) {
                                firstNameValidationLabel.setManaged(false);
                                firstNameValidationLabel.setVisible(false);
                                firstname.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
                            }
                        });
        lastname
                .getValidator()
                .validProperty()
                .addListener(
                        (observable, oldValue, newValue) -> {
                            if (newValue) {
                                lastNameValidationLabel.setManaged(false);
                                lastNameValidationLabel.setVisible(false);
                                lastname.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
                            }
                        });
        username
                .getValidator()
                .validProperty()
                .addListener(
                        (observable, oldValue, newValue) -> {
                            if (newValue) {
                                userNameValidationLabel.setManaged(false);
                                userNameValidationLabel.setVisible(false);
                                username.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
                            }
                        });
        role
                .getValidator()
                .validProperty()
                .addListener(
                        (observable, oldValue, newValue) -> {
                            if (newValue) {
                                roleValidationLabel.setManaged(false);
                                roleValidationLabel.setVisible(false);
                                role.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
                            }
                        });
    }

    private void successMessage(String message) {
        SpotyMessageHolder notificationHolder = SpotyMessageHolder.getInstance();
        SpotyMessage notification =
                new SpotyMessage.MessageBuilder(message)
                        .duration(MessageDuration.SHORT)
                        .icon("fas-triangle-exclamation")
                        .type(MessageVariants.ERROR)
                        .build();
        notificationHolder.addMessage(notification);
        AnchorPane.setRightAnchor(notification, 40.0);
        AnchorPane.setTopAnchor(notification, 10.0);
    }

    private void errorMessage(String message) {
        SpotyMessageHolder notificationHolder = SpotyMessageHolder.getInstance();
        SpotyMessage notification =
                new SpotyMessage.MessageBuilder(message)
                        .duration(MessageDuration.SHORT)
                        .icon("fas-triangle-exclamation")
                        .type(MessageVariants.ERROR)
                        .build();
        notificationHolder.addMessage(notification);
        AnchorPane.setRightAnchor(notification, 40.0);
        AnchorPane.setTopAnchor(notification, 10.0);
    }
}
