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

import inc.nomard.spoty.core.components.message.*;
import inc.nomard.spoty.core.components.message.enums.MessageDuration;
import inc.nomard.spoty.core.components.message.enums.MessageVariants;
import inc.nomard.spoty.core.viewModels.hrm.leave.LeaveStatusViewModel;
import inc.nomard.spoty.network_bridge.dtos.hrm.employee.User;
import inc.nomard.spoty.network_bridge.dtos.hrm.leave.LeaveType;
import inc.nomard.spoty.utils.SpotyLogger;
import io.github.palexdev.materialfx.controls.MFXDatePicker;
import io.github.palexdev.materialfx.controls.MFXFilterComboBox;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.mfxcomponents.controls.buttons.MFXButton;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

import static inc.nomard.spoty.core.GlobalActions.closeDialog;
import static inc.nomard.spoty.core.Validators.requiredValidator;

public class LeaveStatusFormController implements Initializable {
    private static LeaveStatusFormController instance;
    @FXML
    public MFXButton saveBtn;
    @FXML
    public MFXButton cancelBtn;
    @FXML
    public MFXFilterComboBox<User> employee;
    @FXML
    public Label employeeValidationLabel;
    @FXML
    public MFXFilterComboBox<LeaveType> leaveType;
    @FXML
    public Label leaveTypeValidationLabel;
    @FXML
    public MFXDatePicker fromDate;
    @FXML
    public Label fromDateValidationLabel;
    @FXML
    public MFXDatePicker fromTime;
    @FXML
    public Label fromTimeValidationLabel;
    @FXML
    public MFXDatePicker toDate;
    @FXML
    public Label toDateValidationLabel;
    @FXML
    public MFXDatePicker toTime;
    @FXML
    public Label toTimeValidationLabel;
    @FXML
    public MFXTextField reason;
    @FXML
    public Label reasonValidationLabel;
    @FXML
    public VBox documentBtn;
    @FXML
    public HBox uploadIcon;
    @FXML
    public ImageView document;

    public static LeaveStatusFormController getInstance() {
        if (Objects.equals(instance, null)) instance = new LeaveStatusFormController();
        return instance;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Input bindings.
        employee.valueProperty().bindBidirectional(LeaveStatusViewModel.employeeProperty());
        leaveType.valueProperty().bindBidirectional(LeaveStatusViewModel.leaveTypeProperty());
        fromDate.textProperty().bindBidirectional(LeaveStatusViewModel.startDateProperty());
        fromTime.textProperty().bindBidirectional(LeaveStatusViewModel.descriptionProperty());
        toDate.textProperty().bindBidirectional(LeaveStatusViewModel.endDateProperty());
        toTime.textProperty().bindBidirectional(LeaveStatusViewModel.descriptionProperty());
        reason.textProperty().bindBidirectional(LeaveStatusViewModel.descriptionProperty());
        // Input listeners.
        requiredValidator(
                employee, "Name is required.", employeeValidationLabel, saveBtn);
        // Input listeners.
        requiredValidator(
                leaveType, "Name is required.", leaveTypeValidationLabel, saveBtn);
        // Input listeners.
        requiredValidator(
                reason, "Name is required.", reasonValidationLabel, saveBtn);
        dialogOnActions();
    }

    private void dialogOnActions() {
        cancelBtn.setOnAction(
                (event) -> {
                    LeaveStatusViewModel.resetProperties();

                    closeDialog(event);

                    employeeValidationLabel.setVisible(false);
                    leaveTypeValidationLabel.setVisible(false);
                    reasonValidationLabel.setVisible(false);
                });
        saveBtn.setOnAction(
                (event) -> {
                    if (!employeeValidationLabel.isVisible()
                            && !leaveTypeValidationLabel.isVisible()
                            && !reasonValidationLabel.isVisible()) {
                        if (LeaveStatusViewModel.getId() > 0) {
                            try {
                                LeaveStatusViewModel.updateItem(this::onAction, this::onUpdatedSuccess, this::onFailed);
                                closeDialog(event);
                            } catch (Exception e) {
                                SpotyLogger.writeToFile(e, this.getClass());
                            }
                            return;
                        }

                        try {
                            LeaveStatusViewModel.saveLeaveStatus(this::onAction, this::onAddSuccess, this::onFailed);
                            closeDialog(event);
                        } catch (Exception e) {
                            SpotyLogger.writeToFile(e, this.getClass());
                        }
                        return;
                    }
                    onRequiredFieldsMissing();
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

    private void onRequiredFieldsMissing() {
        SpotyMessageHolder notificationHolder = SpotyMessageHolder.getInstance();
        SpotyMessage notification =
                new SpotyMessage.MessageBuilder("Required fields can't be null")
                        .duration(MessageDuration.SHORT)
                        .icon("fas-triangle-exclamation")
                        .type(MessageVariants.ERROR)
                        .build();
        notificationHolder.addMessage(notification);
        cancelBtn.setDisable(false);
        saveBtn.setDisable(false);

        LeaveStatusViewModel.getAllLeaveStatuses(null, null, null);
    }
}
