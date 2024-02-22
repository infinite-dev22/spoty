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

package inc.normad.spoty.core.views.forms;

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
import inc.normad.spoty.core.components.notification.SimpleNotification;
import inc.normad.spoty.core.components.notification.SimpleNotificationHolder;
import inc.normad.spoty.core.components.notification.enums.NotificationDuration;
import inc.normad.spoty.core.components.notification.enums.NotificationVariants;
import inc.normad.spoty.network_bridge.dtos.hrm.employee.User;
import inc.normad.spoty.network_bridge.dtos.hrm.leave.LeaveType;
import inc.normad.spoty.utils.SpotyLogger;
import inc.normad.spoty.utils.SpotyThreader;
import inc.normad.spoty.core.viewModels.hrm.leave.LeaveStatusViewModel;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

import static inc.normad.spoty.core.GlobalActions.closeDialog;
import static inc.normad.spoty.core.Validators.requiredValidator;

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
                    SimpleNotificationHolder notificationHolder = SimpleNotificationHolder.getInstance();

                    if (!employeeValidationLabel.isVisible()
                            && !leaveTypeValidationLabel.isVisible()
                            && !reasonValidationLabel.isVisible()) {
                        if (LeaveStatusViewModel.getId() > 0) {
                            SpotyThreader.spotyThreadPool(() -> {
                                try {
                                    LeaveStatusViewModel.updateItem(this::onAction, this::onSuccess, this::onFailed);
                                } catch (Exception e) {
                                    SpotyLogger.writeToFile(e, this.getClass());
                                }
                            });

                            SimpleNotification notification =
                                    new SimpleNotification.NotificationBuilder("Leave status updated successfully")
                                            .duration(NotificationDuration.SHORT)
                                            .icon("fas-circle-check")
                                            .type(NotificationVariants.SUCCESS)
                                            .build();
                            notificationHolder.addNotification(notification);

                            closeDialog(event);
                            return;
                        }

                        try {
                            LeaveStatusViewModel.saveLeaveStatus(this::onAction, this::onSuccess, this::onFailed);
                        } catch (Exception e) {
                            SpotyLogger.writeToFile(e, this.getClass());
                        }

                        SimpleNotification notification =
                                new SimpleNotification.NotificationBuilder("Leave status saved successfully")
                                        .duration(NotificationDuration.SHORT)
                                        .icon("fas-circle-check")
                                        .type(NotificationVariants.SUCCESS)
                                        .build();
                        notificationHolder.addNotification(notification);

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

    private void onAction() {
        System.out.println("Loading employment status...");
    }

    private void onSuccess() {
        System.out.println("Loaded employment status...");
    }

    private void onFailed() {
        System.out.println("failed loading employment status...");
    }
}
