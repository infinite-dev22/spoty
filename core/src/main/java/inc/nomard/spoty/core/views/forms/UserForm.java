package inc.nomard.spoty.core.views.forms;

import atlantafx.base.theme.*;
import atlantafx.base.util.*;
import static inc.nomard.spoty.core.GlobalActions.*;
import inc.nomard.spoty.core.viewModels.*;
import inc.nomard.spoty.core.viewModels.hrm.employee.*;
import inc.nomard.spoty.core.views.components.label_components.controls.*;
import inc.nomard.spoty.core.views.layout.*;
import inc.nomard.spoty.core.views.layout.message.*;
import inc.nomard.spoty.core.views.layout.message.enums.*;
import inc.nomard.spoty.network_bridge.dtos.*;
import inc.nomard.spoty.network_bridge.dtos.hrm.employee.*;
import io.github.palexdev.materialfx.controls.*;
import io.github.palexdev.materialfx.dialogs.*;
import io.github.palexdev.materialfx.validation.*;
import static io.github.palexdev.materialfx.validation.Validated.*;
import io.github.palexdev.mfxcore.utils.*;
import io.github.palexdev.mfxcore.utils.converters.*;
import java.time.*;
import java.util.*;
import java.util.function.*;
import javafx.beans.property.*;
import javafx.collections.*;
import javafx.event.*;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.util.Duration;
import javafx.util.*;
import lombok.extern.java.*;

@Log
public class UserForm extends ModalPage {
    private Button saveBtn, cancelBtn;
    private LabeledTextField email, phone, firstname, lastname, username;
    private LabeledComboBox<Role> role;
    private MFXToggleButton status;
    private Label firstNameValidationLabel, emailValidationLabel, phoneValidationLabel,
            lastNameValidationLabel, userNameValidationLabel, roleValidationLabel,
            departmentValidationLabel, designationValidationLabel,
            employmentStatusValidationLabel, workShiftValidationLabel;
    private LabeledDatePicker dateOfBirth;
    private LabeledComboBox<Department> department;
    private LabeledComboBox<Designation> designation;
    private LabeledComboBox<EmploymentStatus> employmentStatus;
    private LabeledComboBox<String> workShift;
    private List<Constraint> firstNameConstraints, lastNameConstraints,
            userNameConstraints, userRoleConstraints;
    private ActionEvent actionEvent = null;

    public UserForm() {
        initializeComponents();
        initializeComponentProperties();
        setupLayout();
    }

    private void initializeComponents() {
        firstNameValidationLabel = createValidationLabel();
        lastNameValidationLabel = createValidationLabel();
        userNameValidationLabel = createValidationLabel();
        emailValidationLabel = createValidationLabel();
        phoneValidationLabel = createValidationLabel();
        departmentValidationLabel = createValidationLabel();
        designationValidationLabel = createValidationLabel();
        employmentStatusValidationLabel = createValidationLabel();
        workShiftValidationLabel = createValidationLabel();
        roleValidationLabel = createValidationLabel();

        firstname = createTextField("First name");
        lastname = createTextField("Last name");
        username = createTextField("Username");
        dateOfBirth = createDatePicker();
        email = createTextField("Email");
        phone = createTextField("Phone");

        department = createFilterComboBox("Department", DepartmentViewModel.getDepartments());
        designation = createFilterComboBox("Designation", DesignationViewModel.getDesignations());
        employmentStatus = createFilterComboBox("Employment Status", EmploymentStatusViewModel.getEmploymentStatuses());
        workShift = createComboBox();
        role = createFilterComboBox("Role", RoleViewModel.getRoles());

        status = new MFXToggleButton();
        status.setText("Active");

        saveBtn = new Button("Save");
        saveBtn.setDefaultButton(true);

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
        GridPane gridPane = createGridPane();
        addGridPaneContent(gridPane);

        HBox buttonBox = new HBox(20, saveBtn, cancelBtn);
        buttonBox.setAlignment(Pos.CENTER_RIGHT);
        buttonBox.setPadding(new Insets(10, 10, 10, 10));

        setCenter(gridPane);
        setBottom(buttonBox);
    }

    private Label createValidationLabel() {
        Label label = new Label();
        label.setManaged(false);
        label.setVisible(false);
        label.setWrapText(true);
        label.getStyleClass().add("input-validation-error");
        return label;
    }

    private LabeledTextField createTextField(String floatingText) {
        LabeledTextField textField = new LabeledTextField();
        textField.setLabel(floatingText);
        textField.setPrefWidth(300);
        return textField;
    }

    private LabeledDatePicker createDatePicker() {
        LabeledDatePicker datePicker = new LabeledDatePicker();
        datePicker.setLabel("Date of Birth");
        datePicker.setPrefWidth(300);
        return datePicker;
    }

    private <T> LabeledComboBox<T> createFilterComboBox(String floatingText, ObservableList<T> items) {
        LabeledComboBox<T> comboBox = new LabeledComboBox<>();
        comboBox.setLabel(floatingText);
        comboBox.setPrefWidth(300);
        comboBox.setItems(items);
        return comboBox;
    }

    private LabeledComboBox<String> createComboBox() {
        LabeledComboBox<String> comboBox = new LabeledComboBox<>();
        comboBox.setLabel("Work Shift");
        comboBox.setPrefWidth(300);
        comboBox.setItems(UserViewModel.getWorkShiftsList());
        return comboBox;
    }

    private GridPane createGridPane() {
        GridPane gridPane = new GridPane();
        gridPane.setHgap(20);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(10, 10, 10, 10));

        ColumnConstraints col1 = new ColumnConstraints();
        col1.setHgrow(Priority.SOMETIMES);
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setHgrow(Priority.SOMETIMES);

        gridPane.getColumnConstraints().addAll(col1, col2);

        for (int i = 0; i < 6; i++) {
            RowConstraints row = new RowConstraints();
            row.setVgrow(Priority.SOMETIMES);
            gridPane.getRowConstraints().add(row);
        }

        return gridPane;
    }

    private void addGridPaneContent(GridPane gridPane) {
        addFormRow(gridPane, 0, firstname, firstNameValidationLabel, lastname, lastNameValidationLabel);
        addFormRow(gridPane, 1, username, userNameValidationLabel, dateOfBirth, null);
        addFormRow(gridPane, 2, email, emailValidationLabel, phone, phoneValidationLabel);
        addFormRow(gridPane, 3, department, departmentValidationLabel, designation, designationValidationLabel);
        addFormRow(gridPane, 4, employmentStatus, employmentStatusValidationLabel, workShift, workShiftValidationLabel);
        addFormRow(gridPane, 5, role, roleValidationLabel, status, null);
    }

    private void addFormRow(GridPane gridPane, int rowIndex, Node leftControl, Label leftLabel, Node rightControl, Label rightLabel) {
        addControlToGrid(gridPane, 0, rowIndex, leftControl, leftLabel);
        addControlToGrid(gridPane, 1, rowIndex, rightControl, rightLabel);
    }

    private void addControlToGrid(GridPane gridPane, int colIndex, int rowIndex, Node control, Label validationLabel) {
        VBox vbox = new VBox(5, control);
        if (validationLabel != null) {
            vbox.getChildren().add(validationLabel);
        }
        vbox.setPadding(new Insets(0, 0, 2.5, 0));
        gridPane.add(vbox, colIndex, rowIndex);
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
        bindTextField(firstname, UserViewModel.firstNameProperty());
        bindTextField(lastname, UserViewModel.lastNameProperty());
        bindTextField(username, UserViewModel.userNameProperty());
        bindTextField(email, UserViewModel.emailProperty());
        bindTextField(phone, UserViewModel.phoneProperty());
        bindFilterComboBox(role, UserViewModel.roleProperty());
        bindToggleButton(status, UserViewModel.activeProperty());
        bindFilterComboBox(department, UserViewModel.departmentProperty());
        bindFilterComboBox(designation, UserViewModel.designationProperty());
        bindFilterComboBox(employmentStatus, UserViewModel.employmentStatusProperty());
        bindComboBox(workShift, UserViewModel.workShiftProperty());
        bindDatePicker(dateOfBirth, UserViewModel.dateOfBirthProperty());
    }

    private void bindTextField(LabeledTextField textField, Property<String> property) {
        textField.textProperty().bindBidirectional(property);
    }

    private <T> void bindFilterComboBox(LabeledComboBox<T> comboBox, Property<T> property) {
        comboBox.valueProperty().bindBidirectional(property);
    }

    private <T> void bindComboBox(LabeledComboBox<T> comboBox, Property<T> property) {
        comboBox.valueProperty().bindBidirectional(property);
    }

    private void bindToggleButton(MFXToggleButton toggleButton, Property<Boolean> property) {
        toggleButton.selectedProperty().bindBidirectional(property);
    }

    private void bindDatePicker(LabeledDatePicker datePicker, Property<LocalDate> property) {
        datePicker.valueProperty().bindBidirectional(property);
    }

    private void setupValidators() {
        requiredValidator(firstname, firstNameValidationLabel, "First name is required");
        requiredValidator(lastname, lastNameValidationLabel, "Last name is required");
        requiredValidator(username, userNameValidationLabel, "Username is required");
        requiredValidator(role, roleValidationLabel, "Role is required");
    }

    private void requiredValidator(LabeledTextField control, Label validationLabel, String message) {
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

    private <T> void requiredValidator(LabeledComboBox<T> control, Label validationLabel, String message) {
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
        cancelBtn.setOnAction(this::onCancel);
    }

    private void onSave(ActionEvent event) {
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
    }

    private void onCancel(ActionEvent event) {
        closeDialog(event);
        UserViewModel.resetProperties();

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

        firstname.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
        lastname.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
        username.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
        username.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
        role.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
        this.dispose();
    }

    private void onSuccess() {
        closeDialog(actionEvent);
        UserViewModel.resetProperties();
        UserViewModel.getAllUsers(null, null);
        this.dispose();
    }

    private void successMessage(String message) {
        displayNotification(message, MessageVariants.SUCCESS, "fas-circle-check");
    }

    private void errorMessage(String message) {
        displayNotification(message, MessageVariants.ERROR, "fas-triangle-exclamation");
    }

    private void displayNotification(String message, MessageVariants type, String icon) {
        var notification = new SpotyMessage.MessageBuilder(message)
                .duration(MessageDuration.SHORT)
                .icon(icon)
                .type(type)
                .height(60)
                .build();
        AnchorPane.setTopAnchor(notification, 5.0);
        AnchorPane.setRightAnchor(notification, 5.0);

        var in = Animations.slideInDown(notification, Duration.millis(250));
        if (!AppManager.getMorphPane().getChildren().contains(notification)) {
            AppManager.getMorphPane().getChildren().add(notification);
            in.playFromStart();
            in.setOnFinished(actionEvent -> SpotyMessage.delay(notification));
        }
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
        workShift.setItems(UserViewModel.getWorkShiftsList());
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

    @Override
    public void dispose() {
        super.dispose();
        saveBtn = null;
        cancelBtn = null;
        email = null;
        phone = null;
        firstname = null;
        lastname = null;
        username = null;
        role = null;
        status = null;
        firstNameValidationLabel = null;
        emailValidationLabel = null;
        phoneValidationLabel = null;
        lastNameValidationLabel = null;
        userNameValidationLabel = null;
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
        userNameConstraints = null;
        userRoleConstraints = null;
        actionEvent = null;
    }
}
