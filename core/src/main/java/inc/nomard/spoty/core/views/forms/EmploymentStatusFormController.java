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

import atlantafx.base.theme.Styles;
import inc.nomard.spoty.core.components.message.*;
import inc.nomard.spoty.core.components.message.enums.MessageDuration;
import inc.nomard.spoty.core.components.message.enums.MessageVariants;
import inc.nomard.spoty.core.viewModels.hrm.employee.EmploymentStatusViewModel;
import inc.nomard.spoty.utils.SpotyLogger;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.mfxcomponents.controls.buttons.MFXButton;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

import static inc.nomard.spoty.core.GlobalActions.closeDialog;
import static inc.nomard.spoty.core.Validators.requiredValidator;

public class EmploymentStatusFormController implements Initializable {
    private static EmploymentStatusFormController instance;
    @FXML
    public MFXTextField name;
    @FXML
    public Label nameValidationLabel;
    @FXML
    public ColorPicker colorPicker;
    @FXML
    public MFXTextField description;
    @FXML
    public Label descriptionValidationLabel;
    @FXML
    public MFXButton saveBtn;
    @FXML
    public MFXButton cancelBtn;

    public static EmploymentStatusFormController getInstance() {
        if (Objects.equals(instance, null)) instance = new EmploymentStatusFormController();
        return instance;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Input bindings.
        name.textProperty().bindBidirectional(EmploymentStatusViewModel.nameProperty());
        description.textProperty().bindBidirectional(EmploymentStatusViewModel.descriptionProperty());
        // Input listeners.
        requiredValidator(
                name, "Name is required.", nameValidationLabel, saveBtn);
        dialogOnActions();
        // Color Picker
        colorPicker.getStyleClass().add(ColorPicker.STYLE_CLASS_BUTTON);
        colorPicker.getStyleClass().add(Styles.BUTTON_OUTLINED);
    }

    private void dialogOnActions() {
        cancelBtn.setOnAction(
                (event) -> {
                    EmploymentStatusViewModel.clearEmploymentStatusData();

                    closeDialog(event);

                    nameValidationLabel.setVisible(false);
                    descriptionValidationLabel.setVisible(false);
                });
        saveBtn.setOnAction(
                (event) -> {
                    if (!nameValidationLabel.isVisible()
                            && !descriptionValidationLabel.isVisible()) {
                        if (EmploymentStatusViewModel.getId() > 0) {
                                try {
                                    EmploymentStatusViewModel.updateItem(this::onAction, this::onUpdatedSuccess, this::onFailed);
                                    closeDialog(event);
                                } catch (Exception e) {
                                    SpotyLogger.writeToFile(e, this.getClass());
                                }
                            return;
                        }

                        try {
                            EmploymentStatusViewModel.saveEmploymentStatus(this::onAction, this::onAddSuccess, this::onFailed);
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
                new SpotyMessage.MessageBuilder("Employment status added successfully")
                        .duration(MessageDuration.SHORT)
                        .icon("fas-circle-check")
                        .type(MessageVariants.SUCCESS)
                        .build();
        notificationHolder.addMessage(notification);
        cancelBtn.setDisable(false);
        saveBtn.setDisable(false);

        EmploymentStatusViewModel.getAllEmploymentStatuses(null, null, null);
    }

    private void onUpdatedSuccess() {
        SpotyMessageHolder notificationHolder = SpotyMessageHolder.getInstance();
        SpotyMessage notification =
                new SpotyMessage.MessageBuilder("Employment status updated successfully")
                        .duration(MessageDuration.SHORT)
                        .icon("fas-circle-check")
                        .type(MessageVariants.SUCCESS)
                        .build();
        notificationHolder.addMessage(notification);
        cancelBtn.setDisable(false);
        saveBtn.setDisable(false);

        EmploymentStatusViewModel.getAllEmploymentStatuses(null, null, null);
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

        EmploymentStatusViewModel.getAllEmploymentStatuses(null, null, null);
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

        EmploymentStatusViewModel.getAllEmploymentStatuses(null, null, null);
    }
}
