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

import com.dlsc.gemsfx.*;
import static inc.nomard.spoty.core.GlobalActions.*;
import inc.nomard.spoty.core.components.message.*;
import inc.nomard.spoty.core.components.message.enums.*;
import inc.nomard.spoty.core.viewModels.hrm.leave.*;
import inc.nomard.spoty.network_bridge.dtos.hrm.employee.*;
import inc.nomard.spoty.network_bridge.dtos.hrm.leave.*;
import inc.nomard.spoty.utils.*;
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

public class LeaveRequestFormController implements Initializable {
    private static LeaveRequestFormController instance;
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

    public static LeaveRequestFormController getInstance() {
        if (Objects.equals(instance, null)) instance = new LeaveRequestFormController();
        return instance;
    }

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
                            try {
                                LeaveStatusViewModel.updateItem(this::onAction, this::onUpdatedSuccess, this::onFailed);
                                actionEvent = event;
                            } catch (Exception e) {
                                SpotyLogger.writeToFile(e, this.getClass());
                            }
                            return;
                        }

                        try {
                            LeaveStatusViewModel.saveLeaveStatus(this::onAction, this::onAddSuccess, this::onFailed);
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
                new SpotyMessage.MessageBuilder("Leave status added successfully")
                        .duration(MessageDuration.SHORT)
                        .icon("fas-circle-check")
                        .type(MessageVariants.SUCCESS)
                        .build();
        notificationHolder.addMessage(notification);
        cancelBtn.setDisable(false);
        saveBtn.setDisable(false);

        employee.clearSelection();
        leaveType.clearSelection();
        fromDate.setValue(null);
        toDate.setValue(null);

        closeDialog(actionEvent);
        LeaveStatusViewModel.resetProperties();
        LeaveStatusViewModel.getAllLeaveStatuses(null, null, null);
    }

    private void onUpdatedSuccess() {
        SpotyMessageHolder notificationHolder = SpotyMessageHolder.getInstance();
        SpotyMessage notification =
                new SpotyMessage.MessageBuilder("Leave status updated successfully")
                        .duration(MessageDuration.SHORT)
                        .icon("fas-circle-check")
                        .type(MessageVariants.SUCCESS)
                        .build();
        notificationHolder.addMessage(notification);
        cancelBtn.setDisable(false);
        saveBtn.setDisable(false);

        employee.clearSelection();
        leaveType.clearSelection();
        fromDate.setValue(null);
        toDate.setValue(null);

        closeDialog(actionEvent);
        LeaveStatusViewModel.resetProperties();
        LeaveStatusViewModel.getAllLeaveStatuses(null, null, null);
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

        LeaveStatusViewModel.getAllLeaveStatuses(null, null, null);
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
}
