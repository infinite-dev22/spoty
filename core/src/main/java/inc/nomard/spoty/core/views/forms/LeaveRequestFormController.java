package inc.nomard.spoty.core.views.forms;

import atlantafx.base.util.*;
import com.dlsc.gemsfx.*;
import static inc.nomard.spoty.core.GlobalActions.*;
import inc.nomard.spoty.core.viewModels.hrm.leave.*;
import inc.nomard.spoty.core.views.layout.*;
import inc.nomard.spoty.core.views.layout.message.*;
import inc.nomard.spoty.core.views.layout.message.enums.*;
import inc.nomard.spoty.network_bridge.dtos.hrm.employee.*;
import inc.nomard.spoty.network_bridge.dtos.hrm.leave.*;
import io.github.palexdev.materialfx.controls.*;
import io.github.palexdev.materialfx.dialogs.*;
import io.github.palexdev.materialfx.validation.*;
import static io.github.palexdev.materialfx.validation.Validated.*;
import io.github.palexdev.mfxcomponents.controls.buttons.MFXButton;
import io.github.palexdev.mfxresources.fonts.*;
import java.net.*;
import java.util.*;
import javafx.event.*;
import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.stage.*;
import javafx.util.*;
import lombok.extern.java.*;

@Log
public class LeaveRequestFormController implements Initializable {
    @FXML
    public MFXButton saveBtn, cancelBtn;
    @FXML
    public MFXFilterComboBox<User> employee;
    @FXML
    public MFXFilterComboBox<LeaveType> leaveType;
    @FXML
    public MFXDatePicker fromDate, toDate;
    @FXML
    public TimePicker fromTime, toTime;
    @FXML
    public Label employeeValidationLabel,
            leaveTypeValidationLabel,
            fromDateValidationLabel,
            fromTimeValidationLabel,
            toDateValidationLabel,
            toTimeValidationLabel,
            reasonValidationLabel,
            fileLabel;
    @FXML
    public TextArea reason;
    @FXML
    public VBox documentBtn;
    @FXML
    public HBox uploadIcon;
    private FileChooser fileChooser;
    private List<Constraint> employeeConstraints,
            leaveTypeConstraints,
            fromDateConstraints,
            toDateConstraints;
    private ActionEvent actionEvent = null;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Input bindings.
        employee.valueProperty().bindBidirectional(LeaveStatusViewModel.employeeProperty());
        leaveType.valueProperty().bindBidirectional(LeaveStatusViewModel.leaveTypeProperty());
        fromDate.textProperty().bindBidirectional(LeaveStatusViewModel.startDateProperty());
        fromTime.timeProperty().bindBidirectional(LeaveStatusViewModel.startTimeProperty());
        toDate.textProperty().bindBidirectional(LeaveStatusViewModel.endDateProperty());
        toTime.timeProperty().bindBidirectional(LeaveStatusViewModel.endTimeProperty());
        reason.textProperty().bindBidirectional(LeaveStatusViewModel.descriptionProperty());
        // Input listeners.
        requiredValidator();
        dialogOnActions();
        addDocument();
    }

    private void dialogOnActions() {
        cancelBtn.setOnAction(
                (event) -> {
                    LeaveStatusViewModel.resetProperties();
                    closeDialog(event);

                    employeeValidationLabel.setVisible(false);
                    leaveTypeValidationLabel.setVisible(false);
                    fromDateValidationLabel.setVisible(false);
                    fromTimeValidationLabel.setVisible(false);
                    toDateValidationLabel.setVisible(false);
                    toTimeValidationLabel.setVisible(false);
                    reasonValidationLabel.setVisible(false);

                    employeeValidationLabel.setManaged(false);
                    leaveTypeValidationLabel.setManaged(false);
                    fromDateValidationLabel.setManaged(false);
                    fromTimeValidationLabel.setManaged(false);
                    toDateValidationLabel.setManaged(false);
                    toTimeValidationLabel.setManaged(false);
                    reasonValidationLabel.setManaged(false);

                    employee.clearSelection();
                    leaveType.clearSelection();
                    fromDate.setValue(null);
                    toDate.setValue(null);

                    employee.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
                    leaveType.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
                    fromDate.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
                    toDate.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
                });
        saveBtn.setOnAction(
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
                        MFXStageDialog dialog = (MFXStageDialog) employee.getScene().getWindow();
                        dialog.sizeToScene();
                    }
                    if (!leaveTypeConstraints.isEmpty()) {
                        leaveTypeValidationLabel.setManaged(true);
                        leaveTypeValidationLabel.setVisible(true);
                        leaveTypeValidationLabel.setText(leaveTypeConstraints.getFirst().getMessage());
                        leaveType.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, true);
                        MFXStageDialog dialog = (MFXStageDialog) leaveType.getScene().getWindow();
                        dialog.sizeToScene();
                    }
                    if (!fromDateConstraints.isEmpty()) {
                        fromDateValidationLabel.setManaged(true);
                        fromDateValidationLabel.setVisible(true);
                        fromDateValidationLabel.setText(fromDateConstraints.getFirst().getMessage());
                        fromDate.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, true);
                        MFXStageDialog dialog = (MFXStageDialog) fromDate.getScene().getWindow();
                        dialog.sizeToScene();
                    }
                    if (!toDateConstraints.isEmpty()) {
                        toDateValidationLabel.setManaged(true);
                        toDateValidationLabel.setVisible(true);
                        toDateValidationLabel.setText(toDateConstraints.getFirst().getMessage());
                        toDate.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, true);
                        MFXStageDialog dialog = (MFXStageDialog) toDate.getScene().getWindow();
                        dialog.sizeToScene();
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
        employee.clearSelection();
        leaveType.clearSelection();
        fromDate.setValue(null);
        toDate.setValue(null);
        closeDialog(actionEvent);
        LeaveStatusViewModel.resetProperties();
        LeaveStatusViewModel.getAllLeaveStatuses(null, null);
    }

    private void addDocument() {
        var upload = new MFXFontIcon();
        upload.setIconsProvider(IconsProviders.FONTAWESOME_REGULAR);
        upload.setDescription("far-file-pdf");
        upload.setSize(60);
        upload.setColor(Color.web("#C2C2C2"));
        uploadIcon.getChildren().add(upload);

        if (Objects.equals(fileChooser, null)) {
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Document files (*.pdf)", "*.pdf");
            fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(extFilter);
        }
        documentBtn.setOnMouseClicked(event -> {
            var file = fileChooser.showOpenDialog(new Stage());
            if (Objects.nonNull(file)) {
                fileLabel.setText(file.getName());
            }
        });


        documentBtn.setOnDragOver(event -> {
            if (event.getGestureSource() != documentBtn
                    && event.getDragboard().hasFiles()) {
                /* allow for both copying and moving, whatever user chooses */
                event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
            }
            event.consume();
        });

        documentBtn.setOnDragDropped(event -> {
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
                        .setCondition(employee.textProperty().length().greaterThan(0))
                        .get();
        employee.getValidator().constraint(employeeConstraint);
        Constraint leaveTypeConstraint =
                Constraint.Builder.build()
                        .setSeverity(Severity.ERROR)
                        .setMessage("Leave Type is required")
                        .setCondition(leaveType.textProperty().length().greaterThan(0))
                        .get();
        leaveType.getValidator().constraint(leaveTypeConstraint);
        Constraint fromDateConstraint =
                Constraint.Builder.build()
                        .setSeverity(Severity.ERROR)
                        .setMessage("From Date is required")
                        .setCondition(fromDate.textProperty().length().greaterThan(0))
                        .get();
        fromDate.getValidator().constraint(fromDateConstraint);
        Constraint toDateConstraint =
                Constraint.Builder.build()
                        .setSeverity(Severity.ERROR)
                        .setMessage("To Date is required")
                        .setCondition(toDate.textProperty().length().greaterThan(0))
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
        displayNotification(message, MessageVariants.SUCCESS, "fas-circle-check");
    }

    private void errorMessage(String message) {
        displayNotification(message, MessageVariants.ERROR, "fas-triangle-exclamation");
    }

    private void displayNotification(String message, MessageVariants type, String icon) {
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
}
