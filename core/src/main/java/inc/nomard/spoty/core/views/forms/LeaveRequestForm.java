package inc.nomard.spoty.core.views.forms;

import atlantafx.base.theme.Styles;
import atlantafx.base.util.Animations;
import com.dlsc.gemsfx.TimePicker;
import inc.nomard.spoty.core.util.validation.Constraint;
import inc.nomard.spoty.core.util.validation.Severity;
import inc.nomard.spoty.core.viewModels.hrm.employee.EmployeeViewModel;
import inc.nomard.spoty.core.viewModels.hrm.leave.LeaveStatusViewModel;
import inc.nomard.spoty.core.views.components.CustomButton;
import inc.nomard.spoty.core.views.components.validatables.ValidatableComboBox;
import inc.nomard.spoty.core.views.components.validatables.ValidatableDatePicker;
import inc.nomard.spoty.core.views.components.validatables.ValidatableTextArea;
import inc.nomard.spoty.core.views.layout.AppManager;
import inc.nomard.spoty.core.views.layout.message.SpotyMessage;
import inc.nomard.spoty.core.views.layout.message.enums.MessageDuration;
import inc.nomard.spoty.core.views.layout.message.enums.MessageVariants;
import inc.nomard.spoty.core.views.util.FunctionalStringConverter;
import inc.nomard.spoty.network_bridge.dtos.hrm.employee.Employee;
import javafx.collections.ListChangeListener;
import javafx.event.Event;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.util.StringConverter;
import lombok.extern.slf4j.Slf4j;
import org.kordamp.ikonli.Ikon;
import org.kordamp.ikonli.fontawesome5.FontAwesomeRegular;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;
import org.kordamp.ikonli.javafx.FontIcon;

import java.util.List;
import java.util.Objects;

import static inc.nomard.spoty.core.GlobalActions.closeDialog;
import static inc.nomard.spoty.core.util.validation.Validated.INVALID_PSEUDO_CLASS;

@Slf4j
public class LeaveRequestForm extends BorderPane {
    public CustomButton saveButton;
    public Button cancelButton;
    public ValidatableComboBox<Employee> employee;
    public ValidatableComboBox<String> leaveType;
    public ValidatableDatePicker fromDate, toDate;
    public TimePicker fromTime, toTime;
    public Label employeeValidationLabel,
            leaveTypeValidationLabel,
            fromDateValidationLabel,
            fromTimeValidationLabel,
            toDateValidationLabel,
            toTimeValidationLabel,
            reasonValidationLabel,
            fileLabel;
    public ValidatableTextArea reason;
    public VBox documentButton;
    public HBox uploadIcon;
    private FileChooser fileChooser;
    private List<Constraint> employeeConstraints,
            leaveTypeConstraints,
            fromDateConstraints,
            toDateConstraints;
    private Event actionEvent = null;

    public LeaveRequestForm() {
        init();
        initializeComponentProperties();
        requiredValidator();
        dialogOnActions();
        addDocument();
    }

    public void init() {
        VBox root = new VBox(8.0);
        root.setPadding(new Insets(10.0));

        root.getChildren().addAll(
                createValidationBox(employee = new ValidatableComboBox<>(), "Employee", employeeValidationLabel = new Label()),
                createValidationBox(leaveType = new ValidatableComboBox<>(), "Leave Type", leaveTypeValidationLabel = new Label()),
                createDateTimeBox(fromDate = new ValidatableDatePicker(), "From Date", fromDateValidationLabel = new Label(),
                        fromTime = new TimePicker(), "From Time", fromTimeValidationLabel = new Label(), 190d),
                createDateTimeBox(toDate = new ValidatableDatePicker(), "To Date", toDateValidationLabel = new Label(),
                        toTime = new TimePicker(), "To Time", toTimeValidationLabel = new Label(), 190d),
                createValidationTextArea(reason = new ValidatableTextArea(), "Reason", reasonValidationLabel = new Label(), 400d, 100d),
                createDocumentButtonBox()
        );

        this.setBottom(createBottomButtons());
        this.setCenter(root);
    }

    public void initializeComponentProperties() {
        employee.valueProperty().bindBidirectional(LeaveStatusViewModel.employeeProperty());
        leaveType.valueProperty().bindBidirectional(LeaveStatusViewModel.leaveTypeProperty());
        fromDate.valueProperty().bindBidirectional(LeaveStatusViewModel.startDateProperty());
//        fromTime.timeProperty().bindBidirectional(LeaveStatusViewModel.startDateProperty());
        toDate.valueProperty().bindBidirectional(LeaveStatusViewModel.endDateProperty());
//        toTime.timeProperty().bindBidirectional(LeaveStatusViewModel.startDateProperty());
        reason.textProperty().bindBidirectional(LeaveStatusViewModel.descriptionProperty());
        // Combo box properties.

        StringConverter<Employee> employeeConverter = FunctionalStringConverter.to(
                employeeDetail -> employeeDetail == null ? "" : employeeDetail.getName());
        employee.setConverter(employeeConverter);

        if (EmployeeViewModel.getEmployees().isEmpty()) {
            EmployeeViewModel.getEmployees().addListener((ListChangeListener<Employee>) c -> employee.setItems(EmployeeViewModel.getEmployees()));
        } else {
            employee.itemsProperty().bindBidirectional(EmployeeViewModel.usersProperty());
        }
        leaveType.setItems(LeaveStatusViewModel.getLeaveTypeList());
    }

    private <T> VBox createValidationBox(ValidatableComboBox<T> comboBox, String promptText, Label validationLabel) {
        var label = new Label(promptText);
        comboBox.setPrefWidth(400d);

        return createValidationContainer(label, comboBox, validationLabel);
    }

    private VBox createValidationBox(ValidatableDatePicker datePicker, String promptText, Label validationLabel, double width) {
        var label = new Label(promptText);
        datePicker.setPrefWidth(width);

        return createValidationContainer(label, datePicker, validationLabel);
    }

    private VBox createValidationBox(TimePicker timePicker, String promptText, Label validationLabel, double width) {
        timePicker.setPromptText(promptText);
        var label = new Label(promptText);
        timePicker.setPrefWidth(width);

        return createValidationContainer(label, timePicker, validationLabel);
    }

    private VBox createValidationTextArea(ValidatableTextArea textArea, String promptText, Label validationLabel, double width, double height) {
        var label = new Label(promptText);
        textArea.setPrefWidth(width);
        textArea.setPrefHeight(height);

        return createValidationContainer(label, textArea, validationLabel);
    }

    private VBox createValidationContainer(Label label, Node control, Label validationLabel) {
        VBox box = new VBox(2.0);
        box.setPadding(new Insets(2.5, 0, 5, 0));

        validationLabel.setMaxWidth(Double.MAX_VALUE);
        validationLabel.setStyle("-fx-text-fill: red;");
        validationLabel.setVisible(false);
        validationLabel.setManaged(false);
        validationLabel.setWrapText(true);

        box.getChildren().addAll(label, control, validationLabel);
        return box;
    }

    private HBox createDateTimeBox(ValidatableDatePicker datePicker, String datePrompt, Label dateValidationLabel,
                                   TimePicker timePicker, String timePrompt, Label timeValidationLabel, double width) {
        HBox box = new HBox(10.0);
        box.getChildren().addAll(
                createValidationBox(datePicker, datePrompt, dateValidationLabel, width),
                createValidationBox(timePicker, timePrompt, timeValidationLabel, width)
        );
        return box;
    }

    private HBox createDocumentButtonBox() {
        documentButton = new VBox(8.0);
        documentButton.setAlignment(Pos.CENTER);
        documentButton.setPrefWidth(400.0);
        documentButton.getStyleClass().add("card-flat");
        documentButton.setCursor(Cursor.HAND);
        documentButton.setPadding(new Insets(16.0));

        uploadIcon = new HBox();
        uploadIcon.setAlignment(Pos.CENTER);

        fileLabel = new Label("Click to upload or Drag and drop pdf. (max. size 1MB)");
        fileLabel.setTextAlignment(TextAlignment.CENTER);
        fileLabel.setWrapText(true);

        documentButton.getChildren().addAll(uploadIcon, fileLabel);

        HBox box = new HBox(16.0);
        box.setAlignment(Pos.CENTER);
        box.getChildren().add(documentButton);
        return box;
    }

    private HBox createBottomButtons() {
        HBox box = new HBox(20.0);
        box.setAlignment(Pos.CENTER_RIGHT);
        box.setPadding(new Insets(10.0));

        saveButton = new CustomButton("Save");
        saveButton.getStyleClass().add(Styles.ACCENT);

        cancelButton = new Button("Cancel");
        cancelButton.getStyleClass().add(Styles.BUTTON_OUTLINED);

        box.getChildren().addAll(saveButton, cancelButton);
        return box;
    }

    private void dialogOnActions() {
        cancelButton.setOnAction(
                (event) -> {
                    LeaveStatusViewModel.resetProperties();
                    closeDialog(event);
                    this.dispose();
                });
        saveButton.setOnAction(
                (event) -> {
                    employeeConstraints = employee.validate();
                    leaveTypeConstraints = leaveType.validate();
                    fromDateConstraints = fromDate.validate();
                    toDateConstraints = toDate.validate();
                    if (!employeeConstraints.isEmpty()) {
                        employeeValidationLabel.setManaged(true);
                        employeeValidationLabel.setVisible(true);
                        employeeValidationLabel.setText(employeeConstraints.getFirst().getMessage());
                        employee.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, true);
                    }
                    if (!leaveTypeConstraints.isEmpty()) {
                        leaveTypeValidationLabel.setManaged(true);
                        leaveTypeValidationLabel.setVisible(true);
                        leaveTypeValidationLabel.setText(leaveTypeConstraints.getFirst().getMessage());
                        leaveType.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, true);
                    }
                    if (!fromDateConstraints.isEmpty()) {
                        fromDateValidationLabel.setManaged(true);
                        fromDateValidationLabel.setVisible(true);
                        fromDateValidationLabel.setText(fromDateConstraints.getFirst().getMessage());
                        fromDate.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, true);
                    }
                    if (!toDateConstraints.isEmpty()) {
                        toDateValidationLabel.setManaged(true);
                        toDateValidationLabel.setVisible(true);
                        toDateValidationLabel.setText(toDateConstraints.getFirst().getMessage());
                        toDate.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, true);
                    }
                    if (employeeConstraints.isEmpty()
                            && leaveTypeConstraints.isEmpty()
                            && fromDateConstraints.isEmpty()
                            && toDateConstraints.isEmpty()) {
                        if (LeaveStatusViewModel.getId() > 0) {
                            LeaveStatusViewModel.updateItem(this::onSuccess, this::successMessage, this::errorMessage);
                            actionEvent = event;
                            return;
                        }
                        LeaveStatusViewModel.saveLeaveStatus(this::onSuccess, this::successMessage, this::errorMessage);
                        actionEvent = event;
                    }
                });
    }

    private void onSuccess() {
        closeDialog(actionEvent);
        LeaveStatusViewModel.resetProperties();
        LeaveStatusViewModel.getAllLeaveStatuses(null, null, null, null);
        this.dispose();
    }

    private void addDocument() {
        var upload = new FontIcon(FontAwesomeRegular.FILE_PDF);
        upload.setIconSize(60);
        upload.setIconColor(Color.web("#C2C2C2"));
        uploadIcon.getChildren().add(upload);

        if (Objects.equals(fileChooser, null)) {
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Document files (*.pdf)", "*.pdf");
            fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(extFilter);
        }
        documentButton.setOnMouseClicked(event -> {
            var file = fileChooser.showOpenDialog(new Stage());
            if (Objects.nonNull(file)) {
                fileLabel.setText(file.getName());
            }
        });


        documentButton.setOnDragOver(event -> {
            if (event.getGestureSource() != documentButton
                    && event.getDragboard().hasFiles()) {
                /* allow for both copying and moving, whatever user chooses */
                event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
            }
            event.consume();
        });

        documentButton.setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            boolean success = false;
            if (db.hasFiles()) {
                fileLabel.setText(db.getFiles().getFirst().getName());
                System.out.println("Dropped: " + db.getString() + " " + db.getFiles().toString());
                success = true;
            }
            /* let the source know whether the string was successfully
             * transferred and used */
            event.setDropCompleted(success);
            event.consume();
        });
    }

    public void requiredValidator() {
        // Name input validation.
        Constraint employeeConstraint =
                Constraint.Builder.build()
                        .setSeverity(Severity.ERROR)
                        .setMessage("Employee is required")
                        .setCondition(employee.valueProperty().isNotNull())
                        .get();
        employee.getValidator().constraint(employeeConstraint);
        Constraint leaveTypeConstraint =
                Constraint.Builder.build()
                        .setSeverity(Severity.ERROR)
                        .setMessage("Leave Type is required")
                        .setCondition(leaveType.valueProperty().isNotNull())
                        .get();
        leaveType.getValidator().constraint(leaveTypeConstraint);
        Constraint fromDateConstraint =
                Constraint.Builder.build()
                        .setSeverity(Severity.ERROR)
                        .setMessage("From LocalDate is required")
                        .setCondition(fromDate.valueProperty().isNotNull())
                        .get();
        fromDate.getValidator().constraint(fromDateConstraint);
        Constraint toDateConstraint =
                Constraint.Builder.build()
                        .setSeverity(Severity.ERROR)
                        .setMessage("To LocalDate is required")
                        .setCondition(toDate.valueProperty().isNotNull())
                        .get();
        toDate.getValidator().constraint(toDateConstraint);
        // Display error.
        employee
                .getValidator()
                .validProperty()
                .addListener(
                        (observable, oldValue, newValue) -> {
                            if (newValue) {
                                employeeValidationLabel.setManaged(false);
                                employeeValidationLabel.setVisible(false);
                                employee.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
                            }
                        });
        leaveType
                .getValidator()
                .validProperty()
                .addListener(
                        (observable, oldValue, newValue) -> {
                            if (newValue) {
                                leaveTypeValidationLabel.setManaged(false);
                                leaveTypeValidationLabel.setVisible(false);
                                leaveType.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
                            }
                        });
        fromDate
                .getValidator()
                .validProperty()
                .addListener(
                        (observable, oldValue, newValue) -> {
                            if (newValue) {
                                fromDateValidationLabel.setManaged(false);
                                fromDateValidationLabel.setVisible(false);
                                fromDate.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
                            }
                        });
        toDate
                .getValidator()
                .validProperty()
                .addListener(
                        (observable, oldValue, newValue) -> {
                            if (newValue) {
                                toDateValidationLabel.setManaged(false);
                                toDateValidationLabel.setVisible(false);
                                toDate.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
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

    public void dispose() {
        saveButton = null;
        cancelButton = null;
        employee = null;
        leaveType = null;
        fromDate = null;
        toDate = null;
        fromTime = null;
        toTime = null;
        employeeValidationLabel = null;
        leaveTypeValidationLabel = null;
        fromDateValidationLabel = null;
        fromTimeValidationLabel = null;
        toDateValidationLabel = null;
        toTimeValidationLabel = null;
        reasonValidationLabel = null;
        fileLabel = null;
        reason = null;
        documentButton = null;
        uploadIcon = null;
        fileChooser = null;
        employeeConstraints = null;
        leaveTypeConstraints = null;
        fromDateConstraints = null;
        toDateConstraints = null;
        actionEvent = null;
    }
}
