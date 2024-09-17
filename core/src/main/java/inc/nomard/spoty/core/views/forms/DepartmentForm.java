package inc.nomard.spoty.core.views.forms;

import atlantafx.base.theme.Styles;
import atlantafx.base.util.Animations;
import inc.nomard.spoty.core.viewModels.hrm.employee.DepartmentViewModel;
import inc.nomard.spoty.core.viewModels.hrm.employee.UserViewModel;
import inc.nomard.spoty.core.views.components.CustomButton;
import inc.nomard.spoty.core.views.components.validatables.ValidatableComboBox;
import inc.nomard.spoty.core.views.components.validatables.ValidatableTextArea;
import inc.nomard.spoty.core.views.components.validatables.ValidatableTextField;
import inc.nomard.spoty.core.views.layout.AppManager;
import inc.nomard.spoty.core.views.layout.message.SpotyMessage;
import inc.nomard.spoty.core.views.layout.message.enums.MessageDuration;
import inc.nomard.spoty.core.views.layout.message.enums.MessageVariants;
import inc.nomard.spoty.core.views.util.Validators;
import inc.nomard.spoty.network_bridge.dtos.hrm.employee.Department;
import inc.nomard.spoty.network_bridge.dtos.hrm.employee.Employee;
import io.github.palexdev.materialfx.dialogs.MFXGenericDialog;
import io.github.palexdev.materialfx.dialogs.MFXStageDialog;
import io.github.palexdev.materialfx.utils.others.FunctionalStringConverter;
import io.github.palexdev.materialfx.validation.Constraint;
import io.github.palexdev.materialfx.validation.Severity;
import javafx.collections.ListChangeListener;
import javafx.css.PseudoClass;
import javafx.event.Event;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import javafx.util.StringConverter;
import lombok.extern.java.Log;
import org.kordamp.ikonli.Ikon;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;

import java.util.List;

import static inc.nomard.spoty.core.GlobalActions.closeDialog;

@Log
public class DepartmentForm extends MFXGenericDialog {
    private static final PseudoClass INVALID_PSEUDO_CLASS = PseudoClass.getPseudoClass("invalid");
    public ValidatableTextField name, location;
    public ValidatableComboBox<Employee> manager;
    public ValidatableComboBox<Department> parentDepartment;
    public Label nameValidationLabel;
    public ValidatableTextArea description;
    public CustomButton saveBtn;
    public Button cancelBtn;
    private List<Constraint> constraints;
    private Event actionEvent = null;

    public DepartmentForm() {
        init();
    }

    public void init() {
        buildDialogContent();
        requiredValidator();
        dialogOnActions();
    }


    private VBox buildName() {
        // Input.
        name = new ValidatableTextField();
        var label = new Label("Name");
        name.setPrefWidth(400d);
        name.textProperty().bindBidirectional(DepartmentViewModel.nameProperty());
        // Validation.
        nameValidationLabel = Validators.buildValidationLabel();
        var vbox = new VBox();
        vbox.setSpacing(2d);
        vbox.setPadding(new Insets(2.5d, 0d, 2.5d, 0d));
        vbox.getChildren().addAll(label, name, nameValidationLabel);
        return vbox;
    }

    private VBox buildManager() {
        // Input.
        var label = new Label("Manager");
        manager = new ValidatableComboBox<>();
        manager.setPrefWidth(400d);
        manager.valueProperty().bindBidirectional(DepartmentViewModel.managerProperty());
        setupManagerComboBox();
        var vbox = new VBox();
        vbox.setSpacing(2d);
        vbox.setPadding(new Insets(2.5d, 0d, 2.5d, 0d));
        vbox.getChildren().addAll(label, manager);
        return vbox;
    }

    private VBox buildParentDepartment() {
        // Input.
        var label = new Label("Base Department (Optional)");
        parentDepartment = new ValidatableComboBox<>();
        parentDepartment.setPrefWidth(400d);
        parentDepartment.valueProperty().bindBidirectional(DepartmentViewModel.parentDepartmentProperty());
        setupDepartmentComboBox();
        var vbox = new VBox();
        vbox.setSpacing(2d);
        vbox.setPadding(new Insets(2.5d, 0d, 2.5d, 0d));
        vbox.getChildren().addAll(label, parentDepartment);
        return vbox;
    }

    private VBox buildLocation() {
        // Input.
        location = new ValidatableTextField();
        var label = new Label("Location (Optional)");
        location.setPrefWidth(400d);
        location.textProperty().bindBidirectional(DepartmentViewModel.locationProperty());
        var vbox = new VBox();
        vbox.setSpacing(2d);
        vbox.setPadding(new Insets(2.5d, 0d, 2.5d, 0d));
        vbox.getChildren().addAll(label, location);
        return vbox;
    }

    private VBox buildDescription() {
        // Input.
        description = new ValidatableTextArea();
        var label = new Label("Description (Optional)");
        description.setPrefWidth(400d);
        description.textProperty().bindBidirectional(DepartmentViewModel.descriptionProperty());
        var vbox = new VBox();
        vbox.setSpacing(2d);
        vbox.setPadding(new Insets(2.5d, 0d, 2.5d, 0d));
        vbox.getChildren().addAll(label, description);
        return vbox;
    }

    private VBox buildCenter() {
        var vbox = new VBox();
        vbox.setSpacing(8d);
        vbox.setPadding(new Insets(10d));
        vbox.getChildren().addAll(buildName(), buildManager(), buildParentDepartment(), buildLocation(), buildDescription());
        return vbox;
    }

    private CustomButton buildSaveButton() {
        saveBtn = new CustomButton("Save");
        saveBtn.getStyleClass().add(Styles.ACCENT);
        return saveBtn;
    }

    private Button buildCancelButton() {
        cancelBtn = new Button("Cancel");
        cancelBtn.getStyleClass().add(Styles.BUTTON_OUTLINED);
        return cancelBtn;
    }

    private HBox buildBottom() {
        var hbox = new HBox();
        hbox.setAlignment(Pos.CENTER_RIGHT);
        hbox.setSpacing(20d);
        hbox.getChildren().addAll(buildSaveButton(), buildCancelButton());
        return hbox;
    }

    private void buildDialogContent() {
        this.setCenter(buildCenter());
        this.setBottom(buildBottom());
        this.setShowMinimize(false);
        this.setShowAlwaysOnTop(false);
        this.setShowClose(false);
    }

    private void dialogOnActions() {
        cancelBtn.setOnAction(
                (event) -> {
                    DepartmentViewModel.clearDepartmentData();
                    closeDialog(event);
                    nameValidationLabel.setVisible(false);
                    nameValidationLabel.setManaged(false);
                    name.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
                });
        saveBtn.setOnAction(
                (event) -> {
                    constraints = name.validate();
                    if (!constraints.isEmpty()) {
                        nameValidationLabel.setManaged(true);
                        nameValidationLabel.setVisible(true);
                        nameValidationLabel.setText(constraints.getFirst().getMessage());
                        name.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, true);
                        MFXStageDialog dialog = (MFXStageDialog) name.getScene().getWindow();
                        dialog.sizeToScene();
                    }
                    if (constraints.isEmpty()) {
                        actionEvent = event;
                        if (DepartmentViewModel.getId() > 0) {
                            DepartmentViewModel.updateItem(this::onSuccess, this::successMessage, this::errorMessage);
                            return;
                        }
                        DepartmentViewModel.saveDepartment(this::onSuccess, this::successMessage, this::errorMessage);
                    }
                });
    }

    private void onSuccess() {
        closeDialog(actionEvent);
        DepartmentViewModel.clearDepartmentData();
        DepartmentViewModel.getAllDepartments(null, null, null, null);
    }

    public void requiredValidator() {
        // Name input validation.
        Constraint lengthConstraint =
                Constraint.Builder.build()
                        .setSeverity(Severity.ERROR)
                        .setMessage("Name is required")
                        .setCondition(name.textProperty().length().greaterThan(0))
                        .get();
        name.getValidator().constraint(lengthConstraint);
        // Display error.
        name
                .getValidator()
                .validProperty()
                .addListener(
                        (observable, oldValue, newValue) -> {
                            if (newValue) {
                                nameValidationLabel.setManaged(false);
                                nameValidationLabel.setVisible(false);
                                name.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
                            }
                        });
    }

    private void successMessage(String message) {
        displayNotification(message, MessageVariants.SUCCESS, FontAwesomeSolid.CHECK_CIRCLE);
    }

    private void errorMessage(String message) {
        displayNotification(message, MessageVariants.ERROR, FontAwesomeSolid.EXCLAMATION_TRIANGLE);
    }

    private void displayNotification(String message, MessageVariants type, Ikon icon) {
        SpotyMessage notification = new SpotyMessage.MessageBuilder(message)
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

    private void setupManagerComboBox() {
        StringConverter<Employee> managerConverter = FunctionalStringConverter.to(
                managerDetail -> (managerDetail == null) ? "" : managerDetail.getName());

        manager.setConverter(managerConverter);

        UserViewModel.getEMPLOYEES().addListener((ListChangeListener<Employee>) c ->
                manager.setItems(UserViewModel.getEMPLOYEES())
        );
        if (!UserViewModel.getEMPLOYEES().isEmpty()) {
            manager.itemsProperty().bindBidirectional(UserViewModel.usersProperty());
        }
    }

    private void setupDepartmentComboBox() {
        StringConverter<Department> parentDepartmentConverter = FunctionalStringConverter.to(
                parentDepartmentDetail -> (parentDepartmentDetail == null) ? "" : parentDepartmentDetail.getName());

        parentDepartment.setConverter(parentDepartmentConverter);

        DepartmentViewModel.getDepartments().addListener((ListChangeListener<Department>) c ->
                parentDepartment.setItems(DepartmentViewModel.getDepartments())
        );
        if (!DepartmentViewModel.getDepartments().isEmpty()) {
            parentDepartment.itemsProperty().bindBidirectional(DepartmentViewModel.departmentsProperty());
        }
    }
}
