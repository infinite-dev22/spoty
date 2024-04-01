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
import inc.nomard.spoty.core.viewModels.BranchViewModel;
import inc.nomard.spoty.utils.SpotyLogger;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.mfxcomponents.controls.buttons.MFXButton;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

import static inc.nomard.spoty.core.GlobalActions.closeDialog;
import static inc.nomard.spoty.core.Validators.requiredValidator;
import static inc.nomard.spoty.core.viewModels.BranchViewModel.clearBranchData;
import static inc.nomard.spoty.core.viewModels.BranchViewModel.saveBranch;

public class BranchFormController implements Initializable {
    private static BranchFormController instance;
    @FXML
    public MFXButton saveBtn,
            cancelBtn;
    @FXML
    public MFXTextField branchFormName,
            branchFormEmail,
            branchFormPhone,
            branchFormTown,
            branchFormCity,
            branchFormZipCode;
    @FXML
    public Label branchFormEmailValidationLabel,
            branchFormCityValidationLabel,
            branchFormTownValidationLabel,
            branchFormPhoneValidationLabel,
            branchFormNameValidationLabel;

    public static BranchFormController getInstance() {
        if (Objects.equals(instance, null)) instance = new BranchFormController();
        return instance;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Input bindings.
        branchFormName.textProperty().bindBidirectional(BranchViewModel.nameProperty());
        branchFormEmail.textProperty().bindBidirectional(BranchViewModel.emailProperty());
        branchFormPhone.textProperty().bindBidirectional(BranchViewModel.phoneProperty());
        branchFormTown.textProperty().bindBidirectional(BranchViewModel.townProperty());
        branchFormCity.textProperty().bindBidirectional(BranchViewModel.cityProperty());
        // Input listeners.
        requiredValidator(
                branchFormName, "Name is required.", branchFormNameValidationLabel, saveBtn);
        requiredValidator(
                branchFormEmail, "Email is required.", branchFormEmailValidationLabel, saveBtn);
        requiredValidator(
                branchFormPhone, "Phone is required.", branchFormPhoneValidationLabel, saveBtn);
        requiredValidator(
                branchFormTown, "Town is required.", branchFormTownValidationLabel, saveBtn);
        requiredValidator(
                branchFormCity, "City is required", branchFormCityValidationLabel, saveBtn);
        dialogOnActions();
    }

    private void dialogOnActions() {
        cancelBtn.setOnAction(
                (event) -> {
                    clearBranchData();

                    closeDialog(event);

                    branchFormNameValidationLabel.setVisible(false);
                    branchFormEmailValidationLabel.setVisible(false);
                    branchFormPhoneValidationLabel.setVisible(false);
                    branchFormTownValidationLabel.setVisible(false);
                    branchFormCityValidationLabel.setVisible(false);
                });
        saveBtn.setOnAction(
                (event) -> {
                    if (!branchFormNameValidationLabel.isVisible()
                            && !branchFormEmailValidationLabel.isVisible()
                            && !branchFormPhoneValidationLabel.isVisible()
                            && !branchFormTownValidationLabel.isVisible()
                            && !branchFormCityValidationLabel.isVisible()) {
                        if (BranchViewModel.getId() > 0) {
                            try {
                                BranchViewModel.updateItem(this::onAction, this::onUpdatedSuccess, this::onFailed);
                                closeDialog(event);
                            } catch (Exception e) {
                                SpotyLogger.writeToFile(e, this.getClass());
                            }
                            return;
                        }

                        try {
                            saveBranch(this::onAction, this::onAddSuccess, this::onFailed);
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
                new SpotyMessage.MessageBuilder("Branch added successfully")
                        .duration(MessageDuration.SHORT)
                        .icon("fas-circle-check")
                        .type(MessageVariants.SUCCESS)
                        .build();
        notificationHolder.addMessage(notification);
        cancelBtn.setDisable(false);
        saveBtn.setDisable(false);

        BranchViewModel.getAllBranches(null, null, null);
    }

    private void onUpdatedSuccess() {
        SpotyMessageHolder notificationHolder = SpotyMessageHolder.getInstance();
        SpotyMessage notification =
                new SpotyMessage.MessageBuilder("Branch updated successfully")
                        .duration(MessageDuration.SHORT)
                        .icon("fas-circle-check")
                        .type(MessageVariants.SUCCESS)
                        .build();
        notificationHolder.addMessage(notification);
        cancelBtn.setDisable(false);
        saveBtn.setDisable(false);

        BranchViewModel.getAllBranches(null, null, null);
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

        BranchViewModel.getAllBranches(null, null, null);
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

        BranchViewModel.getAllBranches(null, null, null);
    }
}
