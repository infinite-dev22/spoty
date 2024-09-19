package inc.nomard.spoty.core.views.forms;

import atlantafx.base.controls.ModalPane;
import atlantafx.base.controls.ToggleSwitch;
import atlantafx.base.theme.Styles;
import inc.nomard.spoty.core.viewModels.RoleViewModel;
import inc.nomard.spoty.core.viewModels.hrm.employee.DepartmentViewModel;
import inc.nomard.spoty.core.viewModels.hrm.employee.DesignationViewModel;
import inc.nomard.spoty.core.viewModels.hrm.employee.EmployeeViewModel;
import inc.nomard.spoty.core.viewModels.hrm.employee.EmploymentStatusViewModel;
import inc.nomard.spoty.core.views.components.CustomButton;
import inc.nomard.spoty.core.views.components.validatables.ValidatableComboBox;
import inc.nomard.spoty.core.views.components.validatables.ValidatableDatePicker;
import inc.nomard.spoty.core.views.components.validatables.ValidatableTextField;
import inc.nomard.spoty.core.views.util.SpotyUtils;
import inc.nomard.spoty.network_bridge.dtos.Role;
import inc.nomard.spoty.network_bridge.dtos.hrm.employee.Department;
import inc.nomard.spoty.network_bridge.dtos.hrm.employee.Designation;
import inc.nomard.spoty.network_bridge.dtos.hrm.employee.EmploymentStatus;
import io.github.palexdev.materialfx.dialogs.MFXStageDialog;
import io.github.palexdev.materialfx.validation.Constraint;
import io.github.palexdev.materialfx.validation.Severity;
import io.github.palexdev.mfxcore.utils.StringUtils;
import io.github.palexdev.mfxcore.utils.converters.FunctionalStringConverter;
import javafx.beans.property.Property;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.css.PseudoClass;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;
import lombok.extern.java.Log;

import java.time.LocalDate;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

import static inc.nomard.spoty.core.GlobalActions.closeDialog;

@Log
public class EmployeeForm extends BorderPane {
    private static final PseudoClass INVALID_PSEUDO_CLASS = PseudoClass.getPseudoClass("invalid");
    private final ModalPane modalPane;
    public CustomButton saveBtn;
    public Button cancelBtn;
    private ValidatableTextField email, phone, firstname, lastname, otherName;
    private ValidatableComboBox<Role> role;
    private ToggleSwitch status;
    private Label firstNameValidationLabel, emailValidationLabel, phoneValidationLabel,
            lastNameValidationLabel, otherNameValidationLabel, roleValidationLabel,
            departmentValidationLabel, designationValidationLabel,
            employmentStatusValidationLabel, workShiftValidationLabel;
    private ValidatableDatePicker dateOfBirth;
    private ValidatableComboBox<Department> department;
    private ValidatableComboBox<Designation> designation;
    private ValidatableComboBox<EmploymentStatus> employmentStatus;
    private ValidatableComboBox<String> workShift;
    private List<Constraint> firstNameConstraints, lastNameConstraints,
            otherNameConstraints, userRoleConstraints;
    private Event actionEvent = null;

    public EmployeeForm(ModalPane modalPane) {
        this.modalPane = modalPane;
        initializeComponents();
        initializeComponentProperties();
        setupLayout();
    }

    private void initializeComponents() {
        firstNameValidationLabel = createValidationLabel();
        lastNameValidationLabel = createValidationLabel();
        otherNameValidationLabel = createValidationLabel();
        emailValidationLabel = createValidationLabel();
        phoneValidationLabel = createValidationLabel();
        departmentValidationLabel = createValidationLabel();
        designationValidationLabel = createValidationLabel();
        employmentStatusValidationLabel = createValidationLabel();
        workShiftValidationLabel = createValidationLabel();
        roleValidationLabel = createValidationLabel();

        firstname = createTextField();
        lastname = createTextField();
        otherName = createTextField();
        dateOfBirth = createDatePicker();
        email = createTextField();
        phone = createTextField();

        department = createFilterComboBox(DepartmentViewModel.getDepartments());
        designation = createFilterComboBox(DesignationViewModel.getDesignations());
        employmentStatus = createFilterComboBox(EmploymentStatusViewModel.getEmploymentStatuses());
        workShift = createComboBox();
        role = createFilterComboBox(RoleViewModel.getRoles());

        status = new ToggleSwitch();

        saveBtn = new CustomButton("Save");
        saveBtn.getStyleClass().add(Styles.ACCENT);

        cancelBtn = new Button("Cancel");
        cancelBtn.getStyleClass().add(Styles.BUTTON_OUTLINED);
    }

    private void initializeComponentProperties() {
        setupPhoneField();
        setupBindings();
        setupValidators();
        setupListeners();
        setupFieldProperties();
    }

    private void setupLayout() {
        HBox buttonBox = new HBox(20, saveBtn, cancelBtn);
        buttonBox.setAlignment(Pos.CENTER_RIGHT);
        buttonBox.setPadding(new Insets(10, 10, 10, 10));

        setCenter(buildCenter());
        setBottom(buttonBox);
    }

    private VBox buildCenter() {
        var vbox = new VBox();
        vbox.setSpacing(8d);
        vbox.setPadding(new Insets(10d));
        vbox.getChildren().addAll(
                buildFieldBox(firstname, "First name", firstNameValidationLabel),
                buildFieldBox(lastname, "Last name", lastNameValidationLabel),
                buildFieldBox(otherName, "Other Name", otherNameValidationLabel),
                buildFieldBox(dateOfBirth, "Date Of Birth"),
                buildFieldBox(email, "Email", emailValidationLabel),
                buildFieldBox(phone, "Phone", phoneValidationLabel),
                buildFieldBox(department, "Department", departmentValidationLabel),
                buildFieldBox(designation, "Designation", designationValidationLabel),
                buildFieldBox(employmentStatus, "Employment Status", employmentStatusValidationLabel),
                buildFieldBox(role, "Role", roleValidationLabel),
                buildFieldBox(status, "Status"));
        return vbox;
    }

    private Label createValidationLabel() {
        Label label = new Label();
        label.setManaged(false);
        label.setVisible(false);
        label.setWrapText(true);
        label.getStyleClass().add("input-validation-error");
        return label;
    }

    private ValidatableTextField createTextField() {
        ValidatableTextField textField = new ValidatableTextField();
        textField.setPrefWidth(300);
        return textField;
    }

    private ValidatableDatePicker createDatePicker() {
        ValidatableDatePicker datePicker = new ValidatableDatePicker();
        datePicker.setPrefWidth(1000);
        return datePicker;
    }

    private <T> ValidatableComboBox<T> createFilterComboBox(ObservableList<T> items) {
        ValidatableComboBox<T> comboBox = new ValidatableComboBox<>();
        comboBox.setPrefWidth(1000);
        comboBox.setItems(items);
        return comboBox;
    }

    private ValidatableComboBox<String> createComboBox() {
        ValidatableComboBox<String> comboBox = new ValidatableComboBox<>();
        comboBox.setPrefWidth(300);
        comboBox.setItems(EmployeeViewModel.getWorkShiftsList());
        return comboBox;
    }

    private VBox buildFieldBox(Control control, String floatingText, Label validationLabel) {
        var label = new Label(floatingText);
        return new VBox(label, control, validationLabel);
    }

    private VBox buildFieldBox(Control control, String floatingText) {
        var label = new Label(floatingText);
        return new VBox(label, control);
    }

    private void setupPhoneField() {
        phone.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) phone.setText(newValue.replaceAll("\\D", ""));
        });
        phone.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != oldValue) phone.setLeft(new Label("+"));
        });
    }

    private void setupBindings() {
        bindTextField(firstname, EmployeeViewModel.firstNameProperty());
        bindTextField(lastname, EmployeeViewModel.lastNameProperty());
        bindTextField(otherName, EmployeeViewModel.otherNameProperty());
        bindTextField(email, EmployeeViewModel.emailProperty());
        bindTextField(phone, EmployeeViewModel.phoneProperty());
        bindFilterComboBox(role, EmployeeViewModel.roleProperty());
        bindToggleButton(status, EmployeeViewModel.activeProperty());
        bindFilterComboBox(department, EmployeeViewModel.departmentProperty());
        bindFilterComboBox(designation, EmployeeViewModel.designationProperty());
        bindFilterComboBox(employmentStatus, EmployeeViewModel.employmentStatusProperty());
        bindComboBox(workShift, EmployeeViewModel.workShiftProperty());
        bindDatePicker(dateOfBirth, EmployeeViewModel.dateOfBirthProperty());
    }

    private void bindTextField(ValidatableTextField textField, Property<String> property) {
        textField.textProperty().bindBidirectional(property);
    }

    private <T> void bindFilterComboBox(ValidatableComboBox<T> comboBox, Property<T> property) {
        comboBox.valueProperty().bindBidirectional(property);
    }

    private <T> void bindComboBox(ValidatableComboBox<T> comboBox, Property<T> property) {
        comboBox.valueProperty().bindBidirectional(property);
    }

    private void bindToggleButton(ToggleSwitch toggleButton, Property<Boolean> property) {
        toggleButton.selectedProperty().bindBidirectional(property);
    }

    private void bindDatePicker(ValidatableDatePicker datePicker, Property<LocalDate> property) {
        datePicker.valueProperty().bindBidirectional(property);
    }

    private void setupValidators() {
        requiredValidator(firstname, firstNameValidationLabel, "First name is required");
        requiredValidator(lastname, lastNameValidationLabel, "Last name is required");
        requiredValidator(otherName, otherNameValidationLabel, "Employeename is required");
        requiredValidator(role, roleValidationLabel, "Role is required");
    }

    private void requiredValidator(ValidatableTextField control, Label validationLabel, String message) {
        // Name input validation.
        Constraint firstName =
                Constraint.Builder.build()
                        .setSeverity(Severity.ERROR)
                        .setMessage(message)
                        .setCondition(control.textProperty().length().greaterThan(0))
                        .get();
        control.getValidator().constraint(firstName);
        control
                .getValidator()
                .validProperty()
                .addListener(
                        (observable, oldValue, newValue) -> {
                            if (newValue) {
                                validationLabel.setManaged(false);
                                validationLabel.setVisible(false);
                                control.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
                            }
                        });
    }

    private <T> void requiredValidator(ValidatableComboBox<T> control, Label validationLabel, String message) {
        // Name input validation.
        Constraint firstName =
                Constraint.Builder.build()
                        .setSeverity(Severity.ERROR)
                        .setMessage(message)
                        .setCondition(control.valueProperty().isNotNull())
                        .get();
        control.getValidator().constraint(firstName);
        control
                .getValidator()
                .validProperty()
                .addListener(
                        (observable, oldValue, newValue) -> {
                            if (newValue) {
                                validationLabel.setManaged(false);
                                validationLabel.setVisible(false);
                                control.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
                            }
                        });
    }

    private void setupListeners() {
        dialogOnActions();
    }

    private void dialogOnActions() {
        saveBtn.setOnAction(this::onSave);
        cancelBtn.setOnAction(event -> this.dispose());
    }

    private void onSave(Event event) {
        firstNameConstraints = firstname.validate();
        lastNameConstraints = lastname.validate();
        otherNameConstraints = otherName.validate();
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
        if (!otherNameConstraints.isEmpty()) {
            otherNameValidationLabel.setManaged(true);
            otherNameValidationLabel.setVisible(true);
            otherNameValidationLabel.setText(otherNameConstraints.getFirst().getMessage());
            otherName.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, true);
            MFXStageDialog dialog = (MFXStageDialog) otherName.getScene().getWindow();
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
                otherNameConstraints.isEmpty() &
                userRoleConstraints.isEmpty()
                && !emailValidationLabel.isVisible()
                && !phoneValidationLabel.isVisible()) {
            if (EmployeeViewModel.getId() > 0) {
                EmployeeViewModel.updateItem(this::onSuccess, SpotyUtils::successMessage, SpotyUtils::errorMessage);
                actionEvent = event;
                return;
            }
            EmployeeViewModel.saveEmployee(this::onSuccess, SpotyUtils::successMessage, SpotyUtils::errorMessage);
            actionEvent = event;
        }
    }

    private void onCancel(ActionEvent event) {
        closeDialog(event);
        EmployeeViewModel.resetProperties();

        firstNameValidationLabel.setVisible(false);
        lastNameValidationLabel.setVisible(false);
        otherNameValidationLabel.setVisible(false);
        roleValidationLabel.setVisible(false);
        emailValidationLabel.setVisible(false);
        phoneValidationLabel.setVisible(false);

        firstNameValidationLabel.setManaged(false);
        lastNameValidationLabel.setManaged(false);
        otherNameValidationLabel.setManaged(false);
        roleValidationLabel.setManaged(false);
        emailValidationLabel.setManaged(false);
        phoneValidationLabel.setManaged(false);

        firstname.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
        lastname.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
        otherName.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
        otherName.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
        role.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
        this.dispose();
    }

    private void onSuccess() {
        this.dispose();
        EmployeeViewModel.getAllEmployees(null, null, null, null);
        this.dispose();
    }

    private void setupFieldProperties() {
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
        workShift.setItems(EmployeeViewModel.getWorkShiftsList());
        role.setConverter(roleConverter);
        if (RoleViewModel.getRoles().isEmpty()) {
            RoleViewModel.getRoles()
                    .addListener(
                            (ListChangeListener<Role>)
                                    c -> role.setItems(RoleViewModel.getRoles()));
        } else {
            role.itemsProperty().bindBidirectional(RoleViewModel.rolesProperty());
        }
        department.setConverter(departmentConverter);
        if (DepartmentViewModel.getDepartments().isEmpty()) {
            DepartmentViewModel.getDepartments()
                    .addListener(
                            (ListChangeListener<Department>)
                                    c -> department.setItems(DepartmentViewModel.getDepartments()));
        } else {
            department.itemsProperty().bindBidirectional(DepartmentViewModel.departmentsProperty());
        }
        designation.setConverter(designationConverter);
        if (DesignationViewModel.getDesignations().isEmpty()) {
            DesignationViewModel.getDesignations()
                    .addListener(
                            (ListChangeListener<Designation>)
                                    c -> designation.setItems(DesignationViewModel.getDesignations()));
        } else {
            designation.itemsProperty().bindBidirectional(DesignationViewModel.designationsProperty());
        }
        employmentStatus.setConverter(employmentStatusConverter);
        if (EmploymentStatusViewModel.getEmploymentStatuses().isEmpty()) {
            EmploymentStatusViewModel.getEmploymentStatuses()
                    .addListener(
                            (ListChangeListener<EmploymentStatus>)
                                    c -> employmentStatus.setItems(EmploymentStatusViewModel.getEmploymentStatuses()));
        } else {
            employmentStatus.itemsProperty().bindBidirectional(EmploymentStatusViewModel.employmentStatusesProperty());
        }
    }

    public void dispose() {
        modalPane.hide(true);
        modalPane.setPersistent(false);
        EmployeeViewModel.resetProperties();
        saveBtn = null;
        cancelBtn = null;
        email = null;
        phone = null;
        firstname = null;
        lastname = null;
        otherName = null;
        role = null;
        status = null;
        firstNameValidationLabel = null;
        emailValidationLabel = null;
        phoneValidationLabel = null;
        lastNameValidationLabel = null;
        otherNameValidationLabel = null;
        roleValidationLabel = null;
        departmentValidationLabel = null;
        designationValidationLabel = null;
        employmentStatusValidationLabel = null;
        workShiftValidationLabel = null;
        dateOfBirth = null;
        department = null;
        designation = null;
        employmentStatus = null;
        workShift = null;
        firstNameConstraints = null;
        lastNameConstraints = null;
        otherNameConstraints = null;
        userRoleConstraints = null;
        actionEvent = null;
    }
}
